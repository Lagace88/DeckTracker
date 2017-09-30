package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import static android.graphics.Color.parseColor;

public class UserDeckDisplay extends Activity {
    DBAdapter db;
    ListView decks;
    ListViewCustomAdapterUserDecks adapter;
    int pastRecommended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deck_display);

        // Declare Buttons
        Button AddDeck = findViewById(R.id.btnAddDeck);
        Button Back = findViewById(R.id.btnBack);

        // Listener Events for Buttons
        AddDeck.setOnClickListener(toggleAddDeck);
        Back.setOnClickListener(toggleBack);

        // Populate Deck List
        decks = findViewById(R.id.decks);
        db = new DBAdapter(this);
        populateListView();

        // Set up for item selection.
        decks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create table if it does not exist.
                String selected = "FACEDDECKS";
                db.open();
                db.createTable(selected, 1);

                // Load deck name from "USERDECKS" and pass.
                Cursor c = db.getRow(position + 1, "USERDECKS", 0);
                db.close();

                // Start Main Display
                Intent intent = new Intent(UserDeckDisplay.this, MainDisplay.class);
                intent.putExtra("USERDECKNAME", c.getString(c.getColumnIndexOrThrow("name")));
                intent.putExtra("Position", position + 1);
                startActivity(intent);
            }
        });
    }

   @Override
    protected void onResume() {
        super.onResume();
       populateListView();
    }


    private void populateListView() {
        SharedPreferences sP = getApplicationContext().getSharedPreferences("DTPPref", 0);
        pastRecommended = sP.getInt("PastRecommended", 100);

        db.open();

        // Populate list
        Cursor cursor = db.getAllRows("USERDECKS", 0);
        adapter = new ListViewCustomAdapterUserDecks(this, cursor, this.getResources().getConfiguration());
        decks.setAdapter(adapter);

        TextView recommendedText = findViewById(R.id.txt_RecomendedDeck);

        // Create Recommended table if it does not exist and see if any decks have been played.
        db.createTable("RECOMMENDED", 3);
        cursor = db.getAllRows("RECOMMENDED", 3);

        // Skip if there has not been any decks faced.
        if (cursor.getCount() != 0) {
            cursor = db.getAllRows("FACEDDECKS", 1);

            // Find which deck has been faced the most.
            int timesFaced[] = new int[cursor.getCount()];

            // Count
            cursor = db.getAllRows("RECOMMENDED", 3);
            cursor.moveToLast();

            if (pastRecommended <= cursor.getCount()) {
                for (int x = cursor.getCount(); x > (cursor.getCount() - pastRecommended); x--) {
                    if (cursor.getInt(cursor.getColumnIndexOrThrow("deck")) != 0) {
                        timesFaced[cursor.getInt(cursor.getColumnIndexOrThrow("deck")) - 1]++;
                        //Toast.makeText(getApplicationContext(), Integer.toString(timesFaced[cursor.getInt(cursor.getColumnIndexOrThrow("deck")) - 1]), Toast.LENGTH_SHORT).show();
                    }
                    cursor.moveToPrevious();
                }

            } else {
                for (int x = 1; x <= cursor.getCount(); x++) {
                    if (cursor.getInt(cursor.getColumnIndexOrThrow("deck")) != 0) {
                        timesFaced[cursor.getInt(cursor.getColumnIndexOrThrow("deck")) - 1]++;
                    }
                    cursor.moveToPrevious();
                }
            }
            cursor.close();

            // Find the most played.
            int times = timesFaced[0];
            int deck = 0;

            for (int x = 1; x < timesFaced.length; x++) {
                if (timesFaced[x] > times) {
                    times = timesFaced[x];
                    deck = x;
                }
            }

            // Get how many win/loss databases there are.
            Cursor userDeckCursor = db.getAllRows("USERDECKS", 0); // Used to iterate through win/loss tables.
            Cursor cursorWinLoss; // Used to find win rate.
            cursorWinLoss = db.getAllRows("[" + userDeckCursor.getString(userDeckCursor.getColumnIndexOrThrow("name")) + "]", 2);
            cursorWinLoss.moveToPosition(deck);

            int wins = cursorWinLoss.getInt(cursorWinLoss.getColumnIndexOrThrow("wins"));
            int losses = cursorWinLoss.getInt(cursorWinLoss.getColumnIndexOrThrow("losses"));
            double winRate;

            if (wins + losses != 0) {
                winRate = ((double) wins / (wins + losses)) * 100;
            } else
                winRate = 0;

            int recommendedDeck = 0;

            // Find which has the best win ratio against the deck.
            for (int x = 1; x < userDeckCursor.getCount(); x++) {
                // Collect information from the current win/loss table.
                userDeckCursor.moveToNext();
                cursorWinLoss = db.getAllRows("[" + userDeckCursor.getString(userDeckCursor.getColumnIndexOrThrow("name")) + "]", 2);
                cursorWinLoss.moveToPosition(deck);
                wins = cursorWinLoss.getInt(cursorWinLoss.getColumnIndexOrThrow("wins"));
                losses = cursorWinLoss.getInt(cursorWinLoss.getColumnIndexOrThrow("losses"));

                // Compare it to the current holder of recommended.
                if (((double) wins / (wins + losses)) * 100 > winRate) {
                    winRate = ((double) wins / (wins + losses)) * 100;
                    recommendedDeck = x;
                }
            }
            cursorWinLoss.close();

            // Populate the display.
            userDeckCursor.moveToPosition(recommendedDeck);
            String Name = userDeckCursor.getString(userDeckCursor.getColumnIndexOrThrow("name"));
            recommendedText.setText(Name);

            int Tred = userDeckCursor.getInt(userDeckCursor.getColumnIndexOrThrow("tred"));
            int Tgreen = userDeckCursor.getInt(userDeckCursor.getColumnIndexOrThrow("tgreen"));
            int Tblue = userDeckCursor.getInt(userDeckCursor.getColumnIndexOrThrow("tblue"));

            int Bred = userDeckCursor.getInt(userDeckCursor.getColumnIndexOrThrow("bred"));
            int Bgreen = userDeckCursor.getInt(userDeckCursor.getColumnIndexOrThrow("bgreen"));
            int Bblue = userDeckCursor.getInt(userDeckCursor.getColumnIndexOrThrow("bblue"));

            recommendedText.setTextColor(Color.rgb(Tred, Tgreen, Tblue));
            recommendedText.setBackgroundColor(Color.rgb(Bred, Bgreen, Bblue));
            userDeckCursor.close();
        }

        db.close();
    }

    private final View.OnClickListener toggleAddDeck =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent x = new Intent(UserDeckDisplay.this, AddDeck.class);
                    x.putExtra("USERDECK", true);
                    startActivity(x);
                }
            };

    private final View.OnClickListener toggleBack =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };
}

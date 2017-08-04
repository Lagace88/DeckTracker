package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserDeckDisplay extends Activity {
    DBAdapter db;
    ListView decks;
    ListViewCustomAdapterUserDecks adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_deck_display);

        // Declare Buttons
        Button AddDeck = (Button) findViewById(R.id.btnAddDeck);
        Button Back = (Button) findViewById(R.id.btnBack);

        // Listener Events for Buttons
        AddDeck.setOnClickListener(toggleAddDeck);
        Back.setOnClickListener(toggleBack);

        // Populate Deck List
        decks = (ListView) findViewById(R.id.decks);
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
        db.open();
        // Populate list
        Cursor cursor = db.getAllRows("USERDECKS", 0);
        adapter = new ListViewCustomAdapterUserDecks(this, cursor);
        decks.setAdapter(adapter);

        // Populate recommended deck
        TextView recommendedDeck = (TextView) findViewById(R.id.txt_RecomendedDeck);
        cursor = db.getAllRows("FACEDDECKS", 1);

        // Find which deck has been faced the most.
        int timesFaced[] = new int[cursor.getCount()];

        // Set up for count.
        for (int x = 0; x < cursor.getCount(); x++) {
            timesFaced[x] = 0;
        }

        // Count
        cursor = db.getAllRows("RECOMMENDED", 3);
        for (int x = 0; x < cursor.getCount(); x++) {
            if (cursor.getInt(cursor.getColumnIndexOrThrow("deck")) != 0){
                timesFaced[cursor.getInt(cursor.getColumnIndexOrThrow("deck")) - 1]++;
            }
        }

        // Find the most played.
        // If there have been decks that have been faced.
        if (timesFaced.length != 0) {
            int times = timesFaced[0];
            int deck = 0;

            for (int x = 1; x < timesFaced.length; x++) {
                if (timesFaced[x] > times) {
                    times = timesFaced[x];
                    deck = x;
                }
            }
            cursor.close();

            // Get how many win/loss databases there are.
            Cursor userdeckCursor;
            userdeckCursor = db.getAllRows("USERDECKS", 0);
            cursor.moveToFirst();
            Cursor cursorRecommended = db.getAllRows("[" + userdeckCursor.getString(userdeckCursor.getColumnIndexOrThrow("name")) + "]", 2);
            /*cursorRecommended.moveToPosition(deck);
            int recommended = 0;
            int wins = cursorRecommended.getInt(cursorRecommended.getColumnIndexOrThrow("wins"));

            // Find which has the most wins against the deck.
            for (int x = 1; x < cursor.getCount(); x++) {
                cursor.moveToNext();
                cursorRecommended = db.getAllRows(cursor.getString(cursor.getColumnIndexOrThrow("name")), 2);
                cursorRecommended.moveToPosition(deck);
                if (cursorRecommended.getInt(cursorRecommended.getColumnIndexOrThrow("wins")) > wins) {
                    wins = cursorRecommended.getInt(cursorRecommended.getColumnIndexOrThrow("wins"));
                    recommended = x;
                }
            }
            cursorRecommended.close();

            // Populate the display.
            cursor.moveToPosition(recommended);

            recommendedDeck.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            recommendedDeck.setTextColor(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow("textcolor"))));
            recommendedDeck.setBackgroundColor(Color.parseColor(cursor.getString(cursor.getColumnIndexOrThrow("bgcolor"))));

            db.close();*/
        }
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

package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.MergeCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainDisplay extends Activity {
    // Globals
    DBAdapter db;
    String UserDeckName;
    int UserPosition;
    ListView FacedDecks;
    ListViewCustomAdapterFacedDecks adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);

        // Declare Buttons
        Button AddDeck = (Button) findViewById(R.id.btnAddDeck);
        Button Back = (Button) findViewById(R.id.btnBack);

        // Listener Events for Buttons
        AddDeck.setOnClickListener(toggleAddDeck);
        Back.setOnClickListener(toggleBack);

        FacedDecks = (ListView) findViewById(R.id.facedDecks);

        // Load UserDeckName and UserPosition
        UserDeckName = getIntent().getStringExtra("USERDECKNAME");
        UserPosition = getIntent().getIntExtra("Position", 0);

        db = new DBAdapter(this);

        TextView Current = (TextView) findViewById(R.id.txtCurrentDeck);
        TextView CurrentWinRate = (TextView) findViewById(R.id.txt_CurrentDeckWinRate);

        db.open();
        Cursor c = db.getRow(UserPosition, "USERDECKS", 0);
        db.close();

        String Name = c.getString(c.getColumnIndexOrThrow("name"));
        Current.setText(Name);
        Current.setTextColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("textcolor"))));
        Current.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("bgcolor"))));
        if (Name.length() <= 10) {
            Current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }else if (Name.length() <= 15) {
            Current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }else if (Name.length() <= 28) {
            Current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }

        CurrentWinRate.setText(c.getInt(c.getColumnIndexOrThrow("winrate")) + "%");
        CurrentWinRate.setTextColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("textcolor"))));
        CurrentWinRate.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("bgcolor"))));

        populateListView();

        // Set up for item selection.
        FacedDecks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get Deck Name

                // Start WinLoss
                Intent intent = new Intent(MainDisplay.this, WinLoss.class);
                intent.putExtra("USERDECKNAME", UserDeckName);
                intent.putExtra("Position", position + 1);
                intent.putExtra("UserPosition", UserPosition);
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
        // Use query to combine to tables.
        Cursor cursor = db.combine("[" + UserDeckName + "]");

        adapter = new ListViewCustomAdapterFacedDecks(this, cursor);
        FacedDecks.setAdapter(adapter);

        TextView Current = (TextView) findViewById(R.id.txtCurrentDeck);
        TextView CurrentWinRate = (TextView) findViewById(R.id.txt_CurrentDeckWinRate);

        db.open();
        Cursor c = db.getRow(UserPosition, "USERDECKS", 0);
        db.close();

        String Name = c.getString(c.getColumnIndexOrThrow("name"));
        Current.setText(Name);
        Current.setTextColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("textcolor"))));
        Current.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("bgcolor"))));
        if (Name.length() <= 10) {
            Current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }else if (Name.length() <= 15) {
            Current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }else if (Name.length() <= 28) {
            Current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }

        CurrentWinRate.setText(c.getInt(c.getColumnIndexOrThrow("winrate")) + "%");
        CurrentWinRate.setTextColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("textcolor"))));
        CurrentWinRate.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("bgcolor"))));
        db.close();
    }

    private final View.OnClickListener toggleAddDeck =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainDisplay.this, AddDeck.class);
                    intent.putExtra("USERDECK", false);
                    startActivity(intent);
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

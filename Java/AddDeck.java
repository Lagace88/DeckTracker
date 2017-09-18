package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddDeck extends Activity {
    boolean UserDeck;
    boolean TextColorChange = false;
    boolean BackgroundColorChange = false;
    String TextColorCode;
    String BackgroundColorCode;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);

        // Load UserDeck
        UserDeck = getIntent().getBooleanExtra("USERDECK", true);

        // Declare Image Buttons
        ImageView BackgroundColor = (ImageView) findViewById(R.id.imgBackgroundColor);
        ImageView TextColor = (ImageView) findViewById(R.id.imgTextColor);

        // Declare Buttons
        Button Done = findViewById(R.id.btnDone);
        Button Back = findViewById(R.id.btnBack);

        // Listener Events for Image Buttons
        BackgroundColor.setOnClickListener(toggleBackgroundColor);
        TextColor.setOnClickListener(toggleTextColor);

        // Listener Events for Buttons
        Done.setOnClickListener(toggleDone);
        Back.setOnClickListener(toggleBack);

        // EditText
        EditText deckName = (EditText) findViewById(R.id.etextDeckName);

        //Database
        db = new DBAdapter(this);

        // Text size
        Configuration configuration = this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

        if (screenWidthDp <= 359) {
            deckName.setTextSize(30);
        } else if (screenWidthDp <= 409) {
            deckName.setTextSize(35);
        } else if (screenWidthDp <= 599) {
            deckName.setTextSize(40);
        } else if (screenWidthDp <= 719) {
            deckName.setTextSize(35);
        } else if (screenWidthDp <= 849) {
            deckName.setTextSize(40);
        } else {
            deckName.setTextSize(41);
        }
    }



    private final View.OnClickListener toggleDone =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast to inform users if something is wrong
                    int duration = Toast.LENGTH_LONG;
                    Toast toast;

                    EditText DeckName = (EditText) findViewById(R.id.etextDeckName);

                    // Make Sure all Fields are Filled Out
                    String Name = DeckName.getText().toString();


                    db.open();
                    if (!BackgroundColorChange || !TextColorChange) {
                        toast = Toast.makeText(getApplicationContext(), "Please Select Name, Text and Background Color.", duration);
                        toast.show();

                        // If the name is blank.
                    }else if (Name.length() == 0) {
                        toast = Toast.makeText(getApplicationContext(), "The name field cannot be blank!", duration);
                        toast.show();
                        // Check length.
                    }else if (Name.length() > 28) {
                        toast = Toast.makeText(getApplicationContext(), "Name cannot be longer than 28 characters!", duration);
                        toast.show();
                        // If it's a user deck.
                    }else if(UserDeck) {
                        // Remove whitespace at the beginning.
                        while (Character.isWhitespace(Name.charAt(0))) {
                            Name = Name.substring(1);
                        }

                        // Remove whitespace at the end.
                        while (Character.isWhitespace(Name.charAt(Name.length() - 1))) {
                                Name = Name.substring(0, Name.length() - 1);
                        }

                        // Remove any multiple spaces.
                        for (int x = 0; x < Name.length() - 1; x++) {
                            if (Character.isWhitespace(Name.charAt(x)) && Character.isWhitespace(Name.charAt(x + 1))) {
                                Name = Name.substring(0, x) + Name.substring(x + 1);
                                x--;
                            }
                        }

                        Cursor cursor = db.getAllRows("USERDECKS", 0);
                        cursor.moveToFirst();

                        // Flag to see if name is being used
                        boolean nameUsed = false;

                        for (int x = 0; x < cursor.getCount(); x++){
                            if (Name.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))){
                                nameUsed = true;
                            }
                            cursor.moveToNext();
                        }

                        if (nameUsed == false) {
                            db.insertRow(Name, TextColorCode, BackgroundColorCode, 50, "USERDECKS", 0, 0);

                            // Create table if it does not exist for win/loss
                            db.createTable("[" + Name + "]", 2);

                            // Be sure FACEDDECKS exist
                            db.createTable("FACEDDECKS", 1);

                            // Fill with FACEDDECKS
                            Cursor c = db.getAllRows("FACEDDECKS", 1);

                            for (int x = 0; x < c.getCount(); x++) {
                                db.insertRow(c.getString(c.getColumnIndexOrThrow("name")),
                                        "", "", 0, "[" +Name + "]", 0,  2);
                            }
                            c.close();
                            cursor.close();
                            finish();
                        }else {
                            toast = Toast.makeText(getApplicationContext(), "That name is already in use!", duration);
                            toast.show();
                        }

                    }else if(!UserDeck){
                        // Remove whitespace at the beginning.
                        while (Character.isWhitespace(Name.charAt(0))) {
                            Name = Name.substring(1);
                        }

                        // Remove whitespace at the end.
                        while (Character.isWhitespace(Name.charAt(Name.length() - 1))) {
                            Name = Name.substring(0, Name.length() - 1);
                        }

                        // Remove any multiple spaces.
                        for (int x = 0; x < Name.length() - 1; x++) {
                            if (Character.isWhitespace(Name.charAt(x)) && Character.isWhitespace(Name.charAt(x + 1))) {
                                Name = Name.substring(0, x) + Name.substring(x + 1);
                                x--;
                            }
                        }

                        Cursor cursor = db.getAllRows("FACEDDECKS", 1);
                        cursor.moveToFirst();

                       // Flag to see if name is being used
                        boolean nameUsed = false;

                        for (int x = 0; x < cursor.getCount(); x++) {
                            if (Name.equals(cursor.getString(cursor.getColumnIndexOrThrow("name")))){
                                nameUsed = true;
                            }
                            cursor.moveToNext();
                        }

                        if (nameUsed == false) {
                            // Adds to FACEDDECKS
                            db.insertRow(Name, TextColorCode, BackgroundColorCode, 0, "FACEDDECKS", 0,  1);

                            // Updater the ID
                            cursor = db.getAllRows("FACEDDECKS", 1);


                            cursor.moveToLast();
                            db.updateID(cursor.getInt(cursor.getColumnIndexOrThrow("_id")), cursor.getCount(), "FACEDDECKS");

                            // Insert new faced deck into all UserDecks win/loss
                            Cursor c = db.getAllRows("USERDECKS", 0);
                            Cursor currentTable;

                            for (int x = 0; x < c.getCount(); x++) {
                                db.insertRow(Name, "", "", 0, "[" +
                                        c.getString(c.getColumnIndexOrThrow("name")) + "]", 0,  2);

                                // Update _id.
                                currentTable = db.getAllRows("[" + c.getString(c.getColumnIndexOrThrow("name")) + "]", 2);
                                currentTable.moveToLast();

                                db.updateID(currentTable.getInt(currentTable.getColumnIndexOrThrow("_id")), cursor.getCount(),
                                        "[" + c.getString(c.getColumnIndexOrThrow("name")) + "]");
                                c.moveToNext();
                            }

                            c.close();
                            cursor.close();
                            finish();
                        }else {
                            toast = Toast.makeText(getApplicationContext(), "That name is already in use!", duration);
                            toast.show();
                        }
                    }
                    db.close();
                }
    };

    private final View.OnClickListener toggleBack =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };

    private final View.OnClickListener toggleBackgroundColor =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent x = new Intent(getApplicationContext(), ColorSelection.class);
                    startActivityForResult(x, 1);

                }
            };

    private final View.OnClickListener toggleTextColor =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent x = new Intent(getApplicationContext(), ColorSelection.class);
                    startActivityForResult(x, 2);
                }
            };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Return for Background Color
        if (requestCode == 1 && resultCode == RESULT_OK) {
            BackgroundColorCode = data.getStringExtra("selectedColor");
            ImageView BackgroundColor = (ImageView) findViewById(R.id.imgBackgroundColor);
            BackgroundColor.setBackgroundColor(Color.parseColor(BackgroundColorCode));
            BackgroundColorChange = true;
        }

        // Return for Text Color
        if (requestCode == 2 && resultCode == RESULT_OK) {
            TextColorCode = data.getStringExtra("selectedColor");
            ImageView TextColor = (ImageView) findViewById(R.id.imgTextColor);
            TextColor.setBackgroundColor(Color.parseColor(TextColorCode));
            TextColorChange = true;
        }
    }
}


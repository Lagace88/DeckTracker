package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeName extends Activity {
    DBAdapter db;
    int position;
    int userPosition;
    String userDeckName;
    boolean facedDeck;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_deck_name);

        db = new DBAdapter(this);

        // Declare Buttons
        Button done = (Button) findViewById(R.id.btn_CNDone);
        Button cancel = (Button) findViewById(R.id.btn_CNCancel);

        done.setOnClickListener(toggleDone);
        cancel.setOnClickListener(toggleCancel);

        EditText deckName = (EditText) findViewById(R.id.editNewName);

        position = getIntent().getIntExtra("Position", 0);
        userPosition = getIntent().getIntExtra("UserPosition", 0);
        facedDeck = getIntent().getBooleanExtra("FacedDeck", false);
        userDeckName = getIntent().getStringExtra("UserDeckName");

        // Set text size.
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
                    EditText newName = (EditText) findViewById(R.id.editNewName);
                    String Name = newName.getText().toString();
                    db.open();

                    Toast toast;

                    // Be sure it isn't blank.
                    if (Name.length() == 0) {
                        toast = Toast.makeText(getApplicationContext(), "The name field cannot be blank!", Toast.LENGTH_LONG);
                        toast.show();
                        // Check length.
                    }else if (Name.length() > 28) {
                    toast = Toast.makeText(getApplicationContext(), "Name cannot be longer than 28 characters!", Toast.LENGTH_LONG);
                    toast.show();
                    } else {
                        // Clean name.
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

                        // Save to appropriate table.
                        if (facedDeck == true) {
                            // Update FACEDDECKS
                            db.updateName(position, Name, "FACEDDECKS");
                            db.close();
                            finish();
                        } else {
                            // Update win rate table
                            // Make sure new name is not being used.
                            boolean nameUsed = false;
                            Cursor userDecksCursor = db.getAllRows("USERDECKS", 0);

                            for (int x = 0; x < userDecksCursor.getCount(); x++) {
                                if (Name.equals(userDecksCursor.getString(userDecksCursor.getColumnIndexOrThrow("name")))) {
                                    nameUsed = true;
                                }
                                userDecksCursor.moveToNext();
                            }

                            if (!nameUsed) {
                                // Update USERDECKS
                                db.updateName(userPosition, Name, "USERDECKS");

                                // Change table name.
                                db.updateWinRateName('[' + userDeckName + ']', '[' + Name + ']');
                                db.close();
                                finish();
                            }
                        }
                    }
                    db.close();
                }
            };

    private final View.OnClickListener toggleCancel =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };
}

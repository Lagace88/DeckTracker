package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

public class FDOptions extends Activity {
    DBAdapter db;
    boolean facedDeck;
    int position;
    int userPosition;
    String userDeckName;
    ProgressDialog deleteBar;
    int[] color = new int[3];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faceddecksoptions);

        // Declare Buttons
        Button CDeckName = findViewById(R.id.btn_ChangeName);
        Button CTextColor = findViewById(R.id.btn_ChangeTextColor);
        Button CBackgroundColor = findViewById(R.id.btn_ChangeBackgroundColor);
        Button DPastWinLoss = findViewById(R.id.btn_DeletePastWinLoss);
        Button DeleteDeck = findViewById(R.id.btn_DeleteDeck);
        Button Back = findViewById(R.id.btn_Back);

        // Listener Events for Buttons
        CDeckName.setOnClickListener(toggleCDeckName);
        CTextColor.setOnClickListener(toggleCTextColor);
        CBackgroundColor.setOnClickListener(toggleCBackgroundColor);
        DPastWinLoss.setOnClickListener(toggleDPastWinLoss);
        DeleteDeck.setOnClickListener(toggleDeleteDeck);
        Back.setOnClickListener(toggleBack);

        // Display the deck at hand
        facedDeck = getIntent().getBooleanExtra("FacedDeck", true);
        position = getIntent().getIntExtra("Position", 0);
        userDeckName = getIntent().getStringExtra("UserDeckName");
        userPosition = getIntent().getIntExtra("UserPosition", 0);
        db = new DBAdapter(this);
        populate();

        // Prepare progress bar
        deleteBar = new ProgressDialog(FDOptions.this);
        deleteBar.setCancelable(false);
        deleteBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        deleteBar.setMax(100);
        deleteBar.setMessage("Deleting Deck...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    public void populate() {
        AutoResizeTextView current = findViewById(R.id.txt_FDODeck);


        String Name = "";

        int Tred;
        int Tgreen;
        int Tblue;

        int Bred;
        int Bgreen;
        int Bblue;

        db.open();

        if (facedDeck == true) {
            Cursor c = db.getRow(position, "FACEDDECKS", 1);
            Name = c.getString(c.getColumnIndexOrThrow("name"));

            Tred = c.getInt(c.getColumnIndexOrThrow("tred"));
            Tgreen = c.getInt(c.getColumnIndexOrThrow("tgreen"));
            Tblue = c.getInt(c.getColumnIndexOrThrow("tblue"));

            Bred = c.getInt(c.getColumnIndexOrThrow("bred"));
            Bgreen = c.getInt(c.getColumnIndexOrThrow("bgreen"));
            Bblue = c.getInt(c.getColumnIndexOrThrow("bblue"));

            current.setText(Name);
            current.setTextColor(Color.rgb(Tred, Tgreen, Tblue));
            current.setBackgroundColor(Color.rgb(Bred, Bgreen, Bblue));
            c.close();
        } else {
            Cursor c = db.getRow(userPosition, "USERDECKS", 0);
            Name = c.getString(c.getColumnIndexOrThrow("name"));

            Tred = c.getInt(c.getColumnIndexOrThrow("tred"));
            Tgreen = c.getInt(c.getColumnIndexOrThrow("tgreen"));
            Tblue = c.getInt(c.getColumnIndexOrThrow("tblue"));

            Bred = c.getInt(c.getColumnIndexOrThrow("bred"));
            Bgreen = c.getInt(c.getColumnIndexOrThrow("bgreen"));
            Bblue = c.getInt(c.getColumnIndexOrThrow("bblue"));

            current.setText(Name);
            current.setTextColor(Color.rgb(Tred, Tgreen, Tblue));
            current.setBackgroundColor(Color.rgb(Bred, Bgreen, Bblue));
            c.close();
        }
        db.close();
    }

    private final View.OnClickListener toggleCDeckName =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FDOptions.this, ChangeName.class);
                    intent.putExtra("Position", position);
                    intent.putExtra("UserPosition", userPosition);
                    intent.putExtra("UserDeckName", userDeckName);
                    intent.putExtra("FacedDeck", facedDeck);
                    startActivity(intent);
                }
            };

    private final View.OnClickListener toggleCTextColor =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FDOptions.this, ColorSelection.class);
                    startActivityForResult(intent, 2);
                }
            };

    private final View.OnClickListener toggleCBackgroundColor =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FDOptions.this, ColorSelection.class);
                    startActivityForResult(intent, 1);
                }
            };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Return for Background Color
        db.open();

        if (requestCode == 1 && resultCode == RESULT_OK) {
            color[0] = data.getIntExtra("red", 0);
            color[1] = data.getIntExtra("green", 0);
            color[2] = data.getIntExtra("blue", 0);

            if (facedDeck) {
                // Save new color to FACEDDECKS
                db.updateBGColor(position, color, "FACEDDECKS");
            } else {
                // Save new color to USERDECKS
                db.updateBGColor(userPosition, color, "USERDECKS");
            }
        }

        // Return for Text Color
        if (requestCode == 2 && resultCode == RESULT_OK) {
            color[0] = data.getIntExtra("red", 0);
            color[1] = data.getIntExtra("green", 0);
            color[2] = data.getIntExtra("blue", 0);

            if (facedDeck) {
                // Save new color to FACEDDECKS
                db.updateTextColor(position, color, "FACEDDECKS");
            } else {
                // Save new color to USERDECKS
                db.updateTextColor(userPosition, color, "USERDECKS");
            }
        }
        db.close();
        populate();
    }

    private final View.OnClickListener toggleDPastWinLoss =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayDWinLossPopup();
                }
            };

    private void displayDWinLossPopup() {
        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View container = li.inflate(R.layout.popup_delete_face_past_win_loss, (ViewGroup) findViewById(R.id.PURL));
        Configuration configuration = this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;
        Toast.makeText(getApplicationContext(), Integer.toString(configuration.screenWidthDp), Toast.LENGTH_SHORT).show();

        PopupWindow pWtest;

        // Will be used to convert pixels size to dps for popup window.
        float scale = getResources().getDisplayMetrics().density;
        int hieghtInP;
        int widthInP;

        if (screenWidthDp <= 359) {
            widthInP = (int) (250 * scale + 0.5f);
            hieghtInP = (int) (150 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else if (screenWidthDp <= 400) {
            widthInP = (int) (250 * scale + 0.5f);
            hieghtInP = (int) (150 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else if (screenWidthDp <= 599) {
            widthInP = (int) (300 * scale + 0.5f);
            hieghtInP = (int) (150 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else if (screenWidthDp <= 719) {
            widthInP = (int) (450 * scale + 0.5f);
            hieghtInP = (int) (250 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else {
            widthInP = (int) (450 * scale + 0.5f);
            hieghtInP = (int) (250 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        }

        final PopupWindow pW = pWtest;
        pW.showAtLocation(findViewById(R.id.FDORel), Gravity.CENTER, 0, 0);

        Button Yes = container.findViewById(R.id.btn_DeleteWinLossYes);
        Button No =  container.findViewById(R.id.btn_DeleteWinLossCancel);

        // Listener Events
        Yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.open();

                // If it is a facedDeck.
                if (facedDeck) {
                    // Change the wins and losses.
                    db.updateWins(position, 0, '[' + userDeckName + ']');
                    db.updateLoses(position, 0, '[' + userDeckName + ']');

                    // Update the userDecks winrate.
                    Cursor cFacedDecks = db.getAllRows('[' + userDeckName + ']', 2);
                    cFacedDecks.moveToFirst();
                    double wins = 0;
                    double losses = 0;

                    for (int x = 0; x < cFacedDecks.getCount(); x++) {
                        wins += cFacedDecks.getInt(cFacedDecks.getColumnIndexOrThrow("wins"));
                        losses += cFacedDecks.getInt(cFacedDecks.getColumnIndexOrThrow("losses"));
                        cFacedDecks.moveToNext();
                    }
                    cFacedDecks.close();
                    db.updateWinRate((int) Math.round(wins / (wins + losses) * 100), userPosition);

                    // Else it is a user deck.
                } else {
                    // Change the wins and losses in the win rate table.
                    Cursor winRateCursor = db.getAllRows('[' + userDeckName + ']', 2);

                    for (int x = 1; x <= winRateCursor.getCount(); x++) {
                        db.updateWins(x, 0, '[' + userDeckName + ']');
                        db.updateLoses(x, 0, '[' + userDeckName + ']');
                    }

                    // Change the winrate in USERDECKS
                    db.updateWinRate(50, userPosition);
                }
                db.close();
                pW.dismiss();
            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pW.dismiss();
            }
        });
    }

    private final View.OnClickListener toggleDeleteDeck =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayDeleteDeckPopUp();
                }
            };

    public void displayDeleteDeckPopUp() {
        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View container = li.inflate(R.layout.popup_delete_faced_deck, (ViewGroup) findViewById(R.id.PUDFD));
        PopupWindow pWtest;
        Configuration configuration = this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

        // Will be used to convert pixels size to dps for popup window.
        float scale = getResources().getDisplayMetrics().density;
        int hieghtInP;
        int widthInP;

        if (screenWidthDp <= 359) {
            widthInP = (int) (250 * scale + 0.5f);
            hieghtInP = (int) (150 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else if (screenWidthDp <= 400) {
            widthInP = (int) (250 * scale + 0.5f);
            hieghtInP = (int) (150 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else if (screenWidthDp <= 599) {
            widthInP = (int) (300 * scale + 0.5f);
            hieghtInP = (int) (150 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else if (screenWidthDp <= 719) {
            widthInP = (int) (450 * scale + 0.5f);
            hieghtInP = (int) (250 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        } else {
            widthInP = (int) (450 * scale + 0.5f);
            hieghtInP = (int) (250 * scale + 0.5f);
            pWtest = new PopupWindow(container, widthInP, hieghtInP, true);

        }

        final PopupWindow pW = pWtest;
        pW.showAtLocation(findViewById(R.id.FDORel), Gravity.CENTER, 0, 0);

        Button Yes = container.findViewById(R.id.btn_DeleteFacedDeckYes);
        Button No = container.findViewById(R.id.btn_DeleteFacedDeckNo);

        // Listener Events
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();

                if (facedDeck) {
                    // Delete from each UserDeck Database
                    Cursor userDecksCursor = db.getAllRows("USERDECKS", 0);

                    for (int x = 0; x < userDecksCursor.getCount(); x++) {
                        db.deleteRow(position, "[" + userDecksCursor.getString(userDecksCursor.getColumnIndexOrThrow("name")) + "]");

                        // Decrease ID for each entry beyond position.
                        Cursor cursor1 = db.getAllRows("[" + userDecksCursor.getString(userDecksCursor.getColumnIndexOrThrow("name")) + "]", 2);
                        cursor1.moveToPosition(position - 1);

                        for (int y = position; y <= cursor1.getCount(); y++) {
                            db.updateID(y + 1, y, "[" + userDecksCursor.getString(userDecksCursor.getColumnIndexOrThrow("name")) + "]");
                        }

                        // Calculate new winRate for User Deck.
                        db.updateWinRate(calculateNewWinRate("[" + userDecksCursor.getString(userDecksCursor.getColumnIndexOrThrow("name")) + "]"),
                                userDecksCursor.getInt(userDecksCursor.getColumnIndexOrThrow("_id")));
                        userDecksCursor.moveToNext();
                    }
                    userDecksCursor.close();

                    // Delete from FacedDecks
                    db.deleteRow(position, "FACEDDECKS");
                    Cursor facedDecksCursor = db.getAllRows("FACEDDECKS", 1);
                    facedDecksCursor.moveToPosition(position - 1);

                    // Decrease ID beyond position
                    for (int x = position; x <= facedDecksCursor.getCount(); x++) {
                        db.updateID(x + 1, x, "FACEDDECKS");
                        facedDecksCursor.moveToNext();
                    }

                    facedDecksCursor.close();
                    pW.dismiss();
                    setResult(RESULT_OK);
                    db.close();

                    // Delete from Recommended
                    new deleteDeck().execute();
                } else {
                    // Delete from USERDECKS
                    Cursor userDecksCursor = db.getAllRows("USERDECKS", 0);

                    db.deleteRow(userPosition, "USERDECKS");

                    // Update rest of USERDECKS
                    for (int x = userPosition + 1; x <= userDecksCursor.getCount(); x++) {
                        db.updateID(x, x - 1, "USERDECKS");
                    }

                    // Delete win ratio table
                    db.deleteTable(userDeckName);

                    pW.dismiss();
                    setResult(RESULT_OK);
                    db.close();
                    Intent intent = new Intent(FDOptions.this, UserDeckDisplay.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });



        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pW.dismiss();
            }
        });
    }

    private final View.OnClickListener toggleBack =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };

    public int calculateNewWinRate(String table) {
        Cursor cursor = db.getAllRows(table, 2);
        cursor.moveToFirst();
        double wins = 0;
        double losses = 0;

        for (int x = 0; x < cursor.getCount(); x++) {
            wins += cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
            losses += cursor.getInt(cursor.getColumnIndexOrThrow("losses"));
            cursor.moveToNext();
        }

        if (wins + losses == 0) {
            return 50;
        }
        return (int) Math.round(wins / (wins + losses) * 100);
    }

    class deleteDeck extends AsyncTask<Void, Integer, Integer> {
        Cursor recommendedCursor;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db.open();
            deleteBar.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            deleteBar.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // Delete from Recommended
            // Run through entirety of RECOMMENDED
            recommendedCursor = db.getAllRows("RECOMMENDED", 3);

            for (int x = 0; x < recommendedCursor.getCount() - 1; x++) {
                recommendedCursor = db.getAllRows("RECOMMENDED", 3);
                recommendedCursor.moveToPosition(x);
                // If the entry matches the deck we are deleting.
                if (recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) == position) {
                    // Bring each deck value down.
                    for (int y = x; y < recommendedCursor.getCount() - 1; y++) {
                        recommendedCursor.moveToNext();
                        db.updateRecommended(recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")), y + 1);
                    }
                    x--;
                }
                publishProgress(x);
            }
            publishProgress(100);
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            deleteBar.dismiss();
            db.close();
            finish();
        }
    }
}

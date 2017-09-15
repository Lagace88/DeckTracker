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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class FDOptions extends Activity {
    DBAdapter db;
    boolean facedDeck;
    int position;
    int userPosition;
    String userDeckName;
    ProgressDialog deleteBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faceddecksoptions);

        // Declare Buttons
        Button CDeckName = (Button) findViewById(R.id.btn_ChangeName);
        Button CTextColor = (Button) findViewById(R.id.btn_ChangeTextColor);
        Button CBackgroundColor = (Button) findViewById(R.id.btn_ChangeBackgroundColor);
        Button DPastWinLoss = (Button) findViewById(R.id.btn_DeletePastWinLoss);
        Button DeleteDeck = (Button) findViewById(R.id.btn_DeleteDeck);
        Button Back = (Button) findViewById(R.id.btn_Back);

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
        TextView current = (TextView) findViewById(R.id.txt_FDODeck);

        String Name = "";
        db.open();

        if (facedDeck == true) {
            Cursor c = db.getRow(position, "FACEDDECKS", 1);
            Name = c.getString(c.getColumnIndexOrThrow("name"));
            current.setText(Name);
            current.setTextColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("textcolor"))));
            current.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("bgcolor"))));
        } else {
            Cursor c = db.getRow(userPosition, "USERDECKS", 0);
            Name = c.getString(c.getColumnIndexOrThrow("name"));
            current.setText(Name);
            current.setTextColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("textcolor"))));
            current.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("bgcolor"))));
        }
        db.close();

        // Set appropriate text size.
        Configuration configuration = this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

        if (screenWidthDp <= 359) {
            if (Name.length() <= 10) {
                current.setTextSize(30);
            } else if (Name.length() <= 17) {
                current.setTextSize(18);
            } else if (Name.length() <= 28) {
                current.setTextSize(11);
            }
        } else if (screenWidthDp <= 400) {
            if (Name.length() <= 10) {
                current.setTextSize(35);
            } else if (Name.length() <= 17) {
                current.setTextSize(21);
            } else if (Name.length() <= 28) {
                current.setTextSize(12);
            }
        } else if (screenWidthDp <= 599) {
            if (Name.length() <= 10) {
                current.setTextSize(40);
            } else if (Name.length() <= 17) {
                current.setTextSize(24);
            } else if (Name.length() <= 28) {
                current.setTextSize(15);
            }
        } else if (screenWidthDp <= 719) {
            if (Name.length() <= 10) {
                current.setTextSize(52);
            } else if (Name.length() <= 17) {
                current.setTextSize(31);
            } else if (Name.length() <= 28) {
                current.setTextSize(20);
            }
        } else {
            if (Name.length() <= 10) {
                current.setTextSize(61);
            } else if (Name.length() <= 17) {
                current.setTextSize(36);
            } else if (Name.length() <= 28) {
                current.setTextSize(27);
            }
        }
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
        String color;
        db.open();

        if (requestCode == 1 && resultCode == RESULT_OK) {
            color = data.getStringExtra("selectedColor");
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
            color = data.getStringExtra("selectedColor");
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

        Button Yes = (Button) container.findViewById(R.id.btn_DeleteWinLossYes);
        Button No = (Button) container.findViewById(R.id.btn_DeleteWinLossCancel);

        // Listener Events
        Yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.open();

                if (facedDeck) {
                    db.updateWins(userPosition, 0, userDeckName);
                    db.updateLoses(userPosition, 0, userDeckName);
                    db.updateWinRate(50, userPosition);
                } else {
                    // Change the wins and losses in the win rate table.
                    Cursor winRateCursor = db.getAllRows('[' + userDeckName + ']', 2);

                    for (int x = 1; x <= winRateCursor.getCount(); x++) {
                        db.updateWins(x, 0, '[' + userDeckName + ']');
                        db.updateLoses(x, 0, '[' + userDeckName + ']');
                    }

                    // Change the winrate in USERDECKS
                    db.updateWinRate(0, userPosition);
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

        Button Yes = (Button) container.findViewById(R.id.btn_DeleteFacedDeckYes);
        Button No = (Button) container.findViewById(R.id.btn_DeleteFacedDeckNo);

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

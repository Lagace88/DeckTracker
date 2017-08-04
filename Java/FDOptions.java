package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;

public class FDOptions extends Activity {
    DBAdapter db;
    int position;
    int userPosition;
    String UserDeck;

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
        position = getIntent().getIntExtra("Position", 0);
        UserDeck = getIntent().getStringExtra("UserDeck");
        userPosition = getIntent().getIntExtra("UserPosition", 0);
        db = new DBAdapter(this);
        populate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    public void populate() {
        TextView current = (TextView) findViewById(R.id.txt_FDODeck);

        db.open();
        Cursor c = db.getRow(position, "FACEDDECKS", 1);
        db.close();
        String Name = c.getString(c.getColumnIndexOrThrow("name"));
        current.setText(Name);
        current.setTextColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("textcolor"))));
        current.setBackgroundColor(Color.parseColor(c.getString(c.getColumnIndexOrThrow("bgcolor"))));
        // Set appropriate text size.
        if (Name.length() <= 10) {
            current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }else if (Name.length() <= 17) {
            current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }else if (Name.length() <= 28) {
            current.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }
    }

    private final View.OnClickListener toggleCDeckName =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FDOptions.this, ChangeName.class);
                    intent.putExtra("Position", position);
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
        if (requestCode == 1 && resultCode == RESULT_OK) {
            color = data.getStringExtra("selectedColor");

            // Save new color to FACEDDECKS
            db.open();
            db.updateBGColor(position, color, "FACEDDECKS");
            db.close();
        }

        // Return for Text Color
        if (requestCode == 2 && resultCode == RESULT_OK) {
            color = data.getStringExtra("selectedColor");

            // Save new color to FACEDDECKS
            db.open();
            db.updateTextColor(position, color, "FACEDDECKS");
            db.close();
        }

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
        final PopupWindow pW = new PopupWindow(container, 450, 450, true);
        pW.showAtLocation(findViewById(R.id.FDORel), Gravity.CENTER, 0, 0);

        Button Yes = (Button) container.findViewById(R.id.btn_DeleteWinLossYes);
        Button No = (Button) container.findViewById(R.id.btn_DeleteWinLossCancel);

        // Listener Events
        Yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.open();
                db.updateWins(position, 0, UserDeck);
                db.updateLoses(position, 0, UserDeck);
                db.updateWinRate(calculateNewWinRate(UserDeck), userPosition);
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
        final PopupWindow pW = new PopupWindow(container, 450, 450, true);
        pW.showAtLocation(findViewById(R.id.FDORel), Gravity.CENTER, 0, 0);

        Button Yes = (Button) container.findViewById(R.id.btn_DeleteFacedDeckYes);
        Button No = (Button) container.findViewById(R.id.btn_DeleteFacedDeckNo);

        // Listener Events
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                // Delete from each UserDeck Database
                Cursor userDecksCursor = db.getAllRows("USERDECKS", 0);
                userDecksCursor.moveToFirst();

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

                // Prepare container for delete.
                //Cursor recommendedCursor = db.getAllRows("RECOMMENDED", 3);
               // db.close();
                //ContainerForDelete params = new ContainerForDelete(db, recommendedCursor, position);

                // Start AsyncThread DeleteFromRecommended.
                //DeleteFromRecommended thread = new DeleteFromRecommended();
                //thread.execute(params);

                // Delete for Recommended
               /* Cursor recommendedCursor = db.getAllRows("RECOMMENDED", 3);
                recommendedCursor.moveToFirst();
                int y = 0;
                int numberOfEntriesRemoved = 0;

                for (int x = 0; x < recommendedCursor.getCount(); x++) {
                    // See if deck matches the deck we are deleting.
                    if (recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) == position) {
                        // Bring down each deck after.
                        for (y = x + 1; y < recommendedCursor.getCount(); y++) {
                            recommendedCursor.moveToNext();
                            db.updateRecommended(recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")), y);
                        }
                        // Update cursor1
                        recommendedCursor = db.getAllRows("RECOMMENDED", 3);

                        // Move to one position behind to make up for movetonext.
                        recommendedCursor.moveToPosition(x - 1);

                        // Reset x
                        x = x - 1;

                        numberOfEntriesRemoved++;
                    }
                    recommendedCursor.moveToNext();
                }

                // Assign 0 to number of spots now free to allow win/loss to know it is open.
                for (int x = numberOfEntriesRemoved; x > 0; x--) {
                            db.updateRecommended(0, recommendedCursor.getCount() + 1 - x);
                }

                // Lower each deck number by 1 that is above position.
                recommendedCursor.moveToFirst();
                for (int x = 0; x < recommendedCursor.getCount(); x++) {
                    if (recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) > position &&
                            recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) != 0) {
                        db.updateRecommended(recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) - 1, x + 1);
                    }
                    recommendedCursor.moveToNext();
                }*/

                pW.dismiss();
                setResult(RESULT_OK);

                // Prepare progress bar.
                LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                final View container = li.inflate(R.layout.progressbar_deletedeckprogressbar, (ViewGroup) findViewById(R.id.progressBarLayout));
                final PopupWindow popupProgressBar = new PopupWindow(container, 450, 450, true);
                popupProgressBar.showAtLocation(findViewById(R.id.FDORel), Gravity.CENTER, 0, 0);
                ProgressBar deleteBar = (ProgressBar) findViewById(R.id.progressBar);

                Cursor recommendedCursor = db.getAllRows("RECOMMENDED", 3);
                db.close();
                ContainerForDelete cfd = new ContainerForDelete(db, recommendedCursor, position);
                DeleteFromRecommended task = new DeleteFromRecommended(new DeleteFromRecommended.AsyncResponse() {
                    @Override
                    public void processFinish() {
                        finish = (Button) findViewById(R.id.btn_FinishDelete);
                        finish.setEnabled(true);
                    }
                });
                task.execute(cfd);

                finish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupProgressBar.dismiss();
                        finish();
                    }
                });
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
}

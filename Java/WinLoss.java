package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WinLoss extends Activity {
    DBAdapter db;
    String UserDeck;
    int Position;
    int UserPosition;

    //TextView FacedDeckName = (TextView) findViewById(R.id.txtFacedDeckName);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_faceddeck);

        // Declare Buttons
        Button Win = (Button) findViewById(R.id.btn_Win);
        Button Loss = (Button) findViewById(R.id.btn_Loss);
        Button Options = (Button) findViewById(R.id.btn_Options);
        Button Back = (Button) findViewById(R.id.btn_Back);

        // Listener Events For Buttons
        Win.setOnClickListener(toggleWin);
        Loss.setOnClickListener(toggleLoss);
        Options.setOnClickListener(toggleOptions);
        Back.setOnClickListener(toggleBack);

        // Populate Current Deck
        UserDeck = getIntent().getStringExtra("UserDeckName");
        UserDeck = "[" + UserDeck + "]";
        Position = getIntent().getIntExtra("Position", 0);
        UserPosition = getIntent().getIntExtra("UserPosition", 0);
        db = new DBAdapter(this);
        populate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populate();
    }

    public void populate() {
        // Populate

        // TextView Declares
        TextView DisplayDeckName = (TextView) findViewById(R.id.txt_FacedDeck);
        TextView TimesFaced = (TextView) findViewById(R.id.result_TimesFaced);
        TextView Wins = (TextView) findViewById(R.id.result_Wins);
        TextView Loses = (TextView) findViewById(R.id.result_Loses);
        TextView WinRate = (TextView) findViewById(R.id.result_WinRate);

        // Retrieve information from db
        db.open();
        Cursor cOne = db.getRow(Position, "FACEDDECKS", 1);

        String Name = cOne.getString(cOne.getColumnIndexOrThrow("name"));
        DisplayDeckName.setText(Name);
        DisplayDeckName.setTextColor(Color.parseColor(cOne.getString(cOne.getColumnIndexOrThrow("textcolor"))));
        DisplayDeckName.setBackgroundColor(Color.parseColor(cOne.getString(cOne.getColumnIndexOrThrow("bgcolor"))));

        // Set text size for DisplayDeckName
        Configuration configuration = this.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

        if (screenWidthDp <= 359) {
            if (Name.length() <= 10) {
                DisplayDeckName.setTextSize(30);
            } else if (Name.length() <= 17) {
                DisplayDeckName.setTextSize(18);
            } else if (Name.length() <= 28) {
                DisplayDeckName.setTextSize(11);
            }
        } else if (screenWidthDp <= 400) {
            if (Name.length() <= 10) {
                DisplayDeckName.setTextSize(35);
            } else if (Name.length() <= 17) {
                DisplayDeckName.setTextSize(21);
            } else if (Name.length() <= 28) {
                DisplayDeckName.setTextSize(12);
            }
        } else if (screenWidthDp <= 599) {
            if (Name.length() <= 10) {
                DisplayDeckName.setTextSize(40);
            } else if (Name.length() <= 17) {
                DisplayDeckName.setTextSize(24);
            } else if (Name.length() <= 28) {
                DisplayDeckName.setTextSize(15);
            }
        } else if (screenWidthDp <= 719) {
            if (Name.length() <= 10) {
                DisplayDeckName.setTextSize(52);
            } else if (Name.length() <= 17) {
                DisplayDeckName.setTextSize(31);
            } else if (Name.length() <= 28) {
                DisplayDeckName.setTextSize(20);
            }
        } else {
            if (Name.length() <= 10) {
                DisplayDeckName.setTextSize(61);
            } else if (Name.length() <= 17) {
                DisplayDeckName.setTextSize(36);
            } else if (Name.length() <= 28) {
                DisplayDeckName.setTextSize(27);
            }
        }


        Cursor cTwo = db.getRow(Position, UserDeck, 2);
        db.close();

        int wins = cTwo.getInt(cTwo.getColumnIndexOrThrow("wins"));
        int losses = cTwo.getInt(cTwo.getColumnIndexOrThrow("losses"));

        TimesFaced.setText(String.valueOf(wins + losses));
        Wins.setText(String.valueOf(wins));
        Loses.setText(String.valueOf(losses));

        if (wins + losses == 0) {
            WinRate.setText("50%");
        } else {
            double winRate = ((double) wins / (double) (wins + losses)) * 100;
            winRate = Math.round(winRate);
            WinRate.setText(String.format("%.0f", winRate) + "%");
        }

        UserDeck = getIntent().getStringExtra("UserDeckName");
        UserPosition = getIntent().getIntExtra("UserPosition", 0);
    }

    private final View.OnClickListener toggleWin =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update database for a win.
                    db.open();
                    Cursor cursor = db.getRow(Position, UserDeck, 2);
                    int wins = cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
                    wins++;
                    db.updateWins(Position, wins, UserDeck);
                    calculateNewWinRate();
                    db.updateWinRate(calculateNewWinRate(), UserPosition);
                    manageRecommended();
                    db.close();

                    finish();
                }
            };

    private final View.OnClickListener toggleLoss =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update Database for a Loss
                    db.open();
                    Cursor cursor = db.getRow(Position, UserDeck, 2);
                    int losses = cursor.getInt(cursor.getColumnIndexOrThrow("losses"));
                    losses++;
                    db.updateLoses(Position, losses, UserDeck);
                    db.updateWinRate(calculateNewWinRate(), UserPosition);
                    cursor.close();
                    manageRecommended();
                    db.close();

                    finish();
                }
            };

    public int calculateNewWinRate() {
        Cursor cursor = db.getAllRows(UserDeck, 2);
        cursor.moveToFirst();
        double wins = 0;
        double losses = 0;

        for (int x = 0; x < cursor.getCount(); x++) {
            wins += cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
            losses += cursor.getInt(cursor.getColumnIndexOrThrow("losses"));
            cursor.moveToNext();
        }

        cursor.close();
        return (int) Math.round(wins / (wins + losses) * 100);
    }

    public void manageRecommended() {
        // Create RECOMMENDED table if it doesn't exist.
        db.createTable("RECOMMENDED", 3);

        Cursor RecommendedCursor;
        RecommendedCursor = db.getAllRows("RECOMMENDED", 3);

        // Add end flag if the table is new.
        if (RecommendedCursor.getCount() == 0) {
            db.insertRow(null, null, null, 0, "RECOMMENDED", 0, 3);

            // Update Cursor
            RecommendedCursor = db.getAllRows("RECOMMENDED", 3);

            // Add the deck to the recommended table.
            db.addToRecommended(Position, RecommendedCursor.getCount());

            // Check to see if there is left over space.
        } else if (RecommendedCursor.getCount() <= 100) {
            // Move RecommendedCursor to Position before flag.
            RecommendedCursor.moveToPosition(RecommendedCursor.getCount() - 2);

            // Find the last free space if there is an open space.
            if (RecommendedCursor.getInt(RecommendedCursor.getColumnIndexOrThrow("deck")) == 0) {
                // Counter to see how many steps back we have to go.
                int counter = 0;

                while (RecommendedCursor.getInt(RecommendedCursor.getColumnIndexOrThrow("deck")) == 0
                        && RecommendedCursor.getPosition() != 0) {
                    // If we are not at the beginning.
                        counter++;
                        RecommendedCursor.moveToPrevious();
                }

                db.updateRecommended(Position, RecommendedCursor.getCount() - (counter + 1));
            } else {
                db.addToRecommended(Position, RecommendedCursor.getCount());
            }
            // If the database is full (500 entries).
        } else {
            // If the recommended table is full replace each deck with the one above and add to last.
            RecommendedCursor.moveToPosition(RecommendedCursor.getCount() - 2);

            // If there is open space, fill open space.
            if (RecommendedCursor.getInt(RecommendedCursor.getColumnIndexOrThrow("deck")) == 0) {
                // Counter to see how many steps back we have to go.
                int counter = 0;

                while (RecommendedCursor.getInt(RecommendedCursor.getColumnIndexOrThrow("deck")) == 0) {
                    // If we are not at the beginning.
                    if (RecommendedCursor.getPosition() != 0) {
                        counter++;
                        RecommendedCursor.moveToPrevious();
                    }
                }

                db.updateRecommended(Position, RecommendedCursor.getCount() - counter);
            } else {
                RecommendedCursor.moveToPosition(1);
                for (int x = 1; x < 101; x++) {
                    db.updateRecommended(RecommendedCursor.getInt(RecommendedCursor.getColumnIndexOrThrow("deck")), x);
                    RecommendedCursor.moveToNext();
                }

                // Update with new deck.
                db.updateRecommended(Position, RecommendedCursor.getCount() - 1);
            }
        }

        RecommendedCursor.close();
    }

    private final View.OnClickListener toggleOptions =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start Options Activity
                    Intent intent = new Intent(WinLoss.this, FDOptions.class);
                    intent.putExtra("Position", Position);
                    intent.putExtra("UserDeckName", UserDeck);
                    intent.putExtra("UserPosition", UserPosition);
                    startActivityForResult(intent, 1);
                }
            };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Close WinLoss if deck has been deleted.
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }

    private final View.OnClickListener toggleBack =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close Activity
                    finish();
                }
            };
}


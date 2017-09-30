package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class Options extends Activity {
    EditText pastRecommended;
    SharedPreferences sP;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Declare Buttons
        Button Back = findViewById(R.id.btnBack);
        pastRecommended = findViewById(R.id.editPastRecommended);
        Button DeleteDatabase = findViewById(R.id.btnDeleteDB);

        // Listener Events for Buttons
        Back.setOnClickListener(toggleBack);
        DeleteDatabase.setOnClickListener(toggleDeleteDatabase);

        // Fill in pastRecommended
        sP = getApplicationContext().getSharedPreferences("DTPPref", 0);
        pastRecommended.setText(Integer.toString(sP.getInt("PastRecommended", 100)));

        db = new DBAdapter(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private final View.OnClickListener toggleDeleteDatabase =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayDDPopup();
                }
            };

    private void displayDDPopup() {
        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View container = li.inflate(R.layout.popup_delete_database, (ViewGroup) findViewById(R.id.PUDD));
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
        pW.showAtLocation(findViewById(R.id.OptionsLayout), Gravity.CENTER, 0, 0);

        Button Yes = container.findViewById(R.id.btn_YesDeleteDatabase);
        Button No = container.findViewById(R.id.btn_NoDeleteDatabase);

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.open();

                // Delete Faced Decks.
                db.deleteTable("FACEDDECKS");

                // Delete each win/loss table.
                Cursor cWinLoss = db.getAllRows("USERDECKS", 0);

                for (int x = 0; x < cWinLoss.getCount(); x++) {
                    db.deleteTable(cWinLoss.getString(cWinLoss.getColumnIndexOrThrow("name")));
                    cWinLoss.moveToNext();
                }
                cWinLoss.close();

                // Delete User Decks.
                db.deleteTable("USERDECKS");

                // Delete RECOMMENDED.
                db.deleteTable("RECOMMENDED");

                db.close();
                pW.dismiss();
            }
        });

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pW.dismiss();
            }
        });
    }

    private final View.OnClickListener toggleBack =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newPastRecommended = Integer.parseInt(pastRecommended.getText().toString());

                    if (1 <= newPastRecommended && newPastRecommended <= 100) {
                        SharedPreferences.Editor editor = sP.edit();

                        editor.putInt("PastRecommended", newPastRecommended);
                        editor.commit();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"Past Game Capacity Must Be Within 1 and 100!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int newPastRecommended = Integer.parseInt(pastRecommended.getText().toString());

        if (1 <= newPastRecommended && newPastRecommended <= 100) {
            SharedPreferences.Editor editor = sP.edit();

            editor.putInt("PastRecommended", newPastRecommended);
            editor.commit();
        }
    }
}


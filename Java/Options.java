package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Options extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Declare Buttons
        Button Background = (Button) findViewById(R.id.btnBackground);
        Button Back = (Button) findViewById(R.id.btnBack);

        // Listener Events for Buttons
        Background.setOnClickListener(toggleBackground);
        Back.setOnClickListener(toggleBack);

        getBackground();

        setSpinners();
    }

    @Override
    protected void onResume(){
        super.onResume();

        getBackground();
        setSpinners();
    }

    // Button toggles
    private final View.OnClickListener toggleBackground =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent x = new Intent(Options.this, Background.class);
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

    private final

    void getBackground() {
        // Declare Image for Background
        ImageView OptionsBackground = (ImageView) findViewById(R.id.imgOptionsBackground);

        // Load Background
        SharedPreferences sharedPreferences = getSharedPreferences("HearthDeckTracker", Context.MODE_PRIVATE);
        int ActivityBackground = sharedPreferences.getInt("APPBACKGROUND", 0);
        switch (ActivityBackground) {
            case 0:
                OptionsBackground.setImageResource(R.drawable.hearthstone);
                break;
            case 1:
                OptionsBackground.setImageResource(R.drawable.leeroy);
                break;
            case 2:
                OptionsBackground.setImageResource(R.drawable.hearthstonetwo);
                break;
            default:
                Toast error = Toast.makeText(this.getApplication(), "Background Could Not Be Found, \n" +
                        "Loading Default Background", Toast.LENGTH_LONG);
                error.show();
                OptionsBackground.setImageResource(R.drawable.hearthstone);
        }
    }

    void setSpinners() {
        // Spinners
        Spinner SYDSpinner = (Spinner) findViewById(R.id.SYDSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.SortDeckOptions, R.layout.syd_spinner_item);
        adapter.setDropDownViewResource(R.layout.syd_spinner_dropdown);
        SYDSpinner.setAdapter(adapter);

        Spinner SFDSpinner = (Spinner) findViewById(R.id.SFDSpinner);
        ArrayAdapter<CharSequence> adapterTwo = ArrayAdapter.createFromResource(this,
                R.array.SortDeckOptions, R.layout.syd_spinner_item);
        adapterTwo.setDropDownViewResource(R.layout.syd_spinner_dropdown);
        SFDSpinner.setAdapter(adapterTwo);

        // Set Starting Item
        SharedPreferences sharedPreferences = getSharedPreferences("HearthDeckTracker", Context.MODE_PRIVATE);
        String spinnerOption = sharedPreferences.getString("SYDOPTION", "Alphabetically");
        int spinnerPosition = adapter.getPosition(spinnerOption);
        SYDSpinner.setSelection(spinnerPosition);

        SharedPreferences sharedPreferencesTwo = getSharedPreferences("HearthDeckTracker", Context.MODE_PRIVATE);
        String spinnerOptionTwo = sharedPreferencesTwo.getString("SFDOPTION", "Alphabetically");
        int spinnerPositionTwo = adapterTwo.getPosition(spinnerOptionTwo);
        SFDSpinner.setSelection(spinnerPositionTwo);

        // Spinner Item Selections
        SYDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenItem;

                chosenItem = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = getSharedPreferences("HearthDeckTracker", MODE_PRIVATE).edit();

                switch (chosenItem) {
                    case "Alphabetically":
                        editor.putString("SYDOPTION", "Alphabetically");
                        editor.commit();
                        break;
                    case "Highest Win Rate":
                        editor.putString("SYDOPTION", "Highest Win Rate");
                        editor.commit();
                        break;
                    case "Lowest Win Rate":
                        editor.putString("SYDOPTION", "Lowest Win Rate");
                        editor.commit();
                        break;
                    case "Most Played":
                        editor.putString("SYDOPTION", "Most Played");
                        editor.commit();
                        break;
                    case "Least Played":
                        editor.putString("SYDOPTION", "Least Played");
                        editor.commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SFDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenItem;

                chosenItem = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = getSharedPreferences("HearthDeckTracker", MODE_PRIVATE).edit();

                switch (chosenItem) {
                    case "Alphabetically":
                        editor.putString("SFDOPTION", "Alphabetically");
                        editor.commit();
                        break;
                    case "Highest Win Rate":
                        editor.putString("SFDOPTION", "Highest Win Rate");
                        editor.commit();
                        break;
                    case "Lowest Win Rate":
                        editor.putString("SFDOPTION", "Lowest Win Rate");
                        editor.commit();
                        break;
                    case "Most Played":
                        editor.putString("SFDOPTION", "Most Played");
                        editor.commit();
                        break;
                    case "Least Played":
                        editor.putString("SFDOPTION", "Least Played");
                        editor.commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

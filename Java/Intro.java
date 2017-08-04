package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Intro extends Activity {
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Declare Buttons
        Button Start = (Button) findViewById(R.id.btnStart);
        Button Options = (Button) findViewById(R.id.btnOptions);
        Button Exit = (Button) findViewById(R.id.btnExit);

        // Listener Events for Buttons
        Start.setOnClickListener(toggleStart);
        Options.setOnClickListener(toggleOptions);
        Exit.setOnClickListener(toggleExit);

        getBackground();
         db = new DBAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getBackground();
    }

    private final View.OnClickListener toggleStart =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create FACEDDECKS and RECOMMENDED Table if it does not exist yet]
                    db.open();
                    db.createTable("FACEDDECKS", 1);
                    db.createTable("RECOMMENDED", 3);
                    db.close();

                    Intent x = new Intent(Intro.this, UserDeckDisplay.class);
                    startActivity(x);
                }
            };

    private final View.OnClickListener toggleOptions =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent x = new Intent(Intro.this, Options.class);
                    startActivity(x);
                }
            };

    private final View.OnClickListener toggleExit =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0);
                }
            };

    void getBackground() {
        // Declare Image for Background
        ImageView IntroBackground = (ImageView) findViewById(R.id.imgBackground);

        // Load Background
        SharedPreferences sharedPreferences = getSharedPreferences("HearthDeckTracker", Context.MODE_PRIVATE);
        int ActivityBackground = sharedPreferences.getInt("APPBACKGROUND", 0);

        switch (ActivityBackground) {
            case 0:
                IntroBackground.setImageResource(R.drawable.hearthstone);
                break;
            case 1:
                IntroBackground.setImageResource(R.drawable.leeroy);
                break;
            case 2:
                IntroBackground.setImageResource(R.drawable.hearthstonetwo);
                break;
            default:
                Toast error = Toast.makeText(this.getApplication(), "Background Could Not Be Found, \n" +
                        "Loading Default Background", Toast.LENGTH_LONG);
                error.show();
                IntroBackground.setImageResource(R.drawable.hearthstone);
        }
    }
}


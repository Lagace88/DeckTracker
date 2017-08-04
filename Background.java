package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Background extends Activity {

    String Pref = "HearthDeckTracker";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.background);

        // Declare Image Buttons
        ImageButton hearthstone = (ImageButton) findViewById(R.id.ibHearthstone);
        ImageButton leeroy = (ImageButton) findViewById(R.id.ibLeeroy);
        ImageButton hearthstonetwo = (ImageButton) findViewById(R.id.ibHearthstonetwo);

        // Listener Events for Image Buttons
        hearthstone.setOnClickListener(toggleHearthstone);
        leeroy.setOnClickListener(toggleLeeroy);
        hearthstonetwo.setOnClickListener(toggleHearthstonetwo);
    }

    // Image Button toggles
    public final View.OnClickListener toggleHearthstone =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getSharedPreferences(Pref, MODE_PRIVATE).edit();
                    editor.putInt("APPBACKGROUND", 0);
                    editor.commit();
                    finish();
                }
            };

    private final View.OnClickListener toggleLeeroy =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getSharedPreferences(Pref, MODE_PRIVATE).edit();
                    editor.putInt("APPBACKGROUND", 1);
                    editor.commit();
                    finish();
                }
            };

    private final View.OnClickListener toggleHearthstonetwo =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getSharedPreferences(Pref, MODE_PRIVATE).edit();
                    editor.putInt("APPBACKGROUND", 2);
                    editor.commit();
                    finish();
                }
            };
}

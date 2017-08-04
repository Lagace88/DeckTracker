package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ColorSelection extends Activity {
    // Global
    String Color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selection);

        // Declare Image Buttons
        ImageButton Black = (ImageButton) findViewById(R.id.ibBlack);
        ImageButton Green = (ImageButton) findViewById(R.id.ibGreen);
        ImageButton Red = (ImageButton) findViewById(R.id.ibRed);
        ImageButton Purple = (ImageButton) findViewById(R.id.ibPurple);
        ImageButton Blue = (ImageButton) findViewById(R.id.ibBlue);
        ImageButton Yellow = (ImageButton) findViewById(R.id.ibYellow);
        ImageButton Cyan = (ImageButton) findViewById(R.id.ibCyan);
        ImageButton Orange = (ImageButton) findViewById(R.id.ibOrange);
        ImageButton Pink = (ImageButton) findViewById(R.id.ibPink);

        // Declare Image Button Listeners
        Black.setOnClickListener(toggleBlack);
        Green.setOnClickListener(toggleGreen);
        Red.setOnClickListener(toggleRed);
        Purple.setOnClickListener(togglePurple);
        Blue.setOnClickListener(toggleBlue);
        Yellow.setOnClickListener(toggleYellow);
        Cyan.setOnClickListener(toggleCyan);
        Orange.setOnClickListener(toggleOrange);
        Pink.setOnClickListener(togglePink);
    }

    private final View.OnClickListener toggleBlack =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#000000";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener toggleGreen =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#056600";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener toggleRed =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#a70308";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener togglePurple =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#a602b2";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener toggleBlue =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#0418ad";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener toggleYellow =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#ece800";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener toggleCyan =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#02b29b";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener toggleOrange =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#f07924";
                    sendColorBack(Color);
                }
            };

    private final View.OnClickListener togglePink =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color = "#fe348c";
                    sendColorBack(Color);
                }
            };

    void sendColorBack(String Color) {
        Intent data = new Intent();
        data.putExtra("selectedColor", Color);
        setResult(RESULT_OK, data);
        finish();
    }
}

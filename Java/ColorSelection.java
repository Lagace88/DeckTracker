package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ColorSelection extends Activity {
    // Global
    int[] Color = new int[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_selection);

        // Declare Image Buttons
        ImageButton Brown = findViewById(R.id.ibBlack);
        ImageButton Green = findViewById(R.id.ibGreen);
        ImageButton Red = findViewById(R.id.ibRed);
        ImageButton Purple = findViewById(R.id.ibPurple);
        ImageButton Blue = findViewById(R.id.ibBlue);
        ImageButton Yellow = findViewById(R.id.ibYellow);
        ImageButton Cyan = findViewById(R.id.ibCyan);
        ImageButton Orange = findViewById(R.id.ibOrange);
        ImageButton Pink = findViewById(R.id.ibPink);

        // Declare Image Button Listeners
        Brown.setOnClickListener(toggleBrown);
        Green.setOnClickListener(toggleGreen);
        Red.setOnClickListener(toggleRed);
        Purple.setOnClickListener(togglePurple);
        Blue.setOnClickListener(toggleBlue);
        Yellow.setOnClickListener(toggleYellow);
        Cyan.setOnClickListener(toggleCyan);
        Orange.setOnClickListener(toggleOrange);
        Pink.setOnClickListener(togglePink);
    }

    private final View.OnClickListener toggleBrown =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 121;
                    Color[1] = 85;
                    Color[2] = 72;
                    sendColorBack();
                }
            };

    private final View.OnClickListener toggleGreen =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 5;
                    Color[1] = 102;
                    Color[2] = 0;
                    sendColorBack();
                }
            };

    private final View.OnClickListener toggleRed =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 167;
                    Color[1] = 3;
                    Color[2] = 8;
                    sendColorBack();
                }
            };

    private final View.OnClickListener togglePurple =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 166;
                    Color[1] = 2;
                    Color[2] = 178;
                    sendColorBack();
                }
            };

    private final View.OnClickListener toggleBlue =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 4;
                    Color[1] = 24;
                    Color[2] = 173;
                    sendColorBack();
                }
            };

    private final View.OnClickListener toggleYellow =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 236;
                    Color[1] = 232;
                    Color[2] = 0;
                    sendColorBack();
                }
            };

    private final View.OnClickListener toggleCyan =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 2;
                    Color[1] = 178;
                    Color[2] = 155;
                    sendColorBack();
                }
            };

    private final View.OnClickListener toggleOrange =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 240;
                    Color[1] = 121;
                    Color[2] = 36;
                    sendColorBack();
                }
            };

    private final View.OnClickListener togglePink =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Color[0] = 254;
                    Color[1] = 52;
                    Color[2] = 140;
                    sendColorBack();
                }
            };

    void sendColorBack() {
        Intent data = new Intent();
        data.putExtra("Color", Color);
        setResult(RESULT_OK, data);
        finish();
    }
}

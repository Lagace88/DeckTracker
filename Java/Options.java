package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Options extends Activity {
    EditText pastRecommended;
    SharedPreferences sP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //Declare Buttons
        Button Back = (Button) findViewById(R.id.btnBack);
        pastRecommended = (EditText) findViewById(R.id.editPastRecommended);

        // Listener Events for Buttons
        Back.setOnClickListener(toggleBack);

        // Fill in pastRecommended
        sP = getApplicationContext().getSharedPreferences("DTPPref", 0);
        pastRecommended.setText(Integer.toString(sP.getInt("PastRecommended", 100)));
    }

    @Override
    protected void onResume(){
        super.onResume();
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


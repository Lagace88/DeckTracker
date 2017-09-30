package com.tool.dirtytgaming.decktrackerpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

public class CreateColor extends Activity {
    int red;
    int green;
    int blue;

    EditText etRed;
    EditText etGreen;
    EditText etBlue;

    SeekBar sbRed;
    SeekBar sbGreen;
    SeekBar sbBlue;

    ImageView imgCreateColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_color);

        // Declare Buttons
        Button done = findViewById(R.id.btnCreateColorDone);
        Button chooseColor = findViewById(R.id.btnSelectColor);
        Button cancel = findViewById(R.id.btnCreateColorCancel);

        // Set OnClick
        done.setOnClickListener(toggleDone);
        chooseColor.setOnClickListener(toggleSelectColor);
        cancel.setOnClickListener(toggleCancel);

        seekBarOnChange();
        editTextOnChange();
    }

    // Button OnClicks
    View.OnClickListener toggleDone =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent data = new Intent();
                    data.putExtra("red", red);
                    data.putExtra("green", green);
                    data.putExtra("blue", blue);
                    setResult(RESULT_OK, data);
                    finish();
                }
            };

    View.OnClickListener toggleSelectColor =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CreateColor.this, ColorSelection.class);
                    startActivityForResult(intent, 1);
                }
            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int Colors[] = data.getIntArrayExtra("Color");

        etRed.setText(Integer.toString(Colors[0]));
        etGreen.setText(Integer.toString(Colors[1]));
        etBlue.setText(Integer.toString(Colors[2]));
    }

    View.OnClickListener toggleCancel =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            };

    // SeekBar OnChanges
    public void seekBarOnChange() {
        // Declare Seek Bars
        sbRed = findViewById(R.id.sbRed);
        sbGreen = findViewById(R.id.sbGreen);
        sbBlue = findViewById(R.id.sbBlue);

        // Declare Edit Text
        etRed = findViewById(R.id.editRed);
        etGreen = findViewById(R.id.editGreen);
        etBlue = findViewById(R.id.editBlue);

        sbRed.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        etRed.setText(Integer.toString(i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        sbGreen.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        etGreen.setText(Integer.toString(i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        sbBlue.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        etBlue.setText(Integer.toString(i));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );
    }

    // EditText TextChangeListeners
    public void editTextOnChange() {

        imgCreateColor = findViewById(R.id.imgCreateColor);
        imgCreateColor.setBackgroundColor(Color.rgb(Integer.parseInt(etRed.getText().toString()), Integer.parseInt(etGreen.getText().toString()),
                Integer.parseInt(etBlue.getText().toString())));

        etRed.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!etRed.getText().toString().equals("")) {
                            if (Integer.parseInt(etRed.getText().toString()) > 255) {
                                etRed.setText("255");
                            } else if (Integer.parseInt(etRed.getText().toString()) < 0) {
                                etRed.setText("0");
                            }
                            red = Integer.parseInt(etRed.getText().toString());
                            sbRed.setProgress(red);
                            imgCreateColor.setBackgroundColor(Color.rgb(red, green, blue));
                        }
                    }
                }
        );

        etGreen.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!etGreen.getText().toString().equals("")) {
                            if (Integer.parseInt(etGreen.getText().toString()) > 255) {
                                etGreen.setText("255");
                            } else if (Integer.parseInt(etGreen.getText().toString()) < 0) {
                                etGreen.setText("0");
                            }
                            green = Integer.parseInt(etGreen.getText().toString());
                            sbGreen.setProgress(green);
                            imgCreateColor.setBackgroundColor(Color.rgb(red, green, blue));
                        }
                    }
                }
        );

        etBlue.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!etBlue.getText().toString().equals("")) {
                            if (Integer.parseInt(etBlue.getText().toString()) > 255) {
                                etBlue.setText("255");
                            } else if (Integer.parseInt(etBlue.getText().toString()) < 0) {
                                etBlue.setText("0");
                            }
                            blue = Integer.parseInt(etBlue.getText().toString());
                            sbBlue.setProgress(blue);
                            imgCreateColor.setBackgroundColor(Color.rgb(red, green, blue));
                        }
                    }
                }
        );
    }
}

package com.example.tyler.hearthstonedecktracker;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeName extends Activity {
    DBAdapter db;
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_deck_name);

        db = new DBAdapter(this);

        // Delcare Buttons
        Button done = (Button) findViewById(R.id.btn_CNDone);
        Button cancel = (Button) findViewById(R.id.btn_CNCancel);

        done.setOnClickListener(toggleDone);
        cancel.setOnClickListener(toggleCancel);

        position = getIntent().getIntExtra("Position", 0);
    }
    private final View.OnClickListener toggleDone =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText newName = (EditText) findViewById(R.id.editNewName);
                    String Name = newName.getText().toString();
                    db.open();

                    Toast toast;

                    // Be sure it isn't blank.
                    if (Name.length() == 0) {
                        toast = Toast.makeText(getApplicationContext(), "The name field cannot be blank!", Toast.LENGTH_LONG);
                        toast.show();
                        // Check length.
                    }else if (Name.length() > 28) {
                    toast = Toast.makeText(getApplicationContext(), "Name cannot be longer than 28 characters!", Toast.LENGTH_LONG);
                    toast.show();
                }
                    // Clean name.
                    // Remove whitespace at the beginning.
                    while (Character.isWhitespace(Name.charAt(0))) {
                        Name = Name.substring(1);
                    }

                    // Remove whitespace at the end.
                    while (Character.isWhitespace(Name.charAt(Name.length() - 1))) {
                        Name = Name.substring(0, Name.length() - 1);
                    }

                    // Remove any multiple spaces.
                    for (int x = 0; x < Name.length() - 1; x++) {
                        if (Character.isWhitespace(Name.charAt(x)) && Character.isWhitespace(Name.charAt(x + 1))) {
                            Name = Name.substring(0, x) + Name.substring(x + 1);
                            x--;
                        }
                    }

                    // Update FACEDDECKS
                    db.updateName(position, Name, "FACEDDECKS");
                    db.close();
                    finish();
                }
            };

    private final View.OnClickListener toggleCancel =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };
}

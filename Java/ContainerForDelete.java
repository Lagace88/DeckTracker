package com.example.tyler.hearthstonedecktracker;

import android.database.Cursor;
import android.widget.Button;
import android.widget.ProgressBar;

public class ContainerForDelete {
    DBAdapter db;
    Cursor recommendedCursor;
    int position;
    ProgressBar deleteBar;
    Button finish;

    ContainerForDelete(DBAdapter db, Cursor recommendedCursor, int position) {
        this.db = db;
        this.recommendedCursor = recommendedCursor;
        this.position = position;
    }
}

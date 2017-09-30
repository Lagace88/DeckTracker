package com.tool.dirtytgaming.decktrackerpro;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ListViewCustomAdapterFacedDecks extends CursorAdapter {
    Configuration configuration;

    public ListViewCustomAdapterFacedDecks(Context context, Cursor cursor, Configuration configuration) {
        super(context, cursor, 0);
        this.configuration = configuration;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.custom_list_item_faced_decks, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView facedDeckName = view.findViewById(R.id.txtFacedDeckName);
        TextView winRate = view.findViewById(R.id.txtWinRate);

        // Info from cursor
        String Name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int Tred = cursor.getInt(cursor.getColumnIndexOrThrow("tred"));
        int Tgreen = cursor.getInt(cursor.getColumnIndexOrThrow("tgreen"));
        int Tblue = cursor.getInt(cursor.getColumnIndexOrThrow("tblue"));
        int Bred = cursor.getInt(cursor.getColumnIndexOrThrow("bred"));
        int Bgreen = cursor.getInt(cursor.getColumnIndexOrThrow("bgreen"));
        int Bblue = cursor.getInt(cursor.getColumnIndexOrThrow("bblue"));
        double Wins = cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
        double Losses = cursor.getInt(cursor.getColumnIndexOrThrow("losses"));
        double WinRate;

        if (Wins + Losses == 0) {
            WinRate = 50;
        } else {
            WinRate = Wins / (Wins + Losses) * 100;
            WinRate = Math.round(WinRate);
        }

        // Change properties
        facedDeckName.setText(Name);


        facedDeckName.setTextColor(Color.rgb(Tred, Tgreen, Tblue));
        facedDeckName.setBackgroundColor(Color.rgb(Bred, Bgreen, Bblue));

        winRate.setText(String.format("%.0f", WinRate) + "%");
        winRate.setTextColor(Color.rgb(Tred, Tgreen, Tblue));
        winRate.setBackgroundColor(Color.rgb(Bred, Bgreen, Bblue));
    }
}


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
        TextView facedDeckName = (TextView) view.findViewById(R.id.txtFacedDeckName);
        TextView winRate = (TextView) view.findViewById(R.id.txtWinRate);

        // Info from cursor
        String Name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String TextColor = cursor.getString(cursor.getColumnIndexOrThrow("textcolor"));
        String BGColor = cursor.getString(cursor.getColumnIndexOrThrow("bgcolor"));
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


        facedDeckName.setTextColor(Color.parseColor(TextColor));
        facedDeckName.setBackgroundColor(Color.parseColor(BGColor));

        winRate.setText(String.format("%.0f", WinRate) + "%");
        winRate.setTextColor(Color.parseColor(TextColor));
        winRate.setBackgroundColor(Color.parseColor(BGColor));
    }
}


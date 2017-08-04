package com.example.tyler.hearthstonedecktracker;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ListViewCustomAdapterUserDecks extends CursorAdapter {

    public ListViewCustomAdapterUserDecks(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.custom_list_item_user_decks, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txt_UserDeck = (TextView) view.findViewById(R.id.txt_UserDeck);
        TextView txt_UserDeckWinRate = (TextView) view.findViewById(R.id.txt_UserDeckWinRate);

        // Info from cursor
        String Name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String TextColorCode = cursor.getString(cursor.getColumnIndexOrThrow("textcolor"));
        String BGColorCode = cursor.getString(cursor.getColumnIndexOrThrow("bgcolor"));

        // Info for win rate.
        int winRate = cursor.getInt(cursor.getColumnIndexOrThrow("winrate"));

        // Change Properties
        txt_UserDeck.setText(Name);
        // Set text size.
        if (Name.length() <= 10) {
            txt_UserDeck.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }else if (Name.length() <= 15) {
            txt_UserDeck.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }else if (Name.length() <= 28) {
            txt_UserDeck.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }
        txt_UserDeck.setTextColor(Color.parseColor(TextColorCode));
        txt_UserDeck.setBackgroundColor(Color.parseColor(BGColorCode));

        txt_UserDeckWinRate.setTextColor(Color.parseColor(TextColorCode));
        txt_UserDeckWinRate.setBackgroundColor(Color.parseColor(BGColorCode));
        txt_UserDeckWinRate.setText(winRate + "%");

    }
}

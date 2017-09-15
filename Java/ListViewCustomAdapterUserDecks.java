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

public class ListViewCustomAdapterUserDecks extends CursorAdapter {
    Configuration configuration;

    public ListViewCustomAdapterUserDecks(Context context, Cursor cursor, Configuration configuration) {
        super(context, cursor, 0);
        this.configuration = configuration;
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
        txt_UserDeck.setTextColor(Color.parseColor(TextColorCode));
        txt_UserDeck.setBackgroundColor(Color.parseColor(BGColorCode));

        txt_UserDeckWinRate.setTextColor(Color.parseColor(TextColorCode));
        txt_UserDeckWinRate.setBackgroundColor(Color.parseColor(BGColorCode));
        txt_UserDeckWinRate.setText(winRate + "%");

        // Set Text size.
        int screenWidthDp = configuration.screenWidthDp;

        if (screenWidthDp <= 359) {
            // Set Percent Size
            txt_UserDeckWinRate.setTextSize(23);

            // Set Name Size
            if (Name.length() <= 10) {
                txt_UserDeck.setTextSize(23);
            } else if (Name.length() <= 17) {
                txt_UserDeck.setTextSize(13);
            } else if (Name.length() <= 28) {
                txt_UserDeck.setTextSize(8);
            }
        } else if (screenWidthDp <= 400) {
            txt_UserDeckWinRate.setTextSize(26);

            if (Name.length() <= 10) {
                txt_UserDeck.setTextSize(26);
            } else if (Name.length() <= 17) {
                txt_UserDeck.setTextSize(15);
            } else if (Name.length() <= 28) {
                txt_UserDeck.setTextSize(9);
            }
        } else if (screenWidthDp <= 599) {
            txt_UserDeckWinRate.setTextSize(31);

            if (Name.length() <= 10) {
                txt_UserDeck.setTextSize(31);
            } else if (Name.length() <= 17) {
                txt_UserDeck.setTextSize(18);
            } else if (Name.length() <= 28) {
                txt_UserDeck.setTextSize(11);
            }
        } else if (screenWidthDp <= 719) {
            txt_UserDeckWinRate.setTextSize(52);

            if (Name.length() <= 10) {
                txt_UserDeck.setTextSize(52);
            } else if (Name.length() <= 17) {
                txt_UserDeck.setTextSize(30);
            } else if (Name.length() <= 28) {
                txt_UserDeck.setTextSize(18);
            }
        } else {
            txt_UserDeckWinRate.setTextSize(61);

            if (Name.length() <= 10) {
                txt_UserDeck.setTextSize(61);
            } else if (Name.length() <= 17) {
                txt_UserDeck.setTextSize(36);
            } else if (Name.length() <= 28) {
                txt_UserDeck.setTextSize(27);
            }
        }

    }
}

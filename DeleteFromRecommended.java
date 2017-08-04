package com.example.tyler.hearthstonedecktracker;

import android.database.Cursor;
import android.os.AsyncTask;

public class DeleteFromRecommended extends AsyncTask<ContainerForDelete, Integer, ContainerForDelete> {

    public interface AsyncResponse {
        void processFinish();
    }

    public AsyncResponse delegate = null;

    public DeleteFromRecommended (AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ContainerForDelete doInBackground(ContainerForDelete... params) {
        DBAdapter db = params[0].db;
        Cursor recommendedCursor = params[0].recommendedCursor;
        int position = params[0].position;

        db.open();
        recommendedCursor.moveToFirst();
        int y = 0;
        int numberOfEntriesRemoved = 0;

        for (int x = 0; x < recommendedCursor.getCount(); x++) {
            // See if deck matches the deck we are deleting.
            if (recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) == position) {
                // Bring down each deck after.
                for (y = x + 1; y < recommendedCursor.getCount(); y++) {
                    recommendedCursor.moveToNext();
                    db.updateRecommended(recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")), y);
                }
                // Update cursor1
                recommendedCursor = db.getAllRows("RECOMMENDED", 3);

                // Move to one position behind to make up for move to next.
                recommendedCursor.moveToPosition(x - 1);

                // Reset x
                x = x - 1;

                numberOfEntriesRemoved++;
            }
            recommendedCursor.moveToNext();
        }

        // Assign 0 to number of spots now free to allow win/loss to know it is open.
        for (int x = numberOfEntriesRemoved; x > 0; x--) {
            db.updateRecommended(0, recommendedCursor.getCount() + 1 - x);
        }

        // Lower each deck number by 1 that is above position.
        recommendedCursor.moveToFirst();
        for (int x = 0; x < recommendedCursor.getCount(); x++) {
            if (recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) > position &&
                    recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) != 0) {
                db.updateRecommended(recommendedCursor.getInt(recommendedCursor.getColumnIndexOrThrow("deck")) - 1, x + 1);
            }
            recommendedCursor.moveToNext();
        }

        db.close();
        return params[0];
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate();
        // Update progress bar.

    }

    @Override
    protected void onPostExecute(ContainerForDelete result) {
        super.onPostExecute(result);
        // Remove progress bar.

    }
}

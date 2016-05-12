package io.github.maximgorbatyuk.vkclient.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.Constants;

/**
 * Created by Maxim on 12.05.2016.
 */
public class GetRecordList extends AsyncTask<String, Void, List<Audio>> {

    IGetResult delegate;
    DBHelper helper;

    public GetRecordList(Context context, IGetResult delegate){
        this.delegate = delegate;
        helper = new DBHelper(context);
    }

    @Override
    protected List<Audio> doInBackground(String... params) {

        List<Audio> list = new ArrayList<>(0);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = null;

        try {
            db.beginTransaction();
            cursor = db.query(Constants.TABLE_NAME, null, null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    Audio audio = new Audio();
                    audio.id = cursor.getInt(cursor.getColumnIndex("id"));
                    audio.title = cursor.getString(cursor.getColumnIndex(Constants.TITLE_COLUMN));
                    audio.artist = cursor.getString(cursor.getColumnIndex(Constants.ARTIST_COLUMN));
                    audio.duration = cursor.getInt(cursor.getColumnIndex(Constants.DURATION_COLUMN));
                    audio.url = cursor.getString(cursor.getColumnIndex(Constants.URL_COLUMN));
                    audio.lyrics_id = cursor.getInt(cursor.getColumnIndex(Constants.LYRICS_COLUMN));

                    list.add(audio);
                } while (cursor.moveToNext());
                db.setTransactionSuccessful();
            }
        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, ex.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.endTransaction();
            db.close();
            helper.close();
        }

        return list;
    }

    @Override
    protected void onPostExecute(List<Audio> audios) {
        super.onPostExecute(audios);
        delegate.onExecute(audios);
    }
}

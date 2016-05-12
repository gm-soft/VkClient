package io.github.maximgorbatyuk.vkclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.Constants;

/**
 * Created by Maxim on 12.05.2016.
 */
public class AddRecord extends AsyncTask<Audio, Void, Integer> {

    IExecuteResult delegate;
    DBHelper helper;

    public AddRecord(Context context, IExecuteResult delegate){
        this.delegate = delegate;
        helper = new DBHelper(context);
    }

    @Override
    protected Integer doInBackground(Audio... params) {

        long count = 0;
        for (Audio param : params) {
            ContentValues values = getContentValues( param );
            SQLiteDatabase database = helper.getWritableDatabase();
            try {
                database.beginTransaction();
                count += database.insert(Constants.TABLE_NAME, null, values);
                database.setTransactionSuccessful();
            } catch (Exception ex) {
                Log.d(Constants.LOG_TAG, "Error while adding record: " + ex.getMessage());
            } finally {
                database.endTransaction();
                database.close();
                helper.close();
            }
        }

        return (int) count;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        delegate.onExecute(result);
    }

    private ContentValues getContentValues(Audio record){
        ContentValues result = new ContentValues();
        result.put(Constants.ID_COLUMN,         record.id);
        result.put(Constants.TITLE_COLUMN,      record.title);
        result.put(Constants.ARTIST_COLUMN,     record.artist);
        result.put(Constants.DURATION_COLUMN,   record.duration);
        result.put(Constants.URL_COLUMN,        record.url);
        result.put(Constants.LYRICS_COLUMN,     record.lyrics_id);

        return result;
    }
}

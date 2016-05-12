package io.github.maximgorbatyuk.vkclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import io.github.maximgorbatyuk.vkclient.help.Audio;
import io.github.maximgorbatyuk.vkclient.help.Constants;

/**
 * Created by Maxim on 12.05.2016.
 */
public class UpdateRecord extends AsyncTask<Audio, Void, Integer> {

    IExecuteResult delegate;
    DBHelper helper;

    public UpdateRecord(Context context, IExecuteResult delegate){
        this.delegate = delegate;
        helper = new DBHelper(context);
    }

    @Override
    protected Integer doInBackground(Audio... params) {

        long count = 0;
        ContentValues values = getContentValues(params[0]);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.beginTransaction();
            count = db.update(Constants.TABLE_NAME, values, "id=?", new String[] { "" + params[0].id});
            db.setTransactionSuccessful();
        } catch (Exception ex){
            Log.d(Constants.LOG_TAG, "Error while record updating: " + ex.getMessage());
            count = 0;
        }
        finally {
            db.endTransaction();
            db.close();
            helper.close();
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
        result.put(Constants.TITLE_COLUMN, record.title);
        result.put(Constants.ARTIST_COLUMN, record.artist);
        result.put(Constants.DURATION_COLUMN, record.duration);
        result.put(Constants.URL_COLUMN, record.url);
        result.put(Constants.LYRICS_COLUMN, record.lyrics_id);

        return result;
    }
}

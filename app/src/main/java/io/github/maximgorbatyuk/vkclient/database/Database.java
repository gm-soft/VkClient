package io.github.maximgorbatyuk.vkclient.database;

import android.content.ContentValues;
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
 * Created by Maxim on 13.05.2016.
 */
public class Database {

    Context         context;
    DBHelper        helper;
    // IExecuteResult  iExecuteResult;

    public Database(Context context){
        this.context = context;
        helper = new DBHelper(context);
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

    public void addRecord(Audio[] params, IExecuteResult delegate){
        new AddRecord(delegate).execute(params);
    }

    public void updateRecord(Audio[] params, IExecuteResult delegate){
        new UpdateRecord(delegate).execute(params);
    }

    public void removeRecord(Audio[] params, IExecuteResult delegate){
        new RemoveRecord(delegate).execute(params);
    }

    public void getRecordList(IExecuteResult delegate){
        new GetRecordList(delegate).execute();
    }

    // ------------------------
    // Classes of realization
    private class AddRecord     extends AsyncTask<Audio, Void, Integer> {

        IExecuteResult delegate;

        public AddRecord(IExecuteResult delegate){
            this.delegate = delegate;
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
    }
    private class UpdateRecord  extends AsyncTask<Audio, Void, Integer> {

        IExecuteResult delegate;

        public UpdateRecord(IExecuteResult delegate){
            this.delegate = delegate;
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
    }
    private class RemoveRecord  extends AsyncTask<Audio, Void, Integer> {

        private IExecuteResult delegate;

        public RemoveRecord(IExecuteResult delegate){
            this.delegate = delegate;
        }


        @Override
        protected Integer doInBackground(Audio... params) {

            long count = 0;
            SQLiteDatabase db = helper.getWritableDatabase();

            try {
                db.beginTransaction();
                count = db.delete(Constants.TABLE_NAME, "id=?", new String[] {"" + + params[0].id});
                db.setTransactionSuccessful();
            } catch (Exception ex){
                Log.d(Constants.LOG_TAG, "Error while removing record: " + ex.getMessage());
                count = 0;
            } finally {
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
    }
    public class GetRecordList extends AsyncTask<String, Void, List<Audio>> {

        IExecuteResult delegate;

        public GetRecordList(IExecuteResult delegate){
            this.delegate = delegate;
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
}

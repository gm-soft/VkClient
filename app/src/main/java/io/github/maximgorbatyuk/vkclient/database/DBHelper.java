package io.github.maximgorbatyuk.vkclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.maximgorbatyuk.vkclient.help.Constants;

/**
 * Created by Maxim on 12.05.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    Context context;
    String DROP_TABLE  = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;
    String CREATE_TABLE =
            "CREATE TABLE " + Constants.TABLE_NAME +
                    "( " +
                    Constants.ID_COLUMN           + " INTEGER PRIMARY KEY, " +
                    Constants.TITLE_COLUMN        + " TEXT, " +
                    Constants.ARTIST_COLUMN       + " TEXT, " +
                    Constants.DURATION_COLUMN     + " INTEGER, " +
                    Constants.URL_COLUMN          + " TEXT, " +
                    Constants.LYRICS_COLUMN       + " INTEGER" +
                    " )";

    public DBHelper(Context context){
        super(context, Constants.DATABASE_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}

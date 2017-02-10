package net.sramanovich.fitnessday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Fitness";
    private static DBHelper mDBHelper;
    private static Context mContext;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    public static DBHelper create(Context context) {
        if(mDBHelper==null) {
            mDBHelper = new DBHelper(context);
        }

        return mDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ExercisesTable.CREATE_TABLE_SQL);
        ExercisesTable.initData(db, mContext);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

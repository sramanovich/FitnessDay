package net.sramanovich.fitnessday.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.sramanovich.fitnessday.Constants;

class DBHelper extends SQLiteOpenHelper {
    private static DBHelper mDBHelper;
    private static Context mContext;

    private DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
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
        db.execSQL(TrainingProgramTable.CREATE_TABLE_SQL);
        ExercisesTable.initData(db, mContext);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package net.sramanovich.fitnessday;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBEngine {
    private static Context mContext;
    private static DBHelper mDBHelper;

    private DBEngine() {
    }

    public static void init(Context context) {
        if( mDBHelper == null ) {
            mDBHelper = DBHelper.create(context);
        }
    }

    public static SQLiteDatabase getWritableDatabase() {
        return mDBHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase() {
        return mDBHelper.getReadableDatabase();
    }

     public static Cursor getExercisesCursor() {
         return getReadableDatabase().query(ExercisesTable.DB_TABLE_NAME, null, null, null, null, null, null);
    }
}

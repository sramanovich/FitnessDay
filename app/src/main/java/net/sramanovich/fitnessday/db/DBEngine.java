package net.sramanovich.fitnessday.db;

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

    public static Cursor getTrainingProgramCursor(int isTemplate) {
        return getTrainingProgramCursor(isTemplate, "");
    }

    public static Cursor getTrainingProgramCursor(int isTemplate, String prgNameFilter) {
        StringBuilder additionalQuery = new StringBuilder();
        if( isTemplate >= 0 ) {
            additionalQuery.append(TrainingProgramTable.COL_IS_TEMPLATE);
            additionalQuery.append("=");
            additionalQuery.append(isTemplate);
        } else {
            additionalQuery.setLength(0);
        }

        if( !prgNameFilter.isEmpty() ) {
            if( !additionalQuery.toString().isEmpty() ) {
                additionalQuery.append(" AND ");
                additionalQuery.append(TrainingProgramTable.COL_NAME);
                additionalQuery.append(" LIKE '%");
                additionalQuery.append(prgNameFilter);
                additionalQuery.append("%'");
            }

        }

        return getReadableDatabase().query(TrainingProgramTable.DB_TABLE_NAME, null, additionalQuery.toString(), null, null, null, null);
    }
}

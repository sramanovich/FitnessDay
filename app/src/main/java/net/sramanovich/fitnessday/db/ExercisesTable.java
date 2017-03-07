package net.sramanovich.fitnessday.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.*;

import net.sramanovich.fitnessday.R;

public class ExercisesTable {
    public static final String DB_TABLE_NAME = "Exercises";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "NAME";
    public static final String COL_NOTE = "NOTE";
    public static final String COL_USED = "USED";
    public static final String COL_SPLIT_NR = "SPLIT_NR";
    public static final String COL_TYPE = "TYPE";
    public static final String COL_BODY_PART = "BODY_PART";
    public static final String CREATE_TABLE_SQL =
            "create table "
                    + DB_TABLE_NAME + " ("
                    + COL_ID + " integer primary key autoincrement,"
                    + COL_NAME + " text,"
                    + COL_USED + " integer,"
                    + COL_SPLIT_NR + " integer,"
                    + COL_TYPE + " integer,"
                    + COL_BODY_PART + " integer,"
                    + COL_NOTE + " text"
                    + ")";

    public enum ExriciseType {
        EX_BODY_WEIGHT(0, "Body"),
        EX_BAR(1, "With barbell"),
        EX_DUMBBELL(2, "With dumbbell"),
        EX_CARDIO(2, "Cardio");

        private final int number;

        private final String name;

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        private ExriciseType(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public static ExriciseType fromId(int id) {
            for (ExriciseType type : ExriciseType.values()) {
                if (type.getNumber() == id) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum BodyPart {
        BODY_PART_ALL(0, "All"),
        BODY_PART_ABDOMINALS(1, "Abdominals"),
        BODY_PART_BICEPS(2, "Biceps"),
        BODY_PART_CALVES(3, "Calves"),
        BODY_PART_CHEST(4, "Chest"),
        BODY_PART_FOREARMS(5, "Forearms"),
        BODY_PART_GLUTES(6, "Glutes"),
        BODY_PART_HAMSTRINGS(7, "Hamstrings"),
        BODY_PART_LATS(8, "Lats"),
        BODY_PART_LOWER_BACK(9, "Lower back"),
        BODY_PART_QUADS(10, "Quads"),
        BODY_PART_SHOULDERS(11, "Shoulders"),
        BODY_PART_TRAPS(12, "Traps"),
        BODY_PART_TRICEPS(13, "Triceps");

        private final int number;

        private final String name;

        public int getNumber() {
            return number;
        }

        public String getName() {
            return name;
        }

        private BodyPart(int number, String name) {
            this.number = number;
            this.name = name;
        }

        public static BodyPart fromId(int id) {
            for (BodyPart type : BodyPart.values()) {
                if (type.getNumber() == id) {
                    return type;
                }
            }
            return null;
        }
    }

    public static void initData(SQLiteDatabase db, Context ctx) {
        insertRow(db, ctx.getString(R.string.exercise_1), ExriciseType.EX_BODY_WEIGHT, "", BodyPart.BODY_PART_TRICEPS);
        insertRow(db, ctx.getString(R.string.exercise_2), ExriciseType.EX_BODY_WEIGHT, "", BodyPart.BODY_PART_CHEST);
        insertRow(db, ctx.getString(R.string.exercise_3), ExriciseType.EX_BODY_WEIGHT, "", BodyPart.BODY_PART_LATS);
        insertRow(db, ctx.getString(R.string.exercise_4), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_QUADS);
        insertRow(db, ctx.getString(R.string.exercise_5), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_QUADS);
        insertRow(db, ctx.getString(R.string.exercise_6), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_BICEPS);
        insertRow(db, ctx.getString(R.string.exercise_7), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_TRICEPS);
        insertRow(db, ctx.getString(R.string.exercise_8), ExriciseType.EX_DUMBBELL, "", BodyPart.BODY_PART_CHEST);
        insertRow(db, ctx.getString(R.string.exercise_9), ExriciseType.EX_DUMBBELL, "", BodyPart.BODY_PART_CHEST);
        insertRow(db, ctx.getString(R.string.exercise_10), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_CHEST);
        insertRow(db, ctx.getString(R.string.exercise_11), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_LOWER_BACK);
        insertRow(db, ctx.getString(R.string.exercise_12), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_HAMSTRINGS);
        insertRow(db, ctx.getString(R.string.exercise_13), ExriciseType.EX_BAR, "", BodyPart.BODY_PART_LATS);
        insertRow(db, ctx.getString(R.string.exercise_14), ExriciseType.EX_DUMBBELL, "", BodyPart.BODY_PART_SHOULDERS);
        insertRow(db, ctx.getString(R.string.exercise_15), ExriciseType.EX_DUMBBELL, "", BodyPart.BODY_PART_SHOULDERS);
        insertRow(db, ctx.getString(R.string.exercise_16), ExriciseType.EX_BODY_WEIGHT, "", BodyPart.BODY_PART_SHOULDERS);
    }

    private static boolean insertRow(SQLiteDatabase db, String name, ExriciseType type, String note, BodyPart body_part) {
        ContentValues contValues = new ContentValues();
        contValues.put(COL_NAME, name);
        contValues.put(COL_TYPE, type.ordinal());
        contValues.put(COL_NOTE, note);
        contValues.put(COL_USED, 0);
        contValues.put(COL_SPLIT_NR, 0);
        contValues.put(COL_BODY_PART, body_part.ordinal());
        db.insert(DB_TABLE_NAME, null, contValues);

        return true;
    }

    public static void resetUsedFlag() {

        ContentValues cvSetValues = new ContentValues();
        cvSetValues.put(COL_USED, 0);

        try {
               DBEngine.getWritableDatabase().update(DB_TABLE_NAME, cvSetValues, null, null);
        } catch (SQLiteException e) {
            Log.v("Database:", e.getMessage());
        }
    }

    public static void writeData(long db_id, String name, ExriciseType type, String note) {

        ContentValues cvSetValues = new ContentValues();
        cvSetValues.put(COL_NAME, name);
        if (type != null) {
            cvSetValues.put(COL_TYPE, type.getNumber());
        }
        cvSetValues.put(COL_NOTE, note);

        try {
            if (db_id > 0) {
                DBEngine.getWritableDatabase().update(DB_TABLE_NAME, cvSetValues, "_id=?", new String[]{Long.toString(db_id)});
            } else {
                DBEngine.getWritableDatabase().insert(DB_TABLE_NAME, null, cvSetValues);
            }
        } catch (SQLiteException e) {
            Log.v("Database:", e.getMessage());
        }
    }

    public static ExercisesCursorAdapter getExercisesCursorAdapter(Context context, int layout,
                                                                   Cursor c, String[] from, int[] to, int flags) {
        return new ExercisesCursorAdapter(context, layout, c, from, to, flags);
    }

    public static NewProgramExercisesCursorAdapter getNewProgramExercisesCursorAdapter(SQLiteDatabase db, Context context, int layout,
                                                                                       Cursor c, String[] from, int[] to, int flags) {
        return new NewProgramExercisesCursorAdapter(db, context, layout, c, from, to, flags);
    }
}

class NewProgramExercisesCursorAdapter extends SimpleCursorAdapter {

    private SQLiteDatabase mDB;

    public NewProgramExercisesCursorAdapter(SQLiteDatabase db, Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mDB = db;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int db_id = cursor.getInt(cursor.getColumnIndex(ExercisesTable.COL_ID));
        int isChecked = cursor.getInt(cursor.getColumnIndex(ExercisesTable.COL_USED));
        String name = cursor.getString(cursor.getColumnIndex(ExercisesTable.COL_NAME));
        int typeId = cursor.getInt(cursor.getColumnIndex(ExercisesTable.COL_TYPE));
        ExercisesTable.ExriciseType type = ExercisesTable.ExriciseType.fromId(typeId);
        Integer splitNr = new Integer(cursor.getInt(cursor.getColumnIndex(ExercisesTable.COL_SPLIT_NR)));

        CheckBox chBox = (CheckBox) view.findViewById(R.id.checkBoxIsUsed);
        chBox.setOnCheckedChangeListener(myCheckChangeListener);
        chBox.setTag(db_id);
        chBox.setChecked(isChecked > 0);

        TextView tvName = (TextView) view.findViewById(R.id.textViewProgramExerciseName);
        tvName.setText(name);

        Button btnSplitNr = (Button) view.findViewById(R.id.buttonSplitNumber);
        if(btnSplitNr!=null) {
            btnSplitNr.setTag(splitNr);
            btnSplitNr.setOnClickListener(new ButtonClickListener(db_id));
            setButtonValue(btnSplitNr, splitNr);
        }

        /*ImageView imgViewType = (ImageView) view.findViewById(R.id.imageViewProgramExerciseType);

        switch (type.getNumber()) {
            case 0:
                imgViewType.setImageResource(R.drawable.workout);
                break;
            case 1:
                imgViewType.setImageResource(R.drawable.bar);
                break;
            case 2:
                imgViewType.setImageResource(R.drawable.dumbbell);
                break;
            case 3:
                imgViewType.setImageResource(R.drawable.cardio);
                break;
        }*/
    }

    CompoundButton.OnCheckedChangeListener myCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ContentValues cvIsCheckedValues = new ContentValues();
            cvIsCheckedValues.put(ExercisesTable.COL_USED, isChecked);
            try {
                 mDB.update(ExercisesTable.DB_TABLE_NAME, cvIsCheckedValues, "_id=?", new String[]{Integer.toString((int) buttonView.getTag())});
            } catch (SQLiteException e) {
                Log.v("Database:", e.getMessage());
            }
        }
    };

    private void setButtonValue(Button btnSplitNr, int value) {
        Integer splitNr = new Integer(value);
        if (splitNr <= 0) {
            btnSplitNr.setText("-");
        } else {
            btnSplitNr.setText(splitNr.toString());
        }
    }

    class ButtonClickListener implements View.OnClickListener {

        private int db_id=0;

        public ButtonClickListener(int db_id) {
            this.db_id = db_id;
        }

        @Override
        public void onClick(View v) {
            int splitNr = (int)v.getTag()+1;
            v.setTag(splitNr);
            Button btnSplitNr = (Button) v.findViewById(R.id.buttonSplitNumber);
            setButtonValue(btnSplitNr, splitNr);
            ContentValues cvValues = new ContentValues();
            cvValues.put(ExercisesTable.COL_SPLIT_NR, splitNr);
            try {
                mDB.update(ExercisesTable.DB_TABLE_NAME, cvValues, "_id=?", new String[]{Integer.toString(db_id)});
            } catch (SQLiteException e) {
                Log.v("Database:", e.getMessage());
            }
        }
    }
}

class ExercisesCursorAdapter extends SimpleCursorAdapter {

    public ExercisesCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(ExercisesTable.COL_NAME));
        int typeId = cursor.getInt(cursor.getColumnIndex(ExercisesTable.COL_TYPE));
        ExercisesTable.ExriciseType type = ExercisesTable.ExriciseType.fromId(typeId);

        TextView tvName = (TextView) view.findViewById(R.id.textViewExerciseListItemName);
        tvName.setText(name);

        ImageView imgViewType = (ImageView) view.findViewById(R.id.imgViewExerciseListItemIcon);

        switch (type.getNumber()) {
            case 0:
                imgViewType.setImageResource(R.drawable.workout);
                break;
            case 1:
                imgViewType.setImageResource(R.drawable.bar);
                break;
            case 2:
                imgViewType.setImageResource(R.drawable.dumbbell);
                break;
            case 3:
                imgViewType.setImageResource(R.drawable.cardio);
                break;
        }
    }
};

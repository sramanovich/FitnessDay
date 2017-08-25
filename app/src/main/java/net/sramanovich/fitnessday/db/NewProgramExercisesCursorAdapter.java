package net.sramanovich.fitnessday.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.sramanovich.fitnessday.R;


public class NewProgramExercisesCursorAdapter extends SimpleCursorAdapter {

    private SQLiteDatabase mDB;

    private int bodyPartPositionFilter;

    public NewProgramExercisesCursorAdapter(SQLiteDatabase db, int bodyPartPositionFilter, Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mDB = db;
        this.bodyPartPositionFilter = bodyPartPositionFilter;
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
            btnSplitNr.setOnClickListener(new ButtonClickListener(this, db_id));
            setButtonValue(btnSplitNr, splitNr);
        }

        LinearLayout layoutItemColor = (LinearLayout)view.findViewById(R.id.linLayoutNewProgramItem);

        switch(splitNr) {
            case 1:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground1));
                break;
            case 2:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground2));
                break;
            case 3:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground3));
                break;
            case 4:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground4));
                break;
            case 5:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground5));
                break;
            case 6:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground6));
                break;
            case 7:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackground7));
                break;
            default:
                layoutItemColor.setBackgroundColor(view.getResources().getColor(R.color.colorExerciseListBackgroundDef));
                break;
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

    public void setBodyPartPositionFilter(int bodyPartPositionFilter) {
        this.bodyPartPositionFilter = bodyPartPositionFilter;
    }

    CompoundButton.OnCheckedChangeListener myCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkExercise((int)buttonView.getTag(), isChecked);
        }
    };

    private void checkExercise(int db_id, boolean isChecked) {
        ContentValues cvIsCheckedValues = new ContentValues();
        cvIsCheckedValues.put(ExercisesTable.COL_USED, isChecked);
        try {
            mDB.update(ExercisesTable.DB_TABLE_NAME, cvIsCheckedValues, "_id=?", new String[]{Integer.toString(db_id)});
        } catch (SQLiteException e) {
            Log.v("Database:", e.getMessage());
        }
    }

    private void setSplitNr(int db_id, int splitNr) {

        ContentValues cvValues = new ContentValues();
        cvValues.put(ExercisesTable.COL_SPLIT_NR, splitNr);
        try {
            mDB.update(ExercisesTable.DB_TABLE_NAME, cvValues, "_id=?", new String[]{Integer.toString(db_id)});
        } catch (SQLiteException e) {
            Log.v("Database:", e.getMessage());
        }
    }

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

        private NewProgramExercisesCursorAdapter cursor;

        public ButtonClickListener(NewProgramExercisesCursorAdapter cursor, int db_id) {
            this.cursor = cursor;
            this.db_id = db_id;
        }

        @Override
        public void onClick(View v) {
            boolean isChecked = false;
            int splitNr = (int)v.getTag()+1;
            if (splitNr > 7) {
                splitNr = 0;
            }
            else {
                isChecked = true;
            }


            v.setTag(splitNr);
            Button btnSplitNr = (Button) v.findViewById(R.id.buttonSplitNumber);
            setButtonValue(btnSplitNr, splitNr);

            setSplitNr(db_id, splitNr);
            checkExercise(db_id, isChecked);

            Cursor cursor = DBEngine.getExercisesCursor(bodyPartPositionFilter);
            swapCursor(cursor);
        }
    }
}

package net.sramanovich.fitnessday.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import net.sramanovich.fitnessday.R;
import net.sramanovich.fitnessday.utils.PairSet;
import net.sramanovich.fitnessday.adapters.TrainingProgramListAdapter;
import net.sramanovich.fitnessday.adapters.TrainingProgramViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TrainingProgramTable {
    public static final String DB_TABLE_NAME="TrainingPrograms";
    public static final String COL_ID="_id";
    public static final String COL_NAME="NAME";
    public static final String COL_WEEK_DAY="WEEK_DAY";
    public static final String COL_EXERCISES="EXERCISES";
    public static final String COL_IS_TEMPLATE="TEMPLATE";
    public static final String COL_DATE="DATE";
    public static final String COL_NOTE="NOTE";
    public static final String CREATE_TABLE_SQL =
            "create table "
                    + DB_TABLE_NAME + " ("
                    + COL_ID + " integer primary key autoincrement,"
                    + COL_NAME + " text,"
                    + COL_WEEK_DAY + " integer,"
                    + COL_EXERCISES + " text,"
                    + COL_IS_TEMPLATE + " integer,"
                    + COL_DATE + " integer,"
                    + COL_NOTE +" text"
                    + ")";

    public static final String SEPARATOR_E=";";
    public static final String SEPARATOR_R=",";
    public static final int SET_ITEMS_COUNT=3; // "exercise,reps,weight"
    public static final int SET_ITEMS_EXERCISE_IDX=0;
    public static final int SET_ITEMS_REPS_IDX=1;
    public static final int SET_ITEMS_WEIGHT_IDX=2;

    private static TrainingProgramTable trainingProgramTable;

    private Training training = new Training();

    private TrainingProgramTable() {
    }

    public static TrainingProgramTable getTrainingProgramTable() {
        if( trainingProgramTable == null ) {
            trainingProgramTable = new TrainingProgramTable();
        }

        return trainingProgramTable;
    }

    public long copyFrom(int srcPosition) {
        try {
            //training.init(name);
            training.parseData(srcPosition);
            ContentValues cvSetValues = new ContentValues();
            cvSetValues.put(COL_NAME, training.name);
            cvSetValues.put(COL_IS_TEMPLATE, 1);
            cvSetValues.put(COL_EXERCISES, training.collectExercises());
            Date today = Calendar.getInstance().getTime();
            cvSetValues.put(COL_DATE, today.getTime()/1000);

            long db_id = DBEngine.getWritableDatabase().insert(DB_TABLE_NAME, null, cvSetValues);
            writeData((int)db_id, 1);
            return db_id;
        } catch (SQLiteException e) {
            Log.v("Database:", e.getMessage());
        }

        return 0;
    }

    public long createNew(String name)  {
        try {
            training.init(name);
            Cursor cursorExercise = DBEngine.getExercisesCursor();
            training.createNew(cursorExercise);

            ContentValues cvSetValues = new ContentValues();
            cvSetValues.put(COL_NAME, training.name);
            cvSetValues.put(COL_IS_TEMPLATE, 1);
            Date today = Calendar.getInstance().getTime();
            cvSetValues.put(COL_DATE, today.getTime()/1000);

            long db_id = DBEngine.getWritableDatabase().insert(DB_TABLE_NAME, null, cvSetValues);
            writeData((int)db_id, 1);
            return db_id;
        } catch (SQLiteException e) {
            Log.v("Database:", e.getMessage());
        }

        return 0;
    }

    public void openProgram(int position) {

        training.parseData(position);
    }

    public String getTrainingExercises() {
        return training.collectExercises();
    }
    public void deleteRecord(long db_id) {
        try {
            DBEngine.getWritableDatabase().delete(DB_TABLE_NAME, "_id="+db_id, null);
        } catch (SQLiteException e) {
            Log.v("TrainingProgramTable:", "deleteRecord(), _ID="+db_id);
            return;
        }

    }

    public void setName(String name) {
        training.name = name;
    }

    public void writeData(long db_id, int isTemplate) {
        ContentValues cvSetValues = new ContentValues();
        cvSetValues.put(COL_NAME, training.name);
        cvSetValues.put(COL_NOTE, training.note);
        cvSetValues.put(COL_IS_TEMPLATE, isTemplate);
        cvSetValues.put(COL_EXERCISES, training.collectExercises());
        Date today = Calendar.getInstance(TimeZone.getDefault()).getTime();
        long date = today.getTime()/1000;
        cvSetValues.put(COL_DATE, date);
        Log.v("TrainingProgramTable:", "writeData()," + "name=" + training.name + ", _id="+db_id);
        try {
            if(db_id>0) {
                DBEngine.getWritableDatabase().update(DB_TABLE_NAME, cvSetValues, "_id=?", new String[]{Long.toString(db_id)});
            }
            else {
                Log.v("TrainingProgramTable:", "writeData(), _id="+db_id);
                return;
            }
        } catch (SQLiteException e) {
            Log.v("Database:", e.getMessage());
        }
    }

    public TrainingProgramListAdapter getTrainingProgramAdapter(Context context) {
        return new TrainingProgramListAdapter(context, training.listSets);
    }

    public TrainingProgramListAdapter getTrainingProgramViewAdapter(Context context) {
        return new TrainingProgramViewAdapter(context, training.listSets);
    }

    public ProgramsContentListAdapter getProgramsContentListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        return new ProgramsContentListAdapter(context, layout, c, from, to, flags);
    }

    class ProgramsContentListAdapter extends SimpleCursorAdapter {
        public ProgramsContentListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            String strProgramName = cursor.getString(cursor.getColumnIndex(COL_NAME));
            if(strProgramName==null)
                return;

            TextView tvProgramName = (TextView)view.findViewById(R.id.textViewProgramName);
            tvProgramName.setText(strProgramName);

            int db_time = cursor.getInt(cursor.getColumnIndex(COL_DATE));
            long time = (long)db_time*1000;
            Date date = new Date(time);
            TextView tvProgramDate = (TextView)view.findViewById(R.id.textViewProgramDate);
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            String strDate = df.format(date);
            Log.v("bindVew:", strDate+"("+date+")("+date.getTime()+")");
            tvProgramDate.setText(strDate);
        }
    }

    public class Training {

        public String name;
        public List<TrainingSet> listSets = new ArrayList<>();
        public String note;

        public void init(String name) {
            this.name = name;
            listSets.clear();
        }

        public void createNew(Cursor cursorExercises) {
            listSets.clear();
            while (cursorExercises.moveToNext()) {
                if (cursorExercises.getInt(cursorExercises.getColumnIndex(ExercisesTable.COL_USED)) > 0) {
                    TrainingSet set = new TrainingSet(cursorExercises.getPosition() + 1,
                            cursorExercises.getString(cursorExercises.getColumnIndex(ExercisesTable.COL_NAME)));
                    listSets.add(set);
                }
            }
        }

        public boolean parseData(int position) {
            Cursor cursorPrograms = DBEngine.getTrainingProgramCursor(-1);
            if(!cursorPrograms.moveToPosition(position)) {
                return false;
            }
            Cursor cursorExercises = DBEngine.getExercisesCursor();
            name = cursorPrograms.getString(cursorPrograms.getColumnIndex(COL_NAME));
            note = cursorPrograms.getString(cursorPrograms.getColumnIndex(COL_NOTE));
            String colExercisesValue = cursorPrograms.getString(cursorPrograms.getColumnIndex(COL_EXERCISES));

            listSets.clear();

            if(colExercisesValue!=null) {
                String exercises[] = colExercisesValue.split(SEPARATOR_E);
                for (int e = 0; e < exercises.length; e++) {
                    String sets[] = exercises[e].split(SEPARATOR_R);
                    if (sets.length < SET_ITEMS_COUNT && !exercises[e].isEmpty()) {
                        Integer id = new Integer(exercises[e]);
                        TrainingSet trSet = new TrainingSet(id, "");
                        if(cursorExercises.moveToPosition(id-1)) {
                            trSet.exercise_name = cursorExercises.getString(cursorExercises.getColumnIndex(ExercisesTable.COL_NAME));
                        }
                        listSets.add(trSet);
                        continue;
                    }

                    int exercise_id = new Integer(sets[SET_ITEMS_EXERCISE_IDX]);
                    String exercise_name="";
                    if(cursorExercises.moveToPosition(exercise_id-1)) {
                        exercise_name = cursorExercises.getString(cursorExercises.getColumnIndex(ExercisesTable.COL_NAME));
                    }
                    TrainingSet trSet = new TrainingSet(exercise_id, exercise_name);
                    int location = listSets.indexOf(trSet);
                    if(location>=0) {
                        trSet = listSets.get(location);
                    }

                    trSet.add(new Integer(sets[SET_ITEMS_REPS_IDX]), new Double(sets[SET_ITEMS_WEIGHT_IDX]));

                    if(location<0) {
                        listSets.add(trSet);
                    }
                    else {
                        listSets.set(location, trSet);
                    }
                }
            }

            return true;
        }

        public String collectExercises() {
            StringBuilder setExercise = new StringBuilder();
            for(TrainingSet trainingSet: listSets) {
                if(!trainingSet.setList.isEmpty()) {
                    for(PairSet set: trainingSet.setList) {
                        setExercise.append(trainingSet.exercise_id);
                        setExercise.append(SEPARATOR_R);
                        setExercise.append(set.getReps());
                        setExercise.append(SEPARATOR_R);
                        setExercise.append(set.getWeight());
                        setExercise.append(SEPARATOR_E);
                    }
                }
                else {
                    setExercise.append(trainingSet.exercise_id);
                    setExercise.append(SEPARATOR_E);
                }
            }

            return setExercise.toString();
        }

        public int size() {
            return listSets.size();
        }

        public TrainingSet get(int position) {
            return listSets.get(position);
        }
    }
}

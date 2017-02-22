package net.sramanovich.fitnessday;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import net.sramanovich.fitnessday.adapters.TrainingProgramListAdapter;
import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.TrainingProgramTable;
import net.sramanovich.fitnessday.db.TrainingSet;
import net.sramanovich.fitnessday.utils.DragListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TrainingProgramActivity extends AppCompatActivity{

    private Cursor cursor;

    private TrainingProgramListAdapter adapter;

    private TrainingProgramTable trainingProgram;

    private long db_id=0;

    private int isTemplate=0;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.training_program);

        initToolbar();

        try {
            Intent intent = getIntent();
            db_id = intent.getIntExtra(Constants.INTENT_PARAM_ID, 0);
            isTemplate = intent.getIntExtra(Constants.INTENT_PARAM_IS_TEMPLATE, 0);
            int isViewMode = intent.getIntExtra(Constants.INTENT_PARAM_VIEW_MODE, 0);

            cursor = DBEngine.getTrainingProgramCursor(isTemplate);
            trainingProgram = TrainingProgramTable.getTrainingProgramTable();

            Log.v("Training program:", "onCreate(), IsTemplate="+isTemplate);
            if(isTemplate == Constants.TT_PROGRAM_TEMPLATE) {
                int currPosition = getCursorPositionByID(db_id);
                if(currPosition >= 0) {
                    //trainingProgram.openProgram(cursor.getPosition());
                    //db_id = trainingProgram.createNew("");
                    isTemplate = Constants.TT_USER_PROGRAM_TEMPLATE;
                    db_id = trainingProgram.copyFrom(cursor, currPosition);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.program_name);
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            trainingProgram.setName(input.getText().toString());
                            trainingProgram.writeData(db_id, Constants.TT_USER_PROGRAM_TEMPLATE);
                        }
                    });
                    /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });*/

                    builder.show();
                }
            }
            else
            {
                int currPosition = getCursorPositionByID(db_id);
                if(currPosition >= 0) {
                    if(isViewMode == 0 &&
                            isTemplate == Constants.TT_USER_PROGRAM_TEMPLATE) {
                        isTemplate = Constants.TT_USER_PROGRAM;
                        db_id = trainingProgram.copyFrom(cursor, currPosition);
                        Date today = Calendar.getInstance(TimeZone.getDefault()).getTime();
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String strDate = df.format(today);
                        String newName = trainingProgram.getName() + " - " + strDate;
                        trainingProgram.setName(newName);
                        trainingProgram.writeData(db_id, isTemplate);
                        int newPosition = getCursorPositionByID(db_id);
                        if(newPosition >= 0) {
                            trainingProgram.openProgram(cursor, newPosition);
                        }
                    }
                    else {
                        trainingProgram.openProgram(cursor, currPosition);
                    }
                }
                else
                {
                    Log.v("Training program:", "onCreate(): bad db_id="+db_id);
                    return;
                }
            }

            if(db_id<=0) {
                Toast toast= Toast.makeText(this, "Invalid database table _id:"+db_id, Toast.LENGTH_SHORT);
                toast.show();
                finish();
                return;
            }

            DragListView lvData = (DragListView) findViewById(R.id.trainingListView);

            if(isViewMode>0) {
                adapter = trainingProgram.getTrainingProgramViewAdapter(this);
            }
            else {
                adapter = trainingProgram.getTrainingProgramAdapter(this);
                lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.onOpenExerciseSetActivity(position);
                    }
                });
            }

            lvData.setAdapter(adapter);

        } catch(SQLiteException e) {
            Toast toast= Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    int getCursorPositionByID(long id) {
        //cursorPrograms.moveToFirst();
        cursor = DBEngine.getTrainingProgramCursor(isTemplate);
        while(cursor.moveToNext()) {
            int dbID = cursor.getInt(cursor.getColumnIndex(TrainingProgramTable.COL_ID));
            if( dbID == (int)id ) {
                return cursor.getPosition();
            }
        }

        return -1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("onActivityResult:", ""+data);
        if(data!=null) {
            int position = data.getIntExtra(Constants.INTENT_PARAM_POSITION, -1);
            if(position>=0&&position<adapter.getCount()) {
                TrainingSet trainingSet = adapter.getItem(position);
                if(trainingSet!=null) {
                    trainingSet.setList = data.getParcelableArrayListExtra(Constants.INTENT_PARAM_SET_LIST);
                    adapter.setItem(position, trainingSet);

                    //update data in database
                    trainingProgram.writeData(db_id, isTemplate);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.training_programs);
        //toolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }
}

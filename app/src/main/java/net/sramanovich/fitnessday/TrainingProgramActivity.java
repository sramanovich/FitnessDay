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

public class TrainingProgramActivity extends AppCompatActivity{

    private Cursor cursor;

    private TrainingProgramListAdapter adapter;

    private TrainingProgramTable trainingProgram;

    private long db_id=0;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.training_program);

        initToolbar();

        try {
            cursor = DBEngine.getTrainingProgramCursor(-1);

            trainingProgram = TrainingProgramTable.getTrainingProgramTable();

            Intent intent = getIntent();
            db_id = intent.getIntExtra(Constants.INTENT_PARAM_ID, 0);
            int isTemplate = intent.getIntExtra(Constants.INTENT_PARAM_IS_TEMPLATE, 0);
            int isViewMode = intent.getIntExtra(Constants.INTENT_PARAM_VIEW_MODE, 0);

            if(isTemplate==1) {
                if(moveSetCursorPositionByID(cursor, db_id)) {
                    //trainingProgram.openProgram(cursor.getPosition());
                    //db_id = trainingProgram.createNew("");
                    db_id = trainingProgram.copyFrom(cursor.getPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.program_name);
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            trainingProgram.setName(input.getText().toString());
                            trainingProgram.writeData(db_id, 0);
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
                if(moveSetCursorPositionByID(cursor, db_id)) {
                    trainingProgram.openProgram(cursor.getPosition());
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

    boolean moveSetCursorPositionByID(Cursor cursor, long id) {
        //cursor.moveToFirst();
        while(cursor.moveToNext()) {
            int dbID = cursor.getInt(cursor.getColumnIndex(TrainingProgramTable.COL_ID));
            if( dbID == (int)id ) {
                return true;
            }
        }

        return false;
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
                    trainingProgram.writeData(db_id, 0);
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

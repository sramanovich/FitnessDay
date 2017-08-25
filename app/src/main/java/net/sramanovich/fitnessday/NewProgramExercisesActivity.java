package net.sramanovich.fitnessday;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import net.sramanovich.fitnessday.adapters.BodyPartRecycleViewAdapter;
import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.ExercisesTable;
import net.sramanovich.fitnessday.db.NewProgramExercisesCursorAdapter;
import net.sramanovich.fitnessday.db.TrainingProgramTable;
import net.sramanovich.fitnessday.utils.RecyclerItemClickListener;

/**
 * Activity - exercises list with check boxes
 * to create new program
 * ask program name dialog
 */

public class NewProgramExercisesActivity extends AppCompatActivity {
    private NewProgramExercisesCursorAdapter cursorAdapter;
    private Cursor cursor;
    private Toolbar toolbar;
    private long db_id;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int recycleViewPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_program);

        ExercisesTable.resetUsedFlag();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewBodyParts);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BodyPartRecycleViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, mRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        setFilterByBodyPart(position);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        ListView lvData = (ListView)findViewById(R.id.listExercises);

        Cursor cursor = DBEngine.getExercisesCursor();
        String[] from = new String[]{ExercisesTable.COL_USED, ExercisesTable.COL_NAME, ExercisesTable.COL_SPLIT_NR};
        int[] to = new int[]{R.id.checkBoxIsUsed, R.id.textViewProgramExerciseName, R.id.buttonSplitNumber};
        cursorAdapter = ExercisesTable.getNewProgramExercisesCursorAdapter( DBEngine.getWritableDatabase(), recycleViewPosition, this,
                                                                            R.layout.new_program_exercises_list_item, cursor, from, to, 0);
        if( cursorAdapter!= null ) {
            lvData.setAdapter(cursorAdapter);
        }

        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_program_menu, menu);
        return true;
    }

    private void setFilterByBodyPart(int position) {
        cursorAdapter.setBodyPartPositionFilter(position);
        Cursor cursor = DBEngine.getExercisesCursor(position);
        cursorAdapter.swapCursor(cursor);
    }

    private void onSaveProgram() {
        final TrainingProgramTable trainingProgramTable = TrainingProgramTable.getTrainingProgramTable();
        db_id = trainingProgramTable.createNew("");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.program_name);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                if(!name.isEmpty()) {
                    trainingProgramTable.setName(name);
                    trainingProgramTable.writeData(db_id, 1);
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.program_save_error, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                ExercisesTable.resetUsedFlag();
                closeActivity();
                onStartProgram(db_id);
            }
        });

        builder.show();
        //ModalDialogThread t = new ModalDialogThread("Program:");
        //t.execute();
    }

    private void onStartProgram(long db_id) {
        Intent intentProgram = new Intent(this, TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)db_id);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE);
        startActivity(intentProgram);
    }

    private void closeActivity() {
        this.finish();
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.create_program);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_save: {
                        //addNewExercise();
                        onSaveProgram();
                        break;
                    }
                }
                return false;
            }
        });

        //toolbar.inflateMenu(R.menu.exercises_list_menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null) {
            long db_id = data.getLongExtra(Constants.INTENT_PARAM_ID, 0);
            String name = data.getStringExtra(Constants.INTENT_PARAM_NAME);
            String note = data.getStringExtra(Constants.INTENT_PARAM_NOTE);
            ExercisesTable.ExriciseType type = ExercisesTable.ExriciseType.fromId(data.getIntExtra(Constants.INTENT_PARAM_TYPE, 0));

            ExercisesTable.writeData(db_id, name, type, note);
            cursor = DBEngine.getExercisesCursor();
            cursorAdapter.swapCursor(cursor);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cursor != null) {
            cursor.close();
        }
    }
}

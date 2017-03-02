package net.sramanovich.fitnessday;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.ExercisesTable;
import net.sramanovich.fitnessday.db.TrainingProgramTable;

/**
 * Activity - exercises list with check boxes
 * to create new program
 * ask program name dialog
 */

public class NewProgramExercisesActivity extends AppCompatActivity {
    private CursorAdapter cursorAdapter;
    private Cursor cursor;
    private Toolbar toolbar;
    private long db_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_list);

        ListView lvData = (ListView)findViewById(R.id.listExercises);

        Cursor cursor = DBEngine.getExercisesCursor();
        String[] from = new String[]{ExercisesTable.COL_USED, ExercisesTable.COL_NAME, ExercisesTable.COL_SPLIT_NR};
        int[] to = new int[]{R.id.checkBoxIsUsed, R.id.textViewProgramExerciseName, R.id.buttonSplitNumber};
        cursorAdapter = ExercisesTable.getNewProgramExercisesCursorAdapter( DBEngine.getWritableDatabase(), this, R.layout.new_program_exercises_list_item, cursor, from, to, 0);
        if( cursorAdapter!= null ) {
            lvData.setAdapter(cursorAdapter);
        }

        initToolbar();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveProgram();
            }
        });

        //AskNameDialog.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercises_list_menu, menu);
        return true;
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
            }
        });

        builder.show();
        //ModalDialogThread t = new ModalDialogThread("Program:");
        //t.execute();
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
                    case R.id.menu_add: {
                        addNewExercise();
                        break;
                    }
                }
                return false;
            }
        });

        //toolbar.inflateMenu(R.menu.exercises_list_menu);
    }

    private void addNewExercise() {
        Intent intent = new Intent(this, ExerciseEditorActivity.class);
        intent.putExtra(Constants.INTENT_PARAM_ID, 0);
        startActivityForResult(intent, 1);
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

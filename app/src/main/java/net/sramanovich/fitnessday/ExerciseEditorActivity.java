package net.sramanovich.fitnessday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.ExercisesTable;

public class ExerciseEditorActivity extends AppCompatActivity{

    private long db_id=0;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exercise_editor);

        initToolbar();

        db_id = getIntent().getLongExtra(Constants.INTENT_PARAM_ID, 0);

        Button btnOk = (Button)findViewById(R.id.buttonExerciseEditOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewExercise();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.training_program_menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_save) {
            saveNewExercise();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    private void saveNewExercise() {
        EditText nameEditText= (EditText)findViewById(R.id.editTextExerciseEditName);
        String name = nameEditText.getText().toString();
        EditText noteEditText= (EditText)findViewById(R.id.editTextExerciseEditNote);
        String note = noteEditText.getText().toString();

        ExercisesTable.ExriciseType type = ExercisesTable.ExriciseType.fromId(0);
        ExercisesTable.writeData(db_id, name, type, note);
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.exercies);
        toolbar.inflateMenu(R.menu.training_program_menu);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_done) {
                    saveNewExercise();
                    finish();
                    return true;
                }

                return false;
            }
        });
    }
}
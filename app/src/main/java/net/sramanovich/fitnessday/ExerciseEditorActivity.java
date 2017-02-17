package net.sramanovich.fitnessday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                EditText nameEditText= (EditText)findViewById(R.id.editTextExerciseEditName);
                String name = nameEditText.getText().toString();
                EditText noteEditText= (EditText)findViewById(R.id.editTextExerciseEditNote);
                String note = noteEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(Constants.INTENT_PARAM_ID, db_id);
                intent.putExtra(Constants.INTENT_PARAM_NAME, name);
                intent.putExtra(Constants.INTENT_PARAM_TYPE, 0);
                intent.putExtra(Constants.INTENT_PARAM_NOTE, note);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.exercies);
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
package net.sramanovich.fitnessday;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import net.sramanovich.fitnessday.adapters.ExerciseSetAdapter;
import net.sramanovich.fitnessday.utils.PairSet;

import java.util.ArrayList;

public class ExerciseSetActivity extends AppCompatActivity {

    ArrayList<PairSet> setList;

    ExerciseSetAdapter listAdapter;

    private Toolbar toolbar;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_set);

        Intent intent = getIntent();

        TextView tvName = (TextView) findViewById(R.id.textViewName);
        tvName.setText(intent.getStringExtra(Constants.INTENT_PARAM_NAME));

        position = intent.getIntExtra(Constants.INTENT_PARAM_POSITION, -1);

        initToolbar();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fabExerSetDone);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExerciseSetDone();
            }
        });

        Button btnAddExercise = (Button) findViewById(R.id.buttonAddExercise);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText repsEdit = (EditText)findViewById(R.id.editTextReps);
                EditText weightEdit = (EditText)findViewById(R.id.editTextWeight);
                int reps=0;
                if(repsEdit.getText().toString().length()>0) {
                    reps = Integer.parseInt(repsEdit.getText().toString());
                }

                double weight=0;
                if(weightEdit.getText().toString().length()>0) {
                    weight = Double.parseDouble(weightEdit.getText().toString());
                }

                setList.add(new PairSet(reps, weight));
                repsEdit.setText("");
                weightEdit.setText("");
                listAdapter.notifyDataSetChanged();
            }
        });

        setList = intent.getParcelableArrayListExtra(Constants.INTENT_PARAM_SET_LIST);

        ListView listView = (ListView)findViewById(R.id.listViewSets);
        listAdapter = new ExerciseSetAdapter(this, setList);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    private void onExerciseSetDone() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Constants.INTENT_PARAM_SET_LIST, setList);
        intent.putExtra(Constants.INTENT_PARAM_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }
}

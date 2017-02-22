package net.sramanovich.fitnessday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.TrainingProgramTable;
import net.sramanovich.fitnessday.utils.AskNameDialog;
import net.sramanovich.fitnessday.utils.ModalDialogThread;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initToolbar();

        DBEngine.init(this);

        Button btnExercises = (Button)findViewById(R.id.btnExercises);
        btnExercises.setOnClickListener(this);
        Button btnCreateProgram = (Button)findViewById(R.id.btnCreateProgram);
        btnCreateProgram.setOnClickListener(this);
        Button btnSelectProgram = (Button)findViewById(R.id.btnSelectProgram);
        btnSelectProgram.setOnClickListener(this);
        Button btnMyPrograms = (Button)findViewById(R.id.btnMyPrograms);
        btnMyPrograms.setOnClickListener(this);
        Button btnContinueLastProgram = (Button)findViewById(R.id.btnContinueLastProgram);
        btnContinueLastProgram.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnExercises: {
                Intent intentExercise = new Intent(this, ExercisesActivity.class);
                startActivity(intentExercise);
                break;
            }

            case R.id.btnCreateProgram: {
                Intent intentNewProgramExercises = new Intent(this, NewProgramExercisesActivity.class);
                startActivity(intentNewProgramExercises);
                break;
            }

            case R.id.btnSelectProgram: {
                Intent intentContent = new Intent(this, ProgramsContentListActivity.class);
                intentContent.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE);
                startActivity(intentContent);
                break;
            }

            case R.id.btnMyPrograms: {
                Intent intentContent = new Intent(this, ProgramsContentListActivity.class);
                intentContent.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM_TEMPLATE);
                startActivity(intentContent);
                break;
            }

            case R.id.btnContinueLastProgram: {
                Intent intentContent = new Intent(this, ProgramsContentListActivity.class);
                intentContent.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);
                startActivity(intentContent);
                break;
            }
        }
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        //toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println(item.toString());
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }
}

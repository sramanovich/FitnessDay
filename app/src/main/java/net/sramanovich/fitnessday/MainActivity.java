package net.sramanovich.fitnessday;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.Button;

//import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.TrainingProgramTable;
//import net.sramanovich.fitnessday.utils.AskNameDialog;
//import net.sramanovich.fitnessday.utils.ModalDialogThread;

public class MainActivity extends Fragment/*AppCompatActivity*/ implements View.OnClickListener {

    private Toolbar toolbar;

    private View parent_view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent_view =inflater.inflate(R.layout.main,container,false);

        Button btnCreateProgram = (Button)parent_view.findViewById(R.id.btnCreateProgram);
        btnCreateProgram.setOnClickListener(this);
        Button btnSelectProgram = (Button)parent_view.findViewById(R.id.btnBeginTraining);
        btnSelectProgram.setOnClickListener(this);
        Button btnMyPrograms = (Button)parent_view.findViewById(R.id.btnTransformation);
        btnMyPrograms.setOnClickListener(this);

        return parent_view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnBeginTraining: {
                Cursor cursor = TrainingProgramTable.getTrainingProgramTable().getLastTrainingProgram();
                if (cursor.getPosition() >= 0) {
                    int db_id = cursor.getInt(cursor.getColumnIndex(TrainingProgramTable.COL_ID));
                    int isTemplate = cursor.getInt(cursor.getColumnIndex(TrainingProgramTable.COL_IS_TEMPLATE));
                    Intent intentProgram = new Intent(parent_view.getContext(), TrainingProgramActivity.class);
                    intentProgram.putExtra(Constants.INTENT_PARAM_ID, db_id);
                    if (isTemplate == Constants.TT_USER_PROGRAM) {  //open program
                        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);

                    } else {    //start new program
                        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE);
                    }
                    startActivity(intentProgram);
                }
                break;
            }

            case R.id.btnCreateProgram: {
                Intent intentNewProgramExercises = new Intent(parent_view.getContext(), NewProgramExercisesActivity.class);
                startActivity(intentNewProgramExercises);
                break;
            }
            /*
            case R.id.btnExercises: {
                Intent intentExercise = new Intent(this, ExercisesActivity.class);
                startActivity(intentExercise);
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
            }*/
        }
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

    private void initToolbar() {
        toolbar = (Toolbar)parent_view.findViewById(R.id.toolbar);
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

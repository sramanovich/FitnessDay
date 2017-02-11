package net.sramanovich.fitnessday;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.database.Cursor;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
//import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
//import android.widget.Toast;

public class ExercisesActivity extends AppCompatActivity {

    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_list);

        ListView lvData = (ListView)findViewById(R.id.listExercises);

        Cursor cursor = DBEngine.getExercisesCursor();
        String[] from = new String[]{ExercisesTable.COL_NAME, ExercisesTable.COL_TYPE};
        int[] to = new int[]{R.id.textViewExerciseListItemName, R.id.imgViewExerciseListItemIcon};
        cursorAdapter = ExercisesTable.getExercisesCursorAdapter( this, R.layout.exercises_list_item, cursor, from, to, 0);
        if( cursorAdapter!= null ) {
            lvData.setAdapter(cursorAdapter);
        }

        /*ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }*/

        /*FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ExercisesActivity.this, "Would you like a coffee?", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}

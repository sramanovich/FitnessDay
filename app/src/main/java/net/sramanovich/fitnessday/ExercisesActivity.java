package net.sramanovich.fitnessday;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.database.Cursor;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
//import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;

import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.ExercisesTable;
//import android.widget.Toast;

public class ExercisesActivity extends AppCompatActivity {

    private CursorAdapter cursorAdapter;
    private Cursor cursor;
    private Toolbar toolbar;

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

        //initToolbar();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        if( fab != null ) {
            fab.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercises_list_menu, menu);
        return true;
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

package net.sramanovich.fitnessday;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class ExercisesActivity extends Activity {

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
    }
}

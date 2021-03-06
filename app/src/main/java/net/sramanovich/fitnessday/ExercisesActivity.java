package net.sramanovich.fitnessday;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.database.Cursor;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
//import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ListView;

import net.sramanovich.fitnessday.adapters.BodyPartRecycleViewAdapter;
import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.ExercisesTable;
import net.sramanovich.fitnessday.utils.RecyclerItemClickListener;
//import android.widget.Toast;

public class ExercisesActivity extends Fragment {

    private CursorAdapter cursorAdapter;
    private Cursor cursor;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private View parent_view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent_view =inflater.inflate(R.layout.exercises_list,container,false);

        ExercisesTable.resetUsedFlag();

        mRecyclerView = (RecyclerView) parent_view.findViewById(R.id.recyclerViewBodyParts);
        mLayoutManager = new LinearLayoutManager(parent_view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BodyPartRecycleViewAdapter(parent_view.getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(parent_view.getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Cursor cursor = DBEngine.getExercisesCursor(position);
                        cursorAdapter.swapCursor(cursor);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        ListView lvData = (ListView)parent_view.findViewById(R.id.listExercises);

        Cursor cursor = DBEngine.getExercisesCursor();
        String[] from = new String[]{ExercisesTable.COL_NAME, ExercisesTable.COL_NOTE, ExercisesTable.COL_TYPE};
        int[] to = new int[]{R.id.textViewExerciseListItemName, R.id.textViewExerciseListItemDescription, R.id.imgViewExerciseListItemIcon};
        cursorAdapter = ExercisesTable.getExercisesCursorAdapter( parent_view.getContext(), R.layout.exercises_list_item, cursor, from, to, 0);
        if( cursorAdapter!= null ) {
            lvData.setAdapter(cursorAdapter);
        }

        //initToolbar();

        /*FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        if( fab != null ) {
            fab.hide();
        }*/

        return parent_view;
    }

    @Override
    public void onResume() {
        cursor = DBEngine.getExercisesCursor();
        cursorAdapter.swapCursor(cursor);
        super.onResume();
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercises_menu, menu);
        return true;
    }

    private void initToolbar() {
        Activity activityParent = getParent();
        if (activityParent==null) {
            return;
        }
        toolbar = (Toolbar)activityParent.findViewById(R.id.toolbar);
        if (toolbar==null) {
            return;
        }

        toolbar.setTitle(R.string.app_name);
        //toolbar.setSubtitle(R.string.exercies);
        //toolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(toolbar);

        // don't add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
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

    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }*/
}

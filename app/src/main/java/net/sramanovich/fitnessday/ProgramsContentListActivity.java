package net.sramanovich.fitnessday;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.TrainingProgramTable;

public class ProgramsContentListActivity extends AppCompatActivity {

    final int MENU_ITEM_DELETE_PROGRAM=1;

    final int MENU_ITEM_OPEN_PROGRAM=2;

    final int MENU_ITEM_START_PROGRAM=3;

    final int MENU_ITEM_VIEW_PROGRAM=4;

    TrainingProgramTable mTable;

    Cursor cursor;

    CursorAdapter cursorAdapter;

    private int isTemplate;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.programs_content);

        initToolbar();

        try {
            mTable = TrainingProgramTable.getTrainingProgramTable();

            Intent intent = getIntent();
            isTemplate = intent.getIntExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE);

            cursor = DBEngine.getTrainingProgramCursor(isTemplate);
            String[] from = new String[]{TrainingProgramTable.COL_NAME, TrainingProgramTable.COL_DATE};
            int[] to = new int[]{R.id.textViewProgramName, R.id.textViewProgramDate};

            cursorAdapter = mTable.getProgramsContentListAdapter(this, R.layout.programs_content_item, cursor, from, to, 0);

            ListView lvPrograms = (ListView) findViewById(R.id.listViewPrograms);
            lvPrograms.setAdapter(cursorAdapter);
            lvPrograms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(isTemplate==Constants.TT_USER_PROGRAM_TEMPLATE) {
                        onViewProgram(id);
                    }
                    else {
                        onOpenProgram(id);
                    }
                }
            });
            registerForContextMenu(lvPrograms);
            /*lvPrograms.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return false;
                }
            });*/


        } catch(SQLiteException e) {
            Toast toast= Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.clear();
        int count=0;
        if(isTemplate==Constants.TT_PROGRAM_TEMPLATE) {
            menu.add(0, MENU_ITEM_START_PROGRAM, count++, "Start");
        }
        else if(isTemplate==Constants.TT_USER_PROGRAM_TEMPLATE) {
            menu.add(0, MENU_ITEM_START_PROGRAM, count++, "Start");
            menu.add(0, MENU_ITEM_VIEW_PROGRAM, count++, "View");
        }
        else if(isTemplate==Constants.TT_USER_PROGRAM) {
            menu.add(0, MENU_ITEM_OPEN_PROGRAM, count++, "Continue");
            menu.add(0, MENU_ITEM_VIEW_PROGRAM, count++, "View");
        }

        menu.add(0, MENU_ITEM_DELETE_PROGRAM, count++, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case MENU_ITEM_DELETE_PROGRAM:
                mTable.deleteRecord(info.id);
                cursor = DBEngine.getTrainingProgramCursor(isTemplate);
                cursorAdapter.swapCursor(cursor);
                break;

            case MENU_ITEM_OPEN_PROGRAM:
                onOpenProgram(info.id);
                break;

            case MENU_ITEM_VIEW_PROGRAM:
                onViewProgram(info.id);
                break;

            case MENU_ITEM_START_PROGRAM:
                onStartProgram(info.id);
                break;
        }

        return true;//super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = DBEngine.getTrainingProgramCursor(isTemplate);
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle(R.string.training_programs);
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

    private void onStartProgram(long id) {
        Intent intentNewProgram = new Intent(this, TrainingProgramActivity.class);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, isTemplate);
        startActivity(intentNewProgram);
    }

    private void onOpenProgram(long id) {
        Intent intentProgram = new Intent(this, TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, isTemplate);
        startActivity(intentProgram);
    }

    private void onViewProgram(long id) {
        Intent intentProgram = new Intent(this, TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_VIEW_MODE, 1);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, isTemplate);
        startActivity(intentProgram);
    }
}

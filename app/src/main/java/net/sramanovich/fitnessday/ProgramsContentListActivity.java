package net.sramanovich.fitnessday;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.TrainingProgramTable;

public class ProgramsContentListActivity extends Fragment {

    final int MENU_ITEM_DELETE_PROGRAM=1;

    final int MENU_ITEM_OPEN_PROGRAM=2;

    final int MENU_ITEM_START_PROGRAM=3;

    final int MENU_ITEM_VIEW_PROGRAM=4;

    TrainingProgramTable mTable;

    Cursor cursor;

    CursorAdapter cursorAdapter;

    private int isTemplate;

    private Toolbar toolbar;

    private View parent_view;

    private static ProgramsContentListActivity mInstance = null;

    public static ProgramsContentListActivity getInstance() {
        if (mInstance==null) {
            mInstance = new ProgramsContentListActivity();
        }

        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent_view = inflater.inflate(R.layout.programs_content,container,false);

        //initToolbar();

        try {
            mTable = TrainingProgramTable.getTrainingProgramTable();

            if (getArguments() != null) {
                isTemplate = getArguments().getInt(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE);
            }

            cursor = DBEngine.getTrainingProgramCursor(isTemplate);
            String[] from = new String[]{TrainingProgramTable.COL_NAME, TrainingProgramTable.COL_DATE};
            int[] to = new int[]{R.id.textViewProgramName, R.id.textViewProgramDate};

            cursorAdapter = mTable.getProgramsContentListAdapter(parent_view.getContext(), R.layout.programs_content_item, cursor, from, to, 0);

            ListView lvPrograms = (ListView) parent_view.findViewById(R.id.listViewPrograms);
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
            Toast toast= Toast.makeText(parent_view.getContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

        return parent_view;
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
    public void onResume() {
        super.onResume();
        cursor = DBEngine.getTrainingProgramCursor(isTemplate);
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    private void initToolbar() {
        toolbar = (Toolbar)parent_view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        //toolbar.setSubtitle(R.string.training_programs);
        //toolbar.setLogo(R.drawable.ic_launcher);
        //setSupportActionBar(toolbar);

        // add back arrow to toolbar
        /*if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }*/

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }

    private void onStartProgram(long id) {
        Intent intentNewProgram = new Intent(parent_view.getContext(), TrainingProgramActivity.class);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, isTemplate);
        startActivity(intentNewProgram);
    }

    private void onOpenProgram(long id) {
        Intent intentProgram = new Intent(parent_view.getContext(), TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, isTemplate);
        startActivity(intentProgram);
    }

    private void onViewProgram(long id) {
        Intent intentProgram = new Intent(parent_view.getContext(), TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_VIEW_MODE, 1);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, isTemplate);
        startActivity(intentProgram);
    }
}

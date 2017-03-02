package net.sramanovich.fitnessday;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.TrainingProgramTable;

public class UserProgramsContentListActivity extends AppCompatActivity {

    final int MENU_ITEM_DELETE_PROGRAM=1;

    final int MENU_ITEM_OPEN_PROGRAM=2;

    final int MENU_ITEM_START_PROGRAM=3;

    final int MENU_ITEM_VIEW_PROGRAM=4;

    private TrainingProgramTable mTable;

    private Cursor cursor1;

    private CursorAdapter cursorAdapter1;

    private Cursor cursor2;

    private CursorAdapter cursorAdapter2;

    private String selProgramName;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_programs_content);

        //initToolbar();

        try {
            mTable = TrainingProgramTable.getTrainingProgramTable();

            cursor1 = DBEngine.getTrainingProgramCursor(Constants.TT_PROGRAM_TEMPLATE);
            String[] from = new String[]{TrainingProgramTable.COL_NAME};
            int[] to = new int[]{R.id.textViewProgramName};

            cursorAdapter1 = mTable.getProgramsContentListAdapter(this, R.layout.user_programs_content_item1, cursor1, from, to, 0);

            ListView lvPrograms = (ListView) findViewById(R.id.listViewPrograms);
            lvPrograms.setAdapter(cursorAdapter1);
            lvPrograms.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

            lvPrograms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    /*for(int n = 0; n < parent.getChildCount(); n++)
                    {
                        parent.getChildAt(n).setBackgroundColor(Color.TRANSPARENT);
                    }

                    view.setBackgroundColor(Color.GREEN);*/

                    onSelectProgram(position);
                }
            });

            onSelectProgram(0);

            cursor2 = DBEngine.getTrainingProgramCursor(Constants.TT_USER_PROGRAM, selProgramName);
            String[] from2 = new String[]{TrainingProgramTable.COL_DATE};
            int[] to2 = new int[]{R.id.textViewProgramName};

            cursorAdapter2 = mTable.getProgramsContentListAdapter(this, R.layout.user_programs_content_item2, cursor2, from, to, 0);


            ListView lvPrograms2 = (ListView) findViewById(R.id.listViewUserPrograms);
            lvPrograms2.setAdapter(cursorAdapter2);

            lvPrograms2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onOpenProgram(id);
                }
            });

        } catch(SQLiteException e) {
            Toast toast= Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        /*menu.clear();
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
        */
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /*AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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
        }*/

        return true;//super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor1 = DBEngine.getTrainingProgramCursor(Constants.TT_PROGRAM_TEMPLATE);
        cursorAdapter1.swapCursor(cursor1);

        ListView lvPrograms = (ListView) findViewById(R.id.listViewPrograms);
        lvPrograms.setSelection(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor1.close();
        cursor2.close();
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

    private void onSelectProgram(int position) {
        if (cursor1.moveToPosition(position)) {
            selProgramName = cursor1.getString(cursor1.getColumnIndex(TrainingProgramTable.COL_NAME));
        } else {
            selProgramName = "";
        }

        /*int index = selProgramName.indexOf(" - ");
        if( index >= 0 ) {
            selProgramName = selProgramName.substring(0, index);
        }*/

        //LinearLayout layoutItemColor = (LinearLayout)findViewById(R.id.linLayoutItem1);
        //layoutItemColor.setBackground(new ColorDrawable(Color.parseColor("#997070ff")));

        cursor2 = DBEngine.getTrainingProgramCursor(Constants.TT_USER_PROGRAM, selProgramName);
        if (cursorAdapter2 != null) {
            cursorAdapter2.swapCursor(cursor2);
        }
    }

    private void onStartProgram(long id) {
        Intent intentNewProgram = new Intent(this, TrainingProgramActivity.class);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);
        startActivity(intentNewProgram);
    }

    private void onOpenProgram(long id) {
        Intent intentProgram = new Intent(this, TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);
        startActivity(intentProgram);
    }

    private void onViewProgram(long id) {
        Intent intentProgram = new Intent(this, TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_VIEW_MODE, 1);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);
        startActivity(intentProgram);
    }
}

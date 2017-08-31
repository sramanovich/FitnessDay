package net.sramanovich.fitnessday;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import net.sramanovich.fitnessday.adapters.UserProgramsDayAdapter;
import net.sramanovich.fitnessday.db.DBEngine;
import net.sramanovich.fitnessday.db.TrainingProgramTable;
import net.sramanovich.fitnessday.utils.ProgramData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class UserProgramsContentListActivity extends Fragment {

    final int MENU_ITEM_DELETE_PROGRAM=1;

    final int MENU_ITEM_OPEN_PROGRAM=2;

    final int MENU_ITEM_START_PROGRAM=3;

    final int MENU_ITEM_VIEW_PROGRAM=4;

    private TrainingProgramTable mTable;

    private Cursor cursor1;

    private CursorAdapter cursorAdapter1;

    private long mainProgramID;

    private Cursor cursor2;

    private ListView lvDayPrograms;

    private UserProgramsDayAdapter programDayAdapter;

    private String selProgramName="";

    private int selProgramPosition;

    private Toolbar toolbar;

    //private CaldroidFragment caldroidFragment;

    private Calendar calendar;

    private Date curDate;

    private View parent_view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent_view = inflater.inflate(R.layout.user_programs_content,container,false);

        //initToolbar();

        try {
            mTable = TrainingProgramTable.getTrainingProgramTable();

            cursor1 = DBEngine.getTrainingProgramCursor(Constants.TT_PROGRAM_TEMPLATE);
            String[] from = new String[]{TrainingProgramTable.COL_NAME};
            int[] to = new int[]{R.id.textViewProgramName};

            cursorAdapter1 = mTable.getProgramsContentListAdapter(parent_view.getContext(), R.layout.user_programs_content_item1, cursor1, from, to, 0);

            ListView lvPrograms = (ListView) parent_view.findViewById(R.id.listViewPrograms);
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

            cursor2 = DBEngine.getTrainingProgramCursor(Constants.TT_USER_PROGRAM, selProgramName);
            updateCalendarData(cursor2);

            lvDayPrograms = (ListView) parent_view.findViewById(R.id.listViewDayUserPrograms);
            programDayAdapter = getDayProgramsAdapter(new Date(0)/*curDate*/);
            lvDayPrograms.setAdapter(programDayAdapter);
            lvDayPrograms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Date date = programDayAdapter.getItem(position).getmDate();
                    long db_id = getProgramID(date);
                    if (db_id>0) {
                        onOpenProgram(db_id);
                    }
                }
            });

            onSelectProgram(0);

        } catch(SQLiteException e) {
            Toast toast= Toast.makeText(parent_view.getContext(), e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }

        return parent_view;
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
    public void onResume() {
        super.onResume();
        cursor1 = DBEngine.getTrainingProgramCursor(Constants.TT_PROGRAM_TEMPLATE);
        cursorAdapter1.swapCursor(cursor1);

        ListView lvPrograms = (ListView) parent_view.findViewById(R.id.listViewPrograms);
        lvPrograms.setSelection(selProgramPosition);
        onSelectProgram(selProgramPosition);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor1.close();
        cursor2.close();
    }

    private void onSelectProgram(int position) {
        mainProgramID = 0;
        if (cursor1.moveToPosition(position)) {
            selProgramName = cursor1.getString(cursor1.getColumnIndex(TrainingProgramTable.COL_NAME));
            mainProgramID = cursor1.getLong(cursor1.getColumnIndex(TrainingProgramTable.COL_ID));
            selProgramPosition = position;
        } else {
            selProgramName = "";
            selProgramPosition = 0;
        }

        //cursor2 = DBEngine.getTrainingProgramCursor(Constants.TT_USER_PROGRAM, selProgramName);
        //updateCalendarData(cursor2);

        programDayAdapter = getDayProgramsAdapter(new Date(0));
        lvDayPrograms.setAdapter(programDayAdapter);
    }

    private void onStartProgram(long id) {
        Intent intentNewProgram = new Intent(parent_view.getContext(), TrainingProgramActivity.class);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentNewProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE/*Constants.TT_USER_PROGRAM*/);
        startActivity(intentNewProgram);
    }

    private void onOpenProgram(long id) {
        if (id <= 0) {
            return;
        }

        Intent intentProgram = new Intent(parent_view.getContext(), TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);
        startActivity(intentProgram);
    }

    private void onViewProgram(long id) {
        Intent intentProgram = new Intent(parent_view.getContext(), TrainingProgramActivity.class);
        intentProgram.putExtra(Constants.INTENT_PARAM_ID, (int)id);
        intentProgram.putExtra(Constants.INTENT_PARAM_VIEW_MODE, 1);
        intentProgram.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);
        startActivity(intentProgram);
    }

    private void updateCalendarData(Cursor cursor) {
        /*caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        calendar = Calendar.getInstance();

        args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        caldroidFragment.setCaldroidListener(new JobCaldroidListener(this));

        HashMap<Date, Integer> datesMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int db_time = cursor.getInt(cursor.getColumnIndex(TrainingProgramTable.COL_DATE));
            long time = (long)db_time*1000;
            Date date = new Date(time);
            datesMap.put(date, R.drawable.round_button_green);
        }
        caldroidFragment.setBackgroundResourceForDates(datesMap);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        curDate = calendar.getTime();*/
    }

    private long getProgramID(Date date) {
        Cursor cursor = DBEngine.getTrainingProgramCursor(Constants.TT_USER_PROGRAM, selProgramName);
        while (cursor.moveToNext()) {
            int db_time = cursor.getInt(cursor.getColumnIndex(TrainingProgramTable.COL_DATE));
            long time = (long) db_time * 1000;
            Date dateDB = new Date(time);
            //long curTime = date.getTime();
            if (date.getTime() == dateDB.getTime()) {
                return cursor.getLong(cursor.getColumnIndex(TrainingProgramTable.COL_ID));
            }
        }

        return 0;
    }

    private UserProgramsDayAdapter getDayProgramsAdapter(Date date) {
        List<ProgramData> objects = new ArrayList<>();
        Cursor cursor = DBEngine.getTrainingProgramCursor(Constants.TT_USER_PROGRAM, selProgramName);
        while (cursor.moveToNext()) {
            int db_time = cursor.getInt(cursor.getColumnIndex(TrainingProgramTable.COL_DATE));
            long time = (long) db_time * 1000;
            Date dateDB = new Date(time);
            if ( date.getTime() == 0 || TrainingProgramTable.compareNormalizedDates(date, dateDB)==true) {
                String name = cursor.getString(cursor.getColumnIndex(TrainingProgramTable.COL_NAME));
                String note = cursor.getString(cursor.getColumnIndex(TrainingProgramTable.COL_NOTE));
                ProgramData data = new ProgramData(name, new Date(time), note);
                objects.add(data);
            }
        }

        Collections.sort(objects, new Comparator<ProgramData>() {
            @Override
            public int compare(ProgramData lhs, ProgramData rhs) {
                if (lhs.getmDate().getTime() > rhs.getmDate().getTime()) {
                    return -1;
                } else {
                    if (lhs.getmDate().getTime() < rhs.getmDate().getTime()) {
                        return 1;
                    }
                }

                return 0;
            }
        });
        UserProgramsDayAdapter adapter = new UserProgramsDayAdapter(parent_view.getContext(), objects);
        return adapter;
    }

    class JobCaldroidListener extends CaldroidListener {
        public Context context;
        public JobCaldroidListener(Context context) {
            this.context = context;
        }

        @Override
        public void onSelectDate(Date date, View view) {
            curDate = date;
            programDayAdapter = getDayProgramsAdapter(date);
            lvDayPrograms.setAdapter(programDayAdapter);
            /*long db_id = getProgramID(date);
            if (db_id>0) {
                onOpenProgram(db_id);
            } else {
                onStartProgram(mainProgramID);
            }*/
        }
        @Override
        public void onLongClickDate(Date date, View view) {
        }
    }
}

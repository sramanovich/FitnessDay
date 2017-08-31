package net.sramanovich.fitnessday;

//import android.app.TabActivity;
//import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.view.Menu;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.TabHost;

import com.android.common.view.SlidingTabLayout;

import net.sramanovich.fitnessday.db.DBEngine;

import static net.sramanovich.fitnessday.Constants.*;

//import static android.R.id.tabs;

public class MainTabActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Main", "Exercise", "Select program", "My programs"};
    final private int Numboftabs = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_activity);

        DBEngine.init(this);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        //tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                switch (position) {
                    case TAB_MAIN: {
                        return getResources().getColor(R.color.colorTabMain);
                    }

                    case TAB_EXERCISES: {
                        return getResources().getColor(R.color.colorTabExercises);
                    }

                    case TAB_PROGRAMS: {
                        return getResources().getColor(R.color.colorTabPrograms);
                    }

                    case TAB_USER_PROGRAMS: {
                        return getResources().getColor(R.color.colorUserPrograms);
                    }
                }
                return 0;
            }

            @Override
            public int getDividerColor(int position) {
                return 0;
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.clear();
        if (pager.getCurrentItem() == TAB_EXERCISES) {
            getMenuInflater().inflate(R.menu.exercises_menu, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_add) {
            addNewExercise();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewExercise() {
        Intent intent = new Intent(this, ExerciseEditorActivity.class);
        intent.putExtra(Constants.INTENT_PARAM_ID, 0);
        startActivityForResult(intent, 1);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


        // Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
            super(fm);

            this.Titles = mTitles;
            this.NumbOfTabs = mNumbOfTabsumb;

        }

        //This method return the fragment for the every position in the View Pager
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case TAB_MAIN: {
                    fragment = new MainActivity();
                    break;
                }

                case TAB_EXERCISES: {
                    fragment = new ExercisesActivity();
                    break;
                }

                case TAB_PROGRAMS: {
                    fragment = ProgramsContentListActivity.getInstance();
                    Bundle bundle = new Bundle();
                    bundle.putInt(INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE);
                    fragment.setArguments(bundle);
                    break;
                }

                case TAB_USER_PROGRAMS: {
                    fragment = new UserProgramsContentListActivity();
                    break;
                }
            }

            invalidateOptionsMenu();
            return fragment;
        }

        // This method return the titles for the Tabs in the Tab Strip

        @Override
        public CharSequence getPageTitle(int position) {
            return Titles[position];
        }

        // This method return the Number of tabs for the tabs Strip

        @Override
        public int getCount() {
            return NumbOfTabs;
        }
    }
}

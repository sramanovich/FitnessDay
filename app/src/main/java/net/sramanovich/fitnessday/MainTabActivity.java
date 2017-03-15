package net.sramanovich.fitnessday;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import net.sramanovich.fitnessday.db.DBEngine;

public class MainTabActivity extends TabActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_activity);

        DBEngine.init(this);

        initToolbar();

        TabHost tabHost = getTabHost();

        TabHost.TabSpec tabSpec;
        
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Main");
        tabSpec.setContent(new Intent(this, MainActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Exercises");
        Intent intentExercises = new Intent(this, ExercisesActivity.class);
        //intentExercises.putExtra(Constants.INTENT_PARAM_ID, this);
        tabSpec.setContent(intentExercises);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Select Program");
        Intent intentContent = new Intent(this, ProgramsContentListActivity.class);
        intentContent.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_PROGRAM_TEMPLATE);
        tabSpec.setContent(intentContent);
        tabHost.addTab(tabSpec);

        /*tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("My Programs");
        Intent intentContent2 = new Intent(this, ProgramsContentListActivity.class);
        intentContent2.putExtra(Constants.INTENT_PARAM_IS_TEMPLATE, Constants.TT_USER_PROGRAM);
        tabSpec.setContent(intentContent2);
        tabHost.addTab(tabSpec);*/
        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("My Programs");
        Intent intentContent2 = new Intent(this, UserProgramsContentListActivity.class);
        tabSpec.setContent(intentContent2);
        tabHost.addTab(tabSpec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Menu menu = toolbar.getMenu();
                menu.clear();
                switch(tabId) {
                    case "tag1":
                    case "tag3":
                        toolbar.inflateMenu(R.menu.menu);
                        break;
                    case "tag2": {
                        toolbar.inflateMenu(R.menu.exercises_menu);
                        break;
                    }
                }
            }
        });

        /*tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setIndicator("Calendar");
        tabSpec.setContent(new Intent(this, ExercisesActivity.class));
        tabHost.addTab(tabSpec);*/
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        //toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_save: {
                        //addNewExercise();
                        break;
                    }
                }
                return false;
            }
        });

        toolbar.inflateMenu(R.menu.menu);
    }
}
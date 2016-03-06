package com.group4.thermostat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Schedule extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.custom_schedule_list_view);

        List<ScheduleItem> schedule = new ArrayList<>();

        ScheduleItem item1 = new ScheduleItem(1, 1, 70);
        ScheduleItem item2 = new ScheduleItem(1, 22, 65);
        ScheduleItem item3 = new ScheduleItem(2, 1, 70);

        schedule.add(item1);
        schedule.add(item2);
        schedule.add(item3);

        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, R.layout.linear_schedule_item_row, schedule);
        listView.setAdapter(scheduleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                ScheduleItem item = (ScheduleItem) adapter.getItemAtPosition(position);

                Log.d("Schedule here", "got one");
            }
        });
    }

}

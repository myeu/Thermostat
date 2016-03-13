package com.group4.thermostat;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 *  created by marisayeung
 *
 *      Show the schedule with tabs for each day
 */

public class Schedule extends AppCompatActivity {

    private final static String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private List<ScheduleItem> schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ListView listView = (ListView) findViewById(R.id.custom_schedule_list_view);

        schedule = new ArrayList<>();

        schedule.add(new ScheduleItem(ScheduleItem.MONDAY, 1, 70));
        schedule.add(new ScheduleItem(ScheduleItem.MONDAY, 22, 65));
        schedule.add(new ScheduleItem(ScheduleItem.TUESDAY, 1, 70));
        schedule.add(new ScheduleItem(ScheduleItem.TUESDAY, 22, 65));
        schedule.add(new ScheduleItem(ScheduleItem.WEDNESDAY, 1, 70));
        schedule.add(new ScheduleItem(ScheduleItem.WEDNESDAY, 22, 65));
        schedule.add(new ScheduleItem(ScheduleItem.THURSDAY, 1, 70));
        schedule.add(new ScheduleItem(ScheduleItem.THURSDAY, 22, 65));
        schedule.add(new ScheduleItem(ScheduleItem.FRIDAY, 1, 70));
        schedule.add(new ScheduleItem(ScheduleItem.FRIDAY, 22, 65));
        schedule.add(new ScheduleItem(ScheduleItem.SATURDAY, 1, 70));
        schedule.add(new ScheduleItem(ScheduleItem.SATURDAY, 22, 65));
        schedule.add(new ScheduleItem(ScheduleItem.SUNDAY, 1, 70));
        schedule.add(new ScheduleItem(ScheduleItem.SUNDAY, 22, 65));

        Collections.sort(schedule);

        int lastItemDay = 0;
        for (ScheduleItem item: schedule) {
            item.setSeparator(false);
            if (item.getDay() != lastItemDay) {
                item.setSeparator(true);
            }
            lastItemDay = item.getDay();
        }

//        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, R.layout.linear_schedule_item_row, schedule);
//        listView.setAdapter(scheduleAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//                ScheduleItem item = (ScheduleItem) adapter.getItemAtPosition(position);
//
//                Log.d("Schedule here", "got one");
//            }
//        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        ArrayList<String> daySchedule;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            daySchedule = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            if (daySchedule != null) {
                daySchedule.clear();
            }
            int day = position + 1;
            for (ScheduleItem item : schedule) {
                if (item.getDay() == day) {
                    daySchedule.add(getScheduleItemString(item));
                }
            }

            return ScheduleDay.newInstance(daySchedule);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int day = position + 1;
            switch (day) {
                case ScheduleItem.MONDAY:
                    return "M";
                case ScheduleItem.TUESDAY:
                    return "T";
                case ScheduleItem.WEDNESDAY:
                    return "W";
                case ScheduleItem.THURSDAY:
                    return "Th";
                case ScheduleItem.FRIDAY:
                    return "F";
                case ScheduleItem.SATURDAY:
                    return "Sa";
                case ScheduleItem.SUNDAY:
                    return "Su";
            }
            return null;
        }
    }

    private String getScheduleItemString(ScheduleItem item) {
        return item.getTemp() + " at " + getTimeString(item.getHour());
    }

    private String getTimeString(int hour) {
        String s;
        if (hour > 12) {
            s = " PM";
            hour = hour - 12;
        } else if (hour == 12){
            s = " PM";
        } else {
            s = " AM";
        }
        return hour + ":00" + s;
    }
}

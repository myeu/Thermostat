package com.group4.thermostat;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

/**
 * Created by marisayeung on 3/12/16.
 */
public class ScheduleDay extends ListFragment {
    ScheduleAdapter scheduleAdapter;

//    private static ScheduleList scheduleList;
    private static final String SCHEDULE = "schedule";

    ArrayList<String> scheduleItemList;

    public ScheduleDay() {
    }

    public static ScheduleDay newInstance(ArrayList<String> schedule) {
//        scheduleItemList = new ArrayList<>(schedule);
        ScheduleDay fragment = new ScheduleDay();
        Bundle args = new Bundle();
//        args.putParcelableArrayList(SCHEDULE, scheduleList);
        args.putStringArrayList(SCHEDULE, schedule);
        fragment.setArguments(args);
        return fragment;
    }
//    public static ScheduleDay newInstance(List<ScheduleItem> schedule) {
//        scheduleList = new ScheduleList(schedule);
//        ScheduleDay fragment = new ScheduleDay();
//        Bundle args = new Bundle();
//        args.putParcelableArrayList(SCHEDULE, scheduleList);
//        fragment.setArguments(args);
//        return fragment;
//
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduleItemList = getArguments().getStringArrayList(SCHEDULE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_schedule_list, container, false);
        TextView emptyList = (TextView) rootView.findViewById(R.id.empty_list);

        if (scheduleItemList.size() > 0) {
            if (emptyList != null) {
                emptyList.setVisibility(View.GONE);
            }

            setListAdapter(new ScheduleAdapter(getActivity(), R.layout.linear_schedule_item_row, scheduleItemList));

//            ListView listView= (ListView) rootView.findViewById(list);
//            scheduleAdapter = new ScheduleAdapter(getActivity(), R.layout.linear_schedule_item_row, scheduleItemList);
//            listView.setAdapter(scheduleAdapter);
        } else {
            emptyList.setText("No scheduled items for this day");
        }

        return rootView;
    }

    private class myAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return scheduleItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return scheduleItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            return null;
        }
    }

    private static class ViewHolder {
        private final TextView textView1;

        public ViewHolder(View view) {
            textView1 = (TextView) view.findViewById(android.R.id.text1);
        }
    }


}

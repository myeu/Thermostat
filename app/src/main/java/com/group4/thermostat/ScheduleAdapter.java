package com.group4.thermostat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by marisayeung on 3/6/16.
 */
public class ScheduleAdapter extends ArrayAdapter<String> {

    private final List<String> schedule;

    public ScheduleAdapter(Context context, int resource, List<String> schedule) {
        super(context, resource, schedule);

        this.schedule = schedule;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String item = schedule.get(position);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.linear_schedule_item_row, null);

        TextView textView = (TextView) row.findViewById(R.id.label);
        textView.setText(item);

        return row;
    }
}

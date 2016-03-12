package com.group4.thermostat;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marisayeung on 3/12/16.
 */
public class ScheduleList extends ArrayList<String> implements Parcelable {

    List<String> schedule;
    ScheduleItem item;

    public ScheduleList(List<String> schedule) {
        this.schedule = schedule;
    }

    public ScheduleList() {
    }

    protected ScheduleList(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.clear();

        // First we have to read the list size
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
            String s = in.readString();
            this.add(s);
        }
    }


    public static final Creator<ScheduleList> CREATOR = new Creator<ScheduleList>() {
        @Override
        public ScheduleList createFromParcel(Parcel in) {
            return new ScheduleList(in);
        }

        @Override
        public ScheduleList[] newArray(int size) {
            return new ScheduleList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(schedule);
        dest.writeParcelable(item, flags);
    }
}

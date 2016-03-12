package com.group4.thermostat;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by marisayeung on 3/6/16.
 */
public class ScheduleItem implements Comparable<ScheduleItem>, Parcelable {
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

    private int day;
    private int hour;
    private int temp;
    private boolean isSeparator;

    public ScheduleItem(int day, int hour, int temp) {
        this.day = day;
        this.hour = hour;
        this.temp = temp;
        isSeparator = false;
    }

    protected ScheduleItem(Parcel in) {
        day = in.readInt();
        hour = in.readInt();
        temp = in.readInt();
        isSeparator = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ScheduleItem> CREATOR = new Creator<ScheduleItem>() {
        @Override
        public ScheduleItem createFromParcel(Parcel in) {
            return new ScheduleItem(in);
        }

        @Override
        public ScheduleItem[] newArray(int size) {
            return new ScheduleItem[size];
        }
    };

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getTemp() {
        return temp;
    }

    public int compareTo(ScheduleItem item2) {
        int dayCmp;
        if (this.getDay() > item2.getDay()) {
            dayCmp = 1;
        } else if (this.getDay() == item2.getDay()) {
            dayCmp = 0;
        } else {
            dayCmp = -1;
        }

        if (dayCmp == 0) {
            if (this.getHour() > item2.getHour()) {
                return 1;
            } else if (this.getHour() == item2.getHour()) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return dayCmp;
        }
    }

    public void setSeparator(boolean separator) {
        isSeparator = separator;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(hour);
        dest.writeInt(temp);
    }
}

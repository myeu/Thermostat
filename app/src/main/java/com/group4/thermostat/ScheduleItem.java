package com.group4.thermostat;

/**
 * Created by marisayeung on 3/6/16.
 */
public class ScheduleItem {
    private int day;
    private int hour;
    private int temp;

    public ScheduleItem(int day, int hour, int temp) {
        this.day = day;
        this.hour = hour;
        this.temp = temp;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getTemp() {
        return temp;
    }
}

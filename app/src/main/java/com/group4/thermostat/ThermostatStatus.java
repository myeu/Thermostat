package com.group4.thermostat;

/**
 * Created by marisayeung on 2/29/16.
 */
public class ThermostatStatus {
    private final int id;
    private final int temp;
    private final int date;
    private final int coolSetPoint;
    private final int heatSetPoint;
    private final boolean heatOn;
    private final boolean coolOn;

    public ThermostatStatus(int id, int temp, int date, int coolSetPoint, boolean coolOn, int heatSetPoint, boolean heatOn) {
        this.id = id;
        this.temp = temp;
        this.date = date;
        this.coolSetPoint = coolSetPoint;
        this.heatSetPoint = heatSetPoint;
        this.heatOn = heatOn;
        this.coolOn = coolOn;
    }

    public int getId() {
        return id;
    }

    public int getDate() {
        return date;
    }

    public int getTemp() {
        return temp;
    }

    public int getHeatSetPoint() {
        return heatSetPoint;
    }

    public int getCoolSetPoint() {
        return coolSetPoint;
    }

    public boolean isCoolOn() {
        return coolOn;
    }

    public boolean isHeatOn() {

        return heatOn;
    }
}


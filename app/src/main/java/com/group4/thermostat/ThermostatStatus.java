package com.group4.thermostat;

/**
 * Created by marisayeung on 2/29/16.
 */
public class ThermostatStatus {
    private int id;
    private int temp;
    private int date;
    private int setPoint;
    private boolean heatOn;
    private boolean coolOn;
    private int mode;

    static final String HEATING = "HEATING";
    static final String COOLING = "COOLING";
    static final String HOLDING = "HOLDING";

    static final int HEATMODE = 0;
    static final int COOLMODE = 1;
    static final int OFFMODE = 2;

    public ThermostatStatus(int id, int temp, int date, int setPoint, boolean coolOn, boolean heatOn, int mode) {
        this.id = id;
        this.temp = temp;
        this.date = date;
        this.setPoint = setPoint;
        this.heatOn = heatOn;
        this.coolOn = coolOn;
        this.mode = mode;
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

    public boolean isCoolOn() {
        return coolOn;
    }

    public boolean isHeatOn() {

        return heatOn;
    }

    public int getSetPoint() {
        return setPoint;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}


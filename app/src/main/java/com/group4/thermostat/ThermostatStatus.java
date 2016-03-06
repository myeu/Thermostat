package com.group4.thermostat;

/**
 * Created by marisayeung on 2/29/16.
 */
public class ThermostatStatus {
    private final int id;
    private final int temp;
    private final int date;
    private final int setPoint;
    private final boolean heatOn;
    private final boolean coolOn;

    static final String HEATING = "HEATING";
    static final String COOLING = "COOLING";
    static final String HOLDING = "HOLDING";

    public ThermostatStatus(int id, int temp, int date, int setPoint, boolean coolOn, boolean heatOn) {
        this.id = id;
        this.temp = temp;
        this.date = date;
        this.setPoint = setPoint;
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

    public boolean isCoolOn() {
        return coolOn;
    }

    public boolean isHeatOn() {

        return heatOn;
    }

    public int getSetPoint() {
        return setPoint;
    }
}


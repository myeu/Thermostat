package com.group4.thermostat;

import org.json.simple.JSONObject;

/**
 * Created by marisayeung on 2/29/16.
 */
public class ThermostatStatus {
    private int id;
    private int temp;
    private int date;
    private int setPoint; // TODO: remove
    private boolean heatOn;
    private boolean coolOn;
    private int mode;

    static final String HEATING = "HEATING";
    static final String COOLING = "COOLING";
    static final String HOLDING = "HOLDING";

    static final int HEATMODE = 0;
    static final int COOLMODE = 1;
    static final int OFFMODE = 2;

    long lastReceivedId;

    public ThermostatStatus(int id, int temp, int date, int setPoint, boolean coolOn, boolean heatOn, int mode) {
        this.id = id;
        this.temp = temp;
        this.date = date;
        this.setPoint = setPoint;
        this.heatOn = heatOn;
        this.coolOn = coolOn;
        this.mode = mode;
        lastReceivedId = 0;
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

    //{
    //  "outTemp":64.55965703011874,
    //  "coolState":0,
    //  "inTemp":60.60242489545787,
    //  "heatState":0,
    //  "Id":"1457387407778",
    //  "status":"ok"
    // }

    public void updateThermostatStatus(JSONObject json) {
        String idString = (String) json.get("Id");
        long id = Long.parseLong(idString);
        if (id > lastReceivedId) {
            temp = ((Double) json.get("inTemp")).intValue();
            if (Integer.parseInt(json.get("coolState").toString()) == 0) {
                coolOn = false;
            } else {
                coolOn = true;
            }
            if (Integer.parseInt(json.get("heatState").toString()) == 0) {
                heatOn = false;
            } else {
                heatOn = true;
            }

            lastReceivedId = id;
        }
    }
}


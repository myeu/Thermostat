package com.group4.thermostat;

import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by marisayeung
 *
 *      Holds all the state for the thermostat
 *          Gets updated on change, not replaced
 */
public class ThermostatStatus {
    private int id;
    private int temp;
    private int date;
    private int setPoint; // TODO: remove
    private boolean heatOn;
    private boolean coolOn;
    private int mode;
    private List<ScheduleItem> schedule;

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

    public ThermostatStatus(JSONObject thermostatData, JSONObject appData) {
        updateThermostatStatus(thermostatData);
        updateAppData(appData);
        Log.d("tData", thermostatData.toString());
        Log.d("aData", appData.toString());
        Log.d("Data", "Status created");
    }

//    {
//      "mode":"2",
//      "Id":"1457511426899",
//      "status":"ok",
//      "schedules":[{"hour":6,"setTemp":70,"day":1},
//                  ... ]
//      "setTemp":"70"
//     }
    private void updateAppData(JSONObject appData) {
        this.mode = Integer.parseInt(appData.get("mode").toString());
        this.setPoint = Integer.parseInt(appData.get("setTemp").toString());
        this.schedule = parseSchedule((JSONArray) appData.get("schedules"));
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

    public void up() {
        setPoint += 1;
    }

    public void down() {
        setPoint -= 1;
    }

    public void setPointTo(int setPoint) {
        this.setPoint = setPoint;
    }

//    [
//      {"hour":6,"setTemp":70,"day":1}
//    ]
    private List<ScheduleItem> parseSchedule(JSONArray schedule) {
        List<ScheduleItem> newSchedule = new ArrayList<>();
        Iterator i = schedule.iterator();

        while (i.hasNext()) {
            JSONObject jsonSchedule = (JSONObject) i.next();

            ScheduleItem s = new ScheduleItem(
                    Integer.parseInt(jsonSchedule.get("day").toString()),
                    Integer.parseInt(jsonSchedule.get("hour").toString()),
                    Integer.parseInt(jsonSchedule.get("setTemp").toString()));
            newSchedule.add(s);
        }
        Collections.sort(newSchedule);
        return newSchedule;
    }

    public List<ScheduleItem> getSchedule() {
        return schedule;
    }
}


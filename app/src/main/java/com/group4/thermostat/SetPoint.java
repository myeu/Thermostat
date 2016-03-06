package com.group4.thermostat;

/**
 * Created by marisayeung
 */
public class SetPoint {
    int temperature;
    int id;

    public SetPoint(int id, int temperature) {
        this.id = id;
        this.temperature = temperature;
    }

    public void up() {
        temperature++;
    }

    public void down() {
        temperature--;
    }

    public int getTemperature() {
        return temperature;
    }
}

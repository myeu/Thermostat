package com.group4.thermostat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Thermostat extends AppCompatActivity {

    List<ThermostatStatus> states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        states = new ArrayList<>();

        ThermostatStatus status1 = new ThermostatStatus(1, 70, 1, 75, false, 70, false);
        ThermostatStatus status2 = new ThermostatStatus(2, 69, 1, 75, false, 70, true);
        ThermostatStatus status3 = new ThermostatStatus(3, 76, 1, 75, true, 70, false);
        ThermostatStatus status4 = new ThermostatStatus(4, 66, 1, 78, false, 65, false);

        states.add(status1);
        states.add(status2);
        states.add(status3);
        states.add(status4);
    }

    public void chooseState(View view) {
        int id = view.getId();
        if (id == R.id.state_1) {
            changeState(1);
        } else if (id == R.id.state_2) {
            changeState(2);
        } else if (id == R.id.state_3) {
            changeState(3);
        } else if (id == R.id.state_4) {
            changeState(4);
        }
    }

    public void changeState(int id) {
        ThermostatStatus status = states.get(id - 1);

        TextView temp = (TextView) findViewById(R.id.current_temp);
        temp.setText(status.getTemp() + "");

        TextView coolSetPoint = (TextView) findViewById(R.id.set_temp_cool_value);
        coolSetPoint.setText(status.getCoolSetPoint() + "");

        TextView heatSetPoint = (TextView) findViewById(R.id.set_temp_heat_value);
        heatSetPoint.setText(status.getHeatSetPoint() + "");

    }

    public void changeSetTemp(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thermostat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

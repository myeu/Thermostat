package com.group4.thermostat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Thermostat extends AppCompatActivity {

    List<ThermostatStatus> states;
    ThermostatStatus status;
    SetPoint setPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get status

        // Test status changes via UI
        states = new ArrayList<>();

        ThermostatStatus status1 = new ThermostatStatus(1, 70, 1, 70, false, false);
        ThermostatStatus status2 = new ThermostatStatus(2, 69, 1, 70, false, true);
        ThermostatStatus status3 = new ThermostatStatus(3, 76, 1, 75, true, false);
        ThermostatStatus status4 = new ThermostatStatus(4, 66, 1, 70, false, false);

        states.add(status1);
        states.add(status2);
        states.add(status3);
        states.add(status4);

        changeState(status1);
        setPoint = new SetPoint(0, status1.getSetPoint());
    }

    public void chooseState(View view) {
        int id = view.getId();
        if (id == R.id.state_1) {
            changeState(states.get(0));
        } else if (id == R.id.state_2) {
            changeState(states.get(1));
        } else if (id == R.id.state_3) {
            changeState(states.get(2));
        } else if (id == R.id.state_4) {
            changeState(states.get(3));
        }
    }

    public void changeState(ThermostatStatus status) {
        TextView temp = (TextView) findViewById(R.id.current_temp);
        temp.setText(status.getTemp() + "");

        TextView setPoint = (TextView) findViewById(R.id.set_temp_value);
        setPoint.setText(status.getSetPoint() + "");

        TextView statusString = (TextView) findViewById(R.id.status_string);
        if (status.isCoolOn()) {
            statusString.setText(ThermostatStatus.COOLING);
            statusString.setTextColor(getResources().getColor(R.color.cool));
        } else if (status.isHeatOn()) {
            statusString.setText(ThermostatStatus.HEATING);
            statusString.setTextColor(getResources().getColor(R.color.heat));
        } else {
            statusString.setText(ThermostatStatus.HOLDING);
            statusString.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
    }

    public void changeSetTemp(View view) {
        int id = view.getId();

        if (id == R.id.up) {
            setPoint.up();
        } else if (id == R.id.down) {
            setPoint.down();
        }

        TextView setPointView = (TextView) findViewById(R.id.set_temp_value);

        JSONObject manualUpdate = new JSONObject();
        try {
            manualUpdate.put("setPoint", "" + setPoint.getTemperature());
            Log.d("JSON", manualUpdate.toString());
            setPointView.setText("" + setPoint.getTemperature());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
        return MenuHelper.handleOnItemSelected(this, item);
    }
}

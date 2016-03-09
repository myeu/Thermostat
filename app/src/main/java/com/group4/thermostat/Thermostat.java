package com.group4.thermostat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Thermostat extends AppCompatActivity {

    List<ThermostatStatus> states;
    ThermostatStatus status;
    SetPoint setPoint;
    String setTempURL = "http://52.37.144.142:9000/setTemperature";
    String getStatusURL = "http://52.37.144.142:9000/application";
//    List<Integer> textViewIds = Arrays.asList(R.id.current_temp, R.id.status_string, R.id.set_temp_value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get status
        ThermostatStatus status1 = new ThermostatStatus(1, 73, 1, 70, false, false, ThermostatStatus.HEATMODE);
        status = status1;
        setPoint = new SetPoint(0, status1.getSetPoint());
        Log.d("STATUS", "status created");
        getState(getStatusURL, true);
//        Request r = new Request();
//        JSONObject statusJson = r.getRequest(getStatusURL);
//        Log.d("GET", statusJson.toString());
//        status.updateThermostatStatus(statusJson);
        changeState(status);

        // Test status changes via UI
        states = new ArrayList<>();

        ThermostatStatus status2 = new ThermostatStatus(2, 69, 1, 70, false, true, ThermostatStatus.HEATMODE);
        ThermostatStatus status3 = new ThermostatStatus(3, 76, 1, 75, true, false, ThermostatStatus.COOLMODE);
        ThermostatStatus status4 = new ThermostatStatus(4, 66, 1, 70, false, false, ThermostatStatus.OFFMODE);

        states.add(status1);
        states.add(status2);
        states.add(status3);
        states.add(status4);
    }

    public void chooseState(View view) {
        switch (view.getId()) {
            case R.id.state_1:
                changeState(states.get(0));
                break;
            case R.id.state_2:
                changeState(states.get(1));
                break;
            case R.id.state_3:
                changeState(states.get(2));
                break;
            case R.id.state_4:
                changeState(states.get(3));
        }
    }

    public void fetchUpdate(View view) {
        getState(getStatusURL, false);
        changeState(status);
    }

    public void getState(String url, boolean loop) {
        RequestRunnable rr = new RequestRunnable(url, RequestRunnable.GET, loop);
        rr.execute();
    }
// TODO: get last setpoint from server upon login
    public void changeState(ThermostatStatus status) {
//        Current temp
        TextView temp = (TextView) findViewById(R.id.current_temp);
        temp.setText(status.getTemp() + "");

//        Set temp
        TextView setTemp = (TextView) findViewById(R.id.set_temp_value);
        setTemp.setText(setPoint.getTemperature() + "");

//        Status of HVAC
        TextView statusString = (TextView) findViewById(R.id.status_string);
        if (status.isCoolOn()) {
            statusString.setText(ThermostatStatus.COOLING);
            statusString.setTextColor(getResources().getColor(R.color.cool));
            Log.d("COOLING", "here");
        } else if (status.isHeatOn()) {
            statusString.setText(ThermostatStatus.HEATING);
            statusString.setTextColor(getResources().getColor(R.color.heat));
            Log.d("HEATING", "set status string here");
        } else if (!status.isHeatOn() && !status.isCoolOn()){
            statusString.setText(ThermostatStatus.HOLDING);
            statusString.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            Log.d("HOLDING", "here");
        }

//        Enabled?
        ImageView modeIcon = (ImageView) findViewById(R.id.mode);
        switch (status.getMode()) {
            case ThermostatStatus.HEATMODE:
                disableText(false);
                modeIcon.setImageDrawable(getDrawable(R.drawable.flame));
                break;
            case ThermostatStatus.COOLMODE:
                disableText(false);
                modeIcon.setImageDrawable(getDrawable(R.drawable.snowflake));
                break;
            case ThermostatStatus.OFFMODE:
                modeIcon.setImageDrawable(null);
                TextView offStatus = (TextView) findViewById(R.id.status_string);
                offStatus.setText("OFF");
                disableText(true);
                break;
        }
    }

    private void disableText(boolean disable) {

        TextView t;
        if (disable) {
            t = (TextView) findViewById(R.id.current_temp);
            t.setTextColor(getResources().getColor(R.color.colorDisabledText));
            t = (TextView) findViewById(R.id.status_string);
            t.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            t = (TextView) findViewById(R.id.set_temp_value);
            t.setTextColor(getResources().getColor(R.color.colorDisabledText));
        } else {
            t = (TextView) findViewById(R.id.current_temp);
            t.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            t = (TextView) findViewById(R.id.status_string);
            t.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            t = (TextView) findViewById(R.id.set_temp_value);
            t.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
    }

    public synchronized void changeSetTemp(View view) {
        long timeId = System.currentTimeMillis();

        int viewId = view.getId();

        if (viewId == R.id.up) {
            setPoint.up();
        } else if (viewId == R.id.down) {
            setPoint.down();
        }
        Log.d("getTemperature", "" + setPoint.getTemperature());

        TextView setPointView = (TextView) findViewById(R.id.set_temp_value);
        setPointView.setText("" + setPoint.getTemperature());

        updateServerSetTemp(timeId, setPoint.getTemperature());
    }

    public void updateServerSetTemp(long id, int temp) {

        JSONObject manualUpdate = new JSONObject();
        manualUpdate.put("Id", "" + id);
        manualUpdate.put("setTemp", "" + temp);
        Log.d("JSON", manualUpdate.toString());

        RequestRunnable rr = new RequestRunnable(setTempURL, manualUpdate, RequestRunnable.POST);
        rr.execute();
    }

    class RequestRunnable extends AsyncTask<Void,Void,Void> {
        JSONObject obj;
        String endPointURL;
        String requestType;
        static final String GET = "get";
        static final String POST = "post";
        boolean loop = false;

        public RequestRunnable(String endPointURL, JSONObject obj, String requestType) {
            this.obj = obj;
            this.endPointURL = endPointURL;
            this.requestType = requestType;
        }

        public RequestRunnable(String endPointURL, String requestType, boolean loop) {
            this.endPointURL = endPointURL;
            this.requestType = requestType;
            this.loop = loop;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Request request = new Request();

            if (requestType.equals(POST)) {
                JSONObject posted = request.postRequest(endPointURL, obj);
                Log.d("POST", posted.toString());
            }
            else if (requestType.equals(GET)) {
                JSONObject statusJson = request.getRequest(endPointURL);
                if (statusJson != null) {
                    status.updateThermostatStatus(statusJson);
//                    changeState(status);
                    Log.d("GET", statusJson.toString());
                } else {
                    Log.d("GET", "GET was null");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            changeState(status);
            super.onPostExecute(aVoid);
        }
    }

//    TODO: make sure this runs after initial status GET request
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        TODO: fix default
        int modeId = ThermostatStatus.HEATMODE;

        switch (status.getMode()) {
            case ThermostatStatus.COOLMODE:
                modeId = R.id.mode_cool;
                break;
            case ThermostatStatus.HEATMODE:
                modeId = R.id.mode_heat;
                break;
            case ThermostatStatus.OFFMODE:
                modeId = R.id.mode_off;
                break;
        }
        menu.findItem(modeId).setChecked(true);
        return true;
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

        MenuHelper.handleOnItemSelected(this, item, status);
        return true;
    }
}

package com.group4.thermostat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
 *      Thermostat is the main launcher activity of the app
 *
 *      It launches worker threads to
 *          - do a get request from the server every second to get the temperature data
 *          - post to the server when the set point chagnes
 *
 */

public class Thermostat extends AppCompatActivity {

    List<ThermostatStatus> states;
    ThermostatStatus status;
    String setTempURL = "http://52.37.144.142:9000/setTemperature";
    String getStatusURL = "http://52.37.144.142:9000/application";
    String getPastStateURL = "http://52.37.144.142:9000/thermostat";

    TextView temperature, setTemp, statusString, loading;
    ImageView modeIcon;
    Button b;
    ImageButton up, down;
    ProgressBar progressBar;

    Long originTime = Long.parseLong("1457823797097");
    Timer fetchTimer, scheduleTimer;

    ScheduleItem upcomingItem = null;

    /*
     *  The thread which gets from the server every second
     *      - this loops and calls a new async thread (below) for each get
     *
     */
    class fetchThermostatStatus extends TimerTask {
        @Override
        public void run () {
            Log.d("FETCH", "" + android.os.Process.myTid());
            getState(getStatusURL);
        }
    }

    /*
     *  The thread that checks the current time against the schedule
     *      - If there is a schedule item matching the time, it updates
     */
    class checkThermostatSchedule extends TimerTask {
        @Override
        public void run() {
            long timeId = System.currentTimeMillis();
            // note, we are modeling 1 hour for every 1 second
            long millisToHours = ((timeId - originTime) / 1000);
            int day = (int) (millisToHours / 24) % 7;
            int hour = (int) (millisToHours % 24);

            if (upcomingItem == null) {
                return;
            }

            int position;
            List<ScheduleItem> schedule = status.getSchedule();
            position = (schedule.indexOf(upcomingItem) + 1) % (schedule.size() - 2);

            if (upcomingItem.getDay() == day && upcomingItem.getHour() == hour) {
                Log.d("SCHEDULE", "(" + position + ", " + schedule.size() + ") scheduled change ( " + upcomingItem.getDay() + ", " + upcomingItem.getHour() + "): " + upcomingItem.getTemp());
                status.setPointTo(upcomingItem.getTemp());
                updateServerSetTemp(timeId, upcomingItem.getTemp());
                upcomingItem = schedule.get(position);
            }
            Log.d("THERMOSTAT TIME", " day: " + day + " hour: " + hour);


        }
    }

    /*
     *  The thread that gets the initial state of the thermostat from the server upon loading
     */
    class InitRequestRunnable extends AsyncTask<Void,Void,Void> {
        Request request1 = new Request();
        Request request2 = new Request();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JSONObject thermostatStatusJson = request1.getRequest(getStatusURL);
            JSONObject appStatusJson = request2.getRequest(getPastStateURL);
            if (thermostatStatusJson != null && appStatusJson != null) {
                status = new ThermostatStatus(thermostatStatusJson, appStatusJson);
            } else {
                Log.d("GET", "GET was null");
            }

            findNextScheduleItem(status.getSchedule());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initializeLayout();
            changeState(status);
            invalidateOptionsMenu();
        }
    }

    /*
     *  The one off async task that handles the get
     *      - Async tasks cannot loop, must return to UI thread on completion
     *      - Will get killed if runs too long
     */
    class RequestRunnable extends AsyncTask<Void,Void,Void> {
        JSONObject obj;
        String endPointURL;
        String requestType;
        static final String GET = "get";
        static final String POST = "post";

        public RequestRunnable(String endPointURL, JSONObject obj, String requestType) {
            this.obj = obj;
            this.endPointURL = endPointURL;
            this.requestType = requestType;
        }

        public RequestRunnable(String endPointURL, String requestType) {
            this.endPointURL = endPointURL;
            this.requestType = requestType;
        }

        /*
         * runs in thread separate from TimerTask and UI thread
         */
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
                } else {
                    Log.d("GET", "GET was null");
                }
            }
            return null;
        }

        /*
         * can only update UI from UI thread, this method runs on UI thread
         *      - runs after doInBackground exists
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            changeState(status);
            super.onPostExecute(aVoid);
        }
    }

    /*
     *  Initialize ui components and fetch the initial data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        modeIcon = (ImageView) findViewById(R.id.mode);
        temperature = (TextView) findViewById(R.id.current_temp);
        setTemp = (TextView) findViewById(R.id.set_temp_value);
        statusString = (TextView) findViewById(R.id.status_string);
        up = (ImageButton) findViewById(R.id.up);
        down = (ImageButton) findViewById(R.id.down);
        b = (Button) findViewById(R.id.update);

        loading = (TextView) findViewById(R.id.loading);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Get status
        getPastState();
    }

    /*
     *  Resume suspended timer tasks after pausing the app (i.e. when no longer in foreground)
     */
    @Override
    protected void onResume() {
        super.onResume();
        fetchTimer = new Timer();
        fetchTimer.schedule(new fetchThermostatStatus(), 0, 1000);
        scheduleTimer = new Timer();
        scheduleTimer.schedule(new checkThermostatSchedule(), 500, 1000);
        b.setText("pause updating");
    }

    /*
     *  Suspend timer tasks while app is in background
     */
    @Override
    protected void onPause() {
        super.onPause();
        fetchTimer.cancel();
        fetchTimer.purge();
        scheduleTimer.cancel();
        scheduleTimer.purge();
        b.setText("start updating");
    }

    /*
     * Enable UI
     */
    public void initializeLayout() {
        loading.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        modeIcon.setVisibility(View.VISIBLE);
        temperature.setVisibility(View.VISIBLE);
        statusString.setVisibility(View.VISIBLE);
        setTemp.setVisibility(View.VISIBLE);
        up.setVisibility(View.VISIBLE);
        down.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);
    }

    /*
     *  Based on the time, find the current place in the schedule
     */
    private void findNextScheduleItem(List<ScheduleItem> schedule) {
        Log.d("FIND NEXT", "in");
        Iterator i = schedule.iterator();
        long timeId = System.currentTimeMillis();

        // note, we are modeling 1 hour for every 1 second
        long millisToHours = ((timeId - originTime) / 1000);
        int day = (int) (millisToHours / 24) % 7;
        int hour = (int) (millisToHours % 24);

        ScheduleItem item;
        upcomingItem = schedule.get(0);
        while (i.hasNext()) {
            item = (ScheduleItem) i.next();
//            Log.d("FIND NEXT", "getting next schedule item");
            if (day <= item.getDay() && hour < item.getHour()) {
//                Log.d("SCHEDULE", "found next schedule item: " + "(" + day + ", " + hour + ") " + item.getDay() + ", " + item.getHour());
                upcomingItem = item;
                break;
            }
        }
    }

    /*
     *  Based on menu click, change the mode of the thermostat
     */
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

    /*
     * kick off the timer tasks
     */
    public void fetchUpdate(View view) {
        b = (Button) view;
        if (b.getText().equals("pause updating")) {
            fetchTimer.cancel();
            fetchTimer.purge();
            scheduleTimer.cancel();
            scheduleTimer.purge();
            b.setText("start updating");
        } else {
            fetchTimer = new Timer();
            fetchTimer.schedule(new fetchThermostatStatus(), 0, 1000);
            scheduleTimer = new Timer();
            scheduleTimer.schedule(new checkThermostatSchedule(), 500, 1000);
            b.setText("pause updating");
        }
    }

    /*
     *  Initial status async task
     */
    public void getPastState() {
        InitRequestRunnable irr = new InitRequestRunnable();
        irr.execute();
    }

    /*
     *  Recurring async task
     */
    public void getState(String url) {
        RequestRunnable rr = new RequestRunnable(url, RequestRunnable.GET);
        rr.execute();
    }

    /*
     *  Set up the UI to reflect the current status
     */
    public void changeState(ThermostatStatus status) {
//        Current temp
        temperature = (TextView) findViewById(R.id.current_temp);
        temperature.setText(status.getTemp() + "");

//        Set temp
        setTemp = (TextView) findViewById(R.id.set_temp_value);
        //setTemp.setText(setPoint.getTemperature() + "");
        setTemp.setText(status.getSetPoint() + "");

//        Mode
        modeIcon = (ImageView) findViewById(R.id.mode);
        switch (status.getMode()) {
            case ThermostatStatus.HEATMODE:
                disableText(false);
                setStatusStringColor();
                modeIcon.setImageDrawable(getDrawable(R.drawable.flame));
                break;
            case ThermostatStatus.COOLMODE:
                disableText(false);
                setStatusStringColor();
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

    /*
     *  Set the status to the appropriate string and color
     */
    private void setStatusStringColor() {
        if (status.isCoolOn()) {
            statusString.setText(ThermostatStatus.COOLING);
            statusString.setTextColor(getResources().getColor(R.color.cool));
            Log.d("COOLING", "cooling");
        } else if (status.isHeatOn()) {
            statusString.setText(ThermostatStatus.HEATING);
            statusString.setTextColor(getResources().getColor(R.color.heat));
            Log.d("HEATING", "heating");
        } else if (!status.isHeatOn() && !status.isCoolOn()){
            statusString.setText(ThermostatStatus.HOLDING);
            statusString.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
    }

    /*
     *  Change colors based on whether the mode is off
     */
    private void disableText(boolean disable) {
        if (disable) {
            temperature.setTextColor(getResources().getColor(R.color.colorDisabledText));
            statusString.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            setTemp.setTextColor(getResources().getColor(R.color.colorDisabledText));
        } else {
            temperature.setTextColor(getResources().getColor(R.color.colorPrimaryText));
            statusString.setTextColor(getResources().getColor(R.color.colorSecondaryText));
            setTemp.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        }
    }

    /*
     *  OnClick set temp method, called when up or down is pressed
     */
    public synchronized void changeSetTemp(View view) {
        long timeId = System.currentTimeMillis();

        int viewId = view.getId();

        if (viewId == R.id.up) {
            status.up();
        } else if (viewId == R.id.down) {
            status.down();
        }
        Log.d("getTemperature", "" + status.getSetPoint());
        setTemp.setText("" + status.getSetPoint());

        updateServerSetTemp(timeId, status.getSetPoint());
    }

    /*
     * Create the JSON to post the new set point to the server
     */
    public void updateServerSetTemp(long id, int temp) {

        JSONObject manualUpdate = new JSONObject();
        manualUpdate.put("Id", "" + id);
        manualUpdate.put("setTemp", "" + temp);

        RequestRunnable rr = new RequestRunnable(setTempURL, manualUpdate, RequestRunnable.POST);
        rr.execute();
    }

    /*
     * Create the menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        TODO: fix default
        int modeId = ThermostatStatus.HEATMODE;

        try {
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
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /*
     * Fill out the menu based on options
     */
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

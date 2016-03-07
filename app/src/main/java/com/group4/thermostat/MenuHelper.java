package com.group4.thermostat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;

import org.json.simple.JSONObject;

/**
 * Created by marisayeung on 3/6/16.
 */
public class MenuHelper {

    static String endPointURL = "http://52.37.144.142:9000/application";
    ThermostatStatus status;

    public static boolean handleOnItemSelected(Context context, MenuItem item, ThermostatStatus status) {
        int id = item.getItemId();

//      Uninstall the app
        if (id == R.id.action_uninstall) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
//      Show the schedule
        if (id == R.id.action_schedule) {
            Intent intent = new Intent(context, Schedule.class);
            context.startActivity(intent);
        }

//        TODO: fix so that there is never NO box checked
        if (item.getGroupId() == R.id.group_mode) {
            int newMode;
            long requestId = System.currentTimeMillis();

            if (id == R.id.mode_cool) {
                if (item.isChecked() == false) {
                    item.setChecked(true);
                    newMode = ThermostatStatus.COOLMODE;
                    status.setMode(newMode);
                    updateServerMode(requestId, newMode);
                }
            } else if (id == R.id.mode_heat) {
                if (item.isChecked() == false) {
                    item.setChecked(true);
                    newMode = ThermostatStatus.HEATMODE;
                    status.setMode(newMode);
                    updateServerMode(requestId, newMode);
                }
            } else {
                if (item.isChecked() == false) {
                    item.setChecked(true);
                    newMode = ThermostatStatus.OFFMODE;
                    status.setMode(newMode);
                    updateServerMode(requestId, newMode);
                }
            }


        }

        return true;
    }

    public static void updateServerMode(long id, int mode) {

        JSONObject modeUpdate = new JSONObject();
        modeUpdate.put("Id", "" + id);
        modeUpdate.put("mode", "" + mode);
        Log.d("JSON", modeUpdate.toString());
        RequestRunnable rr = new RequestRunnable(endPointURL, modeUpdate);
        rr.execute();
    }

    static class RequestRunnable extends AsyncTask<Void,Void,Void> {
        JSONObject obj;
        String endPointURL;

        public RequestRunnable(String endPointURL, JSONObject obj) {
            this.obj = obj;
            this.endPointURL = endPointURL;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Request request = new Request();
            JSONObject posted = request.postRequest(endPointURL, obj);
            if (posted != null) {
                Log.d("POST", posted.toString());
            }
            return null;
        }
    }
}

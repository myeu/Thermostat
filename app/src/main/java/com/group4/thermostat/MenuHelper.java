package com.group4.thermostat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;

/**
 * Created by marisayeung on 3/6/16.
 */
public class MenuHelper {
    public static boolean handleOnItemSelected(Context context, MenuItem item) {
        int id = item.getItemId();

//      Uninstall the app
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
//      Show info about the Zoo
        if (id == R.id.action_schedule) {
            Intent intent = new Intent(context, Schedule.class);
            context.startActivity(intent);
        }

        return true;
    }
}

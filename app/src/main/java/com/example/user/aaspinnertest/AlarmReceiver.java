package com.example.user.aaspinnertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by User on 9/10/2014.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
    }
}

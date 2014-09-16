package com.example.user.aaspinnertest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class StartCPUWake extends Service {
    private static final String TAG = "StartCpuWake";
    Alarm alarm = new Alarm();
    //for commit
    public StartCPUWake(){
    }

    public void onCreate() {
        Log.d(TAG, "StartCpuWake created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "StartCpu onStart");
        alarm.setAlarm(StartCPUWake.this);
        Log.d(TAG, "StartCpu set alarm");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

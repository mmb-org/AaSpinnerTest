package com.example.user.aaspinnertest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StopCPUWake extends Service {
    Alarm alarm = new Alarm();
    //for commit
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarm.cancelAlarm(StopCPUWake.this);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

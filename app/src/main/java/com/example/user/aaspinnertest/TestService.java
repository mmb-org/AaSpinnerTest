package com.example.user.aaspinnertest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by User on 9/10/2014.
 */
public class TestService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "Service created", Toast.LENGTH_SHORT).show();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), "Service destroyed", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Service Running", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
}

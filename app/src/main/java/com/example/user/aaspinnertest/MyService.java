package com.example.user.aaspinnertest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";
    //used for getting the handler from other class for sending messages
    public static Handler mMyServiceHandler = null;
    //used to keep track on Android running status
    public static Boolean mIsServiceRunning = false;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "MyService started", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int StartId) {
        MyThread myThread = new MyThread();
        myThread.start();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        mIsServiceRunning = true; //set service running status to true

        Toast.makeText(this, "Congrats, MyService started", Toast.LENGTH_SHORT).show();
        //we need to return if we want to handle this service explicitly
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "MyService stopped", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onDestroy");

        mIsServiceRunning = false; //set service running status to false as it is destroyed
    }

    //Inner thread class is here to get response from Activity and process
    class MyThread extends Thread {
        private static final String INNER_TAG = "MyThread";
        private final static String TAG = "SpinnerTest";

        public void run() {
            this.setName(INNER_TAG);

            //prepare the looper before creating handler
            Looper.prepare();
            mMyServiceHandler = new Handler() {
                public void handleMessage(Message msg) {
                    Log.i("Background Thread", "handleMessage(Message msg)");
                    switch(msg.what) {
                        case 0: //we send message with what value =0 from the activity. here it is
                            //Reply to the activity from here using the same process handle.sendMessage()
                            //So first get the Activity Handler and then send the message
                            if(null != MainActivity.mUiHandler) {
                                Message msgToActivity = new Message();
                                msgToActivity.what = 0;
                                if(true == mIsServiceRunning) {
                                    msgToActivity.obj = "Request received, service is running \n" + msg.obj;
                                }
                                else {
                                    msgToActivity.obj = "Request received, service is not running";
                                }
                                MainActivity.mUiHandler.sendMessage(msgToActivity);
                            }
                            break;
                        case 1:
                            Log.d(TAG,"MyService case 1");
                            if(null != MainActivity.mUiHandler) {
                                Log.d(TAG, "Enters if statement 1");
                                Message msgToActivity = new Message();
                                msgToActivity.what = 1;
                                if(true == mIsServiceRunning) {
                                    Log.d(TAG, "Enters if statement 2");
                                    msgToActivity.obj = "Age is " + msg.obj;
                                }
                                else {
                                    msgToActivity.obj = "Request received, service is not running";
                                }
                                MainActivity.mUiHandler.sendMessage(msgToActivity);
                            }
                            break;
                        default:
                            break;
                    }
                }
            };
            Looper.loop();
        }
    }
}

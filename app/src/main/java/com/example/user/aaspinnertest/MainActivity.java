package com.example.user.aaspinnertest;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.util.Log;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class MainActivity extends Activity {
    private Spinner ageSpinner;
    private Spinner weightSpinner;
    private EditText favNoText;
    private final static String TAG = "SpinnerTest";
    public static Handler mUiHandler = null;

    //variable for periodSvc
    private AlarmManager alarm;

    //variables for Alarm
    private PendingIntent pendingIntent;
    private AlarmManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        mUiHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 0:
                        //add the status which came from service and show on GUI
                        Toast.makeText(MainActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "MainActivity case 0");
                        break;
                    case 1:
                        Log.d(TAG, "MainActivity case 1");
                        Toast.makeText(MainActivity.this, msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

        addItemsToAgeSpinner();
        addItemsToWeightSpinner();
        addListenerToSpinner();
    }

    public void startCpuWake(View V) {
        Log.d(TAG, "startcpuwake");
        startService(new Intent(this, StartCpuWake.class));
    }
    //TODO: see if can stop alarm by using msg codes instead of calling another service
    public void stopCpuWake(View V) {
        Log.d(TAG, "stopcpuwake");
        startService(new Intent(this, StopCpuWake.class));
    }

    //Start service using AlarmManager
    public void startPeriodSvc(View V){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);
        Intent intent = new Intent(this, TestService.class);
        final PendingIntent pIntent = PendingIntent.getService(this, 11, intent, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //for 5 sec
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),5*1000, pIntent);

        startService(new Intent(getBaseContext(), TestService.class));
    }

    //Stop service and AlarmManager
    public void stopPeriodSvc(View V) {
        Intent intent = new Intent(this, TestService.class);
        final PendingIntent pIntent = PendingIntent.getService(this, 11, intent, 0);
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        stopService(new Intent(getBaseContext(), TestService.class));
        alarm.cancel(pIntent);
    }

    //set recurring alarms
    public void startAlarm(View view) {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    //stop the alarms
    public void cancelAlarm(View view) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm canceled", Toast.LENGTH_SHORT).show();

    }

    public void onClickStartSvc(View V) {
        //start the service from here //MyService is your service class name
        startService(new Intent(this, MyService.class));
    }

    public void onClickStopSvc(View V) {
        //Stop the running service from here//MyService is your service class name
        //Service will only stop if it is already running.
        stopService(new Intent(this, MyService.class));
    }

    public void onClickSendMsg(View V) {
        //only we need a handler to send message to any component.
        //here we will get the handler from the service first, then
        //we will send a message to the service.
        ageSpinner = (Spinner) findViewById(R.id.ageSpinner);

        if(null != MyService.mMyServiceHandler) {
            //first build the message and send.
            //put a integer value here and get it from the service handler
            //For Example: lets use 0 (msg.what = 0;) for getting service running status from the service
            Message msg = new Message();
            msg.what = 0;
            msg.obj = String.valueOf(ageSpinner.getSelectedItem());
            MyService.mMyServiceHandler.sendMessage(msg);
        }
    }
    public void createNotification(View view) {
        // prepare intent which is triggered if the notification is selected
        Intent intent = new Intent(this, NotificationReceiverActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //build notification
        //actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("New mail" + "from")
                .setContentText("Subject").setSmallIcon(R.drawable.icon)
                .setContentIntent(pIntent)
                .addAction(R.drawable.icon, "Call", pIntent)
                .addAction(R.drawable.icon, "More", pIntent)
                .addAction(R.drawable.icon, "And More", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //hide notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }

    public void addItemsToAgeSpinner(){
        ageSpinner = (Spinner) findViewById(R.id.ageSpinner);
        List<String> list = new ArrayList<String>();
        list.add("Please select your age");
        list.add("<21");
        list.add("21-40");
        Log.d(TAG,"adding list");
        list.add(">40");

        ArrayAdapter<String> ageDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        ageDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageDataAdapter);
    }

    public void addItemsToWeightSpinner(){
        weightSpinner = (Spinner) findViewById(R.id.weightSpinner);
        List<String> list = new ArrayList<String>();
        list.add("Please select your weight");
        list.add("<70kg");
        list.add("70-79kg");
        list.add(">79 kg");

        ArrayAdapter<String> weightDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        weightDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightDataAdapter);
    }

    public void addListenerToSpinner(){
        ageSpinner = (Spinner) findViewById(R.id.ageSpinner);
        ageSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        weightSpinner = (Spinner) findViewById(R.id.weightSpinner);
        weightSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }


    public void buttonSend(View view){
        ageSpinner = (Spinner) findViewById(R.id.ageSpinner);
        weightSpinner = (Spinner) findViewById(R.id.weightSpinner);
        favNoText = (EditText) findViewById(R.id.favNoText);

        Toast.makeText(MainActivity.this,
                "Result: " +
                        "\nAge: " + String.valueOf(ageSpinner.getSelectedItem()) +
                        "\nWeight: " + String.valueOf(weightSpinner.getSelectedItem()) +
                        "\nFavourite Number: " + favNoText.getText().toString(),Toast.LENGTH_SHORT).show();
        Log.d(TAG,"making toast");
        if(null != MyService.mMyServiceHandler) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = String.valueOf(ageSpinner.getSelectedItem());
            MyService.mMyServiceHandler.sendMessage(msg);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.apps.pixelarium.contactemp;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class TemporizerActivity extends AppCompatActivity {
    private static final String TAG = "programmedContacts";
    private DataModel dataModel;
    private TextView textviewName;
    private TextView textviewNumber;
    private CheckBox checkBox;
    private DatePicker datePicker;
    private ActionBar actionBar;
    private ArrayList<DataModel> programmedContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporizer);
        dataModel = (DataModel) getIntent().getSerializableExtra("dataModel");
        textviewName = (TextView)findViewById(R.id.textview_name);
        textviewNumber = (TextView)findViewById(R.id.textview_number);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        datePicker = (DatePicker)findViewById(R.id.datePicker);


        /*Print data contact*/
        textviewName.setText(dataModel.getName());
        textviewNumber.setText(dataModel.getNumber());
        if(readProgrammedContacts() == null){
            programmedContacts = new ArrayList<DataModel>();
        }else{
            programmedContacts = readProgrammedContacts();
        }
        /*Init datePicker and date change listener*/
        MyOnDateChangeListener onDateChange = new MyOnDateChangeListener();
        datePicker.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH), onDateChange);

        if(dataModel.isProgrammed()){
            checkBox.setChecked(true);
            datePicker.setVisibility(View.VISIBLE);
            Calendar calendar = dataModel.getProgrammedDate();
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH), onDateChange);
        }else{
            datePicker.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH), onDateChange);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // isChecked means that the check button is activated now
                if(checkBox.isChecked()) {
                    datePicker.setVisibility(View.VISIBLE);
                    dataModel.setProgrammed(true);
                    dataModel.setProgrammedDate(getDatePickerDate());
                    programmedContacts.add(dataModel);
                    saveProgrammedContacts();
                    scheduleTask();

                }else{
                    datePicker.setVisibility(View.INVISIBLE);
                    dataModel.setProgrammed(false);

                    readAndSaveProgrammedContacts();
                }
            }
        });


        setUpToolbar();

        readProgrammedContacts();

    }

    private void scheduleTask() {
        scheduleNotification(getApplicationContext(),5000,1);
    }

    private Calendar getDatePickerDate() {
        Calendar programmedDate = new GregorianCalendar();
        programmedDate.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        programmedDate.set(Calendar.MONTH,datePicker.getMonth());
        programmedDate.set(Calendar.YEAR,datePicker.getYear());
        return programmedDate;
    }

    private void readAndSaveProgrammedContacts() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, null);
        Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
        programmedContacts = gson.fromJson(json, type);
        for (int i = 0; i < programmedContacts.size() ; i++) {
            if(programmedContacts.get(i).getNumber().equals(dataModel.getNumber())){
                programmedContacts.remove(i);
            }
        }
        saveProgrammedContacts();
    }

    private void saveProgrammedContacts() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(programmedContacts);
        editor.putString(TAG, json);
        editor.commit();
    }

    public class MyOnDateChangeListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            for (int i = 0; i <programmedContacts.size() ; i++) {
                if(programmedContacts.get(i).getNumber().equals(dataModel.getNumber())){
                    programmedContacts.get(i).setProgrammedDate(getDatePickerDate());
                    saveProgrammedContacts();
                    break;
                }
            }
        }
    }
    private ArrayList<DataModel> readProgrammedContacts() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, null);
        Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void setUpToolbar() {
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    //TODO https://stackoverflow.com/questions/25713157/generate-int-unique-id-as-android-notification-id
    // TODO https://stackoverflow.com/questions/36902667/how-to-schedule-notification-in-android
    public void scheduleNotification(Context context, long delay, int notificationId) {//delay is after how much time(in millis) from current time you want to schedule the notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.timer)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        synchronized (mBuilder){
            notificationManager.notify(1, mBuilder.build());
        }
    }

}

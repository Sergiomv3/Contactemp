package com.apps.pixelarium.contactemp;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
            Toast.makeText(TemporizerActivity.this, year+"."+month+"."+day, Toast.LENGTH_SHORT).show();
            //TODO save date
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
}

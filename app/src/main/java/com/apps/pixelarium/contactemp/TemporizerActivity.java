package com.apps.pixelarium.contactemp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class TemporizerActivity extends AppCompatActivity {
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
        programmedContacts = new ArrayList<DataModel>();

        /*Print data contact*/
        textviewName.setText(dataModel.getName());
        textviewNumber.setText(dataModel.getNumber());

        MyOnDateChangeListener onDateChange = new MyOnDateChangeListener();
        datePicker.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH), onDateChange);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // isChecked means that the check button is activated now
                if(checkBox.isChecked()) {
                    datePicker.setVisibility(View.VISIBLE);
                    programmedContacts.add(dataModel);
                    //TODO GSON https://stackoverflow.com/questions/22984696/storing-array-list-object-in-sharedpreferences
                }else{
                    datePicker.setVisibility(View.INVISIBLE);
                }
            }
        });


        setUpToolbar();

        readProgrammedContacts();

    }

    public class MyOnDateChangeListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            Toast.makeText(TemporizerActivity.this, year+"."+month+"."+day, Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList<DataModel> readProgrammedContacts() {
        //TODO read programed contacts arraylist
        return null;
    }

    private void setUpToolbar() {
        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}

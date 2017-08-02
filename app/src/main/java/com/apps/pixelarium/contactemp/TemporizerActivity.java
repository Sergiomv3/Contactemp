package com.apps.pixelarium.contactemp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toolbar;

public class TemporizerActivity extends Activity {
    private DataModel dataModel;
    private TextView textviewName;
    private TextView textviewNumber;
    private CheckBox checkBox;
    private DatePicker datePicker;
    private ActionBar actionBar;
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
        
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // isChecked means that the check button is activated now
                if(checkBox.isChecked()) {
                    datePicker.setVisibility(View.VISIBLE);
                }else{
                    datePicker.setVisibility(View.INVISIBLE);
                }
            }
        });

        setUpToolbar();

    }

    private void setUpToolbar() {
        actionBar = getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}

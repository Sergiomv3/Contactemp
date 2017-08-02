package com.apps.pixelarium.contactemp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class TemporizerActivity extends AppCompatActivity {
    private DataModel dataModel;
    private TextView textviewName;
    private TextView textviewNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporizer);
        dataModel = (DataModel) getIntent().getSerializableExtra("dataModel");
        textviewName = (TextView)findViewById(R.id.textview_name);
        textviewNumber = (TextView)findViewById(R.id.textview_number);

        /*Print data contact*/
        textviewName.setText(dataModel.getName());
        textviewNumber.setText(dataModel.getNumber());
    }
}

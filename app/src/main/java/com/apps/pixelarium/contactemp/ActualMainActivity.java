package com.apps.pixelarium.contactemp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActualMainActivity extends Activity {

    private static final String TAG = "programmedContacts";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ArrayList<DataModel> alContacts;
    private ArrayList<DataModel> programmedContacts;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_main);

        loadContacts();
        if(alContacts == null){
            alContacts = new ArrayList<DataModel>();
        }
        listView=(ListView)findViewById(R.id.list);
        adapter= new CustomAdapter(alContacts,getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel= alContacts.get(position);
                Intent intent = new Intent(getApplicationContext(), TemporizerActivity.class);
                intent.putExtra("dataModel", dataModel);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        alContacts = fetchProgrammedContacts(alContacts);

    }

    private ArrayList<DataModel> fetchProgrammedContacts(ArrayList<DataModel> alContacts) {
        if(readProgrammedContacts() == null){
            programmedContacts = new ArrayList<DataModel>();
        }else{
            programmedContacts = readProgrammedContacts();
            for (int i = 0; i <alContacts.size() ; i++) {
                alContacts.get(i).setProgrammed(false);
            }
            for (int i = 0; i <alContacts.size() ; i++) {
                for (int j = 0; j <programmedContacts.size() ; j++) {
                    if(alContacts.get(i).getNumber().equals(programmedContacts.get(j).getNumber())){
                        alContacts.get(i).setProgrammed(true);
                        alContacts.get(i).setProgrammedDate(programmedContacts.get(j).getProgrammedDate());
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
        return alContacts;
    }

    private void loadContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            readContacts();
        }

    }

    private void readContacts() {
        ContentResolver cr = this.getContentResolver(); //Activity/Application android.content.Context
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst()) {
            alContacts = new ArrayList<DataModel>();
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        DataModel e = new DataModel(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)), pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                        alContacts.add(e);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }
        Collections.sort(alContacts);
    }

    private void requestPermission() {
        /** REQUEST PERMISSION **/
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    loadContacts();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private ArrayList<DataModel> readProgrammedContacts() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, null);
        Type type = new TypeToken<ArrayList<DataModel>>() {}.getType();
        return gson.fromJson(json, type);
    }


}

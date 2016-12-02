package com.vitche.sms.hub.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Constants;
import com.vitche.sms.hub.model.Source;

public class NewSourceActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";


    Button btnFromContacts;
    Button btnSaveSource;
    EditText etPhoneNumber;
    EditText etPhoneDescription;
    Spinner spDeleteSMS;

//    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_source);

//        prefs = getSharedPreferences(Constants.SMSHUB_SETTINS_PREFS , MODE_PRIVATE);

        btnFromContacts = (Button)findViewById(R.id.btn_from_contacts);
        btnSaveSource = (Button)findViewById(R.id.btn_save_source_settings);
        btnFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromeContacts();
            }
        });
        btnSaveSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePrefs();
                finish();
            }
        });


        etPhoneNumber = (EditText)findViewById(R.id.et_phone_number);
        etPhoneDescription = (EditText)findViewById(R.id.et_phone_decription);
        spDeleteSMS = (Spinner)findViewById(R.id.spinner_del_sms);
//        TODO validate phone number
//        String phoneNumber = prefs.getString(Constants.SETTINS_PHONE_NUMBER, "");
//        if (phoneNumber != null && !phoneNumber.isEmpty()){
//            etPhoneNumber.setText(phoneNumber);
//        }

//        TODO spinner
        final String[] data = {"at mometn", "after 1 min", "after 1 hour", "after 1 day", "never"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDeleteSMS.setAdapter(adapter);
        spDeleteSMS.setSelection(4);
        spDeleteSMS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(getBaseContext(), "TODO del"  + data[position], Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Log.e(TAG, "------NewSourceActivity : onBackPressed: ");
        savePrefs();
    }

    private void savePrefs() {
//        if (prefs == null){
//            prefs = getSharedPreferences(Constants.SMSHUB_SETTINS_PREFS , MODE_PRIVATE);
//        }
//        SharedPreferences.Editor ed = prefs.edit();
//
//        if (etPhoneNumber != null) {
//            String phoneNumber = etPhoneNumber.getText().toString();
//            if (phoneNumber != null && !phoneNumber.isEmpty())
//                ed.putString(Constants.SETTINS_PHONE_NUMBER, phoneNumber);
//        }
//        ed.commit();
        if (etPhoneNumber != null) {
            String phoneNumber = etPhoneNumber.getText().toString();
            if (phoneNumber != null && !phoneNumber.isEmpty())
                SourceDB.createSource(this, phoneNumber, etPhoneDescription.getText().toString());
        }
    }

    private void chooseFromeContacts() {
        Log.d(TAG, "------SettingsActivity : chooseFromeContacts: ");
        Toast.makeText(this, "TODO chooseFromeContacts", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        savePrefs();
//    }
}

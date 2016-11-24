package com.vitche.sms.hub.view;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Constants;
import com.vitche.sms.hub.model.Source;

public class NewSourceActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";


    Button btnFromContacts;
    EditText etPhoneNumber;

//    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_source);

//        prefs = getSharedPreferences(Constants.GUARD_SETTINS_PREFS , MODE_PRIVATE);

        btnFromContacts = (Button)findViewById(R.id.btn_from_contacts);
        btnFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromeContacts();
            }
        });


        etPhoneNumber = (EditText)findViewById(R.id.et_phone_number);
//        TODO validate phone number
//        String phoneNumber = prefs.getString(Constants.SETTINS_PHONE_NUMBER, "");
//        if (phoneNumber != null && !phoneNumber.isEmpty()){
//            etPhoneNumber.setText(phoneNumber);
//        }

    }

    private void savePrefs() {
//        if (prefs == null){
//            prefs = getSharedPreferences(Constants.GUARD_SETTINS_PREFS , MODE_PRIVATE);
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
                SourceDB.createSource(this, phoneNumber, "TODO DECR Field on screen");
        }
    }

    private void chooseFromeContacts() {
        Log.d(TAG, "------SettingsActivity : chooseFromeContacts: ");
        Toast.makeText(this, "TODO chooseFromeContacts", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePrefs();
    }
}

package com.vitche.sms.hub.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.PhoneNumberDataSource;

import java.util.List;

public class NewSourceActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";
    static final int PICK_CONTACT_REQUEST = 1;

    Button btnFromContacts;
//    Button btnSaveSource;
    EditText etPhoneNumber;
    EditText etPhoneDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_source);

        btnFromContacts = (Button) findViewById(R.id.btn_from_contacts);
//        btnSaveSource = (Button) findViewById(R.id.btn_save_source_settings);
        btnFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChooser();
            }
        });
//        btnSaveSource.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                savePrefs();
//                finish();
//            }
//        });

        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etPhoneDescription = (EditText) findViewById(R.id.et_phone_decription);
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etPhoneNumber.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
//        TODO validate phone number
    }

    @Override
    public void onBackPressed() {
        if (phoneNumberValid() && !isObservedPhoneNumber()) {
            super.onBackPressed();
            savePrefs();
        } else {
            showPhoneNumberWarning();
        }

    }

    private void showPhoneNumberWarning() {
        if (etPhoneNumber != null){
            etPhoneNumber.setTextColor(Color.RED);
            Log.d(TAG, "------NewSourceActivity : showPhoneNumberWarning: " + etPhoneNumber.getTextColors().toString());
        }
        Toast.makeText(NewSourceActivity.this, getString(R.string.invalid_phone_format), Toast.LENGTH_SHORT).show();
    }

    private String getPhoneNumberFromET(){
        String phNumber = null;
        if (etPhoneNumber != null) {
            phNumber = etPhoneNumber.getText().toString();
            if (phNumber != null && !phNumber.isEmpty()) {
                return phNumber;
            }
        }
        return null;
    }
    private boolean phoneNumberValid() {
        String phNumber = getPhoneNumberFromET();
        if ( phNumber!= null ) {
            return  PhoneNumberUtils.isGlobalPhoneNumber(phNumber);
        }
        return false;
    }

    private boolean isObservedPhoneNumber() {
        List<PhoneNumberDataSource> list = SourceDB.getAllSorces(this); // TODO check if list size == 0
            String phNumber = getPhoneNumberFromET();
            if ( phNumber!= null ){
                for (PhoneNumberDataSource source : list) {
                    if (phNumber.equals(source.getPhoneNumber()))
                        return true;
                }
            }
        return false;
    }

    private void savePrefs() {
        if (etPhoneNumber != null) {
            String phoneNumber = etPhoneNumber.getText().toString();
            if (phoneNumber != null && !phoneNumber.isEmpty())
                SourceDB.createSource(this, phoneNumber, etPhoneDescription.getText().toString());
        }
    }

    private void startChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT_REQUEST):
                if (resultCode == Activity.RESULT_OK) {
                    Cursor cursor = null;
                    try {
                        Uri contactUri = data.getData();
                        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                        cursor = getContentResolver()
                                .query(contactUri, projection, null, null, null);
                        String number = null;
                        if (cursor.moveToFirst()) {
                            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            number = cursor.getString(column);
                            if (number != null && !number.isEmpty()) {
                                if (etPhoneNumber != null) {
                                    etPhoneNumber.setText(number);
                                }
                            } else {
                                showFuckToast();
                            }
                        } else {
                            Log.e(TAG, "------DebugActivity : onActivityResult: cursor did not moveToFirst");
                            showFuckToast();
                        }

                    } catch (Exception e) {
                        Log.e(TAG, Log.getStackTraceString(e));
                        showFuckToast();
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                }
                break;
        }
    }

    private void showFuckToast() {
        Toast.makeText(NewSourceActivity.this, getString(R.string.choose_number_error), Toast.LENGTH_SHORT).show();
    }

}

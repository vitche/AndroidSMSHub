package com.vitche.sms.hub.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        etPhoneDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "------NewSourceActivity : beforeTextChanged: " + etPhoneDescription.getHintTextColors().toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etPhoneDescription.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(getString(R.string.app_name) + " - " + getString(R.string.new_source));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newsource, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_create_new_source:
                createSource();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createSource() {
        if (isPhoneNumberValid() && !isObservedPhoneNumber()) {
            if (getPhoneDescriptionFromET() != null){
                savePrefs();
                finish();
            }else {
                showPhoneDescriptionWarning();
            }
        } else if(!isPhoneNumberValid()){
            showPhoneNumberWarning();
        }else if (isObservedPhoneNumber()){
            showExistNumberWarning();
        }
    }
    private void showPhoneNumberWarning() {
        if (etPhoneNumber != null){
            etPhoneNumber.setTextColor(Color.RED);
        }
        Toast.makeText(NewSourceActivity.this, getString(R.string.invalid_phone_format), Toast.LENGTH_SHORT).show();
    }

    private void showExistNumberWarning() {
        if (etPhoneNumber != null){
            etPhoneNumber.setTextColor(Color.RED);
        }
        Toast.makeText(NewSourceActivity.this, getString(R.string.phone_number_exist), Toast.LENGTH_SHORT).show();
    }

    private void showPhoneDescriptionWarning() {
        if (etPhoneDescription != null){
            etPhoneDescription.setHintTextColor(Color.RED);
        }

        Toast.makeText(NewSourceActivity.this, getString(R.string.invalid_decription), Toast.LENGTH_SHORT).show();
    }



    private String getPhoneNumberFromET(){
        String phNumber = null;
        if (etPhoneNumber != null) {
            phNumber = etPhoneNumber.getText().toString();
            if (phNumber != null && !phNumber.isEmpty() && phNumber.length() > 9) {
                return phNumber;
            }
        }
        return null;
    }

    private String getPhoneDescriptionFromET(){
        String phDescr = null;
        if (etPhoneDescription != null) {
            phDescr = etPhoneDescription.getText().toString();
            if (phDescr != null && !phDescr.isEmpty()) {
                return phDescr;
            }
        }
        return null;
    }
    private boolean isPhoneNumberValid() {
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
                    if (PhoneNumberUtils.compare(phNumber,source.getPhoneNumber()))
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
    @Override
    public void onBackPressed() {
        if (getPhoneNumberFromET() == null && getPhoneDescriptionFromET() == null){
            super.onBackPressed();
        }else {
            showSaveDialog();
        }
    }

    private void showSaveDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.close_without_saving));
        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }



//    chose from contacts
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

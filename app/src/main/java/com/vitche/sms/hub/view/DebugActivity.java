package com.vitche.sms.hub.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vitche.sms.hub.R;

public class DebugActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";
    static final int PICK_CONTACT_REQUEST = 1;
    Button btnDebug1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        btnDebug1 = (Button) findViewById(R.id.btn_debug1);
        btnDebug1.setText("Del sms");
        btnDebug1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
            }
        });
    }


}


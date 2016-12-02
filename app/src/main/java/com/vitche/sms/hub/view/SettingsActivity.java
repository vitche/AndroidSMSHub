package com.vitche.sms.hub.view;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.MessageListenerService;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Constants;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";

    CheckBox cbEnableService;
        SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(Constants.SMSHUB_SETTINS_PREFS, MODE_PRIVATE);

        cbEnableService = (CheckBox) findViewById(R.id.cb_enable_servise);
        cbEnableService.setChecked(prefs.getBoolean(Constants.ENABLE_SERVICE, true));
        cbEnableService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked) {
                                                               startMessageListenerService();
                                                           } else {
                                                               showDisableDialog();
                                                           }
                                                       }
                                                   }
        );
    }



    private void stopMessageListenerService() {
        if (isMyServiceRunning(MessageListenerService.class)) {
            stopService(new Intent(this, MessageListenerService.class));
            Toast.makeText(this, getString(R.string.service_stopped), Toast.LENGTH_SHORT).show();
        }
    }

    private void startMessageListenerService() {
        if (!isMyServiceRunning(MessageListenerService.class)) {
            startService(new Intent(this, MessageListenerService.class));
            Toast.makeText(this, getString(R.string.service_started), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDisableDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.disable_service_title));
        dialog.setMessage(getString(R.string.disable_service_message));
        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopMessageListenerService();
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

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        savePrefs();
    }

    private void savePrefs() {
        if (prefs == null){
            prefs = getSharedPreferences(Constants.SMSHUB_SETTINS_PREFS , MODE_PRIVATE);
        }
        SharedPreferences.Editor ed = prefs.edit();

        if (cbEnableService != null) {
                ed.putBoolean(Constants.ENABLE_SERVICE, cbEnableService.isChecked());
        }
        ed.commit();
    }
}

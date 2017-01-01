package com.vitche.sms.hub.view;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vitche.sms.hub.BuildConfig;
import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.MessageListenerService;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Constants;
import com.vitche.sms.hub.model.Message;
import com.vitche.sms.hub.model.PhoneNumberDataSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SMSHubMainActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";
    private static final int MENU_DEBUG_SCREEN = 1;

    ListView lvSources;
    SourcesAdapter sourcesAdapter;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences(Constants.SMSHUB_SETTINS_PREFS, MODE_PRIVATE);

        sourcesAdapter = new SourcesAdapter(getSoucesList());

        lvSources = (ListView) findViewById(R.id.lv_sources);
        lvSources.setEmptyView(findViewById(R.id.empty));
        lvSources.setAdapter(sourcesAdapter);

        lvSources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sourceId = getSoucesList().get(i).getPhoneNumber();
                Intent intent = new Intent(SMSHubMainActivity.this, SourceActivity.class);
                intent.putExtra(Constants.CLICKED_ITEM_ID, "" + sourceId);
                startActivity(intent);
            }
        });

        lvSources.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDeleteDialog(getSoucesList().get(i).getPhoneNumber());
                return true;
            }
        });

        if (prefs.getBoolean(Constants.ENABLE_SERVICE, true))
            if (!isMyServiceRunning(MessageListenerService.class)) {
                startService(new Intent(this, MessageListenerService.class));
            }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(getString(R.string.app_name) + " - " + getString(R.string.sources_list_header));
        }

//        SMSNotification.setMessagesNum(0);
//        SMSNotification.updateNotification(this, "" + SourceDB.getAllSorces(this).size());
//        Log.e(TAG, "------SMSHubMainActivity : onCreate: ");
    }

    private void showDeleteDialog(final String sourceUID) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.delete_source) + " " + '"' + sourceUID + '"' + '?');
        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteSource(sourceUID);
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

    private void deleteSource(String sourceUID) {
        SourceDB.deleteSource(this, sourceUID);
        updateListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
        SMSNotification.setMessagesNum(0);
        SMSNotification.updateNotification(this, "" + SourceDB.getAllSorces(this).size());
        Log.e(TAG, "------SMSHubMainActivity : onResume: ");
    }

    private void updateListView() {
        sourcesAdapter = new SourcesAdapter(getSoucesList());
        SMSNotification.updateNotification(this, null);
        if (lvSources != null) {
            lvSources.setAdapter(sourcesAdapter);
            sourcesAdapter.notifyDataSetChanged();
            lvSources.invalidate();
        }
    }


    private List<PhoneNumberDataSource> getSoucesList() {
        return SourceDB.getAllSorcesSorted(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (BuildConfig.DEBUG) {
            menu.add(0, MENU_DEBUG_SCREEN, 0, "Start debug Activity");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_add_source:
                Intent intent = new Intent(this, NewSourceActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
        }
        if (BuildConfig.DEBUG) {
            if (id == MENU_DEBUG_SCREEN) {
                Intent intent = new Intent(this, DebugActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
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

    class SourcesAdapter extends BaseAdapter {
        List<PhoneNumberDataSource> sourceList;

        SourcesAdapter(List<PhoneNumberDataSource> list) {
//            Log.d(TAG, "------SourcesAdapter : SourcesAdapter: list size = " + list.size());
            sourceList = list;
        }

        class ViewHolder {
            public TextView tvPhoneNumber;
            public TextView tvPhoneName;
            public TextView tvLastSMSdate;
            public TextView tvLastSMSbody;
        }

        @Override
        public int getCount() {
            if (sourceList != null) {
                return sourceList.size();
            } else return 0;
        }

        @Override
        public Object getItem(int i) {
            return sourceList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            PhoneNumberDataSource source = sourceList.get(i);

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_source, viewGroup, false);

                holder = new ViewHolder();

                holder.tvPhoneNumber = (TextView) view.findViewById(R.id.tv_item_tel_number);
                holder.tvPhoneName = (TextView) view.findViewById(R.id.tv_item_tel_name);
                holder.tvLastSMSdate = (TextView) view.findViewById(R.id.tv_item_date);
                holder.tvLastSMSbody = (TextView) view.findViewById(R.id.tv_item_body);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.tvPhoneNumber.setText(source.getPhoneNumber());
            holder.tvPhoneName.setText(source.getDecription());
            List<Message> messagesList = source.getMessages();
            if (messagesList.size() > 0) {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(messagesList.get(Constants.LAST_SMS_INDEX).getId()));
                holder.tvLastSMSdate.setText(date);
                holder.tvLastSMSbody.setText(messagesList.get(Constants.LAST_SMS_INDEX).getBody());
            } else {
                holder.tvLastSMSdate.setText(getString(R.string.no_messages));
            }
            return view;
        }
    }
}

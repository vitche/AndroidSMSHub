package com.vitche.sms.hub.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.db.MainAppDB;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Constants;
import com.vitche.sms.hub.model.PhoneNumberDataSource;
import com.vitche.sms.hub.model.Source;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myLogs";
    Button btnStart;
    Button btnStop;
    ListView lvSources;
    SourcesAdapter sourcesAdapter;


    //    TODO notification
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourcesAdapter = new SourcesAdapter(getSoucesList());

        lvSources = (ListView) findViewById(R.id.lv_sources);
        lvSources.setEmptyView(findViewById(R.id.empty));
        lvSources.setAdapter(sourcesAdapter);

        lvSources.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sourceId = getSoucesList().get(i).getTelNumber();
                Intent intent = new Intent(MainActivity.this, SourceActivity.class);
                intent.putExtra(Constants.CLICKED_ITEM_ID, "" + sourceId);
                startActivity(intent);
            }
        });

        lvSources.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDeleteDialog(getSoucesList().get(i).getTelNumber());
                return true;
            }
        });

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startServiceAndExit();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopServiceAndExit();
            }
        });
    }

    private void showDeleteDialog(final String sourceUID) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.delete_source) + " " + sourceUID);
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
    }

    private void updateListView() {
        sourcesAdapter = new SourcesAdapter(getSoucesList());
        if (lvSources != null) {
            lvSources.setAdapter(sourcesAdapter);
            sourcesAdapter.notifyDataSetChanged();
            lvSources.invalidate();
        }
    }


    private List<PhoneNumberDataSource> getSoucesList() {
        return SourceDB.getAllSorces(this);
    }

    private void stopServiceAndExit() {
        Log.d(TAG, "------MainActivity : stopServiceAndExit: ");
        Toast.makeText(MainActivity.this, "do nothing", Toast.LENGTH_SHORT).show();
    }

    private void startServiceAndExit() {
        Log.d(TAG, "------MainActivity : startServiceAndExit: ");
        Toast.makeText(MainActivity.this, "do nothing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    class SourcesAdapter extends BaseAdapter {
        List<PhoneNumberDataSource> sourceList;

        SourcesAdapter(List<PhoneNumberDataSource> list) {
            Log.d(TAG, "------SourcesAdapter : SourcesAdapter: list size = " + list.size());
            sourceList = list;
        }

        class ViewHolder {
            public TextView tvTelNumber;
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

                holder.tvTelNumber = (TextView) view.findViewById(R.id.tv_item_tel_number);
                holder.tvLastSMSdate = (TextView) view.findViewById(R.id.tv_item_date);
                holder.tvLastSMSbody = (TextView) view.findViewById(R.id.tv_item_body);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.tvTelNumber.setText(source.getTelNumber());
            holder.tvLastSMSdate.setText("TODO get last sms date");
            holder.tvLastSMSbody.setText("TODO get last sms body");

            return view;
        }
    }
}

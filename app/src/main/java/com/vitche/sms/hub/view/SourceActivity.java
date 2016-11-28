package com.vitche.sms.hub.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.db.MessageDB;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Constants;
import com.vitche.sms.hub.model.Message;
import com.vitche.sms.hub.model.Source;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SourceActivity extends AppCompatActivity {

    TextView tvSourceId;
    TextView tvSourceDecription;
    ListView lvMessages;
    String sourceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        tvSourceId = (TextView)findViewById(R.id.tv_source_id);
        tvSourceDecription = (TextView)findViewById(R.id.tv_source_decription);
        lvMessages = (ListView) findViewById(R.id.lv_messages);
        lvMessages.setEmptyView(findViewById(R.id.tv_messages_empty));

        sourceId = getIntent().getStringExtra(Constants.CLICKED_ITEM_ID);

        Source source = SourceDB.getSourceInfo(this, sourceId);
        if (source == null) {
            tvSourceId.setText("some error...");
            tvSourceDecription.setText("some error...");
        }else {
            tvSourceId.setText(source.getPhoneNumber());
            tvSourceDecription.setText(source.getDecription());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, getAllSourceMessages());
            lvMessages.setAdapter(adapter);
        }



    }

    private ArrayList<String> getAllSourceMessages(){
        ArrayList<Message> messages =  MessageDB.getAllMessagesInBackOrder(this, sourceId);
        ArrayList<String> result = new ArrayList<>();
        if (messages != null)
        for (int i = 0; i < messages.size(); i++) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(messages.get(i).getId()));
            result.add(date + "  " + messages.get(i).getBody());
        }
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_source, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_del_source:
                showDeleteDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.delete_source) + " " + sourceId);
        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteSource(sourceId);
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
        finish();
    }
}

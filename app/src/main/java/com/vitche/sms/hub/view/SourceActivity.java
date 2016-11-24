package com.vitche.sms.hub.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.vitche.sms.hub.R;
import com.vitche.sms.hub.controller.db.SourceDB;
import com.vitche.sms.hub.model.Constants;
import com.vitche.sms.hub.model.Source;

public class SourceActivity extends AppCompatActivity {

    TextView tvItemId;
    String sourceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        tvItemId = (TextView)findViewById(R.id.tv_item_id);
        sourceId = getIntent().getStringExtra(Constants.CLICKED_ITEM_ID);
        Source source = SourceDB.getSourceInfo(this, sourceId);
        String textInfo;
        if (source == null) {
            textInfo = "some error...";
        }else {
            textInfo = "N = "  + source.getTelNumber() + " , description = " + source.getDecription() + " , messages = " + source.getMessages().size();
        }
        tvItemId.setText(textInfo);


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

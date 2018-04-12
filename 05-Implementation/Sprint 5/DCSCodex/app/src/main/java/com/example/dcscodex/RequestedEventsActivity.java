package com.example.dcscodex;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kim on 4/7/18.
 */

public class RequestedEventsActivity extends AppCompatActivity {

     TextView textViewDate;

     ArrayList<String> listStatusDate = new ArrayList<String>();
     ArrayList<String> listStatusTitle = new ArrayList<String>();
     ArrayList<String> listStatusProf = new ArrayList<String>();
     ArrayList<String> listStatusSubject = new ArrayList<String>();
     ArrayList<String> listStatusTime = new ArrayList<String>();
     ArrayList<String> listStatusDescription = new ArrayList<String>();



     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_events);

          final AlertDialog alertDialog = new AlertDialog.Builder(RequestedEventsActivity.this).create();
          alertDialog.setTitle("Requested Event's Details");
          textViewDate = (TextView) findViewById(R.id.textViewDate);

          Intent incomingIntent = getIntent();    // will retrieve any data from an incoming intent
          String status = incomingIntent.getStringExtra("status");
          textViewDate.setText(status);

          listStatusDate = incomingIntent.getStringArrayListExtra("date");
          listStatusTitle = incomingIntent.getStringArrayListExtra("title");
          listStatusProf =  incomingIntent.getStringArrayListExtra("prof");
          listStatusSubject = incomingIntent.getStringArrayListExtra("subject");
          listStatusTime = incomingIntent.getStringArrayListExtra("time");
          listStatusDescription = incomingIntent.getStringArrayListExtra("description");

          // Sort based on title then arrange the corresponding infos in order



          ListView listView = (ListView) findViewById(R.id.listView);

          ArrayAdapter<String> adapter = new ArrayAdapter<String>(RequestedEventsActivity.this, android.R.layout.simple_list_item_1, listStatusTitle);      // arrayList
          listView.setAdapter(adapter);      /* adapt to ListView */

          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // where alert dialog pops out
                    /* pop out window later */
                    String details = "EVENT: " + listStatusTitle.get(position) + "\n"
                         + "DATE: " + listStatusDate.get(position) + "\n"
                         + "TIME: " + listStatusTime.get(position) + "\n"
                         + "PROFESSOR: " + listStatusProf.get(position) + "\n"
                         + "SUBJECT: " + listStatusSubject.get(position) + "\n"
                         + "DESCRIPTION: " + listStatusDescription.get(position) + "\n";

                    alertDialog.setMessage(details);;
                    alertDialog.show();
               }
          });

     }
}

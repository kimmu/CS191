package com.example.dcscodex;

/*
* Created by Kim Borja
* This is a course requirement for CS 192 Software Engineering II under the supervision of
* Asst. Prof. Ma. Rowena C. Solamo of the Department of Computer Science, College of Engineering,
* University of the Philippines, Diliman for the AY 2017-2018”.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


/*
* Code History
*
* Name of Programmer            Date changed                Description
* Borja, Kim                    02/16/18                    Created the EventsActivity  class
*
* Borja, Kim                    02/17/18                    Made a catcher for the date clicked by user, passed from
*                                                           CalendarActivity class. Stored all the events associated with the date clicked
*                                                           by user.
*
* Borja, Kim                    02/21/16                    Added alert dialogs for the details of the event
*                                                           clicked from the list view
*
*
*/


/*
* File creation date: 02/16/18
* Development group: Group 4
* Client Group: Students
* Purpose: EventsActivity class lists down all the events related to the event clicked by the student.
*
*/

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class EventsActivity extends AppCompatActivity {


     TextView textViewDate;
     AlertDialog alertDialog;

     ArrayList<String> listTitlesOfDateClicked = new ArrayList<String>();
     ArrayList<String> listProfsOfDateClicked = new ArrayList<String>();
     ArrayList<String> listSubjectsOfDateClicked = new ArrayList<String>();
     ArrayList<String> listTimesOfDateClicked = new ArrayList<String>();
     ArrayList<String> listDescriptionsOfDateClicked = new ArrayList<String>();


     /*
     * Method Name: onCreate
     * Creation date: 02/17/18
     * Purpose: Method for displaying all events related to a date clicked by student
     *          and the alert dialogs for additional details.
     * Required Files: CalendarActivity.java
     * Return value: None
     */

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_events);

          textViewDate = (TextView) findViewById(R.id.textViewDate);
          Intent incomingIntent = getIntent();    // will retrieve any data from an incoming intent
          String date = incomingIntent.getStringExtra("date");
          textViewDate.setText(date);

          listTitlesOfDateClicked = incomingIntent.getStringArrayListExtra("title");
          listProfsOfDateClicked =  incomingIntent.getStringArrayListExtra("prof");
          listSubjectsOfDateClicked = incomingIntent.getStringArrayListExtra("subject");
          listTimesOfDateClicked = incomingIntent.getStringArrayListExtra("time");
          listDescriptionsOfDateClicked = incomingIntent.getStringArrayListExtra("description");

          ListView listView = (ListView) findViewById(R.id.listView);

          ArrayAdapter<String> adapter = new ArrayAdapter<String>(EventsActivity.this, android.R.layout.simple_list_item_1, listTitlesOfDateClicked);      // arrayList
          listView.setAdapter(adapter);      /* adapt to ListView */

          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    /* pop out window later */
                    alertDialog = new AlertDialog.Builder(EventsActivity.this).create();
                    alertDialog.setTitle("Details");
                    alertDialog.setMessage(listTitlesOfDateClicked.get(position) + "\n"
                              + "Prof: " + listProfsOfDateClicked.get(position) + "\n"
                              + "Subject: " + listSubjectsOfDateClicked.get(position) + "\n"
                              + "Time: " + listTimesOfDateClicked.get(position) + "\n"
                              + "Description: " + listDescriptionsOfDateClicked.get(position));
                    alertDialog.show();
               }
          });

     }


}

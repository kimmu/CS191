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
* Borja, Kim                    02/21/18                    Added alert dialogs for the details of the event
*                                                           clicked from the list view
*
* Borja, Kim                    04/21/18                    Updated to let this class handle the diplay
*                                                           of listView of Pending and Rejected events
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

import org.w3c.dom.Text;

import java.util.ArrayList;


public class EventsActivity extends AppCompatActivity {


     TextView textViewDate;

     ArrayList<String> listTitlesOfDateClicked = new ArrayList<String>();
     ArrayList<String> listProfsOfDateClicked = new ArrayList<String>();
     ArrayList<String> listSubjectsOfDateClicked = new ArrayList<String>();
     ArrayList<String> listTimesOfDateClicked = new ArrayList<String>();
     ArrayList<String> listDescriptionsOfDateClicked = new ArrayList<String>();
     ArrayList<String> listStatusDate = new ArrayList<String>();
     String status, date;



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
          Intent incomingIntent = getIntent();    /* will retrieve any data from an incoming intent */
          status = incomingIntent.getStringExtra("status");

          if(status.equals("Pending") || status.equals("Rejected")) {
               textViewDate.setText(status);
               listStatusDate = incomingIntent.getStringArrayListExtra("date");
          } else {
               date = incomingIntent.getStringExtra("date");
               textViewDate.setText(date);
          }



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
                    /* where alert dialog pops out */
                    /* pop out window later */
                    String details = "EVENT:\t" + listTitlesOfDateClicked.get(position) + "\n"
                         + "PROFESSOR:\t" + listProfsOfDateClicked.get(position) + "\n"
                         + "SUBJECT:\t" + listSubjectsOfDateClicked.get(position) + "\n"
                         + "TIME:\t" + listTimesOfDateClicked.get(position) + "\n"
                         + "DESCRIPTION:\t" + listDescriptionsOfDateClicked.get(position);

                    String event, professor, subject, time, description;
                    event = "Event:  " + listTitlesOfDateClicked.get(position);
                    professor = "Prof:  " + listProfsOfDateClicked.get(position);
                    subject = "Subject:  " + listSubjectsOfDateClicked.get(position);
                    time = "Time:  " + listTimesOfDateClicked.get(position);
                    description = "Description:  " + listDescriptionsOfDateClicked.get(position);


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EventsActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.custom_event_details, null);
                    TextView eventTextView = (TextView) mView.findViewById(R.id.eventTextView);
                    TextView profTextView = (TextView) mView.findViewById(R.id.profTextView);
                    TextView subjectTextView = (TextView) mView.findViewById(R.id.subjectTextView);
                    TextView timeTextView = (TextView) mView.findViewById(R.id.timeTextView);
                    TextView descriptionTextView = (TextView) mView.findViewById(R.id.descriptionTextView);
                    TextView miniBackgroundHeader = (TextView) mView.findViewById(R.id.miniBackgroundHeader);


                    if(status.equals("Pending") || status.equals("Rejected")) {
                         miniBackgroundHeader.setText("Date: " + listStatusDate.get(position));
                    }
                    eventTextView.setText(event);
                    profTextView.setText(professor);
                    subjectTextView.setText(subject);
                    timeTextView.setText(time);
                    descriptionTextView.setText(description);

                    eventTextView.setSelected(true);
                    profTextView.setSelected(true);
                    subjectTextView.setSelected(true);
                    timeTextView.setSelected(true);
                    descriptionTextView.setSelected(true);

                    alertDialogBuilder.setView(mView);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

               }
          });

     }


}

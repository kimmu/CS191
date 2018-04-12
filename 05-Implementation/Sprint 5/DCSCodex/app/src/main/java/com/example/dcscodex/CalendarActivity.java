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
* Borja, Kim                    02/16/18                    Created the CalendarActivity  class
*                                                           Created Downloader and MyParser class as extension of AsyncTask
*
* Borja, Kim                    02/17/18                    Added Downloader and MyParser class for database syncing
*
* Borja, Kim                    02/21/18                    Added necessary comments
*
*
*/


/*
* File creation date: 02/16/18
* Development group: Group 4
* Client Group: Students
* Purpose: CalendarActivity contains the Downloader class which is responsible for
*          syncing data from the database. After downloading the event details from the
*          database in JSON format, the MyParser class takes action into storing the event
*          details into lists.
*
*/


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.github.clans.fab.FloatingActionButton;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

public class CalendarActivity extends AppCompatActivity {

     CalendarView calendarView;
     CompactCalendarView compactCalendarView;
     private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault());
     int syncButtonFlag = 0, parsingFlag = 0, parsingRequestFlag = 0, listDateSize = 0, counter = 0; // reqeustsFlag indicates if one of the view requests button has been clicked
     String localhost = "http://192.168.254.112/";
     String json_events_url = localhost + "fetchevents_dcscodex.php";  /* must always be checked and replaced by the current IP address */
     String json_requests_url = localhost + "fetchrequestedevents_dcscodex.php";
     String date = null;
     String dataResult = null, dataResultRequest = null;
     MyParser parser;
     MyParserForRequests parserForRequests;
     String user_id, status;
     TextView monthYear;
     ImageButton pendingImageButton, rejectedImageButton, acceptedImageButton;



     ArrayList<String> listDate = new ArrayList<String>();
     ArrayList<Long> listDateInMilli = new ArrayList<Long>();
     ArrayList<String> listTitle = new ArrayList<String>();
     ArrayList<String> listProf = new ArrayList<String>();
     ArrayList<String> listSubject = new ArrayList<String>();
     ArrayList<String> listTime = new ArrayList<String>();
     ArrayList<String> listDescription = new ArrayList<String>();

     ArrayList<String> listTitlesOfDateClicked = new ArrayList<String>();
     ArrayList<String> listProfsOfDateClicked = new ArrayList<String>();
     ArrayList<String> listSubjectsOfDateClicked = new ArrayList<String>();
     ArrayList<String> listTimesOfDateClicked = new ArrayList<String>();
     ArrayList<String> listDescriptionsOfDateClicked = new ArrayList<String>();



     ArrayList<String> listRequestStatus = new ArrayList<String>();
     ArrayList<String> listRequestDate = new ArrayList<String>();
     ArrayList<String> listRequestTitle = new ArrayList<String>();
     ArrayList<String> listRequestProf = new ArrayList<String>();
     ArrayList<String> listRequestSubject = new ArrayList<String>();
     ArrayList<String> listRequestTime = new ArrayList<String>();
     ArrayList<String> listRequestDescription = new ArrayList<String>();
     

     ArrayList<String> listPendingDate = new ArrayList<String>();
     ArrayList<String> listPendingTitle = new ArrayList<String>();
     ArrayList<String> listPendingProf = new ArrayList<String>();
     ArrayList<String> listPendingSubject = new ArrayList<String>();
     ArrayList<String> listPendingTime = new ArrayList<String>();
     ArrayList<String> listPendingDescription = new ArrayList<String>();

     ArrayList<String> listRejectedDate = new ArrayList<String>();
     ArrayList<String> listRejectedTitle = new ArrayList<String>();
     ArrayList<String> listRejectedProf = new ArrayList<String>();
     ArrayList<String> listRejectedSubject = new ArrayList<String>();
     ArrayList<String> listRejectedTime = new ArrayList<String>();
     ArrayList<String> listRejectedDescription = new ArrayList<String>();

     ArrayList<String> listAcceptedDate = new ArrayList<String>();
     ArrayList<String> listAcceptedTitle = new ArrayList<String>();
     ArrayList<String> listAcceptedProf = new ArrayList<String>();
     ArrayList<String> listAcceptedSubject = new ArrayList<String>();
     ArrayList<String> listAcceptedTime = new ArrayList<String>();
     ArrayList<String> listAcceptedDescription = new ArrayList<String>();




     /*
     * Method Name: onCreate
     * Creation date: 02/16/18
     * Purpose: An override method containing an onClick method, which is responsible
     *          for the execution of a downloader object (for database syncing). It also
      *         contains an onSelectedDayChange method responsible for executing necessary
      *         processes whenever a user clicks a date from the calendar.
     * Required Files: EventsActivity.java, fetchevents_dcscodex.php
     * Return value: None
     */

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_calendar);
          compactCalendarView = (CompactCalendarView) findViewById(R.id.compactCalendarView);
          compactCalendarView.setUseThreeLetterAbbreviation(true);
          monthYear = (TextView) findViewById(R.id.miniBackgroundHeader);
          monthYear.setText(dateFormatMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));


          Intent incomingIntent = getIntent();                        // will retrieve any data from an incoming intent
          user_id = incomingIntent.getStringExtra("id");        // pass to RequestActivity.class so that user won't type the student number again



          //floating action buttons on calendar page
          FloatingActionButton syncFAB, requestEventFAB, logOutFAB;
          syncFAB = findViewById(R.id.floatingActionButtonSync);
          requestEventFAB = findViewById(R.id.floatingActionButtonRequest);
          logOutFAB = findViewById(R.id.floatingActionButtonLogOut);
          pendingImageButton = (ImageButton) findViewById(R.id.pendingImageButton);
          rejectedImageButton = (ImageButton) findViewById(R.id.rejectedImageButton);
          acceptedImageButton = (ImageButton) findViewById(R.id.acceptedImageButton);

          requestEventFAB.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Intent intent = new Intent(CalendarActivity.this, RequestActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
               }
          });


          syncFAB.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                    if (InternetConnection.checkConnection(CalendarActivity.this)) {
                         final ListView listView = (ListView) findViewById(R.id.listView);
                         final Downloader downloader = new Downloader(CalendarActivity.this);
                         downloader.execute();
                         final DownloaderForRequests downloaderRequests = new DownloaderForRequests(CalendarActivity.this);
                         downloaderRequests.execute(); // newly added
                         syncButtonFlag = 1;
                         parsingFlag = 0;
                         parsingRequestFlag = 0;

                    } else {
                         Toast.makeText(CalendarActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
                    }
               }
          });


          logOutFAB.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
                    // This ensures that when "back" button is pressed, it will just exit the app and not go back to the
                    // previous visited activity 
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
               }
          });


          pendingImageButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    // use EventsActivity.java and activity_events.xml
                    // pass the title, prof, subject, time, and description to EventsActivity
                    // then use activity_events.xml as layout
                    String str;
                    //Toast.makeText(CalendarActivity.this, "1 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();
                    if (syncButtonFlag == 1) {
                        //Toast.makeText(CalendarActivity.this, "2 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();
                         if (parsingRequestFlag == 0) {


                              listPendingTitle.clear();
                              listPendingDate.clear();
                              listPendingTime.clear();
                              listPendingProf.clear();
                              listPendingSubject.clear();
                              listPendingDescription.clear();

                              listRejectedTitle.clear();
                              listRejectedDate.clear();
                              listRejectedTime.clear();
                              listRejectedProf.clear();
                              listRejectedSubject.clear();
                              listRejectedDescription.clear();

                              listAcceptedTitle.clear();
                              listAcceptedDate.clear();
                              listAcceptedTime.clear();
                              listAcceptedProf.clear();
                              listAcceptedSubject.clear();
                              listAcceptedDescription.clear();


                              parserForRequests =  new MyParserForRequests(CalendarActivity.this);
                              //Toast.makeText(CalendarActivity.this, "3 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();

                              parserForRequests.execute();

                              // Process the user's requests to be displayed
                              // if nakaset na si requestFlag = 1 di na magdownload and parse, process na lang
                              //String str;
                              Toast.makeText(CalendarActivity.this, "4 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).cancel();

                              for (int i = 0; i < listRequestStatus.size(); i++) {
                                   //Toast.makeText(CalendarActivity.this, "I GO HEREEEEEEEEEE", Toast.LENGTH_SHORT).show();

                                   str = listRequestStatus.get(i);
                                   if(str.equals("pending")) {
                                        //Toast.makeText(CalendarActivity.this, "str.equals pending", Toast.LENGTH_SHORT).show();

                                        listPendingTitle.add(listRequestTitle.get(i));
                                        listPendingDate.add(listRequestDate.get(i));
                                        listPendingTime.add(listRequestTime.get(i));
                                        listPendingProf.add(listRequestProf.get(i));
                                        listPendingSubject.add(listRequestSubject.get(i));
                                        listPendingDescription.add(listRequestDescription.get(i));

                                   } else if(str.equals("rejected")) {

                                        listRejectedTitle.add(listRequestTitle.get(i));
                                        listRejectedDate.add(listRequestDate.get(i));
                                        listRejectedTime.add(listRequestTime.get(i));
                                        listRejectedProf.add(listRequestProf.get(i));
                                        listRejectedSubject.add(listRequestSubject.get(i));
                                        listRejectedDescription.add(listRequestDescription.get(i));

                                   } else if(str.equals("accepted")) {

                                        listAcceptedTitle.add(listRequestTitle.get(i));
                                        listAcceptedDate.add(listRequestDate.get(i));
                                        listAcceptedTime.add(listRequestTime.get(i));
                                        listAcceptedProf.add(listRequestProf.get(i));
                                        listAcceptedSubject.add(listRequestSubject.get(i));
                                        listAcceptedDescription.add(listRequestDescription.get(i));

                                   }
                              }
                              parsingRequestFlag = 1;
                         }

                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();
                         /*for(int i = 0; i < listPendingTitle.size(); i++) {
                              Toast.makeText(CalendarActivity.this, "listPendingTitle " + i + "=" + listPendingTitle.get(i), Toast.LENGTH_SHORT).show();
                         }*/

                         status = "Pending";
                         Intent intent = new Intent(CalendarActivity.this, RequestedEventsActivity.class);

                         intent.putExtra("status", status);
                         intent.putStringArrayListExtra("date", (ArrayList<String>) listPendingDate);
                         intent.putStringArrayListExtra("title", (ArrayList<String>) listPendingTitle);
                         intent.putStringArrayListExtra("prof", (ArrayList<String>) listPendingProf);
                         intent.putStringArrayListExtra("subject", (ArrayList<String>) listPendingSubject);
                         intent.putStringArrayListExtra("time", (ArrayList<String>) listPendingTime);
                         intent.putStringArrayListExtra("description", (ArrayList<String>) listPendingDescription);
                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();

                         //Toast.makeText(CalendarActivity.this, "listPendingDate.size() " + "=" + listPendingDate.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingProf.size() " + "=" + listPendingProf.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingSubject.size() " + "=" + listPendingSubject.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingTime.size() " + "=" + listPendingTime.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingDescription.size() " + "=" + listPendingDescription.size(), Toast.LENGTH_SHORT).show();
                         startActivity(intent); // will navigate to RequestedEventsActivity


                    } else {
                         Toast.makeText(CalendarActivity.this, "Press SYNC First", Toast.LENGTH_SHORT).show();
                    }

               }
          });





          rejectedImageButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    // use EventsActivity.java and activity_events.xml
                    // pass the title, prof, subject, time, and description to EventsActivity
                    // then use activity_events.xml as layout
                    String str;
                    //Toast.makeText(CalendarActivity.this, "1 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();
                    if (syncButtonFlag == 1) {
                         //Toast.makeText(CalendarActivity.this, "2 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();
                         if (parsingRequestFlag == 0) {


                              listPendingTitle.clear();
                              listPendingDate.clear();
                              listPendingTime.clear();
                              listPendingProf.clear();
                              listPendingSubject.clear();
                              listPendingDescription.clear();

                              listRejectedTitle.clear();
                              listRejectedDate.clear();
                              listRejectedTime.clear();
                              listRejectedProf.clear();
                              listRejectedSubject.clear();
                              listRejectedDescription.clear();

                              listAcceptedTitle.clear();
                              listAcceptedDate.clear();
                              listAcceptedTime.clear();
                              listAcceptedProf.clear();
                              listAcceptedSubject.clear();
                              listAcceptedDescription.clear();


                              parserForRequests =  new MyParserForRequests(CalendarActivity.this);
                              //Toast.makeText(CalendarActivity.this, "3 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();

                              parserForRequests.execute();

                              // Process the user's requests to be displayed
                              // if nakaset na si requestFlag = 1 di na magdownload and parse, process na lang
                              //String str;
                              Toast.makeText(CalendarActivity.this, "4 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).cancel();

                              for (int i = 0; i < listRequestStatus.size(); i++) {
                                   //Toast.makeText(CalendarActivity.this, "I GO HEREEEEEEEEEE", Toast.LENGTH_SHORT).show();

                                   str = listRequestStatus.get(i);
                                   if(str.equals("pending")) {
                                        //Toast.makeText(CalendarActivity.this, "str.equals pending", Toast.LENGTH_SHORT).show();

                                        listPendingTitle.add(listRequestTitle.get(i));
                                        listPendingDate.add(listRequestDate.get(i));
                                        listPendingTime.add(listRequestTime.get(i));
                                        listPendingProf.add(listRequestProf.get(i));
                                        listPendingSubject.add(listRequestSubject.get(i));
                                        listPendingDescription.add(listRequestDescription.get(i));

                                   } else if(str.equals("rejected")) {

                                        listRejectedTitle.add(listRequestTitle.get(i));
                                        listRejectedDate.add(listRequestDate.get(i));
                                        listRejectedTime.add(listRequestTime.get(i));
                                        listRejectedProf.add(listRequestProf.get(i));
                                        listRejectedSubject.add(listRequestSubject.get(i));
                                        listRejectedDescription.add(listRequestDescription.get(i));

                                   } else if(str.equals("accepted")) {

                                        listAcceptedTitle.add(listRequestTitle.get(i));
                                        listAcceptedDate.add(listRequestDate.get(i));
                                        listAcceptedTime.add(listRequestTime.get(i));
                                        listAcceptedProf.add(listRequestProf.get(i));
                                        listAcceptedSubject.add(listRequestSubject.get(i));
                                        listAcceptedDescription.add(listRequestDescription.get(i));

                                   }
                              }
                              parsingRequestFlag = 1;
                         }

                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();
                         /*for(int i = 0; i < listPendingTitle.size(); i++) {
                              Toast.makeText(CalendarActivity.this, "listPendingTitle " + i + "=" + listPendingTitle.get(i), Toast.LENGTH_SHORT).show();
                         }*/

                         status = "Rejected";
                         Intent intent = new Intent(CalendarActivity.this, RequestedEventsActivity.class);

                         intent.putExtra("status", status);
                         intent.putStringArrayListExtra("date", (ArrayList<String>) listRejectedDate);
                         intent.putStringArrayListExtra("title", (ArrayList<String>) listRejectedTitle);
                         intent.putStringArrayListExtra("prof", (ArrayList<String>) listRejectedProf);
                         intent.putStringArrayListExtra("subject", (ArrayList<String>) listRejectedSubject);
                         intent.putStringArrayListExtra("time", (ArrayList<String>) listRejectedTime);
                         intent.putStringArrayListExtra("description", (ArrayList<String>) listRejectedDescription);
                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();

                         //Toast.makeText(CalendarActivity.this, "listPendingDate.size() " + "=" + listPendingDate.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingProf.size() " + "=" + listPendingProf.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingSubject.size() " + "=" + listPendingSubject.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingTime.size() " + "=" + listPendingTime.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingDescription.size() " + "=" + listPendingDescription.size(), Toast.LENGTH_SHORT).show();
                         startActivity(intent); // will navigate to RequestedEventsActivity


                    } else {
                         Toast.makeText(CalendarActivity.this, "Press SYNC First", Toast.LENGTH_SHORT).show();
                    }

               }
          });




          acceptedImageButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    // use EventsActivity.java and activity_events.xml
                    // pass the title, prof, subject, time, and description to EventsActivity
                    // then use activity_events.xml as layout
                    String str;
                    //Toast.makeText(CalendarActivity.this, "1 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();
                    if (syncButtonFlag == 1) {
                         //Toast.makeText(CalendarActivity.this, "2 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();
                         if (parsingRequestFlag == 0) {


                              listPendingTitle.clear();
                              listPendingDate.clear();
                              listPendingTime.clear();
                              listPendingProf.clear();
                              listPendingSubject.clear();
                              listPendingDescription.clear();

                              listRejectedTitle.clear();
                              listRejectedDate.clear();
                              listRejectedTime.clear();
                              listRejectedProf.clear();
                              listRejectedSubject.clear();
                              listRejectedDescription.clear();

                              listAcceptedTitle.clear();
                              listAcceptedDate.clear();
                              listAcceptedTime.clear();
                              listAcceptedProf.clear();
                              listAcceptedSubject.clear();
                              listAcceptedDescription.clear();


                              parserForRequests =  new MyParserForRequests(CalendarActivity.this);
                              //Toast.makeText(CalendarActivity.this, "3 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).show();

                              parserForRequests.execute();

                              // Process the user's requests to be displayed
                              // if nakaset na si requestFlag = 1 di na magdownload and parse, process na lang
                              //String str;
                              Toast.makeText(CalendarActivity.this, "4 listRequestStatus.size() " + "=" + listRequestStatus.size(), Toast.LENGTH_SHORT).cancel();

                              for (int i = 0; i < listRequestStatus.size(); i++) {
                                   //Toast.makeText(CalendarActivity.this, "I GO HEREEEEEEEEEE", Toast.LENGTH_SHORT).show();

                                   str = listRequestStatus.get(i);
                                   if(str.equals("pending")) {
                                        //Toast.makeText(CalendarActivity.this, "str.equals pending", Toast.LENGTH_SHORT).show();

                                        listPendingTitle.add(listRequestTitle.get(i));
                                        listPendingDate.add(listRequestDate.get(i));
                                        listPendingTime.add(listRequestTime.get(i));
                                        listPendingProf.add(listRequestProf.get(i));
                                        listPendingSubject.add(listRequestSubject.get(i));
                                        listPendingDescription.add(listRequestDescription.get(i));

                                   } else if(str.equals("rejected")) {

                                        listRejectedTitle.add(listRequestTitle.get(i));
                                        listRejectedDate.add(listRequestDate.get(i));
                                        listRejectedTime.add(listRequestTime.get(i));
                                        listRejectedProf.add(listRequestProf.get(i));
                                        listRejectedSubject.add(listRequestSubject.get(i));
                                        listRejectedDescription.add(listRequestDescription.get(i));

                                   } else if(str.equals("accepted")) {

                                        listAcceptedTitle.add(listRequestTitle.get(i));
                                        listAcceptedDate.add(listRequestDate.get(i));
                                        listAcceptedTime.add(listRequestTime.get(i));
                                        listAcceptedProf.add(listRequestProf.get(i));
                                        listAcceptedSubject.add(listRequestSubject.get(i));
                                        listAcceptedDescription.add(listRequestDescription.get(i));

                                   }
                              }
                              parsingRequestFlag = 1;
                         }

                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();
                         /*for(int i = 0; i < listPendingTitle.size(); i++) {
                              Toast.makeText(CalendarActivity.this, "listPendingTitle " + i + "=" + listPendingTitle.get(i), Toast.LENGTH_SHORT).show();
                         }*/

                         status = "Accepted Requests";
                         Intent intent = new Intent(CalendarActivity.this, RequestedEventsActivity.class);

                         intent.putExtra("status", status);
                         intent.putStringArrayListExtra("date", (ArrayList<String>) listAcceptedDate);
                         intent.putStringArrayListExtra("title", (ArrayList<String>) listAcceptedTitle);
                         intent.putStringArrayListExtra("prof", (ArrayList<String>) listAcceptedProf);
                         intent.putStringArrayListExtra("subject", (ArrayList<String>) listAcceptedSubject);
                         intent.putStringArrayListExtra("time", (ArrayList<String>) listAcceptedTime);
                         intent.putStringArrayListExtra("description", (ArrayList<String>) listAcceptedDescription);
                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();

                         //Toast.makeText(CalendarActivity.this, "listPendingDate.size() " + "=" + listPendingDate.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingTitle.size() " + "=" + listPendingTitle.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingProf.size() " + "=" + listPendingProf.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingSubject.size() " + "=" + listPendingSubject.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingTime.size() " + "=" + listPendingTime.size(), Toast.LENGTH_SHORT).show();
                         //Toast.makeText(CalendarActivity.this, "listPendingDescription.size() " + "=" + listPendingDescription.size(), Toast.LENGTH_SHORT).show();
                         startActivity(intent); // will navigate to RequestedEventsActivity


                    } else {
                         Toast.makeText(CalendarActivity.this, "Press SYNC First", Toast.LENGTH_SHORT).show();
                    }

               }
          });










          compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
               @Override
               public void onDayClick(Date dateClicked) {
                   // Toast.makeText(CalendarActivity.this, "syncButtonFlag= " + syncButtonFlag + " parsingFlag= " + parsingFlag, Toast.LENGTH_SHORT).show();

                    if (InternetConnection.checkConnection(CalendarActivity.this)) {
                         Context context = getApplicationContext();

                         // Convert dateClicked into String then into the form mm/dd/yy
                         String dateClickedSegments[], month, day, year;
                         dateClickedSegments = dateClicked.toString().split(" ");
                         month = dateClickedSegments[1];
                         day = dateClickedSegments[2];
                         year = dateClickedSegments[5];

                         if(month.equals("Jan")) {
                              month = "1";
                         } else if(month.equals("Feb")) {
                              month = "2";
                         } else if(month.equals("Mar")) {
                              month = "3";
                         } else if(month.equals("Apr")) {
                              month = "4";
                         } else if(month.equals("May")) {
                              month = "5";
                         } else if(month.equals("Jun")) {
                              month = "6";
                         } else if(month.equals("Jul")) {
                              month = "7";
                         } else if(month.equals("Aug")) {
                              month = "8";
                         } else if(month.equals("Sep")) {
                              month = "9";
                         } else if(month.equals("Oct")) {
                              month = "10";
                         } else if(month.equals("Nov")) {
                              month = "11";
                         } else if(month.equals("Dec")) {
                              month = "12";
                         }

                         date = month + "/" + day + "/" + year;       // mm/dd/yyyy --> get month/date/year
                         int flag = 0;

                         listTitlesOfDateClicked.clear();
                         listProfsOfDateClicked.clear();
                         listSubjectsOfDateClicked.clear();
                         listTimesOfDateClicked.clear();
                         listDescriptionsOfDateClicked.clear();


                         //Toast.makeText(CalendarActivity.this, "", Toast.LENGTH_SHORT).show();

                         if(syncButtonFlag==1) {
                              if(parsingFlag==0) {
                                   parser = new MyParser(CalendarActivity.this);       // call parser
                                   parser.execute();
                                   String segments[];
                                   Event  event;
                                   Toast.makeText(context, "listDate size: " + listDate.size(), Toast.LENGTH_SHORT).cancel();
                                   listDateSize = listDate.size();
                                   compactCalendarView.removeAllEvents();
                                   String str;

                                   for(int j = 0; j < listDateSize; j++) {
                                       // Toast.makeText(CalendarActivity.this, "HEY IM HEREEEEE", Toast.LENGTH_SHORT).show();

                                        // segments = ["1487428000000", "2/28/2018"]
                                        segments = listDate.get(j).split(" ");
                                        listDateInMilli.add(Long.parseLong(segments[0]));
                                        listDate.set(j, segments[1]);
                                        // hightlight part
                                        event = new Event(Color.BLUE, listDateInMilli.get(j));
                                        compactCalendarView.addEvent(event);
                                        //Toast.makeText(CalendarActivity.this, "After parsing and splitting:\n" + listDate.get(j), Toast.LENGTH_SHORT).show();

                                   }
                                   parsingFlag = 1;


                                   //since listDateSize == listStatus.size



                              }

                         } else {
                              Toast.makeText(CalendarActivity.this, "Press SYNC First", Toast.LENGTH_SHORT).show();
                         }




                         // Need to separate Milliseconds from string date first
                         // after parsing (so may laman na yung unang set ng array lists),
                         // loop through all the dates available then put the Milliseconds part to another array list after converting it from String
                         String segments[];
                         Event  event;
                         for(int j = 0; j < listDateSize; j++) {

                              // segments = ["1487428000000", "2/28/2018"]
                              /*segments = listDate.get(j).split(" ");
                              listDateInMilli.add(Long.parseLong(segments[0]));
                              listDate.set(j,segments[1]);
                              // hightlight part
                              event = new Event(Color.BLUE, listDateInMilli.get(j));
                              compactCalendarView.addEvent(event);*/

                              //Toast.makeText(CalendarActivity.this, listDate.get(j) + " " + date, Toast.LENGTH_SHORT).show();
                              if((listDate.get(j)).equals(date)) {
                                  //Toast.makeText(CalendarActivity.this, "flag=1: " + listDate.get(j) + " " + date, Toast.LENGTH_SHORT).show();

                                   flag = 1;
                                   listTitlesOfDateClicked.add(listTitle.get(j));
                                   listProfsOfDateClicked.add(listProf.get(j));
                                   listSubjectsOfDateClicked.add(listSubject.get(j));
                                   listTimesOfDateClicked.add(listTime.get(j));
                                   listDescriptionsOfDateClicked.add(listDescription.get(j));
                              }
                         }




                         if(flag == 1) {     // if there are events related to the date clicked go to the big ListView
                              Intent intent = new Intent(CalendarActivity.this, EventsActivity.class);
                              intent.putExtra("date", date);          // pass the string date to EventsActivity; then, retrieve the data inside EventsActivity

                              intent.putStringArrayListExtra("title", (ArrayList<String>) listTitlesOfDateClicked);
                              intent.putStringArrayListExtra("prof", (ArrayList<String>) listProfsOfDateClicked);
                              intent.putStringArrayListExtra("subject", (ArrayList<String>) listSubjectsOfDateClicked);
                              intent.putStringArrayListExtra("time", (ArrayList<String>) listTimesOfDateClicked);
                              intent.putStringArrayListExtra("description", (ArrayList<String>) listDescriptionsOfDateClicked);
                              startActivity(intent);        // will navigate to EventsActivity
                         } else {
                              Toast.makeText(CalendarActivity.this, "No events", Toast.LENGTH_SHORT).show();
                         }

                    } else {
                         Toast.makeText(CalendarActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
                    }

               }

               @Override
               public void onMonthScroll(Date firstDayOfNewMonth) {
                    if (InternetConnection.checkConnection(CalendarActivity.this)) {
                         monthYear = (TextView) findViewById(R.id.miniBackgroundHeader);
                         monthYear.setText(dateFormatMonth.format(firstDayOfNewMonth));
                    } else {
                         Toast.makeText(CalendarActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
                    }

               }
          });
     }










     /*
     * Sub-class Name: Downloader
     * Creation date: 02/17/18
     * Purpose: Responsible for utilizing the PHP script in order to download
     *          event details from the database in JSON format.
     * Required Files: fetchevents_dcscodex.php
     */

     public class Downloader extends AsyncTask<Void, Integer, String> {    /* doInBackground, onProgressUpdate, and onPostExecute parameter types */

          Context ctx;


          ProgressDialog progressDialog;

          public Downloader(Context ctx) {
               this.ctx = ctx;
          }



          /*
          * Method Name: onPreExecute
          * Creation date: 02/17/18
          * Purpose: Displays a process dialog while fetching data from database
          * Return value: None
          */
          @Override
          protected void onPreExecute() {
               super.onPreExecute();

               progressDialog = new ProgressDialog(ctx);
               progressDialog.setTitle("Status");
               progressDialog.setMessage("Fetching data... Please wait");
               progressDialog.show();
          }



          /*
          * Method Name: doInBackground
          * Creation date: 02/17/18
          * Purpose: Calls the downloadData method
          * Return value: data
          */
          @Override
          protected String doInBackground(Void... params) {      /* string here will be passed on onPostExecute() */
               // json_events_url
               String data = downloadData();
               return data;
          }



          /*
          * Method Name: onPostExecute
          * Creation date: 02/17/18
          * Purpose: Alerts student for download status of data
          * Return value: data
          */
          @Override
          protected void onPostExecute(String result) {     /* string from doInBackground */
               super.onPostExecute(result);
               progressDialog.dismiss();

               /* before parsing, make sure that data not null */
               if (result != null) {
                    /* pass data */

                    Toast.makeText(ctx, "Sync successful", Toast.LENGTH_SHORT).show();    /* this is the cut for the downloading part. now we go on to parsing */
                    dataResult = result;
               } else {
                    Toast.makeText(ctx, "Unable to download data", Toast.LENGTH_SHORT).show();
               }
          }




          /*
          * Method Name: downloadData
          * Creation date: 02/17/18
          * Purpose: For actual downloading of data
          * Return value: null/stringBuffer
          */

          private String downloadData() {
               /* connect and get the stream of data */
               InputStream inputStream = null;
               String line = null;      /* store each line/row in this string */

               try {
                    URL url = new URL(json_events_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer stringBuffer = new StringBuffer();

                    if (bufferedReader != null) {
                         while ((line = bufferedReader.readLine()) != null) {
                              stringBuffer.append(line+"\n");
                         }
                    } else {
                         return  null;
                    }
                    return stringBuffer.toString();

               } catch (MalformedURLException e) {
                    e.printStackTrace();
               } catch (IOException e) {
                    e.printStackTrace();
               } finally {
                    /* check if inputStream is not null; for java version 6 and below */
                    if (inputStream != null) {
                         try {
                              inputStream.close();
                         } catch (IOException e) {
                              e.printStackTrace();
                         }
                    }
               }
               return null;
          }
     }










     /*
     * Sub-class Name: MyParser
     * Creation date: 02/17/18
     * Purpose: Responsible parsing data in JSON format then listing all the retrieved string values
     *          in lists
     *
     */

     public class MyParser extends AsyncTask<Void, Integer, Integer> {

          Context ctx;
          ProgressDialog progressDialog;


          public MyParser(Context ctx) {
               this.ctx = ctx;
          }



          /*
          * Method Name: onPreExecute
          * Creation date: 02/17/18
          * Purpose: Informs user that parsing is taking place
          * Return value: None
          */

          @Override
          protected void onPreExecute() {
               super.onPreExecute();
               progressDialog = new ProgressDialog(ctx);
               progressDialog.setTitle("Parser");
               progressDialog.setMessage("Please wait...");
               progressDialog.show();
          }


          /*
          * Method Name: doInBackground
          * Creation date: 02/17/18
          * Purpose: Calling the parse method
          * Return value: this.parse()- an integer
          */

          @Override
          protected Integer doInBackground(Void... params) {

               return this.parse();     // remember the parse() method returns an integer --> 1 if successful. otherwise, 0
          }

          /*
          * Method Name: onPostExecute
          * Creation date: 02/17/18
          * Purpose: For dismissing the process dialog initialized earlier
          *          and informing the user if parse is unsuccessful
          * Return value: this.parse()- an integer
          */

          @Override
          protected void onPostExecute(Integer integer) {
               super.onPostExecute(integer);

               if(integer != 1) {
                    Toast.makeText(ctx, "Unable to parse", Toast.LENGTH_SHORT).show();
               }
               progressDialog.dismiss();
          }




          /*
          * Method Name: parse
          * Creation date: 02/17/18
          * Purpose: Where actual parsing occurs
          * Return value: 1/0- an integer
          */

          private int parse() {    /* will tell if parsing has been successful */
               try {
                    /* JSONArray contains many data--> JSON objects */
                    String temp;


                    JSONArray jsonArray = new JSONArray(dataResult);   /* assigned by the constructor above */
                    JSONObject jsonObject = null;                /* create a JSON obect to hold a single item */

                    listTitle.clear();  /* make sure to clear array list so there are no duplications */
                    listDate.clear();
                    listProf.clear();
                    listSubject.clear();
                    listTime.clear();
                    listDescription.clear();

                    /* loop through the JSON array */
                    for (int i = 0; i < jsonArray.length(); i++) {
                         jsonObject = jsonArray.getJSONObject(i);
                         temp = jsonObject.getString("title");   /* for each object, get the title of event */
                         listTitle.add(temp);
                         temp = jsonObject.getString("date");
                         listDate.add(temp);
                         temp = jsonObject.getString("prof");
                         listProf.add(temp);
                         temp = jsonObject.getString("subject");
                         listSubject.add(temp);
                         temp = jsonObject.getString("time");
                         listTime.add(temp);
                         temp = jsonObject.getString("description");
                         listDescription.add(temp);
                         //temp = jsonObject.getString("status");
                         //listStatus.add(temp);
                         // I still don't know why you can't put it here!!
                         }
                    return 1;      /* if successful */

               } catch (JSONException e) {
                    e.printStackTrace();
               }
               return 0;
          }
     }









     public class DownloaderForRequests extends AsyncTask<Void, Integer, String> {    /* doInBackground, onProgressUpdate, and onPostExecute parameter types */

          Context ctx;


          ProgressDialog progressDialog;

          public DownloaderForRequests(Context ctx) {
               this.ctx = ctx;
          }

          @Override
          protected void onPreExecute() {
               super.onPreExecute();

               progressDialog = new ProgressDialog(ctx);
               progressDialog.setTitle("Status");
               progressDialog.setMessage("Fetching data... Please wait");
               progressDialog.show();
          }

          @Override
          protected String doInBackground(Void... params) {      /* string here will be passed on onPostExecute() */
               // json_events_url
               String data = downloadData();
               return data;
          }

          @Override
          protected void onPostExecute(String result) {     /* string from doInBackground */
               super.onPostExecute(result);
               progressDialog.dismiss();

               /* before parsing, make sure that data not null */
               if (result != null) {
                    /* pass data */
                    Toast.makeText(ctx, "Sync successful", Toast.LENGTH_SHORT).show();    /* this is the cut for the downloading part. now we go on to parsing */
                    dataResultRequest = result;
               } else {
                    Toast.makeText(ctx, "Unable to download data", Toast.LENGTH_SHORT).show();
               }
          }

          private String downloadData() {
               /* connect and get the stream of data */
               InputStream inputStream = null;
               String line = null;      /* store each line/row in this string */

               try {
                    URL url = new URL(json_requests_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer stringBuffer = new StringBuffer();

                    if (bufferedReader != null) {
                         while ((line = bufferedReader.readLine()) != null) {
                              stringBuffer.append(line+"\n");
                         }
                    } else {
                         return  null;
                    }
                    return stringBuffer.toString();

               } catch (MalformedURLException e) {
                    e.printStackTrace();
               } catch (IOException e) {
                    e.printStackTrace();
               } finally {
                    /* check if inputStream is not null; for java version 6 and below */
                    if (inputStream != null) {
                         try {
                              inputStream.close();
                         } catch (IOException e) {
                              e.printStackTrace();
                         }
                    }
               }
               return null;
          }
     }








     /*
     * Sub-class Name: MyParser
     * Creation date: 02/17/18
     * Purpose: Responsible parsing data in JSON format then listing all the retrieved string values
     *          in lists
     *
     */

     public class MyParserForRequests extends AsyncTask<Void, Integer, Integer> {

          Context ctx;
          ProgressDialog progressDialog;

          public MyParserForRequests(Context ctx) {
               this.ctx = ctx;
          }

          @Override
          protected void onPreExecute() {
               super.onPreExecute();
               progressDialog = new ProgressDialog(ctx);
               progressDialog.setTitle("Parser");
               progressDialog.setMessage("Please wait...");
               progressDialog.show();
          }

          @Override
          protected Integer doInBackground(Void... params) {
               return this.parse();     // remember the parse() method returns an integer --> 1 if successful. otherwise, 0
          }

          @Override
          protected void onPostExecute(Integer integer) {
               super.onPostExecute(integer);

               if(integer != 1) {
                    Toast.makeText(ctx, "Unable to parse", Toast.LENGTH_SHORT).show();
               }
               progressDialog.dismiss();
          }

          private int parse() {    /* will tell if parsing has been successful */
               try {
                    /* JSONArray contains many data--> JSON objects */
                    String temp;


                    JSONArray jsonArray = new JSONArray(dataResultRequest);   /* assigned by the constructor above */
                    JSONObject jsonObject = null;                /* create a JSON object to hold a single item */

                    listRequestTitle.clear();  /* make sure to clear array list so there are no duplications */
                    listRequestDate.clear();
                    listRequestProf.clear();
                    listRequestSubject.clear();
                    listRequestTime.clear();
                    listRequestDescription.clear();
                    listRequestStatus.clear();

                    /* loop through the JSON array */
                    for (int i = 0; i < jsonArray.length(); i++) {
                         jsonObject = jsonArray.getJSONObject(i);
                         temp = jsonObject.getString("title");   /* for each object, get the title of event */
                         listRequestTitle.add(temp);
                         temp = jsonObject.getString("date");
                         listRequestDate.add(temp);
                         temp = jsonObject.getString("prof");
                         listRequestProf.add(temp);
                         temp = jsonObject.getString("subject");
                         listRequestSubject.add(temp);
                         temp = jsonObject.getString("time");
                         listRequestTime.add(temp);
                         temp = jsonObject.getString("description");
                         listRequestDescription.add(temp);
                         temp = jsonObject.getString("status");
                         listRequestStatus.add(temp);

                    }
                    /*runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                              Toast.makeText(CalendarActivity.this, "listRequestStatus.size() = "+listRequestStatus.size(), Toast.LENGTH_SHORT).show();
                         }
                    });*/
                    return 1;      /* if successful */

               } catch (JSONException e) {
                    e.printStackTrace();
               }
               return 0;
          }
     }







}

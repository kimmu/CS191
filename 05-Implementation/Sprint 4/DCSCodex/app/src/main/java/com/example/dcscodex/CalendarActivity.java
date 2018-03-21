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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

     CalendarView calendarView;
     Button syncButton;
     int syncButtonFlag = 0;
     String localhost = "http://192.168.254.112/";
     String json_url = localhost + "fetchevents_dcscodex.php";  /* must always be checked and replaced by the current IP address */
     String date = null;
     String dataResult = null;
     MyParser parser;
     String user_id;


     ArrayList<String> listDate = new ArrayList<String>();
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
          calendarView = (CalendarView) findViewById(R.id.calendarView);   // for the calendarView
          syncButton = (Button) findViewById(R.id.syncButton);

          Intent incomingIntent = getIntent();    // will retrieve any data from an incoming intent
          user_id = incomingIntent.getStringExtra("id");        // pass to RequestActivity.class so that user won't type the student number again

          FloatingActionButton requestFAB = findViewById(R.id.floatingActionButton);

          requestFAB.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Intent intent = new Intent(CalendarActivity.this, RequestActivity.class);
                    intent.putExtra("id", user_id);
                    startActivity(intent);
               }
          });



          syncButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    syncButtonFlag = 1;
                    if (InternetConnection.checkConnection(CalendarActivity.this)) {
                         final ListView listView = (ListView) findViewById(R.id.listView);
                         final Downloader downloader = new Downloader(CalendarActivity.this);
                         downloader.execute();
                    } else {
                         Toast.makeText(CalendarActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
                    }
               }
          });


          /* get date month/day/year */
          calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

               @Override
               public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                    if (InternetConnection.checkConnection(CalendarActivity.this)) {
                         date = (i1+1) + "/" + i2 + "/" + i;   /* mm/dd/yyyy */
                         int flag = 0;



                         listTitlesOfDateClicked.clear();
                         listProfsOfDateClicked.clear();
                         listSubjectsOfDateClicked.clear();
                         listTimesOfDateClicked.clear();
                         listDescriptionsOfDateClicked.clear();


                         if(syncButtonFlag==1) {
                              parser = new MyParser(CalendarActivity.this);       // parser call
                              parser.execute();
                         } else {
                              Toast.makeText(CalendarActivity.this, "Press SYNC First", Toast.LENGTH_SHORT).show();
                         }



                    /* loop through all the dates available --> listDate */
                         for(int j = 0; j < listDate.size(); j++) {
                              if((listDate.get(j)).equals(date)) {
                                   flag = 1;
                                   listTitlesOfDateClicked.add(listTitle.get(j));
                                   listProfsOfDateClicked.add(listProf.get(j));
                                   listSubjectsOfDateClicked.add(listSubject.get(j));
                                   listTimesOfDateClicked.add(listTime.get(j));
                                   listDescriptionsOfDateClicked.add(listDescription.get(j));
                              }
                         }

                         if(flag == 1) {     /* if there are events related to the date clicked go to the big ListView */
                              Intent intent = new Intent(CalendarActivity.this, EventsActivity.class);
                              intent.putExtra("date", date);          /* put extra detail. transfer something, which is "date" in this case, on EventsActivity */
                                                                      /* pass the string date to EventsActivity; then, retrieve the data inside EventsActivity */
                              intent.putStringArrayListExtra("title", (ArrayList<String>) listTitlesOfDateClicked);
                              intent.putStringArrayListExtra("prof", (ArrayList<String>) listProfsOfDateClicked);
                              intent.putStringArrayListExtra("subject", (ArrayList<String>) listSubjectsOfDateClicked);
                              intent.putStringArrayListExtra("time", (ArrayList<String>) listTimesOfDateClicked);
                              intent.putStringArrayListExtra("description", (ArrayList<String>) listDescriptionsOfDateClicked);
                              startActivity(intent);        /* will navigate to EventsActivity */
                         } else {
                              Toast.makeText(CalendarActivity.this, "No events", Toast.LENGTH_SHORT).show();
                         }
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
                    URL url = new URL(json_url);
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
                         String temp = jsonObject.getString("title");   /* for each object, get the title of event */
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
                    }
                    return 1;      /* if successful */

               } catch (JSONException e) {
                    e.printStackTrace();
               }
               return 0;
          }
     }







}

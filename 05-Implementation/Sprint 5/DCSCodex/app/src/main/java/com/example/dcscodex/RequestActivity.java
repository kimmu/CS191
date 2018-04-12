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
* Borja, Kim                    03/07/18                    Creation of RequestActivity class
*
* Borja, Kim                    03/14/18                    Updated the OnRequest Method and integrated it with BackgroundWorker class
*
* Borja, Kim                    03/21/18                    Added necessary comments
*
*
*/


/*
* File creation date: 03/07/18
* Development group: Group 4
* Client Group: Students
* Purpose: RequestActivity class operates necessary functionalities for requesting events.
*          This class enables students to request an event and send the request to the
*          database. The administrator can approve the request and add the event to the "universal" calendar.
*         Otherwise, the admin can just delete the request from the database.
*
*/

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {

     int year_x, month_x, day_x;
     static final int DIALOG_ID = 0, DIALOG_ID_TIME = 1;
     EditText titleEditText, profEditText, subjectEditText, descriptionEditText;
     String user_id="", date="", time="";
     Button dateButton, timeButton;
     int hour_x, minute_x;


     /*
     * Method Name: onCreate
     * Creation date: 03/07/18
     * Purpose: An override method for reading the user input regarding the details of a
     *          requested event. This method is responsible for resetting the request form, whenever
      *         the "+" floating action button is clicked, to its blank state such that the user can
      *         input another event request again.
     * Required Files: None
     * Return value: None
     */

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_request);

          Intent incomingIntent = getIntent();    // will retrieve any data from an incoming intent
          user_id = incomingIntent.getStringExtra("id");

          titleEditText = (EditText) findViewById(R.id.titleEditText);
          profEditText = (EditText) findViewById(R.id.profEditText);
          subjectEditText = (EditText) findViewById(R.id.subjectEditText);
          descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

          final Calendar calendar = Calendar.getInstance();
          year_x = calendar.get(Calendar.YEAR);
          month_x = calendar.get(Calendar.MONTH);
          day_x = calendar.get(Calendar.DAY_OF_MONTH);

     }



     /*
     * Method Name: pickDate
     * Creation date: 03/07/18
     * Purpose: A method for calling a dialog, either a DatePickerDialog or a TimePickerDialog.
     *          The parameter DIALOG_ID which is set to 0 above is assigned to DatePickerDialog.
     * Required Files: None
     * Return value: None
     */

     public void pickDate(View view) {
          showDialog(DIALOG_ID);
     }


     /*
     * Method Name: onCreateDialog
     * Creation date: 03/07/18
     * Purpose: This method is responsible for returning the actual dialog called.
     *          If id == DIALOG_ID, then a DatePickerDialog shall be returned. Otherwise,
     *          a TimePickerDialog shall be returned.
     * Required Files: None
     * Return value: Dialog
     */

     @Override
     protected Dialog onCreateDialog(int id) {
          if(id == DIALOG_ID) {
               return new DatePickerDialog(this, datePickerListener, year_x, month_x, day_x);
          } else if(id == DIALOG_ID_TIME) {
               return new TimePickerDialog(this, timePickerListener, hour_x, minute_x, false);
          } else {
               return null;
          }
     }


     /*
     * Method Name: OnDateListener
     * Creation date: 03/07/18
     * Purpose: This method is for the necessary actions needed whenever a certain date is clicked
     *          in the DatePickerDialog. When a date is clicked by the user, the string for the date
     *          is saved then later on sent to the database if "send request" is clicked.
     * Required Files: None
     * Return value: NA
     */

     private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
               year_x = year;
               month_x = monthOfYear + 1;
               day_x = dayOfMonth;
               date = month_x + "/" + day_x + "/" + year_x;
               Toast.makeText(RequestActivity.this, date, Toast.LENGTH_SHORT).show();
               dateButton = (Button) findViewById(R.id.datePickerButton);
               dateButton.setText(date);
          }
     };


     /*
     * Method Name: OnTimeListener
     * Creation date: 03/07/18
     * Purpose: This method is for the necessary actions needed whenever a certain time is set
     *          in the TimePickerDialog. When time is set by the user, the string for the time (hh:mm)
     *          is saved then later on sent to the database if "send request" is clicked.
     * Required Files: None
     * Return value: NA
     */

     private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
          @Override
          public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
               hour_x = hourOfDay;
               minute_x = minute;
               time = hour_x + ":" + minute_x;
               if(minute_x % 10 == minute_x) {
                    time = hour_x + ":0" + minute_x;
               }
               timeButton = (Button) findViewById(R.id.timePickerButton);
               timeButton.setText(time);
          }
     };


     /*
     * Method Name: pickTime
     * Creation date: 03/07/18
     * Purpose: A method for calling a dialog, either a DatePickerDialog or a TimePickerDialog.
     *          The parameter DIALOG_ID_TIME which is set to 1 above is assigned to TimePickerDialog.
     * Required Files: None
     * Return value: None
     */

     public void pickTime(View view) {
          showDialog(DIALOG_ID_TIME);
     }




     /*
     * Method Name: OnRequest
     * Creation date: 03/07/18
     * Purpose: This method is responsible for storing user input regarding the details of the
     *          requested event. If all fields are filled up, this method calls the BackgroundWorker
     *          class which will be responsible for processing the details, and eventually sending
     *          those details to the database
     * Required Files: BackgroundWorker.java
     * Return value: None
     */

     public void OnRequest(View view) {
          /* Take title, prof, subject, and description then get the text from the EditText */
          String title = titleEditText.getText().toString();
          String prof = profEditText.getText().toString();
          String subject = subjectEditText.getText().toString();
          String description = descriptionEditText.getText().toString();
          String type = "request";                                      /* constant string flag for login */

          if (InternetConnection.checkConnection(this)) {

               if (!user_id.equals("") && !date.equals("") && !time.equals("") && !title.equals("") && !prof.equals("") && !subject.equals("") && !description.equals("")) {

                    /* If all fields are filled up, process the info*/
                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    backgroundWorker.execute(type, user_id, date, title, prof, subject, time, description);     /* to execute the backgroundWorker */

               } else {
                    Toast.makeText(RequestActivity.this, "Fill up all fields", Toast.LENGTH_SHORT).show();
               }

          } else {
               Toast.makeText(RequestActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
          }
     }
}

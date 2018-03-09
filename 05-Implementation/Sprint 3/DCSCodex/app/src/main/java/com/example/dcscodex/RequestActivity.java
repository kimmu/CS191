package com.example.dcscodex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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




     public void pickDate(View view) {
          showDialog(DIALOG_ID);
     }

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




     public void pickTime(View view) {
          showDialog(DIALOG_ID_TIME);
     }






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

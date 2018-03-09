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
* Borja, Kim                    01/31/18                    Created the BackgroundWorker  class
*                                                           Added doInBackground method
*
* Borja, Kim                    02/01/18                    Created onPreExecute, onPostExecute, and onProgressUpdate method
*
* Borja, Kim                    02/02/18                    Edited if-else statements under onPostExecute method
*
* Borja, Kim                    02/03/18                    Integrated the LoginActivity and RegisterActivity class with
*                                                           BackgroundWorker
*
* Borja, Kim                    02/07/18                    Added necessary comments
*
*/


/*
* File creation date: 01/31/18
* Development group: Group 4
* Client Group: Students
* Purpose: BackgroundWorker class contains the doInBackground method which handles the database part of the app
*          during user login and registration. This class handles the user login input and registration input for
*          the database to process through PHP scripts.
*
*/


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;




public class BackgroundWorker extends AsyncTask<String,Void,String> {
     Context context;
     AlertDialog alertDialog;
     String user_id;

     BackgroundWorker (Context ctx) {

          context = ctx; /* assign context to local context */
    }


     /*
     * Method Name: doInBackground
     * Creation date: 01/31/18
     * Purpose: Checks the first parameter passed to BackgroundWorker. If first paramater
     *          is equal to "login", it proceeds to the login procedure. Otherwise, it
     *          proceeds to the register procedure. This handles the php script responsible
     *          for the connection to the database.
     * Required Files: LoginActivity.java, RegisterActivity.java, connection.php, login_dcscodex.php, register_dcscodex.php
     * Return value: String message echoed from the php script
     */

     @Override
     protected String doInBackground(String... params) {

          /* expect the value of the first element which is type="login" */
          String type = params[0];                                                    /* catches the constant string flag for login passed as the first parameter to BackgroundWorker */
          String login_url = "http://10.150.243.235/login_dcscodex.php";             /* variable for the PHP script location used for login (database connection) */
          String register_url = "http://10.150.243.235/register_dcscodex.php";       /* variable for the PHP script location used for registration (database connection) */
          /* always check and update the ip address of the pc which is currently in use since the app might crash */

          if (type.equals("login")) {
               try {

                    /* Get the values from the passed parameters from LoginActivity.java */

                    user_id = params[1];         /* catches the student number passed as parameter from LoginActivity */
                    String password = params[2];        /* catches the password passed as parameter from LoginActivity */

                    URL url = new URL(login_url);       /* for login_url variable defined above*/
                    HttpURLConnection httpURLConnection =  (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    /* data to be added in the database */
                    String post_data = URLEncoder.encode("user_id", "UTF-8")+"="+URLEncoder.encode(user_id, "UTF-8")+"&"
                                   +URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";  /* stores the echoed message from the php script */
                    String line="";    /* for reading lines echoed from php script */

                    while((line = bufferedReader.readLine()) != null) {
                         result += line;
                         /* message from the server side is stored in result; it contains message echoed from the php file */
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;


               } catch (MalformedURLException e) {
                    e.printStackTrace();
               } catch (IOException e) {
                    e.printStackTrace();
               }

          } else if (type.equals("register")) {
               try {
                
                    /* Get the values from the passed parameters from RegisterActivity.java */
                
                    String id = params[1];                      /* catches the student number passed as parameter from RegisterActivity */
                    String surname = params[2];                 /* catches the surname passed as parameter from RegisterActivity */
                    String password = params[3];                /* catches the password passed as parameter from RegisterActivity */
                    String confirm_password = params[4];        /* catches the confirmed password passed as parameter from RegisterActivity */

                    URL url = new URL(register_url);
                    HttpURLConnection httpURLConnection =  (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    /* data to be added in the database */
                    String post_data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id, "UTF-8")
                                   + "&"+URLEncoder.encode("surname", "UTF-8")+"="+URLEncoder.encode(surname, "UTF-8")
                                   + "&"+URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")
                                   + "&"+URLEncoder.encode("confirm_password", "UTF-8")+"="+URLEncoder.encode(confirm_password, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";   /* stores the echoed message from the php script */
                    String line="";     /* for reading lines echoed from php script */

                    while((line = bufferedReader.readLine()) != null) {
                         result += line;
                         /* message from the server side is stored in result; it contains message echoed from the php file */
                    }

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;


               } catch (MalformedURLException e) {
                    e.printStackTrace();
               } catch (IOException e) {
                    e.printStackTrace();
               }
          }
          return null;
     }


     /*
     * Method Name: onPreExecute
     * Creation date: 02/01/16
     * Purpose: Creates alert dialog and displays the message "Status"
     * Return value: void
     */

     @Override
     protected void onPreExecute() {
          alertDialog = new AlertDialog.Builder(context).create();
          alertDialog.setTitle("Status");
     }


     /*
     * Method Name: onPostExecute
     * Creation date: 02/01/16
     * Purpose: Prompts user to check input based on
     *          the message echoed from the php script
     * Return value: void
     */

     @Override
     protected void onPostExecute(String result) {
          alertDialog.setMessage(result);
          alertDialog.show();
          /* show message which is coming from the server */
          /* the String result here contains messages echoed from the php file */
          /* after login displays result */

          if (result.length() == 17) {
               /* "Login successful" */
               Intent intent = new Intent(context, CalendarActivity.class);
               intent.putExtra("id", user_id);
               context.startActivity(intent);
              // context.startActivity(new Intent(context, CalendarActivity.class));
          } else if (result.length() == 19){
               /* "Login unsuccessful" */
               Toast.makeText(context, "Wrong Student Number or Password", Toast.LENGTH_SHORT).show();
          } else if (result.length() == 18) {
               /* "Insert successful" */
               context.startActivity(new Intent(context, LoginActivity.class));
          } else if (result.length() == 30) {
               Toast.makeText(context, "Registration unsuccessful", Toast.LENGTH_SHORT).show();
          }







     }


     /*
     * Method Name: onProgressUpdate
     * Creation date: 02/01/16
     * Purpose: Progress update
     * Return value: void
     */

     @Override
     protected void onProgressUpdate(Void... values) {
          super.onProgressUpdate(values);
     }

}

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
* Borja, Kim                    01/31/18                    Created the LoginActivity  class
*                                                           Added OnLogin and OpenRegister method
*
* Borja, Kim                    02/02/18                    Added some necessary comments
*
* Borja, Kim                    02/03/18                    Integrated the LoginActivity class with
*                                                           BackgroundWorker
*/



/*
* File creation date: 01/31/18
* Development group: Group 4
* Client Group: Students
* Purpose: This Login Activity class contains the OnLogin method which catches
*          the process of user attempting to log in. This also contains the
*          OpenRegister method which starts the RegisterActivity, which handles user registration in the app
*          whenever the user attempts to register.
*/



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;




public class LoginActivity extends AppCompatActivity {

     EditText studNumEditText, passwordEditText;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_login);

          studNumEditText = (EditText) findViewById(R.id.studNumEditText);    /* Edit Text which catches student number */
          passwordEditText = (EditText) findViewById(R.id.passwordEditText);  /* Edit Text which catches password */
     }



     /*
     * Method Name: OnLogin
     * Creation date: 01/31/18
     * Purpose: Checks first whether user has an internet connection or not
     *          before proceeding to the login process. Login process involves
     *          getting the student number and password input of user then passing
     *          them to the BackgroundWorker to be able to check if login credentials
     *          exist.
     * Required Files: BackgroundWorker.java, InternetConnection.java
     * Return value: void
     */

     public void OnLogin(View view) {

     /* Take username and password then get the text from the EditText */
          String user_id = studNumEditText.getText().toString();      /* user_id => student_id; gets the student number input of the user */
          String password = passwordEditText.getText().toString();    /* gets the password input of the user */
          String type = "login";                                      /* constant string flag for login */

          if (InternetConnection.checkConnection(this)) {

               /* Check first if user_id (student number) and password are not empty. */
               if (user_id.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Enter student number and password", Toast.LENGTH_SHORT).show();
               } else {

                    /* If both user_id (student number) and password are not empty, pass the task to the BackgroundWorker.
                   Then, the BackgroundWorker will try to connect to the database and apply the needed query. */

                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    backgroundWorker.execute(type, user_id, password);     /* to execute the backgroundWorker */
               }


          } else {
               Toast.makeText(LoginActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
          }
     }


     /*
     * Method Name: OpenRegister
     * Creation date: 01/31/18
     * Purpose: Starts the RegisterActivity class which handles the process
     *          for user registration
     * Required Files: RegisterActivity.java, BackgroundWorker.java, InternetConnection.java
     * Return value: void
     */

     public void OpenRegister (View view) {
          startActivity(new Intent(this, RegisterActivity.class));
     }
}

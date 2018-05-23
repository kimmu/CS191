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
* Borja, Kim                    01/31/18                    Created the RegisterActivity  class
*                                                           Added OnRegister method
*
* Borja, Kim                    02/03/18                    Integrated the RegisterActivity class with
*                                                           BackgroundWorker
*
* Borja, Kim                    02/07/18                    Added necessary comments
*
*/


/*
* File creation date: 01/31/18
* Development group: Group 4
* Client Group: Students
* Purpose: This RegisterActivity class contains the OnRegister method which processes user registration.
*          It checks first whether there is an internet connection or not before proceeding. To handle the
*          registration of the student, it passes the registration data of the user to the BackgroundWorker.
*/


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class RegisterActivity extends AppCompatActivity {

     EditText studNumEditText, surnameEditText, passwordEditText, confirmPasswordEditText;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_register);

          studNumEditText = (EditText) findViewById(R.id.studNumEditText);                    /* Edit Text which catches student number */
          surnameEditText = (EditText) findViewById(R.id.surnameEditText);                    /* Edit Text which catches surname */
          passwordEditText = (EditText) findViewById(R.id.passwordEditText);                  /* Edit Text which catches password */
          confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);    /* Edit Text which catches confirmation of password */
     }


     /*
     * Method Name: OnRegister
     * Creation date: 01/31/18
     * Purpose: Checks first whether user has an internet connection or not
     *          before proceeding to the login process. Registration process involves
     *          getting the student number, surname, and target password of the user
     *          then passing them to the BackgroundWorker for database connection.
     * Required Files: BackgroundWorker.java, InternetConnection.java
     * Return value: void
     */

     public void OnRegister (View view) {
          String id = studNumEditText.getText().toString();                           /* gets the student number input of the user */
          String surname = surnameEditText.getText().toString();                      /* gets the surname input of the user */
          String password = passwordEditText.getText().toString();                    /* gets the password input of the user */
          String confirm_password = confirmPasswordEditText.getText().toString();     /* gets the confirmation password input of the user */
          String type = "register";                                                   /* constant string flag for register */

          if (InternetConnection.checkConnection(this)) {

               if (!id.equals("") && !surname.equals("") && !password.equals("") && !confirm_password.equals("")) {
                
                    /* If all fields are filled up, check if password and confirm_password are equal and they're not empty strings */
                    if (password.contentEquals(confirm_password) && id.length()==9) {
                    
                         /* If password and confirm_password are equal, pass the parameters to BackgroundWorker.java to handle the insertion to the database */
                         BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                         backgroundWorker.execute(type, id, surname, password, confirm_password);     /* to execute the backgroundWorker */
                    } else if (id.length()!=9) {
                    
                         /* If length of student number is not 9, prompt the user */
                         Toast.makeText(RegisterActivity.this, "Check student number if valid", Toast.LENGTH_SHORT).show();
                    }
                    else {
                         Toast.makeText(RegisterActivity.this, "Password and Confirm password don't match", Toast.LENGTH_SHORT).show();
                    }
               } else {
                    Toast.makeText(RegisterActivity.this, "Fill up all fields", Toast.LENGTH_SHORT).show();
               }

          } else {
               Toast.makeText(RegisterActivity.this, "You have no internet connection", Toast.LENGTH_SHORT).show();
          }


     }
}

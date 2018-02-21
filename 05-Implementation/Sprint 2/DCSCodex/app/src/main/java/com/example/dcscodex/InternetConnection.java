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
* Borja, Kim                    01/31/18                    Created the InternetConnection  class
*
* Borja, Kim                    02/07/18                    Added necessary comments
*
*/


/*
* File creation date: 01/31/18
* Development group: Group 4
* Client Group: Students
* Purpose: InternetConnection class checks whether the user's
*          phone has internet or not.
*
*/


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class InternetConnection {


     /*
     * Method Name: checkConnection
     * Creation date: 01/31/18
     * Purpose: Checks whether internet connection is available or not
     * Return value: boolean; 1 if there's internet connection; otherwise, 0
     */

     /* Check whether internet connection is available or not */
     public static boolean checkConnection(Context context) {
          final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

          NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

          if (activeNetworkInfo != null) { /* connected to internet */
               if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {     /* connected to wifi */
                    return true;
               } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {     /* connected to mobile data */
                    return true;
               }
          }
          return false;
     }
}

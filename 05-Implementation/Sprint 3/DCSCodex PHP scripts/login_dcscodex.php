<?php


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
* 
* Borja, Kim                    01/30/18                    Created login_dcscodex.php
*                                                         
*/


/*
* File creation date: 01/30/18
* Development group: Group 4
* Client Group: Students
* Purpose: This is a script file for querying to the MySQL database. 
*			Checks if user input for student number and password are
*			correct.
*
*/



	require "connection.php";	 	/* import connection.php */
	$user_id = $_POST["user_id"];		/* student number entered by user */
	$user_pass = $_POST["password"];   /* password entered by user */
	
	$mysql_qry = "select * from login_info where id like '$user_id' and password like '$user_pass';";	/* we want to check if user name and password is correct */

	$result = mysqli_query($connection, $mysql_qry);

	if (mysqli_num_rows($result) > 0) {		/* If query provided result of more than 1 line then we know that username and passwor are correct */    
		echo "Login successful";			/* Don't change this. Has effect on onPostExecute in BackgroundWorker. */
											/* If "Login successful", BackgroundWorker.java will bring user to CalendarActivity/Home page where the calendar is located */
	}
	else {
		echo "Login unsuccessful";			/* If "Login unsuccessful", BackgroundWorker.java will display "Wrong Student Number or Password" */
	}
?>
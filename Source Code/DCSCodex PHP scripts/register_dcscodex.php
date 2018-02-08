<?php


/*
* Created by Kim Borja
* This is a course requirement for CS 192 Software Engineering II under the supervision of
* Asst. Prof. Ma. Rowena C. Solamo of the Department of Computer Science, College of Engineering,
* University of the Philippines, Diliman for the AY 2017-2018”.
*/


/*
* Code History
*
* Name of Programmer            Date changed                Description
* 
* Borja, Kim                    01/30/18                    Created register_dcscodex.php
*                                                         
*/


/*
* File creation date: 01/30/18
* Development group: Group 4
* Client Group: Students
* Purpose: This is a script file for querying to the MySQL database. 
*			Adds new user to the "login_info" database.
*
*/



	require "connection.php";	 /* import connection.php */
	
	$id = $_POST["id"];												/* student number entered by user */
	$surname = $_POST["surname"];									/* surname entered by user */
	$password = $_POST["password"];									/* password entered by user */

	$mysql_qry2 = "select * from login_info where id like '$id';";	/* we want to check if student id and password are correct */

	$result = mysqli_query($connection, $mysql_qry2); 				/* for query */

	/*
	* If id (student number) already exists, prompt user with "Student number already exists".
	* If id doesn't exist yet, then add the id/register the user
	*/

	if (mysqli_num_rows($result) > 0) {
		echo "Student number already exists";	/* "Student number already exists" will be displayed */
												/* "BackgroundWorker.java" will display "Registration unsuccessful" */
	} else {
		$mysql_qry = "insert into login_info (id, surname, password) values ('$id', '$surname', '$password')";

		if ($connection->query($mysql_qry) === TRUE and mysqli_num_rows($result) <= 0) {		
			echo "Insert successful";			/* Don't change this. Has effect on onPostExecute in BackgroundWorker */
												/* If "Insert successful", BackgrounWorker.java will bring user to the Login Page to let the user log in using the newly-made account */
												/* "Insert successful" will be displayed */
		}
	}


	$connection->close();
?>
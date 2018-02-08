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
* Borja, Kim                    01/30/18                    Created connection.php
*                                                         
*/


/*
* File creation date: 01/30/18
* Development group: Group 4
* Client Group: Students
* Purpose: This is a script file for connecting to the MySQL database. 
*
*/

$db_name = "student_db";		/* variable for database name */		
$mysql_username = "root";		/* variable for mysql username */
$mysql_password = "";			/* variable for mysql password */
$server_name = "localhost";		/* variable for server name */
$connection = mysqli_connect($server_name, $mysql_username, $mysql_password, $db_name);		/* establishes connection to the database */


?> 
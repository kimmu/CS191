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
* Borja, Kim                    03/14/18                    Created requestevent_dcscodex.php
*                                                         
*/


/*
* File creation date: 03/14/18
* Development group: Group 4
* Client Group: Students
* Purpose: This is a script file for connecting to the MySQL database, particularly the requests_info table
*			for the sending of requested events.
*
*/

	require "connection.php"; /* import */

	$id = $_POST["student_number"];
	$request_date = $_POST["request_date"];
	$request_title = $_POST["request_title"];
	$request_prof = $_POST["request_prof"];
	$request_subject = $_POST["request_subject"];
	$request_time = $_POST["request_time"];
	$request_description = $_POST["request_description"];



	$mysql_qry = "insert into requests_info (id, date, title, prof, subject, time, description) values ('$id', '$request_date', '$request_title', '$request_prof', '$request_subject', '$request_time', '$request_description')";

	if($connection->query($mysql_qry) === TRUE) {
		echo "Request Event Successful";			//25 
	} else {
		/* echo "Request Event Unsuccessful"; */	//27
		echo "Error: " . $sql . "<br>" . $connection->error;
	}

	$connection->close();



?>
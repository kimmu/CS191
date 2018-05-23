

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
* Borja, Kim                    04/04/18                    Created fetchrequestedevents_dcscodex.php
*                                                         
*/


/*
* File creation date: 04/04/18
* Development group: Group 4
* Client Group: Students
* Purpose: This is a script file for fetching the user's requested events from the db.
*
*/


	require "connection.php"; // import

	$user_id = $_POST["user_id"];

	if (mysqli_connect_error($connection)) {
		echo "Failed to connect to database".mysqli_connect_error();
	}

	$query = mysqli_query($connection, "SELECT * FROM requests_info where id like '$user_id'");	// table name

	if($query) {

		while ($row = mysqli_fetch_array($query)) {
			$flag[] = $row;
		}

		print(json_encode($flag));
	}

	mysqli_close($connection);


?>
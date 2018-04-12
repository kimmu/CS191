

<?php
	require "connection.php"; // import

	if (mysqli_connect_error($connection)) {
		echo "Failed to connect to database".mysqli_connect_error();
	}

	$query = mysqli_query($connection, "SELECT * FROM requests_info");	// table name

	if($query) {

		while ($row = mysqli_fetch_array($query)) {
			$flag[] = $row;
		}

		print(json_encode($flag));
	}

	mysqli_close($connection);


?>
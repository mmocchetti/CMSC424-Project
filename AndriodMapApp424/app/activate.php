<?php
	DEFINE('DB_USERNAME', 'root');
	DEFINE('DB_PASSWORD', 'root');
	DEFINE('DB_HOST', 'localhost');
	DEFINE('DB_DATABASE', 'app');

	$mysqli = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

	if (mysqli_connect_error()) {
	  die('Connect Error ('.mysqli_connect_errno().') '.mysqli_connect_error());
	}

	$email = $_GET['email'];
	$hash = $_GET['hash'];
	$t = time();

	$sql = "SELECT sent_time FROM Verification WHERE email = '$email' AND hash = '$hash'";
	$result = mysqli_query($mysqli, $sql);

	if ($result->num_rows > 0) {
		$time = $result->fetch_array()[0];
		if ($t - $time <= 1800000) {
			$sql2 = "UPDATE Users SET type = type - 2 WHERE email = '$email' and type > 2";
			if (mysqli_query($mysqli, $sql2)) {
				echo "Activation succeeded";
			} else {
				echo mysqli_error($mysqli);
			}
		}
		else {
			echo "Timeout";
		}
	} else {
		echo 'Activation failed.';
	}

	$mysqli->close();
?>

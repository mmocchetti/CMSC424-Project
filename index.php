<?php
  DEFINE('DB_USERNAME', 'root');
  DEFINE('DB_PASSWORD', 'root');
  DEFINE('DB_HOST', 'localhost');
  DEFINE('DB_DATABASE', 'app');

  $mysqli = new mysqli(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_DATABASE);

  if (mysqli_connect_error()) {
    die('Connect Error ('.mysqli_connect_errno().') '.mysqli_connect_error());
  }
  echo 'Connected successfully. <br>';

  $sql = "INSERT INTO Categories(name) VALUES ('college park')";

  if (mysqli_query($mysqli, $sql)) {
    echo "Insert successfully. <br>";
  } else {
    echo mysqli_error($mysqli);
  }

  $sql = "SELECT * FROM Categories";
  $result = mysqli_query($mysqli, $sql);
  if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
      echo "id: " . $row["id"]. " - Name: " . $row["name"] . "<br>";
    }
  } else {
    echo "0 results <br>";
  }

  $sql = "DELETE FROM Categories WHERE id > 0";
  if (mysqli_query($mysqli, $sql)) {
    echo "Delete successfully. <br>";
  } else {
    echo mysqli_error($mysqli);
  }

  $mysqli->close();
?>
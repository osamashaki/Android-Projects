<?php

// include db connect class
include("db_connect.php");

// connecting to db
$db = new DB_CONNECT();

    // array for JSON response
	$response = array();
    $result = mysql_query("SELECT points FROM child WHERE firstname = 'Mohammad' ") or die(mysql_error());

    if (mysql_num_rows($result) > 0) {
        $response["points2"] = array();
        
        while ($row = mysql_fetch_array($result)) {
            $contact = array();
        	$contact["chpoints2"] = $row["points"];

            array_push($response["points2"], $contact);
        }
        
        $response["success"] = 1;
        echo json_encode($response);
        
    } else {
        $response["success"] = 0;
        $response["error"] = 1;
        $response["error_msg"] = "No points was found";

        echo json_encode($response);
    }
	
  
?>
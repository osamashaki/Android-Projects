<?php

/*
 * Following code will list all the news
 */

// array for JSON response
$response = array();


// include db connect class
include("db_connect.php");

// connecting to db
$db = new DB_CONNECT();


$sid= $_POST['sid'];

$result = mysql_query("SELECT * FROM feedback where stu_id = '$sid' ") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // malls node
    $response["feedbacks"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $contact = array();
		
        $contact["id"] = $row["id"];
        $contact["title"] = $row["title"];
		$contact["description"] = $row["description"];
		$contact["college"] = $row["college"];
        $contact["fdate"] = $row["fdate"];
        
        
	// push single feedback into final response array
        array_push($response["feedbacks"], $contact);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no feedbacks found
    $response["success"] = 0;
        $response["error"] = 1;
        $response["error_msg"] = "No feedbacks were Found";

    // echo no users JSON
    echo json_encode($response);
}
?>
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


$chid= $_POST['cid'];

$data = mysql_fetch_array(mysql_query("select firstname from child where uid = '$chid'  "));
$name = $data[0];

$result = mysql_query("SELECT * FROM chore where child_id = '$name' And status != 'done' ") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // malls node
    $response["chores"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $contact = array();
		
        $contact["id"] = $row["id"];
        $contact["title"] = $row["choretitle"];
		$contact["description"] = $row["choresdesc"];
		$contact["child_id"] = $row["child_id"];
        $contact["choredate"] = $row["choredate"];
		$contact["status"] = $row["status"];
        
        
	// push single feedback into final response array
        array_push($response["chores"], $contact);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no feedbacks found
    $response["success"] = 0;
        $response["error"] = 1;
        $response["error_msg"] = "No chores were Found";

    // echo no users JSON
    echo json_encode($response);
}
?>
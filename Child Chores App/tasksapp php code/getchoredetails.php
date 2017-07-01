<?php
// array for JSON response
$response = array();


// include db connect class
include("db_connect.php");

// connecting to db
$db = new DB_CONNECT();

if (isset($_POST["id"])) {
	
    $Id = $_POST["id"];
    
    $result = mysql_query("SELECT * FROM chore WHERE id = '$Id' ") or die(mysql_error());

    if (mysql_num_rows($result) > 0) {
        $response["chore"] = array();
        
        while ($row = mysql_fetch_array($result)) {
            $contact = array();
            $contact["id"] = $row["id"];
        	$contact["title"] = $row["choretitle"];
			$contact["description"] = $row["choredesc"];
			$contact["child_id"] = $row["child_id"];
        	$contact["choredate"] = $row["choredate"];
        	$contact["status"] = $row["status"];


            array_push($response["chore"], $contact);
        }
        
        $response["success"] = 1;
        echo json_encode($response);
        
    } else {
        $response["success"] = 0;
        $response["error"] = 1;
        $response["error_msg"] = "No chores was found";

        echo json_encode($response);
    }

} else {
    $response["success"] = 0;
    $response["error"] = 2;
    $response["error_msg"] = "No id was found";
    echo json_encode($response);
}    
?>
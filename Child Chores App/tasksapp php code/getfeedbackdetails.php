<?php
// array for JSON response
$response = array();


// include db connect class
include("db_connect.php");

// connecting to db
$db = new DB_CONNECT();

if (isset($_POST["id"])) {
    $Id = $_POST["id"];
    
    $result = mysql_query("SELECT * FROM feedback WHERE id = '$Id'") or die(mysql_error());

    if (mysql_num_rows($result) > 0) {
        $response["feedback"] = array();
        
        while ($row = mysql_fetch_array($result)) {
            $contact = array();
            $contact["id"] = $row["id"];
        	$contact["title"] = $row["title"];
			$contact["description"] = $row["description"];
			$contact["college"] = $row["college"];
        	$contact["fdate"] = $row["fdate"];
        	$contact["status"] = $row["status"];
			$contact["photo"] = $row["photo"];


            array_push($response["feedback"], $contact);
        }
        
        $response["success"] = 1;
        echo json_encode($response);
        
    } else {
        $response["success"] = 0;
        $response["error"] = 1;
        $response["error_msg"] = "No feedback was found";

        echo json_encode($response);
    }

} else {
    $response["success"] = 0;
    $response["error"] = 2;
    $response["error_msg"] = "No id was found";
    echo json_encode($response);
}    
?>
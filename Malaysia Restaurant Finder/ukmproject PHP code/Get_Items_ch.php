<?php

include("db_connect.php");
$db = new DB_CONNECT();

$response = array();

$result = mysql_query("SELECT * FROM restaurant where category = 'chinese' ") or die(mysql_error());

if (mysql_num_rows($result) > 0) {

    $response["items"] = array();
    
    while ($row = mysql_fetch_array($result)) {
                      
        $contact = array();
        $contact["id"] = $row["id"]; //$row ["id"] from db
        $contact["resname"] = $row["resname"]; 
        $contact["resmobile"] = $row["resmobile"];
        $contact["imagepath"] = $row["imagepath"]; 

		
        array_push($response["items"], $contact);
    }

    $response["success"] = 1;
    $response["error"] = 0;
    $response["error_msg"] = " ";

    echo json_encode($response);
} else {

    $response["success"] = 0;
    $response["error"] = 1;    
    $response["error_msg"] = "No restaurants were found";

    echo json_encode($response);
}
?>
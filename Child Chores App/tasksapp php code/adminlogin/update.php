<?php
// array for JSON response
$response = array();


// include db connect class
include("../db_connect.php");

// connecting to db
$db = new DB_CONNECT();

// response json

if (isset($_POST['fid'])) 
{
	
    $fid = $_POST['fid'];
    $st = $_POST['status'];
    
  
    $result = mysql_query("UPDATE feedback SET status = '$st' WHERE id = '$fid' ");
 
    if ($result) {
        $response["success"] = 1;
        $response["error"] = 0;
        //$response["error_msg"] = "";

        echo json_encode($response);
            
    } else {
        $response["success"] = 0;
        $response["error"] = 1;
        //$response["error_msg"] = "Error occured in Updating status";
            
        echo json_encode($response);
    }

} 
else {
    $response["success"] = 0;
    $response["error"] = 2;
    //$response["error_msg"] = "Error occured in Recieving status";
    
    echo json_encode($response);
}
?>
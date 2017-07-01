<?php

// include db connect class
include("db_connect.php");

// connecting to db
$db = new DB_CONNECT();

// response json
$response = array();

if (isset($_POST["id"]) ) {

    $Id = $_POST["id"];

    $sqlDel = mysql_query("DELETE FROM chore WHERE id='$Id'") or die(mysql_error());
   
        if ($sqlDel) {
            $response["success"] = 1;
            $response["error"] = 0;
            $response["error_msg"] = "";
            
            echo json_encode($response);
            
        } else {
            $response["success"] = 0;
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in deleting chore";
            
            echo json_encode($response);
          }

} else {
    $response["success"] = 0;
    $response["error"] = 2;
    $response["error_msg"] = "Error occured in Recieving Id";
    
    echo json_encode($response);
}
?>
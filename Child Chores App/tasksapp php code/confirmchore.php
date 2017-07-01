<?php

// include db connect class
include("db_connect.php");

// connecting to db
$db = new DB_CONNECT();

// response json
$response = array();

if (isset($_POST["id"]) ) {

    $Id = $_POST["id"];
	$uid = $_POST["uid"];
	
	$data = mysql_fetch_array(mysql_query("select points from child where uid = '$uid'  "));
    $p = $data[0];
	$p = $p + 2 ;
	
	$sqlupdate = mysql_query("update child set points = '$p' WHERE uid='$uid'") or die(mysql_error());
	
    $sqlDel = mysql_query("update chore set status = 'done' WHERE id='$Id'") or die(mysql_error());
   
        if ($sqlDel && $sqlupdate) {
            $response["success"] = 1;
            $response["error"] = 0;
            $response["error_msg"] = "success";
            
            echo json_encode($response);
            
        } else {
            $response["success"] = 0;
            $response["error"] = 1;
            $response["error_msg"] = "Error occured in updating chore status";
            
            echo json_encode($response);
          }

} else {
    $response["success"] = 0;
    $response["error"] = 2;
    $response["error_msg"] = "Error occured in Recieving Id";
    
    echo json_encode($response);
}
?>
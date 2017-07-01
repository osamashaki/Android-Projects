<?php
// array for JSON response
$response = array();


// include db connect class
include("../db_connect.php");

// connecting to db
$db = new DB_CONNECT();

// response json
	
    $name = $_POST['chname'];
	$amount = $_POST['allowance'];
	if($name == "Ahmad")
	{
		$id = 1;	
	}
	else if($name == "Mohammad")
	{
		$id = 2;	
	}
   
    
  
    $result = mysql_query("INSERT INTO allowance(child_id, amount) VALUES('$id', '$amount')");
 
    if ($result) {
        $response["success"] = 1;
        $response["error"] = 0;
        $response["error_msg"] = "add success";

        echo json_encode($response);
            
    } else {
        $response["success"] = 0;
        $response["error"] = 1;
        $response["error_msg"] = "Error occured in add allowance";
            
        echo json_encode($response);
    }


?>
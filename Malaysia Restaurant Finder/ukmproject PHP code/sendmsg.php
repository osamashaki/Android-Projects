<?php
 
$response = array();
 
if (isset($_POST['name']) && isset($_POST['email'])) {
 
    include("db_connect.php");
    $db = new DB_CONNECT();
     
    $name = $_POST['name'];
    $email = $_POST['email'];
	$phone = $_POST['phone'];
    $msg = $_POST['msg'];

      
    $result = mysql_query("INSERT INTO msg(name, email, phone, textmsg, date_) VALUES('$name', '$email', '$phone', '$msg', NOW())");
    if ($result) {
        $response["success"] = 1;
        $response["id"] = mysql_insert_id();
        $response["error"] = 0;        
        $response["error_msg"] = "Message has successfully sent.";
    
        echo json_encode($response);
    } 
    else {
        $response["success"] = 0;
        $response["id"] = 0;        
        $response["error"] = 1;                
        $response["error_msg"] = "Oops! An error occurred.";
    
        echo json_encode($response);
    }
} 
else {
    $response["success"] = 0;
    $response["id"] = 0;            
    $response["error"] = 2;                    
    $response["error_msg"] = "Required field(s) is missing";
    
    echo json_encode($response);
}



?>
<?php

include("db_connect.php");
$db = new DB_CONNECT();

$response = array();

if (isset($_POST["itemId"])) {
    $itemId = $_POST["itemId"];
    
    $result = mysql_query("SELECT * FROM restaurant WHERE id = '$itemId'") or die(mysql_error());

    if (mysql_num_rows($result) > 0) {
        $response["item"] = array();

        while ($row = mysql_fetch_array($result)) {
                        
            $contact = array();            
            $contact["id"] = $row["id"]; //$row ["id"] from db
			$contact["resname"] = $row["resname"]; 
			$contact["resmobile"] = $row["resmobile"];
			$contact["openinghours"] = $row["openinghours"];
			$contact["dishes"] = $row["dishes"];
			$contact["address"] = $row["address"];
			$contact["location"] = $row["location"];
			$contact["imagepath"] = $row["imagepath"];


             
            array_push($response["item"], $contact);
        }

        $response["success"] = 1;
        $response["error"] = 0;
        $response["error_msg"] = " ";
    
        echo json_encode($response);
    
    } else {
        $response["success"] = 0;
        $response["error"] = 1;    
        $response["error_msg"] = "No item were found";
    }

} else {
    $response["success"] = 0;
    $response["error"] = 2;
    $response["error_msg"] = "Required field(s) is missing";
    echo json_encode($response);
}    
?>
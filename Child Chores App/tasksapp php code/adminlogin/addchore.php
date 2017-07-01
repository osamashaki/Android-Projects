<?php

// include db connect class
require_once ('../db_connect.php');

// connecting to db
$db = new DB_CONNECT();

// response json
$response = array();

if (isset($_POST["title"]) && isset($_POST["desc"]) ) 
{

    $title = $_POST["title"];
    $description = $_POST["desc"];
	$child = $_POST["name"];
	$chdate = date("d/m/Y h:m:s");
	
	$result = mysql_query("INSERT INTO chore(choretitle, choredesc, child_id, choredate,status) 
                            VALUES('$title','$description','$child', '$chdate', 'Not finished')");    
    
        // check for successful store
        if ($result) {
            $chore_id = mysql_insert_id(); // last inserted id

            $chore = mysql_query("SELECT * FROM chore WHERE id = $chore_id");
            
            if (mysql_num_rows($chore) > 0) {
                $response["chore"] = array();
                
                while ($row = mysql_fetch_array($chore)) {
                    $contact = array();
                    $contact["id"] = $row["id"];
					$contact["title"] = $row["choretitle"];
					$contact["desc"] = $row["choredesc"];
					$contact["child"] = $row["child_id"];
					$contact["choredate"] = $row["choredate"];
					$contact["status"] = $row["status"];

            
                    array_push($response["chore"], $contact);
                }

                $response["success"] = 1;
                $response["error"] = 0;        
				$response["id"] = mysql_insert_id();
				$response["error_msg"] = "Chore has successfully created.";
				
				        echo json_encode($response);

                
            } else {
                $response["success"] = 0;
                $response["error"] = 1;
                $response["error_msg"] = "chore is not found";
				
				echo json_encode($response);


            }
            
            //echo json_encode($response);
            
        } else {
            $response["success"] = 0;
            $response["error"] = 2;
            $response["error_msg"] = "Error occured in Adding chore";
            
            echo json_encode($response);
          }

} else {
    $response["success"] = 0;
    $response["error"] = 3;
    $response["error_msg"] = "Required field(s) is missing";
    
    echo json_encode($response);
}

function random_string($length) {
    $key = '';
    $keys = array_merge(range(0, 9), range('a', 'z'));

    for ($i = 0; $i < $length; $i++) {
        $key .= $keys[array_rand($keys)];
    }

    return $key;
}
?>
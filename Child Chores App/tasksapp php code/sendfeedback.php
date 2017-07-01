<?php

// include db connect class
require_once ('db_connect.php');

// connecting to db
$db = new DB_CONNECT();

// response json
$response = array();

if (isset($_POST["title"]) && isset($_POST["desc"]) && isset($_POST["venue"]) ) 
{

    $title = $_POST["title"];
    $description = $_POST["desc"];
    $venue = $_POST["venue"];
	$studentId = $_POST["stuid"];
	$fdate = date("d/m/Y h:m:s");
	
	if(isset($_POST['image']) && !empty($_POST['image']) && $_POST['image'] != null){    
        $img = $_POST['image'];
		$img = str_replace('data:image/png;base64,', '', $img);
        $img = str_replace(' ', '+', $img);
        $data = base64_decode($img);
        
        $fileName = random_string(10);
        $photoUrl = 'uploads/'. $fileName . '.png';
        file_put_contents($photoUrl, $data);
    }
       

    
    $result = mysql_query("INSERT INTO feedback(title, description, college, fdate, photo, stu_id) 
                            VALUES('$title','$description', '$venue' , '$fdate','$photoUrl','$studentId')");    
    
        // check for successful store
        if ($result) {
            $feedback_id = mysql_insert_id(); // last inserted id

            $feedback = mysql_query("SELECT * FROM feedback WHERE id = $feedback_id");
            
            if (mysql_num_rows($feedback) > 0) {
                $response["feedback"] = array();
                
                while ($row = mysql_fetch_array($feedback)) {
                    $contact = array();
                    $contact["id"] = $row["id"];
                    
                    array_push($response["feedback"], $contact);
                }

                $response["success"] = 1;
                $response["error"] = 0;        
				$response["id"] = mysql_insert_id();
				$response["error_msg"] = "Item has successfully created.";
				$response['file_path'] = $photoUrl;
				
				        echo json_encode($response);

                
            } else {
                $response["success"] = 0;
                $response["error"] = 1;
                $response["error_msg"] = "feedback is not found";
				$response['file_path'] = "";
				
				        echo json_encode($response);


            }
            
            //echo json_encode($response);
            
        } else {
            $response["success"] = 0;
            $response["error"] = 2;
            $response["error_msg"] = "Error occured in Adding feedback";
            
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
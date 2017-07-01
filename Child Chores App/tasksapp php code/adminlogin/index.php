<?php

/**
 PHP API for Login, Register, Changepassword, Resetpassword Requests and for Email Notifications.
 **/

if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // Get tag
    $tag = $_POST['tag'];

    // Include Database handler
    require_once 'db_functions.php';
    $db = new DB_Functions();
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];

        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
			$response["uid"] = $user["unique_id"];
			$response["user"]["id"]    = $user["uid"]; //from java file, from db fields
			$response["user"]["fname"] = $user["firstname"];
            $response["user"]["lname"] = $user["lastname"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    } 
  else if ($tag == 'chgpass'){
  
  $uid = $_POST['userid']; echo $uid;

  $newpassword = $_POST['newpas'];
  

  		$hash = $db->hashSSHA($newpassword);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"];
  		
		$result = mysql_query("UPDATE parent SET encrypted_password = '$encrypted_password',salt = '$salt' 
					  WHERE uid = '$uid' ");
						  
			  
		if ($result) {
 
			$response["success"] = 1;
			echo json_encode($response);
		
		}
		else
		{
			$response["error"] = 1;
			echo json_encode($response);
		}				  
}//
else if ($tag == 'forpass'){

	$email = $_POST['email'];

		if ($db->isUserExisted($email)) 
		{
			
			$newpassword = '123456';
	  
			$hash = $db->hashSSHA($newpassword);
			$encrypted_password = $hash["encrypted"]; // encrypted password
			$salt = $hash["salt"];
			
			$result = mysql_query("UPDATE parent SET encrypted_password = '$encrypted_password',salt = '$salt' 
						  WHERE email = '$email' ");
							  
					  
							  
			if ($result) 
			{
	 
				$response["success"] = 1;
				echo json_encode($response);
			
			}
			else
			{
				$response["error"] = 1;
				echo json_encode($response);
			}				
			   
			 
		}
			   else 
			   {
				
				$response["error"] = 2;
				$response["error_msg"] = "User not exist";
				echo json_encode($response);
				
			   }
			   
        


}
else if ($tag == 'register') {
        // Request type is Register new user
        $fname = $_POST['fname'];
		$lname = $_POST['lname'];
        $email = $_POST['email'];
        $password = $_POST['password'];


        
        // check if user is already existed
		if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else {
		
                    // store user
            $user = $db->storeUser($fname, $lname, $email, $password);
            if ($user) {
                // user stored successfully
            $response["success"] = 1;
			$response["user"]["uid"] = $user["unique_id"];
			$response["user"]["id"] = $user["uid"]; //from java file, from db fields
            $response["user"]["fname"] = $user["firstname"];
            $response["user"]["lname"] = $user["lastname"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
                           
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "JSON Error occured in Registartion";
                echo json_encode($response);
            
        }
    }
}
else {
         $response["error"] = 3;
         $response["error_msg"] = "JSON ERROR";
        echo json_encode($response);
    }
}
else if ($tag == 'updatestatus'){
  
  		$st  = $_POST['status'];
  		$fid = $_POST['fid'];

 		$result = mysql_query("UPDATE chore SET status = '$st' WHERE id = '$fid' ");
						  
			  
		if ($result) {
 
			$response["success"] = 1;
			echo json_encode($response);
		
		}
		else
		{
			$response["error"] = 1;
			echo json_encode($response);
		}				  
}// 

else {
    echo "Error"; //app name or error
}
?>

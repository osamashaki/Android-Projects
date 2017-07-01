<?php

define('DB_USER', "root");
define('DB_PASSWORD', "123456"); 
define('DB_DATABASE', "complaints"); 
define('DB_SERVER', "localhost"); 

mysql_connect($dbhost,$dbuser,$dbpassword,0,65536);
	mysql_select_db($dbname);
?>

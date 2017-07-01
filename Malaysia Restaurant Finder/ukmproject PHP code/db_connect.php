<?php

class DB_CONNECT {

    function __construct() {
        $this->connect();
    }

    function __destruct() {
        $this->close();
    }

    function connect() {
		
		define("DB_HOST", "localhost");
		define("DB_USER", "root");
		define("DB_PASSWORD", "123456");
		define("DB_DATABASE", "osamashaki");
		
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD) or die(mysql_error());

        $db = mysql_select_db(DB_DATABASE) or die(mysql_error()) or die(mysql_error());

        return $con;
    }

    function close() {
        mysql_close();
    }

}

?>
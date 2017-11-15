<?php
	define('hostname', 'localhost');
	define('user', 'root');
	define('password', '*****'); // Wachtwoord weggelaten
	define('databaseName', 'Android');

	$connect = mysqli_connect(hostname, user, password, databaseName);
?>

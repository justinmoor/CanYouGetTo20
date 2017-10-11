<?php
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		require 'connection.php';
		setScore();
	} 

	function setScore(){
		global $connect;

		$naam =  $_POST["naam"];
		$score = $_POST["score"];

		$query = "INSERT INTO spel (naam, score) VALUES ('$naam', '$score')";
		mysqli_query($connect, $query) or die (mysqli_error($connect));
		mysqli_close($connect);
	}
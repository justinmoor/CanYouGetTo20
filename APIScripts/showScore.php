<?php
	
	if($_SERVER["REQUEST_METHOD"] == "GET"){
		require 'connection.php';
		showScore();
	} else{
		echo "Failed";
	}

	function showScore(){
		global $connect;
		$query = "SELECT * 
				FROM spel 
				ORDER BY score DESC 
				LIMIT 10";

		$result = mysqli_query($connect, $query);

		$number_of_rows = mysqli_num_rows($result);

		$temp_array = array();

		if($number_of_rows > 0){
			while($row = mysqli_fetch_assoc($result))
			{
				$temp_array[] = $row;
			}
		}
	
		header('Content-type: application/json');
		echo json_encode(array("scores"=>$temp_array));
		
		mysqli_close($connect);
	}

	
	?>
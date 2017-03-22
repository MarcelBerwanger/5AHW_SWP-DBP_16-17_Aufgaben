<?php
$herid = $_POST['herid'];
$name = $_POST['name'];
$jahr = $_POST['jahr'];

$con=mysqli_connect("localhost","root","","auto");
if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_query($con,"INSERT INTO hersteller (herstellerid, name, gruendungsjahr) VALUES($herid, '$name', $jahr)");
if(mysqli_errno($con)==1062){
	echo "Saving failed - Duplicate Entry";
}
else if(mysqli_errno($con)!=0){
	echo "Saving failed - Code failed";
}
else{
	echo "Successfull saved";
}
?>
<?php


// check for required fields
if (isset($_POST['lat'])&& isset($_POST['lng']) && isset($_POST['travelno'])) {
    
    $lat= $_POST['lat'];
    $lng= $_POST['lng'];
    $travelno = $_POST['travelno'];
    $vacancy = $_POST['vacancy'];
    $speed= $_POST['speed'];

    $lat = (float) $lat;
    $lng = (float) $lng;
    $speed = (double) $speed;
    $speed=$speed*3.6; //convert the meter per second speed to kilometer per second
    $vacant = (int) $vacant;
    date_default_timezone_set('Asia/Manila');
	$myDate= date("m/d/y");
	$myTime= date("g:i a");
    
    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    //$result = mysql_query("INSERT INTO tbltravelstatus(lat, lng, travelno,vacancy,speed,dateup,timeup) VALUES('$driverid', '$aideid', '$travelno','$vacancy','$speed','$myDate','$myTime')");
      $result = mysql_query("UPDATE tbltravelstatus
                      SET lat =  '$lat',lng='$lng',vacancy='$vacancy',speed='$speed',dateup='$myDate',timeup='$myTime'
                      WHERE travelno = '$travelno'");
// echoing JSON response
    echo json_encode($result);
}
?>	

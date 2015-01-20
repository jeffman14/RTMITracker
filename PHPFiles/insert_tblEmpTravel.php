<?php


// check for required fields
if (isset($_POST['driverid'])&& isset($_POST['aideid']) && isset($_POST['travelno'])) {
    
    $driverid = $_POST['driverid'];
    $aideid = $_POST['aideid'];
    $travelno = $_POST['travelno'];

    
    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO tblemptravel(touraideno, driverno, travelno) VALUES('$aideid', '$driverid', '$travelno')");
// echoing JSON response
    echo json_encode($result);
}
?>	

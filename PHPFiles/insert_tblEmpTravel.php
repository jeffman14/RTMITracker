<?php
//0 error
//1 already signed in
//2 travelno not found
//3 inserted

// check for required fields
if (isset($_POST['driverid'])&& isset($_POST['aideid']) && isset($_POST['travelno'])) {
    
    $driverid = $_POST['driverid'];
    $aideid = $_POST['aideid'];
    $travelno = $_POST['travelno'];

    
    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    $query = "SELECT * FROM tbltravel where travelno='$travelno'"; 
    $q=mysql_query($query);
    $rowCount = mysql_num_rows($q);
    
    if ($rowCount != 0 ){
        //travelno found in tbltravel
        //search for travelno in tblEmpTravel
        $query = "SELECT * FROM tblemptravel where travelno='$travelno'"; 
        $q=mysql_query($query);
        $rowCount = mysql_num_rows($q);
        
        //if found
        if ($rowCount != 0 ){
        //travelno found in tblemptravel
        }
        
        else{
        // insert travelno in tblEmpTravel
        $result = mysql_query("INSERT INTO tblemptravel(touraideno, driverno, travelno) VALUES('$aideid', '$driverid', '$travelno')");
        // echoing JSON response
        $response["success"] = 1;
        echo json_encode("Trial ni");
     } 
   }
echo json_encode("aksa");
 
}
?>		

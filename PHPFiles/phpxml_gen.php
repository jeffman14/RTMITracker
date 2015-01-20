<?php      

function getAddress($lat, $lon){
   $url  = "http://maps.googleapis.com/maps/api/geocode/json?latlng=".
            $lat.",".$lon."&key=AIzaSyA6vVy2Q_kouTjI8t0gUjGsS_wBLBFLotg";
   $json = @file_get_contents($url);
   $data = json_decode($json);
   $status = $data->status;
   $address = '';
   if($status == "OK"){
      $address = $data->results[0]->formatted_address;
    }
   return $address;
  }

function parseToXML($htmlStr) 
{ 
$xmlStr=str_replace('<','&lt;',$htmlStr); 
$xmlStr=str_replace('>','&gt;',$xmlStr); 
$xmlStr=str_replace('"','&quot;',$xmlStr); 
$xmlStr=str_replace("'",'&#39;',$xmlStr); 
$xmlStr=str_replace("&",'&amp;',$xmlStr); 
return $xmlStr; 
}
 
// Opens a connection to a MySQL server
$connection=mysql_connect ("","","");
if (!$connection) {
  die('Not connected : ' . mysql_error());
}

// Set the active MySQL database
$db_selected = mysql_select_db("", $connection);
if (!$db_selected) {
  die ('Can\'t use db : ' . mysql_error());
}

// Select all the rows in the markers table
$query = "SELECT tbltravel.bodyno, tblroute.origin, tblroute.desti, tbltravelstatus.speed, tbltravelstatus.vacancy, tbltravelstatus.eta, tbltravelstatus.timeup, tbltravelstatus.dateup, tbltravelstatus.lat, tbltravelstatus.lng
FROM tblroute,tbltravel,tbltravelstatus where tbltravel.travelno=tbltravelstatus.travelno and tblroute.routeno=tbltravel.routeno";

$result = mysql_query($query);
if (!$result) {
  die('Invalid query: ' . mysql_error());
}

header("Content-type: application/json");

// Start XML file, echo parent node

$data = array();
// Iterate through the rows, printing XML nodes for each
while ($row = @mysql_fetch_assoc($result)){
  // ADD TO XML DOCUMENT NODE
  $temp = array(
    "lat" => $row['lat'],
    "lng" => $row['lng'],
    "speed" => $row['speed'],
    "bodyno" => $row['bodyno'],
    "desti" => $row['desti'],
    "origin" => $row['origin'],
    "vacancy" => $row['vacancy'],
    "timeup" => $row['timeup'],
    "dateup" => $row['dateup'],
    "eta" => $row['eta'],
    "address" =>getAddress($row['lat'], $row['lng'])
  );

  array_push( $data , $temp );
  /*echo '<marker ';
  echo 'lat="' . $row['lat'] . '" ';
  echo 'lng="' . $row['lng'] . '" ';
  echo 'bodyno="' . $row['bodyno'] . '" ';
  echo 'speed="' . $row['speed'] . '" ';
  echo 'desti="' . $row['desti'] . '" ';
  echo 'origin="' . $row['origin'] . '" ';
  echo 'vacancy="' . $row['vacancy'] . '" ';
  echo 'timeup="' . $row['timeup'] . '" ';
  echo 'dateup="' . $row['dateup'] . '" ';
  echo 'eta="' . $row['eta'] . '" ';
echo 'address="' . getAddress($row['lat'], $row['lng']) . '" ';
  echo '/>';*/
}

// End XML file
echo json_encode( $data );

?>

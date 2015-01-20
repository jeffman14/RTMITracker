<?php


mysql_connect("","","");
mysql_select_db("");

ini_set('set_memory_limit',-1);
$json = file_get_contents('php://input');
$obj = json_decode($json);

$username=$_POST['username'];
$password=$_POST['password'];

$query = "SELECT tbltouraide.touraideno
FROM tbltouraide INNER JOIN tblempprofile ON tbltouraide.empno = tblempprofile.empno where tbltouraide.touraideno='$username' and tblempprofile.password='$password'";

$q=mysql_query($query);

$rowCount = mysql_num_rows($q);
//echo $query;

if ($rowCount != 0 ){
   	while($e=mysql_fetch_assoc($q))
          $output[]=$e;
   echo(json_encode($output));

   }

?>

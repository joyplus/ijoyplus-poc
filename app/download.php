<?php
   $iosUrl="http://app.joyplus.tv/app/ijoyplus1.1.ipa";
   $androidUrl="http://app.joyplus.tv/app/ijoyplus1.0.3.ipa";
   $userAgent =  isset($_SERVER['HTTP_USER_AGENT'])?$_SERVER['HTTP_USER_AGENT']:""; //iPod
//   var_dump($userAgent);
   if(strpos($userAgent, "iPhone") !==false || strpos($userAgent, "iPod") !==false || strpos($userAgent, "iPad") !==false){
   	 header("Location:".$iosUrl);
   }else {
//   	var_dump($userAgent);
   	 header("Location:".$androidUrl);
   	 exit();
   }
   
   
   
?>
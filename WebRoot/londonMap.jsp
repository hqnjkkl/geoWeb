<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>aLondonMap</title>
<script src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCBl9iBM91pQJlOfbBKMpKVsFUvFfyBNrA&sensor=false">

</script>

<script>
function initialize()
{
var mapProp = {
  center:new google.maps.LatLng(51.508742,-0.120850),
  zoom:5,
  mapTypeId:google.maps.MapTypeId.ROADMAP
  };
var map=new google.maps.Map(document.getElementById("googleMap"),mapProp);
}

google.maps.event.addDomListener(window, 'load', initialize);

</script>
</head>

<body>
<div id="googleMap" style="width:500px;height:380px;"></div>
<form name="input" action="map/TestReceive" method="post">
first destination: 
<input type="text" name="fDes" />
<br />
Second destination: 
<input type="text" name="sDes" />
<input type="submit" value="规划" />
</form>
<frameset cols="25%,75%">
<!-- 
	<frame src="frame_a.htm">
   <frame src="frame_b.htm">
 -->
   
</frameset>
</body>
</html>
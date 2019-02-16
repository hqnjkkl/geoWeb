<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.io.*,java.util.*,com.geoImage.web.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基于旅游文记的旅游景点推荐及行程路线规划系统</title>
<!--
这些css表单样式可以放在一个文件当中，然后引入就行了
-->
<style type="text/css">
html {
	height: 100%
}

body {
	height: 100%;
	margin: 0;
	padding: 0;
	border: 10px solid #EE872A
}

#map_canvas {
	margin: 3;
	padding: 3;
	width: 64%;
	height: 80%;
	float: right;
	border: 3px solid #32872A
}

#attraction_list {
	margin: 3;
	padding: 0;
	width: 34%;
	height: 80%;
	float: left;
	border: 3px solid blue
}

#attraction_area {
	margin: 1;
	padding: 0;
	width: 100%;
	height: 90%;
	float: left;
	border: 1px solid black;
	overflow: auto
}
</style>
<script type="text/javascript"
	src="http://maps.googleapis.com/maps/api/js?key=AIzaSyCBl9iBM91pQJlOfbBKMpKVsFUvFfyBNrA&sensor=false">
	
</script>
<script type="text/javascript">
	function init3() {
		<%-- /*var numbers=eval("<%=session.getAttribute("selectedAtt")%>");*/ --%>
		getAttFromServlet();
	}
	
	function getAttFromServlet()
	{
		var url = "servlet/GetAttServlet";
    	if(window.XMLHttpRequest){
    	//么有定义语句，反而可以在很多函数当中使用
    		req = new XMLHttpRequest();
    	}else if(window.ActiveXObject){
    		req = new ActiveXObject("Microsoft.XMLHTTP");
    	}
    	if(req){
    		     req.open("GET",url, true); 
                 req.onreadystatechange = logic; 
                 req.send(null); 
    	}
	}
	
	function logic()
	{
		if(req.readyState == 4)
    	{
    		if(req.status==200){
    			var inf = eval(req.responseText);
    			showAtt(inf);
    		}
    	}
	}
	
	function showAtt(objs)
	{
		var tripArray = new Array();
		var tripName = new Array();
		if(!objs || objs.length==0)
		{
			tripArray[0] = new google.maps.LatLng(21.35269, -157.96962);
			tripName[0] ="Pearl Harbor";
		}else
		{
			for(var i=0;i<objs.length;i++)
			{
				tripArray[i] = new google.maps.LatLng(objs[i].latitude,objs[i].longitude);
				var orgName = objs[i].webGeonameContent;
				var name =objs[i].webGeonameId +":"+orgName.substring(orgName.lastIndexOf(",")+2);
				tripName[i] = name;
			}
		}
		var mapOptions = {
			center : tripArray[0],
			zoom : 10,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

		var map = new google.maps.Map(document.getElementById("map_canvas"),
				mapOptions);
		for (var i = 0; i < tripArray.length; i++) {
			var markers = new google.maps.Marker({
				position : tripArray[i],
			});
			markers.setMap(map);
			var infowindow = new google.maps.InfoWindow({
				content : tripName[i] + ":" + i
			});
			infowindow.open(map, markers);
		}
	}
		
	function addAttraction(att, op) {
		//alert(att);
		var url = "servlet/AddAttServlet?op=" + op + "&attId=" + att;
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (req) {
			req.open("GET", url, true);
			req.onreadystatechange = showSelected;
			req.send(null);
		}
	}
	
	function showSelected() {
		//alert("show");
		if (req.readyState == 4) {
			if (req.status == 200) {
				var selected = document.getElementById("selectedAtt");
				var sInf = eval(req.responseText);
				//alert("req.Respnse:" + req.responseText+":"+sInf.length);
				var showText="";
				if (sInf) {
					for (var i = 0; i < sInf.length; i++) {
						showText += "<span style=\"border:1px solid black;font-size:25px\">"
								+ sInf[i]
								+ ":<button style=\"font-size:10px;margin:2;padding:3\" type=\"button\""
								+ "onclick=\"addAttraction(this.value,\'delete\')\" value=\""
								+ sInf[i] + "\">delete</button>" + "</span>";
					}
					selected.innerHTML = showText;
				}
			}
		}
	}
	
	function travelPlan()
	{
		var url = "servlet/TravelPlanServlet?day="+document.getElementById("area").value+"&len=3";
    	if(window.XMLHttpRequest){
    	//么有定义语句，反而可以在很多函数当中使用
    		req = new XMLHttpRequest();
    	}else if(window.ActiveXObject){
    		req = new ActiveXObject("Microsoft.XMLHTTP");
    	}
    	if(req){
    		     req.open("GET",url, true); 
                 req.onreadystatechange = planResult; 
                 req.send(null); 
    	}
	}
	
	function planResult()
	{
		if(req.readyState==4 && req.status==200)
		{
			var inf = eval(req.responseText);
			showPlan(inf);
		}
	}
	
	function showPlan(inf)
	{
		//alert(inf[0].lstNodes[0].latitude);
		var lines = new Array();
		if(inf.length>0)
		{
			//alert(inf);
			//alert(inf[0].lstNodes);
			var lsn = eval(inf[0].lstNodes);
			//alert(lsn);
			var center = new google.maps.LatLng(inf[0].lstNodes[0].latitude,inf[0].lstNodes[0].longtitude);
			var mapOptions = {
	          center: center,
	          zoom: 10,
	          mapTypeId: google.maps.MapTypeId.ROADMAP
	        };
	        var map = new google.maps.Map(document.getElementById("map_canvas"),mapOptions);
	        //alert("from:"+inf[0][0].from.latitude);
	       var table = "<table id=tableList width=\"90%\" height=\"60%\" border=1 align=\"left\" style=\"font-size:12px;\">";
		table += "<tr bgcolor=\"#949494\"><th>Attraction Name</th><th>Attraction occurence</th>"
				+ "<th>latitude</th>"
				+ "<th>longitude</th>"
				+ "<th>attId</th></tr>";
	         for(var i=0;i<inf.length;i++)
	        {
	        table += "<tr><td>第"+(i+1)+"天</td></tr>";
 	 	        for(var j=0;j<inf[i].lstEdges.length;j++)
	 	        {
	 	        var edge = inf[i].lstEdges[j];
	 	        //alert(i+":"+j+"two:"+edge.from.latitude+":"+edge.from.longtitude+";"+edge.to.latitude+":"+edge.to.longtitude);
	 	         	 var node1 = new google.maps.LatLng(edge.from.latitude,edge.from.longtitude);
	 	        	 var node2 = new google.maps.LatLng(edge.to.latitude,edge.to.longtitude); 
	 	        	 var pp = [node1,node2];
	 	        	 var fp=new google.maps.Polyline({
				  	path:pp,
				  	strokeColor:"#FF0099",
				  	strokeOpacity:0.8,
				  	strokeWeight:2
				  	});
				  fp.setMap(map);
				  //alert("fp"+fp);
	 	        }
	 	        lines[i] = new Array();
		        for(var j=0;j<inf[i].lstNodes.length;j++)
		        {
		        table +=  "<tr><td>"
					+ inf[i].lstNodes[j].name
					+ "</td>"
					+ "<td>"
					+ inf[i].lstNodes[j].occurence
					+ "</td>"
					+ "<td>"
					+ inf[i].lstNodes[j].latitude
					+ "</td>"
					+ "<td>"
					+ inf[i].lstNodes[j].longtitude
					+ "</td>"
					+ "<td>"
					+ inf[i].lstNodes[j].id + "</td></tr>";
	 	       		//alert(i+":"+j+":lines:"+lines[i][j]);
		        	lines[i][j] = new google.maps.LatLng(inf[i].lstNodes[j].latitude,inf[i].lstNodes[j].longtitude);
	 	       		var markers = new google.maps.Marker({position:lines[i][j],});
	 	       		var orgName = inf[i].lstNodes[j].name;
	 	       		var name = inf[i].lstNodes[j].id+":"+orgName.substring(orgName.lastIndexOf(",")+2);
					markers.setMap(map);
					var infowindow = new google.maps.InfoWindow({content:name});
					infowindow.open(map,markers);
	 	        }
	        }
	        table += "</table>";
	        document.getElementById("attraction_area").innerHTML = table;
	       // alert(lines[i][j]);
	      // alert(lines[0][0]);
		}
	}
</script>
</head>
<body onload="init3()">
	<h1 align="center">行程规划</h1>
	<a href=HotAttractions.jsp>热门景点展示</a>
	<a href=RuleAttractions.jsp>关联规则景点查询</a>
	<a href=RoutePlan.jsp>路线规划</a> 已选择景点id：
	<span id=selectedAtt> <%
 	Set<Integer> selected = (Set<Integer>) session
 			.getAttribute("selectedAtt");
 	if (selected != null && selected.size() > 0) {
 		for (Iterator<Integer> iterator = selected.iterator(); iterator
 				.hasNext();) {
 			Integer attId = iterator.next();
 %> <span style="border:1px solid black;font-size:25px"><%=attId%>:
			<button style="font-size:10px;margin:2;padding:3" type="button"
				onclick="addAttraction(this.value,'delete')" value=<%=attId%>>
				delete</button></span> <%
 	}
 	}
 %>
	</span>
	<button onclick="getAttFromServlet()" type="button">refresh</button>
	<br />

	<div id="out_form" style="width:100%; height:100%;center">
		<span id="attraction_list"> 输入天数: <input id="area" type="text"
			value="3" name="first_name"><br />
			<button type="button" onclick="travelPlan()">开始规划:</button> <br />
			<div id="attraction_area"></div>
		</span> <span id="map_canvas"></span>
	</div>
	
</body>
</html>
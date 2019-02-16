<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.io.*,java.util.*"
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基于旅游文记的旅游景点推荐及行程路线规划系统</title>
<style type="text/css">
	html{
		height:100%
	}
	body{
		height:100%;
		margin:0;
		padding:0;
		border:10px solid #EE872A
	}
	#map_canvas {
	margin: 3;
	padding: 3;
	width:64%; 
	height:80%;
	float:right;
	border:3px solid #32872A
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
   <script>
   function init3(){
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
    	//alert(req);
    	if(req){
    		     req.open("GET",url, true); 
                 req.onreadystatechange = logic2; 
                 req.send(null); 
    	}
	}
	
	function logic2()
	{
		if(req.readyState == 4)
    	{
    		if(req.status==200){
    			var inf = eval(req.responseText);
    			//alert(inf);
    			showAtt(inf);
    		}
    	}
	}
	
   	function showAtt(objs) {
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
				var name = objs[i].webGeonameId +":"+orgName.substring(orgName.lastIndexOf(",")+2);
				tripName[i] = name;
			}
		}
        var mapOptions = {
          center: tripArray[0],
          zoom: 10,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        
        var map = new google.maps.Map(document.getElementById("map_canvas"),
            mapOptions);
		for(var i=0;i<tripArray.length;i++)
	{
		var markers = new google.maps.Marker({position:tripArray[i],});
		markers.setMap(map);
		var infowindow = new google.maps.InfoWindow({content:tripName[i]+":"+i});
		infowindow.open(map,markers);
	}
	//alert("test2");
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
			//  complete();
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
	
    
    function submitArea()
    {
    	//var req;
    	var url = "servlet/RuleAttServlet?area="+document.getElementById("area").value;
    	if(window.XMLHttpRequest){
    	//么有定义语句，反而可以在很多函数当中使用
    		req = new XMLHttpRequest();
    	}else if(window.ActiveXObject){
    		req = new ActiveXObject("Microsoft.XMLHTTP");
    	}
    	
    	if(req){
    //	alert("req2");
    		     req.open("GET",url, true); 
                 req.onreadystatechange = logic; 
                 req.send(null); 
    	}
    }
    function logic(){
    	if(req.readyState == 4)
    	{
    		if(req.status==200){
    		//alert("haha1");
    			var showArea = document.getElementById("attraction_area");
    			var inf = eval(req.responseText);
    			//顺利解决数据传输问题
    			showArea.innerHTML = inf[inf.length-1].table;
    			showMap(inf);
    		//alert("haha2");
    		}
    	}
    }
    function showMap(pointsInf)
    {
    	var points = new Array();
		var names = new Array();
		var maxShow = 10;
	 	if(pointsInf.length-1>maxShow)
		{
			maxShow = 10;
		}else
		{
			maxShow = pointsInf.length-1;
		}
			for (var i = 0; i < maxShow; i++) {
			points[i] = new google.maps.LatLng(pointsInf[i].latitude,pointsInf[i].longitude);
		 	var name = pointsInf[i].webGeonameContent;
			var lasIndex = name.lastIndexOf(",");
			names[i] = name.substring(lasIndex+2);  
		}
			var mapOptions = {
			center : points[0],
			zoom : 10,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};
		var map = new google.maps.Map(document.getElementById("map_canvas"),mapOptions);
	    var marker=new google.maps.Marker({position:points[0],});
   		marker.setMap(map);
		for (var j = 0; j < points.length; j++) {
			var markers = new google.maps.Marker({position:points[j],});
			markers.setMap(map);
			var infowindow = new google.maps.InfoWindow({content :names[j]+":"+j});
			infowindow.open(map, markers);
		} 
    }
   </script>

</head>
<body onload="init3()">

	<h1 align="center">关联规则景点查询</h1>
	<a href=HotAttractions.jsp>热门景点展示</a>
	<a href=TravelPlan.jsp>行程规划</a>
	<a href=RoutePlan.jsp>路线规划</a>
		已选择景点id：
	<span id=selectedAtt> 
		<%
			Set<Integer> selected = (Set<Integer>)session.getAttribute("selectedAtt");
			if(selected!=null && selected.size()>0)
			{
				for(Iterator<Integer> iterator = selected.iterator();iterator.hasNext();)
				{
					Integer attId = iterator.next();
					%>
				<span style="border:1px solid black;font-size:25px"><%=attId%>:
				<button style="font-size:10px;margin:2;padding:3" type="button" 
				onclick="addAttraction(this.value,'delete')" value=<%=attId%>>
			       delete</button></span>
		<%
				}
			}
		 %>
	</span>
	<button onclick="getAttFromServlet()" type="button">refresh</button>
	<br />
	<div id="out_form" style="width:100%; height:100%;center">
		<span id="attraction_list" >

			输入景点: <input id="area" type="text" value ="park" name="first_name"><br />

			<button type="button" onclick="submitArea()">查询:</button> <br /> 
			<div id="attraction_area">
</div>
		</span>

		<span id="map_canvas" ></span>
	</div>

</body>
</html>
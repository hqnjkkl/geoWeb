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
var tripArray;
var mapOptions;
var map ;
	function init() {
		getAttFromServlet();
	}
	//在地图上刷新已选择的景点
	function getAttFromServlet() {
		var url = "servlet/GetAttServlet";
		if (window.XMLHttpRequest) {
			//有定义语句，反而可以在很多函数当中使用
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (req) {
			req.open("GET", url, true);
			req.onreadystatechange = logic;
			req.send(null);
		}
	}

	function logic() {
		if (req.readyState == 4) {
			if (req.status == 200) {
				var inf = eval(req.responseText);
				showAtt(inf);
			}
		}
	}
	

	function showAtt(objs) {
		tripArray = new Array();
		var tripName = new Array();
		
		if (!objs || objs.length == 0) {
			tripArray[0] = new google.maps.LatLng(21.35269, -157.96962);
			tripName[0] = "Pearl Harbor";
		} else {
			for (var i = 0; i < objs.length; i++) {
				tripArray[i] = new google.maps.LatLng(objs[i].latitude,
						objs[i].longitude);
				var orgName = objs[i].webGeonameContent;
				var name = objs[i].webGeonameId + ":"
						+ orgName.substring(orgName.lastIndexOf(",") + 2);
				tripName[i] = name;
			}
		}
		mapOptions = {
			center : tripArray[0],
			zoom : 10,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		};

	map = new google.maps.Map(document.getElementById("map_canvas"),
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

	//最上方的景点进行添加删除
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
				var showText = "";
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

	function selectStartEnd() {
		var url = "servlet/GetAttServlet";
		if (window.XMLHttpRequest) {
			//么有定义语句，反而可以在很多函数当中使用
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}
		if (req) {
			req.open("GET", url, true);
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					var hotAtts = eval(req.responseText);
					var showArea = document.getElementById("attraction_area");
					showTable(showArea, hotAtts);
				}
			}
			req.send(null);
		}
	}

	function showTable(showArea, hotAtts) {
		//showArea.innerHTML = "<p>haha<p/>";
		//alert("showArea"); 
		//showArea.innerHTML = "<p style=\"color: red; margin-left: 20px\">haha<p/>";
		var table = "<table id=tableList width=\"90%\" height=\"60%\" border=1 align=\"left\" style=\"font-size:12px;\">";
		table += "<tr bgcolor=\"#949494\"><th>id</th><th>Attraction Name</th><th>Attraction occurence</th>"
				+ "<th>latitude</th>"
				+ "<th>longitude</th>"
				+ "<th>start or end</th></tr>";
		for (var i = 0; i < hotAtts.length; i++) {
			var name = hotAtts[i].webGeonameContent;
			var firIndex = name.indexOf(",");
			var aline = "<tr><td>"
					+ hotAtts[i].webGeonameId
					+ "</td>"
					+ "<td>"
					+ name.substring(firIndex + 1)
					+ "</td>"
					+ "<td>"
					+ hotAtts[i].occurence
					+ "</td>"
					+ "<td>"
					+ hotAtts[i].latitude
					+ "</td>"
					+ "<td>"
					+ hotAtts[i].longitude
					+ "</td>"
					+ "<td><button type=\"button\" onclick=\"changeStartEnd(\'start\',this.value)\" value=\""
					+ hotAtts[i].webGeonameId
					+ "\">start</button>"
					+ "<button type=\"button\" onclick=\"changeStartEnd(\'end\',this.value)\" value=\""
					+ hotAtts[i].webGeonameId + "\">end</button>"
					+ "</td></tr>";
			table += aline;
		}
		table += "</table>";
		showArea.innerHTML = table;
		//showArea.innerHTML
	}

	function changeStartEnd(op, value) {
		var rStart = document.getElementById("rStart");
		var rEnd = document.getElementById("rEnd");
		//alert("inner:"+rStart.innerHTML);
		var rs = rStart.innerHTML.lastIndexOf(":");
		var rd = rEnd.innerHTML.lastIndexOf(":");
		//alert("rs:"+rs);
		var oriStartId = rStart.innerHTML.substring(rs + 1);
		var oriEndId = rEnd.innerHTML.substring(rd + 1);
		//alert("oId:"+oriStartId);
		if (op == "start") {
			//alert("oriEndId:"+oriEndId);
			if (oriEndId != value) {
				//alert(op+":"+value);
				rStart.innerHTML = "start:" + value;
			}
		} else {
			if (oriStartId != value) {
				rEnd.innerHTML = "end:" + value;
			}
		}
	}

	//旅游路线规划
	function routePlan() {
		var rStart = document.getElementById("rStart");
		var rEnd = document.getElementById("rEnd");
		var rs = rStart.innerHTML.lastIndexOf(":");
		var rd = rEnd.innerHTML.lastIndexOf(":");
		var oriStartId = rStart.innerHTML.substring(rs + 1);
		var oriEndId = rEnd.innerHTML.substring(rd + 1);
		var url = "servlet/RoutePlanServlet?start=" + oriStartId + "&end="
				+ oriEndId + "&rTime=" + document.getElementById("rTime").value
				+ "&rCost=" + document.getElementById("rCost").value;
		if (window.XMLHttpRequest) {
			//么有定义语句，反而可以在很多函数当中使用
			req = new XMLHttpRequest();
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}

		if (req) {
			req.open("GET", url, true);
			req.onreadystatechange = function() {
				if (req.readyState == 4 && req.status == 200) {
					//var inf = eval(req.responseText);
					//alert(inf);
					var inf = eval(req.responseText);
					//alert(req.responseText);
					//alert("com:");
					//alert(inf);
					//数据到手，天下我有
					//alert(inf[inf.length - 1][0].id);
					//alert("inf0:"+inf[0].route[0].index);
					showRoute(inf);
				}
			};
			req.send(null);
		}
	}

	function showRoute(inf) {
		//alert(inf[0].lstNodes[0].latitude);
		//alert("inf0:" + inf[0].route[0].index);
		var tables ="";
		for (var i = 0; i < inf.length - 1; i++) {
			var routeAll = inf[i];
			var table = "<table id=\"route"
					+ (i + 1)
					+ "\" width=\"90%\" height=\"60%\" border=1 align=\"left\" style=\"font-size:12px;\">";
			table += "<tr bgcolor=\"#949494\"><th>route/Attname</th><th>routeLength/id</th><th>time</th><th>satis</th>"
					+ "<th>cost</th></tr>\n";
			table += "<tr><td>route" + (i + 1) + "</td><td>"
					+ routeAll.routeLength + "</td><td>" + routeAll.routeTime
					+ "</td><td>" + routeAll.routeSatisfaction
					+ "</td><td>" + routeAll.routeCost + "</td></tr>";
			var order = routeAll.order;
			
		 	for (var j = 0; j < order.length; j++) {
				var index = order[j];
				//alert(j+":orderj"+order[j]+";mappoint:"+inf[inf.length-1][index].id);
				table += "<tr><td>" + inf[inf.length - 1][index].name + "</td><td>"
						+ inf[inf.length - 1][index].id + "</td><td>"
						+ routeAll.route[j].stayTime + "</td><td>"
						+ routeAll.route[j].satisfaction + "</td><td>"
						+ routeAll.route[j].playCost + "</td></tr>";
				if(i==0)
				{
					if(j==0)
					{
					 var n1 = new google.maps.LatLng(inf[inf.length - 1][0].latitude,
								inf[inf.length - 1][0].longtitude);
					 var n2 = new google.maps.LatLng(inf[inf.length-1][index].latitude,
						inf[inf.length-1][index].longtitude);
						var p = [n1, n2];
						var fp = new google.maps.Polyline({
							path : p,
							strokeColor : "#FF0099",
							strokeOpacity : 0.8,
							strokeWeight : 2
						});
						fp.setMap(map);
					}
					var node1,node2;
					if(j==(order.length-1))
					{
					 node1 = new google.maps.LatLng(inf[inf.length - 1][index].latitude,
								inf[inf.length - 1][index].longtitude);
					 var size = inf[inf.length-1].length;
					 node2 = new google.maps.LatLng(inf[inf.length-1][size-1].latitude,
						inf[inf.length-1][size-1].longtitude);
					}else
					{
						node1 = new google.maps.LatLng(inf[inf.length-1][index].latitude,
							inf[inf.length-1][index].longtitude);
					 	node2 = new google.maps.LatLng(inf[inf.length - 1][order[j+1]].latitude,
								inf[inf.length - 1][order[j+1]].longtitude);
					}
						var pp = [node1, node2];
						var fp = new google.maps.Polyline({
							path : pp,
							strokeColor : "#FF0099",
							strokeOpacity : 0.8,
							strokeWeight : 2
						});
						fp.setMap(map);
				}
			}
			table += "</table>";
			tables+=table;
			
		}
		//alert("table:"+tables);
		document.getElementById("attraction_area").innerHTML = tables;
		/* if (inf.length > 0) {
			//alert(inf);
			//alert(inf[0].lstNodes);
			var lsn = eval(inf[0].lstNodes);
			//alert(lsn);
			var center = new google.maps.LatLng(inf[0].lstNodes[0].latitude,
					inf[0].lstNodes[0].longtitude);
			var mapOptions = {
				center : center,
				zoom : 10,
				mapTypeId : google.maps.MapTypeId.ROADMAP
			};
			var map = new google.maps.Map(
					document.getElementById("map_canvas"), mapOptions);
			//alert("from:"+inf[0][0].from.latitude);
			var table = "<table id=tableList width=\"90%\" height=\"60%\" border=1 align=\"left\" style=\"font-size:12px;\">";
			table += "<tr bgcolor=\"#949494\"><th>Attraction Name</th><th>Attraction occurence</th>"
					+ "<th>latitude</th>"
					+ "<th>longitude</th>"
					+ "<th>attId</th></tr>";
			for (var i = 0; i < inf.length; i++) {
				table += "<tr><td>第" + (i + 1) + "天</td></tr>";
				for (var j = 0; j < inf[i].lstEdges.length; j++) {
					var edge = inf[i].lstEdges[j];
					//alert(i+":"+j+"two:"+edge.from.latitude+":"+edge.from.longtitude+";"+edge.to.latitude+":"+edge.to.longtitude);
					var node1 = new google.maps.LatLng(edge.from.latitude,
							edge.from.longtitude);
					var node2 = new google.maps.LatLng(edge.to.latitude,
							edge.to.longtitude);
					var pp = [ node1, node2 ];
					var fp = new google.maps.Polyline({
						path : pp,
						strokeColor : "#FF0099",
						strokeOpacity : 0.8,
						strokeWeight : 2
					});
					fp.setMap(map);
					//alert("fp"+fp);
				}
				lines[i] = new Array();
				for (var j = 0; j < inf[i].lstNodes.length; j++) {
					table += "<tr><td>" + inf[i].lstNodes[j].name + "</td>"
							+ "<td>" + inf[i].lstNodes[j].occurence + "</td>"
							+ "<td>" + inf[i].lstNodes[j].latitude + "</td>"
							+ "<td>" + inf[i].lstNodes[j].longtitude + "</td>"
							+ "<td>" + inf[i].lstNodes[j].id + "</td></tr>";
					//alert(i+":"+j+":lines:"+lines[i][j]);
					lines[i][j] = new google.maps.LatLng(
							inf[i].lstNodes[j].latitude,
							inf[i].lstNodes[j].longtitude);
					var markers = new google.maps.Marker({
						position : lines[i][j],
					});
					var orgName = inf[i].lstNodes[j].name;
					var name = inf[i].lstNodes[j].id + ":"
							+ orgName.substring(orgName.lastIndexOf(",") + 2);
					markers.setMap(map);
					var infowindow = new google.maps.InfoWindow({
						content : name
					});
					infowindow.open(map, markers);
				}
			}
			table += "</table>";
			document.getElementById("attraction_area").innerHTML = table;
			// alert(lines[i][j]);
			// alert(lines[0][0]);
		} */
	}
</script>
</head>
<body onload="init3()">
	<h1 align="center">路线规划</h1>
	<a href=HotAttractions.jsp>热门景点展示</a>
	<a href=RuleAttractions.jsp>关联规则景点查询</a>
	<a href=TravelPlan.jsp>行程规划</a> 已选择景点id：
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
		<span id="attraction_list"> 时长(min): <input id="rTime"
			type="text" size="5" value="180" name="first_name">费用(dollar):
			<input id="rCost" type="text" value="30" size="5"><span
			id="rStart">start:<%=request.getParameter("start")%></span> <span
			id="rEnd">end:<%=request.getParameter("end")%></span> <br />
			<button type="button" onclick="selectStartEnd()">选择起始点:</button>
			<button type="button" onclick="routePlan()">路线规划:</button> <br />
			<div id="attraction_area"></div>
		</span> <span id="map_canvas"></span>
	</div>

</body>
</html>
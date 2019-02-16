package com.geoImage.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.geoImage.dao.WebGeoname;
import com.geoImage.dao.WebGeonameDAO;
import com.geoImage.logic.MapPoint;

/**
 * 对应处理RuleAttraction的响应，以及调用RuleAttLogic来完成业务逻辑
 * 
 * @author huqiaonan
 * @version 2.0,2015年4月19日 下午3:50:20
 */
public class TravelPlanServlet extends HttpServlet {
	public TravelPlanServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Set<Integer> obj = (Set<Integer>) session.getAttribute("selectedAtt");
		Integer days = Integer.parseInt(request.getParameter("day"));
		Integer length = Integer.parseInt(request.getParameter("len"));
		List<Graph> lstGraphs = null;
		PrintWriter out = response.getWriter();
		List<MapPoint> mps = new ArrayList<MapPoint>();
		TravelPlan tp = new TravelPlan();
		if (obj == null) {
			Set<Integer> selected = new HashSet<Integer>();
			session.setAttribute("selectedAtt", selected);
			obj = selected;
		} else {
			WebGeonameDAO wgd = new WebGeonameDAO();
			for (Iterator<Integer> iterator = obj.iterator(); iterator
					.hasNext();) {
				Integer id = iterator.next();
				WebGeoname wg = wgd.findById(id);
				MapPoint mp = new MapPoint(wg.getWebGeonameId(),
						wg.getLatitude(), wg.getLongitude(),
						wg.getWebGeonameContent(), wg.getOccurence());
				mps.add(mp);
			}
		}
		Graph graph = new Graph(mps,new ArrayList<Edge>());
		for (int i = 0; i < graph.lstNodes.size(); i++) {
			MapPoint from = graph.lstNodes.get(i);
			for (int j = i + 1; j < graph.lstNodes.size(); j++) {
				MapPoint to = graph.lstNodes.get(j);
				int w = attDistance(from,to);
				graph.lstEdges.add(new Edge(from, to, w));
			}
		}
		if(days<=mps.size())
		{
			if(days!=0)
			{
				lstGraphs = tp.MSTC_LimitCount(graph, days);
			}else
			{
				lstGraphs = tp.MSTC_LimitWeight(graph, length);
			}
		}else{
			lstGraphs = new ArrayList<Graph>();
		}
		JSONArray jaa = new JSONArray();
		System.out.println("subGraph:"+lstGraphs.size());
		
		for(int i=0;i<lstGraphs.size();i++)
		{
			JSONObject ja = new JSONObject();
			ja.put("lstNodes",JSONArray.fromObject(lstGraphs.get(i).lstNodes));
			JSONArray ja2 = new JSONArray();
			//这个语法错误，害得我好惨
			for(int j=0;j<lstGraphs.get(i).lstEdges.size();j++)
			{
				JSONObject from = JSONObject.fromObject(lstGraphs.get(i).lstEdges.get(j).from);
				JSONObject to = JSONObject.fromObject(lstGraphs.get(i).lstEdges.get(j).to);
				JSONObject jobj = new JSONObject();
				jobj.put("from", from);
				jobj.put("to", to);
				
				ja2.add(jobj);
			}
			ja.put("lstEdges", ja2);
			jaa.add(ja);
		}
		out.write(jaa.toString());
		
		// WebGeonameDAO wgd = new WebGeonameDAO();
		// WebGeoname wg = wgd.findById(attId);
		// System.out.println(obj);
		// System.out.println(wg);
		// 这个是输出的部分
		// out.write(joba.toString());
		out.close();
	}
	private int attDistance(MapPoint ms1, MapPoint ms2) {
		return (int) (geo_distance(ms1.getLatitude(), ms1.getLongtitude(),
				ms2.getLatitude(), ms2.getLongtitude()) * 1000);
	}

	/**
	 * 输入两个点的经纬度，返回两个点的距离，以KM为单位
	 * 
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return
	 */
	public double geo_distance(double lat1, double lng1, double lat2,
			double lng2) {
		// earth's mean radius in KM
		double r = 6378.137;
		// 转化为弧度再计算
		lat1 = Math.toRadians(lat1);
		lng1 = Math.toRadians(lng1);
		lat2 = Math.toRadians(lat2);
		lng2 = Math.toRadians(lng2);
		double d1 = Math.abs(lat1 - lat2);
		double d2 = Math.abs(lng1 - lng2);
		double p = Math.pow(Math.sin(d1 / 2), 2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.pow(Math.sin(d2 / 2), 2);
		double dis = r * 2 * Math.asin(Math.sqrt(p));
		return dis;
	}
}

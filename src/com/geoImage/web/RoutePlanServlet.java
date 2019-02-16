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
import com.geoImage.logic.Attraction;
import com.geoImage.logic.MapPoint;
import com.geoImage.logic.MyMap;
import com.geoImage.logic.Route;
import com.geoImage.logic.RoutePlan;

/**
 * 对应处理RuleAttraction的响应，以及调用RuleAttLogic来完成业务逻辑
 * 
 * @author huqiaonan
 * @version 2.0,2015年4月19日 下午3:50:20
 */
public class RoutePlanServlet extends HttpServlet {
	public RoutePlanServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}
	
	/**
	 * 获得相应的数据，让RoutePlan来完成路线规划结果，展示路线规划结果。
	 * 以对象形式，返回数据给前台，让前台来画表格和地图。
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Set<Integer> obj = (Set<Integer>) session.getAttribute("selectedAtt");
		// rTime,rSatisfaction,rCost
		
		Integer rTime = Integer.parseInt(request.getParameter("rTime"));
		Integer rCost = Integer.parseInt(request.getParameter("rCost"));
		Integer startId = Integer.parseInt(request.getParameter("start"));
		Integer endId = Integer.parseInt(request.getParameter("end"));
		
		System.out.println("rTime:"+rTime+";rCost:"+rCost+";start:"+startId+";end:"+endId);
		List<Graph> lstGraphs = null;
		
		PrintWriter out = response.getWriter();
		List<MapPoint> mps = new ArrayList<MapPoint>();
		
		
		if (obj == null) {
			Set<Integer> selected = new HashSet<Integer>();
			session.setAttribute("selectedAtt", selected);
			obj = selected;
		} else {
			WebGeonameDAO wgd = new WebGeonameDAO();
			WebGeoname wg = wgd.findById(startId);
			MapPoint mp = new MapPoint(wg.getWebGeonameId(),
					wg.getLatitude(), wg.getLongitude(),
					wg.getWebGeonameContent(), wg.getOccurence());
			mps.add(mp);
			for (Iterator<Integer> iterator = obj.iterator(); iterator
					.hasNext();) {
				Integer id = iterator.next();
				
				
				if(!id.equals(startId) && !id.equals(endId))
				{		
					wg = wgd.findById(id);
					mp = new MapPoint(wg.getWebGeonameId(),
							wg.getLatitude(), wg.getLongitude(),
							wg.getWebGeonameContent(), wg.getOccurence());
					mps.add(mp);
				}
			}
			wg = wgd.findById(endId);
			mp = new MapPoint(wg.getWebGeonameId(),
					wg.getLatitude(), wg.getLongitude(),
					wg.getWebGeonameContent(), wg.getOccurence());
			mps.add(mp);
		}
		
		MyMap mm = new MyMap();
		mm.buildData(mps.size(), mps);
		RoutePlan rp = new RoutePlan(mm, rTime, rCost);
		//route当中，order的index顺序和route的index顺序相同
		List<Route> routes = rp.testGuide();
		JSONArray jsa = transformData(routes);
		jsa.add(JSONArray.fromObject(mps));
		//out.write(joba.toString());
		//System.out.println(jo.toString());
		out.write(jsa.toString());
		//System.out.println(mps);
		//System.out.println(jsa.toString());
		out.close();
	}
	
	public JSONArray transformData(List<Route> res)
	{
		JSONArray jArray = new JSONArray();
		for(int i=0;i<res.size();i++)
		{
			Route r = res.get(i);
			JSONObject jo = new JSONObject();
			jo.put("route", JSONArray.fromObject(r.getRoute()));
			jo.put("order", JSONArray.fromObject(r.getOrder()));
			jo.put("routeLength", r.getRouteLength());
			jo.put("routeTime", r.getRouteTime());
			jo.put("routeSatisfaction", r.getRouteSatisfaction());
			jo.put("routeCost", r.getRouteCost());
			jArray.add(jo);
		}
		return jArray;
	}
	/*
	 * List<Attraction> route = new ArrayList<Attraction>();
	List<Integer> order = new ArrayList<Integer>();
	int routeLength ;
	int routeTime;
	int routeSatisfaction;
	int routeCost;
	 * 
	 */
}

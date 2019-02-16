package com.geoImage.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import com.geoImage.logic.RuleAttLogic;

/**
 * 对应处理RuleAttraction的响应，以及调用RuleAttLogic来完成业务逻辑
 * 
 * @author huqiaonan
 * @version 2.0,2015年4月19日 下午3:50:20
 */
public class GetAttServlet extends HttpServlet {
	public GetAttServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	/**
	 * 获得session对象当中，景点id列表的值
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		Set<Integer> obj = (Set<Integer>) session.getAttribute("selectedAtt");
		PrintWriter out = response.getWriter();
		List<WebGeoname> atts = new ArrayList<WebGeoname>();
		if (obj == null) {
			Set<Integer> selected = new HashSet<Integer>();
			session.setAttribute("selectedAtt", selected);
			obj = selected;
		}else
		{
			 WebGeonameDAO wgd = new WebGeonameDAO();
			 for (Iterator<Integer> iterator = obj.iterator(); iterator.hasNext();) {
				Integer id = iterator.next();
				atts.add(wgd.findById(id));
			}
		}
		System.out.println("selected:" + obj.size() + ":" + obj);
		if(atts.size()>0)
		{			
			JSONArray jsonArray = JSONArray.fromObject(atts);
			out.write(jsonArray.toString());
		}
		// WebGeoname wg = wgd.findById(attId);
		// System.out.println(obj);
		// System.out.println(wg);
		// 这个是输出的部分
		// out.write(joba.toString());
		out.close();
	}

}

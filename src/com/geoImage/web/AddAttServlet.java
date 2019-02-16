package com.geoImage.web;

import java.io.IOException;
import java.io.PrintWriter;
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
public class AddAttServlet extends HttpServlet {
	public AddAttServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}
	
	/**
	 * 对列表上的景点id进行添加或者删除
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer attId = Integer.parseInt(request.getParameter("attId"));
		HttpSession session = request.getSession();
		Set<Integer> obj = (Set<Integer>) session.getAttribute("selectedAtt");
		String op = request.getParameter("op");
		PrintWriter out = response.getWriter();
		System.out.println("op:"+";attId:"+attId);
		if(op!=null && op.equals("add"))
		{
			if(obj==null)
			{
				Set<Integer> selected = new HashSet<Integer>();
				session.setAttribute("selectedAtt",selected);
				obj = selected;
			}
			obj.add(attId);
			System.out.println("selected:"+obj.size()+":"+obj);
			JSONArray jsonArray = JSONArray.fromObject(obj.toArray());
			System.out.println(jsonArray.toString());
			out.write(jsonArray.toString());
		}else
		{
			if(obj!=null && attId!=null)
			{
				obj.remove(attId);
				System.out.println("selected:"+obj.size()+":"+obj);
				JSONArray jsonArray = JSONArray.fromObject(obj.toArray());
				System.out.println(jsonArray.toString());
				out.write(jsonArray.toString());
			}
		}
	
//		WebGeonameDAO wgd = new WebGeonameDAO();
//		WebGeoname wg = wgd.findById(attId);
//		System.out.println(obj);
		//System.out.println(wg);
		// 这个是输出的部分
		// out.write(joba.toString());
		out.close();
	}

}

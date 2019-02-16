package com.geoImage.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.geoImage.dao.WebGeoname;
import com.geoImage.logic.HotAttLogic;

public class HotAttServlet extends HttpServlet {
	public HotAttServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String area = request.getParameter("area");
		HotAttLogic hotAtt = new HotAttLogic();
		List<WebGeoname> hotAtts = hotAtt.getHotAtt(area, 20);
		//列出20个点
		//这一句话，就可以搞定对象的转换
		JSONArray joba = JSONArray.fromObject(hotAtts);
		PrintWriter out = response.getWriter();
		//System.out.println(jsonArray.toString());
		// 这个是输出的部分
		out.write(joba.toString());
		out.close();
	}

}

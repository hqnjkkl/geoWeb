package com.geoImage.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONString;
import net.sf.json.util.JSONStringer;

import com.geoImage.dao.WebGeoname;
import com.geoImage.logic.HotAttLogic;
import com.geoImage.logic.RuleAttLogic;

/**
 * 对应处理RuleAttraction的响应，以及调用RuleAttLogic来完成业务逻辑
 * 
 * @author huqiaonan
 * @version 2.0,2015年4月19日 下午3:50:20
 */
public class RuleAttServlet extends HttpServlet {
	public RuleAttServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String area = request.getParameter("area");
		RuleAttLogic ruleAtt = new RuleAttLogic();
		Map<List<WebGeoname>, Integer> rules = ruleAtt.getRuleAttractions(area,
				10);
		System.out.println("rule size:"+rules.size());
		StringBuilder sb = new StringBuilder();
		sb.append("<table width=\"100\" border=1 align=\"left\">");
		sb.append("<tr bgcolor=\"#949494\"><th>ruleAttractions</th><th>rule occurence</th>");
		sb.append("</tr>");
		
		for (Iterator<List<WebGeoname>> iterator = rules.keySet().iterator(); iterator
				.hasNext();) {
			List<WebGeoname> rule = (List<WebGeoname>) iterator.next();
			StringBuilder ruleString = new StringBuilder();
			Integer occurence = rules.get(rule);
			for (int i = 0; i < rule.size(); i++) {
				//System.out.println(rule);
				String name = rule.get(i).getWebGeonameContent();
				if (i == 0) {
					ruleString
							.append(name.substring(name.lastIndexOf(",")+2));
				} else {
					ruleString.append(","
							+ name.substring(name.lastIndexOf(",")+2));
				}
			}
			System.out.println(ruleString.toString());
			sb.append("<tr><td>" + ruleString.toString() + "</td><td>"
					+ occurence + "</td></tr>");
		}
		sb.append("</table>");
		List<WebGeoname> wgs = ruleAtt.getDistinctName();
	System.out.println("wgs:"+wgs.size());
		sb.append("<table width=\"100\" border=1 align=\"left\" style=\"font-size:12px;\">");
		sb.append("<tr bgcolor=\"#949494\"><th>Attraction Name</th><th>Attraction occurence</th>"+
				 "<th>latitude</th>"+
		 		 "<th>longitude</th>"+
		 		 "<th>select</th></tr>");
		for(int i=0;i<wgs.size();i++)
		{
			String name = wgs.get(i).getWebGeonameContent();
	 		Integer firIndex = name.indexOf(",");
	 	    String aline = "<tr><td>" + name.substring(firIndex+1) + "</td>"+
	 	    	"<td>"+wgs.get(i).getOccurence()+"</td>"+
	 	    	"<td>"+wgs.get(i).getLatitude()+"</td>"+
	 	    	"<td>"+wgs.get(i).getLongitude()+"</td>"+
	 	    	"<td><button type=\"button\" onclick=\"addAttraction(this.value,\'add\')\" value=\""+wgs.get(i).getWebGeonameId()+
	 	    	"\">add</button></td></tr>";
	 	    sb.append(aline);
		}
		sb.append("</table>");
		JSONArray jsonArray = JSONArray.fromObject(wgs);
		JSONObject jo = new JSONObject();
		jo.put("table", sb.toString());
		
		jsonArray.add(jo);
		//System.out.println(sb);
		// 列出20个点
		// 这一句话，就可以搞定对象的转换
		// JSONArray joba = JSONArray.fromObject(hotAtts);
		PrintWriter out = response.getWriter();
		// 这个是输出的部分
		// out.write(joba.toString());
		out.write(jsonArray.toString());
		out.close();
	}

}

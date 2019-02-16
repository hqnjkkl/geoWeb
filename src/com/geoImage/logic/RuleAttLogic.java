package com.geoImage.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.WebGeoname;
import com.geoImage.dao.WebGeonameDAO;

/**
 * 处理有关关联规则算法的逻辑,使用hqngeo工程当中com.geoImage.frequentItem.InsertNewRule的处理结果
 * 
 * @author huqiaonan
 * @version 2.0,2015年4月19日 上午11:18:31
 */
public class RuleAttLogic {
	List<WebGeoname> distinctName = new ArrayList<WebGeoname>();
	
	
	public List<WebGeoname> getDistinctName() {
		return distinctName;
	}

	public static void main(String[] args) {
		RuleAttLogic ral = new RuleAttLogic();
		//ral.getRuleAttractions("Test");
		ral.getRuleAttractions("", 10);
	}

	public Map<List<WebGeoname>, Integer>  getRuleAttractions(String attraction,int ruleNumber) {
		Map<List<WebGeoname>, Integer> conOccurence = new HashMap<List<WebGeoname>, Integer>();
		WebGeonameDAO wgd = new WebGeonameDAO();
		//System.out.println(wgd.findById(100));
		// 获得这个景点是否存在，并且其ID是多少
		String sql = "select webGeonameId, webGeonameContent from WebGeoname wg where wg.webGeonameContent like '%"
				+ attraction + "%'";
		Session session = HibernateSessionFactory.getSession();
		Query query = session.createQuery(sql);
		List<Object[]> webGeoname = query.list();
		Integer hitId = null;
		System.out.println("name Size:"+webGeoname.size());
		//System.out.println(webGeoname);
		List<Object[]> wraList = null;
		if (webGeoname.size() > 0) {
			for (int i = 0; i < webGeoname.size(); i++) {
				String name = (String) webGeoname.get(i)[1];
				int lastIndex = name.lastIndexOf(",");
				String lastName = name.substring(lastIndex + 2);
				//if (lastName.toLowerCase().contains(attraction.toLowerCase())) {
					hitId = (Integer) webGeoname.get(i)[0];
					
					//break;
				//}
					// 把所有关联的景点都查找出来
					String wraSql = "select WebRuleAttRules,Occurence,ruleSize from WebRuleAtt wra where wra.WebRuleAttRules like '%,"
							+ hitId + ",%' order by ruleSize Desc";
					Query wraQuery = session.createSQLQuery(wraSql);
					wraList = wraQuery.list();
					//System.out.println("rule Size1:"+wraList.size()+":"+wraList);
					if(wraList.size()>0)
					{
						break;
					}
			}
			
			if(wraList==null || wraList.size()==0)
			{
				return conOccurence;
			}
			// 从景点当中找出和这个景点关联的，并把其中单独的单词取出来，用来加速
			// Map<String,Integer> idToOccurence = new HashMap<String,
			// Integer>();
			if(ruleNumber>wraList.size())
			{
				ruleNumber = wraList.size();
			}
			Set<String> distinctId = new HashSet<String>();
			for (int i = 0; i < ruleNumber; i++) {
				String rule = (String) wraList.get(i)[0];
				String[] ids = rule.split(",");
				//System.out.println(ids.length);
				for (int j = 1; j < ids.length; j++) {
					distinctId.add(ids[j]);
				}
				System.out.println(rule+"--");
			}
			//System.out.println(distinctId);
			//存储后进行加速
			Map<Integer,WebGeoname> fasterMap = new HashMap<Integer, WebGeoname>();
			for (Iterator<String> iterator = distinctId.iterator(); iterator.hasNext();) {
				String id = (String) iterator.next();
				Integer webGeonameId = Integer.parseInt(id);
				WebGeoname wg = wgd.findById(webGeonameId);
				distinctName.add(wg);
				fasterMap.put(webGeonameId, wg);
			}
			for(int i=0;i<ruleNumber;i++)
			{
				String rule = (String) wraList.get(i)[0];
				String[] ids = rule.split(",");
				List<WebGeoname> names = new ArrayList<WebGeoname>();
				for(int j=1;j<ids.length;j++)
				{					
					names.add(fasterMap.get(Integer.parseInt(ids[j])));
				}
				conOccurence.put(names, (Integer)wraList.get(i)[1]);
			}
		}else
		{
			System.out.println("hitId:"+hitId);
		}
		return conOccurence;
	}
}

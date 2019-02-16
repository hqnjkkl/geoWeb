package com.geoImage.logic;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.geoImage.dao.HibernateSessionFactory;
import com.geoImage.dao.WebGeoname;

/**
 * 这个类，处理有关HotAttraction的逻辑，
 * @author huqiaonan
 * @version 2.0,2015年4月19日 上午11:17:26
 */
public class HotAttLogic {
	
	public static void main(String[] args) {
		new HotAttLogic().getHotAtt("Hawaii County");
	}
	/**
	 * 根据一定大小来获得纪录的多少
	 * @param area
	 * @param number
	 * @return
	 */
	public List<WebGeoname> getHotAtt(String area,int number)
	{
		List<WebGeoname> hotAtt = getHotAtt(area);
		if(hotAtt.size()>number)
		{
			return hotAtt.subList(0, number);
		}else
		{
			return hotAtt;
		}
	}
	
	/**
	 * 把WebGeoname表当中的数据提取出来，并根据字符串进行筛选，然后按照热度从大到小排列
	 * @param area
	 * @return
	 */
	public List<WebGeoname> getHotAtt(String area)
	{
		List<WebGeoname> hotAtt= null;
		String hql = "from WebGeoname wg where wg.webGeonameContent like '%"+area+"%' "
				+ "order by wg.occurence desc";
		//String hql = "from WebGeoname wg where wg.webGeonameContent like '%"+area+"%'";
			//	+ "order by wg.occurence desc";
		Session session = HibernateSessionFactory.getSession();
		Query query = session.createQuery(hql);
		hotAtt = query.list();
		System.out.println("hotAtt query size:"+hotAtt.size());
		//System.out.println("two times");
//		JSONArray job = JSONArray.fromObject(hotAtt);
//		System.out.println(job);
		
		return hotAtt;
	}
}

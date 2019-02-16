package com.geoImage.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这是一个图的测试数据，这个图的数据用来初步实现demo。 正常的测试，数据估计包含在其中。
 * 
 * @author huqiaonan
 * 
 */
public class MyMap {
	List<Attraction> data = new ArrayList<Attraction>();
	int map[][];
	Map<Integer, Attraction> idToAtt = new HashMap<Integer, Attraction>();

	public MyMap() {
		// initial();
	}

	int costList[][] = { { 0, 10, 20, 20, 30 }, { 0, 10, 20, 20, 50 },
			{ 0, 10, 20, 20, 50 } };
	int timeList[][] = { { 30, 30, 50, 60, 60 },
						{ 30, 40, 50, 60, 60 },
						{ 30, 40, 50, 60, 60 },
						{ 30, 40, 50, 60, 100 },
						{ 30, 50, 50, 60, 100 } };

	public static void main(String[] args) {
		
	}
	/**
	 * 根据给的路径长度，获得需要旅行的时间。这是一个分段函数,x是距离单位为m，y是时间，
	 * 单位为分钟。
	 * @param len
	 * @return
	 */
	public int calculateTimeByLen(int len)
	{
		//x = 0-1000m,y = 5minutes;
		double a = 0,b = 0;
		if(len>=0&&len<=1000)
		{
			return 5;
		}else if(1000<len&&len<=10000)
		{
			//x = 1000-10000;y=5-25minutes
			//y = ax+b;
			a = 20.0/9000;
			b = 25.0-a*10000;
			return (int)(a*len+b);
		}else if(len>10000&&len<=60000)
		{
			//(10000,25),(60000,60)
			a = 35.0/50000;
			b = 60-60000*a;
			return (int)(a*len+b);
		}else if(len>60000){
			return len/1000;
		}else{
			System.out.println("data error!");
			return 0;
		}
	}
	/**
	 * 根据数据来建立运行所需的图，包括路程行驶时间，满意度，时间花费，费用花费。
	 * 
	 * @param size
	 * @param datas
	 *            存储有经纬度，出现次数，以及本身id的d点数据，按照出现的次数由大到小排序。这个
	 *            列表当中存储的是原始数据，由这个原始数据
	 */
	public void buildData(int size, List<MapPoint> datas) {
		int maxLen = 0, sIndex = size-1, eIndex = 0;
		map = new int[size][size];
		boolean change = true;
		//计算距离
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size;j++) {
//				int ii = i*datas.size()/size;
//				int jj = j*datas.size()/size;
				double org = geo_distance(datas.get(i).getLatitude(), datas
						.get(i).getLongtitude(), datas.get(j).getLatitude(),
						datas.get(j).getLongtitude());

				int len = calculateTimeByLen((int)(1000 * org));
				int time = len;
//				time = len / 500 + 2 * (len % 2000);
				map[i][j] = time;
				map[j][i] = time;
				if(maxLen<len){
//					sIndex = i;
//					eIndex = j;
					maxLen = len;
				}
//				if(time>40&&time<50&&change)
//				{
//					sIndex = i;
//					eIndex = j;
//					change = false;
//				}
			}
		}

		// 获取所有的Attraction点
		for (int i = 0; i < size; i++) {
			MapPoint mp = datas.get(i);
			int stayTime = timeList[(int) (Math.random() * 5)][(int) (Math
					.random() * 5)];
			int playCost = costList[(int) (Math.random() * 3)][(int) (Math
					.random() * 5)];
			Attraction att = new Attraction(mp.getOccurence(), stayTime,
					playCost);
			att.setIndex(i);
			att.id = mp.getId();
			data.add(att);
			idToAtt.put(i, att);
			// System.out.println(att);
		}
		
//		if (!(sIndex == 0 && eIndex == (size - 1))) {
//			System.out.println("maxLen:"+maxLen);
//			swapData(0, sIndex, size);
//			swapData(eIndex, size - 1, size);
//		}
		//数据弄好之后，在这里写入数据到文本当中
	}

	/**
	 * 交换数据的两行和两列，
	 * 
	 * @param s1
	 * @param s2
	 * @param size
	 */
	public void swapData(int s1, int s2, int size) {
		if(s1==s2)
			return ;
		for (int i = 0; i < size; i++) {
			int tmp = map[i][s1];
			map[i][s1] = map[i][s2];
			map[i][s2] = tmp;
		}
		for (int i = 0; i < size; i++) {
			int tmp = map[s1][i];
			map[s1][i] = map[s2][i];
			map[s2][i] = tmp;
		}
		Attraction att1 = idToAtt.get(s1);
		Attraction att2 = idToAtt.get(s2);
		att1.setIndex(s2);
		idToAtt.put(s2, att1);
		att2.setIndex(s1);
		idToAtt.put(s1, att2);
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

	/**
	 * 初始化景点和景点之间的连线,简单的测试数据
	 */
	public void initial() {
		// 起点和终点
		data.add(new Attraction(0, 0, 0, 0));
		data.add(new Attraction(1, 5, 20, 10));
		data.add(new Attraction(2, 7, 30, 6));
		data.add(new Attraction(3, 12, 50, 4));
		data.add(new Attraction(4, 10, 30, 13));
		data.add(new Attraction(5, 0, 0, 0));
		for (int i = 0; i < data.size(); i++) {
			data.get(i).setId(i);
			idToAtt.put(i, data.get(i));
		}
		// 6*6的矩阵，来计算每行每列的数据
		map = new int[][] { { 0, 30, 30, 65, 10, 60 },
				{ 30, 0, 30, 70, 35, 75 }, { 30, 30, 0, 45, 50, 55 },
				{ 65, 70, 45, 0, 60, 30 }, { 10, 35, 50, 60, 0, 50 },
				{ 60, 75, 55, 30, 50, 0 } };
	

	}
}
	


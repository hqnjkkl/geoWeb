/**
 * routePlan这个包是用来存放路线推荐，路线规划的算法
 */
package com.geoImage.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 进行旅游路线规划，核心算法在这个类当中，输入是一张图，输出是最优解的路线。
 * 
 * @author huqiaonan
 * 
 */
public class RoutePlan {

	MyMap mm = null;
	OptimalMechanism om =null;

	//存储一个无效子集序列，只存储其id序列，并不存储相应的时间，费用满意度
	Set<Set<Integer>> invalidSet = new HashSet<Set<Integer>>();
	HashSet<Integer> is = new HashSet<Integer>();
	HashMap<Integer,Integer> ism = new HashMap<Integer, Integer>();
	
	int timeRes = 200;
	int costRes = 80;
	//存储一次运行程序所需的时间，单位为毫秒
	long time;
	public RoutePlan(){}
	public RoutePlan(MyMap myMap)
	{
		this.mm = myMap;
		om = new OptimalMechanism(mm);
	}
	
	
	public RoutePlan(MyMap mm, int timeRes, int costRes) {
		this.mm = mm;
		this.timeRes = timeRes;
		this.costRes = costRes;
		om = new OptimalMechanism(mm);
	}
	public static void main(String[] args) {
		// testSkyline();
		RoutePlan rp = new RoutePlan();
		// rp.testSkyline();
//		rp.testGuide(); 
//		rp.testInit();
	}
	
	public void testInit()
	{
		MyMap mm = new MyMap();
		mm.initial();
		this.timeRes = 200;
		this.costRes = 20;
		this.mm = mm;
		this.om = new OptimalMechanism(mm);
		testGuide();
	}

	/**
	 * 用来测试本文产生的算法
	 */
	public List<Route> testGuide() {
		long start = System.currentTimeMillis();
		om.attractionSort(mm.data);
		List<Route> res = mutipleGuide(timeRes, costRes);
		long end = System.currentTimeMillis();
		// System.out.println("s:"+start+",e:"+end+","+(end-start));
		System.out.println("time:"+(end - start));
		this.time = end-start;
		res = skylineFilter(res);
		for (int i = 0; i < res.size(); i++) {
			System.out.println("skyline res:"+i);
			showRoute(res.get(i), true);
		}
		return res;
	}
	
	public void testBruteForce()
	{
		long start = System.currentTimeMillis();
		
		List<Route> res = mutipleGuide(timeRes, costRes);
		
		long end = System.currentTimeMillis();
	}
	
	public void testSkyline() {
		List<Route> res = new ArrayList<Route>();
		int testData[][] = { { 20, 30, 39, 40, 50, 60, 70, 70 },
				{ 60, 50, 55, 40, 30, 70, 20, 50 } };
		for (int i = 0; i < 8; i++) {
			System.out.println(testData[0][i] + "," + testData[1][i]);
			Route r = new Route(testData[0][i], testData[1][i]);
			res.add(r);
		}
		res = skylineFilter(res);
		for (int i = 0; i < res.size(); i++) {
			System.out.println("skyline res:"+i);
			showRoute(res.get(i), true);
		}
	}
	
	/**
	 * 把无效的候选景点集合的排列插入到无效候选集合当中
	 * @param route
	 */
	public void addinvalidSet(Route route)
	{
		Set<Integer> invalid= new HashSet<Integer>();
		List<Attraction> arr = route.getRoute();
		for(int i=0;i<arr.size();i++)
		{
			invalid.add(arr.get(i).getIndex());
		}
		invalidSet.add(invalid);
	}
	/**
	 * 输入时间限制，费用限制
	 * 
	 * @param timeRestriction
	 * @param costRestriction
	 * @return
	 */
	public List<Route> mutipleGuide(int timeRestriction, int costRestriction) {
		List<Route> res = new ArrayList<Route>();

		// 这里，table当中存储的是candidate set
		List<Route> table = new ArrayList<Route>();
		// 获得第1阶候选集合
		for (int i = 1; i < mm.data.size() - 1; i++) {
			Route route = new Route();
			route.route.add(mm.data.get(i));
			setAttributes(route);
			if (isValidSet(route, timeRestriction, costRestriction)) {
				// System.out.println(route.route.get(0).getIndex()+"\t"+route.routeLength+"\t"+route.routeSatisfaction+"\t"+route.routeCost);
				showRoute(route, true);
				route.order.add(mm.data.get(i).getIndex());
				table.add(route);
				if (res.size() == 0) {
					res.add(route);
				} else {
					if (route.routeSatisfaction > res.get(0).routeSatisfaction) {
						res.clear();
						res.add(route);
					} else if (route.routeSatisfaction == res.get(0).routeSatisfaction) {
						res.add(route);
					}
				}
			} else {
				
//				 System.out.println("i:"+route.route.get(0).getIndex()+"\t"+route.routeLength+"\t"+route.routeSatisfaction+"\t"+route.routeCost);
				showRoute(route, false);
				addinvalidSet(route);
			}
		}

		for (int i = 0; i < res.size(); i++) {
			System.out.println("max:"+i);
			showRoute(res.get(i), true);
		}
		List<Route> table2 = new ArrayList<Route>();
		// 循环产生下一阶候选集合
		while (table.size() > 1) {
			//生成候选集合
			for (int i = 0; i < table.size(); i++) {
				Route r1 = table.get(i);
				int len = r1.getRoute().size();
				for (int j = i + 1; j < table.size(); j++) {
					Route r2 = table.get(j);
					boolean join = true;
					for (int k = 0; k < r1.route.size() - 1; k++) {
						if (r1.route.get(k).getIndex() != r2.route.get(k)
								.getIndex()) {
							join = false;
							break;
						}
					}
					if (join) {
						Route r3 = new Route();
						for (int k = 0; k < r1.route.size() - 1; k++) {
							r3.getRoute().add(r1.route.get(k));
						}
						r3.getRoute().add(r1.route.get(len - 1));
						r3.getRoute().add(r2.getRoute().get(len - 1));
						setAttributes(r3);
						// 用来针对特定条件打断点用的
						// if(r1.route.get(0).getIndex()==1 &&
						// r2.route.get(0).getIndex()==3)
						// {
						// j++;
						// j--;
						// }
						// 按照论文中所描述，先把所有景点集合生成，然后再来剪枝
						table2.add(r3);
					}
				}
			}
			
			
			//加入了优化策略ScoreChecking的优化策略，嵌入在TimeChecking当中
			//对与SatisfactionChecking来说，需要把数据由小到大排列，不然删除数据时会出错。
//			Set<Integer> canGen = new TreeSet<Integer>();
//			for (int i = 0; i < table2.size(); i++) {
//				Route route2 = table2.get(i);
//				//子集优化策略
//				if(om.subSetChecking(invalidSet, route2))
//				{
//					System.out.println("subSetChecking!");
//					showRoute(route2, false);
//					addinvalidSet(route2);
//					table2.remove(i);
//					i--;
//					continue;
//				}
//				//timeLowerBound优化
//				if(!om.timeLowBound(route2, timeRestriction))
//				{
//					System.out.println("time lower bound");
//					showRoute(route2, false);
//					addinvalidSet(route2);
//					table2.remove(i);
//					i--;
//					continue;
//				}
//				if (recursiveValid(route2, timeRestriction, costRestriction)) {
//					showRoute(route2, true);
//					if (route2.getRouteSatisfaction() > res.get(0)
//							.getRouteSatisfaction()) {
//						res.clear();
//						res.add(route2);
//					}else if(route2.getRouteSatisfaction() == res.get(0)
//							.getRouteSatisfaction())
//					{
//						res.add(route2);
//					}
//					int j = i + 1;
//					// 是否可以进行Satisfaction Check
//					boolean can = true;
//					canGen.clear();
//					while (j < table2.size()) {
//						if (table2.get(j).getRouteSatisfaction() >= res.get(0)
//								.getRouteSatisfaction()) {
//							can = false;
//							break;
//						}
//						canGen.add(j);
//						j++;
//					}
//					// Map<Integer,Boolean> canGen = new HashMap<Integer,
//					// Boolean>();
//					//Score Checking
//					if (can) {
//						for (j = 0; j < table2.size(); j++) {
//
//							Route rj = table2.get(j);
//							for (int k = j + 1; k < table2.size(); k++) {
//								if (canGen.contains(j) || canGen.contains(k)) {
//									Route rk = table2.get(k);
//									boolean canJoin = true;
//									for (int jj = 0; jj < rj.getRoute().size() - 1; jj++) {
//										if (rj.getRoute().get(jj) != rk
//												.getRoute().get(jj)) {
//											canJoin = false;
//											break;
//										}
//									}
//									if (canJoin) {
//										canGen.remove(j);
//										canGen.remove(k);
//									}
//								}
//
//							}
//						}
//					}else
//					{
//						canGen.clear();
//					}
//					
//					if(canGen.size()>0)
//					{
//						int weight = 0;
//						for (Iterator<Integer> iterator = canGen.iterator(); iterator
//								.hasNext();) {
//							Integer integer =  iterator.next();
//							System.out.println("Score Checking!");
//							try{
//							showRoute(table2.get(integer-weight), false);
//							}catch(Exception e)
//							{
//								e.printStackTrace();
//							}
//							table2.remove(integer-weight);
//							weight++;
//						}
//					}
//					
//				} else {
//					showRoute(route2, false);
//					addinvalidSet(route2);
//					table2.remove(i);
//					i--;
//				}
//			}

			// 没有ScoreChecking优化策略，逐条路线检查，按照Cost或者Time是否超过了
			for (int i=0;i<table2.size();i++) {
				Route route2 = table2.get(i);
				//时间下界优化
//				if(!om.timeLowBound(route2, timeRestriction))
//				{
//					System.out.println("time lower bound");
//					showRoute(route2, false);
//					addinvalidSet(route2);
//					table2.remove(i);
//					i--;
//					continue;
//				}
//				if(om.subSetChecking(invalidSet, route2))
//				{
//					System.out.println("subSetChecking!");
//					showRoute(route2, false);
//					addinvalidSet(route2);
//					table2.remove(i);
//					i--;
//					continue;
//				}
				if (recursiveValid(route2, timeRestriction, costRestriction)) {
					showRoute(route2, true);
					// table2.add(r3);
				} else {
//					System.out.println("recursiveValid");
					showRoute(route2, false);
					table2.remove(i);
					i--;
				}
			}
			// 新生成的景点集合是合法的
			// if (recursiveValid(r3, timeRestriction, costRestriction)) {
			// showRoute(r3, true);
			// table2.add(r3);
			// } else {
			// showRoute(r3, false);
			// }
			// 更新最优值
			for (int i = 0; i < table2.size(); i++) {
				if (table2.get(i).routeSatisfaction > res.get(0).routeSatisfaction) {
					res.clear();
					res.add(table2.get(i));
				} else if (table2.get(i).routeSatisfaction == res.get(0).routeSatisfaction) {
					res.add(table2.get(i));
				}
			}
			// 每次展示当前满意度最高的景点
			for (int i = 0; i < res.size(); i++) {
				System.out.println("max:"+i);
				showRoute(res.get(i), true);
			}
			if (table2.size() > 1) {
				table.clear();
				System.out.println("route size:"+table2.get(0).route.size());
				System.out.println("table size:"+table2.size());
				table.addAll(table2);
				table2.clear();
			} else {
				break;
			}
		}
		return res;
	}

	/**
	 * skyline算法，把最有集合查找出来
	 * 
	 * @param res
	 */
	public List<Route> skylineFilter(List<Route> res) {
		for (int i = 0; i < res.size(); i++) {
			findMinimalTime(res.get(i));
			// showRoute(res.get(i), true);
		}
		int minCost = 9999;
		List<Route> line = new ArrayList<Route>();
		Collections.sort(res, new Comparator<Route>() {
			public int compare(Route r1, Route r2) {
				if (r1.getRouteTime() != r2.getRouteTime()) {
					return r1.getRouteTime() - r2.getRouteTime();
				} else {
					return r1.getRouteCost() - r2.getRouteCost();
				}
			}
		});
		// 天际线算法整个过程
		for (int i = 0; i < res.size(); i++) {
			if (line.size() == 0) {
				line.add(res.get(i));
				minCost = res.get(i).getRouteCost();
			} else {
				if (minCost > res.get(i).getRouteCost()) {
					minCost = res.get(i).getRouteCost();
					line.add(res.get(i));
				}
			}
		}
		return line;
	}

	/**
	 * 把一条路径时间最短的旅游景点序列查找出来，并替换对应的序列
	 * 
	 * @param route
	 */
	public void findMinimalTime(Route route) {
		boolean visit[] = new boolean[route.route.size()];
		List<Integer> order = new ArrayList<Integer>(route.order);
		if (order.size() != 0) {
			setMinimalTime(route, 0, visit, 0, order, new ArrayList<Integer>());
		}
	}

	/**
	 * 经过优化的算法，获取一个集合最小排列时间算法
	 * 
	 * @param route
	 *            输入的Route
	 * @param time
	 *            累计的时间
	 * @param visit
	 *            是否访问过
	 * @param fi
	 *            可以访问到的
	 * @param order
	 *            原始的排列
	 * @param permutation
	 */
	public void setMinimalTime(Route route, int time, boolean visit[], int fi,
			List<Integer> order, List<Integer> permutation) {
		// 已经生成了一个排列
		if (order.size() == permutation.size()) {
			time += mm.map[fi][mm.data.size() - 1];
			// 这个时间比最小值小，替代最小值
			if (time < route.routeTime) {
				route.order.clear();
				route.order.addAll(permutation);
				route.setRouteTime(time);
			}
			return;
		}

		for (int i = 0; i < order.size(); i++) {
			if (!visit[i]) {
				visit[i] = true;
				int tmm = time;
				int fii = fi;
				tmm += mm.map[fii][order.get(i)];
				tmm += mm.idToAtt.get(order.get(i)).getStayTime();
				// 优化步骤
				if (tmm < route.routeTime) {
					permutation.add(order.get(i));
					fii = order.get(i);
					setMinimalTime(route, tmm, visit, fii, order, permutation);
					permutation.remove(permutation.size() - 1);
				}
				visit[i] = false;
			}
		}
		return;
	}

	/**
	 * 检查该集合是否合法，鉴别一个候选集合是否是合法的,产生排列来鉴别是否合法，实际当中需要鉴别时间和费用。
	 * 
	 * @param route
	 * @param timeRestriction
	 * @param costRestriction
	 * @return
	 */
	boolean recursiveValid(Route route, int timeRestriction, int costRestriction) {
		boolean visit[] = new boolean[route.route.size()];
		List<Integer> order = new ArrayList<Integer>();
		// 生成景点id列表
		for (int i = 0; i < route.route.size(); i++) {
			order.add(route.route.get(i).getIndex());
		}
		if (route.routeCost > costRestriction) {
			return false;
		}
		 boolean res = checkPermutation(route, visit, order,
		 new ArrayList<Integer>(), timeRestriction);
//		boolean res = om.checkPermutationOPT(route, visit, order,
//				new ArrayList<Integer>(), 0, timeRestriction, 0);
		return res;
	}

	/**
	 * 对全排列进行检查，只要有一种排列时间有效，就返回。
	 * 
	 * @param r
	 * @param visit
	 * @param order
	 * @param permutation
	 *            新生成的排列，用来检测是否合法
	 * @param timeRestriction
	 * @return
	 */
	boolean checkPermutation(Route r, boolean visit[], List<Integer> order,
			List<Integer> permutation, int timeRestriction) {
		if (permutation.size() == order.size()) {
			// 检查Time是否合法
			int time = 0, fi = 0;
			for (int i = 0; i < permutation.size(); i++) {
				Attraction next = mm.idToAtt.get(permutation.get(i));
				time += next.getStayTime();
				time += mm.map[fi][next.getIndex()];
				fi = next.getIndex();
//				System.out.print(fi+",");
			}
			time += mm.map[fi][mm.data.size() - 1];
//			System.out.println(time+","+r.getRouteSatisfaction()+","+r.getRouteCost());
			// permutation.remove(permutation.size() - 1);
			if (time <= timeRestriction) {
				r.setRouteTime(time);
				r.order = new ArrayList<Integer>(permutation);
				return true;
			}
			
		}
		for (int i = 0; i < order.size(); i++) {
			if (!visit[i]) {
				visit[i] = true;
				permutation.add(order.get(i));
				if (checkPermutation(r, visit, order, permutation,
						timeRestriction)) {
					return true;
				}
				visit[i] = false;
				// if(permutation.size()>0)
				permutation.remove(permutation.size() - 1);
			}
		}
		return false;
	}

	/**
	 * 
	 * 由于我需要调整费用值，来使得程序把一些关键例子显示了出来。
	 * 
	 * @return
	 */
	boolean isValidSet(Route route, int timeRestriction, int costRestriction) {
		if (route.getRouteTime() > timeRestriction) {
			return false;
		}
		if (route.getRouteCost() > costRestriction) {
			return false;
		}
		return true;
	}

	/**
	 * 给Route设置属性,time,cost,satisfaction,这些属性需要通过计算才能够得到 看是否把Time的计算时间去除
	 * 
	 * @param route
	 */
	public void setAttributes(Route route) {
		int length = route.getRoute().size();
		route.setRouteLength(length);

		int time = 0, fi = 0, cost = 0, satisfaction = 0;
		for (int j = 0; j < route.getRoute().size(); j++) {
			Attraction next = route.getRoute().get(j);
			time += next.getStayTime();
			cost += next.getPlayCost();
			satisfaction += next.getSatisfaction();
			time += mm.map[fi][next.getIndex()];
			fi = next.getIndex();
		}
		time += mm.map[fi][mm.data.size() - 1];
		route.setRouteSatisfaction(satisfaction);
		route.setRouteTime(time);
		route.setRouteCost(cost);
	}

	/**
	 * 把一条路线的景点编号，时间长短，获得的满意度，以及费用显示出来
	 * 
	 * @param route
	 * @param valid
	 */
	public void showRoute(Route route, boolean valid) {
		if (!valid) {
			System.out.print("i:");
		}
		System.out.print("<");
		for (int i = 0; i < route.route.size(); i++) {
			System.out.print(route.getRoute().get(i).getIndex());
			if (i != route.route.size() - 1) {
				System.out.print(",");
			}
		}
		System.out.print(">,");
		System.out.print(route.getRouteTime() + ",");
		System.out.print(route.getRouteSatisfaction() + ",");
		System.out.println(route.getRouteCost());
		System.out.println();
	}
}

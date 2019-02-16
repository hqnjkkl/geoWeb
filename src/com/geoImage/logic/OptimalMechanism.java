package com.geoImage.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 这个类强调算法上的优化，相当于把原来算法的优化功能分离出来，单独写一个类
 * @author huqiaonan
 * @version 2.0,2015年2月24日 下午9:57:56
 */
public class OptimalMechanism {
	
	
	MyMap mm = null;
	public OptimalMechanism()
	{
		
	}
	public OptimalMechanism(MyMap m)
	{
		this.mm = m;
	}
	/**
	 * 在进行时间检查时，原文当中是生成候选集合的一个个排列，并对排列逐一检查。
	 * 而使用了优化的策略之后，并不总是需要完整的排列，在生成一个排列的过程当中，就进行时间累加，如果在排列未完全生成时，就已经
	 * 超时了，那么剩下的排列生成工作去除。
	 * @param r  存储一个个景点的路径
	 * @param visit 访问情况
	 * @param order 原始的排列顺序
	 * @param permutation 存储新的排列
	 * @param fi 存储当前所处的景点编号
	 * @param timeRestriction 总的时间限制
	 * @param time	已经使用的时间
	 * @return
	 */
	boolean checkPermutationOPT(Route r, boolean visit[], List<Integer> order,
			List<Integer> permutation, int fi,int timeRestriction,int time) {
		if(permutation.size()==order.size())
		{
			time+=mm.map[fi][mm.data.size()-1];
			if(time<=timeRestriction)
			{
				r.routeTime = time;
				r.order.clear();
				r.order.addAll(order);
				return true;
			}else
			{
				return false;
			}
		}
		for(int i=0;i<order.size();i++)
		{
			if(!visit[i])
			{
				visit[i] = true;
				int tmm = time + mm.map[fi][order.get(i)]+mm.idToAtt.get(order.get(i)).getStayTime();
				//这里是剪枝步骤
				if(tmm<=timeRestriction)
				{
					permutation.add(order.get(i));
					boolean tmpb = checkPermutationOPT(r, visit, order, permutation, order.get(i), timeRestriction, tmm);
					if(tmpb){
						return true;
					}
					permutation.remove(permutation.size()-1);
				}
				visit[i] = false;
			}
		}
		return false;
	}
	/**
	 * 对景点进行排序优化，排序规则旅行时间由大到小，然后费用由大到小。sort优化的好处是，某些时候，生成的下一阶集合
	 * 会减少。
	 * @param atts 需要排序的原始景点集合
	 */
	public void attractionSort(List<Attraction> atts)
	{
		Collections.sort(atts.subList(1, atts.size()-1), new Comparator<Attraction>() {

			public int compare(Attraction o1, Attraction o2) {
				int time1 = mm.map[0][o1.getIndex()]+mm.map[o1.getIndex()][mm.data.size()-1]+o1.getStayTime();
				int time2 = mm.map[0][o2.getIndex()]+mm.map[o2.getIndex()][mm.data.size()-1]+o2.getStayTime();
//				if(time1!=time2)
//				{
//					return -(time1-time2);
//				}else
//				{
//					return -(o1.getPlayCost()-o2.getPlayCost());
//				}
//				if(o1.getStayTime()!=o2.getStayTime())
//				{
//					return -(o1.getStayTime()-o2.getStayTime());
//				}else
//				{
//					return -(o1.getPlayCost()-o2.getPlayCost());
//				}
				
				if(o1.getPlayCost()-o2.getPlayCost()!=0)
				{
					return -(o1.getPlayCost()-o2.getPlayCost());
				}else
				{
					return -(o1.getStayTime()-o2.getStayTime());
//					return -(time1-time2);
				}
			}
		});
		//通过子列表来修改顺序，其结果会反应到父列表当中
//		for(int i=0;i<atts.size();i++)
//		{
//			System.out.println(atts.get(i).getIndex()+",");
//		}
	}
	/**
	 * 这个时间下界优化策略，通过检查n个景点之间最短的n-1条边，以及起点和终点和景点之间的最短边，用来判断
	 * 最短的时间，是否超过最大时间限制
	 * @param route   
	 * @param timeRestriction
	 * @return true，不超过,false超过
	 */
	public boolean timeLowBound(Route route,int timeRestriction)
	{
		List<Attraction> tmpOrder = route.getRoute();
		List<Integer> lowBound = new ArrayList<Integer>();
		int time = 0,stayTime = 0;
		for(int i=0;i<tmpOrder.size();i++)
		{
			int x = tmpOrder.get(i).getIndex();
			stayTime += mm.idToAtt.get(x).getStayTime();
			int minTime = 99999999;
			boolean change = false;
			for(int j=i+1;j<tmpOrder.size();j++)
			{
//				lowBound.add(mm.map[x][tmpOrder.get(j).getIndex()]);
				if(minTime>mm.map[x][tmpOrder.get(j).getIndex()])
				{
					minTime = mm.map[x][tmpOrder.get(j).getIndex()];
					change =  true;
				}
			}
			if(change){
				time+=minTime;
			}
			
		}
//		Collections.sort(lowBound);
//		for(int i=0;i<tmpOrder.size()-1;i++)
//		{
//			time += lowBound.get(i);
//		}
		time += stayTime;
		if(time>timeRestriction)
		{
			return false;
		}
		return true;
	}

	/**
	 * 无效集合的超集都是无效的，因此，在剪枝之后，把无效的集合存起来
	 * @param invalidSet
	 * @param route
	 * @return true,当前集合的子集包含无效集合。false，当前集合的子集不包含无效集合。
	 */
	public boolean subSetChecking(Set<Set<Integer>> invalidSet, Route route)
	{
		Set<Integer> sub= new HashSet<Integer>();
		List<Attraction> arr = route.getRoute();
		List<Integer> order = new ArrayList<Integer>();
		for(int i=0;i<arr.size();i++)
		{
			order.add(arr.get(i).getIndex());
		}
		return recursiveSubSet(order, 0, sub, invalidSet);
	}
	/**
	 * 子集生成算法，HashSet的Contains，最终还是需要使用hash==hash &&(key==e.key ||key.equals(e.key)
	 * @param order 原始的顺序
	 * @param index 下一个开始的位置
	 * @param sub 当前的子集
	 * @param invalidSet 所有无效的集合
	 * @return true,当前集合的子集包含无效集合。false，当前集合的子集不包含无效集合。
	 */
	public boolean recursiveSubSet(List<Integer> order,int index,Set<Integer> sub,Set<Set<Integer>> invalidSet)
	{
		for(int i=index;i<order.size();i++)
		{
			int tmp = order.get(i);
			sub.add(tmp);
			if(invalidSet.contains(sub))
			{
				return true;
			}else
			{
				if(recursiveSubSet(order, index+1, sub, invalidSet))
				{
					return true;
				}
			}
			sub.remove(tmp);
		}
		return false;
	}
	
	/**
	 * 检查是否有无法产生下一阶候选集合的路径，在这个时候就删除.由于这个优化策略单独加入算法时，会产生比较大的负面影响，并且
	 * 这个优化策略。
	 * 这个优化策略和算法本身嵌套密切，所以无法单独拿出来写了。
	 * @param table
	 */
	public void  satisfactionChecking(List<Route> table,int maxScore)
	{
//		List<Integer> checkList = new ArrayList<Integer>();
		for (Iterator<Route> iterator = table.iterator(); iterator.hasNext();) {
			Route route = (Route) iterator.next();
			if(route.getRouteSatisfaction()>=maxScore)
			{
				
			}
		}
		for(int i=0;i<table.size();i++)
		{
			
		}
	}
}

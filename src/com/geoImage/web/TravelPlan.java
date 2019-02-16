package com.geoImage.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geoImage.logic.MapPoint;

/**
 * 图当中的点，暂时有一个属性id
 * @author huqiaonan
 * @version 2.0,2015年2月26日 上午8:54:09
 */

class Edge
{
    public MapPoint from;
    public MapPoint to;
    public int w;
    public Edge() {
	}
	public Edge(MapPoint from, MapPoint to, int w) {
		super();
		this.from = from;
		this.to = to;
		this.w = w;
	}
	
	public String toString() {
		return "Edge [from=" + from + ", to=" + to + ", w=" + w + "]";
	}
}

enum ClusterType
{
    emLimitCount,
    emLimitWeight,
}

class Graph
{
    public List<MapPoint> lstNodes;
    public List<Edge> lstEdges;
    public Graph() {
    	
	}
	public Graph(List<MapPoint> lstNodes, List<Edge> lstEdges) {
		this.lstNodes = lstNodes;
		this.lstEdges = lstEdges;
	}
}

/**
 * 行程规划算法，输入包括一些点和边，输出图的最小生成树聚类图
 * @author huqiaonan
 * @version 2.0,2015年2月26日 上午9:45:25
 */
public class TravelPlan 
{
    public List<Graph> MSTC_LimitCount(Graph graph, int nCountLimit)
    {
    	Graph gg = KruskalMST(graph);
//    	ShowRoute sr = new ShowRoute();
//        List<Graph> lg = new ArrayList<Graph>();
//        lg.add(gg);
//        sr.showSubGraph(lg);
        return Cluster_LimitCount(gg, nCountLimit);
    }

    public List<Graph> MSTC_LimitWeight(Graph graph, int nWeightLimit)
    {
        return Cluster_LimitWeight(KruskalMST(graph), nWeightLimit);
    }

    Graph KruskalMST(Graph map)
    {
        List<MapPoint> lstNodes_Red = new ArrayList<MapPoint>();
        List<Edge> lstEdges_Red = new ArrayList<Edge>();

        Collections.sort(map.lstEdges, new Comparator<Edge>() {

			@Override
			public int compare(Edge o1, Edge o2) {
				return o1.w - o2.w;
			}
		});
        
        //按边的权重从小到大排序
//        map.lstEdges.Sort((a, b) => { return Comparer<int>.Default.Compare(a.w, b.w); });

        for (int i = 0; i < map.lstEdges.size(); ++i)
        {
            Edge e = map.lstEdges.get(i);

            //判断是否为安全边，是则将边和两个端点加入生成树，否则舍弃该边
            List<MapPoint> lstLinkNodes = new ArrayList<MapPoint>();
            List<Edge> lstLinkEdges = new ArrayList<Edge>();
            DFS(e.from, lstEdges_Red, lstLinkNodes, lstLinkEdges,null);
            if (!lstLinkNodes.contains(e.to))
            {
                if (!lstNodes_Red.contains(e.from))
                {
                    lstNodes_Red.add(e.from);
                }
                if (!lstNodes_Red.contains(e.to))
                {
                    lstNodes_Red.add(e.to);
                }
                lstEdges_Red.add(e);
            }
            else
            {
                continue;
            }

            if (lstEdges_Red.size() == map.lstNodes.size() - 1)
            {
                //最小生成树构造完毕
                break;
            }
        }
        

        //返回最小生成树
        return new Graph(lstNodes_Red,lstEdges_Red);
    }

    List<Graph> Cluster_LimitCount(Graph map, int nCountLimit)
    {
        Graph map_Cluster = new Graph(map.lstNodes,new ArrayList<Edge>(map.lstEdges));
        Collections.sort(map.lstEdges, new Comparator<Edge>() {

			public int compare(Edge o1, Edge o2) {
				return o2.w - o1.w;
			}
		});
        for (int i = 0; i < nCountLimit - 1; ++i)
        {
            Edge e = map.lstEdges.get(i);
            map_Cluster.lstEdges.remove(e);
//            ArrayList al = new ArrayList<E>();
        }
       
        return GetSubGraphs(map_Cluster);
    }

    List<Graph> Cluster_LimitWeight(Graph map, int nWeightLimit)
    {
        Graph map_Cluster = new Graph(map.lstNodes,new ArrayList<Edge>(map.lstEdges));

        for (int i = 0; i < map.lstEdges.size(); ++i)
        {
            Edge e = map.lstEdges.get(i);
            if (e.w > nWeightLimit)
            {
                map_Cluster.lstEdges.remove(e);
            }
        }

        return GetSubGraphs(map_Cluster);
    }

    List<Graph> GetSubGraphs(Graph map)
    {
        List<Graph> lstGraphs = new ArrayList<Graph>();
        List<MapPoint> lstNodes_Red = new ArrayList<MapPoint>();
        //深度遍历法划分连通子图,按照点来划分
        for (int i = 0; i < map.lstNodes.size(); ++i)
        {
            MapPoint n = map.lstNodes.get(i);
            if (lstNodes_Red.contains(n))
            {
                continue;
            }

            Graph subGraph = new Graph(new ArrayList<MapPoint>(),new ArrayList<Edge>());
            DFS(n, map.lstEdges, subGraph.lstNodes, subGraph.lstEdges,null);
            lstNodes_Red.addAll(subGraph.lstNodes);
            lstGraphs.add(subGraph);
        }

        return lstGraphs;
    }

    void DFS(MapPoint n, List<Edge> lstEdges, List<MapPoint> lstNodes_Red, List<Edge> lstEdges_Red,Map<MapPoint, List<Edge>> dicNode2Edges)
    {
        lstNodes_Red.add(n);
        //若参数dicNode2Edges为null则根据参数lstEdges生成dicNode2Edges
        if (dicNode2Edges == null)
        {
            dicNode2Edges = new HashMap<MapPoint, List<Edge>>();
            for(Edge e:lstEdges)
//            foreach (Edge e in lstEdges)
            {
                if (!dicNode2Edges.containsKey(e.from))
                {
                    dicNode2Edges.put(e.from, new ArrayList<Edge>());
                }
                if (!dicNode2Edges.containsKey(e.to))
                {
                    dicNode2Edges.put(e.to, new ArrayList<Edge>());
                }
                dicNode2Edges.get(e.from).add(e);
                dicNode2Edges.get(e.to).add(e);
            }
        }
        
        //根据边来把对应点所在的连通子图找出来
        if (dicNode2Edges.containsKey(n))
        {
//            foreach (Edge e in dicNode2Edges[n])
            for(Edge e:dicNode2Edges.get(n))
            {
                if (e.from.equals(n) && !lstNodes_Red.contains(e.to))
                {
                    lstEdges_Red.add(e);
                    DFS(e.to, lstEdges, lstNodes_Red, lstEdges_Red, dicNode2Edges);
                }
                else if (e.to.equals(n) && !lstNodes_Red.contains(e.from))
                {
                    lstEdges_Red.add(e);
                    DFS(e.from, lstEdges, lstNodes_Red, lstEdges_Red, dicNode2Edges);
                }
            }
        }
    }
}

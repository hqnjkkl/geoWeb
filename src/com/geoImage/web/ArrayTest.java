package com.geoImage.web;

import java.util.ArrayList;
import java.util.List;

public class ArrayTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static String getArrays()
	{
//		String[][] res = {{"aaa","bbb"},
//						{"ccc","ddd"}
//		};
		String res = "haha";
		return res;
	}
	public static String getArrays2()
	{
		String[][] res = {{"aaa","bbb"},
						{"ccc","ddd"}
		};
		List<ArrayList<String>> al = new ArrayList<ArrayList<String>>();
		for(int i=0;i<res.length;i++)
		{
			al.add(new ArrayList<String>());
			for(int j=0;j<res[0].length;j++)
			{				
				al.get(i).add(res[i][j]);
			}
		}
//		String res = "haha";
		System.out.println(al.toString());
		return al.toString();
	}
	
	public static String getArrays3(String name)
	{
		System.out.println(name);
		String[] res = {"aaa","bbb","ccc","ddd"};
		List<String> al = new ArrayList<String>();
		for(int i=0;i<res.length;i++)
		{
			al.add(res[i]);

		}
//		String res = "haha";
		System.out.println(al.toString());
		return al.toString();
	}
	
	public static String getArrays4()
	{
		//System.out.println(name);
		String[] res = {"aaa","bbb","ccc","ddd"};
		List<String> al = new ArrayList<String>();
		for(int i=0;i<res.length;i++)
		{
			al.add(res[i]);

		}
//		String res = "haha";
		System.out.println(al.toString());
		return al.toString();
	}
	
	public static String getlats()
	{
		List<Double> lats = new ArrayList<Double>();
		lats.add(34.566);
		lats.add(34.3443);
		return lats.toString();
	}
}

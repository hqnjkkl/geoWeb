package com.geoImage.web;

public class TestParameter {
	
public static void main(String[] args) {
	TestParameter tp = new TestParameter();
	tp.add(4, 6);
	tp.obj1(4);
	
}

public void add(int a,int b)
{
	System.out.println("a+b:"+(a+b));
}

public void add(Integer a,Integer b)
{
	System.out.println("A+B:"+(a+b));
}
public void obj1(Object obj)
{
	System.out.println("Object:"+obj);
}

public void obj1(Integer obj)
{
	System.out.println("Integer:"+obj);
}

}

package com.geoImage.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * 存储Route
 * @author huqiaonan
 *
 */
public class Route {
	List<Attraction> route = new ArrayList<Attraction>();
	List<Integer> order = new ArrayList<Integer>();
	int routeLength ;
	int routeTime;
	int routeSatisfaction;
	int routeCost;
	public static void main(String[] args) {
		System.out.println("haha");
	}
	
	public Route(){
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 11;
		int result = 1;
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((route == null) ? 0 : route.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Route other = (Route) obj;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (route == null) {
			if (other.route != null)
				return false;
		} else if (!route.equals(other.route))
			return false;
		return true;
	}
	public Route(int routeTime, int routeCost) {
//		super();
		this.routeTime = routeTime;
		this.routeCost = routeCost;
	}
	public List<Attraction> getRoute() {
		return route;
	}
	public void setRoute(List<Attraction> route) {
		this.route = route;
	}
	public int getRouteLength() {
		return routeLength;
	}
	public void setRouteLength(int routeLength) {
		this.routeLength = routeLength;
	}
	public int getRouteTime() {
		return routeTime;
	}
	public void setRouteTime(int routeTime) {
		this.routeTime = routeTime;
	}
	public int getRouteSatisfaction() {
		return routeSatisfaction;
	}
	public void setRouteSatisfaction(int routeSatisfaction) {
		this.routeSatisfaction = routeSatisfaction;
	}
	public int getRouteCost() {
		return routeCost;
	}
	public void setRouteCost(int routeCost) {
		this.routeCost = routeCost;
	}

	public List<Integer> getOrder() {
		return order;
	}

	public void setOrder(List<Integer> order) {
		this.order = order;
	}
	
	
}

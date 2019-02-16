package com.geoImage.logic;

public class MapPoint

{
	int id;
	double latitude;
	double longtitude;
	String name;
	int occurence;

	public MapPoint(double latitude, double longtitude, String name) {
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.name = name;
	}
	
	
	public MapPoint(int id, double latitude, double longtitude, String name,
			int occurence) {
		super();
		this.id = id;
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.name = name;
		this.occurence = occurence;
	}


	public double getLatitude() {
		return latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	
    public MapPoint(){}
	public MapPoint(int id) {
		this.id = id;
	}
	

	@Override
	public String toString() {
		return "MapPoint [id=" + id + ", latitude=" + latitude
				+ ", longtitude=" + longtitude + ", name=" + name + "]";
	}
	
	public String toString2()
	{
		return id+"->"+latitude+"->"+longtitude+"->"+name+"->"+occurence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		MapPoint other = (MapPoint) obj;
		if (id != other.id)
			return false;
		return true;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getOccurence() {
		return occurence;
	}


	public void setOccurence(int occurence) {
		this.occurence = occurence;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
    
}
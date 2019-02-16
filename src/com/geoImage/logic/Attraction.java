package com.geoImage.logic;

/**
 * 存储旅游景点的游玩时长，游玩费用，满意度等.
 * @author huqiaonan
 *
 */
public class Attraction {
	private int index ;
	private int satisfaction;
	private int stayTime;
	private int playCost;
	int id;
	public Attraction() {

	}
	
	
	public Attraction(int index, int satisfaction, int stayTime, int playCost) {
		super();
		this.index = index;
		this.satisfaction = satisfaction;
		this.stayTime = stayTime;
		this.playCost = playCost;
	}


	public Attraction(int satisfaction, int stayTime, int playCost) {
		this.satisfaction = satisfaction;
		this.stayTime = stayTime;
		this.playCost = playCost;
	}
	

	@Override
	public String toString() {
		return "Attraction [index=" + index + ", satisfaction=" + satisfaction
				+ ", stayTime=" + stayTime + ", playCost=" + playCost + ", id="
				+ id + "]";
	}

	public String toString2() {
		return id+"->"+index + "->" + satisfaction
				+ "->" + stayTime + "->" + playCost;
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attraction other = (Attraction) obj;
		if (index != other.index)
			return false;
		if (playCost != other.playCost)
			return false;
		if (satisfaction != other.satisfaction)
			return false;
		if (stayTime != other.stayTime)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	@Override
	public int hashCode() {
		final int prime = 11;
		int result = 1;
		result = prime * result + index;
		result = prime * result + playCost;
		result = prime * result + satisfaction;
		result = prime * result + stayTime;
		return result;
	}
	
	
	public static void main(String[] args) {
		Attraction att = new Attraction(20,2,4);
//		System.out.println(att.hashCode());
		System.out.println("你好");
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public int getSatisfaction() {
		return satisfaction;
	}


	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}


	public int getStayTime() {
		return stayTime;
	}


	public void setStayTime(int stayTime) {
		this.stayTime = stayTime;
	}


	public int getPlayCost() {
		return playCost;
	}


	public void setPlayCost(int playCost) {
		this.playCost = playCost;
	}
	
}

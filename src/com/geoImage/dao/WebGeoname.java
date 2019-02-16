package com.geoImage.dao;

/**
 * WebGeoname entity. @author MyEclipse Persistence Tools
 */

public class WebGeoname implements java.io.Serializable {

	// Fields

	private Integer webGeonameId;
	private String webGeonameContent;
	private Double latitude;
	private Double longitude;
	private Integer occurence;

	// Constructors

	/** default constructor */
	public WebGeoname() {
	}

	/** full constructor */
	public WebGeoname(String webGeonameContent, Double latitude,
			Double longitude, Integer occurence) {
		this.webGeonameContent = webGeonameContent;
		this.latitude = latitude;
		this.longitude = longitude;
		this.occurence = occurence;
	}

	// Property accessors

	public Integer getWebGeonameId() {
		return this.webGeonameId;
	}

	public void setWebGeonameId(Integer webGeonameId) {
		this.webGeonameId = webGeonameId;
	}

	public String getWebGeonameContent() {
		return this.webGeonameContent;
	}

	public void setWebGeonameContent(String webGeonameContent) {
		this.webGeonameContent = webGeonameContent;
	}

	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Integer getOccurence() {
		return this.occurence;
	}

	public void setOccurence(Integer occurence) {
		this.occurence = occurence;
	}

	@Override
	public String toString() {
		return "WebGeoname [webGeonameId=" + webGeonameId
				+ ", webGeonameContent=" + webGeonameContent + ", latitude="
				+ latitude + ", longitude=" + longitude + ", occurence="
				+ occurence + "]";
	}

}
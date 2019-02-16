package com.geoImage.dao;

/**
 * WebRuleAtt entity. @author MyEclipse Persistence Tools
 */

public class WebRuleAtt implements java.io.Serializable {

	// Fields

	private Integer webRuleAttId;
	private String webRuleAttRules;
	private Integer occurence;
	private Integer ruleSize;

	// Constructors

	/** default constructor */
	public WebRuleAtt() {
	}

	/** full constructor */
	public WebRuleAtt(String webRuleAttRules, Integer occurence,
			Integer ruleSize) {
		this.webRuleAttRules = webRuleAttRules;
		this.occurence = occurence;
		this.ruleSize = ruleSize;
	}

	// Property accessors

	public Integer getWebRuleAttId() {
		return this.webRuleAttId;
	}

	public void setWebRuleAttId(Integer webRuleAttId) {
		this.webRuleAttId = webRuleAttId;
	}

	public String getWebRuleAttRules() {
		return this.webRuleAttRules;
	}

	public void setWebRuleAttRules(String webRuleAttRules) {
		this.webRuleAttRules = webRuleAttRules;
	}

	public Integer getOccurence() {
		return this.occurence;
	}

	public void setOccurence(Integer occurence) {
		this.occurence = occurence;
	}

	public Integer getRuleSize() {
		return this.ruleSize;
	}

	public void setRuleSize(Integer ruleSize) {
		this.ruleSize = ruleSize;
	}

}
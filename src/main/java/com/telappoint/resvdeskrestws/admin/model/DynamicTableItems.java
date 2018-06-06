package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class DynamicTableItems {
	private String dynamicSelect;
	private String dynamicFrom;
	private String dynamicType;
	private String dynamicTitle;
	private String dynamicHide;
	private String dynamicJavaReflection;
	private String aliasName;
	

	public String getDynamicSelect() {
		return dynamicSelect;
	}

	public void setDynamicSelect(String dynamicSelect) {
		this.dynamicSelect = dynamicSelect;
	}

	public String getDynamicFrom() {
		return dynamicFrom;
	}

	public void setDynamicFrom(String dynamicFrom) {
		this.dynamicFrom = dynamicFrom;
	}

	public String getDynamicType() {
		return dynamicType;
	}

	public void setDynamicType(String dynamicType) {
		this.dynamicType = dynamicType;
	}

	public String getDynamicTitle() {
		return dynamicTitle;
	}

	public void setDynamicTitle(String dynamicTitle) {
		this.dynamicTitle = dynamicTitle;
	}

	public String getDynamicJavaReflection() {
		return dynamicJavaReflection;
	}

	public void setDynamicJavaReflection(String dynamicJavaReflection) {
		this.dynamicJavaReflection = dynamicJavaReflection;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getDynamicHide() {
		return dynamicHide;
	}

	public void setDynamicHide(String dynamicHide) {
		this.dynamicHide = dynamicHide;
	}

}

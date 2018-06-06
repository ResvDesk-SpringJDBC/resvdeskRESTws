package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class DynamicSQL {
	private String category;
	private String sqlQuery;
	private String dynamicKeyMapping;
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSqlQuery() {
		return sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public String getDynamicKeyMapping() {
		return dynamicKeyMapping;
	}

	public void setDynamicKeyMapping(String dynamicKeyMapping) {
		this.dynamicKeyMapping = dynamicKeyMapping;
	}
}

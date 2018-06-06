package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji
 *
 */
		
@JsonSerialize(include = Inclusion.NON_NULL)
public class PrivilageData {
	private Long privilageId;
	private String privilageName;

	public String getPrivilageName() {
		return privilageName;
	}

	public void setPrivilageName(String privilageName) {
		this.privilageName = privilageName;
	}

	public Long getPrivilageId() {
		return privilageId;
	}

	public void setPrivilageId(Long privilageId) {
		this.privilageId = privilageId;
	}
}

package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class PrivilageResponse extends BaseResponse {
	private List<PrivilageData> privilageList;

	public List<PrivilageData> getPrivilageList() {
		return privilageList;
	}

	public void setPrivilageList(List<PrivilageData> privilageList) {
		this.privilageList = privilageList;
	}
}
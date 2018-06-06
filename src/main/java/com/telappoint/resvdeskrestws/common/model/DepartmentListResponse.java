package com.telappoint.resvdeskrestws.common.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class DepartmentListResponse extends BaseResponse {

	private String selectDepartmentLabel;
	private String selectDefaultDepartmentId;
	private List<Options> departmentList;
		
	public String getSelectDepartmentLabel() {
		return selectDepartmentLabel;
	}

	public void setSelectDepartmentLabel(String selectDepartmentLabel) {
		this.selectDepartmentLabel = selectDepartmentLabel;
	}

	public String getSelectDefaultDepartmentId() {
		return selectDefaultDepartmentId;
	}

	public void setSelectDefaultDepartmentId(String selectDefaultDepartmentId) {
		this.selectDefaultDepartmentId = selectDefaultDepartmentId;
	}

	public List<Options> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Options> departmentList) {
		this.departmentList = departmentList;
	}
}

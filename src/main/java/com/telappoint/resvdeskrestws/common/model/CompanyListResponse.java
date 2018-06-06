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
public class CompanyListResponse extends BaseResponse {
 
	private String selectCompanyLabel;
	private String selectDefaultCompanyId;
	private List<Options> companyList;
	
	public String getSelectCompanyLabel() {
		return selectCompanyLabel;
	}
	public void setSelectCompanyLabel(String selectCompanyLabel) {
		this.selectCompanyLabel = selectCompanyLabel;
	}
	public String getSelectDefaultCompanyId() {
		return selectDefaultCompanyId;
	}
	public void setSelectDefaultCompanyId(String selectDefaultCompanyId) {
		this.selectDefaultCompanyId = selectDefaultCompanyId;
	}
	public List<Options> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<Options> companyList) {
		this.companyList = companyList;
	}
}

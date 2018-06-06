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
public class RegistrationInfoResponse extends BaseResponse {
	
	// online field
	private List<RegistrationField> regFieldList;
	
	// ivr field
	private String submitFieldList;

	
	public String getSubmitFieldList() {
		return submitFieldList;
	}

	public void setSubmitFieldList(String submitFieldList) {
		this.submitFieldList = submitFieldList;
	}

	public List<RegistrationField> getRegFieldList() {
		return regFieldList;
	}

	public void setRegFieldList(List<RegistrationField> regFieldList) {
		this.regFieldList = regFieldList;
	}
}

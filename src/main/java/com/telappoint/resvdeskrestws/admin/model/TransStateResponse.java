package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
public class TransStateResponse extends BaseResponse {
	private List<TransState> transStateList;

	public List<TransState> getTransStateList() {
		return transStateList;
	}

	public void setTransStateList(List<TransState> transStateList) {
		this.transStateList = transStateList;
	}	
}

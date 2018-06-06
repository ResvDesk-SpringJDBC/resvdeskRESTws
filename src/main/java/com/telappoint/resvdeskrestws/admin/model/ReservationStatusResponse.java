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
public class ReservationStatusResponse extends BaseResponse {
	private List<ReservationStatus> resvStatusList;

	public List<ReservationStatus> getResvStatusList() {
		return resvStatusList;
	}

	public void setResvStatusList(List<ReservationStatus> resvStatusList) {
		this.resvStatusList = resvStatusList;
	}
	
}

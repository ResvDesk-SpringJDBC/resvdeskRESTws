package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationStatus {
	private Integer resvId;
	private String resvStatus;
	private Integer statusVal;

	public Integer getResvId() {
		return resvId;
	}

	public void setResvId(Integer resvId) {
		this.resvId = resvId;
	}

	public String getResvStatus() {
		return resvStatus;
	}

	public void setResvStatus(String resvStatus) {
		this.resvStatus = resvStatus;
	}

	public Integer getStatusVal() {
		return statusVal;
	}

	public void setStatusVal(Integer statusVal) {
		this.statusVal = statusVal;
	}

}

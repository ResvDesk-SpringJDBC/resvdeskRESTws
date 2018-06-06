package com.telappoint.resvdeskrestws.admin.model;

/**
 * 
 * @author Balaji N
 *
 */
public class TransState {
	private Long transStateId;
	private Long transId;
	private String timeStamp;
	private Integer stateValue;
	
	public Long getTransStateId() {
		return transStateId;
	}
	public void setTransStateId(Long transStateId) {
		this.transStateId = transStateId;
	}
	public Long getTransId() {
		return transId;
	}
	public void setTransId(Long transId) {
		this.transId = transId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Integer getStateValue() {
		return stateValue;
	}
	public void setStateValue(Integer stateValue) {
		this.stateValue = stateValue;
	}
}

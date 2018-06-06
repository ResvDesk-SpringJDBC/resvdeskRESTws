package com.telappoint.resvdeskrestws.admin.model;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
public class GraphResponse extends BaseResponse {
	private String eventTimeStr;
	private String noOfBookedResv;
	private String noOfOpenResv;
	private String noOfResvered;
	
	public String getEventTimeStr() {
		return eventTimeStr;
	}
	public void setEventTimeStr(String eventTimeStr) {
		this.eventTimeStr = eventTimeStr;
	}
	public String getNoOfBookedResv() {
		return noOfBookedResv;
	}
	public void setNoOfBookedResv(String noOfBookedResv) {
		this.noOfBookedResv = noOfBookedResv;
	}
	public String getNoOfOpenResv() {
		return noOfOpenResv;
	}
	public void setNoOfOpenResv(String noOfOpenResv) {
		this.noOfOpenResv = noOfOpenResv;
	}
	public String getNoOfResvered() {
		return noOfResvered;
	}
	public void setNoOfResvered(String noOfResvered) {
		this.noOfResvered = noOfResvered;
	}
}

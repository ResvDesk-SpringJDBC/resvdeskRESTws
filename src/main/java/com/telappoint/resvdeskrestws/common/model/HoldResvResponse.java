package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class HoldResvResponse extends BaseResponse {
	private String scheduleId = "-1";
	private String status;
	private String recordMsg;
	
	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecordMsg() {
		return recordMsg;
	}

	public void setRecordMsg(String recordMsg) {
		this.recordMsg = recordMsg;
	}
}

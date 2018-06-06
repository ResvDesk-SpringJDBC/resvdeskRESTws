package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
public class EventDateTimeResponse extends BaseResponse {
	private List<EventDateTime> eventDateTimeList;

	public List<EventDateTime> getEventDateTimeList() {
		return eventDateTimeList;
	}

	public void setEventDateTimeList(List<EventDateTime> eventDateTimeList) {
		this.eventDateTimeList = eventDateTimeList;
	}

}

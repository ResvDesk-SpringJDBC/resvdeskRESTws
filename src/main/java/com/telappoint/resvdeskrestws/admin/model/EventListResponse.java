package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
public class EventListResponse extends BaseResponse {
	private List<Event> eventList;

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
}

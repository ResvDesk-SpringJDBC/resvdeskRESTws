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
public class EventListResponse extends BaseResponse {

	private String selectEventLabel;
	private String selectDefaultEventId;
	private List<Options> eventList;
		
	public String getSelectEventLabel() {
		return selectEventLabel;
	}

	public void setSelectEventLabel(String selectEventLabel) {
		this.selectEventLabel = selectEventLabel;
	}

	public String getSelectDefaultEventId() {
		return selectDefaultEventId;
	}

	public void setSelectDefaultEventId(String selectDefaultEventId) {
		this.selectDefaultEventId = selectDefaultEventId;
	}

	public List<Options> getEventList() {
		return eventList;
	}

	public void setEventList(List<Options> eventList) {
		this.eventList = eventList;
	}
}

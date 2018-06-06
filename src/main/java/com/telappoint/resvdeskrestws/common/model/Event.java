package com.telappoint.resvdeskrestws.common.model;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = NON_NULL)
public class Event {
    private String eventName;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
}

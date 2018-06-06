package com.telappoint.resvdeskrestws.common.model;

import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class EventTimesResponse extends BaseResponse {
	private Map<String,String> availableTimes;
	private Map<String,String> displayAvailableTimes;
	
	// ivr
	private String ivrTimeBatchSize;
	
	public Map<String,String> getAvailableTimes() {
		return availableTimes;
	}

	public void setAvailableTimes(Map<String,String> availableTimes) {
		this.availableTimes = availableTimes;
	}

	public String getIvrTimeBatchSize() {
		return ivrTimeBatchSize;
	}

	public void setIvrTimeBatchSize(String ivrTimeBatchSize) {
		this.ivrTimeBatchSize = ivrTimeBatchSize;
	}

	public Map<String,String> getDisplayAvailableTimes() {
		return displayAvailableTimes;
	}

	public void setDisplayAvailableTimes(Map<String,String> displayAvailableTimes) {
		this.displayAvailableTimes = displayAvailableTimes;
	}
}

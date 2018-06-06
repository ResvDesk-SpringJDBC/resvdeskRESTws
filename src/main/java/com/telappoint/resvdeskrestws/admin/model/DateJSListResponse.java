package com.telappoint.resvdeskrestws.admin.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class DateJSListResponse extends BaseResponse {
	
	//[key, value] - key is date, value is status of availability 
	//of that date - Y - if fully booked else N 
	private Map<String, String> dateMap = new LinkedHashMap<String,String>(); 
	private String minDate;
	private String maxDate;
	
	public String getMinDate() {
		return minDate;
	}

	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	public String getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	public Map<String, String> getDateMap() {
		return dateMap;
	}

	public void setDateMap(Map<String, String> dateMap) {
		this.dateMap = dateMap;
	}
}

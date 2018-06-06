package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class CalendarOverviewDetailsResponse extends BaseResponse {
	private List<CalendarOverviewDetails> calendarOverviewDetailsList;

	public List<CalendarOverviewDetails> getCalendarOverviewDetailsList() {
		return calendarOverviewDetailsList;
	}

	public void setCalendarOverviewDetailsList(List<CalendarOverviewDetails> calendarOverviewDetailsList) {
		this.calendarOverviewDetailsList = calendarOverviewDetailsList;
	}
}

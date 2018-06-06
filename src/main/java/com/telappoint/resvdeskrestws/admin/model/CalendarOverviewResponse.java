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
public class CalendarOverviewResponse extends BaseResponse {
	private List<CalendarOverview> calendarOverViewList;

	public List<CalendarOverview> getCalendarOverViewList() {
		return calendarOverViewList;
	}

	public void setCalendarOverViewList(List<CalendarOverview> calendarOverViewList) {
		this.calendarOverViewList = calendarOverViewList;
	}
}

package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import com.telappoint.resvdeskrestws.common.model.BaseRequest;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationReportRequest extends BaseRequest {
	private Integer locationId;
	private Long eventId;
	private String startDate;
	private String endDate;
	private String resvStatus;
	private String eventDateTimeIds;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getResvStatus() {
		return resvStatus;
	}

	public void setResvStatus(String resvStatus) {
		this.resvStatus = resvStatus;
	}

	public String getEventDateTimeIds() {
		return eventDateTimeIds;
	}

	public void setEventDateTimeIds(String eventDateTimeIds) {
		this.eventDateTimeIds = eventDateTimeIds;
	}
}

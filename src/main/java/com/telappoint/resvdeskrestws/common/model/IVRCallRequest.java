package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class IVRCallRequest extends BaseRequest {
	private Long ivrCallId;
	private Long customerId;
	private Long eventId;
	private Integer locationId;
	private Long seatId;
	private Long eventDateTimeId;
	private Long confNumber;
	private Integer apptType;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Long getSeatId() {
		return seatId;
	}

	public void setSeatId(Long seatId) {
		this.seatId = seatId;
	}

	public Long getEventDateTimeId() {
		return eventDateTimeId;
	}

	public void setEventDateTimeId(Long eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}

	public Long getConfNumber() {
		return confNumber;
	}

	public void setConfNumber(Long confNumber) {
		this.confNumber = confNumber;
	}

	public Integer getApptType() {
		return apptType;
	}

	public void setApptType(Integer apptType) {
		this.apptType = apptType;
	}

	public Long getIvrCallId() {
		return ivrCallId;
	}

	public void setIvrCallId(Long ivrCallId) {
		this.ivrCallId = ivrCallId;
	}

}

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
public class EventDateTime extends BaseRequest {
	private long eventDateTimeId;
	private long eventId;
	private int locationId;
	private String locationName;
	private String eventName;
	private String date;
	private String time;
	private String noOfSeats;
	private String enable = "Y";
	private int noOfNotifiedReservations;

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(String noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public long getEventDateTimeId() {
		return eventDateTimeId;
	}

	public void setEventDateTimeId(long eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}
	
	public int getNoOfNotifiedReservations() {
		return noOfNotifiedReservations;
	}

	public void setNoOfNotifiedReservations(int noOfNotifiedReservations) {
		this.noOfNotifiedReservations = noOfNotifiedReservations;
	}
}

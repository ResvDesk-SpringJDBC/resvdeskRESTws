package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class CalendarOverview {

	private Long eventDateTimeId;
	private String locationName;
	private String eventName;
	private String date;
	private String time;
	private String totalSeats;
	private String bookedSeats;
	private String openSeats;
	private String reservedSeats;

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
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

	public String getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(String totalSeats) {
		this.totalSeats = totalSeats;
	}

	public String getBookedSeats() {
		return bookedSeats;
	}

	public String toString() {
		System.out.println("");
		return "[CalendarView: eventName=" + eventName + ", locationName=" + locationName + ", date=" + date + ", time=" + time + ", totalSeats= " + totalSeats + ", bookedSeats="
				+ bookedSeats + "]";
	}

	public void setBookedSeats(String bookedSeats) {
		this.bookedSeats = bookedSeats;
	}

	public Long getEventDateTimeId() {
		return eventDateTimeId;
	}

	public void setEventDateTimeId(Long eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}

	public String getOpenSeats() {
		return openSeats;
	}

	public void setOpenSeats(String openSeats) {
		this.openSeats = openSeats;
	}

	public String getReservedSeats() {
		return reservedSeats;
	}

	public void setReservedSeats(String reservedSeats) {
		this.reservedSeats = reservedSeats;
	}
}

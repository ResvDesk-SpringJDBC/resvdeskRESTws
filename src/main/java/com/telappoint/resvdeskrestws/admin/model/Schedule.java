package com.telappoint.resvdeskrestws.admin.model;

/**
 * 
 * @author Balaji N
 *
 */
public class Schedule {
	private Long scheduleId;
	private Integer status;
	private Integer locationId;
	private Long eventId;
	private Long seatId;
	private Long customerId;
	private String reminderStatus;
	private Long eventDateTimeId;

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Long getSeatId() {
		return seatId;
	}

	public void setSeatId(Long seatId) {
		this.seatId = seatId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(String reminderStatus) {
		this.reminderStatus = reminderStatus;
	}

	public Long getEventDateTimeId() {
		return eventDateTimeId;
	}

	public void setEventDateTimeId(Long eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}

}

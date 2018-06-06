package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * 
 * @author Balaji N
 *
 */
public class DailyReminderView {
	private Integer totalNotifications;
	private Integer totalConfirmed;
	private Long eventId;
	private String eventName;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Integer getTotalNotifications() {
		return totalNotifications;
	}

	public void setTotalNotifications(Integer totalNotifications) {
		this.totalNotifications = totalNotifications;
	}

	public Integer getTotalConfirmed() {
		return totalConfirmed;
	}

	public void setTotalConfirmed(Integer totalConfirmed) {
		this.totalConfirmed = totalConfirmed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DailyReminderView other = (DailyReminderView) obj;
		if (eventId == null) {
			if (other.eventId != null)
				return false;
		} else if (!eventId.equals(other.eventId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		ObjectMapper ow = new ObjectMapper();
		String json = null;
		try {
			json = ow.writeValueAsString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
}

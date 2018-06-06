package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReminderStatus {
	private Long reminderStatusId;
	private String statusKey;
	private Integer notifyPhoneStatus;
	private Integer notifyStatus;
	private Integer notifySMSStatus;

	public Integer getNotifyPhoneStatus() {
		return notifyPhoneStatus;
	}

	public void setNotifyPhoneStatus(Integer notifyPhoneStatus) {
		this.notifyPhoneStatus = notifyPhoneStatus;
	}

	public Integer getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(Integer notifyStatus) {
		this.notifyStatus = notifyStatus;
	}

	public Integer getNotifySMSStatus() {
		return notifySMSStatus;
	}

	public void setNotifySMSStatus(Integer notifySMSStatus) {
		this.notifySMSStatus = notifySMSStatus;
	}

	public void setDisplayStatus(String displayStatus) {
	}

	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	public Long getReminderStatusId() {
		return reminderStatusId;
	}

	public void setReminderStatusId(Long reminderStatusId) {
		this.reminderStatusId = reminderStatusId;
	}
}

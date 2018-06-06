package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class NotifyDailyView extends ReservationDetails {
	private Long notifyId;
	private String date;
	private String time;
	private Long eventId;
	private Integer locationId;
	private String campaignName;
	private String notifyByPhoneConfirm;
	private String notifyBySMSConfirm;
	private String notifyByEmailConfirm;
	private String notifyByPushNotif;
	private Integer notifyStatus;

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

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getNotifyByPhoneConfirm() {
		return notifyByPhoneConfirm;
	}

	public void setNotifyByPhoneConfirm(String notifyByPhoneConfirm) {
		this.notifyByPhoneConfirm = notifyByPhoneConfirm;
	}

	public String getNotifyBySMSConfirm() {
		return notifyBySMSConfirm;
	}

	public void setNotifyBySMSConfirm(String notifyBySMSConfirm) {
		this.notifyBySMSConfirm = notifyBySMSConfirm;
	}

	public String getNotifyByEmailConfirm() {
		return notifyByEmailConfirm;
	}

	public void setNotifyByEmailConfirm(String notifyByEmailConfirm) {
		this.notifyByEmailConfirm = notifyByEmailConfirm;
	}

	public String getNotifyByPushNotif() {
		return notifyByPushNotif;
	}

	public void setNotifyByPushNotif(String notifyByPushNotif) {
		this.notifyByPushNotif = notifyByPushNotif;
	}

	public Integer getNotifyStatus() {
		return notifyStatus;
	}

	public void setNotifyStatus(Integer notifyStatus) {
		this.notifyStatus = notifyStatus;
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
	
	public Long getNotifyId() {
		return notifyId;
	}

	public void setNotifyId(Long notifyId) {
		this.notifyId = notifyId;
	}
}

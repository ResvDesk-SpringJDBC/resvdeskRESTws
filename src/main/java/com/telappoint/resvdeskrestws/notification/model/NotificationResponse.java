package com.telappoint.resvdeskrestws.notification.model;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class NotificationResponse extends BaseResponse {
	private String clientCode;
	private String clientName;	
	private Map<NotificationKey, List<CustomerNotification>> notificationInfo=
			new HashMap<NotificationKey, List<CustomerNotification>>();
	
	//phone reminders
	private ByteArrayOutputStream outputStream;
	
	// sms reminder
	private Map<String, String> authDetailsSMS;

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Map<NotificationKey, List<CustomerNotification>> getNotificationInfo() {
		return notificationInfo;
	}

	public void setNotificationInfo(Map<NotificationKey, List<CustomerNotification>> notificationInfo) {
		this.notificationInfo = notificationInfo;
	}

	public Map<String, String> getAuthDetailsSMS() {
		return authDetailsSMS;
	}

	public void setAuthDetailsSMS(Map<String, String> authDetailsSMS) {
		this.authDetailsSMS = authDetailsSMS;
	}

	public ByteArrayOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ByteArrayOutputStream outputStream) {
		this.outputStream = outputStream;
	}
}

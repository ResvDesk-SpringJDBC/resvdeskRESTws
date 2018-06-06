package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.logger.CustomLogger;

/**
 * 
 * @author Balaji Nandarapu
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class BaseRequest {
	private CustomLogger customLogger;
	private String clientCode;
	private String langCode;
	private String device;
	private String token;
	private String transId;
	private String userName;

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public CustomLogger getCustomLogger() {
		return customLogger;
	}

	public void setCustomLogger(CustomLogger customLogger) {
		this.customLogger = customLogger;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}

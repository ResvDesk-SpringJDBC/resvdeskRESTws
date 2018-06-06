package com.telappoint.resvdeskrestws.common.model;

/**
 * 
 * @author Balaji N
 *
 */
public class ErrorConfig {
	private int errorId;
	private String errorCode;
	private String errorMessage;
	private String errorDescription;
	private String errorVXML;
	private String sendAlert;

	public int getErrorId() {
		return errorId;
	}

	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorVXML() {
		return errorVXML;
	}

	public void setErrorVXML(String errorVXML) {
		this.errorVXML = errorVXML;
	}

	public String getSendAlert() {
		return sendAlert;
	}

	public void setSendAlert(String sendAlert) {
		this.sendAlert = sendAlert;
	}
}

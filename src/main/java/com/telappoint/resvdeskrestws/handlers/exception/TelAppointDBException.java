package com.telappoint.resvdeskrestws.handlers.exception;

/**
 * 
 * @author Balaji
 * 
 */
public class TelAppointDBException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorId;
	private String errorCode;
	private String errorMessage;
	private String errorDescription;
	private String errorVXML;
	private String sendAlert;

	public TelAppointDBException() {
		super();
	}

	public TelAppointDBException(Throwable cause) {
		super(cause);
	}

	public TelAppointDBException(String errorCode, String errorMessage, String errorDescription, Throwable cause) {
		super(errorCode,  cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorDescription = errorDescription;
	}
	
	public TelAppointDBException(String errorCode, Throwable cause) {
		super(errorCode,  cause);
		this.errorCode = errorCode;
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
	
	@Override
    public String toString() {
        return errorDescription;
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

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}
}

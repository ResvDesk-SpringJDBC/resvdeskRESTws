package com.telappoint.resvdeskrestws.common.constants;

/**
 * 
 * @author Balaji N
 *
 */
public enum PropertiesConstants {

	RESV_DESK_REST_WS_PROP("resvdeskrestws.properties"),	
	EMAIL_PROP("resvdeskmail.properties"),
	PHONE_LOG_UPLOAD_PROP("phoneLogFileUpload.properties");
	
	private String propertyFileName;
	
	private PropertiesConstants(String propertyFileName) {
		this.setPropertyFileName(propertyFileName);
	}

	public String getPropertyFileName() {
		return propertyFileName;
	}

	public void setPropertyFileName(String propertyFileName) {
		this.propertyFileName = propertyFileName;
	}
}

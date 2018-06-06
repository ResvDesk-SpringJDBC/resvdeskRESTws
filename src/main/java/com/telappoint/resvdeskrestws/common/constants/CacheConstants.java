package com.telappoint.resvdeskrestws.common.constants;

/**
 * 
 * @author Balaji N
 *
 * Cache table by below key name with client code. [key|clientCode]= any object or Map
 */
public enum CacheConstants {

	// Master db table keys
	CLIENT("CLIENT"), 
	CLIENT_DEPLOYMENT_CONFIG("CLIENT_DEPLOYMENT_CONFIG"),
	ERROR_CONFIG("ERROR_CONFIG"),
	
	// client db table keys
	RESV_SYS_CONFIG("RESV_SYS_CONFIG"),
	LOGIN_PARAM_CONFIG("LOGIN_PARAM_CONFIG"), 
	REGISTRATION_PARAM_CONFIG("REGISTRATION_PARAM_CONFIG"), 
	DISPLAY_TEMPLATE("DISPLAY_TEMPLATE"),

	DISPLAY_FIELD_LABEL("DISPLAY_FIELD_LABEL"),
	DISPLAY_PAGE_CONTENT("DISPLAY_PAGE_CONTENT"),
	EMAIL_TEMPLATE("EMAIL_TEMPLATE"),
	DISPLAY_BUTTON_NAMES("DISPLAY_BUTTON_NAMES"), 
	DISPLAY_ALIAES("DISPLAY_ALIAES"),
	DISPLAY_LANG("DISPLAY_LANG"),
	CAMPAIGN_MESSAGE_EMAIL("CAMPAIGN_MESSAGE_EMAIL"),
	CAMPAIGN_MESSAGE_SMS("CAMPAIGN_MESSAGE_SMS"),
	CAMPAIGN_MESSAGE_PHONE("CAMPAIGN_MESSAGE_PHONE"),
	
	IVR_VXML("IVR_VXML");

	private String value;

	private CacheConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

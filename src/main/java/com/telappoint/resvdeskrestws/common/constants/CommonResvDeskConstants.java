package com.telappoint.resvdeskrestws.common.constants;

/**
 * 
 * @author Balaji N
 *
 */
public enum CommonResvDeskConstants {
	VERSION("version"),
	EMPTY_STRING(""),
	ONLINE("online"), 
	MOBILE("mobile"),
	SMS("sms"),
	ADMIN("admin"),
	IVRTTS("ivr_tts"),
	IVRAUDIO("ivr"),
	EMAIL_REMINDER("email"),
	SMS_REMINDER("sms"),
	PHONE_REMINDER("phone"),

	COMMA(",");
	private String value;

	private CommonResvDeskConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}

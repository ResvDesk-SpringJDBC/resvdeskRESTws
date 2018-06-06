package com.telappoint.resvdeskrestws.common.constants;

/**
 * 
 * @author Balaji N
 *
 * 
 */
public enum EmailConstants {

	RESERVATION_CONFIRM_EMAIL("RESERVATION_CONFIRM_EMAIL"), 
	RESERVATION_CANCEL_EMAIL("RESERVATION_CANCEL_EMAIL");
	
	private String value;

	private EmailConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

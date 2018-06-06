package com.telappoint.resvdeskrestws.common.constants;

/**
 * @author Balaji
 */
public enum DisplayButtonsConstants {
	NEXT("NEXT"),
	BACK("BACK"),
	PRINT("PRINT"),
	LOGOUT("LOGOUT");

	private String value;
	private DisplayButtonsConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

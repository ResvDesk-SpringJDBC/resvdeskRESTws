package com.telappoint.resvdeskrestws.admin.constants;

/**
 * @author Balaji
 */
public enum SPConstants {
	START_DATE("START_DATE"),
	END_DATE("END_DATE"),
	RESULT_LIST("RESULT_LIST"),
	RETURN_MESSAGE("RETURN_MESSAGE"),
	RETURN_CODE("RETURN_CODE");

	private String value;

	private SPConstants(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}

package com.telappoint.resvdeskrestws.common.constants;

/**
 * @author Balaji
 */
public enum EmailTemplateConstants {

	EMAIL_RESV_CONFIRM_SUBJECT("EMAIL_RESV_CONFIRM_SUBJECT"),
	EMAIL_RESV_CONFIRM_BODY("EMAIL_RESV_CONFIRM_BODY"),
	EMAIL_RESV_CANCEL_SUBJECT("EMAIL_RESV_CANCEL_SUBJECT"),
	EMAIL_RESV_CANCEL_BODY("EMAIL_RESV_CANCEL_BODY");

	private String value;

	private EmailTemplateConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

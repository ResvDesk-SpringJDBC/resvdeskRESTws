package com.telappoint.resvdeskrestws.common.constants;

/**
 * @author Balaji
 */

public enum DesignTemplateConstants {

	CSS_FILENAME("CSS_FILENAME"),
	LOGO_TAG("LOGO_TAG"),
	FOOTER_CONTENT("FOOTER_CONTENT"),
	FOOTER_LINKS("FOOTER_LINKS"),
	LOCATION_GOOGLE_MAP("LOCATION_GOOGLE_MAP"),
	LOCATION_GOOGLE_MAP_LINK("LOCATION_GOOGLE_MAP_LINK"),
	VERSION("VERSION");
	
	

	private String value;

	private DesignTemplateConstants(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

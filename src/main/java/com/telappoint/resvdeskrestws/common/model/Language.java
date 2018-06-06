package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class Language {
	
	// ivr only
	private String langId;
	
	// ivr and online
	private String langCode;
	private char isDefault;

	// online only
	private String langName;
	private String linkDisplay;

	public Language(String langId,String langCode, String langName, char isDefault, String linkDisplay) {
		this.langCode = langCode;
		this.langName = langName;
		this.isDefault = isDefault;
		this.linkDisplay = linkDisplay;
		this.langId = langId;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	public char getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(char isDefault) {
		this.isDefault = isDefault;
	}

	public String getLinkDisplay() {
		return linkDisplay;
	}

	public void setLinkDisplay(String linkDisplay) {
		this.linkDisplay = linkDisplay;
	}

	public String getLangId() {
		return langId;
	}

	public void setLangId(String langId) {
		this.langId = langId;
	}
}

package com.telappoint.resvdeskrestws.common.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class LanguageDetails {
	private String langCode;

	private String langLink;

	private String defaultLang;

	public String getDefaultLang() {
		return defaultLang;
	}

	public void setDefaultLang(String defaultLang) {
		this.defaultLang = defaultLang;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getLangLink() {
		return langLink;
	}

	public void setLangLink(String langLink) {
		this.langLink = langLink;
	}

	public static List<LanguageDetails> createDummyData() {
		List<LanguageDetails> lanList = new ArrayList<LanguageDetails>();

		LanguageDetails languageDetails = new LanguageDetails();
		languageDetails.setLangCode("us-en");
		languageDetails.setLangLink("In English");
		languageDetails.setDefaultLang("Y");
		lanList.add(languageDetails);

		LanguageDetails languageDetails1 = new LanguageDetails();
		languageDetails1.setLangCode("us-es");
		languageDetails1.setLangLink("In spanish");
		languageDetails1.setDefaultLang("N");
		lanList.add(languageDetails1);

		return lanList;

	}

	public static class Builder {
		private String langCode;
		private String langLink;
		private String defaultLang;

		public Builder withLangCode(String langCode) {
			this.langCode = langCode;
			return this;
		}

		public Builder withLangLink(String langLink) {
			this.langLink = langLink;
			return this;
		}

		public Builder withDefaultLang(String defaultLang) {
			this.defaultLang = defaultLang;
			return this;
		}

		public LanguageDetails build() {
			LanguageDetails languageDetails = new LanguageDetails();
			languageDetails.langCode = langCode;
			languageDetails.langLink = langLink;
			languageDetails.defaultLang = defaultLang;
			return languageDetails;
		}
	}
}
package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * @author Balaji
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class Options {
	private String optionKey;
	private String optionValue;
	private String optionTTS;
	private String optionAudio;

	public String getOptionKey() {
		return optionKey;
	}

	public void setOptionKey(String optionKey) {
		this.optionKey = optionKey;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public String getOptionTTS() {
		return optionTTS;
	}

	public void setOptionTTS(String optionTTS) {
		this.optionTTS = optionTTS;
	}

	public String getOptionAudio() {
		return optionAudio;
	}

	public void setOptionAudio(String optionAudio) {
		this.optionAudio = optionAudio;
	}
}

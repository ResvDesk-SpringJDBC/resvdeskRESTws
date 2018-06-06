package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ResvVerificationDetailsRightSideContentResponse extends BaseResponse {
	
	// online field
	private String resvVerificationDetailsRightSideContent;

	public String getResvVerificationDetailsRightSideContent() {
		return resvVerificationDetailsRightSideContent;
	}

	public void setResvVerificationDetailsRightSideContent(String resvVerificationDetailsRightSideContent) {
		this.resvVerificationDetailsRightSideContent = resvVerificationDetailsRightSideContent;
	}
}

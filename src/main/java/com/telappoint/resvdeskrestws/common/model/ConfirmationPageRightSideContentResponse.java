package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ConfirmationPageRightSideContentResponse extends BaseResponse {
	
	// online field
	private String confirmationPageRightSideContent;

	public String getConfirmationPageRightSideContent() {
		return confirmationPageRightSideContent;
	}

	public void setConfirmationPageRightSideContent(String confirmationPageRightSideContent) {
		this.confirmationPageRightSideContent = confirmationPageRightSideContent;
	}
}

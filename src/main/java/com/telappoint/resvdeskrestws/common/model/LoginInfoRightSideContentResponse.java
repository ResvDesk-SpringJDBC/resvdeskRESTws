package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class LoginInfoRightSideContentResponse extends BaseResponse {
	
	// online field
	private String loginInfoRightSideContent;

	public String getLoginInfoRightSideContent() {
		return loginInfoRightSideContent;
	}

	public void setLoginInfoRightSideContent(String loginInfoRightSideContent) {
		this.loginInfoRightSideContent = loginInfoRightSideContent;
	}
}

package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * @author Balaji Nandarapu
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class AuthResponse extends BaseResponse {
	
	private boolean isAuthSuccess;
	private String authMessage="";
	private long customerId=(long)-1;
	private String firstName="";
	private String lastName="";
	
	// online
	private String welcomeHeader;

	public boolean isAuthSuccess() {
		return isAuthSuccess;
	}

	public void setAuthSuccess(boolean isAuthSuccess) {
		this.isAuthSuccess = isAuthSuccess;
	}

	public String getAuthMessage() {
		return authMessage;
	}

	public void setAuthMessage(String authMessage) {
		this.authMessage = authMessage;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getWelcomeHeader() {
		return welcomeHeader;
	}

	public void setWelcomeHeader(String welcomeHeader) {
		this.welcomeHeader = welcomeHeader;
	}

}

package com.telappoint.resvdeskrestws.common.externalservice.client;


/**
 * 
 * @author Balaji N
 *
 */

public class Carrier {
	private String type;
	private String mobile_country_code;
	private String name;
	private String mobile_network_code;
	private String error_code;
	

	public String getMobile_country_code() {
		return mobile_country_code;
	}

	public void setMobile_country_code(String mobile_country_code) {
		this.mobile_country_code = mobile_country_code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile_network_code() {
		return mobile_network_code;
	}

	public void setMobile_network_code(String mobile_network_code) {
		this.mobile_network_code = mobile_network_code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
}

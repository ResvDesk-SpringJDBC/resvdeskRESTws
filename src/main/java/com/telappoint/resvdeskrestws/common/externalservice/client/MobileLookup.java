package com.telappoint.resvdeskrestws.common.externalservice.client;


/**
 * 
 * @author Balaji
 *
 */

public class MobileLookup {
	private String country_code;
	private String phone_number;
	private String national_format;
	private Carrier carrier;
	private String url;

	public Carrier getCarrier() {
		return carrier;
	}

	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getNational_format() {
		return national_format;
	}

	public void setNational_format(String national_format) {
		this.national_format = national_format;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

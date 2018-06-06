package com.telappoint.resvdeskrestws.common.model;

/**
 * 
 * @author Balaji N
 *
 */
public class ConfPageContactDetailsResponse extends BaseResponse {
	
	private String rightSideContactDetailsHeader;
	private String fax;
	private String address;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String email;
	private String website;
	private String emailLabel;
	private String websiteLabel;
	private String workPhone;
	private String contactDetails;
	
	private String locationGoogleMap;
	private String locationGoogleMapLink;

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocationGoogleMap() {
		return locationGoogleMap;
	}

	public void setLocationGoogleMap(String locationGoogleMap) {
		this.locationGoogleMap = locationGoogleMap;
	}

	public String getLocationGoogleMapLink() {
		return locationGoogleMapLink;
	}

	public void setLocationGoogleMapLink(String locationGoogleMapLink) {
		this.locationGoogleMapLink = locationGoogleMapLink;
	}

	public String getRightSideContactDetailsHeader() {
		return rightSideContactDetailsHeader;
	}

	public void setRightSideContactDetailsHeader(String rightSideContactDetailsHeader) {
		this.rightSideContactDetailsHeader = rightSideContactDetailsHeader;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsiteLabel() {
		return websiteLabel;
	}

	public void setWebsiteLabel(String websiteLabel) {
		this.websiteLabel = websiteLabel;
	}

	public String getEmailLabel() {
		return emailLabel;
	}

	public void setEmailLabel(String emailLabel) {
		this.emailLabel = emailLabel;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

}

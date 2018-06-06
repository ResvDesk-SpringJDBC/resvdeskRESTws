package com.telappoint.resvdeskrestws.common.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ClientInfo extends BaseResponse {

	private String clientCode;
	private String clientName;
	private String website;
	private String fax;
	private String address;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;

	private String loginFirst;
	private String langCode;
	public List<Language> languageList;
	private String cssFileName;
	private String logoFileName;
	private String footerContent;
	private String footerLinks;
	private String version;

	private String leftSideLoginHeader;
	private String leftSideResvDetailsHeader;
	private String leftSideResvVerifyHeader;
	private String leftSideConfirmHeader;

	private String schedulerClosed;
	private String noFunding;
	private String landingPageText;

	private String closedPageHeaderTextLegend;
	private String closedLandingPageTextLegend;
	private String noFundingPageHeaderTextLegend;
	private String noFundingPageTextLegend;
	private String viewExistingResvLabel;
	// ivr
	private String directAccessNumber; 

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

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

	public String getLoginFirst() {
		return loginFirst;
	}

	public void setLoginFirst(String loginFirst) {
		this.loginFirst = loginFirst;
	}

	public String getCssFileName() {
		return cssFileName;
	}

	public void setCssFileName(String cssFileName) {
		this.cssFileName = cssFileName;
	}

	public String getLogoFileName() {
		return logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}

	public String getFooterContent() {
		return footerContent;
	}

	public void setFooterContent(String footerContent) {
		this.footerContent = footerContent;
	}

	public String getFooterLinks() {
		return footerLinks;
	}

	public void setFooterLinks(String footerLinks) {
		this.footerLinks = footerLinks;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLeftSideLoginHeader() {
		return leftSideLoginHeader;
	}

	public void setLeftSideLoginHeader(String leftSideLoginHeader) {
		this.leftSideLoginHeader = leftSideLoginHeader;
	}

	public String getLeftSideResvDetailsHeader() {
		return leftSideResvDetailsHeader;
	}

	public void setLeftSideResvDetailsHeader(String leftSideResvDetailsHeader) {
		this.leftSideResvDetailsHeader = leftSideResvDetailsHeader;
	}

	public String getLeftSideResvVerifyHeader() {
		return leftSideResvVerifyHeader;
	}

	public void setLeftSideResvVerifyHeader(String leftSideResvVerifyHeader) {
		this.leftSideResvVerifyHeader = leftSideResvVerifyHeader;
	}

	public String getLeftSideConfirmHeader() {
		return leftSideConfirmHeader;
	}

	public void setLeftSideConfirmHeader(String leftSideConfirmHeader) {
		this.leftSideConfirmHeader = leftSideConfirmHeader;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public List<Language> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(List<Language> languageList) {
		this.languageList = languageList;
	}

	public String getSchedulerClosed() {
		return schedulerClosed;
	}

	public void setSchedulerClosed(String schedulerClosed) {
		this.schedulerClosed = schedulerClosed;
	}

	public String getNoFunding() {
		return noFunding;
	}

	public void setNoFunding(String noFunding) {
		this.noFunding = noFunding;
	}

	public String getClosedPageHeaderTextLegend() {
		return closedPageHeaderTextLegend;
	}

	public void setClosedPageHeaderTextLegend(String closedPageHeaderTextLegend) {
		this.closedPageHeaderTextLegend = closedPageHeaderTextLegend;
	}

	public String getClosedLandingPageTextLegend() {
		return closedLandingPageTextLegend;
	}

	public void setClosedLandingPageTextLegend(String closedLandingPageTextLegend) {
		this.closedLandingPageTextLegend = closedLandingPageTextLegend;
	}

	public String getNoFundingPageHeaderTextLegend() {
		return noFundingPageHeaderTextLegend;
	}

	public void setNoFundingPageHeaderTextLegend(String noFundingPageHeaderTextLegend) {
		this.noFundingPageHeaderTextLegend = noFundingPageHeaderTextLegend;
	}

	public String getNoFundingPageTextLegend() {
		return noFundingPageTextLegend;
	}

	public void setNoFundingPageTextLegend(String noFundingPageTextLegend) {
		this.noFundingPageTextLegend = noFundingPageTextLegend;
	}

	public String getViewExistingResvLabel() {
		return viewExistingResvLabel;
	}

	public void setViewExistingResvLabel(String viewExistingResvLabel) {
		this.viewExistingResvLabel = viewExistingResvLabel;
	}

	public String getDirectAccessNumber() {
		return directAccessNumber;
	}

	public void setDirectAccessNumber(String directAccessNumber) {
		this.directAccessNumber = directAccessNumber;
	}

	public String getLandingPageText() {
		return landingPageText;
	}

	public void setLandingPageText(String landingPageText) {
		this.landingPageText = landingPageText;
	}

}

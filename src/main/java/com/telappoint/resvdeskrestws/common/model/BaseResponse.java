package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji Nandarapu
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class BaseResponse {

	private String transId;
	private String token;
	
	//online
	private String nextBtn;
	private String backBtn;
	private String printBtn;
	private String logoutBtn;

	// ivr
	private String pageName;
	private String vxml;
	private String pageAudio;
	private String pageTTS;
	private String errorvxml;

	/**
	 * if true - success, lets go forward, else, stop and show the response
	 * message in front end page. eg: availability for seats, times and dates
	 * etc.
	 */
	private boolean responseStatus = true;
	private String responseMessage;

	/**
	 * errorStatus: <i>if true</i>, basically in back end error, unrecoverable,
	 * need to be filled errorCode,errorMessage and errorDescription. so front
	 * will be shown error page. eg: any exception it causes functionality
	 * failure. front as to check first errorStatus, <i>if false</i>, then check
	 * responseStatus value.
	 */
	// online and ivr
	private boolean errorStatus;
	
	// This will be shown in front screen.
	private String errorMessage;

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getVxml() {
		return vxml;
	}

	public void setVxml(String vxml) {
		this.vxml = vxml;
	}

	public String getPageAudio() {
		return pageAudio;
	}

	public void setPageAudio(String pageAudio) {
		this.pageAudio = pageAudio;
	}

	public String getPageTTS() {
		return pageTTS;
	}

	public void setPageTTS(String pageTTS) {
		this.pageTTS = pageTTS;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(boolean errorStatus) {
		this.errorStatus = errorStatus;
	}

	public boolean isResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(boolean responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getErrorvxml() {
		return errorvxml;
	}

	public void setErrorvxml(String errorvxml) {
		this.errorvxml = errorvxml;
	}

	public String getNextBtn() {
		return nextBtn;
	}

	public void setNextBtn(String nextBtn) {
		this.nextBtn = nextBtn;
	}

	public String getBackBtn() {
		return backBtn;
	}

	public void setBackBtn(String backBtn) {
		this.backBtn = backBtn;
	}

	public String getPrintBtn() {
		return printBtn;
	}

	public void setPrintBtn(String printBtn) {
		this.printBtn = printBtn;
	}

	public String getLogoutBtn() {
		return logoutBtn;
	}

	public void setLogoutBtn(String logoutBtn) {
		this.logoutBtn = logoutBtn;
	}
}

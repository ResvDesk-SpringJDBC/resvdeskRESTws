package com.telappoint.resvdeskrestws.common.model;

/**
 * 
 * @author Balaji N
 *
 */
public class ResvSysConfig {
	
	private String maxApptDurationDays;
	private String displayCompany;
	private String displayProcedure;
	private String displayLocation;
	private String displayDepartment;
	private String displayEvent;
	private String displaySeat;
	private String enforceLogin;
	private String loginFirst;
	private String sendConfirmEmail;
	private String sendCancelEmail;
	private String schedulerClosed;
	private String runPhoneTypeLookup;
	//ivr
	private String ivrTimeBatchSize;
	private String recordMsg;
	private String noFunding;
	private String displayComments;
	private String commentsNoOfRows;
	private String commentsNoOfCols;
	

	public String getDisplayCompany() {
		return displayCompany;
	}

	public void setDisplayCompany(String displayCompany) {
		this.displayCompany = displayCompany;
	}

	public String getDisplayProcedure() {
		return displayProcedure;
	}

	public void setDisplayProcedure(String displayProcedure) {
		this.displayProcedure = displayProcedure;
	}

	public String getDisplayLocation() {
		return displayLocation;
	}

	public void setDisplayLocation(String displayLocation) {
		this.displayLocation = displayLocation;
	}

	public String getDisplayDepartment() {
		return displayDepartment;
	}

	public void setDisplayDepartment(String displayDepartment) {
		this.displayDepartment = displayDepartment;
	}

	public String getDisplayEvent() {
		return displayEvent;
	}

	public void setDisplayEvent(String displayEvent) {
		this.displayEvent = displayEvent;
	}

	public String getDisplaySeat() {
		return displaySeat;
	}

	public void setDisplaySeat(String displaySeat) {
		this.displaySeat = displaySeat;
	}

	public String getEnforceLogin() {
		return enforceLogin;
	}

	public void setEnforceLogin(String enforceLogin) {
		this.enforceLogin = enforceLogin;
	}

	public String getLoginFirst() {
		return loginFirst;
	}

	public void setLoginFirst(String loginFirst) {
		this.loginFirst = loginFirst;
	}

	public String getSendConfirmEmail() {
		return sendConfirmEmail;
	}

	public void setSendConfirmEmail(String sendConfirmEmail) {
		this.sendConfirmEmail = sendConfirmEmail;
	}

	public String getSendCancelEmail() {
		return sendCancelEmail;
	}

	public void setSendCancelEmail(String sendCancelEmail) {
		this.sendCancelEmail = sendCancelEmail;
	}

	public String getIvrTimeBatchSize() {
		return ivrTimeBatchSize;
	}

	public void setIvrTimeBatchSize(String ivrTimeBatchSize) {
		this.ivrTimeBatchSize = ivrTimeBatchSize;
	}

	public String getRecordMsg() {
		return recordMsg;
	}

	public void setRecordMsg(String recordMsg) {
		this.recordMsg = recordMsg;
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

	public String getDisplayComments() {
		return displayComments;
	}

	public void setDisplayComments(String displayComments) {
		this.displayComments = displayComments;
	}

	public String getCommentsNoOfRows() {
		return commentsNoOfRows;
	}

	public void setCommentsNoOfRows(String commentsNoOfRows) {
		this.commentsNoOfRows = commentsNoOfRows;
	}

	public String getCommentsNoOfCols() {
		return commentsNoOfCols;
	}

	public void setCommentsNoOfCols(String commentsNoOfCols) {
		this.commentsNoOfCols = commentsNoOfCols;
	}

	public String getMaxApptDurationDays() {
		return maxApptDurationDays;
	}

	public void setMaxApptDurationDays(String maxApptDurationDays) {
		this.maxApptDurationDays = maxApptDurationDays;
	}

	public String getRunPhoneTypeLookup() {
		return runPhoneTypeLookup;
	}

	public void setRunPhoneTypeLookup(String runPhoneTypeLookup) {
		this.runPhoneTypeLookup = runPhoneTypeLookup;
	}
}

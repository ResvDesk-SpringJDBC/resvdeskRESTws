package com.telappoint.resvdeskrestws.common.model;

import java.util.List;

/**
 * 
 * @author Balaji N
 *
 */
public class EventHistoryResponse extends BaseResponse {

	private String rightSideEventHeader;
	private String displayCompanyLabel;
	private String displayProcedureLabel;
	private String displayLocationLabel;
	private String displayDepartmentLabel;
	private String displayEventLabel;
	private String dateLabel;
	private String timeLabel;
	private String displaySeatLabel;
	
	private String displayCompany;
	private String displayProcedure;
	private String displayLocation;
	private String displayDepartment;
	private String displayEvent;
	private String displaySeat;
	
	private List<EventHistory> eventHisory;

	public List<EventHistory> getEventHisory() {
		return eventHisory;
	}

	public void setEventHisory(List<EventHistory> eventHisory) {
		this.eventHisory = eventHisory;
	}

	public String getRightSideEventHeader() {
		return rightSideEventHeader;
	}

	public void setRightSideEventHeader(String rightSideEventHeader) {
		this.rightSideEventHeader = rightSideEventHeader;
	}

	public String getDisplayCompanyLabel() {
		return displayCompanyLabel;
	}

	public void setDisplayCompanyLabel(String displayCompanyLabel) {
		this.displayCompanyLabel = displayCompanyLabel;
	}

	public String getDisplayProcedureLabel() {
		return displayProcedureLabel;
	}

	public void setDisplayProcedureLabel(String displayProcedureLabel) {
		this.displayProcedureLabel = displayProcedureLabel;
	}

	public String getDisplayLocationLabel() {
		return displayLocationLabel;
	}

	public void setDisplayLocationLabel(String displayLocationLabel) {
		this.displayLocationLabel = displayLocationLabel;
	}

	public String getDisplayDepartmentLabel() {
		return displayDepartmentLabel;
	}

	public void setDisplayDepartmentLabel(String displayDepartmentLabel) {
		this.displayDepartmentLabel = displayDepartmentLabel;
	}

	public String getDisplayEventLabel() {
		return displayEventLabel;
	}

	public void setDisplayEventLabel(String displayEventLabel) {
		this.displayEventLabel = displayEventLabel;
	}

	public String getDateLabel() {
		return dateLabel;
	}

	public void setDateLabel(String dateLabel) {
		this.dateLabel = dateLabel;
	}

	public String getTimeLabel() {
		return timeLabel;
	}

	public void setTimeLabel(String timeLabel) {
		this.timeLabel = timeLabel;
	}

	public String getDisplaySeatLabel() {
		return displaySeatLabel;
	}

	public void setDisplaySeatLabel(String displaySeatLabel) {
		this.displaySeatLabel = displaySeatLabel;
	}

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
}

package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * @author Balaji Nandarapu
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ResvDetailsResponse extends BaseResponse {
	private String displayCompany;
	private String displayCompanyLabel;
	private String selectDefaultCompanyId;
	private String selectCompanyLabel;

	private String displayProcedure;
	private String displayProcedureLabel;
	private String selectDefaultProcedureId;
	private String selectProcedureLabel;

	private String displayLocation;
	private String displayLocationLabel;
	private String selectDefaultLocationId;
	private String selectLocationLabel;

	private String displayDepartment;
	private String displayDepartmentLabel;
	private String selectDefaultDepartmentId;
	private String selectDepartmentLabel;

	private String displayEvent;
	private String displayEventLabel;
	private String selectDefaultEventId;
	private String selectEventLabel;

	private String dateLabel;
	private String selectDateLabel;

	private String timeLabel;
	private String selectTimeLabel;
	private String selectDefaultTimetId;

	private String displaySeat;
	private String displaySeatLabel;
	private String selectDefaultSeatId;
	private String selectSeatLabel;

	public String getDisplayCompany() {
		return displayCompany;
	}

	public void setDisplayCompany(String displayCompany) {
		this.displayCompany = displayCompany;
	}

	public String getDisplayCompanyLabel() {
		return displayCompanyLabel;
	}

	public void setDisplayCompanyLabel(String displayCompanyLabel) {
		this.displayCompanyLabel = displayCompanyLabel;
	}

	public String getSelectDefaultCompanyId() {
		return selectDefaultCompanyId;
	}

	public void setSelectDefaultCompanyId(String selectDefaultCompanyId) {
		this.selectDefaultCompanyId = selectDefaultCompanyId;
	}

	public String getSelectCompanyLabel() {
		return selectCompanyLabel;
	}

	public void setSelectCompanyLabel(String selectCompanyLabel) {
		this.selectCompanyLabel = selectCompanyLabel;
	}

	public String getDisplayProcedure() {
		return displayProcedure;
	}

	public void setDisplayProcedure(String displayProcedure) {
		this.displayProcedure = displayProcedure;
	}

	public String getDisplayProcedureLabel() {
		return displayProcedureLabel;
	}

	public void setDisplayProcedureLabel(String displayProcedureLabel) {
		this.displayProcedureLabel = displayProcedureLabel;
	}

	public String getSelectDefaultProcedureId() {
		return selectDefaultProcedureId;
	}

	public void setSelectDefaultProcedureId(String selectDefaultProcedureId) {
		this.selectDefaultProcedureId = selectDefaultProcedureId;
	}

	public String getSelectProcedureLabel() {
		return selectProcedureLabel;
	}

	public void setSelectProcedureLabel(String selectProcedureLabel) {
		this.selectProcedureLabel = selectProcedureLabel;
	}

	public String getDisplayLocation() {
		return displayLocation;
	}

	public void setDisplayLocation(String displayLocation) {
		this.displayLocation = displayLocation;
	}

	public String getDisplayLocationLabel() {
		return displayLocationLabel;
	}

	public void setDisplayLocationLabel(String displayLocationLabel) {
		this.displayLocationLabel = displayLocationLabel;
	}

	public String getSelectDefaultLocationId() {
		return selectDefaultLocationId;
	}

	public void setSelectDefaultLocationId(String selectDefaultLocationId) {
		this.selectDefaultLocationId = selectDefaultLocationId;
	}

	public String getSelectLocationLabel() {
		return selectLocationLabel;
	}

	public void setSelectLocationLabel(String selectLocationLabel) {
		this.selectLocationLabel = selectLocationLabel;
	}

	public String getDisplayDepartment() {
		return displayDepartment;
	}

	public void setDisplayDepartment(String displayDepartment) {
		this.displayDepartment = displayDepartment;
	}

	public String getDisplayDepartmentLabel() {
		return displayDepartmentLabel;
	}

	public void setDisplayDepartmentLabel(String displayDepartmentLabel) {
		this.displayDepartmentLabel = displayDepartmentLabel;
	}

	public String getSelectDefaultDepartmentId() {
		return selectDefaultDepartmentId;
	}

	public void setSelectDefaultDepartmentId(String selectDefaultDepartmentId) {
		this.selectDefaultDepartmentId = selectDefaultDepartmentId;
	}

	public String getSelectDepartmentLabel() {
		return selectDepartmentLabel;
	}

	public void setSelectDepartmentLabel(String selectDepartmentLabel) {
		this.selectDepartmentLabel = selectDepartmentLabel;
	}

	public String getDisplayEvent() {
		return displayEvent;
	}

	public void setDisplayEvent(String displayEvent) {
		this.displayEvent = displayEvent;
	}

	public String getDisplayEventLabel() {
		return displayEventLabel;
	}

	public void setDisplayEventLabel(String displayEventLabel) {
		this.displayEventLabel = displayEventLabel;
	}

	public String getSelectDefaultEventId() {
		return selectDefaultEventId;
	}

	public void setSelectDefaultEventId(String selectDefaultEventId) {
		this.selectDefaultEventId = selectDefaultEventId;
	}

	public String getSelectEventLabel() {
		return selectEventLabel;
	}

	public void setSelectEventLabel(String selectEventLabel) {
		this.selectEventLabel = selectEventLabel;
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

	public String getSelectTimeLabel() {
		return selectTimeLabel;
	}

	public void setSelectTimeLabel(String selectTimeLabel) {
		this.selectTimeLabel = selectTimeLabel;
	}

	public String getSelectDateLabel() {
		return selectDateLabel;
	}

	public void setSelectDateLabel(String selectDateLabel) {
		this.selectDateLabel = selectDateLabel;
	}

	public String getDisplaySeat() {
		return displaySeat;
	}

	public void setDisplaySeat(String displaySeat) {
		this.displaySeat = displaySeat;
	}

	public String getDisplaySeatLabel() {
		return displaySeatLabel;
	}

	public void setDisplaySeatLabel(String displaySeatLabel) {
		this.displaySeatLabel = displaySeatLabel;
	}

	public String getSelectDefaultSeatId() {
		return selectDefaultSeatId;
	}

	public void setSelectDefaultSeatId(String selectDefaultSeatId) {
		this.selectDefaultSeatId = selectDefaultSeatId;
	}

	public String getSelectSeatLabel() {
		return selectSeatLabel;
	}

	public void setSelectSeatLabel(String selectSeatLabel) {
		this.selectSeatLabel = selectSeatLabel;
	}

	public String getSelectDefaultTimetId() {
		return selectDefaultTimetId;
	}

	public void setSelectDefaultTimetId(String selectDefaultTimetId) {
		this.selectDefaultTimetId = selectDefaultTimetId;
	}
}

package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationDetails {
	private String firstName;
	private String lastName;
	private String homePhone;
	private String contactPhone;
	private String workPhone;
	private String cellPhone;
	private String accountNumber;
	private String status;
	private Long scheduleId;
	private Long customerId;
	private String email;
	private String eventDateTime;
	private String companyName;
	private String procedureName;
	private String locationName;
	private String departmentName;
	private String eventName;
	private String seatNumber;
	private String displaySeatNumber;
	private String confNumber;
	private String message;
	private String timeZone;
	private String displayDate;
	private String displayTime;
	private String fileFullPath = "#";
	private String attrib1;
	
	private String displayCompanyLabel;
	private String displayProcedureLabel;
	private String displayLocationLabel;
	private String displayDepartmentLabel;
	private String displayEventLabel;
	private String dateLabel;
	private String timeLabel;
	private String displaySeatLabel;
	private String confirmationNoLabel;
	private String nameLabel;
	
	private String displayCompany;
	private String displayProcedure;
	private String displayLocation;
	private String displayDepartment;
	private String displayEvent;
	private String displaySeat;
	private Integer duration;
	
	private String screened;
	
	//Remainder Icons Related
	private String notifyByPhone;
	private Integer notifyPhoneStatus;
	private String notifyBySMS;
	private Integer notifySMSStatus;
	private String notifyByEmail;
	private Integer notifyEmailStatus;
	private Integer attemptCount;
	
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

	public String getConfNumber() {
		return confNumber;
	}

	public void setConfNumber(String confNumber) {
		this.confNumber = confNumber;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getDisplayDate() {
		return displayDate;
	}

	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
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

	public String getConfirmationNoLabel() {
		return confirmationNoLabel;
	}

	public void setConfirmationNoLabel(String confirmationNoLabel) {
		this.confirmationNoLabel = confirmationNoLabel;
	}

	public String getNameLabel() {
		return nameLabel;
	}

	public void setNameLabel(String nameLabel) {
		this.nameLabel = nameLabel;
	}

	public String getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(String displayTime) {
		this.displayTime = displayTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(String eventDateTime) {
		this.eventDateTime = eventDateTime;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getDisplaySeatNumber() {
		return displaySeatNumber;
	}

	public void setDisplaySeatNumber(String displaySeatNumber) {
		this.displaySeatNumber = displaySeatNumber;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}

	public String getScreened() {
		return screened;
	}

	public void setScreened(String screened) {
		this.screened = screened;
	}

	public String getNotifyByPhone() {
		return notifyByPhone;
	}

	public void setNotifyByPhone(String notifyByPhone) {
		this.notifyByPhone = notifyByPhone;
	}

	public Integer getNotifyPhoneStatus() {
		return notifyPhoneStatus;
	}

	public void setNotifyPhoneStatus(Integer notifyPhoneStatus) {
		this.notifyPhoneStatus = notifyPhoneStatus;
	}

	public String getNotifyBySMS() {
		return notifyBySMS;
	}

	public void setNotifyBySMS(String notifyBySMS) {
		this.notifyBySMS = notifyBySMS;
	}

	public Integer getNotifySMSStatus() {
		return notifySMSStatus;
	}

	public void setNotifySMSStatus(Integer notifySMSStatus) {
		this.notifySMSStatus = notifySMSStatus;
	}

	public String getNotifyByEmail() {
		return notifyByEmail;
	}

	public void setNotifyByEmail(String notifyByEmail) {
		this.notifyByEmail = notifyByEmail;
	}

	public Integer getNotifyEmailStatus() {
		return notifyEmailStatus;
	}

	public void setNotifyEmailStatus(Integer notifyEmailStatus) {
		this.notifyEmailStatus = notifyEmailStatus;
	}

	public Integer getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(Integer attemptCount) {
		this.attemptCount = attemptCount;
	}

	public String getAttrib1() {
		return attrib1;
	}

	public void setAttrib1(String attrib1) {
		this.attrib1 = attrib1;
	}
}

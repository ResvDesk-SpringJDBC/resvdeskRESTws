package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ConfirmResvResponse extends BaseResponse {
	private String firstName;
	private String lastName;
	private String email;
	private String homePhone;
	private String reservationDate;
	private String reservationTime;
	
	// only internal purpose.
	private String dbReservationDate;
	private String dbReservationTime;
	
	private String companyName;
	private String procedure;
	private String location;
	private String department;
	private String event;
	private String seatNumber;
	private String confNumber;
	private String message;
	private String timeZone;
	private String displayDate;
	private String displayTime;
	
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
	private int duration;
	

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

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public String getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(String reservationTime) {
		this.reservationTime = reservationTime;
	}

	public String getProcedure() {
		return procedure;
	}

	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getDbReservationDate() {
		return dbReservationDate;
	}

	public void setDbReservationDate(String dbReservationDate) {
		this.dbReservationDate = dbReservationDate;
	}

	public String getDbReservationTime() {
		return dbReservationTime;
	}

	public void setDbReservationTime(String dbReservationTime) {
		this.dbReservationTime = dbReservationTime;
	}
}

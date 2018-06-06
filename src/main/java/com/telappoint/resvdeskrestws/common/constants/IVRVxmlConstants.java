package com.telappoint.resvdeskrestws.common.constants;

/**
 * @author Balaji
 */
public enum IVRVxmlConstants {

	WELCOME("WELCOME", "welcome"),
	LOGIN("LOGIN", "login"), //FOR IVR AUDIO FOR BOOKING
	LOGIN_CANCEL("LOGIN_CANCEL","login_cancel"), // IVR LOGIN CANCEL.
	
	SINGLE_COMPANY("SINGLE_COMPANY", "single_company"),
	COMPANY("COMPANY", "company"),	
	SINGLE_PROCEDURE("SINGLE_PROCEDURE", "single_procedure"),
	PROCEDURE("PROCEDURE", "procedure"),
	SINGLE_LOCATION("SINGLE_LOCATION", "single_location"),
	LOCATION("LOCATION", "location"),
	SINGLE_DEPARTMENT("SINGLE_DEPARTMENT", "single_department"),
	DEPARTMENT("DEPARTMENT", "department"),
	SINGLE_EVENT("SINGLE_EVENT", "single_event"),
	EVENT("EVENT", "event"),
	SINGLE_DATE("SINGLE_DATE","single_date"),
	TWO_DATE("TWO_DATE","two_date"),
	MULTI_DATE("MULTI_DATE","multi_date"),
	SINGLE_TIME("SINGLE_TIME","single_time"),
	MULTI_TIME("MULTI_TIME","multi_time"),
	SEAT("SEAT","seat"),
	NAME_RECORD("NAME_RECORD","name_record"),
	ONE_SEAT_NOT_AVAILABLE("ONE_SEAT_NOT_AVAILABLE","1_seat_not_available"),
	ALL_SEATS_NOT_AVAILABLE("ALL_SEATS_NOT_AVAILABLE","all_seats_not_available"),
	CONFIRM_RESERVATION("CONFIRM_RESERVATION","confirmation"),
	TOKEN_EXPIRY("TOKEN_EXPIRY","token_expiry"),
	CANCEL_RESERVATION("CANCEL_RESERVATION","cancel_reservation"), // this need to return on getReservationEvents, this called existing booked reservations.
	CANCEL_VERIFY("CANCEL_VERFIY","cancel_verify"),
	CANCEL_CONFIRM("CANCEL_CONFIRM","cancel_confirm"),
	CANCEL_NO_RESERVATION("CANCEL_NO_RESERVATION","cancel_no_resv"),
	
	UPDATE_RECORD_VXML("UPDATE_RECORD_VXML","update_record_vxml"),
	LIST_OF_THINGS_TO_BRING("LIST_OF_THINGS_TO_BRING","list_of_things_to_bring"),
	SPECIFIC_DATE("SPECIFIC_DATE","specific_date"),
	DISCONNECT("DISCONNECT","disconnect"),
	REJECT_DUPLICATION_RESV("REJECT_DUPLICATION_RESV","reject_duplicate"),
	
	NO_APPT_ALL_LOC("NO_APPT_ALL_LOC","no_appt_all_loc"),
	NO_APPT_SELECTED_LOCATION("NO_APPT_SELECTED_LOC","no_appt_selected_loc"),
	NO_APPT_SELECTED_EVENT("NO_APPT_SELECTED_EVENT","no_appt_selected_event"),
	NO_APPT_SELECTED_DATE("NO_APPT_SELECTED_DATE","no_appt_selected_date"),
	NO_APPT_SELECTED_TIME("NO_APPT_SELECTED_TIME","no_appt_selected_time"),
	
	SCHEDULE_CLOSED("SCHEDULE_CLOSED","scheduler_closed"),
	NO_FUNDING("NO_FUNDING","no_funding"),

	APP_CODE("APP_CODE","resv");
	
	private String pageKey;
	private String pageValue;

	private IVRVxmlConstants(String pageKey, String pageValue) {
		this.pageKey = pageKey;
		this.pageValue = pageValue;
	}

	public String getPageKey() {
		return pageKey;
	}

	public void setPageKey(String pageKey) {
		this.pageKey = pageKey;
	}

	public String getPageValue() {
		return pageValue;
	}

	public void setPageValue(String pageValue) {
		this.pageValue = pageValue;
	}

}

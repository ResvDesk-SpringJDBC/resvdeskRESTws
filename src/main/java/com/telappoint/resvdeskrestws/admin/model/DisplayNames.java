package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class DisplayNames {
	private String locationName;
	private String locationsName;
	private String locationSelect;
	private String eventName;
	private String eventsName;
	private String eventSelect;
	private String seatName;
	private String seatsName;
	private String seatSelect;

	private String customerName;
	private String customersName;
	private String customerSelect;

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationsName() {
		return locationsName;
	}

	public void setLocationsName(String locationsName) {
		this.locationsName = locationsName;
	}

	public String getLocationSelect() {
		return locationSelect;
	}

	public void setLocationSelect(String locationSelect) {
		this.locationSelect = locationSelect;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventsName() {
		return eventsName;
	}

	public void setEventsName(String eventsName) {
		this.eventsName = eventsName;
	}

	public String getEventSelect() {
		return eventSelect;
	}

	public void setEventSelect(String eventSelect) {
		this.eventSelect = eventSelect;
	}

	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}

	public String getSeatsName() {
		return seatsName;
	}

	public void setSeatsName(String seatsName) {
		this.seatsName = seatsName;
	}

	public String getSeatSelect() {
		return seatSelect;
	}

	public void setSeatSelect(String seatSelect) {
		this.seatSelect = seatSelect;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomersName() {
		return customersName;
	}

	public void setCustomersName(String customersName) {
		this.customersName = customersName;
	}

	public String getCustomerSelect() {
		return customerSelect;
	}

	public void setCustomerSelect(String customerSelect) {
		this.customerSelect = customerSelect;
	}
}

package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class SeatView {
	private String seatId;
	private String seatNumber;
	private String reserved="N";
	private String booked="N";
	private ReservationDetails reservationDetails;
	private String eventDateTimeId;

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}


	public String getBooked() {
		return booked;
	}

	public void setBooked(String booked) {
		this.booked = booked;
	}

	public String getEventDateTimeId() {
		return eventDateTimeId;
	}

	public void setEventDateTimeId(String eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}

	public ReservationDetails getReservationDetails() {
		return reservationDetails;
	}

	public void setReservationDetails(ReservationDetails reservationDetails) {
		this.reservationDetails = reservationDetails;
	}
}

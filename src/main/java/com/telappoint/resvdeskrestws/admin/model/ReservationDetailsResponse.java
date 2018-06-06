package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationDetailsResponse extends BaseResponse {
	private ReservationDetails reservationDetails;

	public ReservationDetails getReservationDetails() {
		return reservationDetails;
	}

	public void setReservationDetails(ReservationDetails reservationDetails) {
		this.reservationDetails = reservationDetails;
	}

}

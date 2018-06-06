package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationSearchResponse extends BaseResponse {
	private List<ReservationDetails> reservationDetails;
	private String title;
	private String javaRef;
	private String hides;

	public List<ReservationDetails> getReservationDetails() {
		return reservationDetails;
	}

	public void setReservationDetails(List<ReservationDetails> reservationDetails) {
		this.reservationDetails = reservationDetails;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJavaRef() {
		return javaRef;
	}

	public void setJavaRef(String javaRef) {
		this.javaRef = javaRef;
	}

	public String getHides() {
		return hides;
	}

	public void setHides(String hides) {
		this.hides = hides;
	}

}

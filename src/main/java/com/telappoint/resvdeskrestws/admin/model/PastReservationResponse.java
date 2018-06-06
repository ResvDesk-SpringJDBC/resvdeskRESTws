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
public class PastReservationResponse extends BaseResponse {
	List<ReservationDetails> resvDetails;

	public List<ReservationDetails> getResvDetails() {
		return resvDetails;
	}

	public void setResvDetails(List<ReservationDetails> resvDetails) {
		this.resvDetails = resvDetails;
	}
}

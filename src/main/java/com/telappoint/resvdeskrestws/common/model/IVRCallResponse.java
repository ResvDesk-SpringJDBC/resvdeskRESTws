package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji N
 *
 */

@JsonSerialize(include = Inclusion.NON_NULL)
public class IVRCallResponse extends BaseResponse {
	private Long ivrCallId;

	public Long getIvrCallId() {
		return ivrCallId;
	}

	public void setIvrCallId(Long ivrCallId) {
		this.ivrCallId = ivrCallId;
	}
}

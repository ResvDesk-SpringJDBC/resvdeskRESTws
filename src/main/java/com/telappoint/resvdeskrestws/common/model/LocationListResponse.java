package com.telappoint.resvdeskrestws.common.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Balaji
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class LocationListResponse extends BaseResponse {

	private String selectLocationLabel;
	private String selectDefaultLocationId;
	private List<Options> locationList;

	public String getSelectLocationLabel() {
		return selectLocationLabel;
	}

	public void setSelectLocationLabel(String selectLocationLabel) {
		this.selectLocationLabel = selectLocationLabel;
	}

	public String getSelectDefaultLocationId() {
		return selectDefaultLocationId;
	}

	public void setSelectDefaultLocationId(String selectDefaultLocationId) {
		this.selectDefaultLocationId = selectDefaultLocationId;
	}

	public List<Options> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Options> locationList) {
		this.locationList = locationList;
	}
}

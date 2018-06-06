package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationReportConfigResponse extends BaseResponse {
	private List<ReservationReportConfig> resvReportConfigList;

	public List<ReservationReportConfig> getResvReportConfigList() {
		return resvReportConfigList;
	}

	public void setResvReportConfigList(List<ReservationReportConfig> resvReportConfigList) {
		this.resvReportConfigList = resvReportConfigList;
	}
}

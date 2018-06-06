package com.telappoint.resvdeskrestws.admin.model;

import java.sql.Timestamp;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseRequest;

/**
 * 
 * @author Balaji
 * 
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationReportConfig extends BaseRequest {

	public Integer resvReportConfigId;

	public String timestamp;

	private String loginUserId;

	public String reportName;

	public String locationIds;

	public String eventIds;

	public String seatIds;

	public String procedureIds;

	public String departmentIds;

	public String reportColumns;

	public String email1;

	public String email2;

	public String email3;

	public String email4;

	public String email5;

	public String email6;

	public String sortby1;

	public String sortby2;

	public String sortby3;

	public String sortby4;

	public String sortby5;

	public char reportStop = 'N';

	public int noIntervalHrs = 24;

	public int reportNoDays = 1;

	public String fileFormat = "PDF";

	public Timestamp lastRunDate;

	public char enable = 'Y';

	public String resvStatusFetch = "11";

	public String getTimestamp() {
		return timestamp;
	}

	public String getReportName() {
		return reportName;
	}

	public String getLocationIds() {
		return locationIds;
	}

	public String getProcedureIds() {
		return procedureIds;
	}

	public String getDepartmentIds() {
		return departmentIds;
	}

	public String getReportColumns() {
		return reportColumns;
	}

	public String getEmail1() {
		return email1;
	}

	public String getEmail2() {
		return email2;
	}

	public String getEmail3() {
		return email3;
	}

	public String getEmail4() {
		return email4;
	}

	public String getEmail5() {
		return email5;
	}

	public String getEmail6() {
		return email6;
	}

	public String getSortby1() {
		return sortby1;
	}

	public String getSortby2() {
		return sortby2;
	}

	public String getSortby3() {
		return sortby3;
	}

	public String getSortby4() {
		return sortby4;
	}

	public String getSortby5() {
		return sortby5;
	}

	public char getReportStop() {
		return reportStop;
	}

	public int getNoIntervalHrs() {
		return noIntervalHrs;
	}

	public int getReportNoDays() {
		return reportNoDays;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public Timestamp getLastRunDate() {
		return lastRunDate;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public void setLocationIds(String locationIds) {
		this.locationIds = locationIds;
	}

	public void setProcedureIds(String procedureIds) {
		this.procedureIds = procedureIds;
	}

	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}

	public void setReportColumns(String reportColumns) {
		this.reportColumns = reportColumns;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public void setEmail4(String email4) {
		this.email4 = email4;
	}

	public void setEmail5(String email5) {
		this.email5 = email5;
	}

	public void setEmail6(String email6) {
		this.email6 = email6;
	}

	public void setSortby1(String sortby1) {
		this.sortby1 = sortby1;
	}

	public void setSortby2(String sortby2) {
		this.sortby2 = sortby2;
	}

	public void setSortby3(String sortby3) {
		this.sortby3 = sortby3;
	}

	public void setSortby4(String sortby4) {
		this.sortby4 = sortby4;
	}

	public void setSortby5(String sortby5) {
		this.sortby5 = sortby5;
	}

	public void setReportStop(char reportStop) {
		this.reportStop = reportStop;
	}

	public void setNoIntervalHrs(int noIntervalHrs) {
		this.noIntervalHrs = noIntervalHrs;
	}

	public void setReportNoDays(int reportNoDays) {
		this.reportNoDays = reportNoDays;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public void setLastRunDate(Timestamp lastRunDate) {
		this.lastRunDate = lastRunDate;
	}

	public char getEnable() {
		return enable;
	}

	public void setEnable(char enable) {
		this.enable = enable;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public Integer getResvReportConfigId() {
		return resvReportConfigId;
	}

	public void setResvReportConfigId(Integer resvReportConfigId) {
		this.resvReportConfigId = resvReportConfigId;
	}

	public String getEventIds() {
		return eventIds;
	}

	public void setEventIds(String eventIds) {
		this.eventIds = eventIds;
	}

	public String getSeatIds() {
		return seatIds;
	}

	public void setSeatIds(String seatIds) {
		this.seatIds = seatIds;
	}

	public String getResvStatusFetch() {
		return resvStatusFetch;
	}

	public void setResvStatusFetch(String resvStatusFetch) {
		this.resvStatusFetch = resvStatusFetch;
	}
}

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
public class ReminderStatusResponse extends BaseResponse {
	private List<ReminderStatus> reminderStatusList;

	public List<ReminderStatus> getReminderStatusList() {
		return reminderStatusList;
	}

	public void setReminderStatusList(List<ReminderStatus> reminderStatusList) {
		this.reminderStatusList = reminderStatusList;
	}

	
}

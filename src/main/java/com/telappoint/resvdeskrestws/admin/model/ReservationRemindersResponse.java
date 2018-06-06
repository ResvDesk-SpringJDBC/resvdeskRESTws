package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ReservationRemindersResponse extends BaseResponse {
	private Map<DailyReminderView, List<NotifyDailyView>> resvReminders;
	private String remindStatusNeedAppt;
	private boolean showTime;

	public Map<DailyReminderView, List<NotifyDailyView>> getResvReminders() {
		return resvReminders;
	}

	public void setResvReminders(Map<DailyReminderView, List<NotifyDailyView>> resvReminders) {
		this.resvReminders = resvReminders;
	}

	public String getRemindStatusNeedAppt() {
		return remindStatusNeedAppt;
	}

	public void setRemindStatusNeedAppt(String remindStatusNeedAppt) {
		this.remindStatusNeedAppt = remindStatusNeedAppt;
	}

	public boolean isShowTime() {
		return showTime;
	}

	public void setShowTime(boolean showTime) {
		this.showTime = showTime;
	}
}

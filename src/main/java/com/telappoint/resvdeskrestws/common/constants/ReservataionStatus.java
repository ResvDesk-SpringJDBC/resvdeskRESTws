package com.telappoint.resvdeskrestws.common.constants;

/**
 * @author Balaji
 * 
 */

public enum ReservataionStatus {
	NOT_OPEN(-1, "Not Opened"), 
	OPEN(0, "Opened"),

	NONE(0, "None"), 
	HOLD(1, "Hold"),
	RELEASE_HOLD(2, "Release Hold"),

	// All confirm reservation status reserved from 11 to 19
	CONFIRM(11, "Confirm"),
	REMIND_CONFIRM(12, "Remind Cancel"),

	// All Cancel reservation status reservered from 21 to 30
	CANCEL(21, "Cancel"), 
	REMIND_CANCEL(22, "Remaind Cancel"), 
	RESCHEDULE_CANCEL(23, "Reschedule Cancel"),
	DISPLACEMENT_CANCEL(24, "Displacement Cancel");

	private int status;
	private String description;

	private ReservataionStatus(int status, String description) {
		this.status = status;
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static class AppointmentStatusDescription {
		public static String getAppointmentStatusDescription(int status) {
			ReservataionStatus[] keys = ReservataionStatus.values();
			int statusInt = 0;
			for (ReservataionStatus key : keys) {
				statusInt = key.getStatus();
				if (statusInt == status) {
					return key.getDescription();
				}
			}
			return "";
		}
	}

	public static void main(String[] args) {
		String result = AppointmentStatusDescription.getAppointmentStatusDescription(1);
		System.out.println(result);
	}
}

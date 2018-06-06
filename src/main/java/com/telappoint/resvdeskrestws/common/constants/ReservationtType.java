package com.telappoint.resvdeskrestws.common.constants;

/**
 * @author Balaji
 *
 */
public enum ReservationtType {

	MAKE(1, "Make"), CANCEL(2, "Cancel");

	private int type;
	private String message;

	private ReservationtType(int type, String message) {
		this.type = type;
		this.setMessage(message);
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static class ApptTypeStatusMessage {
		public static String getMessage(int apptType) {
			ReservationtType[] keys = ReservationtType.values();
			int typeInt = 0;
			for (ReservationtType key : keys) {
				typeInt = key.getType();
				if (typeInt == apptType) {
					return key.getMessage();
				}
			}
			return "";
		}
	}
}

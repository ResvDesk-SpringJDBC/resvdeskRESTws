package com.telappoint.resvdeskrestws.notification.constants;

/**
 * 
 * @author Balaji
 * 
 */
public enum IVRNotifyStatusConstants {

	/**
	 * Below constants related to notify table(notify_phone_status) for phone
	 */
	NOTIFY_PHONE_PENDING(1,"Pending"), 
	NOTIFY_PHONE_IN_PROGRESS(2,"In Progress"),
	NOTIFY_PHONE_HUMAN_ANS(3,"Human Answer"),
	NOTIFY_PHONE_ANS_MACHINE(4,"Ans machine"),
	NOTIFY_PHONE_CONFIRMED(5,"Human Confirm"),
	NOTIFY_PHONE_CANCELLED(6,"Human Cancel"),
	NOTIFY_PHONE_DONOT_CALL(7,"Do not call"),
	NOTIFY_PHONE_TERMINATE(8,"Terminated"),
	NOTIFY_PHONE_SUSPENDED(9,"Suspended"),
		
	/**
	 * Below constants related to notify_phone_status table(call_status) for phone
	 */
	
	NOTIFY_PHONE_STATUS_NO_ANSWER(1,"No Answer"),
	NOTIFY_PHONE_STATUS_ANSWERED(2,"Answered"),
	NOTIFY_PHONE_STATUS_HUMAN_ANSWER(3,"Human Answer"),
	NOTIFY_PHONE_STATUS_ANS_MACHINE(4,"Ans machine"),
	NOTIFY_PHONE_STATUS_CONFIRMED(5,"Confirmed"),
	NOTIFY_PHONE_STATUS_CANCELLED(6,"Cancelled"),
	NOTIFY_PHONE_STATUS_DROPPED(7,"Dropped");
	
    private int notifyStatus;
	private String notifyMessage;

	private IVRNotifyStatusConstants(int notifyStatus,String notifyMessage) {
		this.notifyStatus = notifyStatus;
		this.notifyMessage = notifyMessage;
	}

	public int getNotifyStatus() {
		return notifyStatus;
	}

	public String getNotifyMessage() {
		return notifyMessage;
	}
	
	public static class NotifyPhoneStatusMessage {
		public static String getNotifyPhoneMessage(int notifyStatus) {
			IVRNotifyStatusConstants[] keys = IVRNotifyStatusConstants.values();
			int _notifyStatus = 0;
			for (IVRNotifyStatusConstants key : keys) {
				_notifyStatus = key.getNotifyStatus();
				if (_notifyStatus == notifyStatus) {
					return key.getNotifyMessage();
				}
			}
			return "";
		}
	}
	
	public static void main(String[] args) {
		String result = NotifyPhoneStatusMessage.getNotifyPhoneMessage(1);
		System.out.println(result);
	}
}
package com.telappoint.resvdeskrestws.handlers.json;

public enum MessageType {
	INFO("info"), WARNING("warn"), ERROR("error");

	private String messageType;

	private MessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the statusType
	 */
	public String getMessageType() {
		return messageType;
	}
}

package com.telappoint.resvdeskrestws.handlers.json;

import static com.telappoint.resvdeskrestws.common.constants.CommonResvDeskConstants.EMPTY_STRING;

import com.google.common.base.Strings;
import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.TelAppointLogger;

public class MessageManager {
	private MessageManager() {
	}

	public static String getProperty(String statusType, String errorCode) {
		String response = "";
		try {	
			if (Strings.isNullOrEmpty(errorCode)) {
				return EMPTY_STRING.getValue();
			}
			if ((MessageType.INFO.getMessageType()).equals(statusType)) {
				response = PropertyUtils.getValueFromProperties(errorCode, "appt-info.properties");
			} else if ((MessageType.WARNING.getMessageType()).equals(statusType)) {
				response = PropertyUtils.getValueFromProperties(errorCode, "appt-warn.properties");
			} else {
				response = PropertyUtils.getValueFromProperties(errorCode, "appt-error.properties");
			}
		}catch(Exception e) {
			TelAppointLogger.logError("Error: ",e);
		}
		return response;
	}
}

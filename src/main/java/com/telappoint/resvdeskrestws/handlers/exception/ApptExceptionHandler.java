package com.telappoint.resvdeskrestws.handlers.exception;

import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.telappoint.logger.CustomLogger;
import com.telappoint.logger.TelAppointLogger;
import com.telappoint.resvdeskrestws.common.constants.FlowStateConstants;
import com.telappoint.resvdeskrestws.handlers.json.JsonDataHandler;

public class ApptExceptionHandler {
	@ExceptionHandler(Exception.class)
	public @ResponseBody JsonDataHandler handleException(CustomLogger customLogger, Exception e) {
		if (e instanceof CannotGetJdbcConnectionException) {
			TelAppointLogger.logError("Error:", e);
			return JsonDataHandler.exceptionResponse(FlowStateConstants.DB_ERROR.getValue());
		} else {
			if (customLogger != null) {
				customLogger.error("Error:", e);
			} else {
				TelAppointLogger.logError("Error:", e);
			}
			return JsonDataHandler.exceptionResponse(e);
		}
	}
}
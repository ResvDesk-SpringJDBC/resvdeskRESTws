package com.telappoint.resvdeskrestws.handlers.json;

import static com.telappoint.resvdeskrestws.common.constants.CommonResvDeskConstants.VERSION;
import static com.telappoint.resvdeskrestws.handlers.json.MessageManager.getProperty;

import java.util.Collection;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointException;

@JsonAutoDetect
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class JsonDataHandler {
	private Object data;
	private String version;
	private String errorCode;
	
	/**
	 * This wont be shown in front screen, But, it will explain about more
	 * information about the error. This will be seen is logs.
	 */
	private String errorDescription;
	private String messageType;
	private int totalCount;

	public JsonDataHandler(Object data, String messageType, String errorCode) {
		this.data = data;
		this.messageType = messageType;
		this.setErrorDescription(getProperty(messageType, errorCode));
		this.setErrorCode(errorCode);
	}

	public JsonDataHandler() {
	}

	public static JsonDataHandler successResponse() {
		JsonDataHandler jsonData = new JsonDataHandler();
		jsonData.setMessageType(MessageType.INFO.getMessageType());
		jsonData.setErrorCode(ErrorCodes.NO_ERROR.getErrorCode());
		jsonData.setVersion(getProperty(MessageType.INFO.getMessageType(), VERSION.getValue()));
		return jsonData;
	}

	public static JsonDataHandler successResponse(String errorCode) {
		JsonDataHandler jsonData = new JsonDataHandler();
		jsonData.setMessageType(MessageType.INFO.getMessageType());
		jsonData.setErrorCode(ErrorCodes.NO_ERROR.getErrorCode());
		jsonData.setErrorCode(getProperty(MessageType.INFO.getMessageType(), errorCode));
		jsonData.setVersion(getProperty(MessageType.INFO.getMessageType(), VERSION.getValue()));
		return jsonData;
	}

	public static JsonDataHandler exceptionResponse(String errorCode) {
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setErrorStatus(true);
		baseResponse.setResponseStatus(false);
		baseResponse.setErrorMessage(getProperty(MessageType.ERROR.getMessageType(),errorCode));
		JsonDataHandler jsonData = new JsonDataHandler(baseResponse,MessageType.ERROR.getMessageType(),errorCode);
		return jsonData;
	}

	public static JsonDataHandler exceptionResponse(Exception e) {
		JsonDataHandler jsonData = new JsonDataHandler();
		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setErrorStatus(true);
		baseResponse.setResponseStatus(false);
		if(e instanceof TelAppointDBException) {
			TelAppointDBException telde = (TelAppointDBException)e;
			jsonData.setMessageType(MessageType.ERROR.getMessageType());
			jsonData.setErrorCode(telde.getErrorCode());
			baseResponse.setErrorMessage(telde.getErrorMessage());
			baseResponse.setErrorvxml(telde.getErrorVXML());
			jsonData.setErrorDescription(telde.getErrorDescription());
			jsonData.setVersion(getProperty(MessageType.ERROR.getMessageType(), VERSION.getValue()));
		} else if(e instanceof TelAppointException) {
			TelAppointException telde = (TelAppointException)e;
			jsonData.setMessageType(MessageType.ERROR.getMessageType());
			jsonData.setErrorCode(telde.getErrorCode());
			baseResponse.setErrorMessage(telde.getErrorMessage());
			baseResponse.setErrorvxml(telde.getErrorVXML());
			jsonData.setErrorDescription(telde.getErrorDescription());
			jsonData.setVersion(getProperty(MessageType.ERROR.getMessageType(), VERSION.getValue()));
		}
		jsonData.withData(baseResponse);
		return jsonData;
	}

	public JsonDataHandler withData(Object data) {
		this.data = data;
		return this;
	}

	public JsonDataHandler withCollection(Object data) {
		this.data = data;
		this.totalCount = ((Collection<?>) data).size();
		return this;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

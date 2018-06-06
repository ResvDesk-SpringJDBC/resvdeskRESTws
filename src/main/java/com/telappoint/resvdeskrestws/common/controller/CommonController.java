package com.telappoint.resvdeskrestws.common.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.handlers.exception.ApptExceptionHandler;
import com.telappoint.resvdeskrestws.handlers.json.JsonDataHandler;

import org.springframework.stereotype.Controller;

import static com.telappoint.resvdeskrestws.handlers.json.JsonDataHandler.*;

/**
 * 
 * @author Balaji N
 *
 */
@Controller
public class CommonController extends ApptExceptionHandler {
	private String callerMethodName;

	public JsonDataHandler populateJDHSuccessData(CustomLogger customLogger, Object data) {
		Throwable t = new Throwable();
		StackTraceElement[] elements = t.getStackTrace();
		callerMethodName = elements[1].getMethodName();
		return populateJDHSuccessData(customLogger, data, true);
	}
	
	public JsonDataHandler populateJDHSuccessData(Object data) {
		Throwable t = new Throwable();
		StackTraceElement[] elements = t.getStackTrace();
		callerMethodName = elements[1].getMethodName();
		return populateJDHSuccessData(data, true);
	}

	public JsonDataHandler populateJDHSuccessCollection(CustomLogger customLogger, Object data) {
		Throwable t = new Throwable();
		StackTraceElement[] elements = t.getStackTrace();
		callerMethodName = elements[1].getMethodName();
		return populateJDHSuccessCollection(customLogger, data, true);
	}

	public JsonDataHandler populateJDHSuccessData(CustomLogger customLogger, Object data, boolean logging) {
		if (logging == false) {
			Throwable t = new Throwable();
			StackTraceElement[] elements = t.getStackTrace();
			callerMethodName = elements[1].getMethodName();
		}
		
		JsonDataHandler jsonDataHandler = new JsonDataHandler();
		jsonDataHandler.withData(data);
		// if(logging) {
		// printJSONResponse(jsonDataHandler,callerMethodName);
		// }
		return jsonDataHandler;
	}
	
	public JsonDataHandler populateJDHSuccessData(Object data, boolean logging) {
		if (logging == false) {
			Throwable t = new Throwable();
			StackTraceElement[] elements = t.getStackTrace();
			callerMethodName = elements[1].getMethodName();
		}
		
		JsonDataHandler jsonDataHandler = new JsonDataHandler();
		jsonDataHandler.withData(data);
		// if(logging) {
		// printJSONResponse(jsonDataHandler,callerMethodName);
		// }
		return jsonDataHandler;
	}

	public JsonDataHandler populateJDHSuccessCollection(CustomLogger customLogger, Object data, boolean logging) {
		if (logging == false) {
			Throwable t = new Throwable();
			StackTraceElement[] elements = t.getStackTrace();
			callerMethodName = elements[1].getMethodName();
		}
		JsonDataHandler jsonDataHandler = successResponse();
		jsonDataHandler.withCollection(data);
		if (logging) {
			printJSONResponse(jsonDataHandler, customLogger, callerMethodName);
		}
		return jsonDataHandler;
	}

	public static void printJSONResponse(JsonDataHandler jsonDataHandler, CustomLogger customLogger, String methodName) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String response = gson.toJson(jsonDataHandler);
		System.out.println(methodName + " method response:" + response);
		// TODO change to debug - before moving to production.
		// logger.info(methodName + " method response:" + response);
	}

	public static String getJSONString(Object object) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String response = gson.toJson(object);
		return "<br/>InputParameters: [" + response + "]";
	}
}

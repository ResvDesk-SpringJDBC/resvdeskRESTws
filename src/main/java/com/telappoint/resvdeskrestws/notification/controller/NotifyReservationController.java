package com.telappoint.resvdeskrestws.notification.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.jersey.multipart.FormDataParam;
import com.telappoint.logger.CustomLogger;
import com.telappoint.logger.TelAppointLogger;
import com.telappoint.resvdeskrestws.admin.model.FileUploadResponse;
import com.telappoint.resvdeskrestws.common.controller.CommonController;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.utils.EmailUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointException;
import com.telappoint.resvdeskrestws.handlers.json.JsonDataHandler;
import com.telappoint.resvdeskrestws.notification.model.NotificationResponse;
import com.telappoint.resvdeskrestws.notification.service.NotifyReservationService;

/**
 * 
 * @author Balaji N
 *
 */
@Controller
@RequestMapping("/notifyservice")
public class NotifyReservationController extends CommonController {

	@Autowired
	private NotifyReservationService notifyReservationService;

	@RequestMapping(method = RequestMethod.GET, value = "/lockDialerLock", produces = "application/json")
	public @ResponseBody JsonDataHandler lockDialerLock(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device) {
		CustomLogger customLogger = null;
		try {
			customLogger = notifyReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			BaseResponse baseResponse = notifyReservationService.lockDialerLock(clientCode,customLogger, device);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/unLockDialerLock", produces = "application/json")
	public @ResponseBody JsonDataHandler unLockDialerLock(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device) {
		CustomLogger customLogger = null;
		try {
			customLogger = notifyReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			BaseResponse baseResponse = notifyReservationService.unLockDialerLock(clientCode,customLogger, device);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getNotifyList", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getNotifyList(HttpServletRequest request,@RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device) {
		CustomLogger customLogger = null;
		try {
			customLogger = notifyReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}	
			return populateJDHSuccessData(notifyReservationService.getNotifyList(clientCode, customLogger,"us-en", device));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getNotifyPhoneList", produces = "application/octet-stream")
	public void getNotifyPhoneList(@RequestParam("clientCode") String clientCode,HttpServletResponse response) {
    	ByteArrayOutputStream outputStream =  null;
    	OutputStream out = null;
    	CustomLogger customLogger = null;
		try {
			customLogger = notifyReservationService.getLogger(clientCode, "phone");
			NotificationResponse notificationResponse = notifyReservationService.getNotifyList(clientCode,customLogger,"us-en", "phone");
			outputStream = notificationResponse.getOutputStream();
			
			if(outputStream == null || "".equals(outputStream)) {
				customLogger.debug("GetNotifyPhoneList is empty!!!");
			}
			System.out.println("Data:"+outputStream);
			response.setHeader("Content-Disposition","attachment; filename=\"" + clientCode+".csv" + "\"");
			out = response.getOutputStream();			
			response.setContentType("application/text; charset=utf-8");
			out.write(outputStream.toByteArray());  
			out.flush();			
		} catch (Exception e) {
			customLogger.error("Exception in getNotifyPhoneList - "+e.getMessage(),e);
		}finally{
			try {
				if(out!=null){
					out.close();
				}
				if(outputStream!=null){
					outputStream.close();
				}
			} catch (IOException e) {
				customLogger.error("Exception in getNotifyPhoneList while closing outputstreams - "+e.getMessage(),e);
			}
			
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/uploadPhoneLogFile", consumes = "application/octet-stream", produces = "application/json")
	public @ResponseBody JsonDataHandler uploadPhoneLogFile(@QueryParam("fileExtension") String fileExtension, @QueryParam("fileSize") String fileSize,
			@QueryParam("clientCode") String clientCode, @FormDataParam("file") InputStream inputStream) {

		System.out.println("fileExtension:" + fileExtension);
		System.out.println("fileSize:" + fileSize);
		CustomLogger customLogger = null;
		try {
			customLogger = notifyReservationService.getLogger(clientCode, "online");
			FileUploadResponse fileUploadResponse = new FileUploadResponse();
			fileUploadResponse.setErrorResponse("File Extension " + fileExtension + " not allowed");
			if ("EXE".equals(fileExtension.toUpperCase()) || "SQL".equals(fileExtension.toUpperCase())) {
				return populateJDHSuccessData(fileUploadResponse);
			}

			fileUploadResponse = notifyReservationService.processThePhoneLogFile(clientCode, customLogger, fileSize,"online","1", inputStream, fileExtension);
			return populateJDHSuccessData(fileUploadResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	private void sendEmailIPNotAllowed(String clientCode, CustomLogger customLogger, String ipaddress, String methodName) throws Exception {
		StringBuilder body = new StringBuilder("IP Not Allowed :" + ipaddress);
		body.append("<br/><br/>");
		body.append("Exception: ");
		body.append("<br/>");
		body.append(methodName);
		try {
			EmailUtils.sendErrorEmail("IPNotAllowed - clientCode:" + clientCode, body.toString());
		} catch (Exception ex) {
			if (customLogger != null) {
				customLogger.error("Error: Unable to send application error email!", ex);
			} else {
				TelAppointLogger.logError("Error: Unable to send application error email!", ex);
			}
			throw new TelAppointException("9998", "", "IP Not Allowed:" + ipaddress, ex);
		}
	}

	private final boolean allowAnyIp = true;
	private final String iptoCheck = "127.0.0.1";

	private boolean checkIP(String ipAddress) {
		if (allowAnyIp) {
			return false;
		}
		return !ipAddress.equals(iptoCheck);
	}

}

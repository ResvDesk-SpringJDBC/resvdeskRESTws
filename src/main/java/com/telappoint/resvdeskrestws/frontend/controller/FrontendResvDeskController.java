package com.telappoint.resvdeskrestws.frontend.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.telappoint.logger.CustomLogger;
import com.telappoint.logger.TelAppointLogger;
import com.telappoint.resvdeskrestws.common.controller.CommonController;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientInfo;
import com.telappoint.resvdeskrestws.common.model.ConfirmationPageRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.IVRCallRequest;
import com.telappoint.resvdeskrestws.common.model.LoginInfoResponse;
import com.telappoint.resvdeskrestws.common.model.LoginInfoRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.ResvDetailsRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.ResvVerificationDetailsRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.service.ReservationService;
import com.telappoint.resvdeskrestws.common.utils.EmailUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointException;
import com.telappoint.resvdeskrestws.handlers.json.JsonDataHandler;

/**
 * 
 * @author Balaji N
 *
 */
@Controller
@RequestMapping("/service/")
public class FrontendResvDeskController extends CommonController {

	@Autowired
	private ReservationService reservationService;

	@RequestMapping(method = RequestMethod.GET, value = "getClientInfo", produces = "application/json")
	public @ResponseBody JsonDataHandler getClientInfo(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam(value = "langCode", required = false) String langCode, @RequestParam("device") String device,
			@RequestParam(value = "loginFirst", required = false) String loginFirst, @RequestParam("param1") String param1, @RequestParam("param2") String param2) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			customLogger.trace("Error");
			ClientInfo clientInfo = reservationService.getClientInfo(customLogger, clientCode, langCode, device, loginFirst, param1, param2);
			return populateJDHSuccessData(customLogger, clientInfo);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getLoginInfo", produces = "application/json")
	public @ResponseBody JsonDataHandler getLoginInfo(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam(value = "action", required = false) String action) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			LoginInfoResponse loginInfoResponse = reservationService.getLoginInfo(customLogger, clientCode, langCode, device, token, transId, action);
			return populateJDHSuccessData(customLogger, loginInfoResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/authenticateCustomer", produces = "application/json")
	public @ResponseBody JsonDataHandler authenticateCustomer(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("loginParams") String loginParams) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.authenticateCustomer(customLogger, clientCode, device, langCode, token, transId, loginParams));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/authenticateCustomerForCancel", produces = "application/json")
	public @ResponseBody JsonDataHandler authenticateCustomerForCancel(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("loginParams") String loginParams) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.authenticateCustomerForCancel(customLogger, clientCode, device, langCode, token, transId, loginParams));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getResvDetailsSelectionInfo", produces = "application/json")
	public @ResponseBody JsonDataHandler getResvDetailsSelectionInfo(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getResvDetailsSelectionInfo(customLogger, clientCode, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getCompanyList", produces = "application/json")
	public @ResponseBody JsonDataHandler getCompanyList(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getCompanyList(customLogger, clientCode, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getProcedureList", produces = "application/json")
	public @ResponseBody JsonDataHandler getProcedureList(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("companyId") String companyId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getProcedureList(customLogger, clientCode, device, langCode, token, transId, companyId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getLocationList", produces = "application/json")
	public @ResponseBody JsonDataHandler getLocationList(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("procedureId") String procedureId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getLocationList(customLogger, clientCode, device, langCode, token, transId, procedureId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getDepartmentList", produces = "application/json")
	public @ResponseBody JsonDataHandler getDepartmentList(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getDepartmentList(customLogger, clientCode, device, langCode, token, transId, locationId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getEventList", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventList(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam("departmentId") String departmentId, @RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getEventList(customLogger, clientCode, device, langCode, token, transId, departmentId, locationId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getEventDates", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventDates(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("eventId") String eventId,
			@RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getEventDates(customLogger, clientCode, device, langCode, token, transId, eventId, locationId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getEventTimes", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventTimes(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("eventId") String eventId,
			@RequestParam("locationId") String locationId, @RequestParam("date") String date) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getEventTimes(customLogger, clientCode, device, langCode, token, transId, eventId, locationId, date));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getEventSeats", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventSeats(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam("eventDateTimeId") String eventDateTimeId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.getEventSeats(customLogger, clientCode, device, langCode, token, transId, eventDateTimeId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/holdReservation", produces = "application/json")
	public @ResponseBody JsonDataHandler holdReservation(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("companyId") String companyId,
			@RequestParam("procedureId") String procedureId, @RequestParam("locationId") String locationId, @RequestParam("departmentId") String departmentId,
			@RequestParam("eventId") String eventId, @RequestParam("eventDateTimeId") String eventDateTimeId, @RequestParam("seatId") String seatId,
			@RequestParam("customerId") String customerId) {

		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger, reservationService.holdReservation(customLogger, clientCode, device, langCode, token, transId, companyId, procedureId,
					locationId, departmentId, eventId, eventDateTimeId, seatId, customerId));

		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getVerifyReservationDetails", produces = "application/json")
	public @ResponseBody JsonDataHandler getVerifyReservationDetails(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam("scheduleId") String scheduleId, @RequestParam("customerId") String customerId, @RequestParam(value = "loginFirst", required = false) String loginFirst) {

		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger,
					reservationService.getVerifyReservationDetails(customLogger, clientCode, device, langCode, token, transId, scheduleId, customerId, loginFirst));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/confirmReservation", produces = "application/json")
	public @ResponseBody JsonDataHandler confirmReservation(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("scheduleId") String scheduleId,
			@RequestParam("customerId") String customerId, @RequestParam("comment") String comment) {

		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(customLogger,
					reservationService.confirmReservation(customLogger, clientCode, device, langCode, token, transId, scheduleId, customerId, comment));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "releaseHoldEventTime", produces = "application/json")
	public @ResponseBody JsonDataHandler releaseHoldEventTime(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("scheduleId") String scheduleId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.releaseHoldEventTime(clientCode, customLogger, device, langCode, token, transId, scheduleId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "cancelReservation", produces = "application/json")
	public @ResponseBody JsonDataHandler cancelReservation(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("scheduleId") String scheduleId,
			@RequestParam("customerId") String customerId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.cancelReservation(clientCode, customLogger, device, langCode, token, transId, scheduleId, customerId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCancelNoResv", produces = "application/json")
	public @ResponseBody JsonDataHandler getCancelNoResv(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getCancelNoResv(clientCode, customLogger, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	// only ivr methods start --
	@RequestMapping(method = RequestMethod.GET, value = "getCancelVerify", produces = "application/json")
	public @ResponseBody JsonDataHandler getCancelVerify(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getCancelVerify(clientCode, customLogger, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getWelcomePageVxml", produces = "application/json")
	public @ResponseBody JsonDataHandler getWelcomePageVxml(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getWelcomePageVxml(clientCode, customLogger, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getNameRecordVxml", produces = "application/json")
	public @ResponseBody JsonDataHandler getNameRecordVxml(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getNameRecordVxml(clientCode, customLogger, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "updateRecordVxml", produces = "application/json")
	public @ResponseBody JsonDataHandler updateRecordVxml(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("scheduleId") String scheduleId,
			@RequestParam("customerId") String customerId, @RequestParam("filePath") String filePath, @RequestParam("fileName") String fileName,
			@RequestParam("recordDurationInSec") String recordDurationInSec) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.updateRecordVxml(clientCode, customLogger, device, langCode, token, transId, scheduleId, customerId, filePath,
					fileName, recordDurationInSec));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr methods end --

	@RequestMapping(method = RequestMethod.GET, value = "getEventHistory", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventHistory(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getEventsHistory(clientCode, customLogger, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getReservedEvents", produces = "application/json")
	public @ResponseBody JsonDataHandler getReservedEvents(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("customerId") String customerId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getReservedEvents(clientCode, customLogger, device, langCode, token, transId, customerId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// online api
	@RequestMapping(method = RequestMethod.GET, value = "getLoginInfoRightSideContent", produces = "application/json")
	public @ResponseBody JsonDataHandler getLoginInfoRightSideContent(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam(value = "loginFirst", required = false) String loginFirst) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			LoginInfoRightSideContentResponse loginInfoRightSideContentResponse = reservationService.getLoginInfoRightSideContent(customLogger, clientCode, langCode, device,
					token, transId, loginFirst);
			return populateJDHSuccessData(customLogger, loginInfoRightSideContentResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// online only
	@RequestMapping(method = RequestMethod.GET, value = "getResvDetailsRightSideContent", produces = "application/json")
	public @ResponseBody JsonDataHandler getResvDetailsRightSideContent(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam(value = "loginFirst", required = false) String loginFirst) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			ResvDetailsRightSideContentResponse resvDetailsRightSideContentResponse = reservationService.getResvDetailsRightSideContent(customLogger, clientCode, langCode, device,
					token, transId, loginFirst);
			return populateJDHSuccessData(customLogger, resvDetailsRightSideContentResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// online api
	@RequestMapping(method = RequestMethod.GET, value = "getResvVerifyDetailsRightSideContent", produces = "application/json")
	public @ResponseBody JsonDataHandler getResvVerificationDetailsRightSideContent(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam(value = "loginFirst", required = false) String loginFirst) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			ResvVerificationDetailsRightSideContentResponse resvVerificationDetailsRightSideContentResponse = reservationService.getResvVerifyDetailsRightSideContent(customLogger,
					clientCode, langCode, device, token, transId, loginFirst);
			return populateJDHSuccessData(customLogger, resvVerificationDetailsRightSideContentResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only online api
	@RequestMapping(method = RequestMethod.GET, value = "getResvConfirmationPageRightSideContent", produces = "application/json")
	public @ResponseBody JsonDataHandler getResvConfirmationPageRightSideContent(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam(value = "loginFirst", required = false) String loginFirst) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			ConfirmationPageRightSideContentResponse confirmationPageContentResponse = reservationService.getConfirmationPageRightSideContent(customLogger, clientCode, langCode,
					device, token, transId, loginFirst);
			return populateJDHSuccessData(customLogger, confirmationPageContentResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "listOfThingsToBring", produces = "application/json")
	public @ResponseBody JsonDataHandler listOfThingsToBring(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("eventId") String eventId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.listOfThingsToBring(clientCode, customLogger, device, langCode, token, transId, eventId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only online
	@RequestMapping(method = RequestMethod.GET, value = "getConfPageContactDetails", produces = "application/json")
	public @ResponseBody JsonDataHandler getConfPageContactDetails(HttpServletRequest request, @RequestParam("clientCode") String clientCode,
			@RequestParam("device") String device, @RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getConfPageContactDetails(clientCode, customLogger, device, langCode, token, transId, locationId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr
	@RequestMapping(method = RequestMethod.GET, value = "getSpecificDateVXML", produces = "application/json")
	public @ResponseBody JsonDataHandler getSpecificDateVXML(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(reservationService.getSpecificDateVXML(clientCode, customLogger, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr
	@RequestMapping(method = RequestMethod.GET, value = "/disconnectVXML", produces = "application/json")
	public @ResponseBody JsonDataHandler disconnectVXML(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			BaseResponse baseResponse = reservationService.disconnectVXML(customLogger, clientCode, langCode, device, token, transId);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	// only ivr
	@RequestMapping(method = RequestMethod.POST, value = "/saveOrUpdateIVRCallLog", produces = "application/json")
	public @ResponseBody JsonDataHandler saveOrUpdateIVRCallLog(HttpServletRequest request, @RequestBody IVRCallRequest ivrCallRequest) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(ivrCallRequest.getClientCode(), ivrCallRequest.getDevice());
			BaseResponse baseResponse = reservationService.saveOrUpdateIVRCallLog(customLogger, ivrCallRequest);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr
	@RequestMapping(method = RequestMethod.GET, value = "/noApptInAllLocation", produces = "application/json")
	public @ResponseBody JsonDataHandler noApptInAllLocation(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			BaseResponse baseResponse = reservationService.noApptInAllLocation(customLogger, clientCode, langCode, device, token, transId);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr
	@RequestMapping(method = RequestMethod.GET, value = "/noApptInSelectedLocation", produces = "application/json")
	public @ResponseBody JsonDataHandler noApptInSelectedLocation(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			BaseResponse baseResponse = reservationService.noApptInSelectedLocation(customLogger, clientCode, langCode, device, token, transId, locationId);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr
	@RequestMapping(method = RequestMethod.GET, value = "/noApptInSelectedEvent", produces = "application/json")
	public @ResponseBody JsonDataHandler noApptInSelectedEvent(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("locationId") String locationId,
			@RequestParam("eventId") String eventId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			BaseResponse baseResponse = reservationService.noApptInSelectedEvent(customLogger, clientCode, langCode, device, token, transId, locationId, eventId);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr
	@RequestMapping(method = RequestMethod.GET, value = "/noApptInSelectedDate", produces = "application/json")
	public @ResponseBody JsonDataHandler noApptInSelectedDate(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("locationId") String locationId,
			@RequestParam("eventId") String eventId, @RequestParam("date") String date) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			BaseResponse baseResponse = reservationService.noApptInSelectedDate(customLogger, clientCode, langCode, device, token, transId, locationId, eventId, date);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	// only ivr
	@RequestMapping(method = RequestMethod.GET, value = "/noApptInSelectedTime", produces = "application/json")
	public @ResponseBody JsonDataHandler noApptInSelectedTime(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("locationId") String locationId,
			@RequestParam("eventId") String eventId, @RequestParam("date") String date, @RequestParam("eventDateTimeId") String eventDateTimeId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			BaseResponse baseResponse = reservationService.noApptInSelectedTime(customLogger, clientCode, langCode, device, token, transId, locationId, eventId, date,
					eventDateTimeId);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/isAllowDuplicateResv", produces = "application/json")
	public @ResponseBody JsonDataHandler isAllowDuplicateResv(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("customerId") String customerId,
			@RequestParam("eventId") String eventId) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, device);
			BaseResponse baseResponse = reservationService.isAllowDuplicateResv(clientCode, customLogger, device, langCode, token, transId, customerId, eventId);
			return populateJDHSuccessData(baseResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/clearCache", produces = "application/json")
	public @ResponseBody JsonDataHandler clearCache(HttpServletRequest request, @RequestParam("clientCode") String clientCode) {
		CustomLogger customLogger = null;
		try {
			customLogger = reservationService.getLogger(clientCode, "refreshCache");
			return populateJDHSuccessData(customLogger, reservationService.clearTheCache(customLogger, clientCode));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/clearTheProperty", produces = "application/json")
	public @ResponseBody JsonDataHandler clearTheProperty(HttpServletRequest request, @RequestParam("fileName") String fileName) {
		try {
			return populateJDHSuccessData(reservationService.clearTheProperty(fileName));
		} catch (Exception e) {
			TelAppointLogger.logError("Failed in clearTheProperty: ", e);
		}
		return null;
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
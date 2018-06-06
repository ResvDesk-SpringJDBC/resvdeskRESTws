package com.telappoint.resvdeskrestws.admin.controller;

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
import com.telappoint.resvdeskrestws.admin.model.ClientDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.CustomerResponse;
import com.telappoint.resvdeskrestws.admin.model.Event;
import com.telappoint.resvdeskrestws.admin.model.EventDateTime;
import com.telappoint.resvdeskrestws.admin.model.FutureReservationResponse;
import com.telappoint.resvdeskrestws.admin.model.GraphResponse;
import com.telappoint.resvdeskrestws.admin.model.InBoundCallReportRequest;
import com.telappoint.resvdeskrestws.admin.model.Location;
import com.telappoint.resvdeskrestws.admin.model.NotifyRequest;
import com.telappoint.resvdeskrestws.admin.model.OutBoundCallReportRequest;
import com.telappoint.resvdeskrestws.admin.model.PastReservationResponse;
import com.telappoint.resvdeskrestws.admin.model.PrivilageResponse;
import com.telappoint.resvdeskrestws.admin.model.RegistrationInfoResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfig;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportRequest;
import com.telappoint.resvdeskrestws.admin.model.ReservationSearchResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchFieldsResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchRequestData;
import com.telappoint.resvdeskrestws.admin.model.TablePrintViewResponse;
import com.telappoint.resvdeskrestws.admin.service.AdminReservationService;
import com.telappoint.resvdeskrestws.common.controller.CommonController;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.utils.EmailUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointException;
import com.telappoint.resvdeskrestws.handlers.json.JsonDataHandler;

/**
 * 
 * @author Balaji N
 *
 */
@Controller
@RequestMapping("/admin/")
public class AdminReservationController extends CommonController {

	@Autowired
	private AdminReservationService adminReservationService;

	@RequestMapping(method = RequestMethod.GET, value = "getLocationList", produces = "application/json")
	public @ResponseBody JsonDataHandler getLocationList(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getLocationList(clientCode, customLogger, langCode, device, transId, true));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getLocationListDropDown", produces = "application/json")
	public @ResponseBody JsonDataHandler getLocationListDropDown(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getLocationList(clientCode, customLogger, langCode, device, transId, false));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getEventListByLocationId", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventListByLocationId(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getEventListByLocationId(clientCode, customLogger, langCode, device, transId, locationId, false));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getLocationById", produces = "application/json")
	public @ResponseBody JsonDataHandler getLocationById(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getLocationById(clientCode, customLogger, langCode, device, transId, locationId, true));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "deleteLocation", produces = "application/json")
	public @ResponseBody JsonDataHandler deleteLocation(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.deleteLocation(clientCode, customLogger, langCode, device, transId, locationId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "openCloseLocation", produces = "application/json")
	public @ResponseBody JsonDataHandler openCloseLocation(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("openClosedFlag") String enable) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.openCloseLocation(clientCode, customLogger, langCode, device, transId, locationId, enable));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "addLocation", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler addLocation(@RequestBody Location location) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(location.getClientCode(), location.getDevice());
			return populateJDHSuccessData(adminReservationService.addLocation(location.getClientCode(), customLogger, location.getLangCode(), location.getDevice(), location.getTransId(), location));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "updateLocation", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler updateLocation(@RequestBody Location location) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(location.getClientCode(), location.getDevice());
			return populateJDHSuccessData(adminReservationService.updateLocation(customLogger, location));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getEventList", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventList(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getEventList(clientCode, customLogger, langCode, device, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "deleteEvent", produces = "application/json")
	public @ResponseBody JsonDataHandler deleteEvent(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("eventId") String eventId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.deleteEvent(clientCode, customLogger, langCode, device, transId, eventId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "openCloseEvent", produces = "application/json")
	public @ResponseBody JsonDataHandler openCloseEvent(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("eventId") String eventId, @RequestParam("openClosedFlag") String enable) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.openCloseEvent(clientCode, customLogger, langCode, device, transId, eventId, enable));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "addEvent", produces = "application/json")
	public @ResponseBody JsonDataHandler addEvent(@RequestBody Event event) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(event.getClientCode(), event.getDevice());
			return populateJDHSuccessData(adminReservationService.addEvent(event.getClientCode(), customLogger, event.getLangCode(), event.getDevice(), event.getTransId(), event));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "updateEvent", produces = "application/json")
	public @ResponseBody JsonDataHandler updateEvent(@RequestBody Event event) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(event.getClientCode(), event.getDevice());
			return populateJDHSuccessData(adminReservationService.updateEvent(event.getClientCode(), customLogger, event.getLangCode(), event.getDevice(), event.getTransId(), event));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getEventById", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventById(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("eventId") String eventId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getEventById(clientCode, customLogger, langCode, device, transId, eventId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getEventDateTimeList", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventDateTimeList(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getEventDateTimeList(clientCode, customLogger, langCode, device, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getEventDateTimeById", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventDateTimeById(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("eventDateTimeId") String eventDateTimeId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getEventDateTimeById(clientCode, customLogger, langCode, device, transId, eventDateTimeId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "openCloseEventDateTime", produces = "application/json")
	public @ResponseBody JsonDataHandler openCloseEventDateTime(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("eventDateTimeId") String eventDateTimeId, @RequestParam("openClosedFlag") String enable) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.openCloseEventDateTime(clientCode, customLogger, langCode, device, transId, eventDateTimeId, enable));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "addEventDateTime", produces = "application/json")
	public @ResponseBody JsonDataHandler addEventDateTime(@RequestBody EventDateTime eventDateTime) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(eventDateTime.getClientCode(), eventDateTime.getDevice());
			return populateJDHSuccessData(adminReservationService.addEventDateTime(eventDateTime.getClientCode(), customLogger, eventDateTime.getLangCode(), eventDateTime.getDevice(),
					eventDateTime.getTransId(), "" + eventDateTime.getLocationId(), eventDateTime));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "updateEventDateTime", produces = "application/json")
	public @ResponseBody JsonDataHandler updateEventDateTime(@RequestBody EventDateTime eventDateTime) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(eventDateTime.getClientCode(), eventDateTime.getDevice());
			return populateJDHSuccessData(adminReservationService.updateEventDateTime(eventDateTime.getClientCode(), customLogger, eventDateTime.getLangCode(), eventDateTime.getDevice(),
					eventDateTime.getTransId(), eventDateTime));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "updateEventSeats", produces = "application/json")
	public @ResponseBody JsonDataHandler updateEventSeats(@RequestBody EventDateTime eventDateTime) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(eventDateTime.getClientCode(), eventDateTime.getDevice());
			return populateJDHSuccessData(adminReservationService.updateEventSeats(customLogger, eventDateTime));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCalendarOverview", produces = "application/json")
	public @ResponseBody JsonDataHandler getCalendarOverview(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getCalendarOverview(clientCode, customLogger, langCode, device, transId, startDate, endDate));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getSeatsCalendarView", produces = "application/json")
	public @ResponseBody JsonDataHandler getSeatsCalendarView(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("eventId") String eventId, @RequestParam("date") String date,
			@RequestParam("time") String time,@RequestParam("showRemainderIcons") String showRemainderIcons) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getSeatsCalendarView(clientCode, customLogger, langCode, device, transId, locationId, eventId, date, time, showRemainderIcons));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getDailyCalendarView", produces = "application/json")
	public @ResponseBody JsonDataHandler getDailyCalendarView(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("eventId") String eventId, @RequestParam("date") String date,
			@RequestParam("showRemainderIcons") String showRemainderIcons) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getDailyCalendarView(clientCode, customLogger, langCode, device, transId, locationId, eventId, date,showRemainderIcons));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCalendarDateList", produces = "application/json")
	public @ResponseBody JsonDataHandler getCalendarDateList(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("eventId") String eventId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getCalendarDateList(clientCode, customLogger, langCode, device, transId, locationId, eventId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getJSCalendarDateList", produces = "application/json")
	public @ResponseBody JsonDataHandler getJSCalendarDateList(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("eventId") String eventId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getJSCalendarDateList(clientCode, customLogger, langCode, device, transId, locationId, eventId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getSeatViewTimeList", produces = "application/json")
	public @ResponseBody JsonDataHandler getSeatViewTimeList(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("eventId") String eventId, @RequestParam("date") String date) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getSeatViewTimeList(clientCode, customLogger, langCode, device, transId, locationId, eventId, date));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getHomePageDetails", produces = "application/json")
	public @ResponseBody JsonDataHandler getHomePageDetails(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("privilegeName") String privilegeName) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getHomePageDetails(clientCode, customLogger, langCode, device, transId, privilegeName));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCustomerNames", produces = "application/json")
	public @ResponseBody JsonDataHandler getCustomerNames(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("customerName") String customerName) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getCustomerNames(clientCode, customLogger, langCode, device, transId, customerName));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getReservationReports", produces = "application/json")
	public @ResponseBody JsonDataHandler getReservationReports(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("eventId") String eventId, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate, @RequestParam("resvStatus") String resvStatus,
			 @RequestParam("eventDateTimeIds") String eventDateTimeIds) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			ReservationReportRequest reservationReportRequest = new ReservationReportRequest();
			reservationReportRequest.setCustomLogger(customLogger);	
			reservationReportRequest.setClientCode(clientCode);
			reservationReportRequest.setLangCode(langCode);
			reservationReportRequest.setDevice(device);
			reservationReportRequest.setTransId(transId);
			reservationReportRequest.setLocationId(Integer.valueOf(locationId));
			reservationReportRequest.setEventId(Long.valueOf(eventId));
			reservationReportRequest.setStartDate(startDate);
			reservationReportRequest.setEndDate(endDate);
			reservationReportRequest.setResvStatus(resvStatus);
			reservationReportRequest.setEventDateTimeIds(eventDateTimeIds);
			
			return populateJDHSuccessData(adminReservationService.getReservationReports(reservationReportRequest));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getEventDateTimeForLocEventDateRange", produces = "application/json")
	public @ResponseBody JsonDataHandler getEventDateTimeForLocEventDateRange(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("locationId") String locationId, @RequestParam("eventId") String eventId, @RequestParam("startDate") String startDate,
			@RequestParam("endDate") String endDate) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			ReservationReportRequest reservationReportRequest = new ReservationReportRequest();
			reservationReportRequest.setCustomLogger(customLogger);	
			reservationReportRequest.setClientCode(clientCode);
			reservationReportRequest.setLangCode(langCode);
			reservationReportRequest.setDevice(device);
			reservationReportRequest.setTransId(transId);
			reservationReportRequest.setLocationId(Integer.valueOf(locationId));
			reservationReportRequest.setEventId(Long.valueOf(eventId));
			reservationReportRequest.setStartDate(startDate);
			reservationReportRequest.setEndDate(endDate);
			return populateJDHSuccessData(adminReservationService.getEventDateTimeForLocEventDateRange(reservationReportRequest));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCalendarOverViewDetails", produces = "application/json")
	public @ResponseBody JsonDataHandler getCalendarOverViewDetails(@RequestParam("clientCode") String clientCode, @RequestParam("langCode") String langCode, @RequestParam("device") String device,
			@RequestParam("transId") String transId, @RequestParam("eventDateTimeId") String eventDateTimeId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			return populateJDHSuccessData(adminReservationService.getCalendarOverViewDetails(customLogger, clientCode, langCode, device, transId, eventDateTimeId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCustomerById", produces = "application/json")
	public @ResponseBody JsonDataHandler getCustomerById(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("customerId") String customerId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			CustomerResponse customerReponse = adminReservationService.getCustomerById(customLogger, clientCode, langCode, device, token, transId, customerId);
			return populateJDHSuccessData(customLogger, customerReponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getFutureReservations", produces = "application/json")
	public @ResponseBody JsonDataHandler getFutureReservations(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("customerId") String customerId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			FutureReservationResponse futureReservationResponse = adminReservationService.getFutureReservations(customLogger, clientCode, langCode, device, token, transId, customerId);
			return populateJDHSuccessData(customLogger, futureReservationResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getPastReservations", produces = "application/json")
	public @ResponseBody JsonDataHandler getPastReservations(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("customerId") String customerId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			PastReservationResponse pastReservationResponse = adminReservationService.getPastReservations(customLogger, clientCode, langCode, device, token, transId, customerId);
			return populateJDHSuccessData(customLogger, pastReservationResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getReservationByScheduleId", produces = "application/json")
	public @ResponseBody JsonDataHandler getReservationByScheduleId(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("scheduleId") String scheduleId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			ReservationDetailsResponse reservationDetailsResponse = adminReservationService.getReservationByScheduleId(customLogger, clientCode, langCode, device, token, transId, scheduleId);
			return populateJDHSuccessData(customLogger, reservationDetailsResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getRegistrationInfo", produces = "application/json")
	public @ResponseBody JsonDataHandler getRegistrationInfo(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			RegistrationInfoResponse regInfoResponse = adminReservationService.getRegistrationInfo(customLogger, clientCode, langCode, device, token, transId);
			return populateJDHSuccessData(customLogger, regInfoResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getTablePrintView", produces = "application/json")
	public @ResponseBody JsonDataHandler getTablePrintView(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("eventId") String eventId,
			@RequestParam("locationId") String locationId, @RequestParam("date") String date) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(clientCode, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			TablePrintViewResponse tablePrintViewResponse = adminReservationService.getTablePrintView(customLogger, clientCode, langCode, device, token, transId, eventId, locationId, date);
			return populateJDHSuccessData(customLogger, tablePrintViewResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "getReservationSearch", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler getReservationSearch(HttpServletRequest request, @RequestBody SearchRequestData resvSearchRequest) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(resvSearchRequest.getClientCode(), resvSearchRequest.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(resvSearchRequest.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			ReservationSearchResponse resvSearchResponse = adminReservationService.getReservationSearch(customLogger, resvSearchRequest);
			return populateJDHSuccessData(customLogger, resvSearchResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "getClientDetails", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler getClientDetails(HttpServletRequest request, @RequestBody SearchRequestData resvSearchRequest) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(resvSearchRequest.getClientCode(), resvSearchRequest.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(resvSearchRequest.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			ClientDetailsResponse clientDetailsResponse = adminReservationService.getClientDetails(customLogger, resvSearchRequest);
			return populateJDHSuccessData(customLogger, clientDetailsResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "getBlockedClientDetails", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler getBlockedClientDetails(HttpServletRequest request, @RequestBody SearchRequestData resvSearchRequest) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(resvSearchRequest.getClientCode(), resvSearchRequest.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(resvSearchRequest.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			ClientDetailsResponse clientDetailsResponse = adminReservationService.getBlockedClientDetails(customLogger, resvSearchRequest);
			return populateJDHSuccessData(customLogger, clientDetailsResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getDynamicSearchFields", produces = "application/json")
	public @ResponseBody JsonDataHandler getDynamicSearchFields(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("searchCategory") String searchCategory) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			SearchFieldsResponse searchFieldReponse = adminReservationService.getDynamicSearchFields(customLogger, clientCode, device, langCode, token, transId, searchCategory);
			return populateJDHSuccessData(customLogger, searchFieldReponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "getGraphDetails", produces = "application/json")
	public @ResponseBody JsonDataHandler getGraphDetails(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId, @RequestParam("locationId") String locationId,
			@RequestParam("date") String date) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			GraphResponse graphResponse = adminReservationService.getGraphDetails(customLogger, clientCode, device, langCode, token, transId, locationId, date);
			return populateJDHSuccessData(customLogger, graphResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getAccessesPrivilages", produces = "application/json")
	public @ResponseBody JsonDataHandler getAccessesPrivilages(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			PrivilageResponse privilageResponse = adminReservationService.getAccessesPrivilages(customLogger, clientCode, device, langCode, token, transId);
			return populateJDHSuccessData(customLogger, privilageResponse);
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	

	@RequestMapping(method = RequestMethod.POST, value = "updateCustomer", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler updateCustomer(HttpServletRequest request, @RequestBody Customer customer) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(customer.getClientCode(), customer.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(customer.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.updateCustomer(customLogger, customer));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/addReservationReportConfig", consumes = "application/json", produces = "application/json")
	public @ResponseBody
	JsonDataHandler addReservationReportConfig(HttpServletRequest request,@RequestBody ReservationReportConfig resvReportConfig) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(resvReportConfig.getClientCode(), resvReportConfig.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(resvReportConfig.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.addReservationReportConfig(customLogger, resvReportConfig));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getReservationReportConfig", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getReservationReportConfig(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.getReservationReportConfig(customLogger, clientCode, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/deleteResvReportConfigById", produces = "application/json")
	public @ResponseBody
	JsonDataHandler deleteResvReportConfigById(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			 @RequestParam("reportConfigId") String reportConfigId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.deleteResvReportConfigById(customLogger, clientCode, device, langCode, token, transId, reportConfigId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getCampaigns", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getCampaigns(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.getCampaigns(clientCode, customLogger, langCode, device, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getReservationReminders", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getReservationReminders(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId,
			@RequestParam("locationId") String locationId, @RequestParam("campaignId") String campaignId,  @RequestParam("date") String date) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.getReservationReminders(customLogger, clientCode, device, langCode, token, transId, locationId, campaignId, date));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getReservationStatusList", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getReservationStatusList(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.getReservationStatusList(customLogger, clientCode, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getNotifyRemainderStatusList", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getNotifyRemainderStatusList(HttpServletRequest request, @RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.getNotifyRemainderStatusList(customLogger, clientCode, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/changeSchedulerStatus", produces = "application/json")
	public @ResponseBody
	JsonDataHandler changeSchedulerStatus(HttpServletRequest request, @RequestParam("clientCode") String clientCode, 
			@RequestParam("device") String device,
			@RequestParam("langCode") String langCode, 
			@RequestParam("token") String token, 
			@RequestParam("transId") String transId,
			@RequestParam("status") String status) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.changeSchedulerStatus(customLogger, clientCode, device, langCode, token, transId,status));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/updateNotifyStatus", consumes = "application/json", produces = "application/json")
	public @ResponseBody
	JsonDataHandler updateNotifyStatus(HttpServletRequest request,@RequestBody NotifyRequest notifyRequest) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(notifyRequest.getClientCode(), notifyRequest.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(notifyRequest.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.updateNotifyStatus(customLogger, notifyRequest));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getInBoundCallReportList", consumes = "application/json", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getInBoundCallReportList(HttpServletRequest request,@RequestBody InBoundCallReportRequest inBCReportRequest) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(inBCReportRequest.getClientCode(), inBCReportRequest.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(inBCReportRequest.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			inBCReportRequest.setCustomLogger(customLogger);
			return populateJDHSuccessData(adminReservationService.getInBoundCallReportList(customLogger, inBCReportRequest));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/getOutBoundCallReportList", consumes = "application/json", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getOutBoundCallReportList(HttpServletRequest request,@RequestBody OutBoundCallReportRequest outBCReportRequest) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(outBCReportRequest.getClientCode(), outBCReportRequest.getDevice());
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(outBCReportRequest.getDevice(), customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			outBCReportRequest.setCustomLogger(customLogger);
			return populateJDHSuccessData(adminReservationService.getOutBoundCallReportList(customLogger, outBCReportRequest));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getTransStates", produces = "application/json")
	public @ResponseBody
	JsonDataHandler getTransStates(HttpServletRequest request,
			@RequestParam("clientCode") String clientCode, @RequestParam("device") String device,
			@RequestParam("langCode") String langCode, @RequestParam("token") String token, @RequestParam("transId") String transId) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.getTransStates(clientCode,customLogger, device, langCode, token, transId));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateScreenedStatus", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler updateScreenedStatus(HttpServletRequest request,
			@RequestParam("clientCode") String clientCode, 
			@RequestParam("device") String device,
			@RequestParam("langCode") String langCode, 
			@RequestParam("token") String token, 
			@RequestParam("transId") String transId,
			@RequestParam("scheduleId") String scheduleId,
			@RequestParam("screened") String screened) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.updateScreenedStatus(customLogger,clientCode,device,langCode,token,transId,scheduleId,screened));
		} catch (Exception e) {
			return handleException(customLogger, e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "updateSeatReservedStatus", produces = "application/json", consumes = "application/json")
	public @ResponseBody JsonDataHandler updateSeatReservedStatus(HttpServletRequest request,
			@RequestParam("clientCode") String clientCode, 
			@RequestParam("device") String device,
			@RequestParam("langCode") String langCode, 
			@RequestParam("token") String token, 
			@RequestParam("transId") String transId,
			@RequestParam("seatId") String seatId,
			@RequestParam("reserved") String reserved) {
		CustomLogger customLogger = null;
		try {
			customLogger = adminReservationService.getLogger(clientCode, device);
			String ipAddress = request.getRemoteAddr();
			if (checkIP(ipAddress)) {
				sendEmailIPNotAllowed(device, customLogger, ipAddress, Thread.currentThread().getStackTrace()[1].getMethodName());
			}
			return populateJDHSuccessData(adminReservationService.updateSeatReservedStatus(customLogger,clientCode,device,langCode,token,transId,seatId,reserved));
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

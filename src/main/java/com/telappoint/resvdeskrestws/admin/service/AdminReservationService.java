package com.telappoint.resvdeskrestws.admin.service;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverviewDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverviewResponse;
import com.telappoint.resvdeskrestws.admin.model.ClientDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.CustomerResponse;
import com.telappoint.resvdeskrestws.admin.model.DailyCalendarView;
import com.telappoint.resvdeskrestws.admin.model.DateJSListResponse;
import com.telappoint.resvdeskrestws.admin.model.DateListResponse;
import com.telappoint.resvdeskrestws.admin.model.Event;
import com.telappoint.resvdeskrestws.admin.model.EventDateTime;
import com.telappoint.resvdeskrestws.admin.model.EventListResponse;
import com.telappoint.resvdeskrestws.admin.model.FutureReservationResponse;
import com.telappoint.resvdeskrestws.admin.model.GraphResponse;
import com.telappoint.resvdeskrestws.admin.model.HomePageResponse;
import com.telappoint.resvdeskrestws.admin.model.InBoundCallReportRequest;
import com.telappoint.resvdeskrestws.admin.model.Location;
import com.telappoint.resvdeskrestws.admin.model.LocationListResponse;
import com.telappoint.resvdeskrestws.admin.model.NotifyRequest;
import com.telappoint.resvdeskrestws.admin.model.OutBoundCallReportRequest;
import com.telappoint.resvdeskrestws.admin.model.PastReservationResponse;
import com.telappoint.resvdeskrestws.admin.model.PrivilageResponse;
import com.telappoint.resvdeskrestws.admin.model.RegistrationInfoResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportCheckboxResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfig;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfigResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportRequest;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationSearchResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchFieldsResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchRequestData;
import com.telappoint.resvdeskrestws.admin.model.SeatsCalendarView;
import com.telappoint.resvdeskrestws.admin.model.TablePrintViewResponse;
import com.telappoint.resvdeskrestws.admin.model.TimeListResponse;
import com.telappoint.resvdeskrestws.common.model.AuthResponse;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.Customer;

/**
 * 
 * @author Balaji N
 *
 */
public interface AdminReservationService {
	
	public CustomLogger getLogger(String clientCode, String device) throws Exception;

	public LocationListResponse getLocationList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, boolean isFullData) throws Exception;

	public BaseResponse deleteLocation(String clientCode, CustomLogger customLogger, String langCode, String device,String transId, String locationId) throws Exception;

	public BaseResponse openCloseLocation(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String enable) throws Exception;
	
	public BaseResponse addLocation(String clientCode, CustomLogger customLogger, String langCode, String device,String transId, Location location) throws Exception;
	
	public BaseResponse updateLocation(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, Location location) throws Exception;

	public BaseResponse getEventList(String clientCode, CustomLogger customLogger, String langCode, String device,String transId) throws Exception;

	public BaseResponse deleteEvent(String clientCode, CustomLogger customLogger, String langCode, String device,String transId, String eventId) throws Exception;

	public BaseResponse openCloseEvent(String clientCode, CustomLogger customLogger, String langCode, String device,String transId, String eventId, String enable) throws Exception;

	public BaseResponse addEvent(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, Event event) throws Exception;
	
	public BaseResponse updateEvent(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, Event event) throws Exception;
	
	
	public BaseResponse getEventDateTimeList(String clientCode, CustomLogger customLogger, String langCode, String device,String transId) throws Exception;

	public BaseResponse openCloseEventDateTime(String clientCode, CustomLogger customLogger, String langCode, String device,String transId, String eventDateTimeId, String enable) throws Exception;

	public BaseResponse addEventDateTime(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, EventDateTime eventDateTime) throws Exception;
	
	public BaseResponse updateEventDateTime(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, EventDateTime eventDateTimeId) throws Exception;
	
	public CalendarOverviewResponse getCalendarOverview(String clientCode, CustomLogger customLogger, String langCode, String device, String transId,String startDate, String endDate) throws Exception;

	public SeatsCalendarView getSeatsCalendarView(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId,
			String date, String time,String showRemainderIcons) throws Exception;
	
	public DailyCalendarView getDailyCalendarView(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId,
			String date,String showRemainderIcons) throws Exception;

	public EventListResponse getEventListByLocationId(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, boolean isFullDate) throws Exception;

	public DateListResponse getCalendarDateList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId) throws Exception;

	public HomePageResponse getHomePageDetails(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String privilegeName) throws Exception;

	public TimeListResponse getSeatViewTimeList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId, String date) throws Exception;

	public BaseResponse updateLocation(CustomLogger customLogger, Location location) throws Exception;

	public EventDateTime getEventDateTimeById(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String eventDateTimeId)  throws Exception;

	public CustomerResponse getCustomerNames(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String customerNames) throws Exception;
	
	public BaseResponse updateEventSeats(CustomLogger customLogger, EventDateTime eventDateTime) throws Exception;

	public ReservationReportResponse getReservationReports(ReservationReportRequest reservationReportRequest) throws Exception;
	public ReservationReportCheckboxResponse getEventDateTimeForLocEventDateRange(ReservationReportRequest resvReportRequest) throws Exception;

	public CalendarOverviewDetailsResponse getCalendarOverViewDetails(CustomLogger customLogger, String clientCode,  String langCode, String device, String transId, String eventDateTimeId) throws Exception;

	public CustomerResponse getCustomerById(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String customerId) throws Exception;

	public FutureReservationResponse getFutureReservations(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String customerId) throws Exception;

	public PastReservationResponse getPastReservations(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String customerId) throws Exception;

	public ReservationDetailsResponse getReservationByScheduleId(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String scheduleId) throws Exception;

	public RegistrationInfoResponse getRegistrationInfo(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId) throws Exception;

	public ReservationSearchResponse getReservationSearch(CustomLogger customLogger, SearchRequestData resvSearchRequest) throws Exception;

	public TablePrintViewResponse getTablePrintView(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String eventId, String locationId,
			String date) throws Exception;

	public SearchFieldsResponse getDynamicSearchFields(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String searchCategory) throws Exception;

	public ClientDetailsResponse getClientDetails(CustomLogger customLogger, SearchRequestData resvSearchRequest) throws Exception;

	public GraphResponse getGraphDetails(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String locationId, String date) throws Exception;
	
	public AuthResponse updateCustomer(CustomLogger customLogger, Customer customer) throws Exception;

	public PrivilageResponse getAccessesPrivilages(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception;

	public LocationListResponse getLocationById(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, boolean isFullData) throws Exception;

	public BaseResponse getEventById(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String eventId) throws Exception;
	
	public ClientDetailsResponse getBlockedClientDetails(CustomLogger customLogger, SearchRequestData searchRequestData) throws Exception;

	public BaseResponse addReservationReportConfig(CustomLogger customLogger, ReservationReportConfig resvReportConfig) throws Exception;

	public ReservationReportConfigResponse getReservationReportConfig(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse deleteResvReportConfigById(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String reportConfigId) throws Exception;
	
	public BaseResponse getCampaigns(String clientCode, CustomLogger customLogger, String langCode, String device, String transId) throws Exception;

	public BaseResponse getReservationReminders(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String locationId, String campaignId, String date)
			throws Exception;

	public BaseResponse getReservationStatusList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse getNotifyRemainderStatusList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse updateNotifyStatus(CustomLogger customLogger, NotifyRequest notifyRequest) throws Exception;

	public BaseResponse changeSchedulerStatus(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String status) throws Exception;

	public BaseResponse getInBoundCallReportList(CustomLogger customLogger, InBoundCallReportRequest inBCReportRequest) throws Exception;
	
	public BaseResponse getOutBoundCallReportList(CustomLogger customLogger, OutBoundCallReportRequest outBCReportRequest) throws Exception;

	public BaseResponse getTransStates(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse updateScreenedStatus(CustomLogger customLogger,	String clientCode, String device, String langCode, String token,String transId, String scheduleId, String screened)	throws Exception;
	
	public DateJSListResponse getJSCalendarDateList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId) throws Exception;
	
	public BaseResponse updateSeatReservedStatus(CustomLogger customLogger,String clientCode, String device, String langCode, String token,String transId, String seatId, String reserve) throws Exception;
}

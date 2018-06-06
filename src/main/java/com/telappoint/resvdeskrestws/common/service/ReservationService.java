package com.telappoint.resvdeskrestws.common.service;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.common.model.AuthResponse;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientInfo;
import com.telappoint.resvdeskrestws.common.model.CompanyListResponse;
import com.telappoint.resvdeskrestws.common.model.ConfPageContactDetailsResponse;
import com.telappoint.resvdeskrestws.common.model.ConfirmResvResponse;
import com.telappoint.resvdeskrestws.common.model.ConfirmationPageRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.DepartmentListResponse;
import com.telappoint.resvdeskrestws.common.model.EventDatesResponse;
import com.telappoint.resvdeskrestws.common.model.EventHistoryResponse;
import com.telappoint.resvdeskrestws.common.model.EventTimesResponse;
import com.telappoint.resvdeskrestws.common.model.HoldResvResponse;
import com.telappoint.resvdeskrestws.common.model.IVRCallRequest;
import com.telappoint.resvdeskrestws.common.model.ListOfThingsResponse;
import com.telappoint.resvdeskrestws.common.model.LocationListResponse;
import com.telappoint.resvdeskrestws.common.model.LoginInfoResponse;
import com.telappoint.resvdeskrestws.common.model.LoginInfoRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.NameRecordResponse;
import com.telappoint.resvdeskrestws.common.model.ProcedureListResponse;
import com.telappoint.resvdeskrestws.common.model.ReservedEventResponse;
import com.telappoint.resvdeskrestws.common.model.ResvDetailsResponse;
import com.telappoint.resvdeskrestws.common.model.ResvDetailsRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.ResvVerificationDetailsRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.SeatResponse;
import com.telappoint.resvdeskrestws.common.model.SpecificDateResponse;
import com.telappoint.resvdeskrestws.common.model.UpdateRecordResponse;
import com.telappoint.resvdeskrestws.common.model.VerifyResvResponse;

public interface ReservationService {

	public CustomLogger getLogger(String clientCode,String device) throws Exception;

	public ClientInfo getClientInfo(CustomLogger logger, String clientCode, String langCode, String device, String loginFirst, String param1, String param2) throws Exception;

	public LoginInfoResponse getLoginInfo(CustomLogger logger, String clientCode, String langCode, String device, String token, String transId, String action) throws Exception;

	public AuthResponse authenticateCustomer(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String loginParams) throws Exception;

	public ResvDetailsResponse getResvDetailsSelectionInfo(CustomLogger logger, String clientCode, String device, String langCode,String token, String transId) throws Exception;

	public CompanyListResponse getCompanyList(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId) throws Exception;

	public ProcedureListResponse getProcedureList(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String companyId) throws Exception;

	public LocationListResponse getLocationList(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String procedureId) throws Exception;

	public DepartmentListResponse getDepartmentList(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String locationId) throws Exception;

	public BaseResponse getEventList(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String departmentId,String locationId) throws Exception;

	public EventDatesResponse getEventDates(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String eventId, String locationId) throws Exception;

	public EventTimesResponse getEventTimes(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String eventId, String locationId, String date) throws Exception;

	public SeatResponse getEventSeats(CustomLogger logger, String clientCode, String device, String langCode, String token, String transId, String eventDateTimeId) throws Exception;
	
	public HoldResvResponse holdReservation(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String companyId, String procedureId, String locationId,
			String departmentId, String eventId, String eventDateTimeId, String seatId, String customerId) throws Exception;
	
	public VerifyResvResponse getVerifyReservationDetails(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String scheduleId, String customerId, String loginFirst) throws Exception;
			
	public ConfirmResvResponse confirmReservation(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String scheduleId, String customerId, String comment) throws Exception;
	
	public BaseResponse releaseHoldEventTime(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String scheduleId) throws Exception;
	
	public String clearTheCache(CustomLogger logger, String clientCode) throws Exception;
	
	public String clearTheProperty(String fileName) throws Exception;

	public BaseResponse cancelReservation(String clientCode, CustomLogger customLogger, String device, String langCode, String token,String transId, String scheduleId, String customerId) throws Exception;
	
	public EventHistoryResponse getEventsHistory(String clientCode, CustomLogger customLogger, String device,String langCode, String token, String transId) throws Exception;

	public NameRecordResponse getNameRecordVxml(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception;
	
	public UpdateRecordResponse updateRecordVxml(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String scheduleId, String customerId, String filePath, String fileName, String recordDurationInSec) throws Exception;
	
	public ListOfThingsResponse listOfThingsToBring(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String eventId) throws Exception;

	public ConfPageContactDetailsResponse getConfPageContactDetails(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String locationId) throws Exception;

	public LoginInfoRightSideContentResponse getLoginInfoRightSideContent(CustomLogger logger, String clientCode, String langCode, String device, String token, String transId, String loginFirst) throws Exception;
	
	public ResvDetailsRightSideContentResponse getResvDetailsRightSideContent(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String loginFirst) throws Exception;

	public ResvVerificationDetailsRightSideContentResponse getResvVerifyDetailsRightSideContent(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String loginFirst) throws Exception;

	public ConfirmationPageRightSideContentResponse getConfirmationPageRightSideContent(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String loginFirst) throws Exception;

	public ReservedEventResponse getReservedEvents(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String customerId) throws Exception;
	
	public SpecificDateResponse getSpecificDateVXML(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse disconnectVXML(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId) throws Exception;
	
	public BaseResponse isAllowDuplicateResv(String clientCode, CustomLogger customLogger, 
			String device, String langCode, String token, String transId, String customerId, String eventId) throws Exception;

	public BaseResponse noApptInAllLocation(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId) throws Exception;

	public BaseResponse noApptInSelectedLocation(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId) throws Exception;

	public BaseResponse noApptInSelectedEvent(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId,
			String eventId) throws Exception;
	
	public BaseResponse noApptInSelectedDate(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId,
			String eventId, String date) throws Exception;

	public BaseResponse noApptInSelectedTime(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId,
			String eventId, String date, String eventDateTimeId) throws Exception;
	
	public BaseResponse getCancelNoResv(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse getCancelVerify(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse getWelcomePageVxml(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception;

	public BaseResponse saveOrUpdateIVRCallLog(CustomLogger customLogger, IVRCallRequest ivrCallRequest) throws Exception;	
	
	public AuthResponse authenticateCustomerForCancel(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String loginParams)
			throws Exception;
}

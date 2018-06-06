package com.telappoint.resvdeskrestws.common.clientdb.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverviewDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverviewResponse;
import com.telappoint.resvdeskrestws.admin.model.CampaignResponse;
import com.telappoint.resvdeskrestws.admin.model.ClientDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.CustomerResponse;
import com.telappoint.resvdeskrestws.admin.model.DailyCalendarView;
import com.telappoint.resvdeskrestws.admin.model.DateJSListResponse;
import com.telappoint.resvdeskrestws.admin.model.DateListResponse;
import com.telappoint.resvdeskrestws.admin.model.DisplayNames;
import com.telappoint.resvdeskrestws.admin.model.DynamicSQL;
import com.telappoint.resvdeskrestws.admin.model.DynamicTableItems;
import com.telappoint.resvdeskrestws.admin.model.Event;
import com.telappoint.resvdeskrestws.admin.model.EventDateTime;
import com.telappoint.resvdeskrestws.admin.model.EventDateTimeResponse;
import com.telappoint.resvdeskrestws.admin.model.EventListResponse;
import com.telappoint.resvdeskrestws.admin.model.FutureReservationResponse;
import com.telappoint.resvdeskrestws.admin.model.GraphResponse;
import com.telappoint.resvdeskrestws.admin.model.HomePageResponse;
import com.telappoint.resvdeskrestws.admin.model.InBoundCallReportRequest;
import com.telappoint.resvdeskrestws.admin.model.InBoundCallReportResponse;
import com.telappoint.resvdeskrestws.admin.model.Location;
import com.telappoint.resvdeskrestws.admin.model.LocationListResponse;
import com.telappoint.resvdeskrestws.admin.model.NotifyRequest;
import com.telappoint.resvdeskrestws.admin.model.OutBoundCallReportRequest;
import com.telappoint.resvdeskrestws.admin.model.OutBoundCallReportResponse;
import com.telappoint.resvdeskrestws.admin.model.PastReservationResponse;
import com.telappoint.resvdeskrestws.admin.model.PrivilageResponse;
import com.telappoint.resvdeskrestws.admin.model.RegistrationField;
import com.telappoint.resvdeskrestws.admin.model.ReminderStatusResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationRemindersResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportCheckboxResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportRequest;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationSearchResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationStatusResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchFieldsResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchRequestData;
import com.telappoint.resvdeskrestws.admin.model.SeatsCalendarView;
import com.telappoint.resvdeskrestws.admin.model.TablePrintViewResponse;
import com.telappoint.resvdeskrestws.admin.model.TimeListResponse;
import com.telappoint.resvdeskrestws.admin.model.TransStateResponse;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ConfirmResvResponse;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.model.EventDate;
import com.telappoint.resvdeskrestws.common.model.HoldResvResponse;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.LoginField;
import com.telappoint.resvdeskrestws.common.model.Options;
import com.telappoint.resvdeskrestws.common.model.ResvSysConfig;

/**
 * 
 * @author Balaji
 *
 */
public interface AdminDAO {

	public boolean updateTransactionState(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long transId, Integer state, ClientDeploymentConfig cdConfig)
			throws Exception;

	public void getAdminLocationList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData,
			LocationListResponse locationListResponse) throws Exception;

	public void deleteLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, BaseResponse baseResponse)
			throws Exception;

	public void updateLocationStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String enable,
			BaseResponse baseResponse) throws Exception;

	public void updateLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Location location, BaseResponse baseResponse)
			throws Exception;

	public void addLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Location location, BaseResponse baseResponse)
			throws Exception;

	public void getAdminEventList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData,
			EventListResponse eventListResponse) throws Exception;

	public void getAdminEventListByLocationId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId,
			final boolean isFullData, final EventListResponse eventListResponse) throws Exception;

	public void deleteEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventId, BaseResponse baseResponse)
			throws Exception;

	public void updateEventStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventId, String enable,
			BaseResponse baseResponse) throws Exception;

	public void addEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Event event, BaseResponse baseResponse) throws Exception;

	public void updateEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Event event, BaseResponse baseResponse)
			throws Exception;

	public void getAdminEventDateTimeList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device,
			final EventDateTimeResponse eventDateTimeResponse) throws Exception;

	public void updateEventDateTimeStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventDateTimeId, String enable,
			BaseResponse baseResponse) throws Exception;

	public void addEventDateTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, EventDateTime eventDateTime,
			BaseResponse baseResponse) throws Exception;

	public void updateEventDateTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, EventDateTime eventDateTime,
			BaseResponse baseResponse) throws Exception;

	public void getCalendarOverview(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String startDate, String endDate,
			CalendarOverviewResponse calendarOverviewResponse) throws Exception;

	public void getSeatsCalendarView(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, String device, String locationId,
			String eventId, String date, final long eventDateTimeId, final SeatsCalendarView seatsCalendarView,String showRemainderIcons) throws Exception;

	public void populateDynamicTableItems(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String category, final DynamicTableItems dynamicTableItems)
			throws Exception;

	public void populateDynamicSQL(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, DynamicSQL> dynamicSQLMap) throws Exception;

	public <T> void executeQueryDynamicQuery(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String sql, Object[] dynamicSQLInject,
			final String aliasColumnNames, final String dataTypes, final String javaRef, final String title, final List<T> list, Class<T> object) throws Exception;

	public long getEventDateTimeId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			String date, String time);

	public void getDailyCalendarView(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, String device, String locationId,
			String eventId, String date, final DailyCalendarView dailyCalendarView,String showRemainderIcons) throws Exception;

	public void getDateList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			DateListResponse dateListResponse) throws Exception;

	public void getPrivileges(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String privilegeName,
			final HomePageResponse homePageResponse) throws Exception;

	public void getDisplayNames(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final DisplayNames displayNames) throws Exception;

	public void getSeatsTimeList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			String dateYYYYMMDD, TimeListResponse timeListResponse) throws Exception;

	public void getEventDateTimeById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventDateTimeId,
			EventDateTime eventDateTime) throws Exception;

	public void getCustomerNames(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, final String device, String customerName,
			CustomerResponse customerResponse) throws Exception;

	public boolean validateEventDateTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, EventDateTime eventDateTime) throws Exception;

	public void updateEventSeats(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, EventDateTime eventDateTime,
			BaseResponse baseResponse) throws Exception;

	public void getReservationReports(JdbcCustomTemplate jdbcCustomTemplate, DynamicSQL dynamicSQL, DynamicTableItems dynamicTableItems, String reportSQLFinal,
			ReservationReportRequest reservationReportRequest, ReservationReportResponse reservationReportResponse) throws Exception;

	public void populateAlias(Map<String, String> aliasMap, List<Options> options) throws Exception;

	public void populateAlias(Map<String, String> aliasMap, Object responseObject, String device);

	public void confirmReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, final String langCode, final String token,
			final String transId, final String scheduleId, final String customerId, final String comment, final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig,
			final ConfirmResvResponse confirmResponse) throws Exception;

	public void holdReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, final String transId, final String companyId,
			final String procedureId, final String locationId, final String departmentId, final String eventId, final String eventDateTimeId, final String seatId,
			final String customerId, final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig, final HoldResvResponse holdResponse) throws Exception;

	public void getCalendarOverviewDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventDateTimeId,
			CalendarOverviewDetailsResponse calOverviewDetailsResponse) throws Exception;

	public void getCustomerById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String customerId,
			CustomerResponse customerResponse) throws Exception;

	public void getFutureReservations(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String customerId,
			FutureReservationResponse futureResvResponse) throws Exception;

	public void getPastReservations(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String customerId,
			PastReservationResponse pastResvResponse) throws Exception;

	public void getReservationByScheduleId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String scheduleId,
			final ClientDeploymentConfig cdConfig, ReservationDetailsResponse resvDetails) throws Exception;

	public void getRegistrationParams(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, List<RegistrationField>> map) throws Exception;

	public void getReservationSearch(JdbcCustomTemplate jdbcCustomTemplate, DynamicTableItems dynamicTableItems, String reportSQLFinal, MapSqlParameterSource parameterSource,
			SearchRequestData resvSearchRequest, ReservationSearchResponse reservationSearchResponse) throws Exception;

	public void getTablePrintView(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			String dateYYYYMMDD, DynamicTableItems dynamicTableItems, String sqlFinal, TablePrintViewResponse tablePrintView) throws Exception;

	public boolean updateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Customer customer, ClientDeploymentConfig cdConfig) throws Exception;

	public long saveCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Customer customer, ClientDeploymentConfig cdConfig) throws Exception;

	public Customer getCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long customerId) throws Exception;

	public void getDynamicSearchFields(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String searchCategory,
			boolean isAllFields, SearchFieldsResponse searchFieldsResponse);

	public void getClientDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, DynamicTableItems dynamicTableItems, String searchSQLFinal,
			MapSqlParameterSource mapSqlParameterSource, SearchRequestData searchRequestData, ClientDetailsResponse clientDetailsResponse) throws Exception;

	public void authenticateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Map<String, String> loginParamsMap,
			Map<String, LoginField> loginFieldMap, final Object response) throws Exception;

	public void getGraphDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String date,
			final GraphResponse graphResponse) throws Exception;

	public boolean validateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Map<String, String> paramsMap,
			Map<String, LoginField> loginFieldMap) throws Exception;

	public void getAccessesPrivilages(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, PrivilageResponse prePrivilageResponse)
			throws Exception;

	public void getLocationById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, boolean isFullData,
			LocationListResponse locationListResponse) throws Exception;

	public void getEventById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventId, final boolean isFullData,
			final EventListResponse eventListResponse) throws Exception;

	public Customer getCustomerIncludingDeletedCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long customerId) throws Exception;

	public void getReservationReminders(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String campaignId,
			String dateYYYYMMDD, DynamicTableItems dynamicTableItems, String finalSQL, MapSqlParameterSource parameterSource, ReservationRemindersResponse resvReminder)
			throws Exception;

	public void getCampaigns(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData,
			final CampaignResponse campaignResponse) throws Exception;

	public void getReservationStatusList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, boolean isFullData,
			ReservationStatusResponse resvStatusResponse) throws Exception;

	public void getNotifyRemainderStatusList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, boolean isFullData,
			ReminderStatusResponse reminderStatusResponse) throws Exception;

	public void updateNotifyStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, NotifyRequest notifyRequest, BaseResponse baseResponse) throws Exception;

	public boolean changeSchedulerStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String status) throws Exception;

	public void getInBoundCallReportList(JdbcCustomTemplate jdbcCustomTemplate, DynamicTableItems dynamicReportItems, String reportSQLFinal, MapSqlParameterSource parameterSource,
			InBoundCallReportRequest inBoundCallReportRequest, InBoundCallReportResponse inBoundCallReportResponse) throws Exception;

	public void getOutBoundCallReportList(JdbcCustomTemplate jdbcCustomTemplate, DynamicTableItems dynamicReportItems, String reportSQLFinal,
			MapSqlParameterSource parameterSource, OutBoundCallReportRequest outBoundCallReportRequest, OutBoundCallReportResponse outBoundCallReportResponse) throws Exception;

	public void getTransStates(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String transId,
			final TransStateResponse transStateResponse) throws Exception;

	public void updateScreenedStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId, String screened, BaseResponse baseResponse)
			throws Exception;

	public void getEventDates(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, String eventId, String locationId,
			final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig, final Map<String, EventDate> availableDateMap) throws Exception;

	public void getBookedDates(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, String eventId, String locationId,
			final Map<String, EventDate> bookedDateMap) throws Exception;

	public void getJSDateList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			final DateJSListResponse dateJsListResponse) throws Exception;

	public void updateBookedSeat(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long eventDateTimeId, boolean isIncrease) throws Exception;

	public void getEventDateTimeForLocEventDateRange(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ReservationReportRequest resvReportRequest,
			ReservationReportCheckboxResponse resvReportCheckboxResonse) throws Exception;
	
	public boolean isGreaterThenTodayDate(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String date, String time, ClientDeploymentConfig cdConfig) throws Exception;

	public Long getSeatScheduleId(JdbcCustomTemplate jdbcCustomTemplate,CustomLogger customLogger, String seatId) throws Exception;
	public void updateSeatReservedStatus(JdbcCustomTemplate jdbcCustomTemplate,CustomLogger customLogger, String seatId, String reserved,BaseResponse baseResponse) throws Exception;

}

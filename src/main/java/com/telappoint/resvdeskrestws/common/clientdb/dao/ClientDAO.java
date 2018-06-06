package com.telappoint.resvdeskrestws.common.clientdb.dao;

import java.util.List;
import java.util.Map;

import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ConfPageContactDetailsResponse;
import com.telappoint.resvdeskrestws.common.model.ConfirmResvResponse;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.model.EventDate;
import com.telappoint.resvdeskrestws.common.model.EventHistory;
import com.telappoint.resvdeskrestws.common.model.HoldResvResponse;
import com.telappoint.resvdeskrestws.common.model.IVRCallRequest;
import com.telappoint.resvdeskrestws.common.model.IVRCallResponse;
import com.telappoint.resvdeskrestws.common.model.IVRXml;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.Language;
import com.telappoint.resvdeskrestws.common.model.ListOfThingsResponse;
import com.telappoint.resvdeskrestws.common.model.LoginField;
import com.telappoint.resvdeskrestws.common.model.Options;
import com.telappoint.resvdeskrestws.common.model.ResvSysConfig;
import com.telappoint.resvdeskrestws.common.model.Seat;
import com.telappoint.resvdeskrestws.common.model.VerifyResvResponse;

public interface ClientDAO {
	public ResvSysConfig getResvSysConfig(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception;

	public List<Options> getCompanyList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, final List<Options> companyOptions) throws Exception;

	public List<Options> getProcedureList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String companyId, String device, final List<Options> procedureOptions) throws Exception;

	public List<Options> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, Long procedureId, final List<Options> locationOptions) throws Exception;

	public List<Options> getDepartmentList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Integer locationId, final List<Options> departmentOptions) throws Exception;

	public List<Options> getEventList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Integer departmentId, Integer locationId, final List<Options> eventOptions) throws Exception;

	public void authenticateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Map<String, String> loginParamsMap,Map<String, LoginField> loginFieldMap,final Object response) throws Exception;
	
	public boolean updateCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Customer customer, ClientDeploymentConfig cdConfig) throws Exception;
	
	public long saveCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Customer customer, ClientDeploymentConfig cdConfig) throws Exception;
	
	public Customer getCustomer(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long customerId) throws Exception;

	public void loadCustomerByPhoneNumber(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String phone, final Customer customer) throws Exception;

	public String getDefaultLangCode(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception;

	public boolean isValidToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String device, String token,ClientDeploymentConfig cdConfig) throws Exception;
	
	public boolean isValidToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String device, String token, ClientDeploymentConfig cdConfig,String customerId) throws Exception;

	public String saveAndGetToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String device,ClientDeploymentConfig cdConfig, String param2) throws Exception;

	public String saveTransIdAndGet(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String identifier, String userName) throws Exception;

	public boolean updateTransactionState(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long transId, Integer state,ClientDeploymentConfig cdConfig) throws Exception;

	// -- cache methods start
	public void getLoginParams(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, List<LoginField>> map) throws Exception;

	public void getI18nAliasesMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception;

	public void getI18nDesignTemplatesMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception;

	public void getI18nButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception;

	public void getI18nDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception;

	public void getI18nPageContentMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception;
	
	public void getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception;

	public List<Language> getLangDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final List<Language> languageList) throws Exception;

	public void loadVXML(final JdbcCustomTemplate jdbcCustomTemplate, final String mainKey, final Map<String, Map<String, IVRXml>> cacheVxmlMap) throws Exception;

	// -- cache methods end

	public void getEventDates(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String eventId, String locationId, final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig, Map<String, EventDate> availableDateMap) throws Exception;

	public void getBookedDates(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, String eventId, String locationId, final Map<String, EventDate> bookedDateMap) throws Exception;
	
	public void getEventsHistory(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, final List<EventHistory> eventHistoryList) throws Exception;

	public void getEventTimes(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String eventId, String locationId, String date, Map<String, String> availableTimeMap) throws Exception;

	public void getEventSeats(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String eventDateTimeId, final List<Seat> seatList) throws Exception;

	public void holdReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger,String device, String transId, String companyId, String procedureId, String locationId, String departmentId,
			String eventId, String eventDateTimeId, String seatId, String customerId, ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig, HoldResvResponse holdResponse) throws Exception;
	
	public void getVerifyReservationDetails(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, final String langCode, final String transId, final String scheduleId,
			final String customerId, String loginFirst, ResvSysConfig resvSysConfig, ClientDeploymentConfig cdConfig, final VerifyResvResponse verifyResponse) throws Exception;

	public void confirmReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger,String device, String langCode, String token, String transId, String scheduleId, String customerId, final String comment,
			final ResvSysConfig resvSysConfig, final ClientDeploymentConfig cdConfig, final ConfirmResvResponse confirmResponse) throws Exception;

	public void releaseHoldEventTime(final JdbcCustomTemplate jdbcCustomTemplate, final String scheduleId, final BaseResponse baseResponse) throws Exception;

	public void cancelReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String scheduleId, final String customerId, final BaseResponse baseResponse) throws Exception;

	public void expireTheToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger,String clientCode, String device, String token,ClientDeploymentConfig cdConfig) throws Exception;

	public void updateTokenCustomerId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String token, String device, Long customerId, ClientDeploymentConfig cdConfig) throws Exception;

	public void updateRecordVxml(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId, String customerId, String filePath, String fileName,
			String recordDurationInSec,ClientDeploymentConfig cdConfig) throws Exception;

	public void listOfThingsToBring(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, String eventId, ListOfThingsResponse listOfThingsResponse) throws Exception;

	public void getConfPageContactDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, String locationId,
			ConfPageContactDetailsResponse confPageContactDetailsResponse) throws Exception;

	public void getReservedEvents(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String customerId, String timeZone, String device, List<EventHistory> reservedEventList) throws Exception;
	
	public void prepareResvDetails(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, Long scheduleId, final ClientDeploymentConfig cdConfig,
			final ConfirmResvResponse confirmResvResponse) throws Exception;
	
	public void populateAlias(Map<String, String> aliasMap, List<Options> options) throws Exception;
	
	public void populateAlias(Map<String, String> aliasMap, Object responseObject, String device);
	
	public boolean isAllowDuplicateResv(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long customerId,long eventId) throws Exception;

	public boolean noApptInAllLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception;
	public boolean noApptInSelectedLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String locationId) throws Exception;
	public boolean noApptInSelectedEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String locationId, String eventId) throws Exception;
	public boolean noApptInSelectedDate(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger,String locationId, String eventId, String date) throws Exception;
	public boolean noApptInSelectedTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger,String locationId, String eventId, String date,String time) throws Exception;
	
	public void updateBookedSeat(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, Long eventDateTimeId, boolean isIncrease) throws Exception;
	public void saveOrUpdateIVRCallLog(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ClientDeploymentConfig cdConfig, IVRCallRequest ivrCallRequest, IVRCallResponse ivrCallResponse) throws Exception;
	public void getEventDisplayTimes(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger,final Map<String, String> displayAvailableTimMap) throws Exception;
	
	
}

package com.telappoint.resvdeskrestws.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.constants.AdminFlowStateConstants;
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
import com.telappoint.resvdeskrestws.admin.model.RegistrationInfoResponse;
import com.telappoint.resvdeskrestws.admin.model.ReminderStatusResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationRemindersResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportCheckboxResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfig;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportConfigResponse;
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
import com.telappoint.resvdeskrestws.admin.service.AdminReservationService;
import com.telappoint.resvdeskrestws.common.clientdb.dao.AdminDAO;
import com.telappoint.resvdeskrestws.common.components.CacheComponent;
import com.telappoint.resvdeskrestws.common.components.ConnectionPoolUtil;
import com.telappoint.resvdeskrestws.common.components.EmailComponent;
import com.telappoint.resvdeskrestws.common.constants.DisplayButtonsConstants;
import com.telappoint.resvdeskrestws.common.constants.FlowStateConstants;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;
import com.telappoint.resvdeskrestws.common.constants.ReservataionStatus;
import com.telappoint.resvdeskrestws.common.masterdb.dao.MasterDAO;
import com.telappoint.resvdeskrestws.common.masterdb.domain.Client;
import com.telappoint.resvdeskrestws.common.model.AuthResponse;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.model.ErrorConfig;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.LoginField;
import com.telappoint.resvdeskrestws.common.model.ResvSysConfig;
import com.telappoint.resvdeskrestws.common.utils.CoreUtils;
import com.telappoint.resvdeskrestws.common.utils.DateUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointException;

/**
 * 
 * @author Balaji N
 *
 */
@Service
public class AdminReservationServiceImpl implements AdminReservationService {

	@Autowired
	private MasterDAO masterDAO;

	@Autowired
	private AdminDAO adminDAO;

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private EmailComponent emailComponent;

	@Autowired
	private ConnectionPoolUtil connectionPoolUtil;

	public CustomLogger getLogger(String clientCode, String device) throws Exception {
		return cacheComponent.getLogger(clientCode, device, true);
	}

	@Override
	public LocationListResponse getLocationList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, boolean isFullData) throws Exception {
		LocationListResponse locationListResponse = new LocationListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_ADMIN_LOCATION_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getAdminLocationList(jdbcCustomTemplate, customLogger, langCode, device, isFullData, locationListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);

		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return locationListResponse;
	}

	@Override
	public BaseResponse deleteLocation(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.DELETE_LOCATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.deleteLocation(jdbcCustomTemplate, customLogger, langCode, device, locationId, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse openCloseLocation(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String enable)
			throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.OPEN_CLOSE_LOCATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateLocationStatus(jdbcCustomTemplate, customLogger, langCode, device, locationId, enable, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse addLocation(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, Location location) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.ADD_LOCATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.addLocation(jdbcCustomTemplate, customLogger, langCode, device, location, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse updateLocation(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, Location location) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_LOCATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateLocation(jdbcCustomTemplate, customLogger, langCode, device, location, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse getEventList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId) throws Exception {
		EventListResponse eventListResponse = new EventListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_ADMIN_EVENT_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getAdminEventList(jdbcCustomTemplate, customLogger, langCode, device, true, eventListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventListResponse;
	}

	@Override
	public BaseResponse deleteEvent(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String eventId) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.DELETE_EVENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.deleteEvent(jdbcCustomTemplate, customLogger, langCode, device, eventId, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse openCloseEvent(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String eventId, String enable)
			throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.OPEN_CLOSE_EVENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateEventStatus(jdbcCustomTemplate, customLogger, langCode, device, eventId, enable, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse addEvent(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, Event event) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.ADD_EVENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.addEvent(jdbcCustomTemplate, customLogger, langCode, device, event, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse updateEvent(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, Event event) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_EVENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateEvent(jdbcCustomTemplate, customLogger, langCode, device, event, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse getEventDateTimeList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId) throws Exception {
		EventDateTimeResponse eventDateTimeResponse = new EventDateTimeResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_ADMIN_EVENT_DATE_TIME_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getAdminEventDateTimeList(jdbcCustomTemplate, customLogger, langCode, device, eventDateTimeResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventDateTimeResponse;
	}

	@Override
	public BaseResponse openCloseEventDateTime(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String eventDateTimeId, String enable)
			throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.OPEN_CLOSE_EVENT_DATE_TIME.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateEventDateTimeStatus(jdbcCustomTemplate, customLogger, langCode, device, eventDateTimeId, enable, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse addEventDateTime(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId,
			EventDateTime eventDateTime) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.ADD_EVENT_DATE_TIME.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			eventDateTime.setDate(DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(eventDateTime.getDate()));
			eventDateTime.setTime(DateUtils.convert12To24HoursHHMMSSFormat(eventDateTime.getTime()));
			boolean isValidateSuccess = adminDAO.validateEventDateTime(jdbcCustomTemplate, customLogger, eventDateTime);
			if (isValidateSuccess == false) {
				baseResponse.setResponseStatus(false);
				baseResponse.setResponseMessage("This record already exist. Try again!");
			} else {
				adminDAO.addEventDateTime(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventDateTime, baseResponse);
			}

			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse updateEventDateTime(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, EventDateTime eventDateTime)
			throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_EVENT_DATE_TIME.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			eventDateTime.setDate(DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(eventDateTime.getDate()));
			eventDateTime.setTime(DateUtils.convert12To24HoursHHMMSSFormat(eventDateTime.getTime()));
			boolean isValidateSuccess = adminDAO.validateEventDateTime(jdbcCustomTemplate, customLogger, eventDateTime);
			
			boolean isGreaterThenToday = adminDAO.isGreaterThenTodayDate(jdbcCustomTemplate, customLogger, eventDateTime.getDate(), eventDateTime.getTime(),cdConfig);
			if(isGreaterThenToday) {
				String message = PropertyUtils.getValueFromProperties("EVENT_DATE_TIME_GREATER_THEN_TODAY_MESSAGE", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				baseResponse.setResponseStatus(false);
				if(message != null) {
					baseResponse.setResponseMessage(message);
				} else {
					baseResponse.setResponseMessage("Past date and time updation is not allowed!");
				}
			} else if (isValidateSuccess == false) {
				baseResponse.setResponseStatus(false);
				baseResponse.setResponseMessage("This record already exist. Try again!");
			} else {
				adminDAO.updateEventDateTime(jdbcCustomTemplate, customLogger, langCode, device, eventDateTime, baseResponse);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse updateEventSeats(CustomLogger customLogger, EventDateTime eventDateTime) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = eventDateTime.getClientCode();
		String device = eventDateTime.getDevice();
		String langCode = eventDateTime.getLangCode();
		String transId = eventDateTime.getTransId();
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_EVENT_SEATS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			eventDateTime.setDate(DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(eventDateTime.getDate()));
			eventDateTime.setTime(DateUtils.convert12To24HoursHHMMSSFormat(eventDateTime.getTime()));
			boolean isValidateSuccess = adminDAO.validateEventDateTime(jdbcCustomTemplate, customLogger, eventDateTime);

			if (isValidateSuccess == false) {
				adminDAO.updateEventSeats(jdbcCustomTemplate, customLogger, langCode, device, eventDateTime, baseResponse);
			} else {
				baseResponse.setResponseStatus(false);
				baseResponse.setResponseMessage("Record is not present in database to update seats. Try again!");
			}

			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public CalendarOverviewResponse getCalendarOverview(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String startDate,
			String endDate) throws Exception {
		CalendarOverviewResponse calendarOverviewReponse = new CalendarOverviewResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_ADMIN_CALENDAR_OVERVIEW_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			startDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(startDate);
			endDate = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(endDate);
			adminDAO.getCalendarOverview(jdbcCustomTemplate, customLogger, langCode, device, startDate, endDate, calendarOverviewReponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return calendarOverviewReponse;
	}

	private void updateTransactionState(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Long transId, ErrorConfig errorConfig,
			ClientDeploymentConfig cdConfig) throws Exception {
		adminDAO.updateTransactionState(jdbcCustomTemplate, customLogger, transId, errorConfig.getErrorId(), cdConfig);
	}

	private void throwException(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Long transId, ErrorConfig errorConfig,
			ClientDeploymentConfig cdConfig, Exception e) throws Exception {
		updateTransactionState(jdbcCustomTemplate, customLogger, device, transId, errorConfig, cdConfig);
		if (e instanceof TelAppointDBException) {
			TelAppointDBException tade = (TelAppointDBException) e;
			tade.setErrorCode("" + errorConfig.getErrorId());
			tade.setErrorDescription(errorConfig.getErrorDescription());
			tade.setErrorMessage(errorConfig.getErrorDescription());
			if (CoreUtils.isIVR(device)) {
				tade.setErrorVXML(errorConfig.getErrorVXML());
			}
			tade.setSendAlert(errorConfig.getSendAlert());
			throw tade;
		} else {
			TelAppointException tae = new TelAppointException(e);
			tae.setErrorCode("" + errorConfig.getErrorId());
			tae.setErrorMessage(errorConfig.getErrorMessage());
			tae.setErrorDescription(errorConfig.getErrorDescription());
			if (CoreUtils.isIVR(device)) {
				tae.setErrorVXML(errorConfig.getErrorVXML());
			}
			tae.setSendAlert(errorConfig.getSendAlert());
			throw tae;
		}
	}

	@Override
	public SeatsCalendarView getSeatsCalendarView(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId,
			String date, String time,String showRemainderIcons) throws Exception {
		SeatsCalendarView seatCalendarView = new SeatsCalendarView();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			String time24Hours = DateUtils.convert12To24HoursHHMMSSFormat(time);
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.SEATS_CALENDAR_VIEW.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			long eventDateTimeId = adminDAO.getEventDateTimeId(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventId, dateYYYYMMDD, time24Hours);
			if (eventDateTimeId == 0) {
				throw new Exception("EventDateTime Id not found!");
			}
			seatCalendarView.setEventDateTimeId("" + eventDateTimeId);
			adminDAO.getSeatsCalendarView(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventId, dateYYYYMMDD, eventDateTimeId, seatCalendarView,showRemainderIcons);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return seatCalendarView;
	}

	@Override
	public DailyCalendarView getDailyCalendarView(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId,
			String date,String showRemainderIcons) throws Exception {
		DailyCalendarView dailyCalendarView = new DailyCalendarView();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.DAILY_CALENDAR_VIEW.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getDailyCalendarView(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventId, dateYYYYMMDD, dailyCalendarView,showRemainderIcons);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return dailyCalendarView;
	}

	@Override
	public EventListResponse getEventListByLocationId(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId,
			boolean isFullDate) throws Exception {
		EventListResponse eventListResponse = new EventListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_ADMIN_EVENT_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getAdminEventListByLocationId(jdbcCustomTemplate, customLogger, langCode, device, locationId, isFullDate, eventListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventListResponse;
	}

	@Override
	public DateListResponse getCalendarDateList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId)
			throws Exception {
		DateListResponse dateListResponse = new DateListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_DATE_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getDateList(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventId, dateListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return dateListResponse;
	}
	
	@Override
	public DateJSListResponse getJSCalendarDateList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId)
			throws Exception {
		DateJSListResponse dateListResponse = new DateJSListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_DATE_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getJSDateList(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventId, dateListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return dateListResponse;
	}

	@Override
	public HomePageResponse getHomePageDetails(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String privilegeName) throws Exception {
		HomePageResponse homePageResponse = new HomePageResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_HOME_PAGE_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getPrivileges(jdbcCustomTemplate, customLogger, langCode, device, privilegeName, homePageResponse);
			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			homePageResponse.setResvSysConfig(resvSysConfig);
			DisplayNames displayNames = new DisplayNames();
			adminDAO.getDisplayNames(jdbcCustomTemplate, customLogger, langCode, device, displayNames);
			homePageResponse.setDisplayNames(displayNames);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return homePageResponse;
	}

	@Override
	public TimeListResponse getSeatViewTimeList(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, String eventId,
			String date) throws Exception {
		TimeListResponse timeListResponse = new TimeListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.SEATS_CALENDAR_TIME_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getSeatsTimeList(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventId, dateYYYYMMDD, timeListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return timeListResponse;
	}

	@Override
	public BaseResponse updateLocation(CustomLogger customLogger, Location location) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, location.getClientCode(), location.getDevice(), true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, location.getClientCode(), location.getDevice(), cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_LOCATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateLocation(jdbcCustomTemplate, customLogger, location.getLangCode(), location.getDevice(), location, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, location.getDevice(), Long.valueOf(location.getTransId()), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, location.getDevice(), Long.valueOf(location.getTransId()), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public EventDateTime getEventDateTimeById(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String eventDateTimeId)
			throws Exception {
		EventDateTime eventDateTime = new EventDateTime();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_ADMIN_EVENT_DATE_TIME_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getEventDateTimeById(jdbcCustomTemplate, customLogger, langCode, device, eventDateTimeId, eventDateTime);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventDateTime;
	}

	@Override
	public CustomerResponse getCustomerNames(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String customerNames) throws Exception {
		CustomerResponse customerResponse = new CustomerResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_CUSTOMER_NAMES.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getCustomerNames(jdbcCustomTemplate, customLogger, langCode, device, customerNames, customerResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return customerResponse;
	}
	
	@Override
	public ReservationReportCheckboxResponse getEventDateTimeForLocEventDateRange(ReservationReportRequest resvReportRequest) throws Exception {
		ReservationReportCheckboxResponse resvReportCheckboxResonse = new ReservationReportCheckboxResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		CustomLogger customLogger = resvReportRequest.getCustomLogger();
		String clientCode = resvReportRequest.getClientCode();
		String device = resvReportRequest.getDevice();
		String transId = resvReportRequest.getTransId();
		try {
			String startDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(resvReportRequest.getStartDate());
			String endDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(resvReportRequest.getEndDate());
			resvReportRequest.setStartDate(startDateYYYYMMDD);
			resvReportRequest.setEndDate(endDateYYYYMMDD);
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.RESERVATION_REPORTS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getEventDateTimeForLocEventDateRange(jdbcCustomTemplate,customLogger, resvReportRequest, resvReportCheckboxResonse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return resvReportCheckboxResonse;
	}


	@Override
	public ReservationReportResponse getReservationReports(ReservationReportRequest resvReportRequest) throws Exception {
		ReservationReportResponse reservationReportResponse = new ReservationReportResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		CustomLogger customLogger = resvReportRequest.getCustomLogger();
		String clientCode = resvReportRequest.getClientCode();
		String device = resvReportRequest.getDevice();
		String transId = resvReportRequest.getTransId();
		try {
			String startDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(resvReportRequest.getStartDate());
			String endDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(resvReportRequest.getEndDate());
			resvReportRequest.setStartDate(startDateYYYYMMDD);
			resvReportRequest.setEndDate(endDateYYYYMMDD);
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.RESERVATION_REPORTS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);

			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "report_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
			DynamicSQL dynamicSQL = null;
			if (resvReportRequest.getEventId() > 0 && resvReportRequest.getLocationId() > 0) {
				dynamicSQL = dynamicSQLMap.get("resvreport_loc_event_selected");
			} else if (resvReportRequest.getLocationId() > 0) {
				dynamicSQL = dynamicSQLMap.get("resvreport_loc_selected");
			} else if (resvReportRequest.getEventId() > 0) {
				dynamicSQL = dynamicSQLMap.get("resvreport_event_selected");
			} else {
				dynamicSQL = dynamicSQLMap.get("resvreport_loc_event_not_selected");
			}
			String resvStatus = (resvReportRequest.getResvStatus() == null || "".equals(resvReportRequest.getResvStatus())) ? "" + ReservataionStatus.CONFIRM.getStatus()
					: resvReportRequest.getResvStatus();
			String reportSQL = dynamicSQL.getSqlQuery();
			reportSQL = reportSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			reportSQL = reportSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			reportSQL = reportSQL.replaceAll("%SCH_STATUS%", resvStatus);
			reportSQL = reportSQL.replaceAll("%EDT_IDS%", resvReportRequest.getEventDateTimeIds());
			final String reportSQLFinal = reportSQL;
			adminDAO.getReservationReports(jdbcCustomTemplate, dynamicSQL, dynamicTableItems, reportSQLFinal, resvReportRequest, reservationReportResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return reservationReportResponse;
	}

	@Override
	public CalendarOverviewDetailsResponse getCalendarOverViewDetails(CustomLogger customLogger, String clientCode, String langCode, String device, String transId,
			String eventDateTimeId) throws Exception {
		CalendarOverviewDetailsResponse calOverviewDetailsResponse = new CalendarOverviewDetailsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.CALENDAR_OVERVIEW_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getCalendarOverviewDetails(jdbcCustomTemplate, customLogger, langCode, device, eventDateTimeId, calOverviewDetailsResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return calOverviewDetailsResponse;
	}

	@Override
	public CustomerResponse getCustomerById(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String customerId)
			throws Exception {
		CustomerResponse customerResponse = new CustomerResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_CUSTOMER_ID.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getCustomerById(jdbcCustomTemplate, customLogger, langCode, device, customerId, customerResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return customerResponse;
	}

	@Override
	public FutureReservationResponse getFutureReservations(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId,
			String customerId) throws Exception {
		FutureReservationResponse futureResvResponse = new FutureReservationResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.FUTURE_RESERVATION_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getFutureReservations(jdbcCustomTemplate, customLogger, langCode, device, customerId, futureResvResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return futureResvResponse;
	}

	@Override
	public PastReservationResponse getPastReservations(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String customerId)
			throws Exception {
		PastReservationResponse pastResvResponse = new PastReservationResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.PAST_RESERVATION_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getPastReservations(jdbcCustomTemplate, customLogger, langCode, device, customerId, pastResvResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return pastResvResponse;
	}

	@Override
	public ReservationDetailsResponse getReservationByScheduleId(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId,
			String scheduleId) throws Exception {
		ReservationDetailsResponse resvDetails = new ReservationDetailsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.RESERVATION_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getReservationByScheduleId(jdbcCustomTemplate, customLogger, langCode, device, scheduleId, cdConfig, resvDetails);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return resvDetails;
	}

	@Override
	public RegistrationInfoResponse getRegistrationInfo(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId)
			throws Exception {
		RegistrationInfoResponse registrationInfoResponse = new RegistrationInfoResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_REGISTRATION_INFO.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			// setTokenValidity(jdbcCustomTemplate, customLogger,
			// loginInfoResponse, device, langCode, token, cdConfig, cache);
			if (registrationInfoResponse.isResponseStatus() == false) {
				return registrationInfoResponse;
			}
			List<RegistrationField> loginFieldList = cacheComponent.getRegistrationParamsList(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if (CoreUtils.isAdmin(device)) {
				registrationInfoResponse.setRegFieldList(loginFieldList);
				Map<String, String> buttonsNames = cacheComponent.getDisplayButtonsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				registrationInfoResponse.setNextBtn(buttonsNames.get(DisplayButtonsConstants.NEXT.getValue()));
				registrationInfoResponse.setBackBtn(buttonsNames.get(DisplayButtonsConstants.BACK.getValue()));
			}
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		} finally {
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		}
		return registrationInfoResponse;
	}

	@Override
	public ReservationSearchResponse getReservationSearch(CustomLogger customLogger, SearchRequestData searchRequestData) throws Exception {
		ReservationSearchResponse reservationSearchResponse = new ReservationSearchResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = searchRequestData.getClientCode();
		String device = searchRequestData.getDevice();
		String transId = searchRequestData.getTransId();
		String langCode = searchRequestData.getLangCode();
		searchRequestData.setCustomLogger(customLogger);
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.RESERVATION_SEARCH.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);

			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "resvsearch_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
			DynamicSQL dynamicSQL = dynamicSQLMap.get("resvsearch_details");

			StringBuilder dynamicWhere = new StringBuilder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			getDynamicWhereClauseForSearch(jdbcCustomTemplate, customLogger, langCode, device, "resv_search_fields", dynamicWhere, mapSqlParameterSource, searchRequestData);

			String searchSQL = dynamicSQL.getSqlQuery();
			searchSQL = searchSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			searchSQL = searchSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			searchSQL = searchSQL.replaceAll("%DYNAMIC_WHERE%", dynamicWhere.toString());
			final String searchSQLFinal = searchSQL;

			adminDAO.getReservationSearch(jdbcCustomTemplate, dynamicTableItems, searchSQLFinal, mapSqlParameterSource, searchRequestData, reservationSearchResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return reservationSearchResponse;
	}

	@Override
	public TablePrintViewResponse getTablePrintView(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String eventId,
			String locationId, String date) throws Exception {
		TablePrintViewResponse tablePrintView = new TablePrintViewResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.TABLE_PRINT_VIEW.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);

			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "table_printview_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
			DynamicSQL dynamicSQL = dynamicSQLMap.get("table_printview_details");
			String tablePrintSQL = dynamicSQL.getSqlQuery();
			tablePrintSQL = tablePrintSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			tablePrintSQL = tablePrintSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			final String finalSQL = tablePrintSQL;
			adminDAO.getTablePrintView(jdbcCustomTemplate, customLogger, langCode, device, locationId, eventId, dateYYYYMMDD, dynamicTableItems, finalSQL, tablePrintView);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return tablePrintView;
	}

	@Override
	public SearchFieldsResponse getDynamicSearchFields(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId,
			String searchCategory) throws Exception {
		SearchFieldsResponse searchFieldsResponse = new SearchFieldsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.DYNAMIC_SEARCH_FIELDS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getDynamicSearchFields(jdbcCustomTemplate, customLogger, langCode, device, searchCategory, false, searchFieldsResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return searchFieldsResponse;
	}

	@Override
	public ClientDetailsResponse getClientDetails(CustomLogger customLogger, SearchRequestData searchRequestData) throws Exception {
		ClientDetailsResponse clientDetailsResponse = new ClientDetailsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = searchRequestData.getClientCode();
		String device = searchRequestData.getDevice();
		String langCode = searchRequestData.getLangCode();
		String transId = searchRequestData.getTransId();
		searchRequestData.setCustomLogger(customLogger);
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.CLIENT_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "customer_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
			DynamicSQL dynamicSQL = dynamicSQLMap.get("customer_details");
			StringBuilder dynamicWhere = new StringBuilder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			getDynamicWhereClauseForSearch(jdbcCustomTemplate, customLogger, langCode, device, "client_search_fields", dynamicWhere, mapSqlParameterSource, searchRequestData);
			String searchSQL = dynamicSQL.getSqlQuery();
			searchSQL = searchSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			searchSQL = searchSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			searchSQL = searchSQL.replaceAll("%DYNAMIC_WHERE%", dynamicWhere.toString());
			final String searchSQLFinal = searchSQL;
			adminDAO.getClientDetails(jdbcCustomTemplate, customLogger, dynamicTableItems, searchSQLFinal, mapSqlParameterSource, searchRequestData, clientDetailsResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return clientDetailsResponse;
	}

	@Override
	public ClientDetailsResponse getBlockedClientDetails(CustomLogger customLogger, SearchRequestData searchRequestData) throws Exception {
		ClientDetailsResponse clientDetailsResponse = new ClientDetailsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = searchRequestData.getClientCode();
		String device = searchRequestData.getDevice();
		String langCode = searchRequestData.getLangCode();
		String transId = searchRequestData.getTransId();
		searchRequestData.setCustomLogger(customLogger);
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.BLOCKED_CLIENT_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "blocked_customer_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
			DynamicSQL dynamicSQL = dynamicSQLMap.get("blocked_customer_details");
			StringBuilder dynamicWhere = new StringBuilder();
			MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
			getDynamicWhereClauseForSearch(jdbcCustomTemplate, customLogger, langCode, device, "client_search_fields", dynamicWhere, mapSqlParameterSource, searchRequestData);
			String searchSQL = dynamicSQL.getSqlQuery();
			searchSQL = searchSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			searchSQL = searchSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			searchSQL = searchSQL.replaceAll("%DYNAMIC_WHERE%", dynamicWhere.toString());
			final String searchSQLFinal = searchSQL;
			adminDAO.getClientDetails(jdbcCustomTemplate, customLogger, dynamicTableItems, searchSQLFinal, mapSqlParameterSource, searchRequestData, clientDetailsResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return clientDetailsResponse;
	}

	@Override
	public GraphResponse getGraphDetails(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String locationId, String date)
			throws Exception {
		GraphResponse graphResponse = new GraphResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GRAPH_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			adminDAO.getGraphDetails(jdbcCustomTemplate, customLogger, langCode, device, locationId, dateYYYYMMDD, graphResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return graphResponse;
	}

	@Override
	public AuthResponse updateCustomer(CustomLogger customLogger, Customer passedCustomer) throws Exception {
		AuthResponse authResponse = new AuthResponse();
		authResponse.setAuthSuccess(true);
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String device = passedCustomer.getDevice();
		String langCode = passedCustomer.getLangCode();
		try {
			Client client = cacheComponent.getClient(customLogger, passedCustomer.getClientCode(), device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, passedCustomer.getClientCode(), passedCustomer.getDevice(), cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_CUSTOMER.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			long customerId = passedCustomer.getCustomerId();

			List<LoginField> loginFieldList = cacheComponent.getLoginParamsList(jdbcCustomTemplate, customLogger, device, langCode, cache);
			Map<String, String> map = getParamMap(loginFieldList, passedCustomer);
			Map<String, String> requiredMap = getRequiredMap(map, loginFieldList, "authenticate");
			Map<String, LoginField> loginFieldMap = getLoginFieldsByFilter(loginFieldList, "authenticate");
			boolean isValid = adminDAO.validateCustomer(jdbcCustomTemplate, customLogger, device, requiredMap, loginFieldMap);
			if (isValid) {
				Customer dbCustomer = adminDAO.getCustomerIncludingDeletedCustomer(jdbcCustomTemplate, customLogger, customerId);
				setUpdateFields(dbCustomer, loginFieldList, passedCustomer);
				if(passedCustomer.getDob() != null && !"".equals(passedCustomer.getDob())) {
					dbCustomer.setDob(DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(passedCustomer.getDob()));
				}
				adminDAO.updateCustomer(jdbcCustomTemplate, customLogger, dbCustomer, cdConfig);
				authResponse.setResponseStatus(true);
				authResponse.setCustomerId(customerId);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(passedCustomer.getTransId()), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(passedCustomer.getTransId()), errorConfig, cdConfig, e);
		}
		return authResponse;
	}

	private void getDynamicWhereClauseForSearch(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String searchCategory,
			StringBuilder dynamicWhere, MapSqlParameterSource mapSQLParamSource, SearchRequestData searchRequestData) throws Exception {
		SearchFieldsResponse searchFields = new SearchFieldsResponse();
		adminDAO.getDynamicSearchFields(jdbcCustomTemplate, customLogger, langCode, device, searchCategory, true, searchFields);
		String javaRef[] = searchFields.getJavaRef().split(",");
		String searchTypes[] = searchFields.getSearchType().split(",");
		String selectAlias[] = searchFields.getAlias().split(",");
		String tableColumn[] = searchFields.getTableColumn().split(",");

		String fieldName = null;
		String alias = null;
		String searchType = null;
		String columnName = null;
		dynamicWhere.append("1=1");
		for (int i = 0; i < javaRef.length; i++) {
			fieldName = javaRef[i];
			alias = selectAlias[i];
			searchType = searchTypes[i];
			columnName = tableColumn[i];
			String value = (String) CoreUtils.getPropertyValue(searchRequestData, fieldName);
			if (value != null && value.length() > 0) {
				dynamicWhere.append(" and ");
				dynamicWhere.append(alias).append(".").append(columnName);
				if ("P".equals(searchType)) {
					dynamicWhere.append(" LIKE ");
					dynamicWhere.append("(").append(":").append(fieldName).append(")");
					mapSQLParamSource.addValue(fieldName, "%" + value + "%");
				} else {
					dynamicWhere.append(" = ");
					dynamicWhere.append("(").append(":").append(fieldName).append(")");
					mapSQLParamSource.addValue(fieldName, value);
				}
			}
		}
	}

	@Override
	public PrivilageResponse getAccessesPrivilages(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception {
		PrivilageResponse prePrivilageResponse = new PrivilageResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.PRIVILAGE_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getAccessesPrivilages(jdbcCustomTemplate, customLogger, langCode, device, prePrivilageResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return prePrivilageResponse;
	}

	@Override
	public LocationListResponse getLocationById(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String locationId, boolean isFullData)
			throws Exception {
		LocationListResponse locationListResponse = new LocationListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_LOCATION_BY_ID.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getLocationById(jdbcCustomTemplate, customLogger, langCode, device, locationId, isFullData, locationListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return locationListResponse;
	}

	private Map<String, String> getParamMap(List<LoginField> paramList, Customer passedCustomer) throws Exception {
		Map<String, String> loginParamsMap = new HashMap<String, String>();
		LoginField loginField = null;
		int paramListSize = paramList.size();
		for (int i = 0; i < paramListSize; i++) {
			loginField = paramList.get(i);
			String fieldName = loginField.getFieldName();
			String fieldValue = (String) CoreUtils.getPropertyValue(passedCustomer, fieldName);
			loginParamsMap.put(fieldName, fieldValue);
		}
		return loginParamsMap;
	}

	private Map<String, String> getRequiredMap(Map<String, String> paramsMap, List<LoginField> loginParamList, String loginType) {
		Map<String, String> authMap = new HashMap<String, String>();
		for (LoginField loginField : loginParamList) {
			if (loginType.equals(loginField.getLoginType())) {
				authMap.put(loginField.getFieldName(), paramsMap.get(loginField.getFieldName()) == null ? "" : paramsMap.get(loginField.getFieldName()));
			}
		}
		return authMap;
	}

	private Map<String, LoginField> getLoginFieldsByFilter(List<LoginField> loginParamList, String loginType) {
		Map<String, LoginField> loginFieldMap = new HashMap<String, LoginField>();
		for (LoginField loginField : loginParamList) {
			if (loginType.equals(loginField.getLoginType())) {
				loginFieldMap.put(loginField.getFieldName(), loginField);
			}
		}
		return loginFieldMap;
	}

	private void setUpdateFields(Customer dbCustomer, List<LoginField> loginFields, Customer passedCustomer) throws Exception {
		for (LoginField loginField : loginFields) {
			String fieldValue = (String) CoreUtils.getPropertyValue(passedCustomer, loginField.getJavaRef());
			if ("accountNumber".equalsIgnoreCase(loginField.getJavaRef())) {
				int storageSize = loginField.getStorageSize();
				String storageType = loginField.getStorageType();
				int paramValueLength = fieldValue.length();
				if (storageSize > 0 && paramValueLength >= storageSize) {
					if ("last".equalsIgnoreCase(storageType)) {
						fieldValue = fieldValue.substring(paramValueLength - storageSize);
					} else if ("first".equalsIgnoreCase(storageType)) {
						fieldValue = fieldValue.substring(0, storageSize);
					} else if ("prefix0".equalsIgnoreCase(storageType)) {
						fieldValue = "0" + fieldValue;
					} else if ("postfix0".equalsIgnoreCase(storageType)) {
						fieldValue = fieldValue + "0";
					}
				}
			}
			CoreUtils.setPropertyValue(dbCustomer, loginField.getJavaRef(), fieldValue);
		}
	}

	@Override
	public BaseResponse getEventById(String clientCode, CustomLogger customLogger, String langCode, String device, String transId, String eventId) throws Exception {
		EventListResponse eventListResponse = new EventListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_EVENT_BY_ID.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getEventById(jdbcCustomTemplate, customLogger, langCode, device, eventId, true, eventListResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventListResponse;
	}

	@Override
	public BaseResponse addReservationReportConfig(CustomLogger customLogger, ReservationReportConfig resvReportConfig) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = resvReportConfig.getClientCode();
		String device = resvReportConfig.getDevice();
		String langCode = resvReportConfig.getLangCode();
		String transId = resvReportConfig.getTransId();

		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.ADD_RESV_REPORT_CONFIG.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			boolean saved = masterDAO.addReservationReportConfig(customLogger, langCode, device, client.getClientId(), resvReportConfig);
			if (saved) {
				baseResponse.setResponseStatus(true);
			} else {
				baseResponse.setResponseStatus(false);
				baseResponse.setResponseMessage("Failed in addReservationReportConfig.");
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public ReservationReportConfigResponse getReservationReportConfig(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId)
			throws Exception {
		ReservationReportConfigResponse reservationReportConfigResponse = new ReservationReportConfigResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_RESV_REPORT_CONFIG.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			masterDAO.getReservationReportConfig(customLogger, langCode, device, client.getClientId(), reservationReportConfigResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return reservationReportConfigResponse;
	}

	@Override
	public BaseResponse deleteResvReportConfigById(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String reportConfigId)
			throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.DELETE_REPORT_CONFIG_BY_ID.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			boolean deleted = masterDAO.deleteResvReportConfigById(customLogger, langCode, device, reportConfigId);
			if (deleted) {
				baseResponse.setResponseStatus(true);
			} else {
				baseResponse.setResponseStatus(false);
				baseResponse.setResponseMessage("Failed in deleteResvReportConfigById.");
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse getCampaigns(String clientCode, CustomLogger customLogger, String langCode, String device, String transId) throws Exception {
		CampaignResponse campaignResponse = new CampaignResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_CAMPAIGNS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getCampaigns(jdbcCustomTemplate, customLogger, langCode, device, false, campaignResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return campaignResponse;
	}

	@Override
	public BaseResponse getReservationReminders(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String locationId,
			String campaignId, String date) throws Exception {
		ReservationRemindersResponse resvReminder = new ReservationRemindersResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.RESERVATION_REMINDERS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);

			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "resv_reminder_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);

			DynamicSQL dynamicSQL = null;
			MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue("date", "%" + dateYYYYMMDD + "%");
			if ("-1".equals(campaignId) && "-1".equals(locationId)) {
				dynamicSQL = dynamicSQLMap.get("remind_details_both_not_sel");
			} else if ("-1".equals(campaignId)) {
				dynamicSQL = dynamicSQLMap.get("remind_details_loc_sel");
				parameterSource.addValue("locationId", Integer.valueOf(locationId));
			} else if ("-1".equals(locationId)) {
				dynamicSQL = dynamicSQLMap.get("remind_details_campaign_sel");
				parameterSource.addValue("campaignId", Long.valueOf(campaignId));
			} else {
				dynamicSQL = dynamicSQLMap.get("remind_details_sel");
				parameterSource.addValue("campaignId", Long.valueOf(campaignId));
				parameterSource.addValue("locationId", Integer.valueOf(locationId));
			}

			String tablePrintSQL = dynamicSQL.getSqlQuery();
			tablePrintSQL = tablePrintSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			tablePrintSQL = tablePrintSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			final String finalSQL = tablePrintSQL;
			adminDAO.getReservationReminders(jdbcCustomTemplate, customLogger, langCode, device, locationId, campaignId, dateYYYYMMDD, dynamicTableItems, finalSQL,
					parameterSource, resvReminder);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return resvReminder;

	}

	@Override
	public BaseResponse getReservationStatusList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception {
		ReservationStatusResponse resvStatusResponse = new ReservationStatusResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_RESERVATION_STATUS_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getReservationStatusList(jdbcCustomTemplate, customLogger, langCode, device, false, resvStatusResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return resvStatusResponse;
	}

	@Override
	public BaseResponse getNotifyRemainderStatusList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception {
		ReminderStatusResponse reminderStatusResponse = new ReminderStatusResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_REMINDER_STATUS_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getNotifyRemainderStatusList(jdbcCustomTemplate, customLogger, langCode, device, false, reminderStatusResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return reminderStatusResponse;
	}

	@Override
	public BaseResponse updateNotifyStatus(CustomLogger customLogger, NotifyRequest notifyRequest) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, notifyRequest.getClientCode(), notifyRequest.getDevice(), true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, notifyRequest.getClientCode(), notifyRequest.getDevice(), cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_REMINDER_STATUS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateNotifyStatus(jdbcCustomTemplate, customLogger, notifyRequest, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, notifyRequest.getDevice(), Long.valueOf(notifyRequest.getTransId()), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, notifyRequest.getDevice(), Long.valueOf(notifyRequest.getTransId()), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse changeSchedulerStatus(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String status)
			throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.CHANGE_SCHEDULER_STATUS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			boolean updated = adminDAO.changeSchedulerStatus(jdbcCustomTemplate, customLogger, langCode, device, status);
			if (updated) {
				baseResponse.setResponseStatus(true);
			} else {
				baseResponse.setResponseStatus(false);
				baseResponse.setResponseMessage("Failed in changeSchedulerStatus.");
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse getInBoundCallReportList(CustomLogger customLogger, InBoundCallReportRequest inBCReportRequest) throws Exception {
		InBoundCallReportResponse inBoundCallReportResponse = new InBoundCallReportResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = inBCReportRequest.getClientCode();
		String device = inBCReportRequest.getDevice();
		String transId = inBCReportRequest.getTransId();

		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_INBOUND_CALL_REPORT_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);

			String startDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(inBCReportRequest.getFromDate());
			String endDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(inBCReportRequest.getToDate());
			inBCReportRequest.setFromDate(startDateYYYYMMDD);
			inBCReportRequest.setToDate(endDateYYYYMMDD);

			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "inboundcalls_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
			DynamicSQL dynamicSQL = null;
			MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue("startTime", startDateYYYYMMDD);
			parameterSource.addValue("endTime", endDateYYYYMMDD);
			if (inBCReportRequest.getCallerId() == null || "".equals(inBCReportRequest.getCallerId())) {
				dynamicSQL = dynamicSQLMap.get("inbound_calls_non_callerId");
			} else {
				dynamicSQL = dynamicSQLMap.get("inbound_calls_callerId");
				parameterSource.addValue("callerId", inBCReportRequest.getCallerId());
			}

			String reportSQL = dynamicSQL.getSqlQuery();
			reportSQL = reportSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			reportSQL = reportSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			final String reportSQLFinal = reportSQL;

			adminDAO.getInBoundCallReportList(jdbcCustomTemplate, dynamicTableItems, reportSQLFinal, parameterSource, inBCReportRequest, inBoundCallReportResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return inBoundCallReportResponse;
	}

	@Override
	public BaseResponse getOutBoundCallReportList(CustomLogger customLogger, OutBoundCallReportRequest outBCReportRequest) throws Exception {
		OutBoundCallReportResponse outBoundCallReportResponse = new OutBoundCallReportResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = outBCReportRequest.getClientCode();
		String device = outBCReportRequest.getDevice();
		String transId = outBCReportRequest.getTransId();

		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_OUTBOUND_CALL_REPORT_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);

			String startDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(outBCReportRequest.getFromDate());
			String endDateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(outBCReportRequest.getToDate());
			outBCReportRequest.setFromDate(startDateYYYYMMDD);
			outBCReportRequest.setToDate(endDateYYYYMMDD);

			final DynamicTableItems dynamicTableItems = new DynamicTableItems();
			adminDAO.populateDynamicTableItems(jdbcCustomTemplate, customLogger, "outboundcalls_results", dynamicTableItems);
			final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
			adminDAO.populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
			DynamicSQL dynamicSQL = null;
			MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue("startDate", startDateYYYYMMDD);
			parameterSource.addValue("endDate", endDateYYYYMMDD);
			if (outBCReportRequest.getCallerId() == null || "".equals(outBCReportRequest.getCallerId())) {
				dynamicSQL = dynamicSQLMap.get("outbound_calls_non_callerId");
			} else {
				dynamicSQL = dynamicSQLMap.get("outbound_calls_callerId");
				parameterSource.addValue("callerId", outBCReportRequest.getCallerId());
			}

			String reportSQL = dynamicSQL.getSqlQuery();
			reportSQL = reportSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
			reportSQL = reportSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
			final String reportSQLFinal = reportSQL;

			adminDAO.getOutBoundCallReportList(jdbcCustomTemplate, dynamicTableItems, reportSQLFinal, parameterSource, outBCReportRequest, outBoundCallReportResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return outBoundCallReportResponse;
	}
		
	@Override
	public BaseResponse getTransStates(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception {
		TransStateResponse transStateResponse = new TransStateResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.GET_TRANS_STATS_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.getTransStates(jdbcCustomTemplate, customLogger, langCode, device, transId, transStateResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return transStateResponse;
	}
	
	@Override
	public BaseResponse updateScreenedStatus(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId,String scheduleId, String screened) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger,clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger,clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_SCREENED_STAUS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			adminDAO.updateScreenedStatus(jdbcCustomTemplate, customLogger, scheduleId, screened, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger,device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger,device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}
	
	@Override
	public BaseResponse updateSeatReservedStatus(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String seatId,String reserved) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger,clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger,clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, AdminFlowStateConstants.UPDATE_SEAT_RESERVED_STAUS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			Long scheduleId = adminDAO.getSeatScheduleId(jdbcCustomTemplate, customLogger, seatId);
			if(scheduleId!=null && scheduleId>0){
				baseResponse.setResponseStatus(true);
				baseResponse.setErrorStatus(true);
				baseResponse.setErrorMessage("This seat is already booked!");
			}else{
				adminDAO.updateSeatReservedStatus(jdbcCustomTemplate, customLogger,seatId,reserved, baseResponse);
				updateTransactionState(jdbcCustomTemplate, customLogger,device, Long.valueOf(transId), errorConfig, cdConfig);
				baseResponse.setResponseStatus(true);
				baseResponse.setErrorStatus(false);
			}			
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger,device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}
	
}

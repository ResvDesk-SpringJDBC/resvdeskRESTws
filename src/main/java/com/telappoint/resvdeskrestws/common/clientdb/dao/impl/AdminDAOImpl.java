package com.telappoint.resvdeskrestws.common.clientdb.dao.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.collect.ImmutableMap;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.constants.AdminFlowStateConstants;
import com.telappoint.resvdeskrestws.admin.constants.SPConstants;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverview;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverviewDetails;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverviewDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.CalendarOverviewResponse;
import com.telappoint.resvdeskrestws.admin.model.CalendarView;
import com.telappoint.resvdeskrestws.admin.model.ClientDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.CustomerResponse;
import com.telappoint.resvdeskrestws.admin.model.DailyCalendarView;
import com.telappoint.resvdeskrestws.admin.model.DailyReminderView;
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
import com.telappoint.resvdeskrestws.admin.model.InBoundIvrCalls;
import com.telappoint.resvdeskrestws.admin.model.Location;
import com.telappoint.resvdeskrestws.admin.model.LocationListResponse;
import com.telappoint.resvdeskrestws.admin.model.NotifyDailyView;
import com.telappoint.resvdeskrestws.admin.model.NotifyRequest;
import com.telappoint.resvdeskrestws.admin.model.OutBoundCallReportRequest;
import com.telappoint.resvdeskrestws.admin.model.OutBoundCallReportResponse;
import com.telappoint.resvdeskrestws.admin.model.OutBoundIvrCalls;
import com.telappoint.resvdeskrestws.admin.model.PastReservationResponse;
import com.telappoint.resvdeskrestws.admin.model.PrivilageData;
import com.telappoint.resvdeskrestws.admin.model.PrivilageResponse;
import com.telappoint.resvdeskrestws.admin.model.RegistrationField;
import com.telappoint.resvdeskrestws.admin.model.ReminderStatusResponse;
import com.telappoint.resvdeskrestws.admin.model.ReportDateTimeCheckBox;
import com.telappoint.resvdeskrestws.admin.model.ReservationDetails;
import com.telappoint.resvdeskrestws.admin.model.ReservationDetailsResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationRemindersResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportCheckboxResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportRequest;
import com.telappoint.resvdeskrestws.admin.model.ReservationReportResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationSearchResponse;
import com.telappoint.resvdeskrestws.admin.model.ReservationStatusResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchFieldsResponse;
import com.telappoint.resvdeskrestws.admin.model.SearchRequestData;
import com.telappoint.resvdeskrestws.admin.model.SeatView;
import com.telappoint.resvdeskrestws.admin.model.SeatsCalendarView;
import com.telappoint.resvdeskrestws.admin.model.TablePrintView;
import com.telappoint.resvdeskrestws.admin.model.TablePrintViewResponse;
import com.telappoint.resvdeskrestws.admin.model.TimeListResponse;
import com.telappoint.resvdeskrestws.admin.model.TransState;
import com.telappoint.resvdeskrestws.admin.model.TransStateResponse;
import com.telappoint.resvdeskrestws.common.clientdb.dao.AdminDAO;
import com.telappoint.resvdeskrestws.common.constants.CommonDateContants;
import com.telappoint.resvdeskrestws.common.constants.ReservataionStatus;
import com.telappoint.resvdeskrestws.common.constants.ReservationtType;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.Seat;
import com.telappoint.resvdeskrestws.common.storedproc.CalendarOverviewStoredProcedure;
import com.telappoint.resvdeskrestws.common.utils.CoreUtils;
import com.telappoint.resvdeskrestws.common.utils.DateUtils;
import com.telappoint.resvdeskrestws.common.utils.StringUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;
import com.telappoint.resvdeskrestws.notification.constants.IVRNotifyStatusConstants;
import com.telappoint.resvdeskrestws.notification.constants.NotifyStatusConstants;

/**
 * 
 * @author Balaji N
 *
 */
@Repository
public class AdminDAOImpl extends AbstractDAOImpl implements AdminDAO {

	@Override
	public void getAdminLocationList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData,
			final LocationListResponse locationListResponse) {
		StringBuilder sql = new StringBuilder("select id,location_name, address,city,state,zip,work_phone,time_zone, enable");
		sql.append(" from location order by placement");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<LocationListResponse>() {
				@Override
				public LocationListResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Location> locationList = new ArrayList<Location>();
					Location location = null;
					while (rs.next()) {
						location = new Location();
						location.setLocationId(rs.getInt("id"));
						location.setLocationName(rs.getString("location_name"));
						location.setTimeZone(rs.getString("time_zone"));
						if (isFullData) {
							location.setAddress(rs.getString("address"));
							location.setCity(rs.getString("city"));
							location.setState(rs.getString("state"));
							location.setZip(rs.getString("zip"));
							location.setEnable(rs.getString("enable"));
						} else {
							location.setEnable(null);

						}
						locationList.add(location);
					}
					locationListResponse.setLocationList(locationList);
					return locationListResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_ADMIN_LOCATION_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public void deleteLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, BaseResponse baseResponse)
			throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("update location set delete_flag = ? where id = ?");
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { "Y", Long.valueOf(locationId) });
			sql.setLength(0);
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.DELETE_LOCATION.getValue(), e);
		}
	}

	@Override
	public void updateLocationStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String enable,
			BaseResponse baseResponse) throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("update location set enable = ? where id = ?");
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { enable, Long.valueOf(locationId) });
			sql.setLength(0);
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			customLogger.error("Error in updateLocationStatus:", e);
			throw new TelAppointDBException(AdminFlowStateConstants.OPEN_CLOSE_LOCATION.getValue(), e);
		}
	}

	@Override
	public void addLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final Location location, BaseResponse baseResponse)
			throws Exception {

		final String sql = "insert into location (location_name,location_name_ivr_tts,location_name_ivr_audio,location_name_remind_sms,address,city,state,zip, time_zone, work_phone,placement) values (?,?,?,?,?,?,?,?,?,?,?)";
		final String locationName = location.getLocationName();
		final String locationNameIvrTts = CoreUtils.removeDigitsAndNonAlpha(locationName).toLowerCase();
		final String locationNameIvrAudio = CoreUtils.removeDigitsAndNonAlpha(locationName).toLowerCase();
		final String locationNameRemindSMS = CoreUtils.removeDigitsAndNonAlpha(locationName).toLowerCase();
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final int placement = getMaxPlacementVal(jdbcCustomTemplate, customLogger, "location");

			KeyHolder holder = new GeneratedKeyHolder();
			jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
				int i = 1;

				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(i++, locationName);
					ps.setString(i++, locationNameIvrTts);
					ps.setString(i++, locationNameIvrAudio);
					ps.setString(i++, locationNameRemindSMS);
					ps.setString(i++, location.getAddress());
					ps.setString(i++, location.getCity());
					ps.setString(i++, location.getState());
					ps.setString(i++, location.getZip());
					ps.setString(i++, location.getTimeZone());
					ps.setString(i++, location.getWorkPhone());
					ps.setInt(i++, placement);
					return ps;
				}
			}, holder);
			final Long id = holder.getKey().longValue();
			addProcedureLocation(jdbcCustomTemplate, customLogger, langCode, device, id);
			dsTransactionManager.commit(status);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			customLogger.error("Error in updateLocationStatus:", e);
			throw new TelAppointDBException(AdminFlowStateConstants.OPEN_CLOSE_LOCATION.getValue(), e);
		}
	}

	private boolean addProcedureLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Long locationId) throws Exception {
		String sql = "insert into procedure_location (procedure_id, location_id, enable) values (?,?,?)";
		Object[] columns = new Object[] { 0, locationId, "Y" };
		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql, columns);
		if (count > 0) {
			return true;
		} else {
			throw new TelAppointDBException();
		}
	}

	@Override
	public void updateLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Location location, BaseResponse baseResponse)
			throws Exception {
		String sql = "update location set location_name=?, location_name_ivr_tts=?, location_name_ivr_audio=?, location_name_remind_sms=?, address=?, city=?, state=?,zip=?, work_phone=? where id= ?";
		String locationName = location.getLocationName();
		String locationNameIvrTts = CoreUtils.removeDigitsAndNonAlpha(locationName).toLowerCase();
		String locationNameIvrAudio = CoreUtils.removeDigitsAndNonAlpha(locationName).toLowerCase();
		String locationNameRemindSMS = CoreUtils.removeDigitsAndNonAlpha(locationName).toLowerCase();
		Object[] columns = new Object[] { locationName, locationNameIvrTts, locationNameIvrAudio, locationNameRemindSMS, location.getAddress(), location.getCity(),
				location.getState(), location.getZip(), location.getWorkPhone(), location.getLocationId() };
		jdbcCustomTemplate.getJdbcTemplate().update(sql, columns);
		baseResponse.setResponseStatus(true);
	}

	@Override
	public void getAdminEventList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData,
			final EventListResponse eventListResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select group_concat(distinct e.event_id) as eventId,group_concat(distinct e.event_name) as eventName");
		sql.append(",group_concat(distinct e.duration) as duration,group_concat(distinct l.location_name) as locationNames,group_concat(distinct lde.location_id) as locationIds");
		sql.append(", e.enable");
		sql.append(" from event e, location_department_event lde,location l");
		sql.append(" where e.event_id=lde.event_id and lde.location_id=l.id group by lde.event_id order by l.location_name, e.event_name");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<EventListResponse>() {
				@Override
				public EventListResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Event> eventList = new ArrayList<Event>();
					Event event = null;
					while (rs.next()) {
						event = new Event();
						event.setEventId(Integer.valueOf(rs.getString("eventId")));
						event.setEventName(rs.getString("eventName"));
						event.setLocationNames(rs.getString("locationNames"));
						event.setLocationIds(rs.getString("locationIds"));
						event.setDuration(Integer.valueOf(rs.getString("duration")));
						if (isFullData) {
							event.setEnable(rs.getString("enable"));
						} else {
							event.setEnable(null);
						}
						eventList.add(event);
					}
					eventListResponse.setEventList(eventList);
					return eventListResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_ADMIN_EVENT_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public void getEventById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventId, final boolean isFullData,
			final EventListResponse eventListResponse) throws Exception {

		StringBuilder sql = new StringBuilder("select e.event_id,e.event_name,e.duration, e.enable,group_concat(lde.location_id) as locationIds");
		sql.append(" from event e,location_department_event lde");
		sql.append(" where e.event_id = lde.event_id and e.event_id=?");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(eventId) }, new ResultSetExtractor<EventListResponse>() {
				@Override
				public EventListResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Event> eventList = new ArrayList<Event>();
					Event event = null;
					while (rs.next()) {
						event = new Event();
						event.setEventId(rs.getLong("event_id"));
						event.setEventName(rs.getString("event_name"));
						event.setDuration(rs.getInt("duration"));
						event.setLocationIds(rs.getString("locationIds"));
						if (isFullData) {
							event.setEnable(rs.getString("enable"));
						} else {
							event.setEnable(null);
						}
						eventList.add(event);
					}
					eventListResponse.setEventList(eventList);
					return eventListResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_EVENT_BY_ID.getValue(), e);
		}
		return;
	}

	@Override
	public void getAdminEventListByLocationId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId,
			final boolean isFullData, final EventListResponse eventListResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct e.event_id,e.event_name");
		sql.append(" from event e, location_department_event lde where lde.event_id = e.event_id and lde.enable='Y' and location_id = ? order by e.placement asc");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Integer.parseInt(locationId) }, new ResultSetExtractor<EventListResponse>() {
				@Override
				public EventListResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Event> eventList = new ArrayList<Event>();
					Event event = null;
					while (rs.next()) {
						event = new Event();
						event.setEventId(rs.getLong("event_id"));
						event.setEventName(rs.getString("event_name"));
						if (isFullData) {
							event.setEnable(rs.getString("enable"));
						} else {
							event.setEnable(null);
						}
						eventList.add(event);
					}
					eventListResponse.setEventList(eventList);
					return eventListResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_ADMIN_EVENT_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public void deleteEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventId, BaseResponse baseResponse)
			throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("update event set delete_flag = ? where event_id = ?");
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { "Y", Long.valueOf(eventId) });
			sql.setLength(0);
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.DELETE_EVENT.getValue(), e);
		}

	}

	@Override
	public void updateEventStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventId, String enable,
			BaseResponse baseResponse) throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("update event set enable = ? where event_id = ?");
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { enable, Long.valueOf(eventId) });
			sql.setLength(0);
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.OPEN_CLOSE_EVENT.getValue(), e);
		}
	}

	@Override
	public void addEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final Event event, BaseResponse baseResponse)
			throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final int placement = getMaxPlacementVal(jdbcCustomTemplate, customLogger, "event");
			final String sql = "insert into event (event_name,event_name_ivr_tts,event_name_ivr_audio,duration,placement) values (?,?,?,?,?)";
			final String eventName = event.getEventName();
			final String eventNameIvrTts = CoreUtils.removeDigitsAndNonAlpha(eventName).toLowerCase();
			final String eventNameIvrAudio = CoreUtils.removeDigitsAndNonAlpha(eventName).toLowerCase();

			KeyHolder holder = new GeneratedKeyHolder();
			jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
				int i = 1;

				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(i++, eventName);
					ps.setString(i++, eventNameIvrTts);
					ps.setString(i++, eventNameIvrAudio);
					ps.setInt(i++, event.getDuration());
					ps.setInt(i++, placement);
					return ps;
				}
			}, holder);

			final Long id = holder.getKey().longValue();
			addLocationDepartmentEvent(jdbcCustomTemplate, customLogger, langCode, device, event.getLocationIds(), id);
			dsTransactionManager.commit(status);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.ADD_EVENT.getValue(), e);
		}
	}

	private boolean addLocationDepartmentEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationIds, Long eventId)
			throws Exception {
		String sql = "insert into location_department_event (location_id,department_id,event_id, enable) values (:locationId,:departmentId,:eventId,:enable)";
		List<SqlParameterSource> list = new ArrayList<SqlParameterSource>();
		MapSqlParameterSource mapSQLParameterSource = null;
		String locationIdArray[] = locationIds.split(",");
		for (String locationId : locationIdArray) {
			mapSQLParameterSource = new MapSqlParameterSource();
			mapSQLParameterSource.addValue("locationId", Integer.valueOf(locationId));
			mapSQLParameterSource.addValue("departmentId", 0);
			mapSQLParameterSource.addValue("eventId", eventId);
			mapSQLParameterSource.addValue("enable", "Y");
			list.add(mapSQLParameterSource);
		}
		SqlParameterSource mapArray[] = new SqlParameterSource[list.size()];
		SqlParameterSource batchArray[] = list.toArray(mapArray);
		jdbcCustomTemplate.getNameParameterJdbcTemplate().batchUpdate(sql.toString(), batchArray);
		return true;
	}

	@Override
	public void updateEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Event event, BaseResponse baseResponse)
			throws Exception {
		String sql = "update event set event_name = ?,event_name_ivr_tts=?, event_name_ivr_audio=?,duration=? where event_id=?";
		final String eventName = event.getEventName();
		final String eventNameIvrTts = CoreUtils.removeDigitsAndNonAlpha(eventName).toLowerCase();
		final String eventNameIvrAudio = CoreUtils.removeDigitsAndNonAlpha(eventName).toLowerCase();

		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			Object[] columns = new Object[] { eventName, eventNameIvrTts, eventNameIvrAudio, event.getDuration(), event.getEventId() };
			jdbcCustomTemplate.getJdbcTemplate().update(sql, columns);
			if(event.getLocationIds().trim().length() > 0) {
				addLocationDepartmentEvent(jdbcCustomTemplate, customLogger, langCode, device, event.getLocationIds(), event.getEventId());
			}
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.UPDATE_EVENT.getValue(), e);
		}
	}

	@Override
	public void getAdminEventDateTimeList(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, final String device,
			final EventDateTimeResponse eventDateTimeResponse) throws Exception {

		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue());
		StringBuilder sql = new StringBuilder("SELECT edt.id, edt.event_id,edt.location_id,e.event_name,l.location_name,");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time,");
		sql.append("edt.enable,edt.num_seats,IFNULL(count(n.id),0) noOfNotifiedReservations");
		sql.append(" FROM  event e, location l,event_date_time edt LEFT JOIN notify n ON edt.id=n.event_date_time_id and n.notify_status > ");
		sql.append(NotifyStatusConstants.NOTIFY_STATUS_PENDING.getNotifyStatus());
		sql.append(" where edt.location_id=l.id and edt.event_id=e.event_id group by edt.id");
		sql.append(" order by edt.date, edt.time,l.location_name asc");

		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<EventDateTimeResponse>() {
				@Override
				public EventDateTimeResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<EventDateTime> eventDateTimeList = new ArrayList<EventDateTime>();
					EventDateTime eventDateTime = null;
					while (rs.next()) {
						eventDateTime = new EventDateTime();
						eventDateTime.setEventDateTimeId(rs.getLong("id"));
						eventDateTime.setEventId(rs.getLong("event_id"));
						eventDateTime.setLocationId(rs.getInt("location_id"));
						eventDateTime.setLocationName(rs.getString("location_name"));
						eventDateTime.setEventName(rs.getString("event_name"));
						eventDateTime.setDate(rs.getString("date"));
						eventDateTime.setTime(rs.getString("time"));
						eventDateTime.setNoOfSeats("" + rs.getLong("num_seats"));
						eventDateTime.setEnable(rs.getString("enable"));
						eventDateTime.setNoOfNotifiedReservations(rs.getInt("noOfNotifiedReservations"));
						eventDateTimeList.add(eventDateTime);
					}
					eventDateTimeResponse.setEventDateTimeList(eventDateTimeList);
					return eventDateTimeResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_ADMIN_EVENT_DATE_TIME_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public void updateEventDateTimeStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventDateTimeId, String enable,
			BaseResponse baseResponse) throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("update event_date_time set enable = ? where id = ?");
			int updateStatus = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { enable, Long.valueOf(eventDateTimeId) });
			sql.setLength(0);
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.OPEN_CLOSE_EVENT_DATE_TIME.getValue(), e);
		}
	}

	@Override
	public void addEventDateTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId,
			final EventDateTime eventDateTime, BaseResponse baseResponse) throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			final String sql = "insert into event_date_time (event_id, location_id, date, time, num_seats) values (?,?,?,?,?)";
			KeyHolder holder = new GeneratedKeyHolder();
			int count = jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
				int i = 1;

				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
					ps.setLong(i++, eventDateTime.getEventId());
					ps.setInt(i++, eventDateTime.getLocationId());
					ps.setString(i++, eventDateTime.getDate());
					ps.setString(i++, eventDateTime.getTime());
					ps.setInt(i++, Integer.valueOf(eventDateTime.getNoOfSeats()));
					return ps;
				}
			}, holder);

			final Long id = holder.getKey().longValue();
			if (count > 0) {
				baseResponse.setResponseStatus(true);
			} else {
				throw new Exception("EventDateTime not saved to DB!.");
			}
			int noOfRows = Integer.valueOf(eventDateTime.getNoOfSeats());
			final List<Seat> listOfSeats = new ArrayList<Seat>();
			Seat seat = null;
			int rowNum = 1;
			int columnNum = 0;
			for (int i = 0; i < noOfRows; i++) {
				int num = i + 1;
				seat = new Seat();
				seat.setEventDateTimeId(id);
				seat.setSeatNumber("" + num);
				seat.setDisplaySeatNumber("" + num);
				seat.setScheduleId((long) 0);
				seat.setPlacement(num);
				if (columnNum > 0 && columnNum % 5 == 0) {
					seat.setRowId(++rowNum);
					columnNum = 1;
					seat.setColumnId(columnNum);
				} else {
					seat.setRowId(rowNum);
					seat.setColumnId(++columnNum);
				}
				listOfSeats.add(seat);
			}

			final String seatSQL = "insert into seat(event_date_time_id,seat_number,display_seat_number,schedule_id,placement,row_id,column_id) value(?,?,?,?,?,?,?)";
			jdbcCustomTemplate.getJdbcTemplate().batchUpdate(seatSQL.toString(), new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Seat seat = listOfSeats.get(i);
					ps.setLong(1, seat.getEventDateTimeId());
					ps.setString(2, seat.getSeatNumber());
					ps.setString(3, seat.getDisplaySeatNumber());
					ps.setLong(4, seat.getScheduleId());
					ps.setInt(5, seat.getPlacement());
					ps.setInt(6, seat.getRowId());
					ps.setInt(7, seat.getColumnId());
				}

				@Override
				public int getBatchSize() {
					return listOfSeats.size();
				}
			});

			dsTransactionManager.commit(status);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.ADD_EVENT_DATE_TIME.getValue(), e);
		}

	}

	@Override
	public void updateEventDateTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, EventDateTime eventDateTime,
			BaseResponse baseResponse) throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			String sql = "update event_date_time set date=? , time=? where id=?";
			Object[] columns = new Object[] { eventDateTime.getDate(), eventDateTime.getTime(), eventDateTime.getEventDateTimeId() };
			jdbcCustomTemplate.getJdbcTemplate().update(sql, columns);
			updateNotify(jdbcCustomTemplate, customLogger, langCode, device, eventDateTime);
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			customLogger.fatal("Error in updateEventDateTime:", e);
			throw new TelAppointDBException(AdminFlowStateConstants.UPDATE_EVENT_DATE_TIME.getValue(), e);
		}
	}

	private void updateNotify(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, EventDateTime eventDateTime) {
		StringBuilder sql = new StringBuilder();
		sql.append("update notify set due_date_time = :dateDateTime where event_date_time_id = :eventDateTimeId");
		MapSqlParameterSource mapSQLParameterSource = new MapSqlParameterSource();
		mapSQLParameterSource.addValue("dateDateTime", eventDateTime.getDate() + " " + eventDateTime.getTime());
		mapSQLParameterSource.addValue("eventDateTimeId", eventDateTime.getEventDateTimeId());
		jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), mapSQLParameterSource);
	}

	@Override
	public void updateEventSeats(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, EventDateTime eventDateTime,
			final BaseResponse baseResponse) throws Exception {
		EventDateTime existingEventDateTime = new EventDateTime();

		if (eventDateTime.getEventDateTimeId() == 0) {
			throw new Exception("EventDateTime Id is zero");
		}
		getEventDateTimeById(jdbcCustomTemplate, customLogger, langCode, device, "" + eventDateTime.getEventDateTimeId(), existingEventDateTime);
		existingEventDateTime.setDate(DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(existingEventDateTime.getDate()));
		existingEventDateTime.setTime(DateUtils.convert12To24HoursHHMMSSFormat(existingEventDateTime.getTime()));

		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			StringBuilder sql = new StringBuilder("update event_date_time set num_seats=?  where id=?");
			Object[] columns = new Object[] { eventDateTime.getNoOfSeats(), eventDateTime.getEventDateTimeId() };
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), columns);
			int noOfExistingSeats = Integer.valueOf(existingEventDateTime.getNoOfSeats());
			int diffSeats = noOfExistingSeats - Integer.valueOf(eventDateTime.getNoOfSeats());
			int noOfSeatsToIncreaseOrDecrease = Math.abs(diffSeats);
			if (diffSeats < 0) {
				increaseSeats(jdbcCustomTemplate, customLogger, noOfExistingSeats, noOfSeatsToIncreaseOrDecrease, eventDateTime.getEventDateTimeId());
			} else {
				sql.setLength(0);
				final Set<Long> finalSeatIds = new HashSet<Long>();
				boolean canWeDecrease = decreaseSeats(jdbcCustomTemplate, customLogger, sql, finalSeatIds, noOfSeatsToIncreaseOrDecrease, eventDateTime, baseResponse);

				ImmutableMap<String, ?> parameters = ImmutableMap.of("ids", finalSeatIds);
				if (canWeDecrease) {
					sql.setLength(0);
					sql.append("delete from seat where id in (:ids)");
					jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), parameters);
				} else {
					baseResponse.setResponseStatus(false);
					baseResponse.setResponseMessage("Unable to update the seats");
					dsTransactionManager.rollback(status);
					return;
				}
			}
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			customLogger.fatal("Error in updateEventSeatNumbers:", e);
			throw new TelAppointDBException(AdminFlowStateConstants.UPDATE_EVENT_SEATS.getValue(), e);
		}

	}

	private boolean decreaseSeats(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, StringBuilder sql, final Set<Long> seatIds, int seatToDecrease,
			EventDateTime eventDateTime, BaseResponse baseResponse) throws Exception {
		sql.append("select s.id, s. schedule_id from event_date_time edt, seat s where date=? and time=?");
		sql.append(" and edt.event_id=? and edt.location_id=? ");
		sql.append(" and  s.event_date_time_id = edt.id and s.event_date_time_id=? order by s.id desc limit ?");
		Object columns[] = new Object[] { eventDateTime.getDate(), eventDateTime.getTime(), eventDateTime.getEventId(), eventDateTime.getLocationId(),
				eventDateTime.getEventDateTimeId(), seatToDecrease };

		// check whether we can do decrease the seats
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), columns, new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					if (rs.getLong("schedule_id") > 0) {
						return false;
					}
					seatIds.add(rs.getLong("id"));
				}
				return true;
			}
		});
	}

	private void increaseSeats(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, int maxExistingSeatNumber, int noOfSeatsToIncrease, long eventDateTimeId)
			throws Exception {

		Object[] returnArray = new Object[2];
		populateRowIdAndColumnIdLastRow(jdbcCustomTemplate, customLogger, eventDateTimeId, returnArray);

		int rowNum = Integer.valueOf(returnArray[0].toString());
		int columnNum = Integer.valueOf(returnArray[1].toString());
		final List<Seat> listOfSeats = new ArrayList<Seat>();
		Seat seat = null;
		for (int i = 0; i < noOfSeatsToIncrease; i++) {
			int num = maxExistingSeatNumber + i + 1;
			seat = new Seat();
			seat.setEventDateTimeId(eventDateTimeId);
			seat.setSeatNumber("" + num);
			seat.setDisplaySeatNumber("" + num);
			seat.setScheduleId((long) 0);
			seat.setPlacement(num);
			if (columnNum % 5 == 0) {
				seat.setRowId(++rowNum);
				columnNum = 1;
				seat.setColumnId(columnNum);
			} else {
				seat.setRowId(rowNum);
				seat.setColumnId(++columnNum);
			}
			listOfSeats.add(seat);
		}

		final String seatSQL = "insert into seat(event_date_time_id,seat_number,display_seat_number,schedule_id,placement,row_id,column_id) value(?,?,?,?,?,?,?)";
		jdbcCustomTemplate.getJdbcTemplate().batchUpdate(seatSQL.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Seat seat = listOfSeats.get(i);
				ps.setLong(1, seat.getEventDateTimeId());
				ps.setString(2, seat.getSeatNumber());
				ps.setString(3, seat.getDisplaySeatNumber());
				ps.setLong(4, seat.getScheduleId());
				ps.setInt(5, seat.getPlacement());
				ps.setInt(6, seat.getRowId());
				ps.setInt(7, seat.getColumnId());
			}

			@Override
			public int getBatchSize() {
				return listOfSeats.size();
			}
		});

	}

	@Override
	public void getCalendarOverview(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String startDate, String endDate,
			final CalendarOverviewResponse calendarOverviewResponse) {
		try {
			CalendarOverviewStoredProcedure carsStoredProcedure = new CalendarOverviewStoredProcedure(jdbcCustomTemplate.getJdbcTemplate());
			Map data = carsStoredProcedure.getCalendarView(startDate, endDate);
			@SuppressWarnings("unchecked")
			List<CalendarOverview> calendarOverviewList = (List<CalendarOverview>) data.get(SPConstants.RESULT_LIST.getValue());
			calendarOverviewResponse.setCalendarOverViewList(calendarOverviewList);
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_ADMIN_CALENDAR_OVERVIEW_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public void getGraphDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String date,
			final GraphResponse graphResponse) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct concat(event_name,' - ',DATE_FORMAT(edt1.time, '%l:%i %p')) as eventTimeLabel,");
			sql.append("(select count(*) from seat s2, event_date_time edt2 ");
			sql.append("where edt2.id  = edt1.id and s2.event_date_time_id=edt2.id and s2.schedule_id>0) as bookedSeats,");
			sql.append("(select count(*) from seat s2, event_date_time edt2 ");
			sql.append("where edt2.id  = edt1.id and s2.event_date_time_id=edt2.id and s2.schedule_id=0 and reserved='N') as availableSeats,");
			sql.append("(select count(*) from seat s2, event_date_time edt2 ");
			sql.append("where edt2.id  = edt1.id and s2.event_date_time_id=edt2.id and s2.schedule_id=0 and reserved='Y') as reservedSeats");
			sql.append(" from event_date_time edt1,seat s1, location l, event e where edt1.date = ? and location_id=? and edt1.id = s1.event_date_time_id");
			sql.append(" and edt1.event_id = e.event_id and edt1.location_id = l.id and edt1.enable='Y' order by edt1.time");

			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { date, Long.valueOf(locationId) }, new ResultSetExtractor<Boolean>() {

				@Override
				public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
					StringBuilder eventTimeLabelSB = new StringBuilder();
					StringBuilder availableSeatSB = new StringBuilder();
					StringBuilder bookedSeatsSB = new StringBuilder();
					StringBuilder reservedSeatsSB = new StringBuilder();
					boolean initial = true;
					while (rs.next()) {
						if (initial) {
							initial = false;
						} else {
							eventTimeLabelSB.append(",");
							availableSeatSB.append(",");
							bookedSeatsSB.append(",");
							reservedSeatsSB.append(",");
						}
						eventTimeLabelSB.append(rs.getString("eventTimeLabel"));
						availableSeatSB.append(rs.getString("availableSeats"));
						bookedSeatsSB.append(rs.getString("bookedSeats"));
						reservedSeatsSB.append(rs.getString("reservedSeats"));
					}
					graphResponse.setEventTimeStr(eventTimeLabelSB.toString());
					graphResponse.setNoOfOpenResv(availableSeatSB.toString());
					graphResponse.setNoOfBookedResv(bookedSeatsSB.toString());
					graphResponse.setNoOfResvered(reservedSeatsSB.toString());
					return true;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GRAPH_DETAILS.getValue(), e);
		}
		return;
	}

	@Override
	public void getSeatsCalendarView(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, String device, String locationId,
			String eventId, String date, final long eventDateTimeId, final SeatsCalendarView seatsCalendarView, String showRemainderIcons) throws Exception {

		Object[] returnArray = new Object[2];
		populateReturnArray(jdbcCustomTemplate, customLogger, eventDateTimeId, returnArray);
		seatsCalendarView.init(Integer.valueOf(returnArray[0].toString()), Integer.valueOf(returnArray[1].toString()));
		final Object seatViewArray[][] = seatsCalendarView.getSeatView();

		final DynamicTableItems dynamicToolTipItems = new DynamicTableItems();
		populateDynamicTableItems(jdbcCustomTemplate, customLogger, "tooltip_results", dynamicToolTipItems);

		final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
		populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
		final DynamicSQL dynamicSQL = dynamicSQLMap.get("tooltip");
		String toolTipSQL = dynamicSQL.getSqlQuery();
		toolTipSQL = toolTipSQL.replaceAll("%DYNAMIC_SELECT%", dynamicToolTipItems.getDynamicSelect());
		toolTipSQL = toolTipSQL.replaceAll("%DYNAMIC_FROM%", dynamicToolTipItems.getDynamicFrom());
		final String toolTipSQLFinal = toolTipSQL;
		final String keys[] = dynamicSQL.getDynamicKeyMapping().split(",");
		final String aliasColumnNames = dynamicToolTipItems.getAliasName();
		final String dataTypes = dynamicToolTipItems.getDynamicType();
		final String javaRef = dynamicToolTipItems.getDynamicJavaReflection();
		final String titles = dynamicToolTipItems.getDynamicTitle();
		final String hides = dynamicToolTipItems.getDynamicHide();
		seatsCalendarView.setTitle(titles);
		seatsCalendarView.setJavaRef(javaRef);
		seatsCalendarView.setHides(hides);

		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		if (keys.length == 1 && "TOOLTIP_SCHEDULE_ID".equals(keys[0])) {
			String scheduleIds = getScheduleIdList(jdbcCustomTemplate, customLogger, eventId, locationId, date);
			if (scheduleIds == null || "".equals(scheduleIds)) {
				parameterSource.addValue("ids", -3);
			} else {
				List<Integer> scheduleIdList = getListFromCommaSeperatedString(scheduleIds);
				parameterSource.addValue("ids", scheduleIdList);
			}
		} else {
			// TODO: need to handle if any required.
		}

		final Class<ReservationDetails> beanType = ReservationDetails.class;
		final Map<Object, Object> map = new HashMap<Object, Object>();
		final String keyString = "scheduleId";
		populateQueryMap(jdbcCustomTemplate, customLogger, toolTipSQLFinal, parameterSource, aliasColumnNames, dataTypes, javaRef, titles, map, beanType, keyString);
		populateVoiceMsgDetails(jdbcCustomTemplate, customLogger, parameterSource, map);
		if (showRemainderIcons != null && "Y".equalsIgnoreCase(showRemainderIcons)) {
			populateRemainderIconsDetails(jdbcCustomTemplate, customLogger, parameterSource, map);
		}
		StringBuilder sql = new StringBuilder("select id, seat_number, reserved, display_seat_number,row_id,column_id,schedule_id from seat");
		sql.append(" where event_date_time_id = ? order by row_id, column_id");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { eventDateTimeId }, new ResultSetExtractor<SeatsCalendarView>() {
				@Override
				public SeatsCalendarView extractData(ResultSet rs) throws SQLException, DataAccessException {
					SeatView seatView = null;
					try {
						while (rs.next()) {
							seatView = new SeatView();
							seatView.setSeatId("" + rs.getLong("id"));
							seatView.setSeatNumber(rs.getString("display_seat_number"));
							int rowId = rs.getInt("row_id");
							int columnId = rs.getInt("column_id");
							String reserved = rs.getString("reserved");
							seatView.setReserved(reserved);
							Long scheduleId = rs.getLong("schedule_id");

							if (scheduleId != null && scheduleId.longValue() > 0) {
								seatView.setBooked("Y");
								Object resvObject = map.get(scheduleId.toString());
								if (resvObject != null) {
									seatView.setReservationDetails((ReservationDetails) map.get(scheduleId.toString()));
								} else {
									seatView.setReservationDetails(null);
								}
							}

							seatViewArray[rowId - 1][columnId - 1] = seatView;
						}
					} catch (Exception e) {
						throw new SQLException("Exception in getSeatsCalendarView method!");
					}
					seatsCalendarView.setSeatView(seatViewArray);
					return seatsCalendarView;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.SEATS_CALENDAR_VIEW.getValue(), e);
		}
		return;
	}

	private void populateReturnArray(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long eventDateTimeId, final Object[] returnArray) {
		StringBuilder sql = new StringBuilder("select max(row_id) as maxRowNum,max(column_id) as maxColumnNum from seat");
		sql.append(" where event_date_time_id = ?");
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { eventDateTimeId }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					returnArray[0] = rs.getInt("maxRowNum");
					returnArray[1] = rs.getInt("maxColumnNum");
				}
				return new Long(0);
			}
		});
	}

	private void populateRowIdAndColumnIdLastRow(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, long eventDateTimeId, final Object[] returnArray) {
		StringBuilder sql = new StringBuilder("select row_id as rowNum, column_id as columnNum from seat ");
		sql.append(" where event_date_time_id = ? order by id desc limit 1");
		returnArray[0] = 0;
		returnArray[1] = 0;
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { eventDateTimeId }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					returnArray[0] = rs.getInt("rowNum");
					returnArray[1] = rs.getInt("columnNum");
				}
				return new Long(0);
			}

		});
	}

	private void initViewArray(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String locationId, String eventId, String date, final Object viewObj) {
		StringBuilder sql = new StringBuilder("select max(num_seats) as maxRowNum, count(id) as maxColumnNum from event_date_time");
		sql.append(" where date=? and location_id=? and event_id=? and enable='Y'");

		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { date, Integer.valueOf(locationId), Long.valueOf(eventId) }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					int rowId = rs.getInt("maxRowNum");
					int columnId = rs.getInt("maxColumnNum");

					if (viewObj instanceof SeatsCalendarView) {
						SeatsCalendarView view = (SeatsCalendarView) viewObj;
						view.init(rowId, columnId);
					} else if (viewObj instanceof DailyCalendarView) {
						DailyCalendarView view = (DailyCalendarView) viewObj;
						view.init(rowId, columnId);
					}

				}
				return new Long(0);
			}
		});
	}

	@Override
	public void getDailyCalendarView(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, String device, String locationId,
			String eventId, String date, final DailyCalendarView dailyCalendarView, String showRemainderIcons) throws Exception {

		initViewArray(jdbcCustomTemplate, customLogger, locationId, eventId, date, dailyCalendarView);
		final Object dailyViewArray[][] = dailyCalendarView.getCaleandarView();

		final DynamicTableItems dynamicTableItems = new DynamicTableItems();
		populateDynamicTableItems(jdbcCustomTemplate, customLogger, "tooltip_results", dynamicTableItems);
		final Map<String, DynamicSQL> dynamicSQLMap = new HashMap<String, DynamicSQL>();
		populateDynamicSQL(jdbcCustomTemplate, customLogger, dynamicSQLMap);
		final DynamicSQL dynamicSQL = dynamicSQLMap.get("tooltip");
		String toolTipSQL = dynamicSQL.getSqlQuery();
		toolTipSQL = toolTipSQL.replaceAll("%DYNAMIC_SELECT%", dynamicTableItems.getDynamicSelect());
		toolTipSQL = toolTipSQL.replaceAll("%DYNAMIC_FROM%", dynamicTableItems.getDynamicFrom());
		final String toolTipSQLFinal = toolTipSQL;
		final String keys[] = dynamicSQL.getDynamicKeyMapping().split(",");

		StringBuilder sql = new StringBuilder();
		sql.append("select s.event_date_time_id,edt.time,s.placement,s.display_seat_number,s.id,s.reserved,s.schedule_id");
		sql.append(" from seat s, event_date_time edt where date=? and edt.event_id=? and edt.location_id=?");
		sql.append(" and s.event_date_time_id=edt.id and edt.enable='Y'");
		sql.append(" order by s.placement,edt.time");

		final String javaRef = dynamicTableItems.getDynamicJavaReflection();
		final String titles = dynamicTableItems.getDynamicTitle();
		final String aliasColumnNames = dynamicTableItems.getAliasName();
		final String dataTypes = dynamicTableItems.getDynamicType();
		final String hides = dynamicTableItems.getDynamicHide();
		final Class<ReservationDetails> beanType = ReservationDetails.class;

		dailyCalendarView.setTitle(titles);
		dailyCalendarView.setJavaRef(javaRef);
		dailyCalendarView.setHides(hides);

		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		if (keys.length == 1 && "TOOLTIP_SCHEDULE_ID".equals(keys[0])) {
			String scheduleIds = getScheduleIdList(jdbcCustomTemplate, customLogger, eventId, locationId, date);
			if (scheduleIds == null || "".equals(scheduleIds)) {
				parameterSource.addValue("ids", -3);
			} else {
				List<Integer> scheduleIdList = getListFromCommaSeperatedString(scheduleIds);
				parameterSource.addValue("ids", scheduleIdList);
			}
		} else {
			// TODO: need to handle if any required.
		}

		final Map<Object, Object> map = new HashMap<Object, Object>();
		final String keyString = "scheduleId";
		populateQueryMap(jdbcCustomTemplate, customLogger, toolTipSQLFinal, parameterSource, aliasColumnNames, dataTypes, javaRef, titles, map, beanType, keyString);
		populateVoiceMsgDetails(jdbcCustomTemplate, customLogger, parameterSource, map);
		if (showRemainderIcons != null && "Y".equalsIgnoreCase(showRemainderIcons)) {
			populateRemainderIconsDetails(jdbcCustomTemplate, customLogger, parameterSource, map);
		}

		final Map<String, List<CalendarView>> dailyViewMap = new LinkedHashMap<String, List<CalendarView>>();
		final List<String> timesList = new ArrayList<String>();
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { date, Long.valueOf(eventId), Integer.valueOf(locationId) }, new ResultSetExtractor<Long>() {
				@Override
				public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
					CalendarView calendarView = null;
					try {
						while (rs.next()) {
							calendarView = new CalendarView();
							calendarView.setSeatId("" + rs.getLong("id"));
							calendarView.setSeatNumber(rs.getString("display_seat_number"));
							calendarView.setEventDateTimeId("" + rs.getLong("event_date_time_id"));
							String time = DateUtils.convert24To12HMMAFormat(rs.getString("time"));
							calendarView.setTime(time);
							calendarView.setReserved(rs.getString("reserved"));
							Long scheduleId = rs.getLong("schedule_id");
							if (scheduleId != null && scheduleId.longValue() > 0) {
								Object resvObject = map.get(scheduleId.toString());
								calendarView.setBooked("Y");
								if (resvObject != null) {
									calendarView.setToolTipDetails((ReservationDetails) map.get(scheduleId.toString()));
								} else {
									calendarView.setToolTipDetails(null);
								}
							}

							if (dailyViewMap.containsKey(time)) {
								List<CalendarView> calendarViewList = dailyViewMap.get(time);
								calendarViewList.add(calendarView);
							} else {
								List<CalendarView> calendarViewList = new ArrayList<CalendarView>();
								calendarViewList.add(calendarView);
								timesList.add(time);
								dailyViewMap.put(time, calendarViewList);
							}
						}

					} catch (Exception e) {
						throw new SQLException("Exception in getDailyCalendarView method!");
					}
					return (long) 0;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.DAILY_CALENDAR_VIEW.getValue(), e);
		}

		String time = null;
		List<CalendarView> calendarViewList = null;
		for (int columnIndex = 0; columnIndex < timesList.size(); columnIndex++) {
			time = timesList.get(columnIndex);
			calendarViewList = dailyViewMap.get(time);
			for (int rowIndex = 0; rowIndex < calendarViewList.size(); rowIndex++) {
				dailyViewArray[rowIndex][columnIndex] = calendarViewList.get(rowIndex);
			}
		}
		dailyCalendarView.setCaleandarView(dailyViewArray);
		return;
	}

	private void populateVoiceMsgDetails(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, MapSqlParameterSource parameterSource,
			final Map<Object, Object> map) {
		String sql = "select schedule_id, display_flag, file_path, file_name from voice_msg where schedule_id in (:ids)";
		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String scheudleId = "";
				String fileFullPath = "#";
				ReservationDetails resvDetails = null;
				while (rs.next()) {
					try {
						scheudleId = "" + rs.getLong("schedule_id");
						resvDetails = (ReservationDetails) map.get(scheudleId);
						String filePath = rs.getString("file_path");
						String fileName = rs.getString("file_name");
						if (filePath == null || "".equals(filePath) || fileName == null && "".equals(fileName)) {
							customLogger.error("filepath or file name doesn't exist");
						} else {
							fileFullPath = filePath + "/" + fileName;
							try {
								File file = new File(fileFullPath);
								if (file.exists() == false) {
									customLogger.error("File doesn't exist: " + filePath);
								}
							} catch (Exception e) {
								customLogger.error("File doesn't exist.");
								fileFullPath = "#";
							}
						}
						resvDetails.setFileFullPath(fileFullPath);
						// map.put(scheudleId, resvDetails);
					} catch (Exception e) {
						throw new SQLException("Exception in populateVoiceMsgDetails.");
					}
				}
				return new Long(0);
			}
		});
	}

	private void populateRemainderIconsDetails(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, MapSqlParameterSource parameterSource,
			final Map<Object, Object> map) {
		StringBuilder sql = new StringBuilder(
				" SELECT n.id, n.schedule_id,n.notify_by_phone,n.notify_phone_status,n.notify_by_sms,n.notify_sms_status,n.notify_by_email,n.notify_email_status,max(nps.attempt_id) as maxAttemptId ");
		sql.append(" FROM notify n LEFT JOIN notify_phone_status nps ON n.id=nps.notify_id WHERE  n.schedule_id in (:ids) group by n.id,n.schedule_id ");

		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), parameterSource, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String scheudleId = "";
				ReservationDetails resvDetails = null;
				while (rs.next()) {
					try {
						scheudleId = "" + rs.getLong("schedule_id");
						resvDetails = (ReservationDetails) map.get(scheudleId);
						resvDetails.setNotifyByPhone(rs.getString("notify_by_phone"));
						resvDetails.setNotifyPhoneStatus(rs.getInt("notify_phone_status"));
						resvDetails.setNotifyBySMS(rs.getString("notify_by_sms"));
						resvDetails.setNotifySMSStatus(rs.getInt("notify_sms_status"));
						resvDetails.setNotifyByEmail(rs.getString("notify_by_email"));
						resvDetails.setNotifyEmailStatus(rs.getInt("notify_email_status"));
						resvDetails.setAttemptCount(rs.getInt("maxAttemptId"));
					} catch (Exception e) {
						throw new SQLException("Exception in populateRemainderIconsDetails.");
					}
				}
				return new Long(0);
			}
		});
	}

	private String getScheduleIdList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String eventId, String locationId, String date) throws Exception {
		StringBuilder sql = new StringBuilder(
				"select group_concat(s.schedule_id) as scheduleIds from seat s,event_date_time edt where edt.date =? and event_id=? and location_id=?");
		sql.append(" and s.schedule_id > 0 and s.event_date_time_id = edt.id and edt.enable='Y'");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { date, Long.valueOf(eventId), Integer.valueOf(locationId) },
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						rs.setFetchSize(1);
						if (rs.next()) {
							return rs.getString("scheduleIds");
						}
						return "";
					}
				});
	}

	private String getScheduleIdListByEventIds(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, MapSqlParameterSource paramSource) throws Exception {
		StringBuilder sql = new StringBuilder("select group_concat(s.schedule_id) as scheduleIds from seat s where s.event_date_time_id in (:ids)");
		sql.append(" and s.schedule_id > 0");
		return jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					return rs.getString("scheduleIds");
				}
				return "";
			}
		});
	}

	@Override
	public void populateDynamicTableItems(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String category, final DynamicTableItems dynamicToolTipItems)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select group_concat(distinct table_name order by placement) as tableNames, group_concat(distinct concat(table_column,' as ',alias_name) order by placement) as columnNames,");
		sql.append(" group_concat(alias_name order by placement) as aliasName,");
		sql.append(" group_concat(`type` order by placement) as type, group_concat(java_reflection order by placement) as javaRef,group_concat(title order by placement) as title,");
		sql.append(" group_concat(`hide` order by placement) as hide");
		sql.append(" from dynamic_table_items where category= ?");
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { category }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					dynamicToolTipItems.setDynamicFrom(rs.getString("tableNames"));
					dynamicToolTipItems.setDynamicSelect(rs.getString("columnNames"));
					dynamicToolTipItems.setAliasName(rs.getString("aliasName"));
					dynamicToolTipItems.setDynamicType(rs.getString("type"));
					dynamicToolTipItems.setDynamicJavaReflection(rs.getString("javaRef"));
					dynamicToolTipItems.setDynamicTitle(rs.getString("title"));
					dynamicToolTipItems.setDynamicHide(rs.getString("hide"));
				}
				return new Long(0);
			}
		});
	}

	@Override
	public void populateDynamicSQL(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, DynamicSQL> dynamicSQLMap) throws Exception {
		StringBuilder sql = new StringBuilder("select category, sql_query, dynamic_key_mapping from dynamic_sql where enable='Y'");
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				DynamicSQL dynamicSQL = null;
				while (rs.next()) {
					dynamicSQL = new DynamicSQL();
					String category = rs.getString("category");
					dynamicSQL.setCategory(category);
					dynamicSQL.setSqlQuery(rs.getString("sql_query"));
					dynamicSQL.setDynamicKeyMapping(rs.getString("dynamic_key_mapping"));
					dynamicSQLMap.put(category, dynamicSQL);
				}
				return new Long(0);
			}
		});
	}

	public <T> void executeQueryDynamicQuery(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String sql, Object[] dynamicSQLInject,
			final String aliasColumnNames, final String dataTypes, final String javaRef, final String title, final List<T> dataList, final Class<T> classType) throws Exception {

		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), dynamicSQLInject, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String alias[] = aliasColumnNames.split(",");
				String types[] = dataTypes.split(",");
				String fieldName[] = javaRef.split(",");
				while (rs.next()) {
					try {
						T object = getInstance(classType);
						for (int i = 0; i < alias.length; i++) {
							if ("int".equalsIgnoreCase(types[i])) {
								if ("status".equals(fieldName[i])) {
									String statusMessage = ReservataionStatus.AppointmentStatusDescription.getAppointmentStatusDescription(rs.getInt(alias[i]));
									CoreUtils.setPropertyValue(object, fieldName[i], statusMessage);
								} else {
									CoreUtils.setPropertyValue(object, fieldName[i], rs.getInt(alias[i]));
								}
							}

							if ("long".equalsIgnoreCase(types[i])) {
								CoreUtils.setPropertyValue(object, fieldName[i], rs.getLong(alias[i]));
							}

							if ("varchar".equalsIgnoreCase(types[i])) {
								CoreUtils.setPropertyValue(object, fieldName[i], rs.getString(alias[i]));
							}
						}
						dataList.add(object);
					} catch (Exception e) {
						throw new SQLException("Exception in executeQueryDynamicQuery.");
					}
				}
				return new Long(0);
			}
		});
	}

	public <T> void executeDynamicQuery(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String sql, MapSqlParameterSource paramSource,
			final String aliasColumnNames, final String dataTypes, final String javaRef, final String title, final List<T> dataList, final Class<T> classType) throws Exception {
		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String alias[] = aliasColumnNames.split(",");
				String types[] = dataTypes.split(",");
				String fieldName[] = javaRef.split(",");
				while (rs.next()) {
					try {
						T object = getInstance(classType);
						for (int i = 0; i < alias.length; i++) {
							if ("int".equalsIgnoreCase(types[i])) {
								// schedule table - status column
								if ("status".equals(fieldName[i])) {
									String statusMessage = ReservataionStatus.AppointmentStatusDescription.getAppointmentStatusDescription(rs.getInt(alias[i]));
									CoreUtils.setPropertyValue(object, fieldName[i], statusMessage);
								} else if ("apptType".equals(fieldName[i])) { // ivr_calls
																				// table
																				// -
																				// appt_type
																				// column
									String apptTypeMessage = ReservationtType.ApptTypeStatusMessage.getMessage(rs.getInt(alias[i]));
									CoreUtils.setPropertyValue(object, fieldName[i], apptTypeMessage);
								} else if ("callStatus".equals(fieldName[i])) { // /
																				// notify_phone_status
																				// table
																				// -
																				// call_status
																				// column
									String callStatus = IVRNotifyStatusConstants.NotifyPhoneStatusMessage.getNotifyPhoneMessage(rs.getInt(alias[i]));
									CoreUtils.setPropertyValue(object, fieldName[i], callStatus);
								} else {
									CoreUtils.setPropertyValue(object, fieldName[i], rs.getInt(alias[i]));
								}
							}

							if ("long".equalsIgnoreCase(types[i])) {
								if ("minutes".equals(fieldName[i])) {
									Long seconds = rs.getLong(alias[i]);
									DecimalFormat df = new DecimalFormat();
									df.applyPattern("#0.##");
									String minutes = (df.format((seconds / 60)));
									// String minutes =
									// LocalTime.MIN.plusSeconds(seconds).toString();
									CoreUtils.setPropertyValue(object, fieldName[i], minutes);
								} else {
									CoreUtils.setPropertyValue(object, fieldName[i], rs.getLong(alias[i]));
								}
							}

							if ("varchar".equalsIgnoreCase(types[i])) {
								CoreUtils.setPropertyValue(object, fieldName[i], rs.getString(alias[i]));
							}
						}
						dataList.add(object);
					} catch (Exception e) {
						throw new SQLException("Exception in executeQueryDynamic.");
					}
				}
				return new Long(0);
			}
		});
	}

	public <T> void executeDynamicQueryForNotifications(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String sql, MapSqlParameterSource paramSource,
			final String aliasColumnNames, final String dataTypes, final String javaRef, final String title, final ReservationRemindersResponse resvReminderResponse)
			throws Exception {
		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSource, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String alias[] = aliasColumnNames.split(",");
				String types[] = dataTypes.split(",");
				String fieldName[] = javaRef.split(",");
				Map<DailyReminderView, List<NotifyDailyView>> resvReminderMap = new LinkedHashMap<DailyReminderView, List<NotifyDailyView>>();

				List<NotifyDailyView> notifications = null;
				DailyReminderView dailyReminderView = null;
				NotifyDailyView notification = null;
				boolean showTime = false;
				String currentTime = null;
				String oldTime = null;
				while (rs.next()) {
					try {
						notification = new NotifyDailyView();
						for (int i = 0; i < alias.length; i++) {
							if ("int".equalsIgnoreCase(types[i])) {
								if ("status".equals(fieldName[i])) {
									String statusMessage = ReservataionStatus.AppointmentStatusDescription.getAppointmentStatusDescription(rs.getInt(alias[i]));
									CoreUtils.setPropertyValue(notification, fieldName[i], statusMessage);
								} else {
									CoreUtils.setPropertyValue(notification, fieldName[i], rs.getInt(alias[i]));
								}
							}

							if ("long".equalsIgnoreCase(types[i])) {
								CoreUtils.setPropertyValue(notification, fieldName[i], rs.getLong(alias[i]));
							}

							if ("varchar".equalsIgnoreCase(types[i])) {
								CoreUtils.setPropertyValue(notification, fieldName[i], rs.getString(alias[i]));
							}
						}
						oldTime = currentTime;
						currentTime = notification.getTime();
						if (!showTime && StringUtils.isNotEmpty(currentTime) && StringUtils.isNotEmpty(oldTime) && !oldTime.equals(currentTime)) {
							showTime = true;
						}

						dailyReminderView = new DailyReminderView();
						if (resvReminderMap.size() > 0) {
							dailyReminderView.setEventId(notification.getEventId());
							Set<DailyReminderView> keySet = resvReminderMap.keySet();
							Iterator<DailyReminderView> it = keySet.iterator();
							while (it.hasNext()) {
								DailyReminderView dailyView = it.next();
								if (dailyView.equals(dailyReminderView)) {
									notifications = resvReminderMap.get(dailyReminderView);
									notifications.add(notification);
									dailyView.setTotalNotifications(dailyView.getTotalNotifications() + 1);
									if (notification.getNotifyStatus().intValue() == 3) {
										dailyView.setTotalConfirmed(dailyView.getTotalConfirmed() + 1);
									}
									break;
								} else {
									notifications = new ArrayList<NotifyDailyView>();
									notifications.add(notification);
									dailyReminderView.setTotalNotifications(1);
									dailyReminderView.setTotalConfirmed(1);
									dailyReminderView.setEventId(notification.getEventId());
									dailyReminderView.setEventName(notification.getEventName());
									resvReminderMap.put(dailyReminderView, notifications);
								}
							}
						} else {
							notifications = new ArrayList<NotifyDailyView>();
							notifications.add(notification);
							dailyReminderView.setTotalNotifications(1);
							dailyReminderView.setEventId(notification.getEventId());
							dailyReminderView.setEventName(notification.getEventName());
							if (notification.getNotifyStatus().intValue() == 3) {
								dailyReminderView.setTotalConfirmed(1);
							} else {
								dailyReminderView.setTotalConfirmed(0);
							}
							resvReminderMap.put(dailyReminderView, notifications);
						}
					} catch (Exception e) {
						throw new SQLException("Exception in executeDynamicQueryForNotifications.");
					}
				}
				resvReminderResponse.setResvReminders(resvReminderMap);
				resvReminderResponse.setShowTime(showTime);
				return new Long(0);
			}
		});
	}

	public <T> void populateQueryMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String sql, MapSqlParameterSource sqlParameterSource,
			final String aliasColumnNames, final String dataTypes, final String javaRef, final String title, final Map<Object, Object> dataMap, final Class<T> classType,
			final String keyFieldName) throws Exception {
		customLogger.info("sql:"+sql.toString());
		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), sqlParameterSource, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {

				String alias[] = aliasColumnNames.split(",");
				String types[] = dataTypes.split(",");
				String fieldName[] = javaRef.split(",");
				while (rs.next()) {
					try {
						T object = getInstance(classType);
						for (int i = 0; i < alias.length; i++) {
							if ("int".equalsIgnoreCase(types[i])) {
								if ("status".equals(fieldName[i])) {
									String statusMessage = ReservataionStatus.AppointmentStatusDescription.getAppointmentStatusDescription(rs.getInt(alias[i]));
									CoreUtils.setPropertyValue(object, fieldName[i], statusMessage);
								} else {
									CoreUtils.setPropertyValue(object, fieldName[i], rs.getInt(alias[i]));
								}
							} else if ("long".equalsIgnoreCase(types[i])) {
								CoreUtils.setPropertyValue(object, fieldName[i], rs.getLong(alias[i]));
							} else if ("varchar".equalsIgnoreCase(types[i]) || "char".equalsIgnoreCase(types[i])) {
								CoreUtils.setPropertyValue(object, fieldName[i], rs.getString(alias[i]));
							}
						}
						String key = CoreUtils.getPropertyValue(object, keyFieldName).toString();
						dataMap.put(key, object);
					} catch (Exception e) {
						throw new SQLException("Exception in executeQueryDynamicQuery.");
					}
				}
				return new Long(0);
			}
		});
	}

	private static <T> List<T> getArrayList(Class<T> type) throws InstantiationException {
		return new ArrayList<T>();
	}

	private static <T> T getInstance(Class<T> theClass) throws IllegalAccessException, InstantiationException {
		return theClass.newInstance();
	}

	@Override
	public long getEventDateTimeId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			String date, String time) {
		StringBuilder sql = new StringBuilder("select id from event_date_time where date=? and time=? and location_id=? and event_id =? and enable='Y'");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { date, time, locationId, eventId }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return rs.getLong("id");
				}
				return new Long(0);
			}
		});
	}

	@Override
	public void getDateList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			final DateListResponse dateListResponse) {
		StringBuilder sql = new StringBuilder();
		sql.append("select group_concat(distinct DATE_FORMAT(date,'%m/%d/%Y') order by date asc) as dateList");
		sql.append(" from event_date_time where location_id=? and event_id=? order by date asc");
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Integer.valueOf(locationId), Long.valueOf(eventId) }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					String dateListStr = rs.getString("dateList");
					List<String> dateList = Arrays.asList(dateListStr.split(","));
					dateListResponse.setDateList(dateList);
				}
				return new Long(0);
			}
		});
	}

	@Override
	public void getJSDateList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			final DateJSListResponse dateJsListResponse) {
		StringBuilder sql = new StringBuilder();
		sql.append("select DATE_FORMAT(edt.date,'%m/%d/%Y') as date, edt.num_seats as numSeats, edt.booked_seats as bookedSeats");
		sql.append(" from event_date_time edt where edt.date > DATE_ADD(CURRENT_DATE, INTERVAL -6 MONTH) and edt.date < DATE_ADD(CURRENT_DATE, INTERVAL 6 MONTH )");
		sql.append(" and location_id=:locationId and edt.enable='Y'");
		if (eventId != null && Long.valueOf(eventId) > 0) {
			sql.append(" and event_id=:eventId ");
		}
		sql.append(" order by edt.date asc");

		MapSqlParameterSource mpsql = new MapSqlParameterSource();
		mpsql.addValue("locationId", Integer.valueOf(locationId));
		if (eventId != null && Long.valueOf(eventId) > 0) {
			mpsql.addValue("eventId", Integer.valueOf(eventId));
		}
		final Map<String, String> map = dateJsListResponse.getDateMap();
		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), mpsql, new ResultSetExtractor<Long>() {

			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String value = "N";
				String dateStr = "";
				int i = 0;
				while (rs.next()) {
					dateStr = rs.getString("date");
					if (i == 0) {
						i++;
						dateJsListResponse.setMinDate(dateStr);
					}
					if (map.containsKey(dateStr)) {
						value = map.get(dateStr);
						if ("N".equals(value)) {
							continue;
						}
					}
					int numSeats = rs.getInt("numSeats");
					int bookedSeats = rs.getInt("bookedSeats");
					if ((numSeats - bookedSeats) == 0) {
						value = "Y";
					} else {
						value = "N";
					}
					map.put(dateStr, value);
				}
				dateJsListResponse.setMaxDate(dateStr);
				return new Long(0);
			}
		});
	}

	@Override
	public void getPrivileges(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String privilegeName,
			final HomePageResponse homePageResponse) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select group_concat(page_name) as pageName from privilege_page_mapping ppm, access_privilege ap where ppm.privilege_id=ap.id and ap.privilege=?");
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { privilegeName.trim() }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					String pageNameStr = rs.getString("pageName");
					if (pageNameStr != null) {
						List<String> pageNameList = Arrays.asList(pageNameStr.split(","));
						homePageResponse.setPrivilegeMapping(pageNameList);
					} else {
						homePageResponse.setPrivilegeMapping(new ArrayList<String>());
					}
				}
				return new Long(0);
			}
		});
	}

	@Override
	public void getDisplayNames(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final DisplayNames displayNames) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select location_name,locations_name,location_select,");
		sql.append("event_name,events_name,event_select,");
		sql.append("seat_name,seats_name,seat_select,");
		sql.append("customer_name,customers_name,customer_select ");
		sql.append(" from display_names where device=?");

		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { device }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					displayNames.setLocationName(rs.getString("location_name"));
					displayNames.setLocationsName(rs.getString("locations_name"));
					displayNames.setLocationSelect(rs.getString("location_select"));
					displayNames.setEventName(rs.getString("event_name"));
					displayNames.setEventsName(rs.getString("events_name"));
					displayNames.setEventSelect(rs.getString("event_select"));
					displayNames.setSeatName(rs.getString("seat_name"));
					displayNames.setSeatsName(rs.getString("seats_name"));
					displayNames.setSeatSelect(rs.getString("seat_select"));
					displayNames.setCustomerName(rs.getString("customer_name"));
					displayNames.setCustomersName(rs.getString("customers_name"));
					displayNames.setCustomerSelect(rs.getString("customer_select"));
				}
				return new Long(0);
			}
		});
	}

	@Override
	public void getSeatsTimeList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			String dateYYYYMMDD, final TimeListResponse timeListResponse) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT group_concat(DATE_FORMAT(time, '%l:%i %p')) timeList");
		sql.append(" from event_date_time where date=? and location_id=? and event_id=? order by time asc");
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { dateYYYYMMDD, Integer.valueOf(locationId), Long.valueOf(eventId) },
				new ResultSetExtractor<Long>() {
					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						rs.setFetchSize(1);
						if (rs.next()) {
							String timeListStr = rs.getString("timeList");
							List<String> timeList = Arrays.asList(timeListStr.split(","));
							timeListResponse.setTimeList(timeList);
						}
						return new Long(0);
					}
				});
	}

	@Override
	public void getEventDateTimeById(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, final String device, String eventDateTimeId,
			final EventDateTime eventDateTime) throws Exception {

		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue());

		StringBuilder sql = new StringBuilder("SELECT edt.id, edt.event_id,edt.location_id, e.event_name, l.location_name,");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time,");
		sql.append(" edt.enable,edt.num_seats");
		sql.append(" from event_date_time edt, location l, event e where edt.id =  ? and edt.location_id=l.id and edt.event_id=e.event_id");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.parseLong(eventDateTimeId) }, new ResultSetExtractor<EventDateTime>() {
				@Override
				public EventDateTime extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						eventDateTime.setEventDateTimeId(rs.getLong("id"));
						eventDateTime.setEventId(rs.getLong("event_id"));
						eventDateTime.setLocationId(rs.getInt("location_id"));
						eventDateTime.setEventName(rs.getString("event_name"));
						eventDateTime.setLocationName(rs.getString("location_name"));
						eventDateTime.setDate(rs.getString("date"));
						eventDateTime.setTime(rs.getString("time"));
						eventDateTime.setNoOfSeats("" + rs.getLong("num_seats"));
					}
					return eventDateTime;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_ADMIN_EVENT_DATE_TIME_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public void getCustomerNames(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String langCode, final String device, String customerName,
			final CustomerResponse customerResponse) throws Exception {
		// TODO: need to fix SQL injection - security issue here.
		try {
			customerName = (String) CoreUtils.getInitCaseValue(customerName);
			StringBuilder sql = new StringBuilder();
			sql.append("select cus.id, cus.first_name,cus.last_name,cus.account_number from customer cus where id > 0 ");
			if (!com.telappoint.resvdeskrestws.common.utils.StringUtils.isEmpty(customerName)) {
				sql.append(" and (cus.last_name LIKE '%" + customerName + "%' ");
				sql.append(" or cus.first_name like '%" + customerName + "%')");
			}

			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<CustomerResponse>() {
				@Override
				public CustomerResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Customer> customerList = new ArrayList<Customer>();
					Customer customer = null;
					while (rs.next()) {
						customer = new Customer();
						customer.setFirstName(rs.getString("first_name"));
						customer.setCustomerId(rs.getLong("id"));
						customer.setLastName(rs.getString("last_name"));
						customer.setAccountNumber(rs.getString("account_number"));
						customerList.add(customer);
					}
					customerResponse.setCustomerList(customerList);
					return customerResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_ADMIN_EVENT_DATE_TIME_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public boolean validateEventDateTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, EventDateTime eventDateTime) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id from event_date_time where date=? and time=? and event_id=? and location_id=?");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(),
				new Object[] { eventDateTime.getDate(), eventDateTime.getTime(), eventDateTime.getEventId(), eventDateTime.getLocationId() }, new ResultSetExtractor<Boolean>() {
					@Override
					public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							return false;
						}
						return true;
					}
				});
	}

	@Override
	public void getReservationReports(JdbcCustomTemplate jdbcCustomTemplate, DynamicSQL dynamicSQL, DynamicTableItems dynamicReportItems, String reportSQLFinal,
			ReservationReportRequest reservationReportRequest, ReservationReportResponse reservationReportResponse) throws Exception {
		CustomLogger customLogger = reservationReportRequest.getCustomLogger();
		final String keys[] = dynamicSQL.getDynamicKeyMapping().split(",");
		final int size = keys.length;
		final Object dynamicSQLInject[] = new Object[size];
		for (int i = 0; i < size; i++) {
			dynamicSQLInject[i] = CoreUtils.getPropertyValue(reservationReportRequest, keys[i]);
		}
		String aliasColumnNames = dynamicReportItems.getAliasName();
		String dataTypes = dynamicReportItems.getDynamicType();
		String javaRef = dynamicReportItems.getDynamicJavaReflection();
		String titles = dynamicReportItems.getDynamicTitle();
		String hides = dynamicReportItems.getDynamicHide();
		List<ReservationDetails> reservationDetails = new ArrayList<ReservationDetails>();
		Class<ReservationDetails> beanType = ReservationDetails.class;
		reservationDetails = getArrayList(beanType);
		customLogger.debug("reportSQLFinal:" + reportSQLFinal);
		executeQueryDynamicQuery(jdbcCustomTemplate, customLogger, reportSQLFinal, dynamicSQLInject, aliasColumnNames, dataTypes, javaRef, titles, reservationDetails, beanType);
		reservationReportResponse.setTitle(titles);
		reservationReportResponse.setJavaRef(javaRef);
		reservationReportResponse.setHides(hides);
		reservationReportResponse.setReportsList(reservationDetails);
	}

	@Override
	public void getInBoundCallReportList(JdbcCustomTemplate jdbcCustomTemplate, DynamicTableItems dynamicReportItems, String reportSQLFinal, MapSqlParameterSource parameterSource,
			InBoundCallReportRequest inBoundCallReportRequest, InBoundCallReportResponse inBoundCallReportResponse) throws Exception {
		CustomLogger customLogger = inBoundCallReportRequest.getCustomLogger();

		String aliasColumnNames = dynamicReportItems.getAliasName();
		String dataTypes = dynamicReportItems.getDynamicType();
		String javaRef = dynamicReportItems.getDynamicJavaReflection();
		String titles = dynamicReportItems.getDynamicTitle();
		String hides = dynamicReportItems.getDynamicHide();
		List<InBoundIvrCalls> inBoundIvrCalls = new ArrayList<InBoundIvrCalls>();
		Class<InBoundIvrCalls> beanType = InBoundIvrCalls.class;
		inBoundIvrCalls = getArrayList(beanType);
		customLogger.debug("reportSQLFinal:" + reportSQLFinal);

		executeDynamicQuery(jdbcCustomTemplate, customLogger, reportSQLFinal, parameterSource, aliasColumnNames, dataTypes, javaRef, titles, inBoundIvrCalls, beanType);
		inBoundCallReportResponse.setTitle(titles);
		inBoundCallReportResponse.setJavaRef(javaRef);
		inBoundCallReportResponse.setHides(hides);
		inBoundCallReportResponse.setIvrcallList(inBoundIvrCalls);
	}

	@Override
	public void getOutBoundCallReportList(JdbcCustomTemplate jdbcCustomTemplate, DynamicTableItems dynamicReportItems, String reportSQLFinal,
			MapSqlParameterSource parameterSource, OutBoundCallReportRequest outBoundCallReportRequest, OutBoundCallReportResponse outBoundCallReportResponse) throws Exception {
		CustomLogger customLogger = outBoundCallReportRequest.getCustomLogger();

		String aliasColumnNames = dynamicReportItems.getAliasName();
		String dataTypes = dynamicReportItems.getDynamicType();
		String javaRef = dynamicReportItems.getDynamicJavaReflection();
		String titles = dynamicReportItems.getDynamicTitle();
		String hides = dynamicReportItems.getDynamicHide();
		List<OutBoundIvrCalls> outBoundIvrCalls = new ArrayList<OutBoundIvrCalls>();
		Class<OutBoundIvrCalls> beanType = OutBoundIvrCalls.class;
		outBoundIvrCalls = getArrayList(beanType);
		customLogger.debug("reportSQLFinal:" + reportSQLFinal);

		executeDynamicQuery(jdbcCustomTemplate, customLogger, reportSQLFinal, parameterSource, aliasColumnNames, dataTypes, javaRef, titles, outBoundIvrCalls, beanType);
		outBoundCallReportResponse.setTitle(titles);
		outBoundCallReportResponse.setJavaRef(javaRef);
		outBoundCallReportResponse.setHides(hides);
		outBoundCallReportResponse.setIvrcallList(outBoundIvrCalls);
	}

	@Override
	public void getCalendarOverviewDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String eventDateTimeId,
			final CalendarOverviewDetailsResponse calOverviewDetailsResponse) throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("(select s.display_seat_number as displaySeatNumber, s.reserved,'Y' as booked, IF(s.reserved='Y', '', cu.account_number) as accNumber, concat(left(cu.contact_phone,3) , \'-\' , mid(cu.contact_phone,4,3) , \'-\', right(cu.contact_phone,4)) as contactPhone, cu.first_name as firstName,cu.last_name as lastName,'Booked' as availableMsg,s.placement");
		sql.append(" from seat s, schedule sc, customer cu where s.event_date_time_id=? and s.schedule_id = sc.id and sc.customer_id = cu.id and s.schedule_id>0)");
		sql.append(" union all ");
		sql.append("(select s.display_seat_number as displaySeatNumber, s.reserved, 'N' as booked, IF(s.reserved='Y', '', cu.account_number) as accNumber, concat(left(cu.contact_phone,3) , \'-\' , mid(cu.contact_phone,4,3) , \'-\', right(cu.contact_phone,4)) as contactPhone, IF(s.reserved = 'Y', '', cu.first_name) as firstName, IF(s.reserved = 'Y', '', cu.last_name) as lastName,IF(s.reserved = 'Y', 'Reserved', 'Open') as availableMsg,s.placement");
		sql.append(" from seat s, schedule sc, customer cu where s.event_date_time_id=? and s.schedule_id = sc.id and sc.customer_id = cu.id and s.schedule_id=0) ");
		sql.append(" order by placement asc");

		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(eventDateTimeId), Long.valueOf(eventDateTimeId) },
					new ResultSetExtractor<CalendarOverviewDetailsResponse>() {
						@Override
						public CalendarOverviewDetailsResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
							List<CalendarOverviewDetails> calendarOverViewDetailList = new ArrayList<CalendarOverviewDetails>();
							CalendarOverviewDetails calendarOverviewDetails = null;
							while (rs.next()) {
								calendarOverviewDetails = new CalendarOverviewDetails();
								calendarOverviewDetails.setDisplaySeatNumber(rs.getString("displaySeatNumber"));
								calendarOverviewDetails.setReserved(rs.getString("reserved"));
								calendarOverviewDetails.setBooked(rs.getString("booked"));
								calendarOverviewDetails.setFirstName(rs.getString("firstName"));
								calendarOverviewDetails.setLastName(rs.getString("lastName"));
								calendarOverviewDetails.setAvailableMsg(rs.getString("availableMsg"));
								calendarOverviewDetails.setAccountNumber(rs.getString("accNumber"));
								calendarOverviewDetails.setContactPhone(rs.getString("contactPhone"));
								calendarOverViewDetailList.add(calendarOverviewDetails);
							}
							calOverviewDetailsResponse.setCalendarOverviewDetailsList(calendarOverViewDetailList);
							return calOverviewDetailsResponse;
						}
					});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.CALENDAR_OVERVIEW_DETAILS.getValue(), e);
		}
		return;
	}

	@Override
	public void getCustomerById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String customerId,
			final CustomerResponse customerResponse) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,first_name, last_name, home_phone,email, concat(MID(dob,6,2) ,'/' , right(dob,2) , '/', left(dob,4)) as dob, concat(left(contact_phone,3) ,'-' , mid(contact_phone,4,3) , '-', right(contact_phone,4)) as contact_phone,account_number from customer where id=?");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(customerId) }, new ResultSetExtractor<CustomerResponse>() {
				@Override
				public CustomerResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Customer> customerList = new ArrayList<Customer>();
					Customer customer = null;
					if (rs.next()) {
						customer = new Customer();
						customer.setCustomerId(rs.getLong("id"));
						customer.setFirstName(rs.getString("first_name"));
						customer.setLastName(rs.getString("last_name"));
						customer.setHomePhone(rs.getString("home_phone"));
						customer.setAccountNumber(rs.getString("account_number"));
						customer.setEmail(rs.getString("email"));
						customer.setContactPhone(rs.getString("contact_phone"));
						customer.setDob(rs.getString("dob"));
						customerList.add(customer);
					}
					customerResponse.setCustomerList(customerList);
					return customerResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_CUSTOMER_ID.getValue(), e);
		}
		return;
	}

	@Override
	public void getFutureReservations(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String customerId,
			final FutureReservationResponse futureResvResponse) throws Exception {

		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue());

		StringBuilder sql = new StringBuilder();
		sql.append("select sc.id, res.conf_number,");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time,");
		sql.append("cu.home_phone as homePhone,s.seat_number as seatNumber,s.display_seat_number as displaySeatNumber,");
		sql.append("l.location_name as locationName, e.event_name as eventName, cu.first_name as firstName,cu.last_name as lastName, cu.email");
		sql.append(" from seat s,location l,event e,customer cu,event_date_time edt,schedule sc,reservation res where sc.customer_id=?");
		// AND edt.time > CURTIME()
		sql.append(" AND edt.date > CURDATE() ");
		sql.append(" and s.schedule_id = sc.id and s.event_date_time_id = edt.id and edt.location_id=l.id and sc.customer_id=cu.id");
		sql.append(" and e.event_id=edt.event_id and res.schedule_id=sc.id order by l.location_name, edt.date, edt.time limit 5");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(customerId) }, new ResultSetExtractor<FutureReservationResponse>() {
				@Override
				public FutureReservationResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<ReservationDetails> resvDetails = new ArrayList<ReservationDetails>();
					ReservationDetails reservationDetails = null;
					while (rs.next()) {
						reservationDetails = new ReservationDetails();
						reservationDetails.setScheduleId(rs.getLong("id"));
						reservationDetails.setConfNumber("" + rs.getLong("conf_number"));
						reservationDetails.setEventDateTime(rs.getString("date") + " " + rs.getString("time"));
						reservationDetails.setFirstName(rs.getString("firstName"));
						reservationDetails.setLastName(rs.getString("lastName"));
						reservationDetails.setEmail(rs.getString("email"));
						reservationDetails.setDisplaySeatNumber(rs.getString("displaySeatNumber"));
						// reservationDetails.setCompanyName(rs.getString("company_name_online"));
						// reservationDetails.setProcedureName(rs.getString("procedure_name_online"));
						reservationDetails.setLocationName(rs.getString("locationName"));
						// reservationDetails.setDepartmentName(rs.getString("department_name_online"));
						reservationDetails.setEventName(rs.getString("eventName"));
						resvDetails.add(reservationDetails);
					}
					futureResvResponse.setResvDetails(resvDetails);
					return futureResvResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.FUTURE_RESERVATION_DETAILS.getValue(), e);
		}
		return;

	}

	@Override
	public void getPastReservations(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String customerId,
			final PastReservationResponse pastResvResponse) throws Exception {

		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue());

		StringBuilder sql = new StringBuilder();
		sql.append("select sc.id, res.conf_number,");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time,");
		sql.append("cu.home_phone as homePhone,s.seat_number as seatNumber,s.display_seat_number as displaySeatNumber,");
		sql.append("l.location_name as locationName, e.event_name as eventName, cu.first_name as firstName,cu.last_name as lastName,cu.email");
		sql.append(" from seat s,location l,event e,customer cu,event_date_time edt,schedule sc,reservation res where sc.customer_id=?");
		sql.append(" AND edt.date < CURDATE() ");
		// AND edt.time < CURTIME()
		sql.append(" and s.schedule_id = sc.id and s.event_date_time_id = edt.id and edt.location_id=l.id and sc.customer_id=cu.id");
		sql.append(" and e.event_id=edt.event_id and res.schedule_id=sc.id order by l.location_name, edt.date, edt.time desc limit 5");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(customerId) }, new ResultSetExtractor<PastReservationResponse>() {
				@Override
				public PastReservationResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<ReservationDetails> resvDetails = new ArrayList<ReservationDetails>();
					ReservationDetails reservationDetails = null;
					while (rs.next()) {
						reservationDetails = new ReservationDetails();
						reservationDetails.setScheduleId(rs.getLong("id"));
						reservationDetails.setConfNumber("" + rs.getLong("conf_number"));
						reservationDetails.setEventDateTime(rs.getString("date") + " " + rs.getString("time"));
						reservationDetails.setFirstName(rs.getString("firstName"));
						reservationDetails.setLastName(rs.getString("lastName"));
						reservationDetails.setEmail(rs.getString("email"));
						reservationDetails.setDisplaySeatNumber(rs.getString("displaySeatNumber"));
						// reservationDetails.setCompanyName(rs.getString("company_name_online"));
						// reservationDetails.setProcedureName(rs.getString("procedure_name_online"));
						reservationDetails.setLocationName(rs.getString("locationName"));
						// reservationDetails.setDepartmentName(rs.getString("department_name_online"));
						reservationDetails.setEventName(rs.getString("eventName"));
						resvDetails.add(reservationDetails);
					}
					pastResvResponse.setResvDetails(resvDetails);
					return pastResvResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.PAST_RESERVATION_DETAILS.getValue(), e);
		}
		return;
	}

	@Override
	public void getReservationByScheduleId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String scheduleId,
			final ClientDeploymentConfig cdConfig, final ReservationDetailsResponse resvDetailsResponse) throws Exception {
		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_HHMMZZZZ.getValue());

		StringBuilder sql = new StringBuilder();
		sql.append("select r.conf_number,");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time,");
		sql.append("c.company_name_online,c.company_name_ivr_tts,c.company_name_ivr_audio,");
		sql.append("p.procedure_name_online,p.procedure_name_ivr_tts,p.procedure_name_ivr_audio,");
		sql.append("l.location_name,l.location_name_ivr_tts,l.location_name_ivr_audio,l.time_zone,");
		sql.append("d.department_name_online,d.department_name_ivr_tts,department_name_ivr_audio,");
		sql.append("e.event_name,e.duration, e.event_name_ivr_tts,e.event_name_ivr_audio,");
		sql.append("s.display_seat_number,s.tts,s.audio,");
		sql.append("cu.first_name,cu.last_name,cu.email");
		sql.append(" from schedule sc, company c, `procedure` p, location l,");
		sql.append(" department d, event e, seat s, event_date_time edt,customer cu, reservation r");
		sql.append(" where sc.id = ? ");
		sql.append(" and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id ");
		sql.append(" and sc.location_id=l.id and sc.department_id=d.id and sc.event_id =e.event_id ");
		sql.append(" and sc.event_date_time_id = edt.id and sc.seat_id=s.id");
		sql.append(" and sc.customer_id = cu.id and sc.id = r.schedule_id");
		customLogger.debug("prepareResvDetails sql:" + sql.toString());
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { scheduleId }, new ResultSetExtractor<ReservationDetailsResponse>() {
			@Override
			public ReservationDetailsResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
				ReservationDetails resvDetails = null;
				if (rs.next()) {
					resvDetails = new ReservationDetails();
					String date = rs.getString("date");
					String time = rs.getString("time");
					resvDetails.setEventDateTime(date + " " + time);
					resvDetails.setFirstName(rs.getString("first_name"));
					resvDetails.setLastName(rs.getString("last_name"));
					resvDetails.setDuration(rs.getInt("duration"));
					resvDetails.setEmail(rs.getString("email"));
					resvDetails.setConfNumber("" + rs.getLong("conf_number"));
					resvDetails.setDuration(rs.getInt("duration"));
					String timeZone = rs.getString("time_zone");
					timeZone = (timeZone == null) ? cdConfig.getTimeZone() : timeZone;
					resvDetails.setTimeZone(timeZone);
					resvDetails.setSeatNumber(rs.getString("display_seat_number"));
					resvDetails.setCompanyName(rs.getString("company_name_online"));
					resvDetails.setProcedureName(rs.getString("procedure_name_online"));
					resvDetails.setLocationName(rs.getString("location_name"));
					resvDetails.setDepartmentName(rs.getString("department_name_online"));
					resvDetails.setEventName(rs.getString("event_name"));
					resvDetailsResponse.setReservationDetails(resvDetails);
				}
				return resvDetailsResponse;
			}
		});
	}

	// not in use - requried to delete
	@Override
	public void getRegistrationParams(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, List<RegistrationField>> map) throws Exception {
		String sql = "select * from customer_registration lpc order by lpc.placement";
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Map<String, List<RegistrationField>>>() {
			@Override
			public Map<String, List<RegistrationField>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				RegistrationField resField = null;
				List<RegistrationField> regParamFields = null;
				StringBuilder key = new StringBuilder();
				while (rs.next()) {
					key.append(rs.getString("device_type"));

					resField = new RegistrationField();
					// Below three fields are keys, we have to get values from
					// display labels tables, That is doing in service layer.
					resField.setDisplayTitle(rs.getString("display_title"));
					resField.setEmptyErrorMessage(rs.getString("empty_error_msg"));
					resField.setInvalidErrorMessage(rs.getString("invalid_error_msg"));

					resField.setDisplayType(rs.getString("display_type"));

					resField.setFieldName(rs.getString("param_column"));
					resField.setJavaRef(rs.getString("java_reflection"));
					resField.setInitValue(rs.getString("list_initial_values"));
					resField.setIsMandatory(rs.getString("required").charAt(0));
					resField.setValidationRequired(rs.getString("validate_required"));
					resField.setLoginType(rs.getString("login_type"));
					// text box size
					resField.setDisplaySize(String.valueOf(rs.getInt("display_size")));
					resField.setMaxLength("" + rs.getInt("max_chars"));
					resField.setListInitValue(rs.getString("list_initial_values"));
					resField.setInitValue(rs.getString("initial_value"));

					resField.setValidateMinValue("" + rs.getInt("validate_min_value"));
					resField.setValidateMaxValue("" + rs.getInt("validate_max_value"));
					resField.setValidateMaxChars("" + rs.getInt("validate_max_chars"));
					resField.setValidateMinChars("" + rs.getInt("validate_min_chars"));
					resField.setValidateRules(rs.getString("validation_rules"));
					resField.setPlacement(rs.getInt("placement"));

					if (map.containsKey(key.toString())) {
						regParamFields = map.get(key.toString());
						regParamFields.add(resField);
					} else {
						regParamFields = new ArrayList<RegistrationField>();
						regParamFields.add(resField);
						map.put(key.toString(), regParamFields);
					}
					key.setLength(0);
				}
				return map;
			}
		});
		return;
	}

	@Override
	public void getReservationSearch(JdbcCustomTemplate jdbcCustomTemplate, DynamicTableItems dynamicTableItems, String searchSQLFinal, MapSqlParameterSource parameterSource,
			SearchRequestData resvSearchRequest, ReservationSearchResponse reservationSearchResponse) throws Exception {
		CustomLogger customLogger = resvSearchRequest.getCustomLogger();
		String aliasColumnNames = dynamicTableItems.getAliasName();
		String dataTypes = dynamicTableItems.getDynamicType();
		String javaRef = dynamicTableItems.getDynamicJavaReflection();
		String titles = dynamicTableItems.getDynamicTitle();
		String hides = dynamicTableItems.getDynamicHide();
		List<ReservationDetails> reservationDetails = new ArrayList<ReservationDetails>();
		Class<ReservationDetails> beanType = ReservationDetails.class;
		reservationDetails = getArrayList(beanType);
		customLogger.debug("searchSQLFinal:" + searchSQLFinal);
		executeDynamicQuery(jdbcCustomTemplate, customLogger, searchSQLFinal, parameterSource, aliasColumnNames, dataTypes, javaRef, titles, reservationDetails, beanType);
		reservationSearchResponse.setTitle(titles);
		reservationSearchResponse.setJavaRef(javaRef);
		reservationSearchResponse.setHides(hides);
		reservationSearchResponse.setReservationDetails(reservationDetails);

	}

	public void getEventsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String date, String locationId, String eventIds,
			final Map<String, CalendarOverview> map) throws Exception {
		StringBuilder sql = new StringBuilder(
				"select distinct DATE_FORMAT(edt1.date,'%m/%d/%Y') as date, DATE_FORMAT(edt1.time, '%l:%i %p') as time,edt1.num_seats, edt1.num_seats-");
		sql.append("(");
		sql.append("select count(*) from seat s2, event_date_time edt2");
		sql.append(" where edt2.id  = edt1.id and s2.event_date_time_id=edt2.id and s2.schedule_id=0 and reserved='N'");
		sql.append(") as bookedSeats, edt1.id as eventDateTimeId");
		sql.append(" from event_date_time edt1,seat s1, location l, event e where edt1.date=:date and edt1.location_id=:locId and edt1.event_id in (:eventIds) and edt1.id = s1.event_date_time_id");
		sql.append(" and  edt1.event_id = e.event_id and edt1.location_id = l.id and edt1.enable='Y' order by edt1.id");
		MapSqlParameterSource mapSQLParam = new MapSqlParameterSource();
		mapSQLParam.addValue("date", date);
		mapSQLParam.addValue("locId", Integer.valueOf(locationId));
		List<Integer> eventIdsInt = getListFromCommaSeperatedString(eventIds);
		mapSQLParam.addValue("eventIds", eventIdsInt);

		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), mapSQLParam, new ResultSetExtractor<Map<String, CalendarOverview>>() {
			@Override
			public Map<String, CalendarOverview> extractData(ResultSet rs) throws SQLException, DataAccessException {
				CalendarOverview calendarOverview = null;
				StringBuilder key = new StringBuilder();
				while (rs.next()) {
					key.append(rs.getLong("eventDateTimeId"));
					calendarOverview = new CalendarOverview();
					calendarOverview.setDate(rs.getString("date"));
					calendarOverview.setTime(rs.getString("time"));
					calendarOverview.setBookedSeats("" + rs.getLong("bookedSeats"));
					calendarOverview.setTotalSeats("" + rs.getLong("num_seats"));
					map.put(key.toString(), calendarOverview);
					key.setLength(0);
				}
				return map;
			}
		});
		return;
	}

	public void getTablePrintView(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId,
			String dateYYYYMMDD, final DynamicTableItems dynamicTableItems, final String finalSQL, final TablePrintViewResponse tablePrintViewReponse) throws Exception {

		final Map<String, CalendarOverview> map = new HashMap<String, CalendarOverview>();
		getEventsMap(jdbcCustomTemplate, customLogger, dateYYYYMMDD, locationId, eventId, map);
		Set<String> eventDateTimeIdsSet = map.keySet();
		String eventDateTimeIdStr = StringUtils.concatWithSeparator(eventDateTimeIdsSet, ",");
		List<Integer> eventDateTimeIdList = getListFromCommaSeperatedString(eventDateTimeIdStr);
		MapSqlParameterSource paramSourceByEventDateTimeIds = new MapSqlParameterSource();
		paramSourceByEventDateTimeIds.addValue("ids", eventDateTimeIdList);
		String scheduleIds = getScheduleIdListByEventIds(jdbcCustomTemplate, customLogger, paramSourceByEventDateTimeIds);

		// all booked reservations and customer details loaded - start
		MapSqlParameterSource paramSourceByScheduleIds = new MapSqlParameterSource();
		if (scheduleIds == null || "".equals(scheduleIds)) {
			paramSourceByScheduleIds.addValue("ids", -3);
		} else {
			List<Integer> scheduleIdList = getListFromCommaSeperatedString(scheduleIds);
			paramSourceByScheduleIds.addValue("ids", scheduleIdList);
		}

		final Class<ReservationDetails> beanType = ReservationDetails.class;
		final Map<Object, Object> reservedMap = new HashMap<Object, Object>();
		final String keyString = "scheduleId";
		String aliasColumnNames = dynamicTableItems.getAliasName();
		String dataTypes = dynamicTableItems.getDynamicType();
		String javaRef = dynamicTableItems.getDynamicJavaReflection();
		String titles = dynamicTableItems.getDynamicTitle();
		String hides = dynamicTableItems.getDynamicHide();
		tablePrintViewReponse.setJavaRefs(javaRef);
		tablePrintViewReponse.setTitles(titles);
		tablePrintViewReponse.setHides(hides);
		customLogger.debug("finalSQL:" + finalSQL);
		populateQueryMap(jdbcCustomTemplate, customLogger, finalSQL, paramSourceByScheduleIds, aliasColumnNames, dataTypes, javaRef, titles, reservedMap, beanType, keyString);
		// all booked reservations and customer details loaded - end

		// fetch all seats details - even available seats to show in table print
		// view page.
		StringBuilder sql = new StringBuilder("select s1.id as seatId,s1.display_seat_number, s1.schedule_id, s1.event_date_time_id,edt1.location_id, edt1.event_Id, s1.reserved ");
		sql.append(" from event_date_time edt1,seat s1, location l, event e where edt1.id in (:ids) and s1.event_date_time_id = edt1.id");
		sql.append(" and  edt1.event_id = e.event_id and edt1.location_id = l.id and edt1.enable='Y' order by s1.placement,edt1.date,edt1.time");
		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), paramSourceByEventDateTimeIds, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				String displaySeatNumber = null;
				Long scheduleId = (long) 0;
				Long eventDateTimeId = null;
				String reserved = "N";
				Long seatId = null;
				TablePrintView tablePrintView = null;
				SeatView seatView = null;
				CalendarOverview calendarOverview = null;
				Map<TablePrintView, List<SeatView>> tablePrintViewMap = new LinkedHashMap<TablePrintView, List<SeatView>>();
				List<SeatView> seatViewList = null;
				while (rs.next()) {
					seatView = new SeatView();
					displaySeatNumber = rs.getString("display_seat_number");
					scheduleId = rs.getLong("schedule_id");
					eventDateTimeId = rs.getLong("event_date_time_id");
					reserved = rs.getString("reserved");
					seatId = rs.getLong("seatId");

					seatView.setBooked((scheduleId > 0) ? "Y" : "N");
					seatView.setReserved(reserved);
					seatView.setSeatId("" + seatId);
					seatView.setSeatNumber(displaySeatNumber);

					if (scheduleId.longValue() > 0) {
						seatView.setReservationDetails((ReservationDetails) reservedMap.get("" + scheduleId));
					}

					tablePrintView = new TablePrintView();
					tablePrintView.setEventDateTimeId("" + eventDateTimeId);

					if (tablePrintViewMap.containsKey(tablePrintView)) {
						seatViewList = tablePrintViewMap.get(tablePrintView);
					} else {
						seatViewList = new ArrayList<SeatView>();
						calendarOverview = map.get("" + eventDateTimeId);
						tablePrintView.setBookedSeats(calendarOverview.getBookedSeats());
						tablePrintView.setTotalSeats(calendarOverview.getTotalSeats());
						tablePrintView.setTime(calendarOverview.getTime());
						tablePrintView.setDate(calendarOverview.getDate());
						tablePrintViewMap.put(tablePrintView, seatViewList);
					}
					seatViewList.add(seatView);
				}
				tablePrintViewReponse.setTablePrintView(tablePrintViewMap);
				return new Long("0");
			}
		});
		return;
	}

	@Override
	public void getDynamicSearchFields(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String searchCategory,
			final boolean isAllFields, final SearchFieldsResponse searchFieldsResponse) {
		StringBuilder sql = new StringBuilder();
		sql.append("select group_concat(title order by placement) as titles, group_concat(field_type order by placement) as fieldTypes,");
		sql.append("group_concat(java_type order by placement) as javaTypes, group_concat(java_reflection order by placement) as javaRef,");
		sql.append("group_concat(search_type order by placement) as searchTypes,");
		sql.append("group_concat(table_column order by placement) as tableColumns,");
		sql.append("group_concat(select_alias order by placement) as selectAlias");
		sql.append(" from dynamic_search");
		sql.append(" where category=? order by placement");

		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { searchCategory }, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					searchFieldsResponse.setFieldTypes(rs.getString("fieldTypes"));
					searchFieldsResponse.setJavaRef(rs.getString("javaRef"));
					searchFieldsResponse.setTitles(rs.getString("titles"));
					if (isAllFields) {
						searchFieldsResponse.setJavaType(rs.getString("javaTypes"));
						searchFieldsResponse.setSearchType(rs.getString("searchTypes"));
						searchFieldsResponse.setTableColumn(rs.getString("tableColumns"));
						searchFieldsResponse.setAlias(rs.getString("selectAlias"));
					}
				}
				return new Long(0);
			}
		});
	}

	@Override
	public void getClientDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, DynamicTableItems dynamicTableItems, String searchSQLFinal,
			MapSqlParameterSource mapSqlParameterSource, SearchRequestData searchRequestData, ClientDetailsResponse clientDetailsResponse) throws Exception {

		String aliasColumnNames = dynamicTableItems.getAliasName();
		String dataTypes = dynamicTableItems.getDynamicType();
		String javaRef = dynamicTableItems.getDynamicJavaReflection();
		String titles = dynamicTableItems.getDynamicTitle();
		String hides = dynamicTableItems.getDynamicHide();
		List<Customer> customerList = new ArrayList<Customer>();
		Class<Customer> beanType = Customer.class;
		customerList = getArrayList(beanType);
		customLogger.debug("searchSQLFinal:" + searchSQLFinal);

		executeDynamicQuery(jdbcCustomTemplate, customLogger, searchSQLFinal, mapSqlParameterSource, aliasColumnNames, dataTypes, javaRef, titles, customerList, beanType);
		clientDetailsResponse.setCustomerList(customerList);
		clientDetailsResponse.setTitles(titles);
		clientDetailsResponse.setJavaRefs(javaRef);
		clientDetailsResponse.setHides(hides);
	}

	@Override
	public void getAccessesPrivilages(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final PrivilageResponse prePrivilageResponse)
			throws Exception {
		StringBuilder sql = new StringBuilder("select id,privilege");
		sql.append(" from access_privilege");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<PrivilageResponse>() {
				@Override
				public PrivilageResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<PrivilageData> privList = new ArrayList<PrivilageData>();
					PrivilageData priv = null;
					while (rs.next()) {
						priv = new PrivilageData();
						priv.setPrivilageId(rs.getLong("id"));
						priv.setPrivilageName(rs.getString("privilege"));
						privList.add(priv);
					}
					prePrivilageResponse.setPrivilageList(privList);
					return prePrivilageResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.PRIVILAGE_DETAILS.getValue(), e);
		}
		return;
	}

	@Override
	public void getLocationById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, final boolean isFullData,
			final LocationListResponse locationListResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select id,location_name,location_name_ivr_tts,location_name_ivr_audio,address,city,state,zip,work_phone,time_zone, enable");
		sql.append(" from location");
		sql.append(" where id=?");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Integer.valueOf(locationId) }, new ResultSetExtractor<LocationListResponse>() {
				@Override
				public LocationListResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Location> locationList = new ArrayList<Location>();
					Location location = null;
					while (rs.next()) {
						location = new Location();
						location.setLocationId(rs.getInt("id"));
						location.setLocationName(rs.getString("location_name"));
						location.setTimeZone(rs.getString("time_zone"));
						if (isFullData) {
							location.setAddress(rs.getString("address"));
							location.setLocationNameIvrTts(rs.getString("location_name_ivr_tts"));
							location.setLocationNameIvrAudio(rs.getString("location_name_ivr_audio"));
							location.setWorkPhone(rs.getString("work_phone"));
							location.setCity(rs.getString("city"));
							location.setState(rs.getString("state"));
							location.setZip(rs.getString("zip"));
							location.setEnable(rs.getString("enable"));
						} else {
							location.setEnable(null);
						}
						locationList.add(location);
					}
					locationListResponse.setLocationList(locationList);
					return locationListResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_LOCATION_BY_ID.getValue(), e);
		}
		return;
	}

	@Override
	public void getReservationReminders(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String campaignId,
			String dateYYYYMMDD, DynamicTableItems dynamicTableItems, String finalSQL, MapSqlParameterSource parameterSource, ReservationRemindersResponse resvReminder)
			throws Exception {
		String aliasColumnNames = dynamicTableItems.getAliasName();
		String dataTypes = dynamicTableItems.getDynamicType();
		String javaRef = dynamicTableItems.getDynamicJavaReflection();
		String titles = dynamicTableItems.getDynamicTitle();
		customLogger.debug("finalSQL:" + finalSQL);

		executeDynamicQueryForNotifications(jdbcCustomTemplate, customLogger, finalSQL, parameterSource, aliasColumnNames, dataTypes, javaRef, titles, resvReminder);
		resvReminder.setRemindStatusNeedAppt(getRemindStatusNeedAppt(jdbcCustomTemplate));
	}

	public void getNotificationsById(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, Long notifyId,
			final NotifyDailyView notification) throws Exception {
		StringBuilder sql = new StringBuilder("select id,notify_by_phone, notify_by_email, notify_by_sms, home_phone");
		sql.append(" from notify");
		sql.append(" where id=?");

		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { notifyId }, new ResultSetExtractor<NotifyDailyView>() {
			@Override
			public NotifyDailyView extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					notification.setNotifyByPhone(rs.getString("notify_by_phone"));
					notification.setNotifyByEmail(rs.getString("notify_by_email"));
					notification.setNotifyBySMS(rs.getString("notify_by_sms"));
					notification.setHomePhone(rs.getString("home_phone"));
					return notification;
				}
				return notification;
			}
		});

	}

	public String getRemindStatusNeedAppt(JdbcCustomTemplate jdbcCustomTemplate) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT group_concat(distinct notify_phone_status) as notifyPhoneStatus from remind_status where need_appt='Y'");
		return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<String>() {
			String notifyPhoneStatus = "";

			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.setFetchSize(1);
				if (rs.next()) {
					notifyPhoneStatus = rs.getString("notifyPhoneStatus");
				}
				return notifyPhoneStatus;
			}
		});
	}

	@Override
	public void getReservationStatusList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, final boolean isFullData,
			final ReservationStatusResponse resvStatusResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select id,status, status_val");
		sql.append(" from reservationstatus where id > 0 and delete_flag='N'");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<ReservationStatusResponse>() {
				List<com.telappoint.resvdeskrestws.admin.model.ReservationStatus> reservationStatusList = new ArrayList<com.telappoint.resvdeskrestws.admin.model.ReservationStatus>();

				@Override
				public ReservationStatusResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					com.telappoint.resvdeskrestws.admin.model.ReservationStatus reservataionStatus = null;
					while (rs.next()) {
						reservataionStatus = new com.telappoint.resvdeskrestws.admin.model.ReservationStatus();
						reservataionStatus.setResvId(rs.getInt("id"));
						reservataionStatus.setResvStatus(rs.getString("status"));
						reservataionStatus.setStatusVal(rs.getInt("status_val"));
						if (isFullData) {

						} else {

						}
						reservationStatusList.add(reservataionStatus);
					}
					resvStatusResponse.setResvStatusList(reservationStatusList);
					return null;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_RESERVATION_STATUS_LIST.getValue(), e);
		}
		return;

	}

	@Override
	public void getNotifyRemainderStatusList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, boolean isFullData,
			final ReminderStatusResponse reminderStatusResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select id,status_key,display_status,notify_status,notify_phone_status,notify_sms_status, notify_email_status");
		sql.append(" from remind_status where id > 0 and delete_flag='N'");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<ReminderStatusResponse>() {
				List<com.telappoint.resvdeskrestws.admin.model.ReminderStatus> reminderStatusList = new ArrayList<com.telappoint.resvdeskrestws.admin.model.ReminderStatus>();

				@Override
				public ReminderStatusResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					com.telappoint.resvdeskrestws.admin.model.ReminderStatus reminderStatus = null;
					while (rs.next()) {
						reminderStatus = new com.telappoint.resvdeskrestws.admin.model.ReminderStatus();
						reminderStatus.setReminderStatusId(rs.getLong("id"));
						reminderStatus.setStatusKey(rs.getString("status_key"));
						reminderStatus.setDisplayStatus(rs.getString("display_status"));
						reminderStatus.setNotifyStatus(rs.getInt("notify_status"));
						reminderStatus.setNotifyPhoneStatus(rs.getInt("notify_phone_status"));
						reminderStatusList.add(reminderStatus);
					}
					reminderStatusResponse.setReminderStatusList(reminderStatusList);
					return null;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_REMINDER_STATUS_LIST.getValue(), e);
		}
		return;

	}

	public void getNotifyReminderStatusByPhoneStatusId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device,
			final NotifyRequest notifyRequest) throws Exception {
		StringBuilder sql = new StringBuilder("select notify_status,notify_email_status,notify_phone_status,notify_sms_status");
		sql.append(" from remind_status where id = ? and delete_flag='N'");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { notifyRequest.getNotifyReminderStatusId() }, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						notifyRequest.setNotifyStatus(rs.getInt("notify_status"));
						notifyRequest.setNotifyEmailStatus(rs.getInt("notify_email_status"));
						notifyRequest.setNotifyPhoneStatus(rs.getInt("notify_phone_status"));
						notifyRequest.setNotifySMSStatus(rs.getInt("notify_sms_status"));
					}
					return 0;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_REMINDER_STATUS_LIST.getValue(), e);
		}
	}

	@Override
	public void updateNotifyStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, NotifyRequest notifyRequest, BaseResponse baseResponse) throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = dsTransactionManager.getTransaction(def);
		try {
			NotifyDailyView notification = new NotifyDailyView();
			long notifyId = notifyRequest.getNotifyId();
			getNotificationsById(jdbcCustomTemplate, customLogger, notifyRequest.getLangCode(), notifyRequest.getDevice(), notifyId, notification);
			getNotifyReminderStatusByPhoneStatusId(jdbcCustomTemplate, customLogger, notifyRequest.getLangCode(), notifyRequest.getDevice(), notifyRequest);
			final StringBuilder sql = new StringBuilder();
			MapSqlParameterSource mapParam = new MapSqlParameterSource();
			sql.append("update notify set notify_status = :notifyStatus, comment=:comment, notes=:notes");
			mapParam.addValue("notifyStatus", notifyRequest.getNotifyStatus());
			mapParam.addValue("comment", "Updated by " + ((notifyRequest.getUserName() == null) ? "" : notifyRequest.getUserName()));
			mapParam.addValue("notes", notifyRequest.getNotes() == null ? "" : notifyRequest.getNotes());
			if ("Y".equals(notification.getNotifyByPhone())) {
				sql.append(", notify_phone_status=:notifyPhoneStatus");
				if ("RESET".equals(notifyRequest.getAction())) {
					mapParam.addValue("notifyPhoneStatus", 0);
				} else {
					mapParam.addValue("notifyPhoneStatus", notifyRequest.getNotifyPhoneStatus());
					Integer attemptId = getMaxAttemptId(jdbcCustomTemplate, customLogger, notifyId);
					notifyRequest.setAttemptId(attemptId);
					notifyRequest.setStartTime(new Date());
					notifyRequest.setPhoneNumber(notification.getHomePhone());
					insertPhoneStatusRecord(jdbcCustomTemplate, customLogger, notifyRequest);
				}
			} else if ("Y".equals(notification.getNotifyByEmail())) {
				sql.append(", notify_sms_status=:notifySMSStatus");
				if ("RESET".equals(notifyRequest.getAction())) {
					mapParam.addValue("notifySMSStatus", 0);
				} else {
					mapParam.addValue("notifySMSStatus", notifyRequest.getNotifySMSStatus());
					insertNotifySMSStatus(jdbcCustomTemplate, customLogger, notifyId, notifyRequest.getNotifySMSStatus(), 1);
				}
			} else if ("Y".equals(notification.getNotifyBySMS())) {
				sql.append(", notify_email_status=:notifyEmailStatus");
				if ("RESET".equals(notifyRequest.getAction())) {
					mapParam.addValue("notifyEmailStatus", 0);
				} else {
					mapParam.addValue("notifyEmailStatus", notifyRequest.getNotifyEmailStatus());
					insertNotifyEmailStatus(jdbcCustomTemplate, customLogger, notifyId, notifyRequest.getNotifyEmailStatus(), 1);
				}
			}

			if ("DNN".equals(status)) {
				sql.append(", do_not_notify=:doNotNotify");
				mapParam.addValue("doNotNotify", "Y");
			}
			sql.append(" where id=:notifyId");
			mapParam.addValue("notifyId", notifyId);
			jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), mapParam);
			sql.setLength(0);
			dsTransactionManager.commit(status);
			baseResponse.setResponseStatus(true);
		} catch (Exception e) {
			dsTransactionManager.rollback(status);
			throw new TelAppointDBException(AdminFlowStateConstants.UPDATE_REMINDER_STATUS.getValue(), e);
		}
	}

	@Override
	public boolean changeSchedulerStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String status) throws Exception {
		DataSourceTransactionManager dsTransactionManager = jdbcCustomTemplate.getDataSourceTransactionManager();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus tStatus = dsTransactionManager.getTransaction(def);
		try {
			final StringBuilder sql = new StringBuilder();
			sql.append("update resv_sys_config set scheduler_closed = ?");
			Object obj[] = new Object[1];
			if ("open".equalsIgnoreCase(status)) {
				obj[0] = "N";
			} else {
				obj[0] = "Y";
			}
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), obj);
			sql.setLength(0);
			dsTransactionManager.commit(tStatus);
			return true;
		} catch (Exception e) {
			dsTransactionManager.rollback(tStatus);
		}
		return false;
	}

	@Override
	public void getTransStates(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String transId,
			final TransStateResponse transStateResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select id,trans_id,timestamp,state");
		sql.append(" from trans_state where trans_id = ?");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(transId) }, new ResultSetExtractor<TransStateResponse>() {
				List<TransState> transStateList = new ArrayList<TransState>();

				@Override
				public TransStateResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					TransState transState = null;
					while (rs.next()) {
						transState = new TransState();
						transState.setTransStateId(rs.getLong("id"));
						transState.setTransId(rs.getLong("trans_id"));
						transState.setTimeStamp(rs.getString("timestamp"));
						transState.setStateValue(rs.getInt("state"));

						transStateList.add(transState);
					}
					transStateResponse.setTransStateList(transStateList);
					return null;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(AdminFlowStateConstants.GET_TRANS_STATS_LIST.getValue(), e);
		}
		return;
	}

	@Override
	public void updateScreenedStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId, String screened, BaseResponse baseResponse)
			throws Exception {
		String sql = "Update  schedule set screened=? where id= ?";
		Object[] columns = new Object[] { screened, Long.valueOf(scheduleId) };
		jdbcCustomTemplate.getJdbcTemplate().update(sql, columns);
		baseResponse.setResponseStatus(true);
	}

	@Override
	public void getEventDateTimeForLocEventDateRange(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ReservationReportRequest resvReportRequest,
			final ReservationReportCheckboxResponse resvReportCheckboxResonse) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id, concat(DATE_FORMAT(edt.date,'%m/%d/%Y'),' ',DATE_FORMAT(edt.time, '%l:%i %p')) as dateTime from event_date_time edt where 1=1");
		sql.append(" and edt.date >=:startDate and edt.date <=:endDate ");
		MapSqlParameterSource mpSQL = new MapSqlParameterSource();
		mpSQL.addValue("startDate", resvReportRequest.getStartDate());
		mpSQL.addValue("endDate", resvReportRequest.getEndDate());
		if (resvReportRequest.getLocationId() > 0) {
			sql.append(" and edt.location_id=:locationId ");
			mpSQL.addValue("locationId", Long.valueOf(resvReportRequest.getLocationId()));
		}

		if (resvReportRequest.getEventId() > 0) {
			sql.append(" and edt.event_id=:eventId ");
			mpSQL.addValue("eventId", Long.valueOf(resvReportRequest.getEventId()));
		}
		sql.append("order by edt.date,edt.time");

		jdbcCustomTemplate.getNameParameterJdbcTemplate().query(sql.toString(), mpSQL, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<ReportDateTimeCheckBox> repDateTimeCBList = new ArrayList<ReportDateTimeCheckBox>();
				ReportDateTimeCheckBox reportDateTimeCheckBox = null;
				while (rs.next()) {
					reportDateTimeCheckBox = new ReportDateTimeCheckBox();
					reportDateTimeCheckBox.setDateTime(rs.getString("dateTime"));
					reportDateTimeCheckBox.setEventDateTimeId(rs.getLong("id"));
					repDateTimeCBList.add(reportDateTimeCheckBox);
				}
				resvReportCheckboxResonse.setReportDateTimeCheckBoxList(repDateTimeCBList);
				return new Long(0);
			}
		});
	}
	
	@Override
	public Long getSeatScheduleId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String seatId) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select schedule_id from seat where id=?");
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] {Long.valueOf(seatId)},
					new ResultSetExtractor<Long>() {
						@Override
						public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								return rs.getLong("schedule_id");
							}
							return -1L;
						}
					});
		} catch (Exception e) {
			throw new Exception("Error while fetching the seat data!");
		}
	}
	
	@Override
	public void updateSeatReservedStatus(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String seatId, String reserved, BaseResponse baseResponse)throws Exception {
		String sql = "Update seat set reserved=? where id= ?";
		Object[] columns = new Object[] {reserved,Long.valueOf(seatId)};
		jdbcCustomTemplate.getJdbcTemplate().update(sql, columns);
		baseResponse.setResponseStatus(true);
	}
}

package com.telappoint.resvdeskrestws.common.clientdb.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.model.Schedule;
import com.telappoint.resvdeskrestws.common.clientdb.dao.ClientDAO;
import com.telappoint.resvdeskrestws.common.constants.CommonDateContants;
import com.telappoint.resvdeskrestws.common.constants.CommonResvDeskConstants;
import com.telappoint.resvdeskrestws.common.constants.FlowStateConstants;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;
import com.telappoint.resvdeskrestws.common.constants.ReservataionStatus;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ConfPageContactDetailsResponse;
import com.telappoint.resvdeskrestws.common.model.ConfirmResvResponse;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.model.EventHistory;
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
import com.telappoint.resvdeskrestws.common.utils.CoreUtils;
import com.telappoint.resvdeskrestws.handlers.exception.TelAppointDBException;

/**
 * 
 * @author Balaji N
 *
 */
@Repository
public class ClientDAOImpl extends AbstractDAOImpl implements ClientDAO {
	@Override
	public void loadCustomerByPhoneNumber(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String phone, final Customer customer) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from customer customer where customer.delete_flag = 'N' and customer.contact_phone=");
		sql.append("'").append(phone).append("'");
		customLogger.debug("loadCustomerByPhoneNumber SQL: " + sql.toString());
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Customer>() {
			@Override
			public Customer extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					customer.setCustomerId(rs.getLong("id"));
					customer.setFirstName(rs.getString("first_name"));
					customer.setLastName(rs.getString("last_name"));
					return customer;
				}
				return customer;
			}
		});
	}

	@Override
	public List<Options> getCompanyList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, final List<Options> companyOptions) throws Exception {
		StringBuilder sql = new StringBuilder("select id,company_name_online,company_name_ivr_tts,company_name_ivr_audio");
		sql.append(" from company ");
		sql.append("admin".equals(device) ? "" : " where id>0 and delete_flag = 'N'");
		sql.append(" order by placement asc");
		customLogger.debug("getCompanyList SQL: " + sql.toString());
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Options>>() {
				@Override
				public List<Options> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Options e = null;
					while (rs.next()) {
						e = new Options();
						e.setOptionKey("" + rs.getInt("id"));
						if (CoreUtils.isOnline(device)) {
							e.setOptionValue(rs.getString("company_name_online"));
						} else if (CoreUtils.isIVR(device)) {
							e.setOptionTTS(rs.getString("company_name_ivr_tts") == null ? "" : rs.getString("company_name_ivr_tts"));
							e.setOptionAudio(rs.getString("company_name_ivr_audio") == null ? "" : rs.getString("company_name_ivr_audio"));
						}
						companyOptions.add(e);
					}
					return companyOptions;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_COMPANY_LIST.getValue(), e);
		}
	}

	@Override
	public List<Options> getProcedureList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String companyId, final String device,
			final List<Options> procedureOptions) throws Exception {
		StringBuilder sql = new StringBuilder("select p.id,p.procedure_name_online,p.procedure_name_ivr_tts,p.procedure_name_ivr_audio");
		sql.append(" from `procedure` p, company_procedure cp where p.id>0 and ");
		sql.append("p.id  = cp.procedure_id and cp.company_id =");
		sql.append(companyId);
		sql.append("admin".equals(device) ? " " : " and delete_flag = 'N' and enable='Y'");
		sql.append(" order by placement asc");
		customLogger.debug("getProcedureList SQL: " + sql.toString());
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Options>>() {
				@Override
				public List<Options> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Options e = null;
					while (rs.next()) {
						e = new Options();
						e.setOptionKey("" + rs.getInt("id"));
						if (CoreUtils.isOnline(device)) {
							e.setOptionValue(rs.getString("procedure_name_online"));
						} else if (CoreUtils.isIVR(device)) {
							e.setOptionTTS(rs.getString("procedure_name_ivr_tts") == null ? "" : rs.getString("procedure_name_ivr_tts"));
							e.setOptionAudio(rs.getString("procedure_name_ivr_audio") == null ? "" : rs.getString("procedure_name_ivr_audio"));
						}
						procedureOptions.add(e);
					}
					return procedureOptions;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_PROCEDURE_LIST.getValue(), e);
		}
	}

	@Override
	public List<Options> getLocationList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, Long procedureId,
			final List<Options> locationOptions) throws Exception {
		StringBuilder sql = new StringBuilder(
				"select id,location_name,location_name_ivr_tts,location_name_ivr_audio from location where id in (select pl.location_id from procedure_location pl where procedure_id =");
		sql.append(procedureId);
		sql.append(" and enable='Y' ) and delete_flag = 'N' and enable='Y'");
		customLogger.debug("getLocationList SQL: " + sql.toString());
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Options>>() {
				@Override
				public List<Options> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Options e = null;
					while (rs.next()) {
						e = new Options();
						e.setOptionKey("" + rs.getInt("id"));
						if (CoreUtils.isOnline(device)) {
							e.setOptionValue(rs.getString("location_name"));
						} else if (CoreUtils.isIVR(device)) {
							e.setOptionTTS(rs.getString("location_name_ivr_tts") == null ? "" : rs.getString("location_name_ivr_tts"));
							e.setOptionAudio(rs.getString("location_name_ivr_audio") == null ? "" : rs.getString("location_name_ivr_audio"));
						}
						locationOptions.add(e);
					}
					return locationOptions;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_LOCATION_LIST.getValue(), e);
		}
	}

	@Override
	public List<Options> getDepartmentList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, Integer locationId,
			final List<Options> departmentOptions) {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,department_name_online,department_name_ivr_tts,department_name_ivr_audio");
		sql.append(" from department where id in (select lde.department_id from location_department_event lde where lde.location_id=");
		sql.append(locationId);
		sql.append(" and enable='Y') and id > 0 and delete_flag='N'");
		customLogger.debug("getDepartmentList SQL: " + sql.toString());
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Options>>() {
				@Override
				public List<Options> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Options e = null;
					while (rs.next()) {
						e = new Options();
						e.setOptionKey("" + rs.getInt("id"));
						if (CoreUtils.isOnline(device)) {
							e.setOptionValue(rs.getString("department_name_online"));
						} else if (CoreUtils.isIVR(device)) {
							e.setOptionTTS(rs.getString("department_name_ivr_tts") == null ? "" : rs.getString("department_name_ivr_tts"));
							e.setOptionAudio(rs.getString("department_name_ivr_audio") == null ? "" : rs.getString("department_name_ivr_audio"));
						}
						departmentOptions.add(e);
					}
					return departmentOptions;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_DEPARTMENT_LIST.getValue(), e);
		}
	}

	@Override
	public List<Options> getEventList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, Integer departmentId, Integer locationId, final List<Options> eventOptions)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select event_id,event_name,event_name_ivr_tts,event_name_ivr_audio from event where event_id in (select lde.event_id from location_department_event lde");
		sql.append(" where lde.department_id=");
		sql.append(departmentId);
		sql.append(" and lde.location_id=");
		sql.append(locationId);
		sql.append(" and enable='Y') and delete_flag='N' and enable='Y'");
		customLogger.debug("getEventList SQL: " + sql.toString());
		try {
			return jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<Options>>() {
				@Override
				public List<Options> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Options e = null;
					while (rs.next()) {
						e = new Options();
						e.setOptionKey("" + rs.getInt("event_id"));
						if (CoreUtils.isOnline(device)) {
							e.setOptionValue(rs.getString("event_name"));
						} else if (CoreUtils.isIVR(device)) {
							e.setOptionTTS(rs.getString("event_name_ivr_tts") == null ? "" : rs.getString("event_name_ivr_tts"));
							e.setOptionAudio(rs.getString("event_name_ivr_audio") == null ? "" : rs.getString("event_name_ivr_audio"));
						}
						eventOptions.add(e);
					}
					return eventOptions;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_EVENT_LIST.getValue(), e);
		}
	}

	@Override
	public void getI18nAliasesMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception {
		String sql = "select device,lang,message_key,message_value from i18n_aliases";
		populateMap(jdbcCustomTemplate, customLogger, sql, map);
		return;
	}

	private void populateMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String sql, final Map<String, Map<String, String>> map) {
		jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
			@Override
			public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, String> subMap = null;
				StringBuilder key = new StringBuilder();
				while (rs.next()) {
					key.append(rs.getString("device")).append("|").append(rs.getString("lang"));
					if (map.containsKey(key.toString())) {
						subMap = map.get(key.toString());
						subMap.put(rs.getString("message_key"), rs.getString("message_value"));
					} else {
						subMap = new HashMap<String, String>();
						subMap.put(rs.getString("message_key"), rs.getString("message_value"));
						map.put(key.toString(), subMap);
					}
					key.setLength(0);
				}
				return map;
			}
		});
	}

	@Override
	public void getI18nDesignTemplatesMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception {
		String sql = "select device,message_key,message_value from i18n_design_templates";
		jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, String>>>() {
			@Override
			public Map<String, Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, String> subMap = null;
				StringBuilder key = new StringBuilder();
				while (rs.next()) {
					key.append(rs.getString("device"));
					if (map.containsKey(key.toString())) {
						subMap = map.get(key.toString());
						subMap.put(rs.getString("message_key"), rs.getString("message_value"));
					} else {
						subMap = new HashMap<String, String>();
						subMap.put(rs.getString("message_key"), rs.getString("message_value"));
						map.put(key.toString(), subMap);
					}
					key.setLength(0);
				}
				return map;
			}
		});
		return;
	}

	@Override
	public void getI18nButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception {
		String sql = "select device,lang,message_key,message_value from i18n_display_button_names";
		populateMap(jdbcCustomTemplate, customLogger, sql, map);
		return;
	}

	@Override
	public void getI18nDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception {
		String sql = "select device,lang,message_key,message_value from i18n_display_field_labels";
		populateMap(jdbcCustomTemplate, customLogger, sql, map);
		return;
	}

	@Override
	public void getI18nPageContentMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception {
		String sql = "select device,lang,message_key,message_value from i18n_display_page_content";
		populateMap(jdbcCustomTemplate, customLogger, sql, map);
		return;
	}

	@Override
	public void getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, Map<String, String>> map) throws Exception {
		String sql = "select device,lang,message_key,message_value from i18n_email_templates";
		populateMap(jdbcCustomTemplate, customLogger, sql, map);
		return;
	}

	@Override
	public List<Language> getLangDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final List<Language> languageList) throws Exception {
		String sql = "select * from i18n_lang ";
		return jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<List<Language>>() {
			@Override
			public List<Language> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Language language = null;
				while (rs.next()) {
					language = new Language("" + rs.getInt("id"), rs.getString("lang_code"), rs.getString("language"), rs.getString("default_lang").charAt(0), rs
							.getString("lang_link"));
					languageList.add(language);
				}
				return languageList;
			}
		});
	}

	@Override
	public String getDefaultLangCode(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception {
		String sql = "select lang_code from i18n_lang where default_lang = 'Y'";
		String defaultLangCode = jdbcCustomTemplate.getJdbcTemplate().queryForObject(sql, String.class);
		return defaultLangCode;
	}

	@Override
	public void getLoginParams(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final Map<String, List<LoginField>> map) throws Exception {
		String sql = "select * from login_param_config lpc order by lpc.placement";
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<Map<String, List<LoginField>>>() {
			@Override
			public Map<String, List<LoginField>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				LoginField loginField = null;
				List<LoginField> loginParamFields = null;
				StringBuilder key = new StringBuilder();
				while (rs.next()) {
					key.append(rs.getString("device_type"));

					loginField = new LoginField();
					// Below three fields are keys, we have to get values from
					// display labels tables, That is doing in service layer.
					loginField.setDisplayTitle(rs.getString("display_title"));
					loginField.setEmptyErrorMessage(rs.getString("empty_error_msg"));
					loginField.setInvalidErrorMessage(rs.getString("invalid_error_msg"));

					loginField.setDisplayType(rs.getString("display_type"));
					loginField.setDisplayContext(rs.getString("display_context"));
					loginField.setFieldName(rs.getString("param_column"));
					loginField.setJavaRef(rs.getString("java_reflection"));
					loginField.setInitValue(rs.getString("list_initial_values"));
					loginField.setIsMandatory(rs.getString("required").charAt(0));
					loginField.setValidationRequired(rs.getString("validate_required"));
					loginField.setLoginType(rs.getString("login_type"));
					// text box size
					loginField.setDisplaySize(String.valueOf(rs.getInt("display_size")));
					loginField.setMaxLength("" + rs.getInt("max_chars"));
					loginField.setListInitValue(rs.getString("list_initial_values"));
					loginField.setInitValue(rs.getString("initial_value"));
					loginField.setListLabels(rs.getString("list_labels"));
					loginField.setListValues(rs.getString("list_values"));

					loginField.setValidateMinValue("" + rs.getInt("validate_min_value"));
					loginField.setValidateMaxValue("" + rs.getInt("validate_max_value"));
					loginField.setValidateMaxChars("" + rs.getInt("validate_max_chars"));
					loginField.setValidateMinChars("" + rs.getInt("validate_min_chars"));
					loginField.setValidateRules(rs.getString("validation_rules"));
					loginField.setPlacement(rs.getInt("placement"));
					loginField.setStorageSize(rs.getInt("storage_size"));
					loginField.setStorageType(rs.getString("storage_type"));
					
					if (map.containsKey(key.toString())) {
						loginParamFields = map.get(key.toString());
						loginParamFields.add(loginField);
					} else {
						loginParamFields = new ArrayList<LoginField>();
						loginParamFields.add(loginField);
						map.put(key.toString(), loginParamFields);
					}
					key.setLength(0);
				}
				return map;
			}
		});
		return;
	}

	@Override
	public boolean isValidToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String device, String token, ClientDeploymentConfig cdConfig)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from tokens tn where 1=1 ");
		sql.append(" and tn.token =").append("'").append(token).append("'");
		sql.append(" and expiry_stamp > ").append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(" and device=").append("'").append(device).append("'");
		sql.append(" and client_code=").append("'").append(clientCode).append("'");
		customLogger.debug("SQL for isValidToken: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString());
		boolean isValid = false;
		if (count > 0) {
			extendExpiryTimestamp(jdbcCustomTemplate, customLogger, clientCode, token, device, cdConfig);
			isValid = true;
		}
		return isValid;
	}

	@Override
	public boolean isValidToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String device, String token, ClientDeploymentConfig cdConfig,
			String customerId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from tokens tn where 1=1 ");
		sql.append(" and tn.token =").append("'").append(token).append("'");
		sql.append(" and expiry_stamp > ").append("CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(" and device=").append("'").append(device).append("'");
		sql.append(" and client_code=").append("'").append(clientCode).append("'");
		sql.append(" and customer_id=").append(customerId);
		customLogger.debug("SQL for isValidToken: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString());
		boolean isValid = false;
		if (count > 0) {
			extendExpiryTimestamp(jdbcCustomTemplate, customLogger, clientCode, token, device, cdConfig);
			isValid = true;
		}
		return isValid;
	}

	private void extendExpiryTimestamp(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String token, String device,
			ClientDeploymentConfig cdConfig) throws Exception {
		try {
			String extendexpiryMinStr = PropertyUtils.getValueFromProperties("TOKEN_EXPIRY_IN_MINS", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
			if (extendexpiryMinStr == null) {
				extendexpiryMinStr = "15";
			}
			extendexpiryMinStr = "00:" + extendexpiryMinStr + ":00";
			StringBuilder sql = new StringBuilder("update tokens set expiry_stamp= CONVERT_TZ(ADDTIME(now(),'");
			sql.append(extendexpiryMinStr).append("'),'US/Central','");
			sql.append(cdConfig.getTimeZone()).append("') where  token=? and client_code=? and device=?");
			customLogger.debug("Extended expiry query:" + sql.toString());
			jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { token, clientCode, device });
		} catch (Exception e) {
			throw new Exception("Expiry time not extended.");
		}
	}

	@Override
	public String saveAndGetToken(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String device, ClientDeploymentConfig cdConfig, String param2)
			throws Exception {
		String expiryMinStr = PropertyUtils.getValueFromProperties("TOKEN_EXPIRY_IN_MINS", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
		int expiryMin = Integer.parseInt(expiryMinStr);
		String token = CoreUtils.getToken(clientCode, device);
		StringBuilder sql = new StringBuilder("insert into tokens (client_code,session_id, device,expiry_stamp, token) values (?,?,?,CONVERT_TZ(ADDTIME(now(),");
		sql.append("'00:").append(expiryMin).append(":00')").append(",'US/Central','").append(cdConfig.getTimeZone()).append("')");
		sql.append(",?)");
		customLogger.debug("saveAndGetToken SQL: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { clientCode, param2, device, token });
		if (count > 0) {
			return token;
		} else {
			throw new Exception("Token not saved to DB!.");
		}
	}

	@Override
	public String saveTransIdAndGet(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, final String param1, final String param2)
			throws Exception {
		final String sql = "insert into main (timestamp,device,uuid,ip_address,caller_id,username) values (?,?,?,?,?,?)";

		KeyHolder holder = new GeneratedKeyHolder();
		int count = jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
			int i = 1;

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
				ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
				ps.setString(i++, device);
				if (CommonResvDeskConstants.ONLINE.getValue().equals(device) || CommonResvDeskConstants.ADMIN.getValue().equals(device)) {
					ps.setString(i++, "");
					ps.setString(i++, param1);
					ps.setString(i++, "");
					ps.setString(i++, param2);
				} else if (CommonResvDeskConstants.MOBILE.getValue().equals(device)) {
					ps.setString(i++, param1);
					ps.setString(i++, "");
					ps.setString(i++, param2);
					ps.setString(i++, "");

				} else if (CommonResvDeskConstants.IVRAUDIO.getValue().equals(device)) {
					ps.setString(i++, "");
					ps.setString(i++, "");
					ps.setString(i++, param1);
					ps.setString(i++, param2);
				}
				return ps;
			}
		}, holder);
		Long id = holder.getKey().longValue();
		if (count > 0) {
			return "" + id;
		} else {
			throw new Exception("TransId not saved to DB!.");
		}
	}

	@Override
	public void loadVXML(final JdbcCustomTemplate jdbcCustomTemplate, final String mainKey, final Map<String, Map<String, IVRXml>> cacheMap) throws Exception {
		String sql = "select app_code,page_name,lang_code,audio,tts,vxml from ivr_vxml where enabled='Y'";
		jdbcCustomTemplate.getJdbcTemplate().query(sql, new ResultSetExtractor<Map<String, Map<String, IVRXml>>>() {
			@Override
			public Map<String, Map<String, IVRXml>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, IVRXml> map = new HashMap<String, IVRXml>();
				StringBuilder key = new StringBuilder();
				String appCode = null;
				String pageName = null;
				String langCode = null;
				IVRXml ivrXML = null;
				while (rs.next()) {
					ivrXML = new IVRXml();
					appCode = rs.getString("app_code");
					pageName = rs.getString("page_name");
					langCode = rs.getString("lang_code");
					ivrXML.setPageName(pageName);
					ivrXML.setPageAudio(rs.getString("audio"));
					ivrXML.setPageTTS(rs.getString("tts"));
					ivrXML.setVxml(rs.getString("vxml"));
					key.append(appCode).append("|").append(pageName).append("|").append(langCode);
					map.put(key.toString(), ivrXML);
					key.setLength(0);
				}

				cacheMap.put(mainKey, map);
				return cacheMap;
			}
		});
		return;
	}

	

	@Override
	public void getEventTimes(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String eventId, String locationId, String date,
			final Map<String, String> availableTimeMap) throws Exception {
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_HMMA.getValue());
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct edt.id,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time");
		sql.append(" from event_date_time edt, seat s");
		sql.append(" where edt.event_id = ? and edt.location_id = ? and edt.enable = 'Y' and edt.num_seats > 0");
		sql.append(" and edt.date = ? and edt.id = s.event_date_time_id and s.schedule_id = 0 ");
		sql.append("and s.reserved = 'N' order by edt.time asc");
		customLogger.debug("getEventTimes SQL: " + sql.toString());
		customLogger.debug("getEventTimes SQL Values: eventId " + eventId + ",locationId=" + locationId + ", date:" + date);
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(eventId), Long.valueOf(locationId), date },
					new ResultSetExtractor<Map<String, String>>() {
						@Override
						public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								availableTimeMap.put("" + rs.getInt("id"), rs.getString("time"));
							}
							return availableTimeMap;
						}
					});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_EVENT_TIMES.getValue(), e);
		}
		return;
	}
	
	@Override
	public void getEventDisplayTimes(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger,final Map<String, String> displayAvailableTimMap) throws Exception {
		String sql = "select time, display_time_online from display_time_lookup";
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(),
					new ResultSetExtractor<Map<String, String>>() {
						@Override
						public Map<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
							while (rs.next()) {
								displayAvailableTimMap.put(rs.getString("time"), rs.getString("display_time_online"));
							}
							return displayAvailableTimMap;
						}
					});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_EVENT_TIMES.getValue(), e);
		}
		
	}

	@Override
	public void getEventSeats(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final String device, String eventDateTimeId, final List<Seat> seatList)
			throws Exception {
		String sql = "select id,display_seat_number,tts,audio from seat where event_date_time_id =? and schedule_id = 0 and reserved = 'N' order by placement";
		customLogger.debug("getEventSeats SQL: " + sql.toString());
		customLogger.debug("getEventSeats SQL Values: event_date_time_id " + eventDateTimeId);

		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(eventDateTimeId) }, new ResultSetExtractor<List<Seat>>() {
				@Override
				public List<Seat> extractData(ResultSet rs) throws SQLException, DataAccessException {
					Seat seat = null;
					while (rs.next()) {
						seat = new Seat();
						seat.setSeatId("" + rs.getInt("id"));
						if (CoreUtils.isOnline(device)) {
							seat.setSeatNumber(rs.getString("display_seat_number"));
						} else if (CoreUtils.isIVR(device)) {
							seat.setSeatTTS(rs.getString("tts"));
							seat.setSeatAudio(rs.getString("audio"));
						}
						seatList.add(seat);
					}
					return seatList;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_EVENT_SEATS.getValue(), e);
		}
		return;
	}

	@Override
	public void getVerifyReservationDetails(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, final String langCode,
			final String transId, final String scheduleId, final String customerId, final String loginFirst, final ResvSysConfig resvSysConfig,
			final ClientDeploymentConfig cdConfig, final VerifyResvResponse verifyResponse) throws Exception {

		jdbcCustomTemplate.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus paramTransactionStatus) {
				try {
					String resvStatusAndReminderFlag = getReservationStatus(jdbcCustomTemplate, customLogger, scheduleId);
					String array[] = resvStatusAndReminderFlag.split("\\|");
					int resvStatus = 0;
					if (resvStatusAndReminderFlag.length() > 1) {
						resvStatus = Integer.valueOf(array[0]);
					}

					if (ReservataionStatus.RELEASE_HOLD.getStatus() == resvStatus) {
						customLogger.error("Reservation status in release state, Please try again");
						throw new Exception(FlowStateConstants.GET_VERIFY_RESERVATION.getValue());
					}

					if (scheduleId != null && scheduleId.length() > 0 && Long.valueOf(scheduleId) <= 0) {
						customLogger.error("scheduleId is less then or equals to zero");
						throw new Exception(FlowStateConstants.GET_VERIFY_RESERVATION.getValue());
					}

					if (ReservataionStatus.HOLD.getStatus() != resvStatus) {
						customLogger.error("Reservation status is not in HOLD state - scheduleId:" + scheduleId);
						throw new Exception(FlowStateConstants.GET_VERIFY_RESERVATION.getValue());
					}

					if (customerId == null || (customerId != null && customerId.trim().length() == 0)) {
						customLogger.error("customerId is Empty or NULL - customerId:" + customerId);
						throw new Exception(FlowStateConstants.GET_VERIFY_RESERVATION.getValue());
					}

					if (Long.valueOf(customerId) <= 0 && "Y".equals(loginFirst)) {
						customLogger.error("customerId not passed from front end to update the schedule table, Passed customerId:" + customerId);
						throw new Exception(FlowStateConstants.GET_VERIFY_RESERVATION.getValue());
					}
					populateVerifyResponse(jdbcCustomTemplate, customLogger, device, Long.valueOf(scheduleId), cdConfig, verifyResponse);

				} catch (Exception e) {
					customLogger.error("Error in getVerifyReservationDetails:", e);
					paramTransactionStatus.setRollbackOnly();
				}
			}
		});

	}

	private void populateVerifyResponse(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, Long scheduleId,
			final ClientDeploymentConfig cdConfig, final VerifyResvResponse verifyResvResponse) throws Exception {

		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_HHMMZZZZ.getValue());

		StringBuilder sql = new StringBuilder();
		sql.append("select ").append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time,");
		sql.append("c.company_name_online,c.company_name_ivr_tts,c.company_name_ivr_audio,");
		sql.append("p.procedure_name_online,p.procedure_name_ivr_tts,p.procedure_name_ivr_audio,");
		sql.append("l.location_name,l.location_name_ivr_tts,l.location_name_ivr_audio,l.time_zone,");
		sql.append("d.department_name_online,d.department_name_ivr_tts,department_name_ivr_audio,");
		sql.append("e.event_name,e.duration, e.event_name_ivr_tts,e.event_name_ivr_audio,");
		sql.append("s.display_seat_number,s.tts,s.audio,");
		sql.append("cu.first_name,cu.last_name");
		sql.append(" from schedule sc, company c, `procedure` p, location l,");
		sql.append(" department d, event e, seat s, event_date_time edt,customer cu");
		sql.append(" where sc.id = ? ");
		sql.append(" and sc.location_id=l.id and sc.company_id = c.id and sc.procedure_id=p.id ");
		sql.append(" and sc.location_id=l.id and sc.department_id=d.id and sc.event_id =e.event_id ");
		sql.append(" and sc.event_date_time_id = edt.id and sc.seat_id=s.id");
		sql.append(" and sc.customer_id = cu.id");
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { scheduleId }, new ResultSetExtractor<VerifyResvResponse>() {
			@Override
			public VerifyResvResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					String date = rs.getString("date");
					String time = rs.getString("time");
					verifyResvResponse.setReservationDate(date);
					verifyResvResponse.setDuration(rs.getInt("duration"));

					if (CoreUtils.isOnline(device)) {
						verifyResvResponse.setDisplayDate(date);
						verifyResvResponse.setDisplayTime(time);
					}
					verifyResvResponse.setReservationDate(date);
					verifyResvResponse.setReservationTime(time);

					String timeZone = rs.getString("time_zone");
					timeZone = (timeZone == null) ? cdConfig.getTimeZone() : timeZone;

					verifyResvResponse.setTimeZone(timeZone);
					verifyResvResponse.setSeatNumber(rs.getString("display_seat_number"));
					if (CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
						verifyResvResponse.setCompanyName(rs.getString("company_name_online"));
						verifyResvResponse.setProcedure(rs.getString("procedure_name_online"));
						verifyResvResponse.setLocation(rs.getString("location_name"));
						verifyResvResponse.setDepartment(rs.getString("department_name_online"));
						verifyResvResponse.setEvent(rs.getString("event_name"));
					} else if (CoreUtils.isIVR(device)) {
						verifyResvResponse.setCompanyName(rs.getString("company_name_ivr_tts"));
						verifyResvResponse.setProcedure(rs.getString("procedure_name_ivr_tts"));
						verifyResvResponse.setLocation(rs.getString("location_name_ivr_tts"));
						verifyResvResponse.setDepartment(rs.getString("department_name_ivr_tts"));
						verifyResvResponse.setEvent(rs.getString("event_name_ivr_tts"));
					}
				}
				return verifyResvResponse;
			}
		});

	}

	@Override
	public void releaseHoldEventTime(final JdbcCustomTemplate jdbcCustomTemplate, final String scheduleId, final BaseResponse baseResponse) throws Exception {
		jdbcCustomTemplate.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
				try {
					final StringBuilder sql = new StringBuilder();
					sql.append("update schedule set status = ? where id = ?");
					int updateSchedule = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(),
							new Object[] { ReservataionStatus.RELEASE_HOLD.getStatus(), Long.valueOf(scheduleId) });

					sql.setLength(0);
					sql.append("update seat set schedule_id=0, reserved='N' where schedule_id = ?");
					int updateSeat = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { Long.valueOf(scheduleId) });

					if (updateSchedule > 0 && updateSeat > 0) {
						baseResponse.setResponseStatus(true);
					} else {
						throw new Exception();
					}
				} catch (Exception e) {
					transactionStatus.setRollbackOnly();
					throw new TelAppointDBException(FlowStateConstants.HOLD_RELEASE_EVENT_TIME.getValue(), e);
				}
			}
		});
	}

	@Override
	public void cancelReservation(final JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String scheduleId, final String customerId,
			final BaseResponse baseResponse) throws Exception {
		if (scheduleId != null && scheduleId.length() > 0 && Long.valueOf(scheduleId) <= 0) {
			customLogger.error("scheduleId is less then or equals to zero");
			throw new Exception(FlowStateConstants.CONFIRM_RESERVATION.getValue());
		}

		if (customerId == null || (customerId != null && customerId.trim().length() == 0)) {
			customLogger.error("customerId is Empty or NULL - customerId:" + customerId);
			throw new Exception(FlowStateConstants.CANCEL_RESERVATION.getValue());
		}

		if (Long.valueOf(customerId) <= 0) {
			customLogger.error("customerId not passed from front end to update the schedule table, Passed customerId:" + customerId);
			throw new Exception(FlowStateConstants.CANCEL_RESERVATION.getValue());
		}
		final Schedule schedule = getSchedule(jdbcCustomTemplate, customLogger, scheduleId);
		int resvStatus = schedule.getStatus();
		if (resvStatus == -1) {
			customLogger.error("Reservation not found with scheduleId:" + scheduleId + ",customerId:" + customerId);
			throw new Exception(FlowStateConstants.CANCEL_RESERVATION.getValue());
		}

		jdbcCustomTemplate.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
				try {
					final StringBuilder sql = new StringBuilder();
					sql.append("update schedule set status = ? where id = ?");
					int updateSchedule = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(),
							new Object[] { ReservataionStatus.CANCEL.getStatus(), Long.valueOf(scheduleId) });

					sql.setLength(0);
					sql.append("update seat set schedule_id=0, reserved='N' where schedule_id = ?");
					int updateSeat = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { Long.valueOf(scheduleId) });
					
					sql.setLength(0);
					sql.append("update notify set do_not_notify='Y' where schedule_id = ?");
					jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { Long.valueOf(scheduleId) });

					if (updateSchedule > 0 && updateSeat > 0) {
						updateBookedSeat(jdbcCustomTemplate, customLogger, schedule.getEventDateTimeId(),false);
						baseResponse.setResponseStatus(true);
					} else {
						customLogger.debug("no row updated. throwing error");
						throw new Exception();
					}
				} catch (Exception e) {
					customLogger.debug("Input values: scheduleId:" + scheduleId + ", customerId:" + customerId);
					System.out.println(":::BOOKED_SEATS:::cancel reservation");
					customLogger.error("Error:", e);
					transactionStatus.setRollbackOnly();
					throw new TelAppointDBException(FlowStateConstants.CANCEL_RESERVATION.getValue(), e);
				}
			}
		});
	}

	@Override
	public void getEventsHistory(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, final List<EventHistory> eventHistoryList)
			throws Exception {
		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue());
		StringBuilder sql = new StringBuilder("select e.event_name, l.location_name, ");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time");
		sql.append(" from event_date_time edt, event e, location l where edt.enable = 'Y' and edt.date >= CURDATE()");
		sql.append(" and edt.event_id = e.event_id and edt.location_id = l.id");
		sql.append(" order by l.location_name, e.placement, edt.date, edt.time");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new ResultSetExtractor<List<EventHistory>>() {

				@Override
				public List<EventHistory> extractData(ResultSet rs) throws SQLException, DataAccessException {
					EventHistory eventHistory = null;
					while (rs.next()) {
						eventHistory = new EventHistory();
						eventHistory.setEventName(rs.getString("event_name"));
						eventHistory.setLocationName(rs.getString("location_name"));
						eventHistory.setDate(rs.getString("date"));
						eventHistory.setTime(rs.getString("time"));
						eventHistoryList.add(eventHistory);
					}
					return eventHistoryList;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_EVENT_HISTORY.getValue(), e);
		}
		return;
	}

	@Override
	public void updateTokenCustomerId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String clientCode, String token, String device, Long customerId,
			ClientDeploymentConfig cdConfig) throws Exception {
		try {
			String sql = "update tokens set customer_id= ? where  token=? and client_code=? and device=?";
			jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[] { customerId, token, clientCode, device });
			extendExpiryTimestamp(jdbcCustomTemplate, customLogger, clientCode, token, device, cdConfig);
		} catch (Exception e) {
			throw new Exception("CustomerId update failed..");
		}
	}

	@Override
	public void updateRecordVxml(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId, String customerId, String filePath, String fileName,
			String recordDurationInSec, ClientDeploymentConfig cdConfig) throws Exception {
		StringBuilder sql = new StringBuilder();

		boolean recordExist = getRecordExist(jdbcCustomTemplate, customLogger, scheduleId, customerId);
		if (recordExist) {
			sql.append("update voice_msg set file_path=?, file_name = ?, record_duration=? where schedule_id=? and customer_id=?");
			int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { filePath, fileName, recordDurationInSec, scheduleId, customerId });
			if (count == 0) {
				throw new Exception("updateRecordVxml update failed..");
			}
		} else {
			sql.append("insert into voice_msg(schedule_id,timestamp, customer_id,file_path, file_name, record_duration)");
			sql.append(" values(?,CONVERT_TZ(now(),'US/Central','" + cdConfig.getTimeZone() + "'),?,?,?,?)");
			int count = jdbcCustomTemplate.getJdbcTemplate().update(sql.toString(), new Object[] { scheduleId, customerId, filePath, fileName, recordDurationInSec });
			if (count == 0) {
				throw new Exception("updateRecordVxml insert failed..");
			}
		}
	}

	private boolean getRecordExist(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String scheduleId, String customerId) {
		// TODO: check record already exist in voice_msg table.
		return false;
	}

	@Override
	public void listOfThingsToBring(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, String eventId,
			final ListOfThingsResponse listOfThingsResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select display_text");
		sql.append(" from list_of_things_bring where event_id = ? and device= ? and lang= ?");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { eventId, device, langCode }, new ResultSetExtractor<ListOfThingsResponse>() {
				@Override
				public ListOfThingsResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						listOfThingsResponse.setDeplayText(rs.getString("display_text"));
					}
					return listOfThingsResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.LIST_OF_THINGS_TO_BRING.getValue(), e);
		}
		return;
	}

	@Override
	public void getConfPageContactDetails(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, String locationId,
			final ConfPageContactDetailsResponse confPageContactDetailsResponse) throws Exception {
		StringBuilder sql = new StringBuilder("select address,city,state,zip,");
		sql.append("work_phone,location_google_map,location_google_map_link,");
		sql.append("concat( right(work_phone,3) , '-' , mid(work_phone,4,3) , '-', right(work_phone,4)) as workPhone,");
		sql.append("contact_details as contactDetails");
		sql.append(" from location where id = ?");
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { locationId }, new ResultSetExtractor<ConfPageContactDetailsResponse>() {
				@Override
				public ConfPageContactDetailsResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						confPageContactDetailsResponse.setAddress(rs.getString("address"));
						confPageContactDetailsResponse.setCity(rs.getString("city"));
						confPageContactDetailsResponse.setState(rs.getString("state"));
						confPageContactDetailsResponse.setZip(rs.getString("zip"));
						confPageContactDetailsResponse.setLocationGoogleMap(rs.getString("location_google_map"));
						confPageContactDetailsResponse.setLocationGoogleMap(rs.getString("location_google_map_link"));
						confPageContactDetailsResponse.setWorkPhone(rs.getString("workPhone"));
						confPageContactDetailsResponse.setContactDetails(rs.getString("contactDetails"));
					}
					return confPageContactDetailsResponse;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.CONF_PAGE_CONTACTS_DETAILS.getValue(), e);
		}
		return;

	}

	@Override
	public void getReservedEvents(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, String customerId, final String timeZone, final String device, final List<EventHistory> reservedEventList)
			throws Exception {
		String dateFormat = getMariaDBDateFormat(CommonDateContants.MM_DD_YYYY_DATE_FORMAT.getValue());
		String timeFormat = getMariaDBTimeFormat(CommonDateContants.TIME_FORMAT_TWELVE_HRS.getValue());
		StringBuilder sql = new StringBuilder("select s.id,");
		sql.append("DATE_FORMAT(edt.date,").append("'").append(dateFormat).append("')").append("as date,");
		sql.append("DATE_FORMAT(edt.time,").append("'").append(timeFormat).append("'").append(") as time,");
		sql.append("l.location_name,c.company_name_online, p.procedure_name_online,");
		sql.append("d.department_name_online,e.event_name,se.display_seat_number");
		sql.append(" from schedule s, event_date_time edt, location l, department d, `procedure` p, company c, event e, seat se");
		sql.append(" where edt.date > CURRENT_DATE() and ");
		sql.append("s.customer_id=? and s.status=");
		sql.append(ReservataionStatus.CONFIRM.getStatus()).append(" and s.event_date_time_id = edt.id and s.location_id=l.id and");
		sql.append(" s.department_id=d.id and");
		sql.append(" s.procedure_id = p.id and");
		sql.append(" s.event_id = e.event_id and s.company_id=c.id and");
		sql.append(" s.seat_id = se.id and (se.reserved='Y' or se.schedule_id > 0)");
		sql.append(" order by edt.date, edt.time");
		customLogger.debug("ReservedEvents query: " + sql.toString());
		try {
			jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { Long.valueOf(customerId) }, new ResultSetExtractor<List<EventHistory>>() {
				@Override
				public List<EventHistory> extractData(ResultSet rs) throws SQLException, DataAccessException {
					EventHistory eventHistory = null;
					int size = 1;
					while (rs.next()) {
						eventHistory = new EventHistory();
						eventHistory.setScheduleId("" + rs.getLong("id"));
						eventHistory.setEventName(rs.getString("event_name"));
						eventHistory.setLocationName(rs.getString("location_name"));
						eventHistory.setCompanyName(rs.getString("company_name_online"));
						eventHistory.setProcedureName(rs.getString("procedure_name_online"));
						eventHistory.setDepartmentName(rs.getString("department_name_online"));
						eventHistory.setSeatNumber(rs.getString("display_seat_number"));
						eventHistory.setDate(rs.getString("date"));
						eventHistory.setTime(rs.getString("time"));
						if(CoreUtils.isIVR(device)) {
							eventHistory.setTimeZone(timeZone);
						}
						reservedEventList.add(eventHistory);
						if (size == 5)
							break;
						size++;
					}
					return reservedEventList;
				}
			});
		} catch (Exception e) {
			throw new TelAppointDBException(FlowStateConstants.GET_EVENT_HISTORY.getValue(), e);
		}
		return;

	}

	public void prepareResvDetails(JdbcCustomTemplate jdbcCustomTemplate, final CustomLogger customLogger, final String device, Long scheduleId,
			final ClientDeploymentConfig cdConfig, final ConfirmResvResponse confirmResvResponse) throws Exception {
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
		jdbcCustomTemplate.getJdbcTemplate().query(sql.toString(), new Object[] { scheduleId }, new ResultSetExtractor<ConfirmResvResponse>() {
			@Override
			public ConfirmResvResponse extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					String date = rs.getString("date");
					String time = rs.getString("time");
					confirmResvResponse.setReservationDate(date);
					confirmResvResponse.setFirstName(rs.getString("first_name"));
					confirmResvResponse.setLastName(rs.getString("last_name"));
					confirmResvResponse.setDuration(rs.getInt("duration"));
					confirmResvResponse.setEmail(rs.getString("email"));
					confirmResvResponse.setConfNumber("" + rs.getLong("conf_number"));
					confirmResvResponse.setDuration(rs.getInt("duration"));

					if (CoreUtils.isOnline(device)) {
						confirmResvResponse.setDisplayDate(date);
						confirmResvResponse.setDisplayTime(time);
					}
					confirmResvResponse.setReservationDate(date);
					confirmResvResponse.setReservationTime(time);

					String timeZone = rs.getString("time_zone");
					timeZone = (timeZone == null) ? cdConfig.getTimeZone() : timeZone;

					confirmResvResponse.setTimeZone(timeZone);
					confirmResvResponse.setSeatNumber(rs.getString("display_seat_number"));
					if (CoreUtils.isOnline(device)) {
						confirmResvResponse.setCompanyName(rs.getString("company_name_online"));
						confirmResvResponse.setProcedure(rs.getString("procedure_name_online"));
						confirmResvResponse.setLocation(rs.getString("location_name"));
						confirmResvResponse.setDepartment(rs.getString("department_name_online"));
						confirmResvResponse.setEvent(rs.getString("event_name"));
					} else if (CoreUtils.isIVR(device)) {
						confirmResvResponse.setCompanyName(rs.getString("company_name_ivr_tts"));
						confirmResvResponse.setProcedure(rs.getString("procedure_name_ivr_tts"));
						confirmResvResponse.setLocation(rs.getString("location_name_ivr_tts"));
						confirmResvResponse.setDepartment(rs.getString("department_name_ivr_tts"));
						confirmResvResponse.setEvent(rs.getString("event_name_ivr_tts"));
					}
				}
				return confirmResvResponse;
			}
		});
	}

	@Override
	public boolean noApptInAllLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from event_date_time edt, seat s, event e, location l where edt.event_id=e.event_id and ");
		sql.append(" l.id=edt.location_id and s.event_date_time_id = edt.id and s.reserved='N' and s.schedule_id=0 and l.delete_flag='N'");
		sql.append(" and edt.enable='Y' and e.delete_flag='N' and e.enable='Y' and l.enable='Y'");
		customLogger.debug("SQL for isValidToken: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString());
		if (count > 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean noApptInSelectedLocation(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String locationId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from event_date_time edt, seat s, event e, location l where edt.location_id=? and s.schedule_id=0 ");
		sql.append(" and edt.event_id=e.event_id and l.id=edt.location_id and s.event_date_time_id = edt.id  and s.reserved='N' and l.delete_flag='N'");
		sql.append(" and edt.enable='Y' and e.delete_flag='N' and e.enable='Y' and l.enable='Y'");
		customLogger.debug("SQL for isValidToken: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[]{Long.valueOf(locationId)});
		if (count > 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean noApptInSelectedEvent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String locationId, String eventId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from event_date_time edt, seat s, event e, location l where edt.location_id=? and edt.event_id=? and s.schedule_id=0 and ");
		sql.append(" edt.event_id=e.event_id and l.id=edt.location_id and s.event_date_time_id = edt.id and s.reserved='N' and l.delete_flag='N'");
		sql.append(" and edt.enable='Y' and e.delete_flag='N' and e.enable='Y' and l.enable='Y'");
		customLogger.debug("SQL for isValidToken: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[]{Long.valueOf(locationId), Long.valueOf(eventId)});
		if (count > 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean noApptInSelectedDate(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger,String locationId, String eventId, String date) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from event_date_time edt, seat s, event e, location l where edt.date=? and edt.location_id=? and edt.event_id=? and s.schedule_id=0 ");
		sql.append(" and edt.event_id=e.event_id and l.id=edt.location_id and s.event_date_time_id = edt.id and s.reserved='N' and l.delete_flag='N'");
		sql.append(" and edt.enable='Y' and e.delete_flag='N' and e.enable='Y' and l.enable='Y'");
		customLogger.debug("SQL for isValidToken: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(),new Object[]{date, Long.valueOf(locationId), Long.valueOf(eventId)});
		if (count > 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean noApptInSelectedTime(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger,String locationId, String eventId, String date,String eventDateTimeId) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from event_date_time edt, seat s, event e, location l where edt.id=? and edt.date=? and edt.location_id=? and edt.event_id=? and s.schedule_id=0 ");
		sql.append(" and edt.event_id=e.event_id and edt.location_id = l.id and s.event_date_time_id = edt.id and s.reserved='N' and l.delete_flag='N'");
		sql.append(" and edt.enable='Y' and e.delete_flag='N' and e.enable='Y' and l.enable='Y'");
		customLogger.debug("SQL for isValidToken: " + sql.toString());
		int count = jdbcCustomTemplate.getJdbcTemplate().queryForInt(sql.toString(), new Object[]{Long.valueOf(eventDateTimeId), date,Long.valueOf(locationId), Long.valueOf(eventId)});
		if (count > 0) {
			return false;
		}
		return true;
	}

	@Override
	public void saveOrUpdateIVRCallLog(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final ClientDeploymentConfig cdConfig, final IVRCallRequest ivrCallRequest, IVRCallResponse ivrCallResponse) throws Exception {
		if (ivrCallRequest.getIvrCallId() ==  null || ivrCallRequest.getIvrCallId() == 0) {
	        final StringBuilder sql = new StringBuilder("insert into ivr_calls (trans_id, start_time)");
	        sql.append(" values (?,now())");
	        KeyHolder holder = new GeneratedKeyHolder();
	        int count = jdbcCustomTemplate.getJdbcTemplate().update(new PreparedStatementCreator() {
	            int i = 1;

	            @Override
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
	                ps.setLong(i++, Long.valueOf(ivrCallRequest.getTransId()));
	                return ps;
	            }
	        }, holder);
	        Long id = holder.getKey().longValue();
	        ivrCallResponse.setIvrCallId(id);
	    } else {
	        StringBuilder sql = new StringBuilder("update ivr_calls set trans_id=:transId");
	        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
	        mapSqlParameterSource.addValue("transId", ivrCallRequest.getTransId());
	        if(ivrCallRequest.getCustomerId() != null && ivrCallRequest.getCustomerId() > 0) {
	        	sql.append(",customer_id=:customerId");
	        	mapSqlParameterSource.addValue("customerId", ivrCallRequest.getCustomerId());
	        }
	        if(ivrCallRequest.getEventId() != null && ivrCallRequest.getEventId() > 0) {
	        	sql.append(",event_id=:eventId");
	        	mapSqlParameterSource.addValue("eventId", ivrCallRequest.getEventId());
	        }
	        if(ivrCallRequest.getEventDateTimeId() != null && ivrCallRequest.getEventDateTimeId() > 0) {
	        	sql.append(",event_date_time_id=:eventDateTimeId");
	        	mapSqlParameterSource.addValue("eventDateTimeId", ivrCallRequest.getEventDateTimeId());
	        }
	        if(ivrCallRequest.getLocationId() != null && ivrCallRequest.getLocationId() > 0) {
	        	sql.append(",location_id=:locationId");
	        	mapSqlParameterSource.addValue("locationId", ivrCallRequest.getLocationId());
	        }
	        if(ivrCallRequest.getSeatId() != null && ivrCallRequest.getSeatId() > 0) {
	        	sql.append(",seat_id=:seatId");
	        	mapSqlParameterSource.addValue("seatId", ivrCallRequest.getSeatId());
	        }
	        if(ivrCallRequest.getConfNumber() != null && ivrCallRequest.getConfNumber() > 0) {
	        	sql.append(",conf_num=:confirmNumber");
	        	mapSqlParameterSource.addValue("confirmNumber", ivrCallRequest.getConfNumber());
	        }
	        
	        if(ivrCallRequest.getApptType() != null && ivrCallRequest.getApptType() > 0) {
	        	sql.append(",appt_type=:apptType");
	        	mapSqlParameterSource.addValue("apptType", ivrCallRequest.getApptType());
	        }
	        
	        sql.append(",end_time= CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')");
        	sql.append(",seconds=TIMESTAMPDIFF(SECOND,start_time,CONVERT_TZ(now(),'US/Central','").append(cdConfig.getTimeZone()).append("')) where id=:ivrCallId");
        	mapSqlParameterSource.addValue("ivrCallId", ivrCallRequest.getIvrCallId());
        	
	       int count = jdbcCustomTemplate.getNameParameterJdbcTemplate().update(sql.toString(), mapSqlParameterSource);
	       if(count == 0) {
	    	   throw new Exception("IVR call upadation failed.");
	       }
	    }	
	}
	
	public int updateSeconds(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, final ClientDeploymentConfig cdConfig, long ivrCallId) {
		int seconds = cdConfig.getLagTimeInSeconds() + cdConfig.getLeadTimeInSeconds();
		String sql = "update ivr_calls set seconds=CEILING((seconds+"+seconds+")/60)*60 where id=?";
		return jdbcCustomTemplate.getJdbcTemplate().update(sql, new Object[]{ivrCallId});	
	}

	
}

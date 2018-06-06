package com.telappoint.resvdeskrestws.common.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.common.clientdb.dao.ClientDAO;
import com.telappoint.resvdeskrestws.common.components.CacheComponent;
import com.telappoint.resvdeskrestws.common.components.ConnectionPoolUtil;
import com.telappoint.resvdeskrestws.common.components.EmailComponent;
import com.telappoint.resvdeskrestws.common.constants.CommonResvDeskConstants;
import com.telappoint.resvdeskrestws.common.constants.DesignTemplateConstants;
import com.telappoint.resvdeskrestws.common.constants.DisplayButtonsConstants;
import com.telappoint.resvdeskrestws.common.constants.DisplayFieldLabelConstants;
import com.telappoint.resvdeskrestws.common.constants.DisplayPageContentConstants;
import com.telappoint.resvdeskrestws.common.constants.EmailConstants;
import com.telappoint.resvdeskrestws.common.constants.EmailTemplateConstants;
import com.telappoint.resvdeskrestws.common.constants.FlowStateConstants;
import com.telappoint.resvdeskrestws.common.constants.IVRVxmlConstants;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;
import com.telappoint.resvdeskrestws.common.masterdb.dao.MasterDAO;
import com.telappoint.resvdeskrestws.common.masterdb.domain.Client;
import com.telappoint.resvdeskrestws.common.model.AllowDuplicateResv;
import com.telappoint.resvdeskrestws.common.model.AuthResponse;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ClientInfo;
import com.telappoint.resvdeskrestws.common.model.CompanyListResponse;
import com.telappoint.resvdeskrestws.common.model.ConfPageContactDetailsResponse;
import com.telappoint.resvdeskrestws.common.model.ConfirmResvResponse;
import com.telappoint.resvdeskrestws.common.model.ConfirmationPageRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.Customer;
import com.telappoint.resvdeskrestws.common.model.DepartmentListResponse;
import com.telappoint.resvdeskrestws.common.model.EmailRequest;
import com.telappoint.resvdeskrestws.common.model.ErrorConfig;
import com.telappoint.resvdeskrestws.common.model.EventDate;
import com.telappoint.resvdeskrestws.common.model.EventDatesResponse;
import com.telappoint.resvdeskrestws.common.model.EventHistory;
import com.telappoint.resvdeskrestws.common.model.EventHistoryResponse;
import com.telappoint.resvdeskrestws.common.model.EventListResponse;
import com.telappoint.resvdeskrestws.common.model.EventTimesResponse;
import com.telappoint.resvdeskrestws.common.model.HoldResvResponse;
import com.telappoint.resvdeskrestws.common.model.IVRCallRequest;
import com.telappoint.resvdeskrestws.common.model.IVRCallResponse;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.Language;
import com.telappoint.resvdeskrestws.common.model.ListOfThingsResponse;
import com.telappoint.resvdeskrestws.common.model.LocationListResponse;
import com.telappoint.resvdeskrestws.common.model.LoginField;
import com.telappoint.resvdeskrestws.common.model.LoginInfoResponse;
import com.telappoint.resvdeskrestws.common.model.LoginInfoRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.NameRecordResponse;
import com.telappoint.resvdeskrestws.common.model.Options;
import com.telappoint.resvdeskrestws.common.model.ProcedureListResponse;
import com.telappoint.resvdeskrestws.common.model.ReservedEventResponse;
import com.telappoint.resvdeskrestws.common.model.ResvDetailsResponse;
import com.telappoint.resvdeskrestws.common.model.ResvDetailsRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.ResvSysConfig;
import com.telappoint.resvdeskrestws.common.model.ResvVerificationDetailsRightSideContentResponse;
import com.telappoint.resvdeskrestws.common.model.Seat;
import com.telappoint.resvdeskrestws.common.model.SeatResponse;
import com.telappoint.resvdeskrestws.common.model.SpecificDateResponse;
import com.telappoint.resvdeskrestws.common.model.UpdateRecordResponse;
import com.telappoint.resvdeskrestws.common.model.VerifyResvResponse;
import com.telappoint.resvdeskrestws.common.service.ReservationService;
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
public class ReservationServiceImpl implements ReservationService {
	@Autowired
	private MasterDAO masterDAO;

	@Autowired
	private ClientDAO clientDAO;

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private EmailComponent emailComponent;

	@Autowired
	private ConnectionPoolUtil connectionPoolUtil;

	public CustomLogger getLogger(String clientCode, String device) throws Exception {
		return cacheComponent.getLogger(clientCode, device, true);
	}

	public ClientInfo getClientInfo(CustomLogger customLogger, String clientCode, String langCode, String device, String loginFirst, String param1, String param2) throws Exception {
		ClientInfo clientInfo = new ClientInfo();
		validate(clientInfo, param1, param2, device);

		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String transId = "0";
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_CLIENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			transId = clientDAO.saveTransIdAndGet(jdbcCustomTemplate, customLogger, device, param1, param2);
			if (clientInfo.isResponseStatus() == false) {
				return clientInfo;
			}

			String defaultLang = null;
			if (langCode == null || "".equals(langCode)) {
				defaultLang = clientDAO.getDefaultLangCode(jdbcCustomTemplate, customLogger);
			} else {
				defaultLang = langCode;
			}
			clientInfo.setLangCode(defaultLang);
			if (CoreUtils.isOnline(device)) {
				// populate client data.
				populateClientData(client, clientInfo);
				// populate login page data
				populateLoginPageData(jdbcCustomTemplate, customLogger, clientInfo, clientCode, defaultLang, device, cache);
				if (loginFirst == null || "".equals(loginFirst)) {
					populateResvSysConfig(jdbcCustomTemplate, customLogger, clientInfo, cache);
				} else {
					clientInfo.setLoginFirst(loginFirst);
				}
			} else if (CoreUtils.isIVR(device)) {
				clientInfo.setClientCode(clientCode);
				clientInfo.setDirectAccessNumber(client.getDirectAccessNumber());
				populateLanguageList(jdbcCustomTemplate, customLogger, device, clientInfo, cache);
				populateResvSysConfig(jdbcCustomTemplate, customLogger, clientInfo, cache);
				if("Y".equals(clientInfo.getSchedulerClosed())) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, clientInfo, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.SCHEDULE_CLOSED.getPageValue(),
							defaultLang, cache);
				} else if("Y".equals(clientInfo.getNoFunding())) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, clientInfo, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.NO_FUNDING.getPageValue(),
							defaultLang, cache);
				} else {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, clientInfo, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.WELCOME.getPageValue(),
							defaultLang, cache);
				}
			}
			clientInfo.setToken(clientDAO.saveAndGetToken(jdbcCustomTemplate, customLogger, clientCode, device, cdConfig, param2));
			clientInfo.setTransId(transId);

		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		} finally {
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		}
		return clientInfo;
	}

	private void validate(ClientInfo clientInfo, String param1, String param2, String device) throws Exception {
		if (CoreUtils.isOnline(device)) {
			if (param1 == null || param1.length() == 0) {
				clientInfo.setResponseStatus(false);
				clientInfo.setResponseMessage(PropertyUtils.getValueFromProperties("PARAM1_EMPTY", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
				return;
			}

			if (CoreUtils.validateIP(param1) == false) {
				clientInfo.setResponseStatus(false);
				clientInfo.setResponseMessage(PropertyUtils.getValueFromProperties("PARAM1_ONLINE_VALIDATION_FAIL",
						PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
				return;
			}

			if (param2.length() <= 6) {
				clientInfo.setResponseStatus(false);
				clientInfo.setResponseMessage(PropertyUtils.getValueFromProperties("PARAM2_VALIDATION_FAIL", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
				return;
			}

		} else if (CoreUtils.isIVR(device)) {
			if (param1 == null || param1.length() == 0) {
				clientInfo.setResponseStatus(false);
				clientInfo.setResponseMessage(PropertyUtils.getValueFromProperties("PARAM1_EMPTY", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
				return;
			}

			if (param2 == null || param2.length() == 0) {
				clientInfo.setResponseStatus(false);
				clientInfo.setResponseMessage(PropertyUtils.getValueFromProperties("PARAM2_EMPTY", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
				return;
			}

			if (param2.length() <= 6) {
				clientInfo.setResponseStatus(false);
				clientInfo.setResponseMessage(PropertyUtils.getValueFromProperties("PARAM2_VALIDATION_FAIL", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
				return;
			}
		}

	}

	private void populateLoginPageData(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ClientInfo clientInfo, String clientCode, String langCode, String device,
			boolean cache) throws Exception {
		populateLanguageList(jdbcCustomTemplate, customLogger, device, clientInfo, cache);
		populateDesignTemplate(jdbcCustomTemplate, customLogger, clientInfo, device, cache);
		populatePageContent(jdbcCustomTemplate, customLogger, clientInfo, device, langCode, cache);
		populateButtonNames(jdbcCustomTemplate, customLogger, clientInfo, device, langCode, cache);
	}

	private void populateResvSysConfig(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ClientInfo clientInfo, boolean cache) throws Exception {
		ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
		clientInfo.setLoginFirst(resvSysConfig.getLoginFirst());
		clientInfo.setSchedulerClosed(resvSysConfig.getSchedulerClosed());
		clientInfo.setNoFunding(resvSysConfig.getNoFunding());
	}

	private void populateLanguageList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, ClientInfo clientInfo, boolean cache) throws Exception {
		List<Language> langList = cacheComponent.getLangList(jdbcCustomTemplate, customLogger, false);
		for (Language language : langList) {
			if (CoreUtils.isOnline(device)) {
				language.setLangId(null);
			} else if (CoreUtils.isIVR(device)) {
				language.setLangName(null);
				language.setLinkDisplay(null);
			}
		}
		clientInfo.setLanguageList(langList);
	}

	private void populateDesignTemplate(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ClientInfo clientInfo, String device, boolean cache) throws Exception {

		Map<String, String> designTemplates = cacheComponent.getDesignTemplatesMap(jdbcCustomTemplate, customLogger, device, cache);
		clientInfo.setCssFileName(designTemplates.get(DesignTemplateConstants.CSS_FILENAME.getValue()));
		clientInfo.setLogoFileName(designTemplates.get(DesignTemplateConstants.LOGO_TAG.getValue()));
		clientInfo.setFooterContent(designTemplates.get(DesignTemplateConstants.FOOTER_CONTENT.getValue()));
		clientInfo.setFooterLinks(designTemplates.get(DesignTemplateConstants.FOOTER_LINKS.getValue()));
		clientInfo.setVersion(designTemplates.get(DesignTemplateConstants.VERSION.getValue()));
	}

	private void populatePageContent(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ClientInfo clientInfo, String device, String langCode, boolean cache)
			throws Exception {
		Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);

		clientInfo.setLeftSideLoginHeader(pageContent.get(DisplayPageContentConstants.LEFT_SIDE_LOGIN_HEADER.getValue()));
		clientInfo.setLeftSideResvDetailsHeader(pageContent.get(DisplayPageContentConstants.LEFT_SIDE_RESV_DETAILS_HEADER.getValue()));
		clientInfo.setLeftSideResvVerifyHeader(pageContent.get(DisplayPageContentConstants.LEFT_SIDE_RESV_VERIFY_HEADER.getValue()));
		clientInfo.setLeftSideConfirmHeader(pageContent.get(DisplayPageContentConstants.LEFT_SIDE_CONFIRM_HEADER.getValue()));

		clientInfo.setClosedPageHeaderTextLegend(pageContent.get(DisplayPageContentConstants.CLOSED_PAGE_HEADER_TEXT_LEGEND.getValue()));
		clientInfo.setClosedLandingPageTextLegend(pageContent.get(DisplayPageContentConstants.CLOSED_LANDING_PAGE_TEXT_LEGEND.getValue()));
		clientInfo.setNoFundingPageHeaderTextLegend(pageContent.get(DisplayPageContentConstants.NO_FUNDING_PAGE_HEADER_TEXT_LEGEND.getValue()));
		clientInfo.setNoFundingPageTextLegend(pageContent.get(DisplayPageContentConstants.NO_FUNDING_PAGE_TEXT_LEGEND.getValue()));
		clientInfo.setLandingPageText(pageContent.get(DisplayPageContentConstants.LANDING_PAGE_CONTENT.getValue()));
		clientInfo.setViewExistingResvLabel(pageContent.get(DisplayPageContentConstants.VIEW_EXISTING_RESV_LABEL.getValue()));
	}

	private void populateButtonNames(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, ClientInfo clientInfo, String device, String langCode, boolean cache)
			throws Exception {
		Map<String, String> buttonsNames = cacheComponent.getDisplayButtonsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
		clientInfo.setLogoutBtn(buttonsNames.get(DisplayButtonsConstants.LOGOUT.getValue()));
	}

	private void populateClientData(Client client, ClientInfo clientInfo) {
		clientInfo.setClientCode(client.getClientCode());
		clientInfo.setClientName(client.getClientName());
		clientInfo.setWebsite(client.getWebsite());
		clientInfo.setAddress(client.getAddress());
		clientInfo.setAddress2(client.getAddress2());
		clientInfo.setCity(client.getCity());
		clientInfo.setState(client.getState());
		clientInfo.setZip(client.getZip() == null ? "" : client.getZip());
		clientInfo.setCountry(client.getCountry());
	}

	@Override
	public LoginInfoResponse getLoginInfo(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId,String action) throws Exception {
		LoginInfoResponse loginInfoResponse = new LoginInfoResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_LOGIN_INFO.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, loginInfoResponse, device, langCode, token, cdConfig, cache);
			if (loginInfoResponse.isResponseStatus() == false) {
				return loginInfoResponse;
			}
			List<LoginField> loginFieldList = cacheComponent.getLoginParamsList(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if (CoreUtils.isOnline(device)) {
				loginInfoResponse.setLoginFieldList(loginFieldList);
				Map<String, String> buttonsNames = cacheComponent.getDisplayButtonsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				loginInfoResponse.setNextBtn(buttonsNames.get(DisplayButtonsConstants.NEXT.getValue()));
				loginInfoResponse.setBackBtn(buttonsNames.get(DisplayButtonsConstants.BACK.getValue()));
			} else if (CoreUtils.isAdmin(device)) {
				loginInfoResponse.setLoginFieldList(loginFieldList);
			} else if (CoreUtils.isIVR(device)) {
				StringBuilder submitFields = new StringBuilder("");
				for (LoginField loginField : loginFieldList) {
					submitFields.append(loginField.getFieldName()).append(" ");
				}
				int length = submitFields.length();
				if (length > 0) {
					submitFields.setLength(length - 1);
				}
				loginInfoResponse.setSubmitFieldList(submitFields.toString());
				if("login".equalsIgnoreCase(action)) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, loginInfoResponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.LOGIN.getPageValue(),
					langCode, cache);
				} else if("cancel".equalsIgnoreCase(action)) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, loginInfoResponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.LOGIN_CANCEL.getPageValue(),
							langCode, cache);
				}
			}
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		} finally {
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		}
		return loginInfoResponse;
	}

	@Override
	public AuthResponse authenticateCustomer(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String loginParams)
			throws Exception {
		AuthResponse authResponse = new AuthResponse();
		Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
		setTokenValidity(jdbcCustomTemplate, customLogger, authResponse, device, langCode, token, cdConfig, cache);
		if (authResponse.isResponseStatus() == false) {
			return authResponse;
		}
		ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
		ErrorConfig errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.AUTH_CUSTOMER.getValue(), cache);
		try {
			if (authResponse.isResponseStatus() == false) {
				return authResponse;
			}

			List<LoginField> loginParamList = cacheComponent.getLoginParamsList(jdbcCustomTemplate, customLogger, device, langCode, true);
			List<String> inputArray = Arrays.asList(loginParams.split("\\|", -1));
			int inputArraySize = inputArray.size();

			if (inputArraySize != loginParamList.size()) {
				throw new Exception("Invalid login parameters passed from device: " + device);
			}

			Map<String, String> loginParamsMap = new HashMap<String, String>();
			LoginField loginField = null;
			int loginParamListSize = loginParamList.size();
			for (int i = 0; i < loginParamListSize; i++) {
				loginField = loginParamList.get(i);
				loginParamsMap.put(loginField.getFieldName(), inputArray.get(i));
			}

			// populate authenticate fields map.
			Map<String, String> authMap = getRequiredMap(loginParamsMap, loginParamList);
			Map<String, LoginField> loginFieldMap = getLoginFieldsByFilter(loginParamList, "authenticate");
			clientDAO.authenticateCustomer(jdbcCustomTemplate, customLogger, device, authMap, loginFieldMap, authResponse);
			boolean noLogin = "N".equals(resvSysConfig.getEnforceLogin());
			if (CoreUtils.isAdmin(device)) {
				noLogin = true;
			}
			Customer customer = null;
			loginFieldMap = getLoginFieldsByFilter(loginParamList, "both");
			if (authResponse.isAuthSuccess() == false && noLogin) {
				customer = new Customer();
				
				if(loginFieldMap != null && loginFieldMap.size() > 0) {
					populateCustomer(customLogger, device, customer, loginParamsMap,loginFieldMap);
				}
				if (customer.getFirstName() == null) {
					customer.setFirstName("");
				}
				if (customer.getLastName() == null) {
					customer.setLastName("");
				}
				long customerId = clientDAO.saveCustomer(jdbcCustomTemplate, customLogger, customer, cdConfig);
				authResponse.setCustomerId(customerId);
			}

			if (authResponse.getCustomerId() <= 0) {
				authResponse.setAuthSuccess(false);
				authResponse.setAuthMessage(PropertyUtils.getValueFromProperties("CUSTOMER_AUTH_FAILURE", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
			} else {
				customer = clientDAO.getCustomer(jdbcCustomTemplate, customLogger, authResponse.getCustomerId());
				if(loginFieldMap != null && loginFieldMap.size() > 0) {
					populateCustomer(customLogger, device, customer, loginParamsMap,loginFieldMap);
				}
				
				if (CoreUtils.isIVR(device)) {
					customer.setLastName("Customer-" + authResponse.getCustomerId());
				}
				clientDAO.updateCustomer(jdbcCustomTemplate, customLogger, customer, cdConfig);
				clientDAO.updateTokenCustomerId(jdbcCustomTemplate, customLogger, clientCode, token, device, Long.valueOf(customer.getCustomerId()), cdConfig);
				authResponse.setFirstName(CoreUtils.capitalizeString(customer.getFirstName()));
				if (CoreUtils.isOnline(device)) {
					authResponse.setLastName(CoreUtils.capitalizeString(customer.getLastName()));
				} else if (CoreUtils.isIVR(device)) {
					authResponse.setLastName(customer.getLastName());
				}
				authResponse.setAuthSuccess(true);
			}

			if (CoreUtils.isOnline(device)) {
				Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				authResponse.setWelcomeHeader(pageContent.get(DisplayPageContentConstants.LEFT_SIDE_TOP_WELCOME_HEADER.getValue()));
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			authResponse.setAuthSuccess(false);
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return authResponse;
	}
	
	@Override
	public AuthResponse authenticateCustomerForCancel(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String loginParams)
			throws Exception {
		AuthResponse authResponse = new AuthResponse();
		Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
		boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
		ClientDeploymentConfig cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, true);
		JdbcCustomTemplate jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
		setTokenValidity(jdbcCustomTemplate, customLogger, authResponse, device, langCode, token, cdConfig, cache);
		if (authResponse.isResponseStatus() == false) {
			return authResponse;
		}
		ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
		ErrorConfig errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.AUTH_CUSTOMER.getValue(), cache);
		try {
			if (authResponse.isResponseStatus() == false) {
				return authResponse;
			}

			List<LoginField> loginParamList = cacheComponent.getLoginParamsList(jdbcCustomTemplate, customLogger, device, langCode, true);
			List<String> inputArray = Arrays.asList(loginParams.split("\\|", -1));
			int inputArraySize = inputArray.size();

			if (inputArraySize != loginParamList.size()) {
				throw new Exception("Invalid login parameters passed from device: " + device);
			}

			Map<String, String> loginParamsMap = new HashMap<String, String>();
			LoginField loginField = null;
			int loginParamListSize = loginParamList.size();
			for (int i = 0; i < loginParamListSize; i++) {
				loginField = loginParamList.get(i);
				loginParamsMap.put(loginField.getFieldName(), inputArray.get(i));
			}

			// populate authenticate fields map.
			Map<String, String> authMap = getRequiredMap(loginParamsMap, loginParamList);
			Map<String, LoginField> loginFieldMap = getLoginFieldsByFilter(loginParamList, "authenticate");
			clientDAO.authenticateCustomer(jdbcCustomTemplate, customLogger, device, authMap, loginFieldMap, authResponse);
			
			Customer customer = null;
			if (authResponse.getCustomerId() <= 0) {
				authResponse.setAuthSuccess(false);
				authResponse.setAuthMessage(PropertyUtils.getValueFromProperties("CUSTOMER_AUTH_FAILURE", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
			} else {
				customer = clientDAO.getCustomer(jdbcCustomTemplate, customLogger, authResponse.getCustomerId());
				authResponse.setFirstName(CoreUtils.capitalizeString(customer.getFirstName()));
				if (CoreUtils.isOnline(device)) {
					authResponse.setLastName(CoreUtils.capitalizeString(customer.getLastName()));
				} else if (CoreUtils.isIVR(device)) {
					authResponse.setLastName(customer.getLastName());
				}
				authResponse.setAuthSuccess(true);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			authResponse.setAuthSuccess(false);
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return authResponse;
	}

	private Map<String, LoginField> getLoginFieldsByFilter(List<LoginField> loginParamList, String loginType) {
		Map<String, LoginField> loginFieldMap = new HashMap<String, LoginField>();
		for (LoginField loginField : loginParamList) {
			if(loginType.equals("both")) {
				loginFieldMap.put(loginField.getFieldName(), loginField);
			} else if (loginType.equals(loginField.getLoginType())) {
				loginFieldMap.put(loginField.getFieldName(), loginField);
			}
		}
		return loginFieldMap;
	}

	private Map<String, String> getRequiredMap(Map<String, String> loginParamsMap, List<LoginField> loginParamList) {
		Map<String, String> authMap = new HashMap<String, String>();
		for (LoginField loginField : loginParamList) {
			if ("authenticate".equals(loginField.getLoginType())) {
				authMap.put(loginField.getFieldName(), loginParamsMap.get(loginField.getFieldName()) == null ? "" : loginParamsMap.get(loginField.getFieldName()));
			} 
		}
		return authMap;
	}

	private void populateCustomer(CustomLogger customLogger, String device, Customer customer, Map<String, String> loginParamsMap, Map<String, LoginField> loginFieldMap) {
		LoginField loginField = null;
		for (String loginParamColumn : loginParamsMap.keySet()) {
			String paramValue = loginParamsMap.get(loginParamColumn);
			if (CommonResvDeskConstants.IVRAUDIO.getValue().equals(device)) {
				customer.setFirstName("NEW");
				customer.setLastName("");
			} else {
				if ("first_name".equalsIgnoreCase(loginParamColumn)) {
					customer.setFirstName(CoreUtils.capitalizeString(paramValue));
				} else if ("last_name".equalsIgnoreCase(loginParamColumn)) {
					customer.setLastName(CoreUtils.capitalizeString(paramValue));
				} else if ("middle_name".equalsIgnoreCase(loginParamColumn)) {
					customer.setMiddleName(CoreUtils.capitalizeString(paramValue));
				}
			}
			if("attrib1".equals(loginParamColumn)) {
				customer.setAttrib1(paramValue);
			} if("attrib2".equals(loginParamColumn)) {
				customer.setAttrib2(paramValue);
			} if("attrib3".equals(loginParamColumn)) {
				customer.setAttrib3(paramValue);
			} else if ("contact_phone".equalsIgnoreCase(loginParamColumn)) {
				customer.setContactPhone(paramValue);
			} else if ("home_phone".equalsIgnoreCase(loginParamColumn)) {
				customer.setHomePhone(paramValue);
			} else if ("work_phone".equalsIgnoreCase(loginParamColumn)) {
				customer.setWorkPhone(paramValue);
			} else if ("dob".equalsIgnoreCase(loginParamColumn)) {
				if(paramValue !=null && !"".equals(paramValue)) {
					try {
						paramValue = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(paramValue);
					} catch(ParseException e) {
						customLogger.error("DOB formating failed."+e,e);
					}
					customer.setDob(paramValue);
				}
			}else if ("account_number".equalsIgnoreCase(loginParamColumn)) {
				loginField = loginFieldMap.get(loginParamColumn);
				if(loginField != null) {
					int storageSize = loginField.getStorageSize();
					String storageType = loginField.getStorageType();
					int paramValueLength = paramValue.length();
					if(storageSize > 0 && paramValueLength >= storageSize) {
						if("last".equalsIgnoreCase(storageType)) {
							paramValue = paramValue.substring(paramValueLength - storageSize);
						} else if("first".equalsIgnoreCase(storageType)) {
							paramValue = paramValue.substring(0,storageSize);
						} else if("prefix0".equalsIgnoreCase(storageType)) {
							paramValue = "0"+paramValue;
						} else if("postfix0".equalsIgnoreCase(storageType)) {
							paramValue = paramValue+"0";
						}
					}
				}
				customer.setAccountNumber(paramValue == null ? "0" : paramValue);
			} else if ("email".equalsIgnoreCase(loginParamColumn)) {
				customer.setEmail(paramValue);
			}
		}

		if ("0".equals(customer.getAccountNumber()) || customer.getAccountNumber() == null) {
			customer.setAccountNumber(customer.getHomePhone());
		}

		if (customer.getAccountNumber() == null) {
			customer.setAccountNumber(customer.getContactPhone());
		}
	}

	@Override
	public ResvDetailsResponse getResvDetailsSelectionInfo(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId)
			throws Exception {
		ResvDetailsResponse resvDetailsResponse = new ResvDetailsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_RESV_DETAILS_SEL_INFO.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, resvDetailsResponse, device, langCode, token, cdConfig, cache);
			if (resvDetailsResponse.isResponseStatus() == false) {
				return resvDetailsResponse;
			}
			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			Map<String, String> buttonsNames = cacheComponent.getDisplayButtonsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);

			resvDetailsResponse.setDisplayCompany(resvSysConfig.getDisplayCompany());
			resvDetailsResponse.setDisplayProcedure(resvSysConfig.getDisplayProcedure());
			resvDetailsResponse.setDisplayLocation(resvSysConfig.getDisplayLocation());
			resvDetailsResponse.setDisplayDepartment(resvSysConfig.getDisplayDepartment());
			resvDetailsResponse.setDisplayEvent(resvSysConfig.getDisplayEvent());
			resvDetailsResponse.setDisplaySeat(resvSysConfig.getDisplaySeat());

			if (CoreUtils.isIVR(device)) {
				return resvDetailsResponse;
			} else if (CoreUtils.isOnline(device) || CoreUtils.isMobile(device)) {
				resvDetailsResponse.setSelectDefaultCompanyId("-1");
				resvDetailsResponse.setSelectDefaultProcedureId("-1");
				resvDetailsResponse.setSelectDefaultLocationId("-1");
				resvDetailsResponse.setSelectDefaultDepartmentId("-1");
				resvDetailsResponse.setSelectDefaultEventId("-1");
				resvDetailsResponse.setSelectDefaultTimetId("-1");
				resvDetailsResponse.setSelectDefaultSeatId("-1");

				resvDetailsResponse.setDisplayCompanyLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_COMPANY.getValue()));
				resvDetailsResponse.setSelectCompanyLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_COMPANY_LABEL.getValue()));

				resvDetailsResponse.setDisplayProcedureLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_PROCEDURE.getValue()));
				resvDetailsResponse.setSelectProcedureLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_PROCEDURE_LABEL.getValue()));

				resvDetailsResponse.setDisplayLocationLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_LOCATION.getValue()));
				resvDetailsResponse.setSelectLocationLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_LOCATION_LABEL.getValue()));

				resvDetailsResponse.setDisplayDepartmentLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DEPARTMENT.getValue()));
				resvDetailsResponse.setSelectDepartmentLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_DEPARTMENT_LABEL.getValue()));

				resvDetailsResponse.setDisplayEventLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_EVENT.getValue()));
				resvDetailsResponse.setSelectEventLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_EVENT_LABEL.getValue()));

				resvDetailsResponse.setDateLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DATE.getValue()));
				resvDetailsResponse.setSelectDateLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_DATE_LABEL.getValue()));

				resvDetailsResponse.setTimeLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_TIME.getValue()));
				resvDetailsResponse.setSelectTimeLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_TIME_LABEL.getValue()));

				resvDetailsResponse.setDisplaySeatLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SEAT.getValue()));
				resvDetailsResponse.setSelectSeatLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_SEAT_LABEL.getValue()));

				resvDetailsResponse.setNextBtn(buttonsNames.get(DisplayButtonsConstants.NEXT.getValue()));
				resvDetailsResponse.setBackBtn(buttonsNames.get(DisplayButtonsConstants.BACK.getValue()));
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return resvDetailsResponse;
	}

	@Override
	public CompanyListResponse getCompanyList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId) throws Exception {
		CompanyListResponse companyListRes = new CompanyListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_COMPANY_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, companyListRes, device, langCode, token, cdConfig, cache);
			if (companyListRes.isResponseStatus() == false) {
				return companyListRes;
			}
			List<Options> companyOptions = new ArrayList<Options>();
			clientDAO.getCompanyList(jdbcCustomTemplate, customLogger, device, companyOptions);

			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if (CoreUtils.isOnline(device)) {
				Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				companyListRes.setSelectCompanyLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_COMPANY_LABEL.getValue()));
				companyListRes.setSelectDefaultCompanyId("-1");
			} else if (CoreUtils.isIVR(device)) {
				String pageName = (companyOptions.size() == 1) ? IVRVxmlConstants.SINGLE_COMPANY.getPageValue() : IVRVxmlConstants.COMPANY.getPageValue();
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, companyListRes, IVRVxmlConstants.APP_CODE.getPageValue(), pageName, langCode, cache);
			}
			// populate drop down values from alias.
			clientDAO.populateAlias(aliasMap, companyOptions);

			companyListRes.setCompanyList(companyOptions);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return companyListRes;
	}

	@Override
	public ProcedureListResponse getProcedureList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String companyId)
			throws Exception {
		ProcedureListResponse procListRes = new ProcedureListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_PROCEDURE_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, procListRes, device, langCode, token, cdConfig, cache);
			if (procListRes.isResponseStatus() == false) {
				return procListRes;
			}
			List<Options> procedureOptions = new ArrayList<Options>();
			clientDAO.getProcedureList(jdbcCustomTemplate, customLogger, companyId, device, procedureOptions);

			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if (CoreUtils.isOnline(device)) {
				Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				procListRes.setSelectProcedureLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_PROCEDURE_LABEL.getValue()));
				procListRes.setSelectDefaultProcedureId("-1");
			} else if (CoreUtils.isIVR(device)) {
				String pageName = (procedureOptions.size() == 1) ? IVRVxmlConstants.SINGLE_PROCEDURE.getPageValue() : IVRVxmlConstants.PROCEDURE.getPageValue();
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, procListRes, IVRVxmlConstants.APP_CODE.getPageValue(), pageName, langCode, cache);
			}

			// populate drop down values from alias.
			clientDAO.populateAlias(aliasMap, procedureOptions);

			procListRes.setProcedureList(procedureOptions);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return procListRes;
	}

	@Override
	public LocationListResponse getLocationList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String procedureId)
			throws Exception {
		LocationListResponse locationListRes = new LocationListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_LOCATION_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, locationListRes, device, langCode, token, cdConfig, cache);
			if (locationListRes.isResponseStatus() == false) {
				return locationListRes;
			}

			List<Options> locationOptions = new ArrayList<Options>();
			clientDAO.getLocationList(jdbcCustomTemplate, customLogger, device, Long.valueOf(procedureId), locationOptions);
			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);

			if (CoreUtils.isOnline(device)) {
				Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				locationListRes.setSelectLocationLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_LOCATION_LABEL.getValue()));
				locationListRes.setSelectDefaultLocationId("-1");
			} else if (CoreUtils.isIVR(device)) {
				String pageName = (locationOptions.size() == 1) ? IVRVxmlConstants.SINGLE_LOCATION.getPageValue() : IVRVxmlConstants.LOCATION.getPageValue();
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, locationListRes, IVRVxmlConstants.APP_CODE.getPageValue(), pageName, langCode, cache);
			}

			// populate drop down values from alias.
			clientDAO.populateAlias(aliasMap, locationOptions);

			locationListRes.setLocationList(locationOptions);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return locationListRes;
	}

	@Override
	public DepartmentListResponse getDepartmentList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String locationId)
			throws Exception {
		DepartmentListResponse departmentListRes = new DepartmentListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_DEPARTMENT_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, departmentListRes, device, langCode, token, cdConfig, cache);
			if (departmentListRes.isResponseStatus() == false) {
				return departmentListRes;
			}
			List<Options> departmentOptions = new ArrayList<Options>();
			clientDAO.getDepartmentList(jdbcCustomTemplate, customLogger, device, Integer.valueOf(locationId), departmentOptions);
			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);

			if (CoreUtils.isOnline(device)) {
				Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				departmentListRes.setSelectDepartmentLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_DEPARTMENT_LABEL.getValue()));
				departmentListRes.setSelectDefaultDepartmentId("-1");
			} else if (CoreUtils.isIVR(device)) {
				String pageName = (departmentOptions.size() == 1) ? IVRVxmlConstants.SINGLE_DEPARTMENT.getPageValue() : IVRVxmlConstants.DEPARTMENT.getPageValue();
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, departmentListRes, IVRVxmlConstants.APP_CODE.getPageValue(), pageName, langCode, cache);
			}

			// populate drop down values from alias.
			clientDAO.populateAlias(aliasMap, departmentOptions);

			departmentListRes.setDepartmentList(departmentOptions);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return departmentListRes;
	}

	@Override
	public EventListResponse getEventList(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String departmentId,
			String locationId) throws Exception {
		EventListResponse eventListRes = new EventListResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_EVENT_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, eventListRes, device, langCode, token, cdConfig, cache);
			if (eventListRes.isResponseStatus() == false) {
				return eventListRes;
			}
			List<Options> eventOptions = new ArrayList<Options>();
			clientDAO.getEventList(jdbcCustomTemplate, customLogger, device, Integer.valueOf(departmentId), Integer.valueOf(locationId), eventOptions);
			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);

			if (CoreUtils.isOnline(device)) {
				Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				eventListRes.setSelectEventLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SELECT_EVENT_LABEL.getValue()));
				eventListRes.setSelectDefaultEventId("-1");
			} else if (CoreUtils.isIVR(device)) {
				String pageName = (eventOptions.size() == 1) ? IVRVxmlConstants.SINGLE_EVENT.getPageValue() : IVRVxmlConstants.EVENT.getPageValue();
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, eventListRes, IVRVxmlConstants.APP_CODE.getPageValue(), pageName, langCode, cache);
			}

			// populate drop down values from alias.
			clientDAO.populateAlias(aliasMap, eventOptions);

			eventListRes.setEventList(eventOptions);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventListRes;
	}

	@Override
	public EventDatesResponse getEventDates(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String eventId,
			String locationId) throws Exception {
		EventDatesResponse eventDatesRes = new EventDatesResponse();
		Map<String, EventDate> eventDateMap = new LinkedHashMap<String, EventDate>();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_EVENT_DATES_LIST.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, eventDatesRes, device, langCode, token, cdConfig, cache);
			if (eventDatesRes.isResponseStatus() == false) {
				return eventDatesRes;
			}
			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			clientDAO.getEventDates(jdbcCustomTemplate, customLogger, device, eventId, locationId, resvSysConfig, cdConfig, eventDateMap);
			if (eventDateMap.isEmpty()) {
				eventDatesRes.setResponseMessage(PropertyUtils.getValueFromProperties("NO_AVAIL_DATE_FOR_EVENT", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
			}
			
			if(CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
				clientDAO.getBookedDates(jdbcCustomTemplate, customLogger, device, eventId, locationId, eventDateMap);
			}
			
			if (CoreUtils.isIVR(device)) {
				int size = eventDateMap.size();
				if (size == 1) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, eventDatesRes, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.SINGLE_DATE.getPageValue(),
							langCode, cache);
				} else if (size == 2) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, eventDatesRes, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.TWO_DATE.getPageValue(),
							langCode, cache);
				} else if (size >= 3) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, eventDatesRes, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.MULTI_DATE.getPageValue(),
							langCode, cache);
				} else {
					eventDatesRes.setVxml("");
				}
			}
			eventDatesRes.setEventDateMap(eventDateMap);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventDatesRes;
	}

	@Override
	public EventTimesResponse getEventTimes(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String eventId,
			String locationId, String date) throws Exception {
		EventTimesResponse eventTimesResponse = new EventTimesResponse();
		String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
		Map<String, String> availableTimMap = new LinkedHashMap<String, String>();
		Map<String, String> displayAvailableTimMap = new LinkedHashMap<String, String>();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_EVENT_TIMES.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, eventTimesResponse, device, langCode, token, cdConfig, cache);
			if (eventTimesResponse.isResponseStatus() == false) {
				return eventTimesResponse;
			}

			clientDAO.getEventTimes(jdbcCustomTemplate, customLogger, eventId, locationId, dateYYYYMMDD, availableTimMap);
			clientDAO.getEventDisplayTimes(jdbcCustomTemplate, customLogger, displayAvailableTimMap);
			
			if (availableTimMap.isEmpty()) {
				eventTimesResponse.setResponseMessage(PropertyUtils.getValueFromProperties("NO_SEAT_AVAIL_TIMES_FOR_DATE",
						PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
			}

			if (CoreUtils.isIVR(device)) {
				ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
				eventTimesResponse.setIvrTimeBatchSize(resvSysConfig.getIvrTimeBatchSize());
				if (availableTimMap.size() == 1) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, eventTimesResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
							IVRVxmlConstants.SINGLE_TIME.getPageValue(), langCode, cache);
				} else {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, eventTimesResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
							IVRVxmlConstants.MULTI_TIME.getPageValue(), langCode, cache);
				}
			}
			eventTimesResponse.setAvailableTimes(availableTimMap);
			eventTimesResponse.setDisplayAvailableTimes(displayAvailableTimMap);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventTimesResponse;
	}

	@Override
	public SeatResponse getEventSeats(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String eventDateTimeId)
			throws Exception {
		SeatResponse seatResponse = new SeatResponse();

		List<Seat> seatList = new ArrayList<Seat>();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_EVENT_SEATS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, seatResponse, device, langCode, token, cdConfig, cache);
			if (seatResponse.isResponseStatus() == false) {
				return seatResponse;
			}

			clientDAO.getEventSeats(jdbcCustomTemplate, customLogger, device, eventDateTimeId, seatList);
			if (CoreUtils.isIVR(device)) {
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, seatResponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.SEAT.getPageValue(), langCode,
						cache);
			}

			if (seatList.isEmpty()) {
				ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
				if ("Y".equals(resvSysConfig.getDisplaySeat())) {
					seatResponse.setResponseMessage(PropertyUtils.getValueFromProperties("NO_SEAT_AVAIL_FOR_EVENT_DATE_TIME",
							PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
				}
			}

			seatResponse.setSeats(seatList);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return seatResponse;
	}

	public HoldResvResponse holdReservation(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String companyId,
			String procedureId, String locationId, String departmentId, String eventId, String eventDateTimeId, String seatId, String customerId) throws Exception {

		HoldResvResponse holdResponse = new HoldResvResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.HOLD_RESERVATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, holdResponse, device, langCode, token, cdConfig, cache);
			if (holdResponse.isResponseStatus() == false) {
				return holdResponse;
			}
			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			clientDAO.holdReservation(jdbcCustomTemplate, customLogger, device, transId, companyId, procedureId, locationId, departmentId, eventId, eventDateTimeId, seatId,
					customerId, resvSysConfig, cdConfig, holdResponse);
			if ((CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) && holdResponse.isResponseStatus() == false) {
				holdResponse.setResponseMessage(PropertyUtils.getValueFromProperties("NO_AVAIL_SEAT", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
			} else if (CoreUtils.isIVR(device)) {
				holdResponse.setRecordMsg(resvSysConfig.getRecordMsg());
				if (holdResponse.isResponseStatus() == false) {
					if (IVRVxmlConstants.ONE_SEAT_NOT_AVAILABLE.getPageKey().equals(holdResponse.getResponseMessage())) {
						cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, holdResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
								IVRVxmlConstants.ONE_SEAT_NOT_AVAILABLE.getPageValue(), langCode, cache);
						holdResponse.setResponseMessage(PropertyUtils.getValueFromProperties("NO_AVAIL_SEAT", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
					} else if (IVRVxmlConstants.ALL_SEATS_NOT_AVAILABLE.getPageKey().equals(holdResponse.getResponseMessage())) {
						holdResponse.setResponseMessage(PropertyUtils.getValueFromProperties("NO_AVAIL_SEAT_FOR_EVENT_DATE_TIME",
								PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
						cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, holdResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
								IVRVxmlConstants.ALL_SEATS_NOT_AVAILABLE.getPageValue(), langCode, cache);
					}
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return holdResponse;
	}

	public VerifyResvResponse getVerifyReservationDetails(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId,
			String scheduleId, String customerId, String loginFirst) throws Exception {
		VerifyResvResponse verifyResvResponse = new VerifyResvResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_VERIFY_RESERVATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, verifyResvResponse, device, langCode, token, cdConfig, cache);
			if (verifyResvResponse.isResponseStatus() == false) {
				return verifyResvResponse;
			}
			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			clientDAO.getVerifyReservationDetails(jdbcCustomTemplate, customLogger, device, langCode, transId, scheduleId, customerId, loginFirst, resvSysConfig, cdConfig,
					verifyResvResponse);

			// populate alias names.
			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			clientDAO.populateAlias(aliasMap, verifyResvResponse, device);

			Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if (CoreUtils.isOnline(device)) {
				Map<String, String> buttonsNames = cacheComponent.getDisplayButtonsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				verifyResvResponse.setNextBtn(buttonsNames.get(DisplayButtonsConstants.NEXT.getValue()));
				verifyResvResponse.setBackBtn(buttonsNames.get(DisplayButtonsConstants.BACK.getValue()));
			} else if (CoreUtils.isAdmin(device)) {

			}
			verifyResvResponse.setDisplayCompanyLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_COMPANY.getValue()));
			verifyResvResponse.setDisplayProcedureLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_PROCEDURE.getValue()));
			verifyResvResponse.setDisplayLocationLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_LOCATION.getValue()));
			verifyResvResponse.setDisplayDepartmentLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DEPARTMENT.getValue()));
			verifyResvResponse.setDisplayEventLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_EVENT.getValue()));
			verifyResvResponse.setDateLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DATE.getValue()));
			verifyResvResponse.setTimeLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_TIME.getValue()));
			verifyResvResponse.setDisplaySeatLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SEAT.getValue()));
			verifyResvResponse.setDisplayCompany(resvSysConfig.getDisplayCompany());
			verifyResvResponse.setDisplayProcedure(resvSysConfig.getDisplayProcedure());
			verifyResvResponse.setDisplayLocation(resvSysConfig.getDisplayLocation());
			verifyResvResponse.setDisplayDepartment(resvSysConfig.getDisplayDepartment());
			verifyResvResponse.setDisplayEvent(resvSysConfig.getDisplayEvent());
			verifyResvResponse.setDisplaySeat(resvSysConfig.getDisplaySeat());

			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return verifyResvResponse;
	}

	public ConfirmResvResponse confirmReservation(CustomLogger customLogger, String clientCode, String device, String langCode, String token, String transId, String scheduleId,
			String customerId, String comment) throws Exception {
		ConfirmResvResponse confirmResponse = new ConfirmResvResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.CONFIRM_RESERVATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, confirmResponse, device, langCode, token, cdConfig, cache);
			if (confirmResponse.isResponseStatus() == false) {
				return confirmResponse;
			}

			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);

			customLogger.debug("scheduleId:::" + scheduleId);
			clientDAO.confirmReservation(jdbcCustomTemplate, customLogger, device, langCode, token, transId, scheduleId, customerId, comment, resvSysConfig, cdConfig,
					confirmResponse);
			if (CoreUtils.isIVR(device)) {
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, confirmResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
						IVRVxmlConstants.CONFIRM_RESERVATION.getPageValue(), langCode, cache);
			}

			Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if (CoreUtils.isOnline(device)) {
				Map<String, String> buttonsNames = cacheComponent.getDisplayButtonsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				confirmResponse.setPrintBtn(buttonsNames.get(DisplayButtonsConstants.PRINT.getValue()));
			} else if (CoreUtils.isAdmin(device)) {

			}

			if (labelsMap != null){
				confirmResponse.setDisplayCompanyLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_COMPANY.getValue()));
				confirmResponse.setDisplayProcedureLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_PROCEDURE.getValue()));
				confirmResponse.setDisplayLocationLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_LOCATION.getValue()));
				confirmResponse.setDisplayDepartmentLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DEPARTMENT.getValue()));
				confirmResponse.setDisplayEventLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_EVENT.getValue()));
				confirmResponse.setDateLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DATE.getValue()));
				confirmResponse.setTimeLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_TIME.getValue()));
				confirmResponse.setDisplaySeatLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SEAT.getValue()));
				confirmResponse.setConfirmationNoLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_CONFIRMATION_NO.getValue()));
				confirmResponse.setNameLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_CUST_NAME.getValue()));
			}

			confirmResponse.setDisplayCompany(resvSysConfig.getDisplayCompany());
			confirmResponse.setDisplayProcedure(resvSysConfig.getDisplayProcedure());
			confirmResponse.setDisplayLocation(resvSysConfig.getDisplayLocation());
			confirmResponse.setDisplayDepartment(resvSysConfig.getDisplayDepartment());
			confirmResponse.setDisplayEvent(resvSysConfig.getDisplayEvent());
			confirmResponse.setDisplaySeat(resvSysConfig.getDisplaySeat());

			// populate alias names.
			Map<String, String> aliasMap = cacheComponent.getDisplayAliasMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			clientDAO.populateAlias(aliasMap, confirmResponse, device);

			if ("Y".equals(resvSysConfig.getSendConfirmEmail())) {
				String email = confirmResponse.getEmail();
				if (email != null && email.length() > 0) {
					EmailRequest emailRequest = new EmailRequest();
					emailRequest.setClientName(client.getClientName());
					emailRequest.setEventName(confirmResponse.getEvent());
					emailRequest.setEventDuration(confirmResponse.getDuration());
					emailRequest.setProcedureName(confirmResponse.getProcedure());
					emailRequest.setLocationName(confirmResponse.getLocation());
					emailRequest.setCompanyName(confirmResponse.getCompanyName());
					emailRequest.setDepartmentName(confirmResponse.getDepartment());
					emailRequest.setSeatNumber(confirmResponse.getSeatNumber());
					emailRequest.setScheduleId(scheduleId);
					emailRequest.setDate(confirmResponse.getReservationDate());
					emailRequest.setTime(confirmResponse.getReservationTime());
					emailRequest.setDateTime(emailRequest.getDate() + " " + emailRequest.getTime());
					emailRequest.setToAddress(email);
					emailRequest.setClientResvLink(client.getApptLink());
					emailRequest.setClientAddress(client.getAddress());
					emailRequest.setClientWebsite(client.getWebsite());
					emailRequest.setFirstName(confirmResponse.getFirstName());
					emailRequest.setLastName(confirmResponse.getLastName());
					emailRequest.setConfNumber(confirmResponse.getConfNumber());
					emailRequest.setEmailType(EmailConstants.RESERVATION_CONFIRM_EMAIL.getValue());
					emailComponent.prepareEmailRequest(jdbcCustomTemplate, customLogger, langCode, device, token, transId,
							EmailTemplateConstants.EMAIL_RESV_CONFIRM_SUBJECT.getValue(), EmailTemplateConstants.EMAIL_RESV_CONFIRM_BODY.getValue(), cdConfig, resvSysConfig,
							emailRequest, cache);
				} else {
					customLogger.error("EmailId is not present, so email not sent");
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return confirmResponse;
	}

	private void setTokenValidity(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, BaseResponse baseResponse, String device, String langCode, String token,
			ClientDeploymentConfig cdConfig, boolean cache) throws Exception {

		boolean isTokenValid = clientDAO.isValidToken(jdbcCustomTemplate, customLogger, jdbcCustomTemplate.getClientCode(), device, token, cdConfig);

		String tokenCheck = PropertyUtils.getValueFromProperties("TOKEN_CHECK", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
		if ("0".equals(tokenCheck)) {
			baseResponse.setResponseStatus(true);
			return;
		}

		if (isTokenValid) {
			baseResponse.setResponseStatus(true);
		} else {
			baseResponse.setResponseStatus(false);
			baseResponse.setResponseMessage(PropertyUtils.getValueFromProperties("TOKEN_EXPIRY_MESSAGE", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
		}
		if (CoreUtils.isIVR(device)) {
			cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseResponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.TOKEN_EXPIRY.getPageValue(),
					langCode, cache);
		}

	}

	private void setTokenValidity(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, BaseResponse baseResponse, String device, String langCode, String token,
			ClientDeploymentConfig cdConfig, String customerId, boolean cache) throws Exception {

		boolean isTokenValid = clientDAO.isValidToken(jdbcCustomTemplate, customLogger, jdbcCustomTemplate.getClientCode(), device, token, cdConfig, customerId);
		String tokenCheck = PropertyUtils.getValueFromProperties("TOKEN_CHECK", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
		if ("0".equals(tokenCheck)) {
			baseResponse.setResponseStatus(true);
			return;
		}

		if (isTokenValid) {
			baseResponse.setResponseStatus(true);
		} else {
			baseResponse.setResponseStatus(false);
			baseResponse.setResponseMessage(PropertyUtils.getValueFromProperties("TOKEN_EXPIRY_MESSAGE", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName()));
		}
		if (CoreUtils.isIVR(device)) {
			cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseResponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.TOKEN_EXPIRY.getPageValue(),
					langCode, cache);
		}

	}

	@Override
	public BaseResponse releaseHoldEventTime(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String scheduleId)
			throws Exception {
		BaseResponse baseResponse = new BaseResponse();

		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.CANCEL_RESERVATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseResponse, device, langCode, token, cdConfig, cache);
			if (baseResponse.isResponseStatus() == false) {
				return baseResponse;
			}
			clientDAO.releaseHoldEventTime(jdbcCustomTemplate, scheduleId, baseResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public BaseResponse cancelReservation(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String scheduleId,
			String customerId) throws Exception {
		BaseResponse baseResponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.CANCEL_RESERVATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseResponse, device, langCode, token, cdConfig, customerId, cache);
			if (baseResponse.isResponseStatus() == false) {
				return baseResponse;
			}
			clientDAO.cancelReservation(jdbcCustomTemplate, customLogger, scheduleId, customerId, baseResponse);
			if (CoreUtils.isIVR(device) && baseResponse.isResponseStatus()) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
						IVRVxmlConstants.CANCEL_CONFIRM.getPageValue(), langCode, cache);
			}

			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			ConfirmResvResponse confirmResponse = new ConfirmResvResponse();
			clientDAO.prepareResvDetails(jdbcCustomTemplate, customLogger, device, Long.valueOf(scheduleId), cdConfig, confirmResponse);
			if ("Y".equals(resvSysConfig.getSendCancelEmail())) {
				String email = confirmResponse.getEmail();
				if (email != null && email.length() > 0) {
					customLogger.debug("Inside email send");
					customLogger.debug("Confirm Number:" + confirmResponse.getConfNumber());
					EmailRequest emailRequest = new EmailRequest();
					emailRequest.setClientName(client.getClientName());
					emailRequest.setEventName(confirmResponse.getEvent());
					emailRequest.setEventDuration(confirmResponse.getDuration());
					emailRequest.setScheduleId(scheduleId);
					emailRequest.setDate(confirmResponse.getReservationDate());
					emailRequest.setTime(confirmResponse.getReservationTime());
					emailRequest.setDateTime(emailRequest.getDate() + " " + emailRequest.getTime());
					emailRequest.setToAddress(email);
					emailRequest.setClientResvLink(client.getApptLink());
					emailRequest.setClientAddress(client.getAddress());
					emailRequest.setClientWebsite(client.getWebsite());
					emailRequest.setFirstName(emailRequest.getFirstName());
					emailRequest.setLastName(emailRequest.getLastName());
					emailRequest.setConfNumber(confirmResponse.getConfNumber());
					emailRequest.setEmailType(EmailConstants.RESERVATION_CANCEL_EMAIL.getValue());
					emailComponent.prepareEmailRequest(jdbcCustomTemplate, customLogger, langCode, device, token, transId,
							EmailTemplateConstants.EMAIL_RESV_CANCEL_SUBJECT.getValue(), EmailTemplateConstants.EMAIL_RESV_CANCEL_BODY.getValue(), cdConfig, resvSysConfig,
							emailRequest, cache);
				} else {
					customLogger.error("EmailId is not present, so email not sent");
				}
			}

			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseResponse;
	}

	@Override
	public EventHistoryResponse getEventsHistory(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception {
		EventHistoryResponse eventHistoryResponse = new EventHistoryResponse();
		List<EventHistory> eventHistoryList = new ArrayList<EventHistory>();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_EVENT_HISTORY.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, eventHistoryResponse, device, langCode, token, cdConfig, cache);
			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			if (eventHistoryResponse.isResponseStatus() == false) {
				return eventHistoryResponse;
			}
			clientDAO.getEventsHistory(jdbcCustomTemplate, customLogger, device, eventHistoryList);
			eventHistoryResponse.setEventHisory(eventHistoryList);
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			eventHistoryResponse.setRightSideEventHeader(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_EVENT_HISTORY_HEADER.getValue()));

			Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			eventHistoryResponse.setDisplayCompanyLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_COMPANY.getValue()));
			eventHistoryResponse.setDisplayProcedureLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_PROCEDURE.getValue()));
			eventHistoryResponse.setDisplayLocationLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_LOCATION.getValue()));
			eventHistoryResponse.setDisplayDepartmentLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DEPARTMENT.getValue()));
			eventHistoryResponse.setDisplayEventLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_EVENT.getValue()));
			eventHistoryResponse.setDateLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DATE.getValue()));
			eventHistoryResponse.setTimeLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_TIME.getValue()));
			eventHistoryResponse.setDisplaySeatLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SEAT.getValue()));

			eventHistoryResponse.setDisplayCompany(resvSysConfig.getDisplayCompany());
			eventHistoryResponse.setDisplayProcedure(resvSysConfig.getDisplayProcedure());
			eventHistoryResponse.setDisplayLocation(resvSysConfig.getDisplayLocation());
			eventHistoryResponse.setDisplayDepartment(resvSysConfig.getDisplayDepartment());
			eventHistoryResponse.setDisplayEvent(resvSysConfig.getDisplayEvent());
			eventHistoryResponse.setDisplaySeat(resvSysConfig.getDisplaySeat());

			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return eventHistoryResponse;
	}

	@Override
	public String clearTheCache(CustomLogger customLogger, String clientCode) throws Exception {
		cacheComponent.clearCache(clientCode);
		return "success";
	}

	@Override
	public String clearTheProperty(String fileName) throws Exception {
		PropertyUtils.clearProperties(fileName);
		return "success";
	}

	private void updateTransactionState(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, Long transId, ErrorConfig errorConfig,
			ClientDeploymentConfig cdConfig) throws Exception {
		clientDAO.updateTransactionState(jdbcCustomTemplate, customLogger, transId, errorConfig == null ? 0 : errorConfig.getErrorId(), cdConfig);
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
	public NameRecordResponse getNameRecordVxml(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception {
		NameRecordResponse nameRecordResponse = new NameRecordResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_NAME_RECORD_VXML.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, nameRecordResponse, device, langCode, token, cdConfig, cache);
			if (nameRecordResponse.isResponseStatus() == false) {
				return nameRecordResponse;
			}
			if (CoreUtils.isIVR(device)) {
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, nameRecordResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
						IVRVxmlConstants.NAME_RECORD.getPageValue(), langCode, cache);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return nameRecordResponse;

	}

	@Override
	public UpdateRecordResponse updateRecordVxml(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String scheduleId,
			String customerId, String filePath, String fileName, String recordDurationInSec) throws Exception {
		UpdateRecordResponse updateRecordResponse = new UpdateRecordResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.UPDATE_RECORD_VXML.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, updateRecordResponse, device, langCode, token, cdConfig, cache);
			if (updateRecordResponse.isResponseStatus() == false) {
				return updateRecordResponse;
			}
			clientDAO.updateRecordVxml(jdbcCustomTemplate, customLogger, scheduleId, customerId, filePath, fileName, recordDurationInSec, cdConfig);
			if (CoreUtils.isIVR(device)) {
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, updateRecordResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
						IVRVxmlConstants.UPDATE_RECORD_VXML.getPageValue(), langCode, cache);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return updateRecordResponse;
	}

	@Override
	public ListOfThingsResponse listOfThingsToBring(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String eventId)
			throws Exception {
		ListOfThingsResponse listOfThingsResponse = new ListOfThingsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.LIST_OF_THINGS_TO_BRING.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, listOfThingsResponse, device, langCode, token, cdConfig, cache);
			if (listOfThingsResponse.isResponseStatus() == false) {
				return listOfThingsResponse;
			}
			clientDAO.listOfThingsToBring(jdbcCustomTemplate, customLogger, device, langCode, eventId, listOfThingsResponse);
			if (CoreUtils.isIVR(device)) {
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, listOfThingsResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
						IVRVxmlConstants.LIST_OF_THINGS_TO_BRING.getPageValue(), langCode, cache);
			} else if (CoreUtils.isOnline(device)) {
				Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				listOfThingsResponse.setRightSideDisplayHeader(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LIST_OF_THING_BRING_HEADER.getValue()));
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return listOfThingsResponse;
	}

	@Override
	public SpecificDateResponse getSpecificDateVXML(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception {
		SpecificDateResponse specificDateResponse = new SpecificDateResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.SPECIFIC_DATE.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, specificDateResponse, device, langCode, token, cdConfig, cache);
			if (specificDateResponse.isResponseStatus() == false) {
				return specificDateResponse;
			}
			if (CoreUtils.isIVR(device)) {
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, specificDateResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
						IVRVxmlConstants.SPECIFIC_DATE.getPageValue(), langCode, cache);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return specificDateResponse;
	}

	@Override
	public ConfPageContactDetailsResponse getConfPageContactDetails(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId,
			String locationId) throws Exception {

		ConfPageContactDetailsResponse confPageContactDetailsResponse = new ConfPageContactDetailsResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.CONF_PAGE_CONTACTS_DETAILS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, confPageContactDetailsResponse, device, langCode, token, cdConfig, cache);
			if (confPageContactDetailsResponse.isResponseStatus() == false) {
				return confPageContactDetailsResponse;
			}
			clientDAO.getConfPageContactDetails(jdbcCustomTemplate, customLogger, device, langCode, locationId, confPageContactDetailsResponse);
			confPageContactDetailsResponse.setEmail(client.getContactEmail());
			confPageContactDetailsResponse.setWebsite(client.getWebsite());
			Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			confPageContactDetailsResponse.setEmailLabel(labelsMap.get(DisplayFieldLabelConstants.RIGHT_SIDE_EMAIL_LABEL.getValue()));
			confPageContactDetailsResponse.setWebsiteLabel(labelsMap.get(DisplayFieldLabelConstants.RIGHT_SIDE_WEBSITE_LABEL.getValue()));
			confPageContactDetailsResponse.setRightSideContactDetailsHeader(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_CONTACT_DETAILS_HEADER.getValue()));
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return confPageContactDetailsResponse;
	}

	@Override
	public LoginInfoRightSideContentResponse getLoginInfoRightSideContent(CustomLogger customLogger, String clientCode, String langCode, String device, String token,
			String transId, String loginFirst) throws Exception {
		LoginInfoRightSideContentResponse loginInfoRightSideContentResponse = new LoginInfoRightSideContentResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.LOGININFO_PAGE_RIGHT_SIDE_CONTENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, loginInfoRightSideContentResponse, device, langCode, token, cdConfig, cache);
			if (loginInfoRightSideContentResponse.isResponseStatus() == false) {
				return loginInfoRightSideContentResponse;
			}
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);

			if ("Y".equals(loginFirst)) {
				loginInfoRightSideContentResponse
						.setLoginInfoRightSideContent(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_FIRST_LOGININFO_PAGE_CONTENT.getValue()));
			} else {
				loginInfoRightSideContentResponse
						.setLoginInfoRightSideContent(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_LAST_LOGININFO_PAGE_CONTENT.getValue()));
			}
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return loginInfoRightSideContentResponse;
	}

	@Override
	public ResvDetailsRightSideContentResponse getResvDetailsRightSideContent(CustomLogger customLogger, String clientCode, String langCode, String device, String token,
			String transId, String loginFirst) throws Exception {
		ResvDetailsRightSideContentResponse resvDetailsRightSideContentResponse = new ResvDetailsRightSideContentResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.RESV_DETAILS_RIGHT_SIDE_CONTENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, resvDetailsRightSideContentResponse, device, langCode, token, cdConfig, cache);
			if (resvDetailsRightSideContentResponse.isResponseStatus() == false) {
				return resvDetailsRightSideContentResponse;
			}
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if ("Y".equals(loginFirst)) {
				resvDetailsRightSideContentResponse.setResvDetailsRightSideContent(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_FIRST_RESV_DETAILS_CONTENT
						.getValue()));
			} else {
				resvDetailsRightSideContentResponse.setResvDetailsRightSideContent(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_LAST_RESV_DETAILS_CONTENT
						.getValue()));
			}
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return resvDetailsRightSideContentResponse;
	}

	@Override
	public ResvVerificationDetailsRightSideContentResponse getResvVerifyDetailsRightSideContent(CustomLogger customLogger, String clientCode, String langCode, String device,
			String token, String transId, String loginFirst) throws Exception {
		ResvVerificationDetailsRightSideContentResponse resvVerifyRightSideContent = new ResvVerificationDetailsRightSideContentResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.RESV_VERIFY_PAGE_RIGHT_SIDE_CONTENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, resvVerifyRightSideContent, device, langCode, token, cdConfig, cache);
			if (resvVerifyRightSideContent.isResponseStatus() == false) {
				return resvVerifyRightSideContent;
			}
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if ("Y".equals(loginFirst)) {
				resvVerifyRightSideContent.setResvVerificationDetailsRightSideContent(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_FIRST_RESV_VERIFY_PAGE_CONTENT
						.getValue()));
			} else {
				resvVerifyRightSideContent.setResvVerificationDetailsRightSideContent(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_LAST_RESV_VERIFY_PAGE_CONTENT
						.getValue()));
			}

		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return resvVerifyRightSideContent;
	}

	@Override
	public ConfirmationPageRightSideContentResponse getConfirmationPageRightSideContent(CustomLogger customLogger, String clientCode, String langCode, String device, String token,
			String transId, String loginFirst) throws Exception {
		ConfirmationPageRightSideContentResponse confirmPageRightSideContentResponse = new ConfirmationPageRightSideContentResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.CONFIRMATION_PAGE_RIGHT_SIDE_CONTENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, confirmPageRightSideContentResponse, device, langCode, token, cdConfig, cache);
			if (confirmPageRightSideContentResponse.isResponseStatus() == false) {
				return confirmPageRightSideContentResponse;
			}
			Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
			if ("Y".equals(loginFirst)) {
				confirmPageRightSideContentResponse.setConfirmationPageRightSideContent(pageContent
						.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_FIRST_CONFIRMATION_PAGE_CONTENT.getValue()));
			} else {
				confirmPageRightSideContentResponse.setConfirmationPageRightSideContent(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_LOGIN_LAST_CONFIRMATION_PAGE_CONTENT
						.getValue()));
			}

		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return confirmPageRightSideContentResponse;
	}

	@Override
	public ReservedEventResponse getReservedEvents(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId, String customerId)
			throws Exception {
		ReservedEventResponse reservedEventResponse = new ReservedEventResponse();
		List<EventHistory> reservedEvents = new ArrayList<EventHistory>();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_RESERVED_EVENTS.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, reservedEventResponse, device, langCode, token, cdConfig, cache);
			ResvSysConfig resvSysConfig = cacheComponent.getResvSysConfig(jdbcCustomTemplate, customLogger, cache);
			
			if (reservedEventResponse.isResponseStatus() == false) {
				return reservedEventResponse;
			}
			clientDAO.getReservedEvents(jdbcCustomTemplate, customLogger, customerId, cdConfig.getTimeZone(), device, reservedEvents);
			if (CoreUtils.isIVR(device)) {
				if(reservedEvents.size() > 0) {
					reservedEventResponse.setTimeZone(cdConfig.getTimeZone());
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, reservedEventResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
						IVRVxmlConstants.CANCEL_RESERVATION.getPageValue(), langCode, cache);
				} else {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, reservedEventResponse, IVRVxmlConstants.APP_CODE.getPageValue(),
							IVRVxmlConstants.CANCEL_NO_RESERVATION.getPageValue(), langCode, cache);
				}
			}
			reservedEventResponse.setReservedEvents(reservedEvents);
			
			if(CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
				Map<String, String> pageContent = cacheComponent.getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				reservedEventResponse.setRightSideEventHeader(pageContent.get(DisplayPageContentConstants.RIGHT_SIDE_EVENT_HISTORY_HEADER.getValue()));
				Map<String, String> labelsMap = cacheComponent.getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
				reservedEventResponse.setDisplayCompanyLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_COMPANY.getValue()));
				reservedEventResponse.setDisplayProcedureLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_PROCEDURE.getValue()));
				reservedEventResponse.setDisplayLocationLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_LOCATION.getValue()));
				reservedEventResponse.setDisplayDepartmentLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DEPARTMENT.getValue()));
				reservedEventResponse.setDisplayEventLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_EVENT.getValue()));
				reservedEventResponse.setDateLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_DATE.getValue()));
				reservedEventResponse.setTimeLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_TIME.getValue()));
				reservedEventResponse.setDisplaySeatLabel(labelsMap.get(DisplayFieldLabelConstants.RESV_SEAT.getValue()));
	
				reservedEventResponse.setDisplayCompany(resvSysConfig.getDisplayCompany());
				reservedEventResponse.setDisplayProcedure(resvSysConfig.getDisplayProcedure());
				reservedEventResponse.setDisplayLocation(resvSysConfig.getDisplayLocation());
				reservedEventResponse.setDisplayDepartment(resvSysConfig.getDisplayDepartment());
				reservedEventResponse.setDisplayEvent(resvSysConfig.getDisplayEvent());
				reservedEventResponse.setDisplaySeat(resvSysConfig.getDisplaySeat());
			}

			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return reservedEventResponse;
	}

	@Override
	public BaseResponse disconnectVXML(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.DISCONNECT_VXML.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
				cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.DISCONNECT.getPageValue(),
						langCode, cache);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}
	
	@Override
	public BaseResponse isAllowDuplicateResv(String clientCode,CustomLogger customLogger,  
			String device, String langCode, String token, String transId, String customerId, String eventId) throws Exception {
		AllowDuplicateResv allowDuplicateResv = new AllowDuplicateResv();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.CHECK_ALLOW_DUPLICATE_RESV.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			boolean updated = clientDAO.isAllowDuplicateResv(jdbcCustomTemplate, customLogger,Long.valueOf(customerId),Long.valueOf(eventId));
			if(updated) {
				allowDuplicateResv.setResponseStatus(true);
				allowDuplicateResv.setAllowDuplicateResv(true);
			} else {
				allowDuplicateResv.setResponseStatus(false);
				if (CoreUtils.isIVR(device)) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, allowDuplicateResv, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.REJECT_DUPLICATION_RESV.getPageValue(),
							langCode, cache);
				} else if(CoreUtils.isOnline(device) || CoreUtils.isAdmin(device)) {
					allowDuplicateResv.setMessage("Not allowed duplicate reservation!");
					allowDuplicateResv.setResponseMessage("Not allowed duplicate reservation!");
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return allowDuplicateResv;
	}

	// not used at present
	private void setUpdateFields(Customer dbCustomer, List<LoginField> loginFields, Customer passedCustomer) throws Exception {
		for (LoginField loginField : loginFields) {
			if ("update".equals(loginField.getLoginType())) {
				String fieldValue = (String) CoreUtils.getPropertyValue(passedCustomer, loginField.getFieldName());
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
				CoreUtils.setPropertyValue(dbCustomer, loginField.getFieldName(), fieldValue);
			}
		}
	}

	@Override
	public BaseResponse noApptInAllLocation(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.NO_APPT_ALL_LOC.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
				boolean isNoApptInLocation = clientDAO.noApptInAllLocation(jdbcCustomTemplate, customLogger);
				if(isNoApptInLocation) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.NO_APPT_ALL_LOC.getPageValue(),
						langCode, cache);
				} else {
					baseReponse.setResponseStatus(false);
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public BaseResponse noApptInSelectedLocation(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId)
			throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.NO_APPT_SELECTED_LOCATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
				boolean isNoApptInSelectedLocation = clientDAO.noApptInSelectedLocation(jdbcCustomTemplate, customLogger, locationId);
				if(isNoApptInSelectedLocation) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.NO_APPT_SELECTED_LOCATION.getPageValue(),
						langCode, cache);
				} else {
					baseReponse.setResponseStatus(false);
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public BaseResponse noApptInSelectedEvent(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId,
			String eventId) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.NO_APPT_SELECTED_EVENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
				boolean isNoApptInSelectedEvent = clientDAO.noApptInSelectedEvent(jdbcCustomTemplate, customLogger,locationId, eventId);
				if(isNoApptInSelectedEvent) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.NO_APPT_SELECTED_EVENT.getPageValue(),
						langCode, cache);
				} else {
					baseReponse.setResponseStatus(false);
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public BaseResponse noApptInSelectedDate(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId,
			String eventId, String date) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.NO_APPT_SELECTED_DATE.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
				boolean isNoApptInSelectedDate = clientDAO.noApptInSelectedDate(jdbcCustomTemplate, customLogger,locationId, eventId, dateYYYYMMDD);
				if(isNoApptInSelectedDate) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.NO_APPT_SELECTED_DATE.getPageValue(),
						langCode, cache);
				} else {
					baseReponse.setResponseStatus(false);
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public BaseResponse noApptInSelectedTime(CustomLogger customLogger, String clientCode, String langCode, String device, String token, String transId, String locationId,
			String eventId, String date, String eventDateTimeId) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.NO_APPT_SELECTED_TIME.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			String dateYYYYMMDD = DateUtils.convertMMDDYYYY_TO_YYYYMMDDFormat(date);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
				boolean isNoApptInSelectedTime = clientDAO.noApptInSelectedTime(jdbcCustomTemplate, customLogger, locationId,eventId, dateYYYYMMDD, eventDateTimeId);
				if(isNoApptInSelectedTime) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.NO_APPT_SELECTED_TIME.getPageValue(),
						langCode, cache);
				} else {
					baseReponse.setResponseStatus(false);
				}
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}
	
	@Override
	public BaseResponse getCancelNoResv(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_VERIFY_RESERVATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.CANCEL_NO_RESERVATION.getPageValue(),
						langCode, cache);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public BaseResponse getCancelVerify(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_VERIFY_RESERVATION.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.CANCEL_VERIFY.getPageValue(),
						langCode, cache);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public BaseResponse getWelcomePageVxml(String clientCode, CustomLogger customLogger, String device, String langCode, String token, String transId) throws Exception {
		BaseResponse baseReponse = new BaseResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.GET_CLIENT.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, baseReponse, device, langCode, token, cdConfig, cache);
			if (baseReponse.isResponseStatus() == false) {
				return baseReponse;
			}
			if (CoreUtils.isIVR(device)) {
					cacheComponent.loadVXML(jdbcCustomTemplate, customLogger, baseReponse, IVRVxmlConstants.APP_CODE.getPageValue(), IVRVxmlConstants.WELCOME.getPageValue(),
						langCode, cache);
			}
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return baseReponse;
	}

	@Override
	public IVRCallResponse saveOrUpdateIVRCallLog(CustomLogger customLogger, IVRCallRequest ivrCallRequest) throws Exception {
		IVRCallResponse ivrCallResponse = new IVRCallResponse();
		JdbcCustomTemplate jdbcCustomTemplate = null;
		ClientDeploymentConfig cdConfig = null;
		ErrorConfig errorConfig = null;
		String clientCode = ivrCallRequest.getClientCode();
		String device = ivrCallRequest.getDevice();
		String langCode = ivrCallRequest.getLangCode();
		String token = ivrCallRequest.getToken();
		String transId = ivrCallRequest.getTransId();
		try {
			Client client = cacheComponent.getClient(customLogger, clientCode, device, true);
			boolean cache = "Y".equals(client.getCacheEnabled()) ? true : false;
			cdConfig = cacheComponent.getClientDeploymentConfig(customLogger, clientCode, device, cache);
			errorConfig = cacheComponent.getErrorConfig(customLogger, FlowStateConstants.SAVE_UPDATE_IVR_CALL.getValue(), cache);
			jdbcCustomTemplate = connectionPoolUtil.getJdbcCustomTemplate(customLogger, client);
			setTokenValidity(jdbcCustomTemplate, customLogger, ivrCallResponse, device, langCode, token, cdConfig, cache);
			if (ivrCallResponse.isResponseStatus() == false) {
				return ivrCallResponse;
			}
			clientDAO.saveOrUpdateIVRCallLog(jdbcCustomTemplate, customLogger, cdConfig, ivrCallRequest, ivrCallResponse);
			updateTransactionState(jdbcCustomTemplate, customLogger, device, Long.valueOf(transId), errorConfig, cdConfig);
		} catch (Exception e) {
			throwException(jdbcCustomTemplate, customLogger, device,  Long.valueOf(transId), errorConfig, cdConfig, e);
		}
		return ivrCallResponse;
	}
}
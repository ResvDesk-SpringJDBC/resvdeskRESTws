package com.telappoint.resvdeskrestws.common.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.logger.CustomLogger;
import com.telappoint.resvdeskrestws.admin.model.RegistrationField;
import com.telappoint.resvdeskrestws.common.clientdb.dao.AdminDAO;
import com.telappoint.resvdeskrestws.common.clientdb.dao.ClientDAO;
import com.telappoint.resvdeskrestws.common.clientdb.dao.NotifyResvDAO;
import com.telappoint.resvdeskrestws.common.constants.CacheConstants;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;
import com.telappoint.resvdeskrestws.common.masterdb.dao.MasterDAO;
import com.telappoint.resvdeskrestws.common.masterdb.domain.Client;
import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.ClientDeploymentConfig;
import com.telappoint.resvdeskrestws.common.model.ErrorConfig;
import com.telappoint.resvdeskrestws.common.model.IVRXml;
import com.telappoint.resvdeskrestws.common.model.JdbcCustomTemplate;
import com.telappoint.resvdeskrestws.common.model.Language;
import com.telappoint.resvdeskrestws.common.model.LoginField;
import com.telappoint.resvdeskrestws.common.model.ResvSysConfig;
import com.telappoint.resvdeskrestws.notification.model.CampaignMessageEmail;

/**
 * 
 * @author Balaji Nandarapu
 *
 */

@Component
public class CacheComponent {

	private static Map<String, Client> clientCacheMap = new HashMap<String, Client>();
	private static Map<String, Object> cacheObject = new HashMap<String, Object>();
	private static Map<String, Map<String, IVRXml>> cacheMap = new HashMap<String, Map<String, IVRXml>>();
	private static Map<String, CustomLogger> loggerCacheMap = new HashMap<String, CustomLogger>();

	private static final Object lock = new Object();

	@Autowired
	private MasterDAO masterDAO;

	@Autowired
	private ClientDAO clientDAO;
	
	@Autowired
	private NotifyResvDAO notifyResvDAO;
	
	@Autowired
	private AdminDAO adminDAO;

	public Client getClient(CustomLogger customLogger, String clientCode, String device, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.CLIENT.getValue()).append("|").append(clientCode);
		Client client = clientCacheMap.get(key.toString());
		if (client != null && cache) {
			if (customLogger != null) {
				customLogger.debug("Client object returned from cache.");
			}
			return client;
		} else {
			if (customLogger != null) {
				customLogger.debug("Client object returned from DB.");
			}
			synchronized (lock) {
				masterDAO.getClients(CacheConstants.CLIENT.getValue(), clientCacheMap);
			}
			client = clientCacheMap.get(key.toString());
			if (client == null) {
				customLogger.info("Client is not available to process - [clientCode:" + clientCode + ", device:" + device + "]");
				throw new Exception("Client not found!");
			}
			return client;
		}
	}

	public ClientDeploymentConfig getClientDeploymentConfig(CustomLogger customLogger, String clientCode, String device, boolean cache) throws Exception {
		Client client = getClient(customLogger, clientCode, device, cache);
		if (client == null) {
			customLogger.info("Client is not available to process - [clientCode:" + clientCode + ", device:" + device + "]");
			throw new Exception("Client not found!");
		}
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.CLIENT_DEPLOYMENT_CONFIG.getValue()).append("|").append(clientCode);

		ClientDeploymentConfig clientDeploymentConfig = (ClientDeploymentConfig) cacheObject.get(key.toString());
		if (clientDeploymentConfig != null && cache) {
			customLogger.debug("ClientDeploymentConfig object returned from cache.");
			return clientDeploymentConfig;
		} else {
			customLogger.debug("ClientDeploymentConfig object returned from DB.");
			synchronized (lock) {
				masterDAO.getClientDeploymentConfig(CacheConstants.CLIENT_DEPLOYMENT_CONFIG.getValue(), clientCode, client.getClientId(), cacheObject);
			}
			clientDeploymentConfig = (ClientDeploymentConfig) cacheObject.get(key.toString());
			return clientDeploymentConfig;
		}
	}

	public ErrorConfig getErrorConfig(CustomLogger customLogger, String errorPropKey, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.ERROR_CONFIG.getValue()).append("|").append(errorPropKey);

		ErrorConfig errorConfig = (ErrorConfig) cacheObject.get(key.toString());
		if (errorConfig != null && cache) {
			customLogger.debug("ErrorConfig object returned from cache.");
			return errorConfig;
		} else {
			customLogger.debug("ErrorConfig object returned from DB.");
			synchronized (lock) {
				masterDAO.getErrorConfig(CacheConstants.ERROR_CONFIG.getValue(), cacheObject);
			}
			errorConfig = (ErrorConfig) cacheObject.get(key.toString());
			return errorConfig;
		}
	}

	public CustomLogger getLogger(String clientCode, String device, boolean cache) throws Exception {
		StringBuilder clientKey = new StringBuilder();
		clientKey.append(CacheConstants.CLIENT.getValue()).append("|").append(clientCode);

		CustomLogger customLogger = loggerCacheMap.get(clientCode);
		if (customLogger != null && cache) {
			customLogger.debug("Logger object returned from cache.");
			return customLogger;
		} else {
			Client client = getClient(null, clientCode, device, true);
			if (client != null) {
				String logLevel = PropertyUtils.getValueFromProperties("LOG_LEVEL", PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
				customLogger = new CustomLogger(clientCode.toLowerCase(), true, "ClientCode:" + clientCode, logLevel);
			} else {
				if (customLogger == null) {
					customLogger = new CustomLogger("invalidclientreq", false, clientCode, "INFO");
					return customLogger;
				}
			}
			return customLogger;
		}
	}

	public ResvSysConfig getResvSysConfig(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.RESV_SYS_CONFIG.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Object obj = cacheObject.get(key.toString());
		if (obj != null && cache) {
			customLogger.debug("ResvSysConfig object returned from cache");
			return (ResvSysConfig) obj;
		} else {
			customLogger.debug("ResvSysConfig object returned from DB.");
			ResvSysConfig resvSysConfig = clientDAO.getResvSysConfig(jdbcCustomTemplate, customLogger);
			synchronized (lock) {
				cacheObject.put(key.toString(), resvSysConfig);
			}
			return resvSysConfig;
		}

	}

	private Map<String, List<LoginField>> getLoginParamsList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("LoginParamList returned from cache.");
			return (Map<String, List<LoginField>>) obj;
		} else {
			customLogger.debug("LoginParamList returned from DB.");
			Map<String, List<LoginField>> subMap = new HashMap<String, List<LoginField>>();
			clientDAO.getLoginParams(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public List<LoginField> getLoginParamsList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, boolean cache) throws Exception {
		StringBuilder mainkey = new StringBuilder();
		mainkey.append(CacheConstants.LOGIN_PARAM_CONFIG.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, String> labelsMap = getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
		Map<String, List<LoginField>> map = getLoginParamsList(jdbcCustomTemplate, customLogger, mainkey.toString(), cache);

		StringBuilder subkey = new StringBuilder().append(device);

		List<LoginField> loginParamListFromCache = map.get(subkey.toString());
		List<LoginField> loginParamList = new ArrayList<LoginField>();
		LoginField loginFormNew = null;
		for (LoginField loginField : loginParamListFromCache) {
			loginFormNew = new LoginField();
			BeanUtils.copyProperties(loginField, loginFormNew);

			if (labelsMap != null && labelsMap.size() > 0) {
				loginFormNew.setDisplayTitle(labelsMap.get(loginFormNew.getDisplayTitle()));
				loginFormNew.setEmptyErrorMessage(labelsMap.get(loginFormNew.getEmptyErrorMessage()));
				loginFormNew.setInvalidErrorMessage(labelsMap.get(loginFormNew.getInvalidErrorMessage()));
			}
			loginParamList.add(loginFormNew);
		}
		return loginParamList;
	}
	
	
	private Map<String, List<RegistrationField>> getRegParamsList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("RegParamList returned from cache.");
			return (Map<String, List<RegistrationField>>) obj;
		} else {
			customLogger.debug("RegParamList returned from DB.");
			Map<String, List<RegistrationField>> subMap = new HashMap<String, List<RegistrationField>>();
			adminDAO.getRegistrationParams(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}
	
	public List<RegistrationField> getRegistrationParamsList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, boolean cache) throws Exception {
		StringBuilder mainkey = new StringBuilder();
		mainkey.append(CacheConstants.REGISTRATION_PARAM_CONFIG.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, String> labelsMap = getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, device, langCode, cache);
		Map<String, List<RegistrationField>> map = getRegParamsList(jdbcCustomTemplate, customLogger, mainkey.toString(), cache);

		StringBuilder subkey = new StringBuilder().append(device);

		List<RegistrationField> regParamListFromCache = map.get(subkey.toString());
		List<RegistrationField> loginParamList = new ArrayList<RegistrationField>();
		RegistrationField regFields = null;
		for (RegistrationField regField : regParamListFromCache) {
			regFields = new RegistrationField();
			BeanUtils.copyProperties(regField, regFields);

			if (labelsMap != null && labelsMap.size() > 0) {
				regFields.setDisplayTitle(labelsMap.get(regFields.getDisplayTitle()));
				regFields.setEmptyErrorMessage(labelsMap.get(regFields.getEmptyErrorMessage()));
				regFields.setInvalidErrorMessage(labelsMap.get(regFields.getInvalidErrorMessage()));
			}
			loginParamList.add(regFields);
		}
		return loginParamList;
	}

	private Map<String, Map<String, String>> getDesignTemplatesMapCache(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("DesignTemplates returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			customLogger.debug("DesignTemplates returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			clientDAO.getI18nDesignTemplatesMap(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getDesignTemplatesMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_TEMPLATE.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDesignTemplatesMapCache(jdbcCustomTemplate, customLogger, key.toString(), cache);

		key.setLength(0);
		key.append(device);
		return map.get(key.toString());
	}

	private Map<String, Map<String, String>> getDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("DisplayFieldLabels returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			customLogger.debug("DisplayFieldLabels returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			clientDAO.getI18nDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getDisplayFieldLabelsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_FIELD_LABEL.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayFieldLabelsMap(jdbcCustomTemplate, customLogger, key.toString(), cache);
		key.setLength(0);
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}

	private Map<String, Map<String, String>> getDisplayPageContentsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("DisplayPageContents returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			customLogger.debug("DisplayPageContents returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			clientDAO.getI18nPageContentMap(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}
	
	public Map<String, String> getDisplayPageContentsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_PAGE_CONTENT.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayPageContentsMap(jdbcCustomTemplate, customLogger, key.toString(), cache);
		key.setLength(0);
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}
	
	
	private Map<String, Map<String, String>> getI18nEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("EmailTemplateMap returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			customLogger.debug("EmailTemplateMap returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			clientDAO.getI18nEmailTemplateMap(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getEmailTemplateMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.EMAIL_TEMPLATE.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getI18nEmailTemplateMap(jdbcCustomTemplate, customLogger, key.toString(), cache);
		key.setLength(0);
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}


	private Map<String, Map<String, String>> getDisplayButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("DisplayButtons returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			customLogger.debug("DisplayButtons returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			clientDAO.getI18nButtonsMap(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getDisplayButtonsMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_BUTTON_NAMES.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());
		Map<String, Map<String, String>> map = getDisplayButtonsMap(jdbcCustomTemplate, customLogger, key.toString(), cache);
		key.setLength(0);
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}

	private Map<String, Map<String, String>> getDisplayAliasMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Object obj = cacheObject.get(mainKey);
		if (obj != null && cache) {
			customLogger.debug("DisplayAlias returned from cache.");
			Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) obj;
			return map;
		} else {
			customLogger.debug("DisplayAlias returned from DB.");
			Map<String, Map<String, String>> subMap = new HashMap<String, Map<String, String>>();
			clientDAO.getI18nAliasesMap(jdbcCustomTemplate, customLogger, subMap);
			synchronized (lock) {
				cacheObject.put(mainKey, subMap);
			}
			return subMap;
		}
	}

	public Map<String, String> getDisplayAliasMap(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String device, String langCode, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_ALIAES.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, Map<String, String>> map = getDisplayAliasMap(jdbcCustomTemplate, customLogger, key.toString(), cache);
		key.setLength(0);
		key.append(device).append("|").append(langCode);
		return map.get(key.toString());
	}

	public List<Language> getLangList(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.DISPLAY_LANG.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Object obj = cacheObject.get(key.toString());
		if (obj != null && cache) {
			customLogger.debug("LanguageList returned from cache.");
			return (List<Language>) ((ArrayList<Language>) obj).clone();
		} else {
			customLogger.debug("LanguageList returned from DB.");
			List<Language> languageList = new ArrayList<Language>();
			clientDAO.getLangDetails(jdbcCustomTemplate, customLogger, languageList);
			synchronized (lock) {
				cacheObject.put(key.toString(), languageList);
			}
			return languageList;
		}
	}

	private Map<String, IVRXml> getVXML(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String mainKey, boolean cache) throws Exception {
		Map<String, IVRXml> map = cacheMap.get(mainKey);
		if (map != null && map.size() > 0 && cache) {
			customLogger.debug("VXML returned from cache.");
			return map;
		} else {
			customLogger.debug("VXML returned from DB.");
			synchronized (lock) {
				clientDAO.loadVXML(jdbcCustomTemplate, mainKey, cacheMap);
			}
			return cacheMap.get(mainKey);
		}
	}

	public void loadVXML(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, BaseResponse baseResponse, String appCode, String pageName, String langCode, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.IVR_VXML.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Map<String, IVRXml> map = getVXML(jdbcCustomTemplate, customLogger, key.toString(), cache);
		key.setLength(0);
		key.append(appCode).append("|").append(pageName).append("|").append(langCode);
		IVRXml ivrxml = map.get(key.toString());

		baseResponse.setPageName(ivrxml == null || ivrxml.getPageName() == null ? "" : ivrxml.getPageName());
		baseResponse.setVxml(ivrxml == null || ivrxml.getVxml() == null ? "" : ivrxml.getVxml());
		baseResponse.setPageAudio(ivrxml == null || ivrxml.getPageAudio() == null ? "" : ivrxml.getPageAudio());
		baseResponse.setPageTTS(ivrxml == null || ivrxml.getPageTTS() == null ? "" : ivrxml.getPageTTS());
	}

	public void clearCache(String clientCode) {
		Set<String> cacheKeys = new HashSet<String>();
		CacheConstants[] keys = CacheConstants.values();

		for (CacheConstants key : keys) {
			cacheKeys.add(key.getValue() + "|" + clientCode);
		}

		synchronized (lock) {
			clientCacheMap.keySet().removeAll(cacheKeys);
		}
	}

	public void getEventDateTimeId(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, String langCode, String device, String locationId, String eventId, String date, String time) {
		// TODO: 
		
	}
	
	
	public CampaignMessageEmail getCampaignMessageEmail(JdbcCustomTemplate jdbcCustomTemplate, CustomLogger customLogger, boolean cache) throws Exception {
		StringBuilder key = new StringBuilder();
		key.append(CacheConstants.CAMPAIGN_MESSAGE_EMAIL.getValue()).append("|").append(jdbcCustomTemplate.getClientCode());

		Object obj = cacheObject.get(key.toString());
		if (obj != null && cache) {
			customLogger.debug("campaignMessageEmail object returned from cache");
			return (CampaignMessageEmail) obj;
		} else {
			customLogger.debug("campaignMessageEmail object returned from DB.");
			CampaignMessageEmail campaignMsgEmail = notifyResvDAO.getCampaignMessageEmails(jdbcCustomTemplate, customLogger, (long)1);
			synchronized (lock) {
				cacheObject.put(key.toString(), campaignMsgEmail);
			}
			return campaignMsgEmail;
		}

	}
}

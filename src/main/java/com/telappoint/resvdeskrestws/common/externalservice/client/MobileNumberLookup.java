package com.telappoint.resvdeskrestws.common.externalservice.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.telappoint.common.utils.PropertyUtils;
import com.telappoint.resvdeskrestws.common.constants.PropertiesConstants;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;

/**
 * 
 * @author Balaji N
 * 
 */
public class MobileNumberLookup {
	private static final Logger logger = Logger.getLogger(MobileNumberLookup.class);
	private static TwilioRestClient client = null;

	public static void main(String[] args) throws Exception {
		// while(true) {
		long startTime = System.currentTimeMillis();
		getMobileType("919886262735", "AC4458b0b635416cba4238effa76e78e84", "31056ad9bdd97443ec55231d6c966881");
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - startTime);
		// }
	}

	public static String getMobileType(String phoneNumber, String accountSid, String authToken) throws Exception {
		String endPointUrl = PropertyUtils.getValueFromProperties("PHONE_LOOKUP_URL",PropertiesConstants.RESV_DESK_REST_WS_PROP.getPropertyFileName());
		if (endPointUrl == null) {
			endPointUrl = "https://lookups.twilio.com/v1/PhoneNumbers/@PHONENUMBER@?Type=carrier";
		}
		endPointUrl = endPointUrl.replaceAll("@PHONENUMBER@", phoneNumber);
		if (client == null) {
			client = new TwilioRestClient(accountSid, authToken);
		}
		TwilioRestResponse resp = null;
		try {
			resp = client.request(endPointUrl, "GET", null);
		} catch (TwilioRestException e) {
			logger.error("Phone lookup failed for phone Number" + phoneNumber);
			logger.error("Phone lookup failed " + e, e);
		}
		if (!resp.isError()) {
			String data = resp.getResponseText();

			MobileLookup ml = null;
			ObjectMapper mapper = new ObjectMapper();
			try {
				ml = mapper.readValue(data, MobileLookup.class);
			} catch (JsonGenerationException e) {
				throw new Exception("JsonGenerationException");
			} catch (JsonMappingException e) {
				throw new Exception("JsonMappingException");
			} catch (IOException e) {
				throw new Exception("IOException");
			}
			return ml.getCarrier().getType(); 
		} else {
			logger.error("phone lookup failed: isServerError " + resp.isServerError());
		}
		return null;
	}
}

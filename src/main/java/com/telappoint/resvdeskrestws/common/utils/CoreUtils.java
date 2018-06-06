package com.telappoint.resvdeskrestws.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import com.telappoint.resvdeskrestws.common.constants.CommonDateContants;
import com.telappoint.resvdeskrestws.common.constants.CommonResvDeskConstants;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.PostUpdate;
import facebook4j.RawAPIResponse;
import facebook4j.auth.AccessToken;
import facebook4j.auth.OAuthAuthorization;
import facebook4j.auth.OAuthSupport;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

/**
 * 
 * @author Balaji Nandarapu
 *
 */
public class CoreUtils {

	private static DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	public static Object getPropertyValue(Object object, String fieldName) throws NoSuchFieldException {
		try {
			BeanInfo info = Introspector.getBeanInfo(object.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if (pd.getName().equals(fieldName)) {
					Method getter = pd.getReadMethod();
					if (getter != null) {
						getter.setAccessible(true);
						return getter.invoke(object, null);
					}

				}
			}
		} catch (Exception e) {
			throw new NoSuchFieldException(object.getClass() + " has no field " + fieldName);
		}
		return "";
	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String removeNonDigits(final String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str.replaceAll("\\D+", "");
	}

	public static boolean isStringEqual(String str1, String str2) {
		if (str1 == null || str2 == null)
			return false;
		str1 = str1.trim();
		str2 = str2.trim();
		if (str1.length() == 0 || str2.length() == 0)
			return false;
		if (str1.equals(str2))
			return true;
		return false;
	}

	public static String getNotifyStartDate(int hours, String timeZone) {
		GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
		cal.add(Calendar.HOUR_OF_DAY, hours);
		ThreadLocal<DateFormat> df = DateUtils.getSimpleDateFormat(CommonDateContants.DATETIME_FORMAT_YYYYMMDDHHMMSS_CAP.getValue());
		df.get().setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
		return df.get().format(cal.getTime());
	}

	public static void setPropertyValue(Object object, String propertyName, Object propertyValue) throws Exception {
		try {
			BeanInfo bi = Introspector.getBeanInfo(object.getClass());
			PropertyDescriptor pds[] = bi.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				if (pd.getName().equals(propertyName)) {
					Method setter = pd.getWriteMethod();
					if (setter != null) {
						setter.invoke(object, new Object[] { propertyValue });
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static Object getInitCaseValue(Object value) {
		String name = (String) value;
		StringBuilder nameBuilder = new StringBuilder();
		String[] nameStrs = name.split("\\s+");
		if (nameStrs != null && nameStrs.length > 0) {
			for (String nameStr : nameStrs) {
				if (nameStr != null && !" ".equals(nameStr) && nameStr.length() > 0) {
					nameBuilder.append(nameStr.substring(0, 1) != null ? nameStr.substring(0, 1).toUpperCase() : "");
					nameBuilder.append(nameStr.substring(1));
					nameBuilder.append(" ");
				}
			}
		}
		if (nameBuilder.toString() != null && !"".equals(nameBuilder.toString().trim())) {
			value = nameBuilder.toString().trim();
		}
		return value;
	}

	public static String capitalizeString(String string) {
		char[] chars = string.toLowerCase().toCharArray();
		StringBuilder result = new StringBuilder();
		boolean found = false;
		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				result.append(Character.toUpperCase(chars[i]));
				found = true;
			} else if ("'".equals(String.valueOf(chars[i]))) {
				result.append(" ");
				found = false;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '-') {
				result.append(chars[i]);
				found = false;
			} else if (Character.isLetter(chars[i])) {
				result.append(chars[i]);
			}
		}
		return result.toString();
	}

	public static Date addMinsToCurrentTime(int mins) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, mins);
		return calendar.getTime();
	}

	public static boolean isOnline(String device) {
		return CommonResvDeskConstants.ONLINE.getValue().equals(device) ? true : false;
	}

	public static boolean isAdmin(String device) {
		return CommonResvDeskConstants.ADMIN.getValue().equals(device) ? true : false;
	}

	public static boolean isMobile(String device) {
		return CommonResvDeskConstants.MOBILE.getValue().equals(device) ? true : false;
	}

	public static boolean isIVR(String device) {
		return CommonResvDeskConstants.IVRAUDIO.getValue().equals(device) ? true : false;
	}

	public static boolean isEmailReminder(String device) {
		return CommonResvDeskConstants.EMAIL_REMINDER.getValue().equals(device) ? true : false;
	}

	public static boolean isSMSReminder(String device) {
		return CommonResvDeskConstants.SMS_REMINDER.getValue().equals(device) ? true : false;
	}

	public static String getToken(String clientCode, String device) {
		StringBuilder sb = new StringBuilder();
		sb.append(clientCode);
		sb.append(device);
		sb.append(formatter.format(new Date()));
		sb.append(GenerateRandomToken.getRandomToken(6, "N"));
		return UUID.nameUUIDFromBytes(sb.toString().getBytes()).toString();
	}

	public static String removeDigitsAndNonAlpha(final String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		return str.replaceAll("[^A-Za-z]", "");
	}

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean validateIP(final String ip) {
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	static int values[] = { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 };

	static void printArray(int i) {
		if (i == 0) {
			return;
		} else {
			printArray(i - 1);
		}
		System.out.println("[" + (i - 1) + "] " + values[i - 1]);
		System.out.println(i);
	}

	public static String reverseRecursively(String str) {

		// base case to handle one char string and empty string
		if (str.length() < 2) {
			return str;
		}
		return reverseRecursively(str.substring(1)) + str.charAt(0);
	}

	static String result;

	static String fact(String word, boolean isLastWord) {
		String[] test = word.split(" ");
		if (test.length == 1) {
			return word;
		} else {

			boolean lastWord = (test.length == 1);
			int index = word.indexOf(" ");
			word = word.substring(index + 1);
			result = fact(word, lastWord) + " " + test[0];
			return result;
		}
	}

	void CoreUtils() {

	}

	public static Configuration createConfiguration() {
		ConfigurationBuilder confBuilder = new ConfigurationBuilder();

		confBuilder.setDebugEnabled(true);
		confBuilder.setOAuthAppId("164908340550073");
		confBuilder.setOAuthAppSecret("c824635ec9852cccbdaefe1ae0b82150");
		confBuilder.setUseSSL(true);
		confBuilder.setJSONStoreEnabled(true);
		Configuration configuration = confBuilder.build();
		return configuration;
	}

	public static void test1() throws FacebookException {
		Configuration configuration = createConfiguration();
		
		FacebookFactory facebookFactory = new FacebookFactory(configuration);
		Facebook facebookClient = facebookFactory.getInstance();
		// String commaSeparetedPermissions =
		// "user_friends,user_groups,user_photos,user_videos,user_birthday,user_status,user_likes,user_activities,user_location";
		// facebookClient.setOAuthPermissions(commaSeparetedPermissions);
		AccessToken accessToken = null;
		try {
			OAuthSupport oAuthSupport = new OAuthAuthorization(configuration);
			accessToken = oAuthSupport.getOAuthAppAccessToken();
			System.out.println(accessToken);
			// String accessTokenStr =
			// "164908340550073|3Pv5BAEhqhLRW22zRxY7pzIouzs";
			facebookClient.setOAuthAccessToken(accessToken);

		} catch (FacebookException e) {
			System.err.println("Error while creating access token " + e.getLocalizedMessage());
		}
		facebookClient.setOAuthAccessToken(accessToken);
		// results in an error says {An active access token must be used to
		// query information about the current user}
		facebookClient.postStatusMessage("Hello World from Facebook4J.");
	}

	public static void testFine2() throws Exception {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId("164908340550073", "c824635ec9852cccbdaefe1ae0b82150");
		//facebook.setOAuthPermissions("email,publish_stream");
		facebook.setOAuthPermissions("user_friends, public_profile");
		
	    AccessToken accessToken = facebook.getOAuthAppAccessToken();
	    accessToken = refreshToken(facebook,accessToken);
		System.out.println(accessToken.getToken());
		facebook.setOAuthAccessToken(accessToken);
		
		PostUpdate post = new PostUpdate(new URL("http://facebook4j.org"))
        .picture(new URL("http://facebook4j.org/images/hero.png"))
        .name("Facebook4J - A Java library for the Facebook Graph API")
        .caption("facebook4j.org")
        .description("hELLO");
		facebook.postFeed(post);

		//facebook.postStatusMessage("Hello World Balaji.3");
	}

	public static String getAccessToken() throws Exception {
		String url = "https://graph.facebook.com/oauth/access_token";
	    String charset = "UTF-8";
	    String grandType = "fb_exchange_token";
	  
	    String query = String.format("grant_type=%s&client_id=%s&client_secret=%s&fb_exchange_token=%s",
	            URLEncoder.encode(grandType, charset),
	            URLEncoder.encode("164908340550073", charset),
	            URLEncoder.encode("c824635ec9852cccbdaefe1ae0b82150", charset),
	            URLEncoder.encode("164908340550073|3Pv5BAEhqhLRW22zRxY7pzIouzs", charset));
	    HttpsURLConnection con = (HttpsURLConnection) new URL(url + "?" + query).openConnection();
	    InputStream ins = con.getInputStream();
	    InputStreamReader isr = new InputStreamReader(ins);
	    BufferedReader in = new BufferedReader(isr);

	    String inputLine;
	    String result = "";
	    while ((inputLine = in.readLine()) != null) {
	        System.out.println(inputLine);
	        result += inputLine;
	    }
	    in.close();

	    String[] params = result.split("&");
	    Map<String, String> map = new HashMap<String, String>();
	    for (String param : params) {
	        String name = param.split("=")[0];
	        String value = param.split("=")[1];
	        map.put(name, value);
	    }
	    String longToken=map.get("access_token");
	    return longToken;
	}
	
	public static void testFine() throws Exception {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthAppId("164908340550073").setOAuthAppSecret("c824635ec9852cccbdaefe1ae0b82150").setOAuthAccessToken("164908340550073|3Pv5BAEhqhLRW22zRxY7pzIouzs")
				.setOAuthPermissions("user_friends, public_profile");

		FacebookFactory ff = new FacebookFactory(cb.build());
		Facebook facebook = ff.getInstance();
		facebook.postStatusMessage("Hello World Balaji 2.");
	}
	
	private static AccessToken refreshToken(Facebook facebook, AccessToken currentToken) throws Exception {
		
	    String clientId = "164908340550073";
	    String clientSecret = "c824635ec9852cccbdaefe1ae0b82150";

	    Map<String, String> params = new HashMap<String, String>();
	    params.put("client_id", clientId);
	    params.put("client_secret", clientSecret);
	    params.put("grant_type", "fb_exchange_token");
	    params.put("fb_exchange_token", currentToken.getToken());

	    RawAPIResponse apiResponse = facebook.callGetAPI("/oauth/access_token", params);

	    String response = apiResponse.asString();
	    AccessToken newAccessToken = new AccessToken(response);

	    facebook.setOAuthAccessToken(newAccessToken);

	    return newAccessToken;
	}

	public static void sendMessage() throws Exception {
		Facebook facebook = new FacebookFactory().getInstance();
		String appId = "164908340550073";
		String appSecret = "c824635ec9852cccbdaefe1ae0b82150";
		facebook.setOAuthAppId(appId, appSecret);
		String commaSeparetedPermissions = "publish_actions,manage_pages,email";
	
		facebook.setOAuthPermissions(commaSeparetedPermissions);
		//AccessToken testTOken = new AccessToken("CAACEdEose0cBAHcVSMpgCjuZAZCikFOWkGb5V8bFQLzGrcIyosuiHDfOLTx0nFZBVTTnb8pVfGvo14goFKyWxttSs09zK3zi0qQSlnHBzUDVWcoZA5fscKFf2K8WjDv7lmDBzBZA36wOuoVMDAGMddeGBKNtZBHev6tf00GnNsBmZCJKMWLHR2gwsPUzRArfFSfirJ0cv64NWo5rrnkxDAj", (long)2000);
		AccessToken accessTokenTest = refreshToken(facebook,new AccessToken("164908340550073|3Pv5BAEhqhLRW22zRxY7pzIouzs"));
		System.out.println(accessTokenTest.getToken());
		//CAACEdEose0cBAK19OJZBVPM0z1UK8YNr2HLZALHtDX4Iln8iG4aITC6479TdMsA5Bdmued5OXH6hQqlqLY1EllOcSdy1LIZCBqBegk7F2Uy5NQqIK5K2zd2kh2wKS88F1KYZAqAFypSkIlrRI9jPk8KCnZC6an9OMaASfYZCYl3AboSRenBzhqwiVWy8ZAZAFP8ZC5rI8WW5VUoYU6uKANz0h
		String accessToken = "CAACEdEose0cBANZAlXxmbsLYnro70KzZBZBOqDGoUD94qD56e4g1GUxuJtn3XvdPpBZCq1ZCZCij5LcS6m71bK58lSeqnNtpQtZAxY41n94imNU5wKLFItB9FORZBMUYxthBakzseGD9jKcRUB2nvWy2sc0lBMXKsucvQ0qw3CHGn9VZBgnTMKJJVIlY8Yi26rYyOl5ogg49O0QMnjpZBE0OgQ";
		
		//String accessToken= facebook.getOAuthAppAccessToken();
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));

		try {
			
			
			facebook.postStatusMessage("Hello World Balaji - This is good. New.Test manage pages.3");
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		//test1();
		sendMessage();
		//refreshToken()
		//testFine();
		//System.out.println(getAccessToken());
	}

	// hello();
	// System.out.println(PropertyUtils.getValueFromProperties("admin",
	// "users.properties"));

	/*
	 * System.out.println("hellp:::;"+"Y".equals(null));
	 * 
	 * class Foo { public int i = 3; }
	 * 
	 * class Foo1 { public int i = 3; }
	 * 
	 * Object o = new Foo(); Foo foo = (Foo)o; System.out.println("i = " +
	 * foo.i);
	 */

	/*
	 * System.out.println(reverseRecursively("balaji"));
	 * System.out.println(fact("bnaa nanda shoba", false)); int array[] = { 1,
	 * 2, 3, 4, 5, 6, 3, 8, 9, 10 }; int sum = 0; for (int i = 0; i < 10; i++)
	 * {567890- if (i + 1 == array.length) { if (array[i] - array[i - 1] == 0) {
	 * System.out.println("duplicate:" + array[i]); continue; } } else if
	 * (array[i + 1] - array[i] == 0) { System.out.println("duplicate:" +
	 * array[i + 1]); } sum = sum + array[i]; }
	 */

	/*
	 * String k="balaji"; String s="balaji1"; int j=9;
	 * 
	 * if(k.equals(s) && ++j >= 10) { System.out.println("hello:"+j); }
	 * System.out.println(j);
	 */

	// printArray(10);

	/*
	 * int x = 11 & 9; int y = x ^ 3; System.out.println( y | 12 ); new
	 * CoreUtils().start();
	 * 
	 * }
	 * 
	 * void start() { String s1 = "slip"; String s2 = fix(s1);
	 * System.out.println(s1 + " " + s2); }
	 * 
	 * String fix(String s1) {
	 * 
	 * try { Thread.sleep(1000); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } float f1[ ], f2[ ]; f1
	 * = new float[10]; f2 = f1; System.out.println("f2[0] = " + f2[0]);
	 * 
	 * s1 = s1 + "stream"; System.out.print(s1 + " "); boolean is = false;
	 * if(is) { System.out.println("ddd"); return "ddd"; } return "stream"; }
	 */
}

package com.telappoint.resvdeskrestws.common.model;

import com.google.common.collect.Lists;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;
import java.util.Map;

import static org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class LoginDetails {

	private String placement;
	private String paramTable;
	private String paramColumn;
	private String deviceType;
	private String displayContext;
	private String id;
	private String paramType;
	private String loginType;
	private String displayType;
	private String displayTitle;
	private String fieldNotes;
	private String displayFormat;
	private String displaySize;
	private String maxChars;
	private String textAreaRows;
	private String textAreaCols;
	private String displayHint;
	private String displayTooltip;
	private String emptyErrorMessage;
	private String invalidErrorMessage;
	private String validationRequired;
	private String validationRules;
	private String validateMaxChars;
	private String validateMinValue;
	private String validateMaxValue;
	private String listLabels;
	private String listValues;
	private String listInitialValues;
	private String required;
	private Map<Integer,String> map;
	
	/**
	 * @return the placement
	 */
	public String getPlacement() {
		return placement;
	}
	/**
	 * @param placement the placement to set
	 */
	public void setPlacement(String placement) {
		this.placement = placement;
	}
	/**
	 * @return the paramTable
	 */
	public String getParamTable() {
		return paramTable;
	}
	/**
	 * @param paramTable the paramTable to set
	 */
	public void setParamTable(String paramTable) {
		this.paramTable = paramTable;
	}
	/**
	 * @return the paramColumn
	 */
	public String getParamColumn() {
		return paramColumn;
	}
	/**
	 * @param paramColumn the paramColumn to set
	 */
	public void setParamColumn(String paramColumn) {
		this.paramColumn = paramColumn;
	}
	/**
	 * @return the deviceType
	 */
	public String getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the displayContext
	 */
	public String getDisplayContext() {
		return displayContext;
	}
	/**
	 * @param displayContext the displayContext to set
	 */
	public void setDisplayContext(String displayContext) {
		this.displayContext = displayContext;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the paramType
	 */
	public String getParamType() {
		return paramType;
	}
	/**
	 * @param paramType the paramType to set
	 */
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return loginType;
	}
	/**
	 * @param loginType the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	/**
	 * @return the displayType
	 */
	public String getDisplayType() {
		return displayType;
	}
	/**
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}
	/**
	 * @return the displayTitle
	 */
	public String getDisplayTitle() {
		return displayTitle;
	}
	/**
	 * @param displayTitle the displayTitle to set
	 */
	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}
	/**
	 * @return the fieldNotes
	 */
	public String getFieldNotes() {
		return fieldNotes;
	}
	/**
	 * @param fieldNotes the fieldNotes to set
	 */
	public void setFieldNotes(String fieldNotes) {
		this.fieldNotes = fieldNotes;
	}
	/**
	 * @return the displayFormat
	 */
	public String getDisplayFormat() {
		return displayFormat;
	}
	/**
	 * @param displayFormat the displayFormat to set
	 */
	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}
	/**
	 * @return the displaySize
	 */
	public String getDisplaySize() {
		return displaySize;
	}
	/**
	 * @param displaySize the displaySize to set
	 */
	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}
	/**
	 * @return the maxChars
	 */
	public String getMaxChars() {
		return maxChars;
	}
	/**
	 * @param maxChars the maxChars to set
	 */
	public void setMaxChars(String maxChars) {
		this.maxChars = maxChars;
	}
	/**
	 * @return the textAreaRows
	 */
	public String getTextAreaRows() {
		return textAreaRows;
	}
	/**
	 * @param textAreaRows the textAreaRows to set
	 */
	public void setTextAreaRows(String textAreaRows) {
		this.textAreaRows = textAreaRows;
	}
	/**
	 * @return the textAreaCols
	 */
	public String getTextAreaCols() {
		return textAreaCols;
	}
	/**
	 * @param textAreaCols the textAreaCols to set
	 */
	public void setTextAreaCols(String textAreaCols) {
		this.textAreaCols = textAreaCols;
	}
	/**
	 * @return the displayHint
	 */
	public String getDisplayHint() {
		return displayHint;
	}
	/**
	 * @param displayHint the displayHint to set
	 */
	public void setDisplayHint(String displayHint) {
		this.displayHint = displayHint;
	}
	/**
	 * @return the displayTooltip
	 */
	public String getDisplayTooltip() {
		return displayTooltip;
	}
	/**
	 * @param displayTooltip the displayTooltip to set
	 */
	public void setDisplayTooltip(String displayTooltip) {
		this.displayTooltip = displayTooltip;
	}
	/**
	 * @return the emptyErrorMessage
	 */
	public String getEmptyErrorMessage() {
		return emptyErrorMessage;
	}
	/**
	 * @param emptyErrorMessage the emptyErrorMessage to set
	 */
	public void setEmptyErrorMessage(String emptyErrorMessage) {
		this.emptyErrorMessage = emptyErrorMessage;
	}
	/**
	 * @return the invalidErrorMessage
	 */
	public String getInvalidErrorMessage() {
		return invalidErrorMessage;
	}
	/**
	 * @param invalidErrorMessage the invalidErrorMessage to set
	 */
	public void setInvalidErrorMessage(String invalidErrorMessage) {
		this.invalidErrorMessage = invalidErrorMessage;
	}
	/**
	 * @return the validationRequired
	 */
	public String getValidationRequired() {
		return validationRequired;
	}
	/**
	 * @param validationRequired the validationRequired to set
	 */
	public void setValidationRequired(String validationRequired) {
		this.validationRequired = validationRequired;
	}
	/**
	 * @return the validationRules
	 */
	public String getValidationRules() {
		return validationRules;
	}
	/**
	 * @param validationRules the validationRules to set
	 */
	public void setValidationRules(String validationRules) {
		this.validationRules = validationRules;
	}
	/**
	 * @return the validateMaxChars
	 */
	public String getValidateMaxChars() {
		return validateMaxChars;
	}
	/**
	 * @param validateMaxChars the validateMaxChars to set
	 */
	public void setValidateMaxChars(String validateMaxChars) {
		this.validateMaxChars = validateMaxChars;
	}
	/**
	 * @return the validateMinValue
	 */
	public String getValidateMinValue() {
		return validateMinValue;
	}
	/**
	 * @param validateMinValue the validateMinValue to set
	 */
	public void setValidateMinValue(String validateMinValue) {
		this.validateMinValue = validateMinValue;
	}
	/**
	 * @return the validateMaxValue
	 */
	public String getValidateMaxValue() {
		return validateMaxValue;
	}
	/**
	 * @param validateMaxValue the validateMaxValue to set
	 */
	public void setValidateMaxValue(String validateMaxValue) {
		this.validateMaxValue = validateMaxValue;
	}
	/**
	 * @return the listLabels
	 */
	public String getListLabels() {
		return listLabels;
	}
	/**
	 * @param listLabels the listLabels to set
	 */
	public void setListLabels(String listLabels) {
		this.listLabels = listLabels;
	}
	/**
	 * @return the listValues
	 */
	public String getListValues() {
		return listValues;
	}
	/**
	 * @param listValues the listValues to set
	 */
	public void setListValues(String listValues) {
		this.listValues = listValues;
	}
	/**
	 * @return the listInitialValues
	 */
	public String getListInitialValues() {
		return listInitialValues;
	}
	/**
	 * @param listInitialValues the listInitialValues to set
	 */
	public void setListInitialValues(String listInitialValues) {
		this.listInitialValues = listInitialValues;
	}
	public static List<LoginDetails> createDummyData() {
		
		List<LoginDetails> loginDetailsList = Lists.newArrayList();
		
		LoginDetails loginDetails = new LoginDetails();
		loginDetails.setPlacement("1");
		loginDetails.setParamTable("customer");
		loginDetails.setParamColumn("first_name");
		loginDetails.setDeviceType("online");
		loginDetails.setDisplayContext(null);
		loginDetails.setId("1");
		loginDetails.setParamType("string");
		loginDetails.setLoginType("login");
		loginDetails.setDisplayType("textbox");
		loginDetails.setDisplayTitle("LOGIN_FIRST_NAME");
		loginDetails.setFieldNotes(null);
		loginDetails.setDisplayFormat(null);
		loginDetails.setDisplaySize("30");
		loginDetails.setMaxChars("50");
		loginDetails.setTextAreaRows("0");
		loginDetails.setTextAreaCols("0");
		loginDetails.setDisplayHint(null);
		loginDetails.setDisplayTooltip(null);
		loginDetails.setEmptyErrorMessage("LOGIN_FIRSTNAME_NAME_EMPTY_ERROR");
		loginDetails.setInvalidErrorMessage("LOGIN_FIRSTNAME_INVALID_ERROR");
		loginDetails.setValidationRequired("N");
		loginDetails.setValidationRules("alpha, space, hypen, single-quote");
		loginDetails.setValidateMaxChars("50");
		loginDetails.setValidateMinValue("0");
		loginDetails.setValidateMaxValue("0");
		loginDetails.setListLabels(null);
		loginDetails.setListValues(null);
		loginDetails.setListInitialValues(null);
		
		LoginDetails loginDetails0 = new LoginDetails();
		loginDetails0.setPlacement("2");
		loginDetails0.setParamTable("customer");
		loginDetails0.setParamColumn("last_name");
		loginDetails0.setDeviceType("online");
		loginDetails0.setDisplayContext(null);
		loginDetails0.setId("2");
		loginDetails0.setParamType("string");
		loginDetails0.setLoginType("login");
		loginDetails0.setDisplayType("textbox");
		loginDetails0.setDisplayTitle("LOGIN_LAST_NAME");
		loginDetails0.setFieldNotes(null);
		loginDetails0.setDisplayFormat(null);
		loginDetails0.setDisplaySize("30");
		loginDetails0.setMaxChars("50");
		loginDetails0.setTextAreaRows("0");
		loginDetails0.setTextAreaCols("0");
		loginDetails0.setDisplayHint(null);
		loginDetails0.setDisplayTooltip(null);
		loginDetails0.setEmptyErrorMessage("LOGIN_LAST_NAME_EMPTY_ERROR");
		loginDetails0.setInvalidErrorMessage("LOGIN_LAST_NAME_INVALID_ERROR");
		loginDetails0.setValidationRequired("Y");
		loginDetails0.setValidationRules("alpha, space, hypen, single-quote");
		loginDetails0.setValidateMaxChars("50");
		loginDetails0.setValidateMinValue("0");
		loginDetails0.setValidateMaxValue("0");
		loginDetails0.setListLabels(null);
		loginDetails0.setListValues(null);
		loginDetails0.setListInitialValues(null);
		
		LoginDetails loginDetails1 = new LoginDetails();
		loginDetails1.setPlacement("3");
		loginDetails1.setParamTable("customer");
		loginDetails1.setParamColumn("contact_phone");
		loginDetails1.setDeviceType("online");
		loginDetails1.setDisplayContext(null);
		loginDetails1.setId("3");
		loginDetails1.setParamType("string");
		loginDetails1.setLoginType("login");
		loginDetails1.setDisplayType("textbox-3-3-4");
		loginDetails1.setDisplayTitle("LOGIN_CONTACT_PHONE");
		loginDetails1.setFieldNotes("format: xxx-xxx-xxxx");
		loginDetails1.setDisplayFormat(null);
		loginDetails1.setDisplaySize("10");
		loginDetails1.setMaxChars("15");
		loginDetails1.setTextAreaRows("0");
		loginDetails1.setTextAreaCols("0");
		loginDetails1.setDisplayHint(null);
		loginDetails1.setDisplayTooltip(null);
		loginDetails1.setEmptyErrorMessage("LOGIN_CONTACT_PHONE_EMPTY_ERROR");
		loginDetails1.setInvalidErrorMessage("LOGIN_CONTACT_PHONE_INVALID_ERROR");
		loginDetails1.setValidationRequired("Y");
		loginDetails1.setValidationRules("phone");
		loginDetails1.setValidateMaxChars("10");
		loginDetails1.setValidateMinValue("0");
		loginDetails1.setValidateMaxValue("0");
		loginDetails1.setListLabels(null);
		loginDetails1.setListValues(null);
		loginDetails1.setListInitialValues(null);
		
		LoginDetails loginDetails2 = new LoginDetails();
		loginDetails2.setPlacement("4");
		loginDetails2.setParamTable("customer");
		loginDetails2.setParamColumn("attrib1");
		loginDetails2.setDeviceType("online");
		loginDetails2.setDisplayContext(null);
		loginDetails2.setId("4");
		loginDetails2.setParamType("int");
		loginDetails2.setLoginType("update");
		loginDetails2.setDisplayType("radio");
		loginDetails2.setDisplayTitle("LOGIN_AGE_CATEGORY");
		loginDetails2.setFieldNotes(null);
		loginDetails2.setDisplayFormat(null);
		loginDetails2.setDisplaySize("60");
		loginDetails2.setMaxChars("0");
		loginDetails2.setTextAreaRows("0");
		loginDetails2.setTextAreaCols("0");
		loginDetails2.setDisplayHint(null);
		loginDetails2.setDisplayTooltip(null);
		loginDetails2.setEmptyErrorMessage("LOGIN_AGE_CATEGORY_EMPTY_ERROR");
		loginDetails2.setInvalidErrorMessage("LOGIN_AGE_CATEGORY_INVALID_ERROR");
		loginDetails2.setValidationRequired("N");
		loginDetails2.setValidationRules(null);
		loginDetails2.setValidateMaxChars("1");
		loginDetails2.setValidateMinValue("0");
		loginDetails2.setValidateMaxValue("0");
		loginDetails2.setListLabels("18 or under, 19 - 65, over 65");
		loginDetails2.setListValues("1,2,3");
		loginDetails2.setListInitialValues("2");
		
		LoginDetails loginDetails3 = new LoginDetails();
		loginDetails3.setPlacement("5");
		loginDetails3.setParamTable("customer");
		loginDetails3.setParamColumn("attrib2");
		loginDetails3.setDeviceType("online");
		loginDetails3.setDisplayContext(null);
		loginDetails3.setId("5");
		loginDetails3.setParamType("string");
		loginDetails3.setLoginType("update");
		loginDetails3.setDisplayType("select");
		loginDetails3.setDisplayTitle("LOGIN_STATE");
		loginDetails3.setFieldNotes(null);
		loginDetails3.setDisplayFormat(null);
		loginDetails3.setDisplaySize("60");
		loginDetails3.setMaxChars("0");
		loginDetails3.setTextAreaRows("0");
		loginDetails3.setTextAreaCols("0");
		loginDetails3.setDisplayHint(null);
		loginDetails3.setDisplayTooltip(null);
		loginDetails3.setEmptyErrorMessage("LOGIN_STATE_EMPTY_ERROR");
		loginDetails3.setInvalidErrorMessage("LOGIN_STATE_INVALID_ERROR");
		loginDetails3.setValidationRequired("N");
		loginDetails3.setValidationRules(null);
		loginDetails3.setValidateMaxChars("2");
		loginDetails3.setValidateMinValue("0");
		loginDetails3.setValidateMaxValue("0");
		loginDetails3.setListLabels("Alabama,Alaska,American Samoa,Arizona,Arkansas,California,Colorado,Connecticut,Delaware,District of Columbia,Florida,Georgia,Guam,Hawaii,Idaho,Illinois,Indiana,Iowa,Kansas,Kentucky,Louisiana,Maine,Maryland,Massachusetts,Michigan,Minnesota,Mississippi,Missouri,Montana,Nebraska,Nevada,New Hampshire,New Jersey,New Mexico,New York,North Carolina,North Dakota,Northern Marianas Islands,Ohio,Oklahoma,Oregon,Pennsylvania,Puerto Rico,Rhode Island,South Carolina,South Dakota,Tennessee,Texas,Utah,Vermont,Virginia,Virgin Islands,Washington,West Virginia,Wisconsin,Wyoming");
		loginDetails3.setListValues("AL,AK,AS,AZ,AR,CA,CO,CT,DE,DC,FL,GA,GU,HI,ID,IL,IN,IA,KS,KY,LA,ME,MD,MA,MI,MN,MS,MO,MT,NE,NV,NH,NJ,NM,NY,NC,ND,MP,OH,OK,OR,PA,PR,RI,SC,SD,TN,TX,UT,VT,VA,VI,WA,WV,WI,WY");
		loginDetails3.setListInitialValues("TN");
		
		loginDetailsList.add(loginDetails);
		loginDetailsList.add(loginDetails0);
		loginDetailsList.add(loginDetails1);
		loginDetailsList.add(loginDetails2);
		loginDetailsList.add(loginDetails3);

		return loginDetailsList;
	}
	public Map<Integer, String> getMap() {
		return map;
	}
	public void setMap(Map<Integer, String> map) {
		this.map = map;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}

	
	
}
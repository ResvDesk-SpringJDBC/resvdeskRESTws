package com.telappoint.resvdeskrestws.common.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class LoginField {

	// display field label name, type, required field or not.
	private String displayTitle;
	private String displayContext;
	private String displayType;
	private String fieldName; // param column
	private String javaRef;  // java field Name.

	//private String displayNote; // field note - use this later
	private String listInitValue; // initial values of the fields
	private char isMandatory; // required

	// specifies validation is required or not.
	private String validationRequired;

	private String displaySize; // text box size
	private String maxLength;

	// for validation
	private String validateMinValue;
	private String validateMaxValue;
	private String validateMaxChars;
	
	private String initValue;
	private String validateMinChars;
	private String listLabels;
	private String listValues;

	// validation message
	private String emptyErrorMessage;
	private String invalidErrorMessage;

	// validate rules
	private String validateRules;
	
	// This is required for internal
	@JsonIgnore
	private String loginType;
	
	private int storageSize;
	private String storageType;

	// drop down fields - TODO: we will use below two fields later
	//private List<Options> optionsList;
	//private String defaultOptionValue;
	
	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	// this field is required for internal purpose
	@JsonIgnore
	private int placement;

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/*public String getDisplayNote() {
		return displayNote;
	}

	public void setDisplayNote(String displayNote) {
		this.displayNote = displayNote;
	}
	*/

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public char getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(char isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getValidationRequired() {
		return validationRequired;
	}

	public void setValidationRequired(String validationRequired) {
		this.validationRequired = validationRequired;
	}

	public String getValidateMinValue() {
		return validateMinValue;
	}

	public void setValidateMinValue(String validateMinValue) {
		this.validateMinValue = validateMinValue;
	}

	public String getValidateMaxValue() {
		return validateMaxValue;
	}

	public void setValidateMaxValue(String validateMaxValue) {
		this.validateMaxValue = validateMaxValue;
	}

	public String getValidateMaxChars() {
		return validateMaxChars;
	}

	public void setValidateMaxChars(String validateMaxChars) {
		this.validateMaxChars = validateMaxChars;
	}

	public String getEmptyErrorMessage() {
		return emptyErrorMessage;
	}

	public void setEmptyErrorMessage(String emptyErrorMessage) {
		this.emptyErrorMessage = emptyErrorMessage;
	}

	public String getInvalidErrorMessage() {
		return invalidErrorMessage;
	}

	public void setInvalidErrorMessage(String invalidErrorMessage) {
		this.invalidErrorMessage = invalidErrorMessage;
	}

	public String getValidateRules() {
		return validateRules;
	}

	public void setValidateRules(String validateRules) {
		this.validateRules = validateRules;
	}

	public String getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}

	public int getPlacement() {
		return placement;
	}

	public void setPlacement(int placement) {
		this.placement = placement;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getValidateMinChars() {
		return validateMinChars;
	}

	public void setValidateMinChars(String validateMinChars) {
		this.validateMinChars = validateMinChars;
	}

	public String getListInitValue() {
		return listInitValue;
	}

	public void setListInitValue(String listInitValue) {
		this.listInitValue = listInitValue;
	}

	public String getJavaRef() {
		return javaRef;
	}

	public void setJavaRef(String javaRef) {
		this.javaRef = javaRef;
	}

	public String getDisplayContext() {
		return displayContext;
	}

	public void setDisplayContext(String displayContext) {
		this.displayContext = displayContext;
	}

	public String getListValues() {
		return listValues;
	}

	public void setListValues(String listValues) {
		this.listValues = listValues;
	}

	public String getListLabels() {
		return listLabels;
	}

	public void setListLabels(String listLabels) {
		this.listLabels = listLabels;
	}
}

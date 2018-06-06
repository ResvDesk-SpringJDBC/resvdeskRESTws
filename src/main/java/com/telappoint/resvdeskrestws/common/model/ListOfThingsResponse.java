package com.telappoint.resvdeskrestws.common.model;
/**
 * 
 * @author Balaji N
 *
 */
public class ListOfThingsResponse extends BaseResponse {
	
	private String rightSideDisplayHeader;
	private String deplayText;

	public String getDeplayText() {
		return deplayText;
	}

	public void setDeplayText(String deplayText) {
		this.deplayText = deplayText;
	}

	public String getRightSideDisplayHeader() {
		return rightSideDisplayHeader;
	}

	public void setRightSideDisplayHeader(String rightSideDisplayHeader) {
		this.rightSideDisplayHeader = rightSideDisplayHeader;
	}

}

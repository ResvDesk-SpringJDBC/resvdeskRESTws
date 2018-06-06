package com.telappoint.resvdeskrestws.admin.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class DailyCalendarView extends BaseResponse {

	public Object[][] caleandarView;
	
	private String title;
	private String javaRef;
	private String hides;

	public void init(int row, int column) {
		caleandarView = new Object[row][column];
	}

	public Object[][] getCaleandarView() {
		return caleandarView;
	}

	public void setCaleandarView(Object[][] caleandarView) {
		this.caleandarView = caleandarView;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJavaRef() {
		return javaRef;
	}

	public void setJavaRef(String javaRef) {
		this.javaRef = javaRef;
	}

	public String getHides() {
		return hides;
	}

	public void setHides(String hides) {
		this.hides = hides;
	}

}

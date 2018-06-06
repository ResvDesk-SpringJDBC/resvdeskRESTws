package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class TablePrintViewResponse extends BaseResponse {
	
	private Map<TablePrintView, List<SeatView>> tablePrintView;
	private String javaRefs;
	private String titles;
	private String hides;
	
	public Map<TablePrintView, List<SeatView>> getTablePrintView() {
		return tablePrintView;
	}

	public void setTablePrintView(Map<TablePrintView, List<SeatView>> tablePrintView) {
		this.tablePrintView = tablePrintView;
	}

	public String getJavaRefs() {
		return javaRefs;
	}
	
	
	public void setJavaRefs(String javaRefs) {
		this.javaRefs = javaRefs;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public String getHides() {
		return hides;
	}

	public void setHides(String hides) {
		this.hides = hides;
	}
	
}
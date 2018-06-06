package com.telappoint.resvdeskrestws.admin.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.telappoint.resvdeskrestws.common.model.BaseResponse;
import com.telappoint.resvdeskrestws.common.model.Customer;

/**
 * 
 * @author Balaji N
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ClientDetailsResponse extends BaseResponse {
	private List<Customer> customerList;
	private String javaRefs;
	private String titles;
	private String hides;

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
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

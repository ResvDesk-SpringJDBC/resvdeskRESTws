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
public class CustomerResponse extends BaseResponse {
	List<Customer> customerList;

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}
}

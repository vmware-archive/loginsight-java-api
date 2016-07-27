/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi.core;

public class OrderBy {
	
	public enum OrderByFunction {
		SAMPLE,
		NONE,
		COUNT,
		UCOUNT,
		MIN,
		MAX,
		SUM,
		STDEV,
		VARIANCE
	}
	
	public enum Direction {
		ASC,
		DESC
	}
	private final String orderByFunction;
	private final String orderByField;
	private final Direction orderByDirection;
	
	public OrderBy(String orderByFunction, String orderByField, Direction orderByDirection) {
		this.orderByFunction = orderByFunction;
		this.orderByField = orderByField;
		this.orderByDirection = orderByDirection;
	}
	
	public String getOrderByFunction() {
		return this.orderByFunction;
	}
	
	public String getOrderByField() {
		return this.orderByField;
	}
	
	public Direction getOrderByDirection() {
		return this.orderByDirection;
	}

}

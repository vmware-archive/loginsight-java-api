/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi.core;

import com.vmware.loginsightapi.AggregateQueryBuilder.AggregationFunction;

/**
 * Class representing the OrderBy clause in the aggregate query.
 */
public class OrderBy {

	/**
	 * Enum for OrderByFunction
	 * 
	 * @see AggregationFunction
	 */
	public enum OrderByFunction {
		/**
		 * An arbitrary event in the bin
		 */
		SAMPLE,

		/**
		 * Indicates that Order by is not any aggregate function.
		 */
		NONE,

		/**
		 * Count of the events in each bin
		 */
		COUNT,

		/**
		 * Count of unique values in the bin
		 */
		UCOUNT,

		/**
		 * Minimum value in the bin
		 */
		MIN,

		/**
		 * Maximum value in the bin
		 */
		MAX,

		/**
		 * Sum of the values in the bin
		 */
		SUM,

		/**
		 * Standard deviation of the values in the bin
		 */
		STDEV,

		/**
		 * Variance of the values in the bin
		 */
		VARIANCE
	}

	/**
	 * Enum for Direction in OrderBy clause
	 *
	 */
	public enum Direction {
		
		/**
		 * Ascending order
		 */
		ASC, 
		
		/**
		 * Descending order
		 */
		DESC
	}

	private final OrderByFunction orderByFunction;
	private final String orderByField;
	private final Direction orderByDirection;

	/**
	 * Constructs OrderBy object using orderByFunction, orderByField,
	 * orderByDirection
	 * 
	 * @param orderByFunction
	 *            order by function
	 * @param orderByField
	 *            order by field
	 * @param orderByDirection
	 *            order by direction
	 */
	public OrderBy(OrderBy.OrderByFunction orderByFunction, String orderByField, Direction orderByDirection) {
		this.orderByFunction = orderByFunction;
		this.orderByField = orderByField;
		this.orderByDirection = orderByDirection;
	}

	/**
	 * Getter for orderByFunction
	 * 
	 * @return order by function
	 */
	public OrderByFunction getOrderByFunction() {
		return this.orderByFunction;
	}

	/**
	 * Getter for order by field
	 * 
	 * @return order by field
	 */
	public String getOrderByField() {
		return this.orderByField;
	}

	/**
	 * Getter for order by direction
	 * 
	 * @return order by direction
	 */
	public Direction getOrderByDirection() {
		return this.orderByDirection;
	}

}

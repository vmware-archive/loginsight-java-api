/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vmware.loginsightapi.core.GroupBy;
import com.vmware.loginsightapi.core.GroupByDynamicBinWidth;
import com.vmware.loginsightapi.core.GroupByFixedBinWidth;
import com.vmware.loginsightapi.core.OrderBy;

/**
 * Builder class for creating AggregateQueries.
 * 
 * @author gopalk
 *
 */
public class AggregateQueryBuilder extends QueryBuilder {

	/**
	 * Enum for aggregate function
	 *
	 */
	public enum AggregationFunction {

		/**
		 * Count of the events in each bin
		 */
		COUNT,

		/**
		 * Count of unique values in the bin
		 */
		UCOUNT,

		/**
		 * Average of the values in the bin
		 */
		AVG,

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
		VARIANCE,

		/**
		 * An arbitrary event in the bin
		 */
		SAMPLE
	}

	/**
	 * Relative URL for querying event groups
	 */
	public static final String API_URL_AGGREGATED_EVENTS_PATH = "/api/v1/aggregated-events/";

	/**
	 * Default width of the bin
	 */
	public static final int DEFAULT_BIN_WIDTH = 5000;

	/**
	 * Default aggregate function (COUNT)
	 */
	public static final AggregationFunction DEFAULT_AGGREGATION_FUNCTION = AggregationFunction.COUNT;
	private int binWidth;
	private AggregationFunction aggregationFunction;
	private String aggregationField;
	private List<GroupBy> groupBy;
	private List<OrderBy> orderBys;

	/**
	 * Default constructor.
	 */
	public AggregateQueryBuilder() {
		super();
		this.binWidth = AggregateQueryBuilder.DEFAULT_BIN_WIDTH;
		this.aggregationFunction = AggregateQueryBuilder.DEFAULT_AGGREGATION_FUNCTION;
		this.groupBy = new ArrayList<GroupBy>();
		this.orderBys = new ArrayList<OrderBy>();
	}

	/**
	 * The time-span of time range bins, in milliseconds. A special "all"
	 * bin-width will effectively group by over the entire time range.
	 * 
	 * @param binWidth
	 *            bin width
	 * @return AggregateQueryBuilder (this) object
	 */
	public AggregateQueryBuilder binWidth(int binWidth) {
		this.binWidth = binWidth;
		return this;
	}

	/**
	 * 
	 * General purpose aggregator method. Recommended to use specific aggregate
	 * methods.
	 * 
	 * @param aggregateFunc
	 *            Aggregate function
	 *            {@link AggregateQueryBuilder.AggregationFunction}
	 * @param aggregationField
	 *            Aggregation field
	 * @return AggregateQueryBuilder (this) object
	 * @deprecated
	 */
	@Deprecated
	public AggregateQueryBuilder aggregator(AggregationFunction aggregateFunc, String aggregationField) {
		this.aggregationFunction = aggregateFunc;
		if ((aggregateFunc == AggregationFunction.COUNT || aggregateFunc == AggregationFunction.SAMPLE)
				&& aggregationField != null) {
			// warning: aggregationField is not supported for COUNT and SAMPLE
			this.aggregationField = null;
		}
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * Registers the aggregate function as count
	 * 
	 * @return AggregateQueryBuilder instance (this)
	 */

	public AggregateQueryBuilder count() {
		this.aggregationFunction = AggregationFunction.COUNT;
		this.aggregationField = null;
		return this;
	}

	/**
	 * Registers the aggregate function as sample
	 * 
	 * @return AggregateQueryBuilder instance (this)
	 */

	public AggregateQueryBuilder sample() {
		this.aggregationFunction = AggregationFunction.SAMPLE;
		this.aggregationField = null;
		return this;
	}

	/**
	 * Registers the aggregate function as ucount (Unique Count)
	 * 
	 * @param aggregationField
	 *            name of the field whose unique count to be calculated.
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder ucount(String aggregationField) {
		this.aggregationFunction = AggregationFunction.UCOUNT;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * Registers the aggregate function as average
	 * 
	 * @param aggregationField
	 *            name of the field whose average to be calculated.
	 * @return AggregateQueryBuilder instance (this)
	 */

	public AggregateQueryBuilder average(String aggregationField) {
		this.aggregationFunction = AggregationFunction.AVG;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * Registers the aggregate function as minimum
	 * 
	 * @param aggregationField
	 *            name of the field whose minimum to be calculated.
	 * @return AggregateQueryBuilder object (this)
	 */
	public AggregateQueryBuilder min(String aggregationField) {
		this.aggregationFunction = AggregationFunction.MIN;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * Registers the aggregate function as maximum
	 * 
	 * @param aggregationField
	 *            name of the field whose minimum to be calculated.
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder max(String aggregationField) {
		this.aggregationFunction = AggregationFunction.MAX;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * Registers the aggregate function as sum
	 * 
	 * @param aggregationField
	 *            name of the field whose sum to be calculated
	 * @return AggregateQueryBuilder instance (this)
	 */

	public AggregateQueryBuilder sum(String aggregationField) {
		this.aggregationFunction = AggregationFunction.SUM;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * Registers the aggregate function as stdev (standard deviation)
	 * 
	 * @param aggregationField
	 *            name of the field whose stdev to be calculated
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder stdev(String aggregationField) {
		this.aggregationFunction = AggregationFunction.STDEV;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * Registers the aggregate function as variance (standard deviation)
	 * 
	 * @param aggregationField
	 *            name of the field whose variance to be calculated
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder variance(String aggregationField) {
		this.aggregationFunction = AggregationFunction.VARIANCE;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * GroupBy bins are configured to fixed bin width. Group by field and bin
	 * width must be supplied.
	 * 
	 * @param groupByField name of the group by field
	 * @param binWidth     Width of the bin
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder groupByFixedBinWidth(String groupByField, int binWidth) {
		this.groupBy.add(new GroupByFixedBinWidth(groupByField, binWidth));
		return this;
	}

	/**
	 * GroupBy bins are configured to dynamic widths. Group by field and bins
	 * must be supplied in a comma separated string (like 10,20,50)
	 * 
	 * @param groupByField
	 *            name of the group by field
	 * @param bins
	 *            Comma separated list of bin widths (10,20,50)
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder groupByDynamicBins(String groupByField, String bins) {
		this.groupBy.add(new GroupByDynamicBinWidth(groupByField, bins));
		return this;
	}

	/**
	 * Registers list of GroupBy objects. <br>
	 * Calling this function will replace any of the previously defined GrouBy
	 * clauses.
	 * 
	 * @param groupBy
	 *            list of GroupBy objects
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder setGroupBy(List<GroupBy> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	/**
	 * Adds an orderBy clause to the existing event group query builder
	 * 
	 * @param orderByFunction
	 *            OrderBy function (Aggregation functions + NONE)
	 * @param orderByField
	 *            Field name in the orderBy clause
	 * @param orderByDirection
	 *            Direction for orderBy clause (ASC/DESC)
	 * @return AggregateQueryBuilder instance (this)
	 * @see OrderBy
	 */
	public AggregateQueryBuilder orderBy(String orderByFunction, String orderByField,
			OrderBy.Direction orderByDirection) {
		this.orderBys.add(new OrderBy(orderByFunction, orderByField, orderByDirection));
		return this;
	}

	/**
	 * Registers list of OrderBy objects. <br>
	 * Calling this function will replace any of the previously defined OrderBy
	 * clauses.
	 * 
	 * @param orderBy
	 *            list of OrderBy objects
	 * @return AggregateQueryBuilder instance (this)
	 */
	public AggregateQueryBuilder setOrderBy(List<OrderBy> orderBy) {
		this.orderBys = orderBy;
		return this;
	}

	/**
	 * Builds Url parameters for aggregate query
	 * 
	 * @return relative URL string containing aggregated query
	 */
	@Override
	protected String buildUrlParameters() {
		List<String> urlParams = new ArrayList<String>();

		if (limit != QueryBuilder.DEFAULT_LIMIT || this.includeDefaults) {
			urlParams.add("limit=" + limit);
		}
		if (timeout != QueryBuilder.DEFAULT_TIMEOUT || this.includeDefaults) {
			urlParams.add("timeout=" + timeout);
		}
		if (binWidth != DEFAULT_BIN_WIDTH || this.includeDefaults) {
			urlParams.add("bin-width=" + binWidth);
		}
		if (aggregationFunction != DEFAULT_AGGREGATION_FUNCTION || this.includeDefaults) {
			if (aggregationFunction == AggregationFunction.COUNT || aggregationFunction == AggregationFunction.SAMPLE) {
				urlParams.add("aggregation-function=" + aggregationFunction);
			} else {
				urlParams.add("aggregation-function=" + aggregationFunction + "&aggregation-field=" + aggregationField);
			}
		}

		// better to collect to new list and perform addAll to respect FP
		// principles.
		List<String> contentPackParams = contentPackFields.stream()
				.map(contentPack -> "content-pack-fields=" + contentPack).collect(Collectors.toList());

		List<String> groupByParams = groupBy.stream().map(groupBy -> {
			String paramString = "";
			if (groupBy instanceof GroupByFixedBinWidth) {
				paramString += "group-by-field=" + groupBy.getGroupByField() + "&bin-width=" + groupBy.getBinWidth();
			} else if (groupBy instanceof GroupByDynamicBinWidth) {
				paramString += "group-by-field=" + groupBy.getGroupByField() + "&bins=" + groupBy.getBins();
			}
			return paramString;
		}).collect(Collectors.toList());
		List<String> orderByParams = orderBys.stream().map(orderBy -> {
			String paramString = "order-by-function=" + orderBy.getOrderByFunction();
			if (null != orderBy.getOrderByField()) {
				paramString += "&order-by-field=" + orderBy.getOrderByField();
			}
			if (null != orderBy.getOrderByDirection()) {
				paramString += "&order-by-direction=" + orderBy.getOrderByDirection();
			}
			return paramString;
		}).collect(Collectors.toList());

		urlParams.addAll(orderByParams);
		urlParams.addAll(contentPackParams);
		urlParams.addAll(groupByParams);

		return urlParams.parallelStream().collect(Collectors.joining("&"));
	}

	/**
	 * Builds the url segment based on the field constraints and other url
	 * parameters supplied. <br>
	 * This URL is a relative URL path that can be combined with loginsight
	 * server and port prefix {@code https://loginsight-host:port/ }
	 * 
	 * @return relative URL string containing aggregated query
	 */
	@Override
	public String toUrlString() {
		String path = buildPathSegment();
		String params = buildUrlParameters();
		String url = API_URL_AGGREGATED_EVENTS_PATH;
		if (!path.isEmpty()) {
			url += path;
		}
		if (!params.isEmpty()) {
			url += "?" + params;
		}
		return url;
	}

}

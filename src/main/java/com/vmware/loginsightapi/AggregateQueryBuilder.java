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

public class AggregateQueryBuilder extends QueryBuilder {

	public enum AggregationFunction {
		COUNT, UCOUNT, AVG, MIN, MAX, SUM, STDEV, VARIANCE, SAMPLE
	}

	public static final String API_URL_AGGREGATED_EVENTS_PATH = "/api/v1/aggregated-events/";
	private static final int DEFAULT_BIN_WIDTH = 5000;
	private static final AggregationFunction DEFAULT_AGGREGATION_FUNCTION = AggregationFunction.COUNT;
	private int binWidth;
	private AggregationFunction aggregationFunction;
	private String aggregationField;
	private List<GroupBy> groupBy;
	private List<OrderBy> orderBys;

	public AggregateQueryBuilder() {
		super();
		this.binWidth = AggregateQueryBuilder.DEFAULT_BIN_WIDTH;
		this.aggregationFunction = AggregateQueryBuilder.DEFAULT_AGGREGATION_FUNCTION;
		this.groupBy = new ArrayList<GroupBy>();
		this.orderBys = new ArrayList<OrderBy>();
	}

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
	 * @param aggregationField
	 * @return
	 */
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
	 * 
	 * @return
	 */

	public AggregateQueryBuilder count() {
		this.aggregationFunction = AggregationFunction.COUNT;
		this.aggregationField = null;
		return this;
	}

	/**
	 * 
	 * @return
	 */

	public AggregateQueryBuilder sample() {
		this.aggregationFunction = AggregationFunction.SAMPLE;
		this.aggregationField = null;
		return this;
	}

	/**
	 * 
	 * @param aggregationField
	 * @return
	 */
	public AggregateQueryBuilder ucount(String aggregationField) {
		this.aggregationFunction = AggregationFunction.UCOUNT;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * 
	 * @param aggregationField
	 * @return
	 */

	public AggregateQueryBuilder average(String aggregationField) {
		this.aggregationFunction = AggregationFunction.AVG;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * 
	 * @param aggregationField
	 * @return
	 */
	public AggregateQueryBuilder min(String aggregationField) {
		this.aggregationFunction = AggregationFunction.MIN;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * 
	 * @param aggregationField
	 * @return
	 */
	public AggregateQueryBuilder max(String aggregationField) {
		this.aggregationFunction = AggregationFunction.MAX;
		this.aggregationField = aggregationField;
		return this;
	}

	/**
	 * 
	 * @param aggregationField
	 * @return
	 */

	public AggregateQueryBuilder sum(String aggregationField) {
		this.aggregationFunction = AggregationFunction.SUM;
		this.aggregationField = aggregationField;
		return this;
	}

	public AggregateQueryBuilder stdev(String aggregationField) {
		this.aggregationFunction = AggregationFunction.STDEV;
		this.aggregationField = aggregationField;
		return this;
	}

	public AggregateQueryBuilder variance(String aggregationField) {
		this.aggregationFunction = AggregationFunction.VARIANCE;
		this.aggregationField = aggregationField;
		return this;
	}

	public AggregateQueryBuilder groupByFixedBinWidth(String groupByField, int binWidth) {
		this.groupBy.add(new GroupByFixedBinWidth(groupByField, binWidth));
		return this;
	}

	public AggregateQueryBuilder groupByDynamicBins(String groupByField, String bins) {
		this.groupBy.add(new GroupByDynamicBinWidth(groupByField, bins));
		return this;
	}

	public AggregateQueryBuilder setGroupBy(List<GroupBy> groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public AggregateQueryBuilder orderBy(String orderByFunction, String orderByField,
			OrderBy.Direction orderByDirection) {
		this.orderBys.add(new OrderBy(orderByFunction, orderByField, orderByDirection));
		return this;
	}

	public AggregateQueryBuilder setOrderBy(List<OrderBy> orderBy) {
		this.orderBys = orderBy;
		return this;
	}

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

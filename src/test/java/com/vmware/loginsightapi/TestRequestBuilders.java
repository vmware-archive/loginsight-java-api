/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */

package com.vmware.loginsightapi;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.vmware.loginsightapi.AggregateQueryBuilder;
import com.vmware.loginsightapi.MessageQueryBuilder;
import com.vmware.loginsightapi.RequestBuilders;
import com.vmware.loginsightapi.core.FieldConstraint;

/**
 * @author gopalk
 *
 */
public class TestRequestBuilders {

	/**
	 * Test method for
	 * {@link com.vmware.loginsightapi.RequestBuilders#messageQuery()}.
	 */
	@Test
	public void testMessageQuery() {
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1234").build();
		MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100)
				.setConstraints(constraints);
		assertEquals("Mismatch in query statement", MessageQueryBuilder.API_URL_EVENTS_PATH + "EQ+1234", mqb.toUrlString());
		System.out.println(mqb.toUrlString());
	}
	@Test
	public void testMessageQueryWithDefaults() {
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1234").build();
		MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().withDefaults().limit(100)
				.setConstraints(constraints);
		assertEquals("Mismatch in query statement", MessageQueryBuilder.API_URL_EVENTS_PATH + "EQ+1234?limit=100&timeout=30000", mqb.toUrlString());
		System.out.println(mqb.toUrlString());
	}
	
	@Test
	public void testAggregateQuery() {
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1234").build();
		AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().count().setConstraints(constraints);
		assertEquals("Mismatch in query statement", AggregateQueryBuilder.API_URL_AGGREGATED_EVENTS_PATH + "vclap_caseid/EQ+1234", aqb.toUrlString());
		System.out.println(aqb.toUrlString());
	}
	@Test
	public void testAggregateQueryWithDefaults() {
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1234").build();
		AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().withDefaults().limit(100)
				.setConstraints(constraints);
		assertEquals("Mismatch in query statement", AggregateQueryBuilder.API_URL_AGGREGATED_EVENTS_PATH + "vclap_caseid/EQ+1234?limit=100&timeout=30000&bin-width=5000&aggregation-function=COUNT", aqb.toUrlString());
		System.out.println(aqb.toUrlString());
	}

	@Test
	public void testStreams() {
		List<String> params = new ArrayList<String>();
		params.add("limit=100");
		params.add("timeout=1000");
		List<String> contentPackFields = new ArrayList<String>();
		contentPackFields.add("test");
		contentPackFields.add("test2");
		List<String> contentPackFields2 = new ArrayList<String>();
		String path = "http://localhost:9000";
		String relativePath = params.parallelStream().collect(Collectors.joining("&"));
		System.out.println("old " + relativePath);
		List<String> newParams = new ArrayList<String>();
		String newRelativePath = newParams.parallelStream().collect(Collectors.joining("&"));
		System.out.println("new " + newRelativePath.length());
		List<String> contentPackParams = contentPackFields2.stream().map(contentPack -> "param=" + contentPack)
				.collect(Collectors.toList());
		System.out.println("cnew " + contentPackParams.size());
	}

	/**
	 * Test method for
	 * {@link com.vmware.loginsightapi.RequestBuilders#aggreateQuery()}.
	 */
	@Test
	public void testAggreateQuery() {
		fail("Not yet implemented");
	}

}

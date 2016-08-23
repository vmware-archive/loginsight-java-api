/**
 * Copyright Â© 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the â€œLicenseâ€�); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.loginsightapi.core.FieldConstraint;
import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.IngestionResponse;
import com.vmware.loginsightapi.core.LogInsightConnectionStrategy;
import com.vmware.loginsightapi.core.Message;
import com.vmware.loginsightapi.core.MessageQueryResponse;
import com.vmware.loginsightapi.util.AsyncLogInsightConnectionStrategy;

@Ignore
public class TestLogInsightClient {

	private LogInsightClient client;
	private final static Logger logger = LoggerFactory.getLogger(TestLogInsightClient.class);
	String user;
	String password;
	Configuration config;

	@Before
	public void setUp() {
		Configuration config = Configuration.buildFromConfig("config-integration.properties");
		LogInsightConnectionStrategy<CloseableHttpAsyncClient> connectionStrategy = new AsyncLogInsightConnectionStrategy();
		client = new LogInsightClient(config, connectionStrategy);
	}

	@Test
	public void testMessageQuery() {
		long startTime = System.nanoTime();
		List<FieldConstraint> constraints = new ConstraintBuilder().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		MessageQuery mqb = (MessageQuery) new MessageQuery().limit(100)
				.setConstraints(constraints);
		CompletableFuture<MessageQueryResponse> responseFuture = client.messageQuery(mqb.toUrlString());
		MessageQueryResponse messages;
		try {
			messages = responseFuture.get();
			Assert.assertTrue("Invalid number of messages", messages.getEvents().size() <= 100);
			System.out.println("Returned " + messages.getEvents().size() + " messages");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		long duration = System.nanoTime() - startTime;
		logger.info("duration=" + duration);
	}

	@Test
	public void testAggregateQuery() {
		List<FieldConstraint> constraints =  new ConstraintBuilder().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		AggregateQuery aqb = (AggregateQuery) new AggregateQuery().limit(100)
				.setConstraints(constraints);
		client.aggregateQuery(aqb.toUrlString());
	}

	@Test
	public void testIngestion() {
		Message msg1 = new Message("Testing the ingestion");
		msg1.addField("vclap_test_id", "11111");
		IngestionRequest request = new IngestionRequest();
		request.addMessage(msg1);
		CompletableFuture<IngestionResponse> responseFuture = client.ingest(request);
		IngestionResponse response;
		try {
			response = responseFuture.get();
			System.out.println("Resonse " + response.getMessage() + response.getStatus() + response.getIngested());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

	@After
	public void tearDown() {
		client.stopAsyncHttpClient();
	}

}

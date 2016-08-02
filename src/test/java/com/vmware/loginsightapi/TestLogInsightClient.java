/**
 * Copyright Â© 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the â€œLicenseâ€�); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.loginsightapi.core.AggregateResponse;
import com.vmware.loginsightapi.core.FieldConstraint;
import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.IngestionResponse;
import com.vmware.loginsightapi.core.LogInsightApiError;
import com.vmware.loginsightapi.core.Message;
import com.vmware.loginsightapi.core.MessageQueryResponse;

@Ignore
public class TestLogInsightClient {

	private LogInsightClient client;
	private final static Logger logger = LoggerFactory.getLogger(TestLogInsightClient.class);
	String user;
	String password;

	@Before
	public void setUp() {
		user = System.getenv("user");
		password = System.getenv("password");
		Properties connectionConfig = new Properties();
		try {
			connectionConfig.load(getClass().getResourceAsStream("/config.properties"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		if (StringUtils.isEmpty(user)) {
			user = connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_USER);
		}
		
		if (StringUtils.isEmpty(password)) {
			password = connectionConfig.getProperty(LogInsightConnectionConfig.LOGINSIGHT_PASSWORD);
		}
		
		LogInsightConnectionStrategy<CloseableHttpAsyncClient> connectionStrategy = new AsyncLogInsightConnectionStrategy();
		client = new LogInsightClient(connectionStrategy, connectionConfig);
		client.connect(user, password);
	}

	@Test
	public void testMessageQuery() {
		long startTime = System.nanoTime();
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100)
				.setConstraints(constraints);
		MessageQueryResponse messages = client.messageQuery(mqb.toUrlString());
		Assert.assertTrue("Invalid number of messages", messages.getEvents().size() <= 100);
		System.out.println("Returned " + messages.getEvents().size() + " messages");
		long duration = System.nanoTime() - startTime;
		logger.info("duration=" + duration);
	}

	@Test
	public void testAggregateQuery() {
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().limit(100)
				.setConstraints(constraints);
		client.aggregateQuery(aqb.toUrlString());
	}
	
	@Test
	public void testAggregateQueryCallback() {
		long startTime = System.nanoTime();
		final CountDownLatch latch = new CountDownLatch(1);
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		AggregateQueryBuilder aqb = (AggregateQueryBuilder) RequestBuilders.aggreateQuery().limit(100)
				.setConstraints(constraints);
		client.aggregateQuery(aqb.toUrlString(), (AggregateResponse response, LogInsightApiError error) -> {
			if (error.isError()) {
				System.out.println("Call failed" + error.getMessage());
			} else {
				logger.info("Call completed");
				logger.info("Returned " + response.getBins().size() + " messages");
				Assert.assertTrue("Invalid number of messages", response.getBins().size() <= 100);
				long duration = System.nanoTime() - startTime;
				logger.info("duration=" + duration);
				latch.countDown();
			}
		});
		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			logger.info("Test Finished completely!!!");
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testMessageQueryLambda() {
		long startTime = System.nanoTime();
		logger.info("Starting Call completed");
		final CountDownLatch latch = new CountDownLatch(1);
		List<FieldConstraint> constraints = RequestBuilders.constraint().eq("vclap_caseid", "1423244")
				.gt("timestamp", "0").build();
		MessageQueryBuilder mqb = (MessageQueryBuilder) RequestBuilders.messageQuery().limit(100)
				.setConstraints(constraints);
		client.messageQuery(mqb.toUrlString(), (MessageQueryResponse response, LogInsightApiError error) -> {
			if (error.isError()) {
				System.out.println("Call failed" + error.getMessage());
			} else {
				logger.info("Call completed");
				logger.info("Returned " + response.getEvents().size() + " messages");
				Assert.assertTrue("Invalid number of messages", response.getEvents().size() <= 100);
				long duration = System.nanoTime() - startTime;
				logger.info("duration=" + duration);
				latch.countDown();
			}
		});
		try {
			latch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			logger.info("Test Finished completely!!!");
			e1.printStackTrace();
		}
	}

	@Test
	public void testIngestion() {
		Message msg1 = new Message("Testing the ingestion");
		msg1.addField("vclap_test_id", "11111");
		IngestionRequest request = new IngestionRequest();
		request.addMessage(msg1);
		IngestionResponse response = client.injest(request);
		System.out.println("Resonse " + response.getMessage() + response.getStatus() + response.getIngested());
	}

	@After
	public void tearDown() {
		client.stopAsyncHttpClient();
	}

}

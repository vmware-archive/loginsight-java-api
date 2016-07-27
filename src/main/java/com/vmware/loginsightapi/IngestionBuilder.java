/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.loginsightapi.core.Field;
import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.Message;

public class IngestionBuilder extends RequestBuilder {
	
	public final String INGESTION_API_URL = "/api/v1/messages/ingest/";
	public final String DEFAULT_AGENT_ID = "54947df8-0e9e-4471-a2f9-9af509fb5889";
	public final int DEFAULT_INGESTION_PORT = 9543;
	private String agentId;
	private int port;
	private Map<String, String> headers = new HashMap<>();
	private List<Message> messages;
	public IngestionBuilder() {
		agentId = DEFAULT_AGENT_ID;
		port = DEFAULT_INGESTION_PORT;
		headers.put("Content-Type", "application/json");
	}
	
	public IngestionBuilder addMessage(String text, Long timestamp, List<Field> fields) {
		messages.add(new Message(text, timestamp, fields));
		return this;
	}
	
	public String build() {
		IngestionRequest body = new IngestionRequest(this.messages);
		ObjectMapper mapper = new ObjectMapper();
		String bodyStr;
		try {
			bodyStr = mapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Unable serialize messages to string");
		} 
		return bodyStr;
	}
	
	public void send() {
		
	}

}

/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi.core;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class representing the LogInsight Ingestion request object
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IngestionRequest {
	private List<Message> messages;
	
	/**
	 * Constructs IngestionRequest object with empty list of messages
	 */
	public IngestionRequest () {
		messages = new ArrayList<Message>();
		
	}
	
	/**
	 * Constructs IngestionRequest object with provided list of messages
	 * 
	 * @param messages List of Message objects
	 */
	public IngestionRequest (List<Message> messages) {
		this.messages = messages;
	}

	/**
	 * Getter for messages
	 * 
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * Setter for messages
	 * 
	 * @param messages the messages to set
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	/**
	 * Add a message to existing messages
	 * 
	 * @param message Message object
	 */
	public void addMessage(Message message) {
		messages.add(message);
	}
	
	/**
	 * Add a list of messages to existing messages
	 * 
	 * @param messages List of Message objects
	 */
	public void addMessages(List<Message> messages) {
		this.messages.addAll(messages);
	}
	
	/**
	 * Get the count messages in IngestionRequest
	 * 
	 * @return count of messages
	 */
	public int count() {
		return this.messages.size();
	}
	
	/**
	 * Serializes the IngestionRequest to Json string
	 * 
	 * @return serialized json string of IngestinoRequest
	 */
	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		String body;
		try {
			body = mapper.writeValueAsString(this);
			return body;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Unable serialize messages to string");
		}
	}
}

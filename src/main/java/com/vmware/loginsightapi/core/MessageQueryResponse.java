/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi.core;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.loginsightapi.ParseException;

/**
 * Class representing the response of a message query.
 *
 */
public class MessageQueryResponse {

	private boolean complete;
	private int duration;
	private List<Message> events;

	/**
	 * Default constructor
	 */
	public MessageQueryResponse() {
	}

	/**
	 * Returns true if the query execution is complete or else returns false.
	 * 
	 * @return true or false
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Setter for marking the query execution as complete.
	 * 
	 * @param complete
	 *            true or false
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	/**
	 * Gets the execution duration of the query
	 * 
	 * @return duration in milliseconds
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the execution duration of the query
	 * 
	 * @param duration
	 *            query duration in milliseconds
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Get the list of events in the message query response.
	 * 
	 * @return list of messages
	 * @see Message
	 */
	public List<Message> getEvents() {
		return events;
	}

	/**
	 * Sets the list of events in the MessageQueryResponse
	 * 
	 * @param events
	 *            list of messages
	 * @see Message
	 */
	public void setEvents(List<Message> events) {
		this.events = events;
	}

	/**
	 * De-serialize and construct the Message Query Resposne object from json
	 * string
	 * 
	 * @param json
	 *            json string representation of the MessageQueryResponse
	 * @return message query response object
	 */
	public static MessageQueryResponse fromJsonString(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, MessageQueryResponse.class);
		} catch (IOException e) {
			throw new ParseException("Message query response parsing failed.", e);
		}
	}
}

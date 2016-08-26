/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
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
 * Class to represent the response of event group query
 *
 */
public class AggregateResponse {
	
	private boolean complete;
	private int duration;
	private List<AggregateResponseBin> bins;
	
	/**
	 * Indicates whether query is complete or not.
	 * 
	 * @return true or false
	 */
	public boolean isComplete() {
		return complete;
	}
	
	/**
	 * Setter for query complete indicator
	 * 
	 * @param complete true or false
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	/**
	 * Getter for query duration
	 * 
	 * @return duration of the query
	 */
	public int getDuration() {
		return duration;
	}
	
	/**
	 * Setter for query duration
	 * 
	 * @param duration duration of the query
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	/**
	 * Returns the list of aggregate bins from the response.
	 * 
	 * @return list of aggregate bins
	 * @see AggregateResponseBin
	 */
	public List<AggregateResponseBin> getBins() {
		return bins;
	}
	
	/**
	 * Setter for aggregate bins received in the event group query response.
	 * 
	 * @param bins List of aggregate response bins
	 * @see AggregateResponseBin
	 */
	public void setBins(List<AggregateResponseBin> bins) {
		this.bins = bins;
	}
	
	/**
	 * Static method to De-serialize a JSON string to AggregateResponse structure
	 * 
	 * @param json JSON string (representing the AggregateResponse)
	 * @return AggregateResponse
	 */
	public static AggregateResponse fromJsonString(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, AggregateResponse.class);
		} catch (IOException e) {
			throw new ParseException("Unable to parse the aggregation query response.", e);
		}
	}
}

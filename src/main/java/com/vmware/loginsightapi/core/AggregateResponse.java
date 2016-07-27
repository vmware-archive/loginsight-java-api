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
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.loginsightapi.ParseException;

public class AggregateResponse {
	
	private boolean complete;
	private int duration;
	private List<AggregateResponseBin> bins;
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public List<AggregateResponseBin> getBins() {
		return bins;
	}
	public void setBins(List<AggregateResponseBin> bins) {
		this.bins = bins;
	}
	
	public static AggregateResponse fromJsonString(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, AggregateResponse.class);
		} catch (IOException e) {
			throw new ParseException("Unable to parse the aggregation query response.", e);
		}
	}
	
	

}

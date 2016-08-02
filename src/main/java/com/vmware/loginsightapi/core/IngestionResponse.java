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
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.loginsightapi.ParseException;

/**
 * Class representing the response structure of an ingestion request.
 */
public class IngestionResponse {
	
	private String status;
	private String message;
	private int ingested;
	
	/**
	 * Returns the status of the ingestion request.
	 * 
	 * @return status of ingestion request
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status of the ingestion request
	 * 
	 * @param status status of the ingestion request
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Get the ingestion response message
	 * 
	 * @return message in the ingestion response
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Setter for message in Ingestion Response.
	 * 
	 * @param message message to be supplied for ingestion response
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Get the number of ingested messages
	 * 
	 * @return number of ingested messages
	 */
	public int getIngested() {
		return ingested;
	}
	
	/**
	 * Setter for number of ingested messages
	 * 
	 * @param ingested number of ingested messages
	 */
	public void setIngested(int ingested) {
		this.ingested = ingested;
	}
	
	/**
	 * Static method for constructing the IngestionResponse from JSON string
	 * 
	 * @param json json string for building ingestion response
	 * @return IngestionResponse object
	 */
	public static IngestionResponse fromJsonString(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, IngestionResponse.class);
		} catch (IOException e) {
			throw new ParseException("Unable parse the ingestion response.", e);
		}
	}
	
	

}

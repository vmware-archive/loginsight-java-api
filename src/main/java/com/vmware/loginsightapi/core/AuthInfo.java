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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.loginsightapi.ParseException;

/**
 * Class to hold the authInfo retrieved from LogInsight on successful
 * authentication
 *
 */
public class AuthInfo {

	private String userId;
	private String sessionId;
	private int ttl;

	/**
	 * Default constructor. Attributes are initialized later.
	 */
	public AuthInfo() {
	}

	/**
	 * Getter for session Id
	 * 
	 * @return user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter for user id
	 * 
	 * @param userId
	 *            user Id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Getter for session Id
	 * 
	 * @return session id
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Setter for session id
	 * 
	 * @param sessionId
	 *            session id
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Getter for TTL (Time to live) indicates session duration
	 * 
	 * @return ttl time to live
	 */
	public int getTtl() {
		return ttl;
	}

	/**
	 * Setter for TTL (Time to live) indicates session duration
	 * 
	 * @param ttl
	 *            Time to live
	 */
	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	/**
	 * Constructs a AuthInfo object from a valid json
	 * 
	 * @param json
	 *            json string representing AuthInfo
	 * @return AuthInfo object
	 */
	public static AuthInfo fromJsonString(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, AuthInfo.class);
		} catch (IOException e) {
			throw new ParseException("Unable parse the auth response.", e);
		}
	}
}

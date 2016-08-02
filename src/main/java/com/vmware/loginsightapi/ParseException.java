/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

/**
 * Generic Parse exception in LogInsight API
 *
 */
public class ParseException extends LogInsightApiException {

	private static final long serialVersionUID = 5003833322724218523L;

	/**
	 * Default Constructor
	 */
	public ParseException() {
	}

	/**
	 * Constructs a ParseException object with supplied message
	 * @param message error message
	 * @see   LogInsightApiException#LogInsightApiException(String)
	 */
	public ParseException(String message) {
		super(message);
	}

	/**
	 * Constructs a ParseException object with supplied throwable
	 * @param cause Throwable instance
	 * @see   LogInsightApiException#LogInsightApiException(Throwable)
	 */
	public ParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a ParseException object with supplied message and throwable
	 * 
	 * @param message error message
	 * @param cause   Throwable instance
	 * @see   LogInsightApiException#LogInsightApiException(String, Throwable)
	 */
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

}

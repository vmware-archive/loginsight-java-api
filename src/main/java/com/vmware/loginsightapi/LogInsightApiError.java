/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * An API error object that holds necessary information on errors.
 */
public class LogInsightApiError {

	private String message;
	private String stackTrace;
	private boolean isError;
	
	/**
	 * Constructor with error and stackTrace
	 * 
	 * @param error       error message
	 * @param stackTrace  stackTrace string
	 */
	public LogInsightApiError(String error, String stackTrace) {
		this.message = error;
		this.stackTrace = stackTrace;
		this.isError = true;
	}
	
	/**
	 * Constructor with error and Throwable
	 * 
	 * @param error error message
	 * @param th    Throwable instance
	 */
	public LogInsightApiError(String error, Throwable th) {
		this.message = error;
		this.stackTrace = ExceptionUtils.getStackTrace(th);
		this.isError = true;
	}
	
	/**
	 * Getter for error message
	 * 
	 * @return error message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Setter for error message
	 * 
	 * @param message error message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Getter for stack trace
	 * 
	 * @return stack trace string
	 */
	public String getStackTrace() {
		return stackTrace;
	}
	
	/**
	 * Setter for stack trace
	 * 
	 * @param stackTrace stackTrace string
	 */
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	/**
	 * Default constructor
	 */
	public LogInsightApiError() {
		isError = false;
	}
	
	/**
	 * Static method to create a new LogInsightApiError
	 * 
	 * @return a new LogInsightApiError instance
	 */
	public static LogInsightApiError create() {
		return new LogInsightApiError();
	}
	
	/**
	 * Checks whether the object is an error object
	 * 
	 * @return true if the object is error or else false
	 */
	public boolean isError() {
		return isError;
	}
	
	/**
	 * Setter for error in object
	 * 
	 * @param isError true or false
	 */
	public void setError(boolean isError) {
		this.isError = isError;
	}
}

/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FieldConstraint {
	
	public enum Operator {
		EQ,
		NE,
		LT,
		LE,
		GT,
		GE,
		CONTAINS,
		NOT_CONTAINS,
		HAS,
		NOT_HAS,
		MATCHES_REGEX,
		NOT_MATCHES_REGEX,
		EXISTS
	}
	private final String name;
	private final Operator operator;
	private final Object value;
	
	public FieldConstraint(String name, Operator operator, Object value){
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	
	public String toExpression() {
		StringBuilder sb = new StringBuilder().append(name).append("/").append(operator).append(" ").append(value.toString());
		return sb.toString();
	}
	public String toExpressionEncoded() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("/").append(operator.toString());
		switch(operator) {
			case MATCHES_REGEX: 
			case NOT_MATCHES_REGEX: 
			case CONTAINS: 
			case NOT_CONTAINS:
			case HAS:
			case NOT_HAS:
				sb.append(URLEncoder.encode(" ", "UTF-8"));
				sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
				break;
			case EXISTS:
				break;
			default:
				sb.append(URLEncoder.encode(" ", "UTF-8"));
				sb.append(value);
				break;
		}
		
		return sb.toString();
	}
}

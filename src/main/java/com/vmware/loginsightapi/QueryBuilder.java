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
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public abstract class QueryBuilder {

	public static final int DEFAULT_LIMIT = 100;
	public static final int DEFAULT_TIMEOUT = 30000;
	protected List<FieldConstraint> constraints;
	protected int limit;
	protected int timeout;
	protected List<String> contentPackFields;
	protected boolean includeDefaults = false;

	
	public QueryBuilder() {
		this.limit = QueryBuilder.DEFAULT_LIMIT;
		this.timeout = QueryBuilder.DEFAULT_TIMEOUT;
		contentPackFields = new ArrayList<String>();
		constraints = new ArrayList<FieldConstraint>();
	}

	public QueryBuilder limit(int limit) {
		this.limit = limit;
		return this;
	}

	public QueryBuilder timeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public QueryBuilder setContentPackFields(List<String> contentPackFields) {
		this.contentPackFields = contentPackFields;
		return this;
	}

	public QueryBuilder addContentPackField(String contentPackField) {
		this.contentPackFields.add(contentPackField);
		return this;
	}

	public QueryBuilder addContentPackFields(List<String> contentPackFields) {
		this.contentPackFields.addAll(contentPackFields);
		return this;
	}

	public QueryBuilder setConstraints(List<FieldConstraint> constraints) {
		this.constraints = constraints;
		return this;
	}

	public QueryBuilder addConstraint(String name, FieldConstraint.Operator operator, String value) {
		this.constraints.add(new FieldConstraint(name, operator, value));
		return this;
	}

	public QueryBuilder addConstraints(List<FieldConstraint> constraints) {
		this.constraints.addAll(constraints);
		return this;
	}
	
	public QueryBuilder withDefaults() {
		this.includeDefaults = true;
		return this;
	}

	protected String buildPathSegment() {
		String expression = constraints.stream().map(constraint -> {
			try {
				return constraint.toExpressionEncoded();
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeException("Unable to encode the field constraint " + constraint.toExpression());
			}
		}).collect(Collectors.joining("/"));
		return expression;

	}

	protected String buildUrlParameters() {
		List<String> urlParams = new ArrayList<String>();
		
		if (limit != QueryBuilder.DEFAULT_LIMIT || this.includeDefaults) {
			urlParams.add("limit="+limit);
		}
		if (timeout != QueryBuilder.DEFAULT_TIMEOUT || this.includeDefaults) {
			urlParams.add("timeout="+timeout);
		}
		
		// better to collect to new list and perform addAll to respect FP principles. 
		List<String> contentPackParams = contentPackFields.stream().map(contentPack -> "content-pack-fields=" + contentPack)
				.collect(Collectors.toList());
		
		urlParams.addAll(contentPackParams);
		return urlParams.parallelStream().collect(Collectors.joining("&"));
	}
	
	public abstract String toUrlString();

}

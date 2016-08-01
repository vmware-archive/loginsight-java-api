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
import java.util.stream.Collectors;

/**
 * Abstract class for QueryBuilder.
 */
public abstract class QueryBuilder {

	/**
	 * Default max number of messages in query response
	 */

	public static final int DEFAULT_LIMIT = 100;

	/**
	 * Default max timeout for the query
	 */
	public static final int DEFAULT_TIMEOUT = 30000;

	protected List<FieldConstraint> constraints;
	protected int limit;
	protected int timeout;
	protected List<String> contentPackFields;
	protected boolean includeDefaults = false;

	/**
	 * Default constructor
	 */
	public QueryBuilder() {
		this.limit = QueryBuilder.DEFAULT_LIMIT;
		this.timeout = QueryBuilder.DEFAULT_TIMEOUT;
		contentPackFields = new ArrayList<String>();
		constraints = new ArrayList<FieldConstraint>();
	}

	/**
	 * Sets the limit on number of events in query response.
	 * 
	 * @param limit
	 *            maximum number of events
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder limit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * Sets the timeout of the query
	 * 
	 * @param timeout
	 *            Query execution timeout
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder timeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * Sets the content-pack-fields to be returned in the events. This call will
	 * replace any existing content-pack-fields in the query.
	 * 
	 * @param contentPackFields
	 *            List of content pack fields
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder setContentPackFields(List<String> contentPackFields) {
		this.contentPackFields = contentPackFields;
		return this;
	}

	/**
	 * Add a content pack field to the query
	 * 
	 * @param contentPackField
	 *            field name from the already installed content packs in
	 *            loginsight
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder addContentPackField(String contentPackField) {
		this.contentPackFields.add(contentPackField);
		return this;
	}

	/**
	 * Add content pack fields (multiple) to the existing query
	 * 
	 * @param contentPackFields
	 *            list of content pack fields
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder addContentPackFields(List<String> contentPackFields) {
		this.contentPackFields.addAll(contentPackFields);
		return this;
	}

	/**
	 * Registers set of field constraints
	 * 
	 * @param constraints
	 *            List of constraints
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder setConstraints(List<FieldConstraint> constraints) {
		this.constraints = constraints;
		return this;
	}

	/**
	 * Register a single constraint to the field constraints
	 * 
	 * @param name
	 *            Name of the field
	 * @param operator
	 *            Operator
	 * @param value
	 *            Value of the field
	 * @return QueryBuilder instance (this)
	 * @see FieldConstraint.Operator
	 */
	public QueryBuilder addConstraint(String name, FieldConstraint.Operator operator, String value) {
		this.constraints.add(new FieldConstraint(name, operator, value));
		return this;
	}

	/**
	 * Add multiple constraints to the field constraints
	 * 
	 * @param constraints
	 *            List of FieldConstraint objects
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder addConstraints(List<FieldConstraint> constraints) {
		this.constraints.addAll(constraints);
		return this;
	}

	/**
	 * Enables to pass all the default values to Queries (otherwise default
	 * values are ignored in URL)
	 * 
	 * @return QueryBuilder instance (this)
	 */
	public QueryBuilder withDefaults() {
		this.includeDefaults = true;
		return this;
	}

	/**
	 * Builds the path segment based on the FieldConstraints registered to this
	 * object.
	 * 
	 * @return partial url path
	 */
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

	/**
	 * Builds a partial url string from the supplied Url parts
	 * 
	 * @return partial url string
	 */
	protected String buildUrlParameters() {
		List<String> urlParams = new ArrayList<String>();

		if (limit != QueryBuilder.DEFAULT_LIMIT || this.includeDefaults) {
			urlParams.add("limit=" + limit);
		}
		if (timeout != QueryBuilder.DEFAULT_TIMEOUT || this.includeDefaults) {
			urlParams.add("timeout=" + timeout);
		}

		// better to collect to new list and perform addAll to respect FP
		// principles.
		List<String> contentPackParams = contentPackFields.stream()
				.map(contentPack -> "content-pack-fields=" + contentPack).collect(Collectors.toList());

		urlParams.addAll(contentPackParams);
		return urlParams.parallelStream().collect(Collectors.joining("&"));
	}

	/**
	 * Builds the url segment based on the field constraints and other url
	 * parameters supplied.
	 * 
	 * @return partial url string
	 */
	public abstract String toUrlString();

}

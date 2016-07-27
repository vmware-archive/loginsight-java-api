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

public class ConstraintBuilder {
	
	List<FieldConstraint> constraints;
	public ConstraintBuilder() {
		constraints = new ArrayList<FieldConstraint>();
	}
	
	public ConstraintBuilder eq(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.EQ, value));
		return this;
	}
	
	public ConstraintBuilder ne(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.NE, value));
		return this;
	}
	public ConstraintBuilder lt(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.LT, value));
		return this;
	}
	public ConstraintBuilder le(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.LE, value));
		return this;
	}
	public ConstraintBuilder gt(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.GT, value));
		return this;
	}
	public ConstraintBuilder ge(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.GE, value));
		return this;
	}
	
	public ConstraintBuilder contains(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.CONTAINS, value));
		return this;
	}
	public ConstraintBuilder notContains(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.NOT_CONTAINS, value));
		return this;
	}
	public ConstraintBuilder has(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.HAS, value));
		return this;
	}
	
	public ConstraintBuilder notHas(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.NOT_HAS, value));
		return this;
	}
	public ConstraintBuilder matches(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.MATCHES_REGEX, value));
		return this;
	}
	public ConstraintBuilder notMatches(String field, String value) {
		this.constraints.add(new FieldConstraint(field, FieldConstraint.Operator.NOT_MATCHES_REGEX, value));
		return this;
	}
	
	public List<FieldConstraint> build() {
		return constraints;
	}
	
	public String buildPathSegment() {
		String expression = constraints.stream().map(constraint -> {
			try {
				return constraint.toExpressionEncoded();
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeException("Unable to encode the field constraint " + constraint.toExpression());
			}
		}).collect(Collectors.joining("/"));
		return expression;
	}
	

}

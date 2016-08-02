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

import com.vmware.loginsightapi.core.FieldConstraint;

/**
 * This class helps in building the field constraints required loginsight
 * queries
 * 
 * @see FieldConstraint
 */

public class ConstraintBuilder {

	List<FieldConstraint> constraints;

	/**
	 * Default Constructor to initialize the constraints.
	 */
	public ConstraintBuilder() {
		constraints = new ArrayList<FieldConstraint>();
	}

	/**
	 * Build a field constraint with <b>equals</b> operator <br>
	 * This constraint is valid only for numeric values <br>
	 * <br>
	 * {@code field EQ value} or {@code field == value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports numeric only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder eq(String field, String value) {
		this.constraints.add(FieldConstraint.eq(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>not equals</b> operator <br>
	 * This constraint is valid only for numeric values <br>
	 * <br>
	 * {@code field NE value} or {@code field != value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports numeric only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder ne(String field, String value) {
		this.constraints.add(FieldConstraint.ne(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>less than</b> operator <br>
	 * This constraint is valid only for numeric values <br>
	 * <br>
	 * {@code field LT value} or {@code field < value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports numeric only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder lt(String field, String value) {
		this.constraints.add(FieldConstraint.lt(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>less than or equals to</b> operator <br>
	 * This constraint is valid only for numeric values <br>
	 * <br>
	 * {@code field LE value} or {@code field <= value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports numeric only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder le(String field, String value) {
		this.constraints.add(FieldConstraint.le(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>greater than</b> operator <br>
	 * This constraint is valid only for numeric values <br>
	 * <br>
	 * {@code field GT value} or {@code field > value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports numeric only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder gt(String field, String value) {
		this.constraints.add(FieldConstraint.gt(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>greater than or equals to</b> operator <br>
	 * This constraint is valid only for numeric values <br>
	 * <br>
	 * {@code field GE value} or {@code field >= value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports numeric only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder ge(String field, String value) {
		this.constraints.add(FieldConstraint.ge(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>contains</b> operator <br>
	 * This constraint is valid only for string values <br>
	 * <br>
	 * {@code field CONTAINS value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports string type only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder contains(String field, String value) {
		this.constraints.add(FieldConstraint.contains(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>not contains</b> operator <br>
	 * This constraint is valid only for string values <br>
	 * <br>
	 * {@code field NOT_CONTAINS value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports string type only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder notContains(String field, String value) {
		this.constraints.add(FieldConstraint.notContains(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>has</b> operator <br>
	 * This constraint is valid only for string values <br>
	 * <br>
	 * {@code field HAS value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports string type only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder has(String field, String value) {
		this.constraints.add(FieldConstraint.has(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>not has</b> operator <br>
	 * This constraint is valid only for string values <br>
	 * <br>
	 * {@code field NOT_HAS value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports string type only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder notHas(String field, String value) {
		this.constraints.add(FieldConstraint.notHas(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>matches regex</b> operator <br>
	 * This constraint is valid only for string values <br>
	 * <br>
	 * {@code field MATCHES_REGEX value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports string type only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder matchesRegex(String field, String value) {
		this.constraints.add(FieldConstraint.matchesRegex(field, value));
		return this;
	}

	/**
	 * Build a field constraint with <b>not matches regex</b> operator <br>
	 * This constraint is valid only for string values <br>
	 * <br>
	 * {@code field NOT_MATCHES_REGEX value} </b>
	 * 
	 * @param field
	 *            name of the field
	 * @param value
	 *            field's value - supports string type only
	 * @return ConstraintBuilder instance (this)
	 */
	public ConstraintBuilder notMatchesRegex(String field, String value) {
		this.constraints.add(FieldConstraint.notMatchesRegex(field, value));
		return this;
	}

	/**
	 * Returns the list of constructs as an arrayList.
	 * 
	 * @return the list of field constraints
	 */
	public List<FieldConstraint> build() {
		return constraints;
	}

	/**
	 * Returns the segment of the url path based on the provided constraints.
	 * 
	 * @return segment of the url path (string)
	 */
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

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

/**
 * POJO for modeling a Field Constraint.
 *
 */
public class FieldConstraint {

	/**
	 * Operator enum
	 */
	public enum Operator {

		/**
		 * Operator symbol to check if a numeric field is equal to supplied
		 * value.
		 */
		EQ,

		/**
		 * Operator symbol to check if a numeric field is not equal to supplied
		 * value.
		 */
		NE,

		/**
		 * Operator symbol to check if a numeric field is less than supplied
		 * value.
		 */
		LT,

		/**
		 * Operator symbol to check if a numeric field is less than or equals to
		 * supplied value.
		 */
		LE,

		/**
		 * Operator symbol to check if a numeric field is greater than supplied
		 * value.
		 */
		GT,

		/**
		 * Operator symbol to check if a numeric field is greater than or equals
		 * to supplied value.
		 */
		GE,

		/**
		 * Operator symbol to check if the field contains supplied value.
		 */
		CONTAINS,

		/**
		 * Operator symbol to check if the field do not contain supplied value.
		 */
		NOT_CONTAINS,

		/**
		 * Operator symbol to check if the field has supplied value.
		 */
		HAS,

		/**
		 * Operator symbol to check if the field do not have supplied value.
		 */
		NOT_HAS,

		/**
		 * Operator symbol to check if a text field matches with supplied regex
		 * {@code (=~) }
		 */
		MATCHES_REGEX,

		/**
		 * Operator symbol to check if a text field do not match with supplied
		 * regex {@code (!=~) }
		 */
		NOT_MATCHES_REGEX,

		/**
		 * Operator symbol to check if the field exists
		 */
		EXISTS
	}

	private final String name;
	private final Operator operator;
	private final Object value;

	/**
	 * Constructor with name, operator and value <br>
	 * This constructor is for all operators except EXISTS
	 * 
	 * @param name
	 *            name of the field
	 * @param operator
	 *            operator (except EXISTS operator)
	 * @param value
	 *            value
	 */
	public FieldConstraint(String name, Operator operator, Object value) {
		this.name = name;
		this.operator = operator;
		this.value = value;
	}

	/**
	 * Constructor with name, operator <br>
	 * This constructor is only for EXISTS operator at present.
	 * 
	 * @param name
	 *            name of the field
	 * @param operator
	 *            operator (EXISTS as of now)
	 */
	public FieldConstraint(String name, Operator operator) {
		this.name = name;
		this.operator = operator;
		this.value = null;
	}

	/**
	 * Returns a field constraint with Equals (==) operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be numeric)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint eq(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.EQ, value);
	}

	/**
	 * Returns a field constraint with Not Equals (!=) operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be numeric)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint ne(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.NE, value);
	}

	/**
	 * Returns a field constraint with less than {@code < } operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be numeric)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint lt(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.LT, value);
	}

	/**
	 * Returns a field constraint with less than {@code <= } operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be numeric)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint le(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.LE, value);
	}

	/**
	 * Returns a field constraint with less than {@code > } operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be numeric)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint gt(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.GT, value);
	}

	/**
	 * Returns a field constraint with less than {@code >= } operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be numeric)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint ge(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.GE, value);
	}

	/**
	 * Returns a field constraint with CONTAINS operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be String)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint contains(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.CONTAINS, value);
	}

	/**
	 * Returns a field constraint with NOT_CONTAINS operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be String)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint notContains(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.NOT_CONTAINS, value);
	}

	/**
	 * Returns a field constraint with HAS operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be String)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint has(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.HAS, value);
	}

	/**
	 * Returns a field constraint with NOT_HAS operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be String)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint notHas(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.NOT_HAS, value);
	}

	/**
	 * Returns a field constraint with MATCHES_REGEX operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be String)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint matchesRegex(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.MATCHES_REGEX, value);
	}

	/**
	 * Returns a field constraint with NOT_MATCHES_REGEX operator
	 * 
	 * @param name
	 *            Name of the field
	 * @param value
	 *            Value of the field (should be String)
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint notMatchesRegex(String name, String value) {
		return new FieldConstraint(name, FieldConstraint.Operator.NOT_MATCHES_REGEX, value);
	}

	/**
	 * Returns a field constraint with EXISTS operator
	 * 
	 * @param name
	 *            Name of the field
	 * @return FieldConstraint instance
	 */
	public static FieldConstraint exists(String name) {
		return new FieldConstraint(name, FieldConstraint.Operator.EXISTS);
	}

	/**
	 * This methods builds and returns an non encoded url segment for the field
	 * constraint. <br>
	 * <b> Do not use this directly with urls </b>
	 * 
	 * @return an un-encoded url segment for the field constraint.
	 */
	public String toExpression() {
		StringBuilder sb = new StringBuilder().append(name).append("/").append(operator).append(" ")
				.append(value.toString());
		return sb.toString();
	}

	/**
	 * Builds an URL expression for the field constraint. <br>
	 * This method currently uses textual symbols of operators only. <br>
	 * The field values are encoded in case of textual values.
	 * 
	 * @return A sting containing partial url path segment
	 * @throws UnsupportedEncodingException
	 *             unable to encode the url string
	 */
	public String toExpressionEncoded() throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("/").append(operator.toString());
		switch (operator) {
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

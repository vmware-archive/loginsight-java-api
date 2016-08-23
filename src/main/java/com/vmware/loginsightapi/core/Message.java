/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Message class holding the each loginsight message/event
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Message {
	private String text;
	private Long timestamp;
	private List<Field> fields;
	/**
	 * Default constructor
	 */
	public Message() {
		this.fields = new ArrayList<Field>();
	}

	/**
	 * Creates a message from text. Initialized fields to empty list.
	 * 
	 * @param text
	 *            message text
	 */
	public Message(String text) {
		this.text = text;
		this.fields = new ArrayList<Field>();
	}

	/**
	 * Creates a message from text and timestamp fields. Initializes fields to
	 * empty list.
	 * 
	 * @param text
	 *            message text
	 * @param timestamp
	 *            message timestamp
	 */
	public Message(String text, Long timestamp) {
		this.text = text;
		this.timestamp = timestamp;
		this.fields = new ArrayList<Field>();
	}

	/**
	 * Creates a message from text, timestamp and supplied fields.
	 * 
	 * @param text
	 *            message text
	 * @param timestamp
	 *            message timestamp
	 * @param fields
	 *            List of fields for message
	 */
	public Message(String text, Long timestamp, List<Field> fields) {
		this.text = text;
		this.timestamp = timestamp;
		this.fields = fields;
	}

	/**
	 * Getter for message text
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter for message text
	 * 
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Getter for message timestamp
	 * 
	 * @return the timeStamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * Setter for message timestamp
	 * 
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimestamp(Long timeStamp) {
		this.timestamp = timeStamp;
	}

	/**
	 * Automatically set the current time in mills to Message
	 */
	public void setTimestamp() {
		this.timestamp = DateTime.now().getMillis();
	}

	/**
	 * Getter for fields in message
	 * 
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * Setter for fields in message
	 * 
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * Add a field by name and content to already registered fields. Please note
	 * that this does not check for the duplicate field names.
	 * 
	 * @param name
	 *            name of the field
	 * @param content
	 *            content of the field
	 */
	public void addField(String name, String content) {
		this.fields.add(new Field(name, content));
	}

	/**
	 * Add a field by name, startPosition and length to already registered
	 * fields. Please note that this does not check for the duplicate field
	 * names.
	 * 
	 * @param name
	 *            name of the new field
	 * @param startPosition
	 *            startPosition of the new field
	 * @param length
	 *            length of the new field.
	 */
	public void addField(String name, int startPosition, int length) {
		this.fields.add(new Field(name, startPosition, length));
	}

	/**
	 * Checks whether Message object is empty <br>
	 * Returns true if <br>
	 * {@code
	 * ((null == this.text || StringUtils.isEmpty(this.text)) && this.fields.size() == 0)
	 * }
	 * 
	 * @return true of false
	 */
	public boolean checkIsEmpty() {
		if ((null == this.text || StringUtils.isEmpty(this.text)) && this.fields.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks whether message is a valid loginsight ingestion message object.
	 * 
	 * @return true or false
	 */
	public boolean checkIsValid() {
		boolean valid = true;
		if (this.checkIsEmpty()) {
			return true;
		}
		if (null != this.text && !StringUtils.isEmpty(this.text)) {
			for (Field field : fields) {
				if (field.getStartPosition() + field.getLength() > this.text.length()) {
					return false;
				}
			}
		}
		return valid;
	}
}

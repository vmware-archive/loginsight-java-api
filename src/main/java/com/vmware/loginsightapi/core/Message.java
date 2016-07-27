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

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Message {
	private String text;
	private Long timestamp;
	private List<Field> fields;
	
	public Message () {
//		timestamp = DateTime.now().getMillis();
		this.fields = new ArrayList<Field>();
	}
	
	public Message (String text) {
		this.text = text;
		this.fields = new ArrayList<Field>();
	}
	
	public Message (String text, Long timestamp) {
		this.text = text;
		this.timestamp = timestamp;
		this.fields = new ArrayList<Field>();
	}
	
	public Message (String text, Long timestamp, List<Field> fields) {
		this.text = text;
		this.timestamp = timestamp;
		this.fields = fields;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the timeStamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimestamp(Long timeStamp) {
		this.timestamp = timeStamp;
	}
	/**
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public void addField(Field field) {
		this.fields.add(field);
	}
	
	public void addField(String name, String content) {
		this.fields.add(new Field(name, content));
	}
	public void addField(String name, String startPosition, String length) {
		this.fields.add(new Field(name, startPosition, length));
	}
	public void addField(String name, String content, String startPosition, String length) {
		this.fields.add(new Field(name, content, startPosition, length));
	}
}

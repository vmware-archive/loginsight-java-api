/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the “License”); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import com.vmware.loginsightapi.core.Message;

/**
 * Builder class for Message
 */
public class MessageBuilder {

	private Message message;

	/**
	 * Default constructor
	 */
	public MessageBuilder() {
		message = new Message();
	}

	/**
	 * Initializes MessageBuilder with text
	 * 
	 * @param text
	 *            message text
	 */
	public MessageBuilder(String text) {
		message = new Message(text);
	}

	/**
	 * Add a field with content
	 * 
	 * @param name
	 *            name of the field
	 * @param content
	 *            content of the field
	 * @return MessageBuilder instance (this)
	 */
	public MessageBuilder withField(String name, String content) {
		message.addField(name, content);
		return this;
	}

	/**
	 * Add a field with startPosition and length
	 * 
	 * @param name
	 *            name of the field
	 * @param startPosition
	 *            startPosition for the field in text
	 * @param length
	 *            length of the field value in the text
	 * @return MessageBuilder instance (this)
	 */
	public MessageBuilder withField(String name, String startPosition, String length) {
		message.addField(name, startPosition, length);
		return this;
	}
	
	/**
	 * Set the provided timestamp to the Message
	 * 
	 * @param timeStamp timeStamp of the message
	 * @return MessageBuilder instance (object)
	 */
	public MessageBuilder withTimestamp(long timeStamp) {
		message.setTimestamp(timeStamp);
		return this;
	}
	
	/**
	 * Automatically set the current timestamp to Message
	 * 
	 * @return MessageBuilder instance (this)
	 */
	public MessageBuilder withCurrentTimestamp() {
		message.setTimestamp();
		return this;
	}

	/**
	 * Build the message object
	 * 
	 * @return Message object
	 */
	public Message build() {
		return this.message;
	}

}

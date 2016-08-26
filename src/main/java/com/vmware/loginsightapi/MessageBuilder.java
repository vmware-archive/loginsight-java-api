/**
 * Copyright © 2016 VMware, Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy of 
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 * Some files may be comprised of various open source software components, each of which
 * has its own license that is located in the source code of the respective component.
 */
package com.vmware.loginsightapi;

import com.vmware.loginsightapi.core.LogInsightApiException;
import com.vmware.loginsightapi.core.Message;

/**
 * Builder class for Message
 * <br><br><b> Sample Usages: </b> <br>
 * Sample message with message text.<br>
 * {@code
 * new MessageBuilder("System failed")
 * } <br> <br>
 * 
 * Sample message with message text with field and its value.<br>
 * {@code
 * new MessageBuilder("System failed").field("field_1", "value_1")
 * } <br><br>
 *
 * Sample message with message text with field and its startPosition and length.<br>
 * {@code
 * new MessageBuilder("System failed").field("field_1", 7, 6)
 * } <br><br>
 * 
 * Sample message with message text with field and timestamp.<br>
 * {@code
 * new MessageBuilder("System failed").timestamp(1471788597753)
 * } <br><br>
 * 
 * Sample message with message text with field and current time as message timestamp.<br>
 * {@code
 * new MessageBuilder("System failed").currentTimestamp()
 * } <br><br>
 * 
 * Builds message with empty message text. <br>
 * {@code
 * new MessageBuilder()
 * } <br><br>
 * 
 * 
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
	public MessageBuilder field(String name, String content) {
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
	public MessageBuilder field(String name, int startPosition, int length) {
		message.addField(name, startPosition, length);
		return this;
	}
	
	/**
	 * Set the provided timestamp to the Message
	 * 
	 * @param timeStamp timeStamp of the message
	 * @return MessageBuilder instance (object)
	 */
	public MessageBuilder timestamp(long timeStamp) {
		message.setTimestamp(timeStamp);
		return this;
	}
	
	/**
	 * Automatically set the current timestamp to Message
	 * 
	 * @return MessageBuilder instance (this)
	 */
	public MessageBuilder currentTimestamp() {
		message.setTimestamp();
		return this;
	}

	/**
	 * Build the message object
	 * 
	 * @return Message object
	 */
	public Message build() {
		if (this.message.checkIsValid()) {
			return this.message;
		} else {
			throw new LogInsightApiException("Invalid message structure.");
		}
	}

}

/**
 * 
 */
package com.vmware.loginsightapi;

import java.util.List;

import com.vmware.loginsightapi.core.IngestionRequest;
import com.vmware.loginsightapi.core.Message;

/**
 * Builder class for building ingestion requests
 * 
 * <b> Some Usages: </b> <br>
 * {@code
 * new IngestionRequetBuilder().withMessage(new Message("System failed"))
 * } <br>
 * {@code
 * new IngestionRequetBuilder().withMessage(new MessageBuilder("System failed"))
 * } <br>
 * {@code
 * new IngestionRequetBuilder().withMessage(new MessageBuilder("System failed").withField("field1", "value1"))
 * }
 */
public class IngestionRequestBuilder {

	private IngestionRequest ingestionRequest;

	/**
	 * Default constructor
	 */
	public IngestionRequestBuilder() {
		this.ingestionRequest = new IngestionRequest();
	}

	/**
	 * Builds IngestionRequest with a message
	 * 
	 * @param message
	 *            Message object
	 * @return IngestionRequestBuilder instance (this)
	 */
	public IngestionRequestBuilder withMessage(Message message) {
		this.ingestionRequest.addMessage(message);
		return this;
	}

	/**
	 * Builds IngestionRequest with multiple messages
	 * 
	 * @param messages
	 *            List of message objects
	 * @return IngestionRequestBuilder instance (this)
	 */
	public IngestionRequestBuilder withMessages(List<Message> messages) {
		this.ingestionRequest.addMessages(messages);
		return this;
	}

}

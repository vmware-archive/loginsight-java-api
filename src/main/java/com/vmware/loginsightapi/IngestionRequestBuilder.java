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
 * <br><br><b> Sample Usages: </b> <br><br>
 * 
 * Build an ingestion request with message <br>
 * {@code
 * new IngestionRequetBuilder().message(new Message("System failed"))
 * } <br> <br>
 * 
 * Build an ingestion request with MessageBuilder <br>
 * {@code
 * new IngestionRequetBuilder().message(new MessageBuilder("System failed"))
 * } <br>
 * 
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
	public IngestionRequestBuilder message(Message message) {
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
	public IngestionRequestBuilder messages(List<Message> messages) {
		this.ingestionRequest.addMessages(messages);
		return this;
	}
	
	/**
	 * builds the IngestionRequest
	 * 
	 * @return IngestionRequest instance
	 */
	public IngestionRequest build()  {
		return this.ingestionRequest;
	}
}

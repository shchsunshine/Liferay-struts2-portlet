package com.winenergy.helloworld.model;

/**
 * Model class that stores a message.
 * @author Fuping Ma
 *
 */
public class MessageStore {
	
	private String message;
	
	public MessageStore() {
		
		setMessage("Hello WINenergy User");
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
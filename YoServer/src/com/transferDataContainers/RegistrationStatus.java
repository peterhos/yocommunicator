package com.transferDataContainers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RegistrationStatus implements Serializable{
	private boolean confirmation = false;
	private String message;
	
	public void send(ObjectOutputStream out) throws IOException {
		out.writeObject(this);
	}
	
	public boolean isConfirmation() {
		return confirmation;
	}
	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

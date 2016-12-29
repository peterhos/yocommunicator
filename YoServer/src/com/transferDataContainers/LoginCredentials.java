package com.transferDataContainers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LoginCredentials implements Serializable{
	private String login;
	private String password;
	private boolean confirmed = false;
	
	public LoginCredentials(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public void sendCredentials(ObjectOutputStream out) throws IOException {
		out.writeObject(this);
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}

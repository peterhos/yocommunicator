package com.data;

import java.io.ObjectOutputStream;

public class LoggedInUser {
	private String nick;
	private ObjectOutputStream out;
	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public LoggedInUser(String nick, ObjectOutputStream out) {
		this.nick = nick;
		this.out = out;
	}
}

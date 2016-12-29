package com.server.main;

import java.io.IOException;

public class MainServer {

	public static void main(String[] args) {
		Server server1 = new Server(1056);
		try {
			server1.runServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

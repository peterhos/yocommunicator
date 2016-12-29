package com.server.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private int serverPortNumber;
	
	public Server (int serverPortNumber) {
		this.serverPortNumber = serverPortNumber;
	}
	
	public void runServer() throws IOException {
        
        try (ServerSocket serverSocket = new ServerSocket(this.getServerPortNumber());)
        {
        	System.out.println("Server is up & ready for connections");
              
        	while (true) {								
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection established. Port: " + clientSocket.getPort());
                new ServerThread(clientSocket).start(); 	
            }
        } catch (IOException e) {
        	System.out.println("Exception caught when trying to listen on port "
        			+ this.getServerPortNumber() + " or listening for a connection");
        	System.out.println(e.getMessage());
        } 
		
    }
	
	
	public int getServerPortNumber() {
		return serverPortNumber;
	}
	
	public void setServerPortNumber(int serverPortNumber) {
		this.serverPortNumber = serverPortNumber;
	}
}

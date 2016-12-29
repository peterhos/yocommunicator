package com.server.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.data.LoggedInUser;
import com.database.DatabaseConnector;
import com.transferDataContainers.LoginCredentials;
import com.transferDataContainers.Message;
import com.transferDataContainers.RegistrationInformation;
import com.transferDataContainers.RegistrationStatus;

public class ServerThread extends Thread {
	public static ArrayList<LoggedInUser> onlineUsers = new ArrayList<LoggedInUser>();
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private DatabaseConnector dbConnector;
	
	public ServerThread(Socket socket) throws IOException{
		this.socket = socket;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
		try {
			dbConnector = new DatabaseConnector("mysql", "127.0.0.1", "3306", "YoDB", "ServerSquad", "Server1.Conn");
		} catch (SQLException e) {
			System.err.println("Cannot connect to database.");
		}
	}
	
	public void run() {
		try {
			while(true) {
				this.readInputData(in, out);
			}
		} catch (ClassNotFoundException e) {
			System.err.println("Nie znalazlo klasy do castowania");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readInputData(ObjectInputStream in, ObjectOutputStream out) throws ClassNotFoundException, IOException {
		Object input = in.readObject();
		if (input instanceof Message) {
			this.redirectMessage((Message)input);
		} else if (input instanceof LoginCredentials) {	
			this.checkCredentials((LoginCredentials)input);
		} else if (input instanceof RegistrationInformation) {
			this.registerNewUser((RegistrationInformation)input);
		} /*else if (input instanceof DataRequest) {			// Jesli przyszła prośba o dane danego użytkownika
			System.out.println("RegistrationInformation");
		}*/
	}
	
	private void registerNewUser(RegistrationInformation input) {
		RegistrationStatus reply = new RegistrationStatus();
		
		if((dbConnector.userExists(input.getNick()))){		
			dbConnector.addNewUser(input);
			reply.setMessage("User added successfully");
			reply.setConfirmation(true);
		} else {
			reply.setMessage("User with this nick already exist in database");
		}
		try {
			reply.send(out);
		} catch (IOException e) {
			System.err.println("Couldn't send a reply to client");
		}
	}
	
	public static boolean isOnline(String user) {
		for(LoggedInUser u : onlineUsers) {
			if(u.getNick().equals(user))
				return true;
		}
		return false;
	}
	
	public void saveMessageInDatabase(Message message) {
		
	}
	
	public static LoggedInUser findUser(String user) {
		LoggedInUser ret = null;
		for(LoggedInUser u : onlineUsers){
			if(u.getNick().equals(user))
				ret = u;
		}
		return ret;
	}
	
	public void sendMessage(Message message) throws IOException{
		LoggedInUser usr = findUser(message.getReceiver());
		
		usr.getOut().writeObject(message);
		System.out.println("Sended");
	}
	
	public void redirectMessage(Message message){
		try {
			if(isOnline(message.getReceiver())){
				this.sendMessage(message);
			}
		} catch (IOException e) {
			System.err.println("Problems with sending message. Failed!");
		}
	}
	
	public void checkCredentials(LoginCredentials credentials) throws IOException {
		LoginCredentials user = credentials;
		String password = null;
		if(dbConnector.userExists(user.getLogin())){
			password = dbConnector.getUserPassword(user.getLogin());
			if(user.getPassword().equals(password)){
				user.setConfirmed(true);
				addOnlineUser(user.getLogin(), out);
			}
			out.writeObject(user);
		}else {
			out.writeObject(null);
		}
	}

	public static void addOnlineUser(String nick, ObjectOutputStream out) {
		onlineUsers.add(new LoggedInUser(nick, out));
	}
	
	public static void removeOnlineUser(String nick){
		
	}
}

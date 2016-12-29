package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.transferDataContainers.RegistrationInformation;

public class DatabaseConnector {
	private Connection databaseConnection = null;
	private PreparedStatement prepStatement = null;
	
	public DatabaseConnector(String DBMS, String ip, String port, String schema, String user, String password) throws SQLException{
		String url = "jdbc:" + DBMS + "://" + ip + ":" + port + "/" + schema +"?useSSL=true";
		databaseConnection = DriverManager.getConnection(url, user, password);
	}
	
	public void close() throws SQLException {
		databaseConnection.close();
	}
	
	public boolean userExists(String user) {
		ResultSet myRs = null;
		
		try {
			prepStatement = databaseConnection.prepareStatement("SELECT CASE "
										+ "WHEN EXISTS (SELECT * FROM YoDB.User WHERE Nick = ?) "
										+ "THEN 'true' "
										+ "ELSE 'false' "
										+ "END AS result "  
										+ "FROM YoDB.User");
			
			prepStatement.setString(1, user);
			myRs = prepStatement.executeQuery();
			if(myRs.next())
					return true;
			
		} catch (SQLException e) {
			System.err.println("Something with execution SQL statement went wrong. Can't check if user exist.");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String getUserPassword(String nick) {
		ResultSet myRs = null;
		
		String pass = null;
		try {
			prepStatement = databaseConnection.prepareStatement("select Password "
											+ "from YoDB.User "
											+ "where Nick = ?");
			prepStatement.setString(1, nick);
			myRs = prepStatement.executeQuery();
			try {
				if(myRs.next())
					pass = myRs.getString("Password");
				
			} catch (SQLException e) {
				System.out.println("Kurwa");
			}
			
			
		} catch(SQLException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pass;
	}
	
	public void addNewUser(RegistrationInformation newUser) {
		ResultSet resultSet = null;
		
		try {
			prepStatement = databaseConnection.prepareStatement("INSERT INTO `YoDB`.`User` (`Nick`, `Password`, `FirstName`, `LastName`, `Email`, `Age`, `Country`, `City`, `Gender_GenderId`) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
			
			prepStatement.setString(1, newUser.getNick());
			prepStatement.setString(2, newUser.getPassword());
			prepStatement.setString(3, newUser.getFirstName());
			prepStatement.setString(4, newUser.getLastName());
			prepStatement.setString(5, newUser.geteMail());
			prepStatement.setInt(6, newUser.getAge());
			prepStatement.setString(7, newUser.getCountry());
			if (newUser.getGender().equalsIgnoreCase("Male"))
				prepStatement.setInt(9, 1);
			else if (newUser.getGender().equalsIgnoreCase("Female"))
				prepStatement.setInt(9, 2);
			else
				prepStatement.setInt(9, 3);
			
			int rowsAffected = prepStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

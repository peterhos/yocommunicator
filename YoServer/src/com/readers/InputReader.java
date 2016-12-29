package com.readers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.transferDataContainers.LoginCredentials;
import com.transferDataContainers.Message;
import com.transferDataContainers.RegistrationInformation;

public class InputReader {
	
	public void readInputData(ObjectInputStream in, ObjectOutputStream out) throws ClassNotFoundException, IOException {
		Object input = in.readObject();
		if (input instanceof Message) {
			this.redirectMessage((Message)input);
		} else if (input instanceof LoginCredentials) {	
			LoginCredentials credentials = (LoginCredentials)input;
			credencials.check();
			this.checkCredentials((LoginCredentials)input);
		} else if (input instanceof RegistrationInformation) {
			this.registerNewUser((RegistrationInformation)input);
		} /*else if (input instanceof DataRequest) {			// Jesli przyszła prośba o dane danego użytkownika
			System.out.println("RegistrationInformation");
		}*/
	}
}

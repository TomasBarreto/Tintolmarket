package src.domain;

import java.io.ObjectOutputStream;
import java.util.HashMap;

public class UserCatalog {
	private HashMap<String, User> users;
	
	public UserCatalog() {
		this.users = new HashMap<>();
	}
	
	public void addUser(String userID) {
		users.put(userID, new User(userID));
	}

	public void sendMessage(String user, String message, ObjectOutputStream outStream) {
		User target = users.get(user);
		target.receiveMessage(new Message(user, message));
		
		outStream.writeObject("Message Successfully sent!");
	}
}

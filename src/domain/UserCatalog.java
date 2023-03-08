package src.domain;

import java.io.IOException;
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

	public void sendMessage(String user, String message) throws IOException {
		User target = users.get(user);
		target.receiveMessage(new Message(user, message));
		
		try {
			outStream.writeObject("Message Successfully sent!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getWalletMoney(String userID) {
		User target = this.users.get(userID);
		
		return String.valueOf(target.getWalletMoney());
	}
}

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

	public boolean sendMessage(String user, String message) throws IOException {
		User target = users.get(user);

		if(target == null) {
			return false;
		}

		target.receiveMessage(new Message(user, message));
		return true;
	}

	public int getWalletMoney(String userID) {
		User target = this.users.get(userID);
		
		return target.getWalletMoney();
	}
}

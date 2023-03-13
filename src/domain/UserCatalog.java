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
		if(!users.containsKey(userID)) {
			users.put(userID, new User(userID));
		}
	}

	public boolean sendMessage(String user, String userFrom, String message)  {
		User target;
		if (users.containsKey(user)) {
			target = users.get(user);
		} else {
			return false;
		}

		target.receiveMessage(new Message(user, userFrom, message));
		return true;
	}

	public int getWalletMoney(String userID) {
		User target = this.users.get(userID);
		
		return target.getWalletMoney();
	}

	public String readMessages(String userID){
		User user = users.get(userID);
		return user.readMessages();
	}

	public void reduceBalance(String userID, int winePrice) {
		this.users.get(userID).reduceBalance(winePrice);
	}
}

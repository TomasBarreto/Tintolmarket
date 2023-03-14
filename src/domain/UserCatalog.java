package src.domain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class UserCatalog {
	private HashMap<String, User> users;
	private UserMessagesFileHandler msgFH;
	
	public UserCatalog() {
		this.users = new HashMap<>();
		this.msgFH = new UserMessagesFileHandler();
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

		Message newMessage = new Message(user, userFrom, message);
		target.receiveMessage(newMessage);

		Command cmd = new Command();
		cmd.setCommand("addMsg");
		cmd.setUser(userFrom);
		cmd.setUserReceiver(user);
		cmd.setMessage(message);
		this.msgFH.alterFile(cmd);
		return true;
	}

	public int getWalletMoney(String userID) {
		User target = this.users.get(userID);
		
		return target.getWalletMoney();
	}

	public String readMessages(String userID){
		User user = users.get(userID);
		Command cmd = new Command();
		cmd.setCommand("removeMsg");
		cmd.setUserReceiver(userID);
		this.msgFH.alterFile(cmd);
		return user.readMessages();
	}

	public void reduceBalance(String userID, int winePrice) {
		this.users.get(userID).reduceBalance(winePrice);
	}

	public void loadMessage(String userFrom, String userReceiver, String message) {
		User target = this.users.get(userReceiver);
		Message newMessage = new Message(userReceiver, userFrom, message);
		target.loadMessage(newMessage);
	}
}

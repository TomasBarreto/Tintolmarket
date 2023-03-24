package src.domain;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
/**
 * Represents a catalog of Users in the system.
 */
public class UserCatalog {
	private HashMap<String, User> users;
	private UserMessagesFileHandler msgFH;

	/**
	 * Constructs a UserCatalog object with an empty HashMap of Users and a new UserMessagesFileHandler.
	 */
	public UserCatalog() {
		this.users = new HashMap<>();
		this.msgFH = new UserMessagesFileHandler();
	}

	/**
	 * Adds a new User with the specified identifier and wallet balance to the catalog.
	 * @param userID the identifier of the new User
	 * @param wallet the initial balance of the new User's wallet
	 */
	public void addUser(String userID, String wallet) {
		if(!users.containsKey(userID)) {
			users.put(userID, new User(userID, Integer.parseInt(wallet)));
		}
	}

	/**
	 * Adds a new User with the specified identifier and a default wallet balance to the catalog.
	 * @param userID the identifier of the new User
	 */
	public void addUser(String userID) {
		if(!users.containsKey(userID)) {
			users.put(userID, new User(userID));
		}
	}

	/**
	 * Sends a Message from one User to another and stores it in the recipient's Mailbox.
	 * @param user the identifier of the User to receive the Message
	 * @param userFrom the identifier of the User sending the Message
	 * @param message the contents of the Message
	 * @return true if the Message was successfully sent and stored, false otherwise
	 */
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

	/**
	 * Returns the current balance of the specified User's wallet.
	 * @param userID the identifier of the User to check
	 * @return the current balance of the User's wallet
	 */
	public int getWalletMoney(String userID) {
		User target = this.users.get(userID);
		
		return target.getWalletMoney();
	}

	/**
	 * Returns a String with all Messages in the specified User's Mailbox and removes them.
	 * @param userID the identifier of the User whose Mailbox should be read
	 * @return a String with all Messages in the User's Mailbox
	 */
	public String readMessages(String userID){
		User user = users.get(userID);
		Command cmd = new Command();
		cmd.setCommand("removeMsg");
		cmd.setUserReceiver(userID);
		this.msgFH.alterFile(cmd);
		return user.readMessages();
	}

	/**
	 * Reduces the balance of the specified User's wallet by the specified amount.
	 * @param userID the identifier of the User to deduct from
	 * @param winePrice the amount to deduct from the User's wallet
	 * @return the new balance of the User's wallet
	 */
	public int reduceBalance(String userID, int winePrice) {
		return this.users.get(userID).reduceBalance(winePrice);
	}

	/**
	 * Loads a new message for the given User receiver.
	 * @param userFrom the User who sent the message
	 * @param userReceiver the User who received the message
	 * @param message the message content
	 */
	public void loadMessage(String userFrom, String userReceiver, String message) {
		User target = this.users.get(userReceiver);
		Message newMessage = new Message(userReceiver, userFrom, message);
		target.loadMessage(newMessage);
	}

	/**
	 * Increases the wallet balance of the specified seller by the given wine price.
	 * @param seller the User who is selling the wine
	 * @param winePrice the price of the wine being sold
	 * @return the new wallet balance of the seller
	 */
    public int increaseBalance(String seller, int winePrice) {
		return this.users.get(seller).increaseWalletMoney(winePrice);
    }
}

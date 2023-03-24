package src.domain;

/**
 * Represents a User of the system.
 */
public class User {
	private static String id;
	private Wallet wallet;
	private Mailbox mailBox;

	/**
	 * Constructs a User object with the specified identifier.
	 * @param id the unique identifier of the User
	 */
	public User(String id) {
		this.id = id;
		this.wallet = new Wallet();
		this.mailBox = new Mailbox();
	}

	/**
	 * Constructs a User object with the specified identifier and initial wallet balance.
	 * @param id the unique identifier of the User
	 * @param wallet the initial balance of the User's wallet
	 */
	public User(String id, int wallet) {
		this.id = id;
		this.wallet = new Wallet(wallet);
		this.mailBox = new Mailbox();
	}

	/**
	 * Receives a Message and adds it to the User's Mailbox.
	 * @param message the Message to be received
	 */
	public void receiveMessage(Message message) {
		mailBox.receiveMessage(message);
	}

	/**
	 * Returns the current balance of the User's Wallet.
	 * @return the current balance of the User's Wallet
	 */
	public int getWalletMoney() {
		return this.wallet.getMoney();
	}

	/**
	 * Returns a String with all Messages in the User's Mailbox.
	 * @return a String with all Messages in the User's Mailbox
	 */
	public String readMessages() {
		return mailBox.readMessages();
	}

	/**
	 * Reduces the User's Wallet balance by the specified amount.
	 * @param winePrice the amount to be deducted from the User's Wallet
	 * @return the new balance of the User's Wallet
	 */
	public int reduceBalance(int winePrice) {
		return this.wallet.reduceBalance(winePrice);
	}

	/**
	 * Adds a new Message to the User's Mailbox.
	 * @param newMessage the Message to be added
	 */
	public void loadMessage(Message newMessage) {
		this.mailBox.loadMessage(newMessage);
	}

	/**
	 * Increases the balance of the User's Wallet by the specified amount.
	 * @param money the amount to be added to the User's Wallet
	 * @return the new balance of the User's Wallet
	 */
    public int increaseWalletMoney(int money) {
		return this.wallet.addMoney(money);
    }
}

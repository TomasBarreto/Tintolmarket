package src.interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;

import src.domain.User;
import src.domain.Wine;
/**
 * The ITintolmarketStub interface defines the methods to handle client requests.
 */
public interface ITintolmarketStub {

	/**
	 * Adds a new wine with the specified name and image.
	 * @param wine The name of the wine to add.
	 * @param image The path to the image file of the wine.
	 */
	public void addWine(String wine, String image);

	/**
	 * Sells the specified quantity of the specified wine for the specified value.
	 * @param wine the name of the wine to be sold
	 * @param value the price of the wine
	 * @param quantity the quantity of wine to be sold
	 * @param keyStorePath the path to the keystore file containing the private key used to sign the command
	 * @param keyStorePass the password to access the keystore
	 */
	public void sellWine(String wine, int value, int quantity, String keyStorePath, String keyStorePass);

	/**
	 * Displays information about the specified wine.
	 * @param wine The name of the wine to view.
	 */
	public void viewWine(String wine);

	/**
	 * Buys the specified quantity of the specified wine from the specified seller.
	 * @param wine the name of the wine to buy
	 * @param seller the name of the seller to buy from
	 * @param quantity the quantity of wine to buy
	 * @param userID the ID of the user buying the wine
	 * @param keyStorePath the path of the user's keystore
	 * @param keyStorePass the password of the user's keystore
	 * @throws RuntimeException if there is a problem with the command execution
	 */
	public void buyWine(String wine, String seller, int quantity, String userID, String keyStorePath, String keyStorePass);

	/**
	 * Displays information about the user's wallet.
	 */
	public void viewWallet();

	/**
	 * Classifies the specified wine with the specified number of stars.
	 * @param wine The name of the wine to classify.
	 * @param stars The number of stars to classify the wine with.
	 */
	public void classifyWine(String wine, float stars);

	/**
	 * Sends a message to the specified user.
	 * @param user the name of the user to send the message to
	 * @param message the message to send
	 * @param trustStorePath the path to the truststore file containing the receiver's public key
	 * @param trustStorePassword the password to access the truststore file
	 */
	public void sendMessage(String user, String message, String trustStorePath, String trustStorePassword);

	/**
	 * Reads the user's messages.
	 * @param trustStorePath the path to the trust store file
	 * @param trustStorePassword the password to access the trust store
	 */
	public void readMessages(String trustStorePath, String trustStorePassword);
}

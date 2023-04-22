package src.interfaces;

import java.awt.image.BufferedImage;

/**
 * The ITintolmarketServerSkel interface defines the methods to handle client requests.
 */
public interface ITintolmarketServerSkel {

	/**
	 * Adds a new wine to the Tintolmarket server.
	 * @param wine the name of the wine to add
	 * @param imageName the name of the image file for the wine
	 * @param imageBuffer the byte array containing the image data for the wine
	 * @return a message indicating whether the operation was successful or not
	 */
	public String addWine(String wine, String imageName, byte[] imageBuffer);

	/**
	 * Sells a wine from the Tintolmarket server.
	 * @param wine the name of the wine to sell
	 * @param value the value of the wine
	 * @param quantity the quantity of the wine
	 * @param seller the name of the seller
	 * @return a message indicating whether the operation was successful or not
	 */
	public String sellWine(String wine, int value, int quantity, String seller, String keyStorePath, String keyStorePass);

	/**
	 * Views information about a wine from the Tintolmarket server.
	 * @param wine the name of the wine to view
	 * @return a message containing information about the wine
	 */
	public String viewWine(String wine);

	/**
	 * Buys a wine from the Tintolmarket server.
	 * @param wine the name of the wine to buy
	 * @param seller the name of the seller
	 * @param quantity the quantity of the wine to buy
	 * @param userID the ID of the user who is buying the wine
	 * @return a message indicating whether the operation was successful or not
	 */
	public String buyWine(String wine, String seller, int quantity, String userID, String keyStorePath, String keyStorePass);

	/**
	 * Views the wallet of a user from the Tintolmarket server.
	 * @param userID the ID of the user to view the wallet of
	 * @return a message containing information about the user's wallet
	 */
	public String viewWallet(String userID);

	/**
	 * Classifies a wine from the Tintolmarket server.
	 * @param wine the name of the wine to classify
	 * @param stars the number of stars to give the wine
	 * @return a message indicating whether the operation was successful or not
	 */
	public String classifyWine(String wine, float stars);

	/**
	 * Sends a message from one user to another on the Tintolmarket server.
	 * @param user the name of the user to send the message to
	 * @param userFrom the name of the user who is sending the message
	 * @param message the content of the message to send
	 * @return a message indicating whether the operation was successful or not
	 */
	public String sendMessage(String user, String userFrom, String message) ;

	/**
	 * Reads messages for a user from the Tintolmarket server.
	 * @param userID the ID of the user to read messages for
	 * @return a message containing the user's messages
	 */
	public String readMessages(String userID);
}

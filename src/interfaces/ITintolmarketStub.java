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
	 * @param wine The name of the wine to sell.
	 * @param value The value to sell each unit of the wine for.
	 * @param quantity The quantity of the wine to sell.
	 */
	public void sellWine(String wine, int value, int quantity);

	/**
	 * Displays information about the specified wine.
	 * @param wine The name of the wine to view.
	 */
	public void viewWine(String wine);

	/**
	 * Buys the specified quantity of the specified wine from the specified seller.
	 * @param wine The name of the wine to buy.
	 * @param seller The name of the seller to buy the wine from.
	 * @param quantity The quantity of the wine to buy.
	 */
	public void buyWine(String wine, String seller, int quantit, String userId);

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
	 * @param user The name of the user to send the message to.
	 * @param message The message to send.
	 */
	public void sendMessage(String user, String message);

	/**
	 * Reads the user's messages.
	 */
	public void readMessages();
}

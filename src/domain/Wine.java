package src.domain;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Represents a wine object, containing information about its name, image url, rating, and sellers list.
 */
public class Wine {
	
	private final String name;
	private final String imageUrl;
	private Rating averageRating;
	private HashMap<String,WineSeller> sellersList;

	/**
	 * Constructs a new Wine object with the specified name and image URL.
	 * @param name the name of the wine
	 * @param url the URL of the image associated with the wine
	 */
	public Wine(String name, String url){
		this.name = name;
		this.imageUrl = url;
		this.averageRating = new Rating();
		this.sellersList = new HashMap<String,WineSeller>();
    }

	/**
	 * Constructs a new Wine object with the specified name, image URL, and rating.
	 * @param name the name of the wine
	 * @param url the URL of the image associated with the wine
	 * @param rating the rating of the wine
	 */
	public Wine(String name, String url, Rating rating){
		this.name = name;
		this.imageUrl = url;
		this.averageRating = rating;
		this.sellersList = new HashMap<String,WineSeller>();
	}

	/**
	 * Updates the classification of the wine based on a new rating.
	 * @param rating the new rating
	 */
    public void updateClassification(int rating){
    	this.averageRating.update(rating);
    }

	/**
	 * Adds a new seller to the list of sellers of the wine.
	 * If the seller is already in the list, updates the quantity and price of the wine.
	 * @param seller the name of the seller
	 * @param value the price of the wine
	 * @param quantity the quantity of the wine
	 * @param wineFH the WineFileHandler to handle file I/O
	 */
    public void addNewSeller(String seller, int value, int quantity, WineFileHandler wineFH) {
		WineSeller target = getSeller(seller);
		Command cmd = new Command();
		
		if(target != null) {

			cmd.setCommand("updateSellerStats");
			cmd.setWine(this.name);
			cmd.setWineSeller(seller);
			cmd.setWineQuantity(target.getQuantity() + quantity);
			cmd.setWinePrice(value);

			target.setPrice(value);
			target.setQuantity(target.getQuantity() + quantity);
			wineFH.alterFile(cmd);
		}
		else {
			target = new WineSeller(seller,value,quantity);

			this.sellersList.put(seller, target);
			
			cmd.setCommand("addSeller");
			cmd.setWine(this.name);
			cmd.setWineSeller(seller);
			cmd.setWineQuantity(quantity);
			cmd.setWinePrice(value);
			
			wineFH.alterFile(cmd);
		}
	}

	/**
	 * Returns a string representation of the wine's information, including its name,
	 image URL, average rating, and list of sellers.
	 @return a string representation of the wine's information
	 */
    public String wineInfo() {

    	StringBuilder sb = new StringBuilder();
    	sb.append("Wine " + this.name + ":\n");
    	sb.append("	Associated Image: " + this.imageUrl + "\n");
    	sb.append("	Average Rating: " + this.averageRating.getRating() + "\n");
    	
    	if(stockAvailable()) {
    		sb.append("	Sellers: \n");
    		
    		for(WineSeller target : this.sellersList.values())
				if(target.getQuantity() != 0)
    				sb.append("\t\tSeller: " + target.getSeller()
    							+ "	Price: " + target.getPrice()
    							+ "	Quantity: " + target.getQuantity()
    							+ "\n");
    	}

		return sb.toString();
    }

	/**
	 * Returns the WineSeller object associated with the given seller name.
	 * @param seller The name of the seller.
	 * @return The WineSeller object associated with the given seller name,
	 or null if the seller is not found.
	 */
    private WineSeller getSeller(String seller) {
		if(sellersList.containsKey(seller))
			return sellersList.get(seller);
	
		return null;
	}

	/**
	 * Checks if the wine is available in stock from any seller.
	 * @return True if the wine is available in stock from any seller, false otherwise.
	 */
	private boolean stockAvailable() {
		if(this.sellersList.size() == 0)
			return false;

		return true;
	}

	/**
	 * Updates the average rating of the wine.
	 * @param stars The new rating to add to the average.
	 */
	public void updateRating(float stars) {
		this.averageRating.update(stars);
	}

	/**
	 * Buys a given quantity of wine from a given seller, if the seller has enough stock and the buyer has enough balance.
	 * @param seller The name of the seller to buy from.
	 * @param quantity The quantity of wine to buy.
	 * @param balance The balance of the buyer's wallet.
	 * @param wineFH The WineFileHandler object to alter the file with the updated seller statistics.
	 * @return A string indicating whether the order was successful, or an error message.
	 */
	public String buy(String seller, int quantity, int balance, WineFileHandler wineFH) {
		if(!sellersList.containsKey(seller))
			return "This seller is not selling this wine";

		WineSeller sellerBuy = sellersList.get(seller);
		Command cmd = new Command();
		
		if (sellerBuy.getQuantity()<quantity) 
			return "There is not enough stock at the moment.";
		else if(quantity*sellerBuy.getPrice()>balance)
			return "There is not enough money in your wallet.";



		if (sellerBuy.getQuantity()==quantity) {
			cmd.setCommand("updateSellerStats");
			cmd.setWine(this.name);
			cmd.setWineSeller(seller);
			cmd.setWinePrice(sellerBuy.getPrice());
			cmd.setWineQuantity(sellerBuy.getQuantity() - quantity);
			sellerBuy.removeQuantity(quantity);
			wineFH.alterFile(cmd);
			return "Success! Your order is completed!";
		}
		

		
		cmd.setCommand("updateSellerStats");
		cmd.setWine(this.name);
		cmd.setWineSeller(seller);
		cmd.setWineQuantity(sellerBuy.getQuantity() - quantity);
		cmd.setWinePrice(sellerBuy.getPrice());
		sellerBuy.removeQuantity(quantity);
		wineFH.alterFile(cmd);
			
		return "Success! Your order is completed!";
	}

	/**
	 * Gets the price of the wine from the specified seller.
	 * @param seller the name of the seller
	 * @return the price of the wine from the specified seller
	 */
	public int getPrice(String seller) {
		return this.sellersList.get(seller).getPrice();
	}

	/**
	 * Loads a new seller with the specified name, value, and quantity to the wine's sellers list.
	 * @param seller the name of the seller
	 * @param value the value of the wine
	 * @param quantity the quantity of the wine
	 */
	public void loadSeller(String seller, String value, String quantity) {
		WineSeller wineSeller = new WineSeller(seller, Integer.parseInt(value), Integer.parseInt(quantity));
		this.sellersList.put(seller, wineSeller);

	}

	/**
	 * Gets the image URL of the wine.
	 * @return the image URL of the wine
	 */
	public String getUrl() {
		return this.imageUrl;
	}
}
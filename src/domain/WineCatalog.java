
package src.domain;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * WineCatalog class represents a catalog of wines and their information, including
 the sellers and ratings.
 */
public class WineCatalog {
	private HashMap<String, Wine> wineCat;
	private WineFileHandler wineFH;

	/**
	 * Constructor for WineCatalog class. Initializes the HashMap and WineFileHandler.
	 */
	public WineCatalog() {
		this.wineCat = new HashMap<String, Wine>();
		this.wineFH = new WineFileHandler();
	}

	/**
	 * Adds a new wine to the catalog.
	 * @param wineName The name of the new wine.
	 * @param imageName The name of the image file for the new wine.
	 * @param imageBuffer The byte array of the image for the new wine.
	 * @return Returns true if the wine was added successfully, false otherwise.
	 */
	public boolean addWine(String wineName, String imageName, byte[] imageBuffer) {
		if(wineCat.containsKey(wineName)) 
			return false;
		
		String newUrl = "imgs/" + wineName + "." + imageName.split("\\.")[1];
		
		Wine newWine = new Wine(wineName, newUrl);
		wineCat.put(wineName, newWine);

		Command cmd = new Command();
		cmd.setCommand("addWine");
		cmd.setWine(wineName);
		cmd.setImageName(newUrl);
		
		wineFH.alterFile(cmd);
		
		saveImageOnServer(imageName, imageBuffer, newUrl);
		
		return true;
	}

	/**
	 * Sells a specific quantity of a wine to a seller.
	 * @param wine The name of the wine to be sold.
	 * @param value The value of the wine.
	 * @param quantity The quantity of the wine to be sold.
	 * @param seller The name of the seller who is buying the wine.
	 * @return Returns true if the sale was successful, false otherwise.
	 */
	public boolean sellWine(String wine, int value, int quantity, String seller) {
		if(wineCat.containsKey(wine)) {
			Wine target = wineCat.get(wine);
			target.addNewSeller(seller, value, quantity, this.wineFH);

			return true;
		}
		
		return false;
	}

	/**
	 * Views information about a specific wine.
	 * @param wine The name of the wine to view information for.
	 * @return Returns a String containing the information for the requested wine.
	 */
	public String viewWine(String wine) {
		if(!wineCat.containsKey(wine)) 
			return "We are sorry, but this wine is not available at the moment.";
		else {
			return getInfo(wine);
		}
	}

	/**
	 * Helper method for viewWine that returns information for a specific wine.
	 * @param wine The name of the wine to get information for.
	 * @return Returns a String containing the information for the requested wine.
	 */
	private String getInfo(String wine) {
		return wineCat.get(wine).wineInfo();
	}

	/**
	 * Classifies a specific wine with a star rating.
	 * @param wine The name of the wine to be classified.
	 * @param stars The star rating to assign to the wine.
	 * @return Returns true if the classification was successful, false otherwise.
	 */
	public boolean classifyWine(String wine, float stars) {
		Wine target = this.wineCat.get(wine);
		
		if(target != null) {
			target.updateRating(stars);
			
			Command cmd = new Command();
			
			cmd.setCommand("updateRating");
			cmd.setWine(wine);
			cmd.setWineStars(stars);
			this.wineFH.alterFile(cmd);
			
			return true;
		}
		
		return false;
	}

	/**
	 * Buys a specified quantity of a Wine object from a specified seller, given a buyer's balance.
	 * @param wine The name of the Wine object to buy.
	 * @param seller The name of the seller to buy the Wine object from.
	 * @param quantity The quantity of the Wine object to buy.
	 * @param balance The balance of the buyer purchasing the Wine object.
	 * @return A string indicating whether the purchase was successful or not.
	 */
	public String buyWine(String wine, String seller,int quantity, int balance) {
	    if(!wineCat.containsKey(wine)) {
	        return "Wine not available...";
	    }
	    else {
			String answer = wineCat.get(wine).buy(seller, quantity, balance, this.wineFH);
	        return answer;
	    }
	}

	/**
	 * Gets the price of a specified Wine object from a specified seller.
	 * @param wine The name of the Wine object to get the price of.
	 * @param seller The name of the seller to get the price from.
	 * @return The price of the Wine object from the specified seller.
	 */
	public int getWinePrice(String wine, String seller) {
		return wineCat.get(wine).getPrice(seller);
	}

	/**
	 * Loads a new Wine object into the WineCatalog with a specified name, image, and
	 average rating.
	 * @param wine The name of the new Wine object to load.
	 * @param image The name of the image file associated with the new Wine object.
	 * @param avNumber The average number of stars for the new Wine object.
	 * @param avTotal The total number of stars for the new Wine object.
	 */
    public void loadWine(String wine, String image, String avNumber, String avTotal) {
		Rating rating = new Rating();
		rating.setCounter(Integer.parseInt(avNumber));
		rating.setStarsSum(Float.parseFloat(avTotal));
		Wine newWine = new Wine(wine, image, rating);
		this.wineCat.put(wine, newWine);

	}

	/**
	 * Loads a new Seller object into a Wine object in the WineCatalog with a specified
	 name, location, and price.
	 * @param seller An array containing the name, location, and price of the new Seller
	 object to load.
	 */
	public void loadSeller(String[] seller) {
		Wine target = this.wineCat.get(seller[0]);
		target.loadSeller(seller[1], seller[2], seller[3]);
	}

	/**
	 * Saves a Wine object's image file to a specified URL.
	 * @param imageName The name of the Wine object's image file.
	 * @param imageBuffer A byte array containing the image data for the Wine object.
	 * @param newUrl The URL to save the image file to.
	 */
	private void saveImageOnServer(String imageName, byte[] imageBuffer, String newUrl) {
		
		try {
			ByteArrayInputStream bs = new ByteArrayInputStream(imageBuffer);

			bs.read(imageBuffer);

			File newFile = new File(newUrl);
			FileOutputStream fo = new FileOutputStream(newFile);
			fo.write(imageBuffer);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the URL of the image associated with the given wine.
	 * @param wine the name of the wine to retrieve the URL of its image
	 * @return the URL of the image associated with the given wine
	 */
	public String getWineUrl(String wine) {
		return this.wineCat.get(wine).getUrl();
	}
}

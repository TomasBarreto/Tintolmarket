package src.domain;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Wine {
	
	private final String name;
	private final String imageUrl;
	private Rating averageRating;
	private HashMap<String,WineSeller> sellersList;
	
	public Wine(String name, String url){
		this.name = name;
		this.imageUrl = url;
		this.averageRating = new Rating();
		this.sellersList = new HashMap<String,WineSeller>();
    }

	public Wine(String name, String url, Rating rating){
		this.name = name;
		this.imageUrl = url;
		this.averageRating = rating;
		this.sellersList = new HashMap<String,WineSeller>();
	}
    
    public void updateClassification(int rating){
    	this.averageRating.update(rating);
    }
    
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

    private WineSeller getSeller(String seller) {
		if(sellersList.containsKey(seller))
			return sellersList.get(seller);
	
		return null;
	}
	
	private boolean stockAvailable() {
		if(this.sellersList.size() == 0)
			return false;

		return true;
	}

	public void updateRating(float stars) {
		this.averageRating.update(stars);
	}
	
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

	public int getPrice(String seller) {
		return this.sellersList.get(seller).getPrice();
	}

	public void loadSeller(String seller, String value, String quantity) {
		WineSeller wineSeller = new WineSeller(seller, Integer.parseInt(value), Integer.parseInt(quantity));
		this.sellersList.put(seller, wineSeller);

	}

	public String getUrl() {
		return this.imageUrl;
	}
}
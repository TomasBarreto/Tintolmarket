package src.domain;

import java.util.HashMap;

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
    
    public void updateClassification(int rating){
    	this.averageRating.update(rating);
    }
    
    public void addNewSeller(String seller, int value, int quantity) {
		WineSeller target = getSeller(seller);
		
		if(target != null) {
			target.setQuantity(target.getQuantity() + quantity);
			target.setPrice(value);
		}
		else {
			target = new WineSeller(seller,value,quantity);
			target.setQuantity(quantity);
			target.setPrice(value);
			
			this.sellersList.put(seller, target);
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
    			sb.append("	Seller: " + target.getSeller() 
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
		
		for(WineSeller target : this.sellersList.values())
			if(target.getQuantity() != 0)
				return false;
		
		return true;
	}

	public void updateRating(int stars) {
		this.averageRating.update(stars);
	}
	
	public String buy(String seller, int quantity, int balance) {
		WineSeller sellerBuy = sellersList.get(seller);
		if (sellerBuy.getQuantity()<quantity) 
			return "There is not enough stock at the moment.";
		else if(quantity*sellerBuy.getPrice()>balance)
			return "There is not enough money in your wallet.";
		else {
			sellerBuy.removeQuantity(quantity);
			if (sellerBuy.getQuantity()==quantity)
				sellersList.remove(seller);
			return "Success! Your order is completed!";
		}
	}

	public int getPrice(String seller) {
		return this.sellersList.get(seller).getPrice();
	}
}
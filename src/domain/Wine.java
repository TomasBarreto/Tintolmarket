package src.domain;

import java.util.ArrayList;
import java.util.List;

public class Wine {
	
	private final String name;
	private final String imageUrl;
	private Rating averageRating;
	private List<WineSeller> sellersList;
	
	public Wine(String name, String url){
		this.name = name;
		this.imageUrl = url;
		this.averageRating = new Rating();
		this.sellersList = new ArrayList<>();
    }
    
    public void updateClassification(float rating){
    	this.averageRating.update(rating);
    }
    
	public void addNewSeller(String seller, int value, int quantity) {
		WineSeller target = getSeller(seller);
		
		if(target != null) {
			target.setQuantity(target.getQuantity() + quantity);
			target.setPrice(value);
		}
		else {
			target = new WineSeller(seller);
			target.setQuantity(quantity);
			target.setPrice(value);
			
			this.sellersList.add(target);
		}
	}
	
	public String wineInfo() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Wine " + this.name + ":\n");
    	sb.append("	Associated Image: " + this.imageUrl + "\n");
    	sb.append("	Average Rating: " + this.averageRating.value() + "\n");
    	
    	if(stockAvailable()) {
    		sb.append("	Sellers: \n");
    		
    		for(WineSeller target : this.sellersList)
    			sb.append("	Seller: " + target.getSeller() 
    						+ "	Price: " + target.getPrice() 
    						+ "	Quantity: " + target.getQuantity() 
    						+ "\n");
    	}
    
    	return sb.toString();
    }

	private WineSeller getSeller(String seller) {
		for (WineSeller target : this.sellersList)
			if(target.getSeller().equals(seller))
				return target;
		
		return null;
	}
	
	private boolean stockAvailable() {
		if(this.sellersList.size() == 0)
			return false;
		
		for(WineSeller target : this.sellersList)
			if(target.getQuantity() != 0)
				return false;
		
		return true;
	}
}
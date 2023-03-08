
package src.domain;

import java.util.HashMap;

public class WineCatalog {
	private HashMap<String, Wine> wineCat;

	public WineCatalog() {
		this.wineCat = new HashMap<String, Wine>();
	}

	public boolean addWine(String wineName, String imageUrl) {
		if(wineCat.containsKey(wineName)) 
			return false;
		
		Wine newWine = new Wine(wineName, imageUrl);
		wineCat.put(wineName, newWine);
		
		return true;
	}

	public boolean sellWine(String wine, int value, int quantity, String seller) {
		if(wineCat.containsKey(wine)) {
			Wine target = wineCat.get(wine);
			target.addNewSeller(seller, value, quantity);
			
			return true;
		}
		
		return false;
	}

	public String viewWine(String wine) {
		if(!wineCat.containsKey(wine)) 
			return "We are sorry, but this wine is not available at the moment.";
		else {
			return getInfo(wine);
		}
	}
	
	private String getInfo(String wine) {
		return wineCat.get(wine).wineInfo();
	}

	public boolean classifyWine(String wine, int stars) {
		Wine target = this.wineCat.get(wine);
		
		if(target != null) {
			target.updateRating(stars);
			
			return true;
		}
		
		return false;
	}
	
	public String buyWine(String wine, String seller,int quantity, int balance) {
	    if(!wineCat.containsKey(wine)) {
	        return "Wine not available...";
	    }
	    else {
	        return wineCat.get(wine).buy(seller, quantity, balance);
	    }
	}
}

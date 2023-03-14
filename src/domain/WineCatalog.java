
package src.domain;

import java.util.HashMap;

public class WineCatalog {
	private HashMap<String, Wine> wineCat;
	private WineFileHandler wineFH;

	public WineCatalog() {
		this.wineCat = new HashMap<String, Wine>();
		this.wineFH = new WineFileHandler();
	}

	public boolean addWine(String wineName, String imageUrl) {
		if(wineCat.containsKey(wineName)) 
			return false;
		
		Wine newWine = new Wine(wineName, imageUrl);
		wineCat.put(wineName, newWine);

		Command cmd = new Command();
		cmd.setCommand("addWine");
		cmd.setWine(wineName);
		cmd.setImage(imageUrl);
		
		wineFH.alterFile(cmd);
		
		return true;
	}

	public boolean sellWine(String wine, int value, int quantity, String seller) {
		if(wineCat.containsKey(wine)) {
			Wine target = wineCat.get(wine);
			target.addNewSeller(seller, value, quantity, this.wineFH);

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
			
			Command cmd = new Command();
			
			cmd.setCommand("updateRating");
			cmd.setWine(wine);
			cmd.setWineStars(stars);
			
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

	public int getWinePrice(String wine, String seller) {

			return wineCat.get(wine).getPrice(seller);
	}

    public void loadWine(String wine, String image, String avNumber, String avTotal) {
		Rating rating = new Rating();
		rating.setCounter(Integer.parseInt(avNumber));
		rating.setStarsSum(Integer.parseInt(avTotal));
		Wine newWine = new Wine(wine, image, rating);
		this.wineCat.put(wine, newWine);
		System.out.println("Added " + this.wineCat.get(wine));

	}

	public void loadSeller(String[] seller) {
		Wine target = this.wineCat.get(seller[0]);
		target.loadSeller(seller[1], seller[2], seller[3]);
	}
}

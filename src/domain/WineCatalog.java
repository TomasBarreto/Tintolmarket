package src.domain;

import java.util.HashMap;

public class WineCatalog {
	private HashMap<String,Wine> wineCat;
	
	public WineCatalog() {
		this.wineCat = new HashMap<String,Wine>();	
	}
	
	public void addWine(String name, String imageUrl) {
		Wine newWine = new Wine(name,imageUrl);
	}
	public String getInfo(String name) {
		return wineCat.get(name).wineInfo();
	}
	
	public boolean containWine(String name) {
		if(wineCat.contains(name)) {
			return true;
		}
		else {
			return false;
		}
	}
}

package src.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SellerList {
	private HashMap<String,List<WineSeller>> listSeller;
	
	public SellerList() {
		this.listSeller = new HashMap <String,List<WineSeller>>();
	}
	public void newWine(String wine) {
		List<WineSeller> addWine = new ArrayList<WineSeller>();
		listSeller.put(wine,addWine);
	}
	public void addSeller(String wine, int value, int quantity, String seller) {
		WineSeller newSeller = new WineSeller(seller, value, quantity);
		List<WineSeller> updatedList = listSeller.get(wine);
		updatedList.add(newSeller);
		listSeller.replace(wine,updatedList);
	}
	
}
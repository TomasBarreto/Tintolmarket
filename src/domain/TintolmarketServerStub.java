package src.domain;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import src.interfaces.ITintolmarketServerStub;

public class TintolmarketServerStub implements ITintolmarketServerStub {
	
	private UserCatalog userCat;
	private SellerList listSeller;
	private WineCatalog wineCat;
	
	public TintolmarketServerStub() {
		this.userCat = new UserCatalog();
		this.listSeller = new SellerList();
		this.wineCat = new WineCatalog();
	}
	
	@Override
	public void addWine(String wine, String image, ObjectOutputStream outStream) {
		if(!wineCat.containsWine(wine)) {
			List<WineSeller> listSeller= new ArrayList<WineSeller>();
			listSeller.put(wine, newSeller);
			wineCat.addWine(wine, image);
		}
		else {
			//erro
		}
		
	}
	
	@Override
	public void sellWine(String wine, int value, int quantity, ObjectOutputStream outStream) {
		
	}
	
	@Override
	public void viewWine(String wine, ObjectOutputStream outStream) {
		StringBuilder sb = new StringBuilder();
		sb.append(wineCat.getInfo(wine));
		
	}
	
	@Override
	public void buyWine(String wine, String seller, int quantity, ObjectOutputStream outStream) {
		
	}
	
	@Override
	public void viewWallet(ObjectOutputStream outStream) {
		
	}
	
	@Override
	public void classifyWine(String wine, int stars, ObjectOutputStream outStream) {
		
	}
	
	@Override
	public void sendMessage(String user, String message, ObjectOutputStream outStream) {
		userCat.sendMessage(user, message, outStream);
	}
	
	@Override
	public void readMessages(ObjectOutputStream outStream) {
		
	}
}

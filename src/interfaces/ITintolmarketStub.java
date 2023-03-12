package src.interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;

import src.domain.User;
import src.domain.Wine;

public interface ITintolmarketStub {
	
	public void addWine(String wine, String image);
	
	public void sellWine(String wine, int value, int quantity);
	
	public void viewWine(String wine);
	
	public void buyWine(String wine, String seller, int quantity);
	
	public void viewWallet();
	
	public void classifyWine(String wine, int stars);

	public void sendMessage(String user, String message);
	
	public void readMessages();
}

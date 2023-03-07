package src.interfaces;

import java.io.ObjectOutputStream;

import src.domain.User;
import src.domain.Wine;

public interface ITintolmarketStub {
	
	public boolean addWine(String wine, String image);
	
	public boolean sellWine(String wine, int value, int quantity);
	
	public String viewWine(String wine);
	
	public String buyWine(String wine, String seller, int quantity);
	
	public int viewWallet();
	
	public boolean classifyWine(String wine, int stars);

	public boolean sendMessage(String user, String message);
	
	public String readMessages();
}

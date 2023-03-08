package src.interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface ITintolmarketServerSkel {
	public boolean addWine(String wine, String image);
	
	public boolean sellWine(String wine, int value, int quantity, String seller);
	
	public String viewWine(String wine);
	
	public void buyWine(String wine, String seller, int quantity);
	
	public String viewWallet(String userID);
	
	public void classifyWine(String wine, int stars);

	public void sendMessage(String user, String message) throws IOException;
	
	public void readMessages(String userID);
}

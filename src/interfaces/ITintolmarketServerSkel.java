package src.interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface ITintolmarketServerSkel {
	public boolean addWine(String wine, String image);
	
	public boolean sellWine(String wine, int value, int quantity, String seller);
	
	public String viewWine(String wine);
	
	public String buyWine(String wine, String seller, int quantity, String userID);
	
	public int viewWallet(String userID);
	
	public boolean classifyWine(String wine, int stars);

	public boolean sendMessage(String user, String message) throws IOException;
	
	public String readMessages(String userID);
}

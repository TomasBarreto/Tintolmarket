package src.interfaces;

import java.io.IOException;
import java.io.ObjectOutputStream;

public interface ITintolmarketServerStub {
	public boolean addWine(String wine, String image, ObjectOutputStream outStream);
	
	public boolean sellWine(String wine, int value, int quantity, String seller, ObjectOutputStream outStream);
	
	public String viewWine(String wine, ObjectOutputStream outStream);
	
	public void buyWine(String wine, String seller, int quantity, ObjectOutputStream outStream);
	
	public void viewWallet(ObjectOutputStream outStream);
	
	public void classifyWine(String wine, int stars, ObjectOutputStream outStream);

	public void sendMessage(String user, String message, ObjectOutputStream outStream) throws IOException;
	
	public void readMessages(ObjectOutputStream outStream);
}

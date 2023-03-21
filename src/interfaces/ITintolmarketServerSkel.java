package src.interfaces;

public interface ITintolmarketServerSkel {
	public String addWine(String wine, String image);
	
	public String sellWine(String wine, int value, int quantity, String seller);
	
	public String viewWine(String wine);
	
	public String buyWine(String wine, String seller, int quantity, String userID);
	
	public String viewWallet(String userID);
	
	public String classifyWine(String wine, float stars);

	public String sendMessage(String user, String userFrom, String message) ;
	
	public String readMessages(String userID);
}

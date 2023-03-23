
package src.domain;

import src.interfaces.ITintolmarketServerSkel;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TintolmarketServerSkel implements ITintolmarketServerSkel {
	
	private UserCatalog userCat;
	private WineCatalog wineCat;
	
	public TintolmarketServerSkel() {
		this.userCat = new UserCatalog();
		this.wineCat = new WineCatalog();
		loadUsers();
		loadWine();
		loadSellers();
		loadMessages();
	}

	@Override
	public synchronized String addWine(String wine, String imageName, byte[] imageBuffer) {
		boolean value = wineCat.addWine(wine, imageName, imageBuffer);
		if(value){
			return "Wine added successfully\n";
		}else {
			return "Wine already in system\n";
		}
	}

	public synchronized String sellWine(String wine, int value, int quantity, String seller) {
		boolean bool = wineCat.sellWine(wine, value, quantity, seller);
		if (bool){
			return "Wine is now for sale\n";
		}else{
			return "Wine doesnt exist\n";
		}
	}

	public synchronized String viewWine(String wine) {
		return wineCat.viewWine(wine);
	}
	
	public synchronized String buyWine(String wine, String seller, int quantity, String userID) {
		int balance = userCat.getWalletMoney(userID);
		String result = wineCat.buyWine(wine, seller, quantity, balance);
		if(result.equals("Success! Your order is completed!")){
			int winePrice = wineCat.getWinePrice(wine, seller);
			int buyerWallet = this.userCat.reduceBalance(userID, winePrice * quantity);
			int sellerWallet = this.userCat.increaseBalance(seller, winePrice * quantity);

			try{
				File file = new File("Users");
				Scanner scanner = new Scanner(file);
				List<String> lines = new ArrayList<>();
				while(scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String userAndPass [] = line.split(":");
					if (userAndPass[0].equals(userID)){
						lines.add(userAndPass[0] + ":" + userAndPass[1] + ":" + buyerWallet);
					} else if (userAndPass[0].equals(seller)) {
						lines.add(userAndPass[0] + ":" + userAndPass[1] + ":" + sellerWallet);
					} else {
						lines.add(line);
					}
				}

				FileWriter fw = new FileWriter("Users", false);

				for(int i = 0; i < lines.size(); i++){
					fw.write(lines.get(i) + "\n");
				}

				scanner.close();
				fw.close();

			} catch (FileNotFoundException e){
				System.out.println("Users file not found\n");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}


		}

		return result + "\n";
	}

	public synchronized String viewWallet(String userID) {
		return "Wallet: " + this.userCat.getWalletMoney(userID) + "\n";
	}
	
	public synchronized String classifyWine(String wine, float stars) {
		boolean value = this.wineCat.classifyWine(wine, stars);
		if(value){
			return "Wine classified successfully\n";
		} else{
			return "Wine doesnt exist\n";
		}
	}
	
	public synchronized String sendMessage(String user, String userFrom, String message) {
		boolean value = userCat.sendMessage(user, userFrom, message);
		if(value){
			return "Message sent!\n";
		} else{
			return "User not found\n";
		}
	}

	public synchronized String readMessages(String userID) {
		return userCat.readMessages(userID);
	}

	private void loadUsers() {
		try{
			File file = new File("Users");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String userAndPass [] = line.split(":");
				if(userAndPass.length > 1)
					this.userCat.addUser(userAndPass[0], userAndPass[2]);
			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}

	}

	public void addUser(String userID) {
		this.userCat.addUser(userID);
	}

	private void loadWine() {
		try{
			File file = new File("wine_cat");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String wine [] = line.split(":");
				if(wine.length > 1)
					this.wineCat.loadWine(wine[0], wine[1], wine[2], wine[3]);
			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}
	}

	private void loadSellers() {
		try{
			File file = new File("wine_sellers");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String seller [] = line.split(":");
				if (seller.length > 1)
					this.wineCat.loadSeller(seller);
			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}
	}

	private void loadMessages() {
		try{
			File file = new File("messages");
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String msg [] = line.split(":");
				if (msg.length > 1){
					this.userCat.loadMessage(msg[1], msg[3], msg[5]);
				}

			}
		} catch (FileNotFoundException e){
			System.out.println("Users file not found\n");
		}
	}

	public byte[] getImage(String wine) {

		String wineUrl = wineCat.getWineUrl(wine);

		FileInputStream fs = null;

		File file = new File(wineUrl);
		byte[] bytes = new byte[(int) file.length()];
		try {
			fs = new FileInputStream(file);
			fs.read(bytes);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return bytes;
	}

	public synchronized String getImageUrl(String wine) {
		return this.wineCat.getWineUrl(wine);
	}
}

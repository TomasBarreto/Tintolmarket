
package src.domain;

import src.interfaces.ITintolmarketServerSkel;

import java.awt.image.BufferedImage;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The TintolmarketServerSkel class represents a skeleton of the Tintolmarket server.
 */
public class TintolmarketServerSkel implements ITintolmarketServerSkel {
	
	private UserCatalog userCat;
	private WineCatalog wineCat;
	private final String USERS = "users";
	private long currentBlock;
	private final String CURR_BLOCK_FILE = "currBlk";
	private final String ALIAS = "server";

	/**
	 * Constructor that initializes the user and wine catalogs and loads them from files.
	 */
	public TintolmarketServerSkel() {
		this.userCat = new UserCatalog();
		this.wineCat = new WineCatalog();
		loadUsers();
		loadWine();
		loadSellers();
		loadMessages();
		try {
			FileInputStream fs = new FileInputStream(CURR_BLOCK_FILE);
			this.currentBlock = fs.read();

			fs.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a wine to the catalog.
	 * @param wine The name of the wine to add.
	 * @param imageName The name of the image of the wine.
	 * @param imageBuffer The byte array containing the image data.
	 * @return A message indicating whether the wine was successfully added or if it already exists in the catalog.
	 */
	public synchronized String addWine(String wine, String imageName, byte[] imageBuffer) {
		boolean value = wineCat.addWine(wine, imageName, imageBuffer);
		if(value){
			return "Wine added successfully\n";
		}else {
			return "Wine already in system\n";
		}
	}

	/**
	 * Puts a wine up for sale.
	 * @param wine The name of the wine to put up for sale.
	 * @param value The price of the wine.
	 * @param quantity The quantity of the wine.
	 * @param seller The name of the seller.
	 * @return A message indicating whether the wine was successfully put up for sale or if it doesn't exist in the catalog.
	 */
	public synchronized String sellWine(String wine, int value, int quantity, String seller) {
		boolean bool = wineCat.sellWine(wine, value, quantity, seller);
		if (bool){

			String transaction = "sell:" + wine + ":" + quantity + ":" + value + ":" + seller;

			writeTransaction(transaction);

			return "Wine is now for sale\n";
		}else{
			return "Wine doesnt exist\n";
		}
	}

	/**
	 * Views the information of a wine.
	 * @param wine The name of the wine to view.
	 * @return A string with the information of the wine.
	 */
	public synchronized String viewWine(String wine) {
		return wineCat.viewWine(wine);
	}

	/**
	 * Buys a wine.
	 * @param wine The name of the wine to buy.
	 * @param seller The name of the seller.
	 * @param quantity The quantity of the wine to buy.
	 * @param userID The ID of the buyer.
	 * @return A message indicating whether the purchase was successful and updating the wallets of the buyer and seller.
	 */
	public synchronized String buyWine(String wine, String seller, int quantity, String userID) {
		int balance = userCat.getWalletMoney(userID);
		String result = wineCat.buyWine(wine, seller, quantity, balance);
		if(result.equals("Success! Your order is completed!")){
			int winePrice = wineCat.getWinePrice(wine, seller);
			int buyerWallet = this.userCat.reduceBalance(userID, winePrice * quantity);
			int sellerWallet = this.userCat.increaseBalance(seller, winePrice * quantity);

			try{
				File file = new File(USERS);
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

				FileWriter fw = new FileWriter(USERS, false);

				for(int i = 0; i < lines.size(); i++){
					fw.write(lines.get(i) + "\n");
				}

				scanner.close();
				fw.close();

				String transaction = "buy:" + wine + ":" + quantity + ":" + winePrice + ":" + userID;

				writeTransaction(transaction);


			} catch (FileNotFoundException e){
				System.out.println("Users file not found\n");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return result + "\n";
	}

	/**
	 * Returns the wallet balance of a given user.
	 * @param userID the ID of the user whose wallet balance is to be returned.
	 * @return a string representation of the user's wallet balance.
	 */
	public synchronized String viewWallet(String userID) {
		return "Wallet: " + this.userCat.getWalletMoney(userID) + "\n";
	}

	/**
	 * Classifies a wine with a given number of stars.
	 * @param wine  the name of the wine to be classified.
	 * @param stars the number of stars to be given to the wine.
	 * @return a string indicating whether the wine was classified successfully or not.
	 */
	public synchronized String classifyWine(String wine, float stars) {
		if(stars > 5.0 || stars < 0){
			return "Classification must be between 0.0 and 5.0";
		}
		
		boolean value = this.wineCat.classifyWine(wine, stars);
		if(value){
			return "Wine classified successfully\n";
		} else{
			return "Wine doesnt exist\n";
		}
	}

	/**
	 * Sends a message from one user to another.
	 * @param user     the recipient of the message.
	 * @param userFrom the sender of the message.
	 * @param message  the message to be sent.
	 * @return a string indicating whether the message was sent successfully or not.
	 */
	public synchronized String sendMessage(String user, String userFrom, String message) {
		boolean value = userCat.sendMessage(user, userFrom, message);
		if(value){
			return "Message sent!\n";
		} else{
			return "User not found\n";
		}
	}

	/**
	 * Reads messages for the given user ID.
	 * @param userID the ID of the user to read messages for
	 * @return a string representing the messages for the given user ID
	 */
	public synchronized String readMessages(String userID) {
		return userCat.readMessages(userID);
	}

	/**
	 * Loads users information from the users file.
	 */
	private void loadUsers() {
		try{
			File file = new File(USERS);
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

	/**
	 * Adds a user with the given user ID.
	 * @param userID the ID of the user to add
	 */
	public void addUser(String userID) {
		this.userCat.addUser(userID);
	}

	/**
	 * Loads wines information from the wine catalog file.
	 */
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

	/**
	 * Loads sellers information from the wine sellers file.
	 */
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


	/**
	 * Loads messages from the messages file.
	 */
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

	/**
	 * Return the byte array of the image associated with the specified wine.
	 * @param wine the name of the wine to return the image for
	 * @return the byte array of the image associated with the specified wine
	 */
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

	/**
	 * Return the URL of the image associated with the specified wine.
	 * @param wine the name of the wine to return the image URL for
	 * @return the URL of the image associated with the specified wine
	 */
	public synchronized String getImageUrl(String wine) {
		return this.wineCat.getWineUrl(wine);
	}

	private void writeTransaction(String transaction) {
		String currentBlockPath = "logs/block_" + this.currentBlock + ".blk";

		try {
			FileReader fr = new FileReader(currentBlockPath);
			BufferedReader br = new BufferedReader(fr);

			String currentLine = "";

			int nrLine = 1;

			int n_trx = 0;

			List<String> lines = new ArrayList<>();

			while((currentLine = br.readLine()) != null)  {
				if(nrLine == 3) {
					n_trx = Integer.parseInt(currentLine);
					lines.add((n_trx + 1) + "");
				}
				else
					lines.add(currentLine);


				nrLine++;
			}

			lines.add(transaction);

			FileWriter fw1 = new FileWriter(currentBlockPath, false);

			for(String line : lines)
				fw1.write(line + "\n");

			fw1.close();
			fr.close();
			br.close();

			if(n_trx == 4) {
				this.currentBlock++;

				FileWriter fw2 = new FileWriter(CURR_BLOCK_FILE, false);

				fw2.write(this.currentBlock + "");

				fw2.close();

				FileInputStream fis = new FileInputStream(new File("logs/block_" + (this.currentBlock - 1) + ".blk"));

				byte[] fileBytes = fis.readAllBytes();

				String keyStorePath = System.getProperty("javax.net.ssl.keyStore");
				char[] keyStorePass = System.getProperty("javax.net.ssl.keyStorePassword").toCharArray();

				KeyStore keyStore = KeyStore.getInstance("JCEKS");
				FileInputStream fis2 = new FileInputStream(keyStorePath);

				keyStore.load(fis2, keyStorePass);

				PrivateKey privateKey = (PrivateKey) keyStore.getKey(this.ALIAS, keyStorePass);

				Signature signature = Signature.getInstance("MD5withRSA");
				signature.initSign(privateKey);

				signature.update(fileBytes);
				byte[] signedBytes = signature.sign();

				FileOutputStream fos = new FileOutputStream("logs/block_" + (this.currentBlock - 1) + ".blk");
				fos.write(signedBytes);

				fis.close();
				fis2.close();
				fos.close();

				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.update(signedBytes);

				byte[] fileHash = messageDigest.digest();

				new File("logs/block_" + this.currentBlock + ".blk");
				FileWriter fw = new FileWriter("logs/block_" + this.currentBlock + ".blk");
				fw.write(fileHash.toString() + "\n");
				fw.write(this.currentBlock + "\n");
				fw.write(0 + "\n");

				fw.close();
			}

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (CertificateException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		}
	}
}

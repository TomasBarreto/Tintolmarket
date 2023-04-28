
package src.domain;

import src.interfaces.ITintolmarketServerSkel;

import java.awt.image.BufferedImage;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
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
	private PBEDUsers pbedUsers;

	/**
	 * Constructor that initializes the user and wine catalogs and loads them from files.
	 */
	public TintolmarketServerSkel(PBEDUsers pbedUsers) {
		this.userCat = new UserCatalog();
		this.wineCat = new WineCatalog();
		this.pbedUsers = pbedUsers;
		loadUsers();
		loadWine();
		loadSellers();
		loadMessages();
		try {
			Scanner sc = new Scanner(new File(CURR_BLOCK_FILE));

			this.currentBlock = sc.nextInt();

			sc.close();
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
	 * @param keyStorePath The path of the keystore file.
	 * @param keyStorePass The password for the keystore file.
	 * @return A message indicating whether the wine was successfully put up for sale or if it doesn't exist in the catalog.
	 */
	public synchronized String sellWine(String wine, int value, int quantity, String seller, String keyStorePath, String keyStorePass) {
		boolean bool = wineCat.sellWine(wine, value, quantity, seller);
		if (bool){

			String transaction = "sell:" + wine + ":" + quantity + ":" + value + ":" + seller;

			writeTransaction(transaction, keyStorePath, keyStorePass);

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
	 * @param keyStorePath The path of the keystore file.
	 * @param keyStorePass The password for the keystore file.
	 * @return A message indicating whether the purchase was successful and updating the wallets of the buyer and seller.
	 */
	public synchronized String buyWine(String wine, String seller, int quantity, String userID, String keyStorePath, String keyStorePass) {
		int balance = userCat.getWalletMoney(userID);
		String result = wineCat.buyWine(wine, seller, quantity, balance);
		if(result.equals("Success! Your order is completed!")){
			int winePrice = wineCat.getWinePrice(wine, seller);
			int buyerWallet = this.userCat.reduceBalance(userID, winePrice * quantity);
			int sellerWallet = this.userCat.increaseBalance(seller, winePrice * quantity);

			try{
				List<String> fileStrings = this.pbedUsers.decrypt();
				List<String> lines = new ArrayList<>();

				for(String line : fileStrings) {
					String userAndPass [] = line.split(":");
					if (userAndPass[0].equals(userID)){
						lines.add(userAndPass[0] + ":" + userAndPass[1] + ":" + buyerWallet);
					} else if (userAndPass[0].equals(seller)) {
						lines.add(userAndPass[0] + ":" + userAndPass[1] + ":" + sellerWallet);
					} else {
						lines.add(line);
					}
				}

				this.pbedUsers.encryptAllLines(lines);

				String transaction = "buy:" + wine + ":" + quantity + ":" + winePrice + ":" + userID;

				writeTransaction(transaction, keyStorePath, keyStorePass);

			} catch (Exception e) {
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
		List<String> fileLines = this.pbedUsers.decrypt();

		for(String line : fileLines) {
			String userAndPass [] = line.split(":");
			if(userAndPass.length > 1)
				this.userCat.addUser(userAndPass[0], userAndPass[2]);
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

	/**
	 * Write a new transaction in a block.
	 * @param transaction a String representing the transaction to be added.
	 * @param keyStorePath a String representing the path to the keystore file.
	 * @param keyStorePass a String representing the password to the keystore file.
	 * @throws RuntimeException if there is an issue with file input/output or if there is an issue with the message digest algorithm.
	 */
	private synchronized void writeTransaction(String transaction, String keyStorePath, String keyStorePass) {
		String currentBlockPath = "logs/block_" + this.currentBlock + ".blk";

		try {
			FileInputStream fis = new FileInputStream(currentBlockPath);
			ObjectInputStream in = new ObjectInputStream(fis);
			Block block = (Block) in.readObject();

			block.addTransaction(transaction);

			if(block.getNrTransacions() == 5) {
				this.currentBlock++;

				FileWriter fw2 = new FileWriter(CURR_BLOCK_FILE, false);

				fw2.write(this.currentBlock + "");

				fw2.close();

				new HMacHandler().updateHMac(CURR_BLOCK_FILE);

				SignedObject signedBlock = signedObject(block, keyStorePath, keyStorePass);

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bos);
				out.writeObject(signedBlock);
				byte[] blockBytes = bos.toByteArray();

				out.close();
				bos.close();

				FileOutputStream fos = new FileOutputStream("logs/block_" + (this.currentBlock - 1) + ".blk");
				ObjectOutputStream out2 = new ObjectOutputStream(fos);
				out2.writeObject(signedBlock);

				fis.close();
				fos.close();

				MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
				messageDigest.update(blockBytes);

				byte[] fileHash = messageDigest.digest();

				Block newBlock = new Block(fileHash, this.currentBlock, 0);

				FileOutputStream fos2 = new FileOutputStream("logs/block_" + (this.currentBlock) + ".blk");
				ObjectOutputStream out3 = new ObjectOutputStream(fos2);
				out3.writeObject(newBlock);
			} else {
				FileOutputStream fos = new FileOutputStream("logs/block_" + (this.currentBlock) + ".blk");
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(block);
			}

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Signs a Block using the private key from the keystore located in the given path and with the given password.
	 * @param block the Block to sign.
	 * @param keyStorePath the path to the keystore file.
	 * @param keyStorePass the password to access the keystore.
	 * @return a SignedObject containing the signed Block object.
	 * @throws RuntimeException if there is an error accessing or reading the keystore, retrieving the private key, creating the signature, or signing the object.
	 */
	private SignedObject signedObject(Block block, String keyStorePath, String keyStorePass) {

		try {
			FileInputStream fs = new FileInputStream(keyStorePath);

			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			keyStore.load(fs, keyStorePass.toCharArray());

			PrivateKey pk = (PrivateKey) keyStore.getKey("server", keyStorePass.toCharArray());

			return new SignedObject(block, pk, Signature.getInstance("MD5withRSA"));

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException(e);
		} catch (CertificateException e) {
			throw new RuntimeException(e);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns a string with all transactions stored in the blockchain.
	 * @param keyStorePath the path to the keystore file
	 * @param keyStorePass the password to access the keystore file
	 * @return a string with all transactions stored in the blockchain
	*/
 	public synchronized String getAllTransactions(String keyStorePath, String keyStorePass) {
		List<String> transactions = new ArrayList<>();

		try {
			int currBlock = 1;

			while (currBlock <= this.currentBlock) {
				String currentBlockPath = "logs/block_" + currBlock + ".blk";
				FileInputStream fis = new FileInputStream(currentBlockPath);
				ObjectInputStream in = new ObjectInputStream(fis);
				Block block = null;
				if (currBlock == this.currentBlock){
					block = (Block) in.readObject();
				}
				else{
					block = verifySignedObject((SignedObject) in.readObject(),keyStorePath,keyStorePass);
				}
				in.close();
				fis.close();
				if (block.getNrTransacions() > 0) {
					List<String> blockTransactions = block.getTransactions();
					transactions.addAll(blockTransactions);
				}

				currBlock++;
			}
		} catch (FileNotFoundException e) {
			// End of blocks
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		StringBuilder sb = new StringBuilder();

		for (String s : transactions) {
			sb.append(s+"\n");
		}
		return sb.toString();
	}

	/**
	 * Verifies a SignedObject containing a Block by checking its signature against the server's public
	 key in the keystore.
	 * @param signedBlock the SignedObject to be verified.
	 * @param keyStorePath the file path to the keystore containing the server's public key.
	 * @param keyStorePass the password to access the keystore.
	 * @return the Block object if the signature is valid.
	 * @throws RuntimeException if the signature is invalid or if there's an error in the verification process.
	 */
	private Block verifySignedObject(SignedObject signedBlock, String keyStorePath, String keyStorePass) {
		try {
			FileInputStream fs = new FileInputStream(keyStorePath);
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			keyStore.load(fs, keyStorePass.toCharArray());

			PublicKey publicKey = keyStore.getCertificate("server").getPublicKey();

			if (signedBlock.verify(publicKey, Signature.getInstance("MD5withRSA"))) {
				return (Block) signedBlock.getObject();
			} else {
				throw new RuntimeException("Invalid signature for signed block.");
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (CertificateException e) {
			throw new RuntimeException(e);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (SignatureException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}

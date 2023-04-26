package src.domain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

import src.interfaces.ITintolmarketStub;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;


/**
 * TintolmarketStub class represents a stub to access the Tintolmarket server.
 * This class implements the ITintolmarketStub interface.
 * It is responsible for managing the connection with the server and providing access to its services.
 */
public class TintolmarketStub implements ITintolmarketStub {

	private Socket clientSocket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	private String userID;

	/**
	 * Creates a new instance of TintolmarketStub.
	 * @param socket     the client socket.
	 * @param outStream  the output stream.
	 * @param inStream   the input stream.
	 */
	public TintolmarketStub(Socket socket, ObjectOutputStream outStream, ObjectInputStream inStream) {
		this.clientSocket = socket;
		this.outStream = outStream;
		this.inStream = inStream;
	}

	/**
	 * Authenticates the user in the server.
	 * @param userID     the user ID.
	 * @return           true if authentication succeeds, false otherwise.
	 * @throws IOException            if there is an I/O error.
	 * @throws ClassNotFoundException if the class is not found.
	 */
	public boolean autenticate(String userID, String keyStoreFileName, String keyStorePass) throws IOException, ClassNotFoundException {
		outStream.writeObject(userID);

		boolean isRegistered = (boolean) inStream.readObject();
		Long nonce = (Long) inStream.readObject();

		KeyStore keyStore = null;

		try {
			String keyStorePath = "storesClient/" + keyStoreFileName;

			keyStore = KeyStore.getInstance("JCEKS");
			keyStore.load(new FileInputStream(keyStorePath), keyStorePass.toCharArray());

			PrivateKey pk = (PrivateKey) keyStore.getKey(userID, keyStorePass.toCharArray());

			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initSign(pk);
			signature.update(nonce.byteValue());
			byte[] signedNonce = signature.sign();

			outStream.writeObject(signedNonce);

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

		if (!isRegistered) {
			try {

				Certificate certificate = keyStore.getCertificate(userID);
				outStream.writeObject(certificate);

			} catch (KeyStoreException e) {
				throw new RuntimeException(e);
			}
		}

		boolean autenticated = (boolean) inStream.readObject();

		if(autenticated)
			System.out.println("Authentication Completed!");
		else
			System.out.println("There was a problem with your authentication");

		this.userID = userID;

		return autenticated;
	}

	/**
	 * Adds a wine to the server.
	 * @param wine      the wine name.
	 * @param imageUrl  the image URL.
	 */
	public void addWine(String wine, String imageUrl){

		File file = new File(imageUrl);
		try {
			FileInputStream fs = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			fs.read(bytes);

			String[] imageName = imageUrl.split(Pattern.quote(File.separator));

			Command cmd = new Command();
			cmd.setCommand("add");
			cmd.setWine(wine);
			cmd.setImageName(imageName[imageName.length - 1]);
			cmd.setImageBuffer(bytes);

			try {
				outStream.writeObject(0);
				outStream.writeObject(cmd);
				System.out.println((String)inStream.readObject());

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Sells a wine in the server.
	 * @param wine      the wine name.
	 * @param value     the wine value.
	 * @param quantity  the wine quantity.
	 */
	public void sellWine(String wine, int value, int quantity, String keyStorePath, String keyStorePass){
		Command cmd = new Command();
		cmd.setCommand("sell");
		cmd.setWine(wine);
		cmd.setWinePrice(value);
		cmd.setWineQuantity(quantity);

		SignedObject signedObject = signedObject(cmd, keyStorePath, keyStorePass);

		try {
			outStream.writeObject(1);
			outStream.writeObject(signedObject);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "view" command to the server with the specified wine to view.
	 * Displays the wine's information and asks the user if they would like to open the wine's image.
	 * @param wine the name of the wine to view
	 */
	public void viewWine(String wine){
		Command cmd = new Command();
		cmd.setCommand("view");
		cmd.setWine(wine);

		try {
			outStream.writeObject(0);
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

			String imageUrl = (String) inStream.readObject();
			byte[] imageBuffer = (byte[]) inStream.readObject();

			imageUrl = "client" + imageUrl;

			try {
				ByteArrayInputStream bs = new ByteArrayInputStream(imageBuffer);

				bs.read(imageBuffer);

				File newFile = new File(imageUrl);
				FileOutputStream fo = new FileOutputStream(newFile);
				fo.write(imageBuffer);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Scanner sc = new Scanner(System.in);
			System.out.println("Open image? (y/n)\n");
			String input = sc.nextLine();

			if(input.equals("y")) {
				File file = new File(imageUrl);
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.open(file);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "buy" command to the server with the specified wine, seller and quantity to buy.
	 * Displays a message indicating whether the purchase was successful or not.
	 * @param wine the name of the wine to buy
	 * @param seller the seller of the wine
	 * @param quantity the quantity of wine to buy
	 */
	public void buyWine(String wine, String seller, int quantity, String userID, String keyStorePath, String keyStorePass){
		Command cmd = new Command();
		cmd.setCommand("buy");
		cmd.setWine(wine);
		cmd.setWineSeller(seller);
		cmd.setWineQuantity(quantity);

		SignedObject signedObject = signedObject(cmd, keyStorePath, keyStorePass);

		try {
			outStream.writeObject(1);
			outStream.writeObject(signedObject);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "wallet" command to the server and displays the current balance of the user's wallet.
	 */
	public void viewWallet(){
		Command cmd = new Command();
		cmd.setCommand("wallet");
		cmd.setUserReceiver(userID);

		try {
			outStream.writeObject(0);
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "classify" command to the server with the specified wine and star rating to classify.
	 * Displays a message indicating whether the classification was successful or not.
	 * @param wine the name of the wine to classify
	 * @param stars the number of stars to rate the wine
	 */
	public void classifyWine(String wine, float stars){
		Command cmd = new Command();
		cmd.setCommand("classify");
		cmd.setWine(wine);
		cmd.setWineStars(stars);

		try {
			outStream.writeObject(0);
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "talk" command to the server with the specified user and message to send.
	 * Displays a message indicating whether the message was sent successfully or not.
	 * @param user the name of the user to send the message to
	 * @param message the message to send
	 * @param trustStorePath the path to the truststore file containing the receiver's public key
	 * @param trustStorePassword the password to access the truststore file
	 */
	public void sendMessage(String user, String message, String trustStorePath, String trustStorePassword){
		Command cmd = new Command();
		cmd.setCommand("talk");
		cmd.setUserReceiver(user);
		//cifrar a message com a public key do recetor

		String messageEncrypted = encryptWithReceiverPublicKey(message, user, trustStorePath ,trustStorePassword);
		cmd.setMessage(messageEncrypted);

		try {
			outStream.writeObject(0);
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Encrypts the specified message using the public key of the receiver.
	 * @param message the message to be encrypted
	 * @param user the name of the receiver user whose public key will be used for encryption
	 * @param trustStorePath the path of the truststore file containing the receiver's public key
	 * @param trustStorePassword the password to access the truststore file
	 * @return the encrypted message as a Base64-encoded string
	 * @throws RuntimeException if an error occurs during the encryption process
	 */
	private String encryptWithReceiverPublicKey(String message, String user, String trustStorePath, String trustStorePassword) {
		try {
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			FileInputStream fis = new FileInputStream(trustStorePath);
			keyStore.load(fis, trustStorePassword.toCharArray());

			Certificate certificate = keyStore.getCertificate(user);
			PublicKey pk = certificate.getPublicKey();

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, pk);

			return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));

		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (CertificateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "read" command to the server and displays all messages addressed to the user.
	 Decrypts the messages with the user's private key before displaying them.
	 * @param trustStorePath the path to the truststore containing the user's private key
	 * @param trustStorePassword the password to the truststore
	 */
	public void readMessages(String trustStorePath, String trustStorePassword){
		Command cmd = new Command();
		cmd.setCommand("read");

		try {
			outStream.writeObject(0);
			outStream.writeObject(cmd);
			// decifrar a mensagem com a private key
			String message = (String)inStream.readObject();
			if (message.equals("There are no messages available\n")){
				System.out.println(message);
				return;
			}
			String [] lines = message.split("\n");

			String result = "";

			for (String line : lines){
				String [] currentLine = line.split(":");

				if(currentLine[0].equals("text")){
					result = result + "text: " + decryptWithPrivateKey(currentLine[1].substring(1), trustStorePath, trustStorePassword) + "\n";
				}else
					result = result + line + "\n";

			}

			System.out.println(result);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Decrypts a message with the user's private key.
	 * @param message the message to be decrypted
	 * @param trustStorePath the path to the truststore file containing the user's private key
	 * @param trustStorePassword the password for the truststore file
	 * @return the decrypted message
	 */
	private String decryptWithPrivateKey(String message, String trustStorePath, String trustStorePassword) {
		try {
			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			FileInputStream fis = new FileInputStream(trustStorePath);
			keyStore.load(fis, trustStorePassword.toCharArray());

			PrivateKey pk = (PrivateKey) keyStore.getKey(userID, trustStorePassword.toCharArray());

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, pk);

			byte [] messageBytes = Base64.getDecoder().decode(message.getBytes(StandardCharsets.UTF_8));

			return new String(cipher.doFinal(messageBytes), StandardCharsets.UTF_8);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (CertificateException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e);
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Concatenates two byte arrays into a single byte array.
	 * @param a the first byte array
	 * @param b the second byte array
	 * @return a byte array resulting from the concatenation of a and b
	 */
	private byte[] concatenateByteArrays(byte[] a, byte[] b) {
		byte[] result = new byte[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	/**
	 * Sends a "stop" command to the server to terminate the connection.
	 * @throws IOException if an I/O error occurs
	 */
	public void stop() throws IOException {
		try {
			Command cmd = new Command();
			cmd.setCommand("stop");
			outStream.writeObject(0);
			outStream.writeObject(cmd);
		} catch (SocketException e) {
			System.out.println("Server Offline");
		}
	}

	/**
	 * Signs a command object using the private key associated with the user and returns the resulting signed object.
	 * @param cmd The command object to be signed.
	 * @param keyStorePath The path to the key store file.
	 * @param keyStorePass The password to access the key store.
	 * @return The signed object.
	 * @throws RuntimeException If there is an error accessing the key store or signing the object.
	 */
	private SignedObject signedObject(Command cmd, String keyStorePath, String keyStorePass) {

		try {
			FileInputStream fs = new FileInputStream(keyStorePath);

			KeyStore keyStore = KeyStore.getInstance("JCEKS");
			keyStore.load(fs, keyStorePass.toCharArray());

			PrivateKey pk = (PrivateKey) keyStore.getKey(userID, keyStorePass.toCharArray());

			return new SignedObject(cmd, pk, Signature.getInstance("MD5withRSA"));

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
	 * Sends a "list" command to the server and prints the list of all transactions.
	 */
	public void getList() {
		Command cmd = new Command();
		cmd.setCommand("list");
		try {
			outStream.writeObject(0);
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}

package src.domain;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

import src.interfaces.ITintolmarketStub;

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
	 * @param passWord   the password.
	 * @return           true if authentication succeeds, false otherwise.
	 * @throws IOException            if there is an I/O error.
	 * @throws ClassNotFoundException if the class is not found.
	 */
	public boolean autenticate(String userID, String passWord) throws IOException, ClassNotFoundException {
		outStream.writeObject(userID);
		outStream.writeObject(passWord);
		if ((boolean) inStream.readObject() == false) {
			System.out.println("closed");
			return false;
		}
		this.userID = userID;
		return true;
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
	public void sellWine(String wine, int value, int quantity){
		Command cmd = new Command();
		cmd.setCommand("sell");
		cmd.setWine(wine);
		cmd.setWinePrice(value);
		cmd.setWineQuantity(quantity);
		
		try {
			outStream.writeObject(cmd);
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
	public void buyWine(String wine, String seller, int quantity){
		Command cmd = new Command();
		cmd.setCommand("buy");
		cmd.setWine(wine);
		cmd.setWineSeller(seller);
		cmd.setWineQuantity(quantity);
		
		try {
			outStream.writeObject(cmd);
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
	 */
	public void sendMessage(String user, String message){
		Command cmd = new Command();
		cmd.setCommand("talk");
		cmd.setUserReceiver(user);
		cmd.setMessage(message);
		
		try {
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "read" command to the server and displays all messages addressed to the user.
	 */
	public void readMessages(){
		Command cmd = new Command();
		cmd.setCommand("read");
		
		try {
			outStream.writeObject(cmd);
			System.out.println((String)inStream.readObject());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends a "stop" command to the server to terminate the connection.
	 * @throws IOException if an I/O error occurs
	 */
	public void stop() throws IOException {
		try {
			Command cmd = new Command();
			cmd.setCommand("stop");
			outStream.writeObject(cmd);
		} catch (SocketException e) {
			System.out.println("Server Offline");
		}
	}
}

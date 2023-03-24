package src.domain;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Represents a command object that can be sent between client and server in a
 network communication.
 * It contains information about the action to be performed, as well as additional data
 depending on the command.
 */
public class Command implements Serializable {
    private String command;
    private String wine;
    private String imageName;

    private byte[] imageBuffer;

    private int winePrice;
    private int wineQuantity;
    private String wineSeller;
    private float wineStars;
    private String userReceiver;
    private String message;
    private String user;

    /**
     * Sets the command of the command.
     * @param command a string representing the command to be executed.
     */
    public void setCommand(String command){
        this.command = command;
    }

    /**
     * Sets the wine name of the command.
     * @param wine a string representing the name of the wine.
     */
    public void setWine(String wine){
        this.wine = wine;
    }

    /**
     * Sets the image name of the command.
     * @param imageName a string representing the name of the image.
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Sets the quantity of wine of the command.
     * @param wineQuantity an integer representing the quantity of wine.
     */
    public void setWineQuantity(int wineQuantity){
        this.wineQuantity = wineQuantity;
    }

    /**
     * Sets the price of wine of the command.
     * @param winePrice an integer representing the price of wine.
     */
    public void setWinePrice(int winePrice) {
        this.winePrice = winePrice;
    }

    /**
     * Sets the seller of the wine of the command.
     * @param wineSeller a string representing the name of the seller.
     */
    public void setWineSeller(String wineSeller) {
        this.wineSeller = wineSeller;
    }

    /**
     * Sets the stars of the wine of the command.
     * @param wineStars a float representing the rating of the wine.
     */
    public void setWineStars(float wineStars) {
        this.wineStars = wineStars;
    }

    /**
     * Sets the user who will receive the message of the command.
     * @param userReceiver a string representing the name of the user who will receive the message.
     */
    public void setUserReceiver(String userReceiver) {
        this.userReceiver = userReceiver;
    }

    /**
     * Sets the message of the command.
     * @param message a string representing the message to be sent.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the user of the command.
     * @param user a string representing the user who is sending the command.
     */
    public void setUser(String user) {
    	this.user = user;
    }

    /**
     * Returns the command string.
     * @return the command string
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the wine string.
     * @return the wine string
     */
    public String getWine() {
        return wine;
    }

    /**
     * Returns the image name string.
     * @return the image name string
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Returns the wine price.
     * @return the wine price
     */
    public int getWinePrice() {
        return winePrice;
    }

    /**
     * Returns the wine quantity.
     * @return the wine quantity
     */
    public int getWineQuantity() {
        return wineQuantity;
    }

    /**
     * Returns the name of the wine seller.
     * @return the name of the wine seller
     */
    public String getWineSeller() {
        return wineSeller;
    }

    /**
     * Returns the wine stars.
     * @return the wine stars
     */
    public float getWineStars() {
        return wineStars;
    }


    /**
     * Returns the name of the user receiver.
     * @return the name of the user receiver
     */
    public String getUserReceiver() {
        return userReceiver;
    }

    /**
     * Returns the message string.
     * @return the message string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the name of the user.
     * @return the name of the user
     */
    public String getUser() {
    	return user;
    }

    /**
     * Sets the image buffer.
     * @param buffer the byte array to set as the image buffer
     */
    public void setImageBuffer(byte[] buffer) {
        this.imageBuffer = buffer;
    }

    /**
     * Returns the image buffer byte array.
     * @return the image buffer byte array
     */
    public byte[] getImageBuffer() {
        return this.imageBuffer;
    }
}

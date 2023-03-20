package src.domain;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Command implements Serializable {
    private String command;
    private String wine;
    private String imageName;

    private BufferedImage imageBuffer;

    private int winePrice;
    private int wineQuantity;
    private String wineSeller;
    private int wineStars;
    private String userReceiver;
    private String message;
    private String user;

    public void setCommand(String command){
        this.command = command;
    }

    public void setWine(String wine){
        this.wine = wine;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setWineQuantity(int wineQuantity){
        this.wineQuantity = wineQuantity;
    }

    public void setWinePrice(int winePrice) {
        this.winePrice = winePrice;
    }

    public void setWineSeller(String wineSeller) {
        this.wineSeller = wineSeller;
    }

    public void setWineStars(int wineStars) {
        this.wineStars = wineStars;
    }

    public void setUserReceiver(String userReceiver) {
        this.userReceiver = userReceiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setUser(String user) {
    	this.user = user;
    }

    public String getCommand() {
        return command;
    }

    public String getWine() {
        return wine;
    }

    public String getImageName() {
        return imageName;
    }

    public int getWinePrice() {
        return winePrice;
    }

    public int getWineQuantity() {
        return wineQuantity;
    }

    public String getWineSeller() {
        return wineSeller;
    }

    public int getWineStars() {
        return wineStars;
    }

    public String getUserReceiver() {
        return userReceiver;
    }

    public String getMessage() {
        return message;
    }
    
    public String getUser() {
    	return user;
    }

    public void setImageBuffer(BufferedImage buffer) {
        this.imageBuffer = buffer;
    }

    public BufferedImage getImageBuffer() {
        return this.imageBuffer;
    }
}

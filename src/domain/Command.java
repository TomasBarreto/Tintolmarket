package src.domain;

public class Command {
    private String command;
    private String wine;
    private String image;

    private float winePrice;
    private int wineQuantity;
    private String wineSeller;
    private int wineStars;
    private String userReceiver;
    private String message;

    public void setCommand(String command){
        this.command = command;
    }

    public void setWine(String wine){
        this.wine = wine;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setWineQuantity(int wineQuantity){
        this.wineQuantity = wineQuantity;
    }

    public void setWinePrice(float winePrice) {
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

    public String getCommand() {
        return command;
    }

    public String getWine() {
        return wine;
    }

    public String getImage() {
        return image;
    }

    public float getWinePrice() {
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
}

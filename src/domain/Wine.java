package src.domain;

public class Wine {
	private final String name;
    private final String url;
    private Rating medRating;
    private int quantity;
    public Wine(String name, String url){
        this.name = name;
        this.url = url;
        this.medRating = new Rating();
        this.quantity = 0;
    }
    public void updateClassification(float rating){
    	this.medRating.update(rating);
    }
    public void add(int value) {
    	this.quantity += value;
    }
    public void remove(int value) {
    	this.quantity -= value;
    }

    public int quantity() {
    	return this.quantity;
    }
    public String view() {
    	System.out.println("Wine " + this.name + ":");
    	System.out.println("	Associated Image: "+ this.url);
    	System.out.println("	Average Rating: "+ this.medRating.value());
    	if(quantity==0) {
    		System.out.println("	There are no units available for sale.");
    	}
    	else {
    		//utilizador
    	}
    }
}

package src.domain;

import src.domain.Rating;

public class Wine {
	
	private final String name;
    private final String imageUrl;
    private Rating averageRating;
    private int quantity;
    
    public Wine(String name, String url){
        this.name = name;
        this.imageUrl = url;
        this.averageRating = new Rating();
        this.quantity = 0;
    }
    
    public void updateClassification(float rating){
    	this.averageRating.update(rating);
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
    	System.out.println("	Associated Image: "+ this.imageUrl);
    	System.out.println("	Average Rating: "+ this.averageRating.value());
    	
    	if(quantity==0) {
    		System.out.println("	There are no units available for sale.");
    	}
    	else {
    		//utilizador
    	}
    }
}

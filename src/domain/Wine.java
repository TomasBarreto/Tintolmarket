package src.domain;

import src.domain.Rating;

public class Wine {
	
	private final String name;
    private final String imageUrl;
    private Rating averageRating;

    
    public Wine(String name, String url){
        this.name = name;
        this.imageUrl = url;
        this.averageRating = new Rating();

    }
    
    public void updateClassification(float rating){
    	this.averageRating.update(rating);
    }
    
 
    
    public String wineInfo() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Wine " + this.name + ":\n");
    	sb.append("	Associated Image: "+ this.imageUrl+"\n");
    	sb.append("	Average Rating: "+ this.averageRating.value()+"\n");
    	return sb.toString();
    }
}

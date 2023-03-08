package src.domain;

public class Rating {
    private int starsSum;
    private int counter;
    private float rating;
    
    public Rating() {
    	this.starsSum = 0;
    	this.counter = 0;
    	this.rating = 0;
    }
    
    public void update(int stars){
    	this.starsSum += stars;
        this.counter ++;
        this.rating = this.starsSum / this.counter;
    }
    
    public float getRating() {
    	return this.rating;
    }
}

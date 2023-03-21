package src.domain;

public class Rating {
    private float starsSum;

    private int counter;

    private float rating;

    public float getStarsSum() {
        return starsSum;
    }

    public void setStarsSum(float starsSum) {
        this.starsSum = starsSum;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    public Rating() {
    	this.starsSum = 0;
    	this.counter = 0;
    	this.rating = 0;
    }
    
    public void update(float stars){
    	this.starsSum += stars;
        this.counter ++;
        this.rating = this.starsSum / this.counter;
    }
    
    public float getRating() {
    	return this.rating;
    }
}

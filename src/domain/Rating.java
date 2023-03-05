package src.domain;

public class Rating {
    private float sum;
    private int counter;
    
    public Rating() {
    	this.sum = 0;
    	this.counter = 0;
    }
    
    public void update(float rating){
    	this.sum += rating;
        this.counter ++;
    }
    
    public double value() {
    	if (counter==0){
    		return -1;
    	}
    	else {
    		return (double)sum/counter;
    	}
    }
}

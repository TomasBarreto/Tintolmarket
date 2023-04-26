package src.domain;

/**
 * This class represents the rating given to a product by multiple users.
 */
public class Rating {
    private float starsSum;

    private int counter;

    private float rating;

    /**
     * Returns the sum of all stars given by the users.
     * @return the sum of all stars given by the users
     */
    public float getStarsSum() {
        return starsSum;
    }

    /**
     * Sets the sum of all stars given by the users.
     * @param starsSum the sum of all stars given by the users
     */
    public void setStarsSum(float starsSum) {
        this.starsSum = starsSum;
    }

    /**
     * Returns the number of users who have rated the product.
     * @return the number of users who have rated the product
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets the number of users who have rated the product.
     * @param counter the number of users who have rated the product
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * Constructs a new Rating object with initial values of starsSum = 0, counter = 0, and rating = 0.
     */
    public Rating() {
    	this.starsSum = 0;
    	this.counter = 0;
    	this.rating = 0;
    }

    /**
     * Updates the rating with the stars given by a user.
     * @param stars the number of stars given by the user
     */
    public void update(float stars){
    	this.starsSum += stars;
        this.counter ++;
        this.rating = this.starsSum / this.counter;
    }

    /**
     * Returns the rating of the wine.
     * @return the rating of the wine as a float value.
     */
    public float getRating() {
    	return this.rating;
    }
}

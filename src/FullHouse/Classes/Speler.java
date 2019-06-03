package FullHouse.Classes;

/**
 * Created by sjoer on 30-5-2019.
 */
public class Speler {
    private int rating;
    private int temp_rating;
    private int id;

    public Speler(int rating, int id){
        this.rating = rating;
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getTemp_rating() {
        return temp_rating;
    }

    public void setTemp_rating(int temp_rating) {
        this.temp_rating = temp_rating;
    }
}

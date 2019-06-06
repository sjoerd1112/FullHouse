package FullHouse.Classes;

import FullHouse.DB.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sjoer on 30-5-2019.
 */
public class Speler {
    private String naam;
    private int rating;
    private int temp_rating;
    private int id;

    public Speler(int rating, int id){
        this.rating = rating;
        this.id = id;
    }

    public Speler(int id) throws SQLException {
        String query = "SELECT naam, rating FROM Speler WHERE id="+id;
        ResultSet rs = DBConnector.query(query);
        if(rs.next()){
            this.naam = rs.getString("naam");
            this.rating = rs.getInt("rating");
            this.temp_rating = this.rating;
            this.id = id;
        }
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

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getId() {
        return id;
    }
}

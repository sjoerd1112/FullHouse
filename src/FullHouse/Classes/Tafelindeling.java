package FullHouse.Classes;

import FullHouse.DB.DBConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sjoer on 29-5-2019.
 */
public class Tafelindeling {
    private static ArrayList<Tafel> tafels = new ArrayList<>();
    private static ArrayList<Integer> IDs = new ArrayList<>();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ArrayList<Tafel> tafelArrayList = createTafels(10, "Pink Ribbon", 1, 1);
    }

    public static ArrayList<Tafel> createTafels(int aantalSpelers, String type, int toernooi_id, int ronde_nummer) throws SQLException, ClassNotFoundException {
        int aantalTafels;
        DBConnector.startConnection();
        if(type.equals("Normaal")) {
            if(aantalSpelers<9){
                aantalTafels = 1;
                tafels.add(new Tafel(aantalSpelers));
            }
            else {
                if (aantalSpelers % 10 == 0) {
                    aantalTafels = aantalSpelers / 10;
                    for (int i = 0; i < aantalTafels; i++) {
                        tafels.add(new Tafel(10));
                    }
                } else {
                    int restantSpelers = aantalSpelers % 9;
                    int inTeDelenSpelers = aantalSpelers - restantSpelers;
                    aantalTafels = inTeDelenSpelers / 9;
                    for (int i = 0; i < aantalTafels; i++) {
                        tafels.add(new Tafel(9));
                    }
                    for (int i = 0; i < restantSpelers; i++) {
                        tafels.get(i).setAantal(tafels.get(i).getAantal() + 1);
                    }
                }
            }
            addSpelersTest(aantalTafels, aantalSpelers, toernooi_id, ronde_nummer);
        }
        else{
            if(aantalSpelers<5){
                aantalTafels = 1;
                tafels.add(new Tafel(aantalSpelers));
            }
            else {
                if (aantalSpelers % 5 == 0) {
                    aantalTafels = aantalSpelers / 5;
                    for (int i = 0; i < aantalTafels; i++) {
                        tafels.add(new Tafel(5));
                    }
                } else {
                    int restantSpelers = aantalSpelers % 4;
                    int inTeDelenSpelers = aantalSpelers - restantSpelers;
                    aantalTafels = inTeDelenSpelers / 4;
                    for (int i = 0; i < aantalTafels; i++) {
                        tafels.add(new Tafel(4));
                    }
                    for (int i = 0; i < restantSpelers; i++) {
                        tafels.get(i).setAantal(tafels.get(i).getAantal() + 1);
                    }
                }
            }
            addSpelersTest(aantalTafels, aantalSpelers, toernooi_id, ronde_nummer);
        }
        for (int i = 0; i < aantalTafels; i++) {
            int spelersSize = tafels.get(i).getSpelers().size();
            System.out.println("Tafel " + (i + 1) + " met " + spelersSize + " spelers");
        }
        return tafels;
    }

    private static void addSpelers(int aantalTafels, int aantalSpelers){
        for (int i = 0; i < aantalTafels; i++) {
            Tafel tafel = tafels.get(i);
            for (int x = 0; x < tafel.getAantal(); x++) {
                int id = (int) Math.round(Math.random() * aantalSpelers);
                while (IDs.contains(id)) {
                    id = (int) Math.round(Math.random() * aantalSpelers);
                }
                int rating = 0;
                IDs.add(id);
                Speler speler = new Speler(rating, id);
                tafel.addSpeler(speler);
            }
        }
    }

    private static void addSpelersTest(int aantalTafels, int aantalSpelers, int toernooi_id, int ronde) throws SQLException, ClassNotFoundException {
        int aantal = 0;
        Random rand = new Random();
        for (int i = 0; i < aantalTafels; i++) {
            Tafel tafel = tafels.get(i);
            for (int x = 0; x < tafel.getAantal(); x++) {
                int id = rand.nextInt(aantalSpelers)+1;
                while (IDs.contains(id)) {
                    id = rand.nextInt(aantalSpelers)+1;
                }
                IDs.add(id);
                String query = "SELECT id FROM Speler JOIN toernooi_inschrijving ON Speler.id = toernooi_inschrijving.speler WHERE toernooi=" + toernooi_id;
                ResultSet rs = DBConnector.query(query);
                if (rs.absolute(id)) {
                    Speler speler = new Speler(rs.getInt("id"));
                    tafel.addSpeler(speler);
                    aantal++;
                }
            }
        }
        for(int i = 0;i<tafels.size();i++){
            Tafel tafel = tafels.get(i);
            for(int x = 0;x<tafel.getAantal();x++){
                Speler speler = tafel.getSpelers().get(x);
                int id = speler.getId();
                String query = "INSERT INTO Tafelindeling(tafelNummer, spelers, resultaat, ronde, winnaar) VALUES ("+i+", "+id+", null, "+ronde+", NULL )";
                DBConnector.executeQuery(query);
            }
        }
    }
}

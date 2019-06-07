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

    public static ArrayList<Tafel> createTafels(int aantalSpelers, String type, int toernooi_id, int ronde_nummer) throws SQLException, ClassNotFoundException {
        int aantalTafels;
        if(type.equals("Regulier")) {
            if(aantalSpelers<9){
                aantalTafels = 1;
                tafels.add(new Tafel(aantalSpelers));
            }
            else {
                if (aantalSpelers < 18) {
                    aantalTafels = 2;
                    if(aantalSpelers%2==0){
                        tafels.add(new Tafel(aantalSpelers/2));
                        tafels.add(new Tafel(aantalSpelers/2));
                    }else{
                        int aantal1 = (aantalSpelers-1)/2;
                        int aantal2 = (aantalSpelers+1)/2;
                        tafels.add(new Tafel(aantal1));
                        tafels.add(new Tafel(aantal2));
                    }
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
            }
            addSpelers(aantalTafels, aantalSpelers, toernooi_id, ronde_nummer);
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
            addSpelers(aantalTafels, aantalSpelers, toernooi_id, ronde_nummer);
        }
        for (int i = 0; i < aantalTafels; i++) {
            int spelersSize = tafels.get(i).getSpelers().size();
            System.out.println("Tafel " + (i + 1) + " met " + spelersSize + " spelers");
        }
        return tafels;
    }

    private static void addSpelers(int aantalTafels, int aantalSpelers, int toernooi_id, int ronde) throws SQLException, ClassNotFoundException {
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
                ResultSet rs;
                if(ronde==1) {
                    String query = "SELECT id FROM Speler JOIN toernooi_inschrijving ON Speler.id = toernooi_inschrijving.speler WHERE toernooi_inschrijving.toernooi=" + toernooi_id;
                    rs = DBConnector.query(query);
                } else{
                    String query = "SELECT speler as id FROM Tafelindeling WHERE resultaat='W' AND toernooi="+toernooi_id+" AND ronde="+(ronde-1);
                    rs = DBConnector.query(query);
                }
                System.out.println("hier");
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
                String query = "INSERT INTO Tafelindeling(tafelNummer, speler, resultaat, ronde, toernooi) VALUES ("+i+", "+id+", null, "+ronde+", "+toernooi_id+")";
                DBConnector.executeQuery(query);
            }
        }
    }
}

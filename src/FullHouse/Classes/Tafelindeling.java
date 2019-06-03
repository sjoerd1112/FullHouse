package FullHouse.Classes;

import java.util.ArrayList;

/**
 * Created by sjoer on 29-5-2019.
 */
public class Tafelindeling {
    private static ArrayList<Tafel> tafels = new ArrayList<>();
    private static ArrayList<Integer> IDs = new ArrayList<>();

    public static void main(String[] args) {
        ArrayList<Tafel> tafelArrayList = aantalPerTafel(111);
    }

    public static ArrayList<Tafel> aantalPerTafel(int aantalSpelers) {
        int aantalTafels;
        if(aantalSpelers%10==0){
            aantalTafels = aantalSpelers/10;
            for(int i = 0;i<aantalTafels;i++){
                tafels.add(new Tafel(10));
            }
        }
        else{
            int restantSpelers = aantalSpelers%9;
            int inTeDelenSpelers = aantalSpelers-restantSpelers;
            aantalTafels = inTeDelenSpelers/9;
            for(int i = 0;i<aantalTafels;i++){
                tafels.add(new Tafel(9));
            }
            for(int i = 0;i<restantSpelers;i++){
                tafels.get(i).setAantal(tafels.get(i).getAantal()+1);
            }
        }

        for(int i = 0;i<aantalTafels;i++){
            Tafel tafel = tafels.get(i);
            for(int x = 0;x<tafel.getAantal();x++) {
                int id = (int) Math.round(Math.random()*aantalSpelers);
                while (IDs.contains(id)) {
                    id = (int) Math.round(Math.random()*aantalSpelers);
                }
                int rating = 0;
                IDs.add(id);
                Speler speler = new Speler(rating, id);
                tafel.addSpeler(speler);
            }
        }

        for(int i = 0;i<aantalTafels;i++){
            int spelersSize = tafels.get(i).getSpelers().size();
            System.out.println("Tafel " + (i+1) + " met " + spelersSize + " spelers");
        }

        return tafels;
    }
}

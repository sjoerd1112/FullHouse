package FullHouse.Classes;

import java.util.ArrayList;

/**
 * Created by sjoer on 29-5-2019.
 */
public class Tafel {
    private int aantal;
    private ArrayList<Speler> spelers = new ArrayList<>();

    public Tafel(int aantal){
        this.aantal = aantal;
    }

    public int getAantal() {
        return aantal;
    }

    public void setAantal(int aantal) {
        this.aantal = aantal;
    }

    public void addSpeler(Speler speler){
        spelers.add(speler);
    }

    public ArrayList<Speler> getSpelers(){
        return spelers;
    }
}

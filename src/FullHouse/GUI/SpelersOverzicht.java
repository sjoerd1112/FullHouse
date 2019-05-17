package FullHouse.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sjoer on 13-5-2019.
 */
public class SpelersOverzicht {
    private static JFrame frame = new JFrame("Spelers");
    private static JPanel spelersPanel = new JPanel(new GridLayout(5,5, 20, 0));

    public static void showSpelers(){

    }

    public static void addLabel(int aantal){
        for(int i = 0;i<aantal;i++){
            spelersPanel.add(new JLabel());
        }
    }
}

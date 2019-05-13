package FullHouse.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sjoer on 13-5-2019.
 */
public class Home {
    private static JPanel homePanel = new JPanel(new GridLayout(3,4,25,0));
    public static void showHome(JFrame frame, JPanel panel){
        frame.remove(panel);
        frame.setTitle("Home");
        JButton spelers = new JButton("Spelers");
        JButton toernooien = new JButton("Toernooien");
        JButton masterclasses = new JButton("Masterclasses");
        JButton statistieken = new JButton("Statistieken");
        addLabel(4);
        homePanel.add(spelers);
        homePanel.add(toernooien);
        homePanel.add(masterclasses);
        homePanel.add(statistieken);
        addLabel(4);
        frame.add(homePanel);
        frame.pack();
        frame.setSize(800,250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void addLabel(int aantal){
        for(int i = 0;i<aantal;i++){
            homePanel.add(new JLabel());
        }
    }
}

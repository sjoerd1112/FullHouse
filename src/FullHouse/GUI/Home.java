package FullHouse.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by sjoer on 13-5-2019.
 */
public class Home {
    private static JPanel homePanel = new JPanel(new GridLayout(3,4,25,10));
    private static boolean created = false;
    public static void showHome(JFrame frame, JPanel panel){
        if(!created) {
            frame.remove(panel);
            frame.setTitle("Home");
            JButton spelers = new JButton("Spelers");
            JButton toernooien = new JButton("Toernooien");
            JButton masterclasses = new JButton("Masterclasses");
            JButton medewerkers = new JButton("Medewerkers");
            JButton toevoegen = new JButton("Gebruiker toevoegen");
            JButton uitloggen = new JButton("Log uit");
            homePanel.add(toevoegen);
            addLabel(2);
            homePanel.add(uitloggen);
            homePanel.add(spelers);
            homePanel.add(toernooien);
            homePanel.add(masterclasses);
            homePanel.add(medewerkers);
            addLabel(4);
            frame.add(homePanel);
            frame.pack();
            frame.setSize(800, 250);

            spelers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT naam, id FROM Speler";
                        Spelers.showSpelers(frame, homePanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            toernooien.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT toernooi_id FROM Toernooi";
                        Toernooi.showToernooien(frame, homePanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            masterclasses.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT code, datum FROM masterClass";
                        Masterclasses.showMasterclasses(frame, homePanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            medewerkers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Medewerkers");
                }
            });

            toevoegen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addGebruiker.showToevoegen(frame, homePanel);
                }
            });

            uitloggen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Login.showLogin(frame, homePanel);
                }
            });

            created = true;
        }
        else{
            frame.setTitle("Home");
            frame.remove(panel);
            frame.add(homePanel);
            frame.pack();
            frame.setSize(800,250);
        }
    }

    public static void showHome(JFrame frame){
        frame.setTitle("Home");
        frame.add(homePanel);
        frame.pack();
        frame.setSize(800,250);
    }

    public static void addLabel(int aantal){
        for(int i = 0;i<aantal;i++){
            homePanel.add(new JLabel());
        }
    }
}

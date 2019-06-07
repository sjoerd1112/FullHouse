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
    private static JPanel homePanel;
    private static JFrame frame;

    public static void showHome(){
            if (frame != null) {
                frame.dispose();
            }

            homePanel = new JPanel(new GridLayout(3,4,25,10));
            frame = new JFrame("Home");

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
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(800, 250);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            spelers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT naam, id FROM Speler";
                        Spelers.clearSearchBar();
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
                        String query = "SELECT * FROM Toernooi";
                        Toernooi.clearSearchBar();
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
                    addGebruiker.clearFields();
                    addGebruiker.showToevoegen(frame, homePanel);
                }
            });

            uitloggen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        frame.dispose();
                        Login.showLogin();
                    } catch (SQLException | ClassNotFoundException e2) {
                        e2.printStackTrace();
                    }
                }
            });
    }

    public static void addLabel(int aantal){
        for(int i = 0;i<aantal;i++){
            homePanel.add(new JLabel());
        }
    }
}

package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static FullHouse.DB.DBConnector.query;

/**
 * Created by sjoer on 20-5-2019.
 */
public class Toernooien{

    private static JPanel toernooiPanel = new JPanel(new GridLayout(6, 5, 5, 5));

    private static JLabel toernooi_id = new JLabel();
    private static JLabel datum = new JLabel();
    private static JLabel beginTijd = new JLabel();
    private static JLabel eindTijd = new JLabel();
    private static JLabel beschrijving = new JLabel();
    private static JLabel condities= new JLabel();
    private static JLabel max_aantallen = new JLabel();
    private static JLabel inleggeld = new JLabel();
    private static JLabel max_inschrijf_datum = new JLabel();
    private static JLabel locatie = new JLabel();

    public static void showToernooi(JFrame frame, int id) throws SQLException, ClassNotFoundException {
            toernooiPanel.removeAll();
            frame.setTitle("Toernooien overzicht");
            JButton terug = new JButton("Terug");
            JButton wijzigen = new JButton("Wijzigen");
            JButton verwijderen = new JButton("Verwijderen");
            JButton spelers = new JButton("Spelers");

            addComponent(terug,
                    spelers,
                    new JLabel(),
                    wijzigen,
                    verwijderen,
                    new JLabel("Toernooi id: "),
                    toernooi_id,
                    new JLabel(),
                    new JLabel("Datum: "),
                    datum,
                    new JLabel("Begintijd: "),
                    beginTijd,
                    new JLabel(),
                    new JLabel("Eindtijd: "),
                    eindTijd,
                    new JLabel("Beschrijving: "),
                    beschrijving,
                    new JLabel(),
                    new JLabel("Condities:"),
                    condities,
                    new JLabel("Max aantallen:"),
                    max_aantallen,
                    new JLabel(),
                    new JLabel("Inleggeld:"),
                    inleggeld,
                    new JLabel("Max inschrijfdatum: "),
                    max_inschrijf_datum,
                    new JLabel(),
                    new JLabel("Locatie: "),
                    locatie);

            String query = "SELECT * FROM Toernooi WHERE toernooi_id=" + id;
            setText(query);

            frame.add(toernooiPanel);
            frame.pack();
            frame.setSize(800, 250);

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT toernooi_id FROM Toernooi";
                        Toernooi.showToernooien(frame, toernooiPanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            spelers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT * FROM Speler s INNER JOIN toernooi_inschrijving TI on s.id = TI.speler WHERE TI.toernooi = " + id + " AND s.id IN (SELECT speler FROM toernooi_inschrijving)";
                        Toernooi.clearSearchBar();
                        Toernooi.showIngeschrevenSpelers(frame, toernooiPanel, id, query);
                    } catch (SQLException e2) {
                        e2.printStackTrace();
                    }
                }
            });

            wijzigen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        wijzigToernooi.showWijzigToernooi(frame, toernooiPanel, id);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            verwijderen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        verwijderToernooi(id, frame);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });
    }

    public static void verwijderToernooi(int id, JFrame frame) throws SQLException, ClassNotFoundException {
        if(JOptionPane.showConfirmDialog(null, "Wilt u deze toernooi verwijderen", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            String query = "DELETE FROM Toernooi where toernooi_id = "+id;
            DBConnector.executeQuery(query);
            query = "SELECT toernooi_id FROM Toernooi";
            Toernooi.showToernooien(frame, toernooiPanel, query);
        }
    }

    public static void setText(String query) throws SQLException {
            ResultSet rs = query(query);
            while (rs.next()) {
            toernooi_id.setText(rs.getString("toernooi_id"));
            Date date = rs.getDate("datum");
            String inschrijf_datum = new SimpleDateFormat("dd-MM-yyyy").format(date);
            datum.setText(inschrijf_datum);
            beginTijd.setText(rs.getString("begintijd"));
            eindTijd.setText(rs.getString("eindtijd"));
            beschrijving.setText(rs.getString("beschrijving"));
            condities.setText(rs.getString("condities"));
            max_aantallen.setText(rs.getString("max_aantallen"));
            inleggeld.setText("" + rs.getString("inleggeld") + " euro");
            Date max_date = rs.getDate("Max_inschrijfdatum");
            Date time = rs.getTime("Max_inschrijfdatum");
            String convertDate = new SimpleDateFormat("dd-MM-yyyy").format(max_date);
            String convertTime = new SimpleDateFormat("HH:mm:ss").format(time);
            max_inschrijf_datum.setText(convertDate + " " + convertTime);
            locatie.setText(rs.getString("locatie"));
        }
    }

    private static void addComponent(Component... com) {
        for (Component components : com) {
            toernooiPanel.add(components);
        }
    }
}


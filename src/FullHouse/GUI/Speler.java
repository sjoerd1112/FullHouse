package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static FullHouse.DB.DBConnector.query;

/**
 * Created by sjoer on 20-5-2019.
 */
public class Speler{

    private static JPanel spelerPanel = new JPanel(new GridLayout(5, 5, 5, 5));

    private static JLabel naam = new JLabel();
    private static JLabel adres = new JLabel();
    private static JLabel woonplaats = new JLabel();
    private static JLabel telefoonnummer = new JLabel();
    private static JLabel email = new JLabel();
    private static JLabel gbdatum = new JLabel();
    private static JLabel geslacht = new JLabel();
    private static JLabel rating = new JLabel();
    private static JLabel type = new JLabel();

    public static void showSpeler(JFrame frame, int id) throws SQLException, ClassNotFoundException {
            spelerPanel.removeAll();
            frame.setTitle("Speler");

            JButton terug = new JButton("Terug");
            JButton wijzigen = new JButton("Wijzigen");
            JButton verwijderen = new JButton("Verwijderen");
            JButton inschrijven = new JButton("<html>Inschrijven<br>Uitschrijven</html>");

            addComponent(terug,
                         inschrijven,
                         type,
                         wijzigen,
                         verwijderen,
                         new JLabel("Naam: "),
                         naam,
                         new JLabel(),
                         new JLabel("Adres: "),
                         adres,
                         new JLabel("Woonplaats: "),
                         woonplaats,
                         new JLabel(),
                         new JLabel("Telefoonnummer: "),
                         telefoonnummer,
                         new JLabel("E-mail: "),
                         email,
                         new JLabel(),
                         new JLabel("Geboortedatum: "),
                         gbdatum,
                         new JLabel("Geslacht: "),
                         geslacht,
                         new JLabel(),
                         new JLabel("Rating: "),
                         rating);

            String query = "SELECT * FROM Speler WHERE id=" + id;
            setText(query);

            frame.add(spelerPanel);
            frame.pack();
            frame.setSize(800, 250);

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT naam, id FROM Speler";
                        Spelers.showSpelers(frame, spelerPanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            wijzigen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        wijzigSpeler.showWijzigSpeler(frame, spelerPanel, id);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            verwijderen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        verwijderSpeler(id, frame);
                    } catch (SQLException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            inschrijven.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Inschrijven.showInschrijven(frame, spelerPanel, id);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
    }

    private static void addComponent(Component... com) {
        for (Component components : com) {
            spelerPanel.add(components);
        }
    }

    public static void verwijderSpeler(int id, JFrame frame) throws SQLException, ClassNotFoundException {
        if(JOptionPane.showConfirmDialog(null, "Wilt u deze speler verwijderen", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            String query = "delete from Speler where id = "+id;
            DBConnector.executeQuery(query);
            query = "SELECT naam, id FROM Speler";
            Spelers.showSpelers(frame, spelerPanel, query);
        }
    }

    public static void setText(String query) throws SQLException {
        ResultSet rs = query(query);
        while (rs.next()) {
            naam.setText(rs.getString("naam"));
            adres.setText(rs.getString("adres"));
            woonplaats.setText(rs.getString("woonplaats"));
            telefoonnummer.setText("0"+rs.getString("telefoonnummer"));
            email.setText(rs.getString("email"));
            Date gbDate = rs.getDate("geboortedatum");
            String datum = new SimpleDateFormat("dd-MM-yyyy").format(gbDate);
            gbdatum.setText(datum);
            String bepaalGeslacht = rs.getString("geslacht");
            if (bepaalGeslacht.equals("M")) {
                geslacht.setText("Man");
            } else {
                geslacht.setText("Vrouw");
            }
            rating.setText(rs.getString("rating"));
            if(rs.getInt("bekende_speler")==1){
                type.setText("Bekende speler");
            }
            else{
                type.setText("Speler");
            }
        }
    }
}


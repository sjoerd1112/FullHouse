package FullHouse.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import static FullHouse.DB.DBConnector.query;

/**
 * Created by sjoer on 20-5-2019.
 */
public class Speler{
    private static boolean created = false;
    private static JPanel spelerPanel = new JPanel(new GridLayout(5, 5, 5, 5));
    private static JLabel naam;
    private static JLabel adres;
    private static JLabel woonplaats;
    private static JLabel telefoonnummer;
    private static JLabel email;
    private static JLabel gbdatum;
    private static JLabel geslacht;
    private static JLabel rating;

    public static void showSpeler(JFrame frame, int id) throws SQLException, ClassNotFoundException {
        if(!created) {
            frame.setTitle("Speler");
            JButton terug = new JButton("Terug");
            spelerPanel.add(terug);
            spelerPanel.add(new JLabel());
            spelerPanel.add(new JLabel());
            JButton wijzigen = new JButton("Wijzigen");
            JButton verwijderen = new JButton("Verwijderen");
            spelerPanel.add(wijzigen);
            spelerPanel.add(verwijderen);
            naam = new JLabel("naam");
            adres = new JLabel("adres");
            woonplaats = new JLabel("woonplaats");
            telefoonnummer = new JLabel("telefoonnummer");
            email = new JLabel("email");
            gbdatum = new JLabel("gbdatum");
            geslacht = new JLabel("geslacht");
            rating = new JLabel("rating");
            spelerPanel.add(new JLabel("Naam: "));
            spelerPanel.add(naam);
            spelerPanel.add(new JLabel());
            spelerPanel.add(new JLabel("Adres: "));
            spelerPanel.add(adres);
            spelerPanel.add(new JLabel("Woonplaats: "));
            spelerPanel.add(woonplaats);
            spelerPanel.add(new JLabel());
            spelerPanel.add(new JLabel("Telefoonnummer: "));
            spelerPanel.add(telefoonnummer);
            spelerPanel.add(new JLabel("E-mail: "));
            spelerPanel.add(email);
            spelerPanel.add(new JLabel());
            spelerPanel.add(new JLabel("Geboortedatum: "));
            spelerPanel.add(gbdatum);
            spelerPanel.add(new JLabel("Geslacht: "));
            spelerPanel.add(geslacht);
            spelerPanel.add(new JLabel());
            spelerPanel.add(new JLabel("Rating: "));
            spelerPanel.add(rating);

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
                        Spelers.showSpelers(frame, spelerPanel,query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        else{
            frame.setTitle("Speler");
            spelerPanel.removeAll();
            created = false;
            showSpeler(frame, id);
        }
        created = true;
    }


    public static void setText(String query) throws SQLException {
        ResultSet rs = query(query);
        while (rs.next()) {
            naam.setText(rs.getString("naam"));
            adres.setText(rs.getString("adres"));
            woonplaats.setText(rs.getString("woonplaats"));
            telefoonnummer.setText(rs.getString("telefoonnummer"));
            email.setText(rs.getString("email"));
            gbdatum.setText(rs.getString("geboortedatum"));
            String bepaalGeslacht = rs.getString("geslacht");
            if (bepaalGeslacht.equals("M")) {
                geslacht.setText("Man");
            } else {
                geslacht.setText("Vrouw");
            }
            rating.setText(rs.getString("rating"));
        }

    }
}


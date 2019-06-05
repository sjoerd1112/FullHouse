package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static FullHouse.Classes.checkDatum.checkDateFormat;

/**
 * Created by sjoer on 22-5-2019.
 */

//JCalendar input


public class addSpeler {

    private static JPanel addSpelerPanel = new JPanel(new GridLayout(5, 5, 5, 5));
    private static ArrayList<Boolean> checkValue = new ArrayList<>();
    private static int telnummer = 0;
    private static String gbdate;

    private static JTextField naam = new JTextField();
    private static JTextField adres = new JTextField();
    private static JTextField woonplaats = new JTextField();
    private static JTextField telefoonnummer = new JTextField();
    private static JTextField email = new JTextField();
    private static JTextField gbdatum = new JTextField();
    private static JTextField geslacht = new JTextField();
    private static JLabel melding = new JLabel();
    private static JLabel melding2 = new JLabel();

    public static void showAddSpeler(JFrame frame, JPanel panel) {
        addSpelerPanel.removeAll();
        frame.setTitle("Speler toevoegen");
        frame.remove(panel);

        JButton toevoegen = new JButton("Toevoegen");
        JButton terug = new JButton("Terug");
        String[] types = new String[]{"<html>Gecontracteerd</html>", "Normale speler"};
        JComboBox<String> type = new JComboBox<>(types);

        addComponent(terug,
                new JLabel(),
                melding,
                new JLabel(),
                toevoegen,
                new JLabel("Naam: "),
                naam,
                melding2,
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
                new JLabel("<html>Geboortedatum:<br>(dd-mm-jjjj)</html>"),
                gbdatum,
                new JLabel("<html>Geslacht:<br>(M/V)</html>"),
                geslacht,
                new JLabel(),
                new JLabel("Type speler: "),
                type
        );

        clearText(naam);
        clearText(adres);
        clearText(woonplaats);
        clearText(email);
        clearText(telefoonnummer);
        clearText(gbdatum);
        clearText(geslacht);

        frame.add(addSpelerPanel);
        frame.pack();
        frame.setSize(800, 250);

        toevoegen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                check(naam);
                check(adres);
                check(woonplaats);
                check(email);
                check(telefoonnummer);
                check(gbdatum);
                check(geslacht);

                if (!checkValue.contains(false)) {
                    int bekend;
                    if (type.getSelectedItem().equals("Speler")) {
                        bekend = 0;
                    } else {
                        bekend = 1;
                    }

                    String query = "insert into Speler(naam, adres, woonplaats, telefoonnummer, email, geboortedatum, geslacht, bekende_speler) VALUES ('" + naam.getText() + "','" + adres.getText() + "','" + woonplaats.getText() + "'," + telnummer + ",'" + email.getText() + "','" + gbdate + "','" + geslacht.getText() + "'," + bekend + ")";
                    try {
                        DBConnector.executeQuery(query);
                        query = "SELECT id FROM Speler WHERE id = (SELECT max(id) FROM Speler)";
                        ResultSet rs = DBConnector.query(query);
                        if (rs.next()) {
                            int id = rs.getInt("id");
                            frame.remove(addSpelerPanel);
                            Speler.showSpeler(frame, id);
                        }
                        checkValue.clear();
                    } catch (SQLException | ClassNotFoundException e2) {
                        e2.printStackTrace();
                    }
                } else {
                    checkValue.clear();
                }
            }
        });

        terug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "SELECT naam, id FROM Speler";
                    Spelers.showSpelers(frame, addSpelerPanel, query);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void check(JTextField field) {
        boolean result;
        if (field.getText().equals("")) {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
            result = false;
        } else {
            field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            result = true;
        }
        if (field.equals(telefoonnummer)) {
            if (telefoonnummer.getText().length() == 10) {
                melding2.setText("");
                telnummer = Integer.parseInt("0" + telefoonnummer.getText());
                result = true;
            } else {
                melding2.setText("incorrect telefoonnummer");
                telefoonnummer.setBorder(BorderFactory.createLineBorder(Color.RED));
                result = false;
            }
        }
        if (field.equals(gbdatum)) {
            Date parseDatum;
            try {
                parseDatum = new SimpleDateFormat("dd-MM-yyyy").parse(field.getText());
                gbdate = new SimpleDateFormat("yyyy-MM-dd").format(parseDatum);

                gbdatum.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                result = true;
            } catch (ParseException e) {
                gbdatum.setBorder(BorderFactory.createLineBorder(Color.RED));
                result = false;
            }
        }
        if (field.equals(geslacht)) {
            if (geslacht.getText().equals("M") || geslacht.getText().equals("V")) {
                geslacht.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                result = true;
            } else {
                geslacht.setBorder(BorderFactory.createLineBorder(Color.RED));
                result = false;
            }
        }
        checkValue.add(result);
    }

    private static void addComponent(Component... com) {
        for (Component components : com) {
            addSpelerPanel.add(components);
        }
    }

    public static void clearText(JTextField field) {
        field.setText("");
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public static ArrayList<Boolean> getCheckValue() {
        return checkValue;
    }

    public static String getGbdate() {
        return gbdate;
    }

    public static int getTelnummer() {
        return telnummer;
    }

    public static JTextField getTelefoonnummer() {
        return telefoonnummer;
    }

    public static JTextField getGbdatum() {
        return gbdatum;
    }

    public static JTextField getGeslacht() {
        return geslacht;
    }
}

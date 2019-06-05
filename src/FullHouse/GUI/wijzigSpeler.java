package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import static FullHouse.Classes.checkDatum.checkDateFormat;

/**
 * Created by sjoer on 24-5-2019.
 */
public class wijzigSpeler {
    private static JTextField naam = new JTextField();
    private static JTextField adres = new JTextField();
    private static JTextField woonplaats = new JTextField();
    private static JTextField telefoonnummer = addSpeler.getTelefoonnummer();
    private static JTextField email = new JTextField();
    private static JTextField gbdatum = addSpeler.getGbdatum();
    private static JTextField geslacht = addSpeler.getGeslacht();
    private static JTextField rating = new JTextField();
    private static JComboBox<String> type;

    private static JLabel melding = new JLabel();
    private static JLabel melding2 = new JLabel();

    private static JPanel spelerPanel = new JPanel(new GridLayout(5,5,5,5));

    public static void showWijzigSpeler(JFrame frame, JPanel panel, int id) throws SQLException {

        spelerPanel.removeAll();
        frame.remove(panel);
        frame.setTitle("Speler wijzigen");

        String[] types = new String[]{"<html>Gecontracteerd</html>","Normale speler"};
        type = new JComboBox<>(types);

        JButton terug = new JButton("Terug");
        JButton wijzig = new JButton("Wijzigen");

        addComponent(terug,
                new JLabel(),
                melding,
                new JLabel(),
                wijzig,
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
                new JLabel("Type speler:"),
                type);

        addSpeler.clearText(naam);
        addSpeler.clearText(adres);
        addSpeler.clearText(woonplaats);
        addSpeler.clearText(email);
        addSpeler.clearText(telefoonnummer);
        addSpeler.clearText(gbdatum);
        addSpeler.clearText(geslacht);

        setText(id);

        frame.add(spelerPanel);
        frame.pack();
        frame.setSize(800, 250);

        wijzig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addSpeler.check(naam);
                addSpeler.check(adres);
                addSpeler.check(woonplaats);
                addSpeler.check(email);
                addSpeler.check(telefoonnummer);
                addSpeler.check(gbdatum);
                addSpeler.check(geslacht);

                if (!addSpeler.getCheckValue().contains(false)) {
                    int bekend;
                    if(type.getSelectedItem().equals("Normale speler")){
                        bekend = 0;
                    }
                    else{
                        bekend = 1;
                    }
                    System.out.println("Datum: "+addSpeler.getGbdate());
                    String query = "UPDATE Speler SET naam='"+naam.getText()+"', adres='"+adres.getText()+"',woonplaats='"+woonplaats.getText()+"',telefoonnummer="+addSpeler.getTelnummer()+",email='"+email.getText()+"',geboortedatum='"+addSpeler.getGbdate()+"',geslacht='"+geslacht.getText()+"',rating="+rating.getText()+", bekende_speler="+bekend+" WHERE id="+id;
                    try {
                        DBConnector.executeQuery(query);
                        addSpeler.getCheckValue().clear();
                        frame.remove(spelerPanel);
                        Speler.showSpeler(frame, id);
                    } catch (SQLException | ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Vul alle velden correct in a.u.b.", "Er is iets misgegaan!", JOptionPane.ERROR_MESSAGE);
                    addSpeler.getCheckValue().clear();
                }
            }
        });

        terug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(spelerPanel);
                try {
                    Speler.showSpeler(frame, id);
                } catch (SQLException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void setText(int id) throws SQLException {
        String query = "SELECT * FROM Speler WHERE id="+id;
        ResultSet rs = DBConnector.query(query);
        while(rs.next()){
            naam.setText(rs.getString("naam"));
            adres.setText(rs.getString("adres"));
            woonplaats.setText(rs.getString("woonplaats"));
            telefoonnummer.setText("0"+rs.getString("telefoonnummer"));
            email.setText(rs.getString("email"));
            Date gbDate = rs.getDate("geboortedatum");
            String datum = new SimpleDateFormat("dd-MM-yyyy").format(gbDate);
            gbdatum.setText(datum);
            geslacht.setText(rs.getString("geslacht"));
            rating.setText(rs.getString("rating"));
            if(rs.getInt("bekende_speler")==1){
                type.setSelectedItem("<html>Gecontracteerd</html>");
            }
            else{
                type.setSelectedItem("Normale speler");
            }
        }
    }

    private static void addComponent(Component... com) {
        for (Component components : com) {
            spelerPanel.add(components);
        }
    }
}

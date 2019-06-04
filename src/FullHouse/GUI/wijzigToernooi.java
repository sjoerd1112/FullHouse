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
import java.util.Collections;
import java.util.Date;
import static FullHouse.Classes.checkDatum.checkDateFormat;

/**
 * Created by sjoer on 24-5-2019.
 */
public class wijzigToernooi {

    private static JTextField toernooi_id = addToernooi.getToernooi_id();
    private static JTextField datum = addToernooi.getDatum();
    private static JTextField beginTijd = addToernooi.getBeginTijd();
    private static JTextField eindTijd = addToernooi.getEindTijd();
    private static JTextField beschrijving = addToernooi.getBeschrijving();
    private static JTextField condities = addToernooi.getCondities();
    private static JTextField max_aantallen = addToernooi.getMax_aantallen();
    private static JTextField inleggeld = addToernooi.getInleggeld();
    private static JTextField max_inschrijf_datum = addToernooi.getMax_inschrijf_datum();

    private static JPanel toernooiPanel;

    public static void showWijzigToernooi(JFrame frame, JPanel panel, int id) throws SQLException, ClassNotFoundException {
        frame.remove(panel);
        frame.setTitle("Toernooi wijzigen");

        toernooiPanel = new JPanel(new GridLayout(6,5,5,5));
        JButton terug = new JButton("Terug");
        JButton wijzig = new JButton("Wijzigen");
        JLabel melding = new JLabel();

        //add components to panel
        addComponent(terug,
                new JLabel(),
                melding,
                new JLabel(),
                wijzig,
                new JLabel("Toernooi id: "),
                toernooi_id,
                new JLabel(),
                new JLabel("<html>Datum:<br>(dd-mm-jjjj)</html>"),
                datum,
                new JLabel("<html>Begintijd:<br>(hh:mm:ss)</html>"),
                beginTijd,
                new JLabel(),
                new JLabel("<html>Eindtijd:<br>(hh:mm:ss)</html>"),
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
                new JLabel("<html>Max inschrijfdatum:<br>(dd-mm-jjjj hh:mm:ss)</html>"),
                max_inschrijf_datum);

        addToernooi.clear(toernooi_id);
        addToernooi.clear(datum);
        addToernooi.clear(beginTijd);
        addToernooi.clear(eindTijd);
        addToernooi.clear(beschrijving);
        addToernooi.clear(condities);
        addToernooi.clear(max_aantallen);
        addToernooi.clear(inleggeld);
        addToernooi.clear(max_inschrijf_datum);

        setText(id);
        frame.add(toernooiPanel);
        frame.pack();
        frame.setSize(800, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        wijzig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToernooi.check(toernooi_id);
                addToernooi.check(datum);
                addToernooi.check(beginTijd);
                addToernooi.check(eindTijd);
                addToernooi.check(beschrijving);
                addToernooi.check(condities);
                addToernooi.check(max_aantallen);
                addToernooi.check(inleggeld);
                addToernooi.check(max_inschrijf_datum);

                if (!addToernooi.getCheckValues().contains(false)) {
                    melding.setText("");
                    String query = "UPDATE Toernooi SET toernooi_id='"+toernooi_id.getText()+"', datum='"+addToernooi.getFormatDatum()+"',begintijd='"+addToernooi.getBeginTime()+"',eindtijd='"+addToernooi.getEndTime()+"',beschrijving='"+beschrijving.getText()+"',condities='"+condities.getText()+"',max_aantallen='"+addToernooi.getAantallen()+"',inleggeld='"+addToernooi.getGeld()+"',max_inschrijfdatum='"+addToernooi.getMax_datum()+"' WHERE toernooi_id="+id;
                    try {
                        DBConnector.executeQuery(query);
                        frame.remove(toernooiPanel);
                        Toernooien.showToernooi(frame, Integer.parseInt(toernooi_id.getText()));
                        addToernooi.removeCheckValues();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Vul alle velden correct in a.u.b.", "Er is iets misgegaan!", JOptionPane.ERROR_MESSAGE);
                    addToernooi.removeCheckValues();
                }
            }
        });

        terug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(toernooiPanel);
                try {
                    Toernooien.showToernooi(frame, id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public static void setText(int id) throws SQLException {
        String query = "SELECT * FROM Toernooi WHERE toernooi_id="+id;
        ResultSet rs = DBConnector.query(query);
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
            inleggeld.setText("" + rs.getString("inleggeld"));
            Date max_date = rs.getDate("Max_inschrijfdatum");
            Date time = rs.getTime("Max_inschrijfdatum");
            String convertDate = new SimpleDateFormat("dd-MM-yyyy").format(max_date);
            String convertTime = new SimpleDateFormat("HH:mm:ss").format(time);
            max_inschrijf_datum.setText(convertDate + " " + convertTime);
        }
    }

    private static void addComponent(Component... com) {
        for (Component components : com) {
            toernooiPanel.add(components);
        }
    }
}


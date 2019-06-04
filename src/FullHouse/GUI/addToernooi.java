package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class addToernooi{

    private static JPanel addToernooiPanel = new JPanel(new GridLayout(6, 5, 5, 5));

    private static JTextField toernooi_id = new JTextField();
    private static JTextField datum = new JTextField();
    private static JTextField beginTijd = new JTextField();
    private static JTextField eindTijd = new JTextField();
    private static JTextField beschrijving = new JTextField();
    private static JTextField condities= new JTextField();
    private static JTextField max_aantallen = new JTextField();
    private static JTextField inleggeld = new JTextField();
    private static JTextField max_inschrijf_datum = new JTextField();

    private static ArrayList<Boolean> checkValue = new ArrayList<>();

    private static int inleg;
    private static int aantallen;
    private static int toernooiId;

    private static String max_datum;
    private static String formatDatum;
    private static String beginTime;
    private static String endTime;

    public static void showAddToernooi(JFrame frame, JPanel panel) {

            frame.setTitle("Toernooi toevoegen");
            frame.remove(panel);
            addToernooiPanel.removeAll();

            JButton terug = new JButton("Terug");
            JButton toevoegen = new JButton("Toevoegen");

            addComponent(terug,
                         new JLabel(),
                         new JLabel(),
                         new JLabel(),
                         toevoegen,
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

            clear(toernooi_id);
            clear(datum);
            clear(beginTijd);
            clear(eindTijd);
            clear(beschrijving);
            clear(condities);
            clear(max_aantallen);
            clear(inleggeld);
            clear(max_inschrijf_datum);

            frame.add(addToernooiPanel);
            frame.pack();
            frame.setSize(800, 250);

            toevoegen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    check(toernooi_id);
                    check(datum);
                    check(beginTijd);
                    check(eindTijd);
                    check(beschrijving);
                    check(condities);
                    check(max_aantallen);
                    check(inleggeld);
                    check(max_inschrijf_datum);

                    if (!checkValue.contains(false)) {
                        String query = "insert into Toernooi(toernooi_id, datum, begintijd, eindtijd, beschrijving, condities, max_aantallen, inleggeld, max_inschrijfdatum) VALUES ('" + toernooiId + "','" + formatDatum + "','" + beginTime + "','" + endTime + "','" + beschrijving.getText() + "','" + condities.getText() + "','" + aantallen + "','"+inleg+"','"+max_datum+"')";
                        try {
                            DBConnector.executeQuery(query);
                            frame.remove(addToernooiPanel);
                            Toernooien.showToernooi(frame, toernooiId);
                            checkValue.clear();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Vul alle velden correct in a.u.b.", "Er is iets misgegaan!", JOptionPane.ERROR_MESSAGE);
                        checkValue.clear();
                    }
                }
            });

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT toernooi_id FROM Toernooi";
                        Toernooi.showToernooien(frame, addToernooiPanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
    }

    public static void check(JTextField field) {
        boolean result = true;
        if (field.equals(beschrijving) || field.equals(condities)) {
            if (field.getText().equals("")) {
                field.setBorder(BorderFactory.createLineBorder(Color.RED));
                result = false;
            } else {
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                result = true;
            }
        }
        if (field.equals(toernooi_id) || field.equals(max_aantallen) || field.equals(inleggeld)) {
            try {
                if (field.equals(max_aantallen)) {
                    aantallen = Integer.parseInt(max_aantallen.getText());
                } else if (field.equals(inleggeld)) {
                    inleg = Integer.parseInt(inleggeld.getText());
                } else if (field.equals(toernooi_id)) {
                    toernooiId = Integer.parseInt(toernooi_id.getText());
                }
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                result = true;
            } catch (NumberFormatException exception) {
                result = false;
                field.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }
        if (field.equals(beginTijd) || field.equals(eindTijd)) {
            try {
                Date datum = new SimpleDateFormat("HH:mm:ss").parse(field.getText());
                if (field.equals(beginTijd)) {
                    beginTime = new SimpleDateFormat("HH:mm:ss").format(datum);
                } else {
                    endTime = new SimpleDateFormat("HH:mm:ss").format(datum);
                }
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                result = true;
            } catch (ParseException e) {
                result = false;
                field.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }
        if (field.equals(max_inschrijf_datum) || field.equals(datum)) {
            Date parseDatum;
            try {
                if (field.equals(datum)) {
                    parseDatum = new SimpleDateFormat("dd-MM-yyyy").parse(field.getText());
                    formatDatum = new SimpleDateFormat("yyyy-MM-dd").format(parseDatum);
                } else {
                    parseDatum = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(field.getText());
                    max_datum = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(parseDatum);
                }
                field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                result = true;
            } catch (ParseException e) {
                result = false;
                field.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }
        checkValue.add(result);
    }

    public static ArrayList<Boolean> getCheckValues() {
        return checkValue;
    }

    public static void removeCheckValues() {
        checkValue.clear();
    }

    public static JTextField getToernooi_id() {
        return toernooi_id;
    }

    public static JTextField getDatum() {
        return datum;
    }

    public static JTextField getBeginTijd() {
        return beginTijd;
    }

    public static JTextField getEindTijd() {
        return eindTijd;
    }

    public static JTextField getBeschrijving() {
        return beschrijving;
    }

    public static JTextField getCondities() {
        return condities;
    }

    public static JTextField getMax_aantallen() {
        return max_aantallen;
    }

    public static JTextField getInleggeld() {
        return inleggeld;
    }

    public static JTextField getMax_inschrijf_datum() {
        return max_inschrijf_datum;
    }

    public static String getMax_datum() {
        return max_datum;
    }

    public static String getFormatDatum() {
        return formatDatum;
    }

    public static String getBeginTime() {
        return beginTime;
    }

    public static String getEndTime() {
        return endTime;
    }

    public static int getGeld() {
        return inleg;
    }

    public static int getAantallen() {
        return aantallen;
    }

    private static void addComponent(Component... com) {
        for (Component components : com) {
            addToernooiPanel.add(components);
        }
    }

    public static void clear(JTextField field) {
        field.setText("");
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}


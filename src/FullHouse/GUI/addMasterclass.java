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
import java.util.Date;

import static FullHouse.Classes.checkDatum.checkDateFormat;

public class addMasterclass {
    private static boolean created = false;
    private static JPanel addMasterclassPanel = new JPanel(new GridLayout(5, 5, 5, 5));
    private static JComboBox<String> locatie;

    public static void showAddMasterclass(JFrame frame, JPanel panel) {

            addMasterclassPanel.removeAll();
            frame.setTitle("Masterclass toevoegen");
            frame.remove(panel);

            String[] locaties = new String[]{"Amsterdam", "Den Haag", "Utrecht", "Eindhoven", "Arnhem"};
            locatie = new JComboBox<>(locaties);

            JButton terug = new JButton("Terug");
            addMasterclassPanel.add(terug);
            JLabel melding = new JLabel();
            addMasterclassPanel.add(melding);
            addMasterclassPanel.add(new JLabel());
            JButton toevoegen = new JButton("Toevoegen");
            addMasterclassPanel.add(toevoegen);
            JTextField datum = new JTextField();
            JTextField begintijd = new JTextField();
            JTextField eindtijd = new JTextField();
            JTextField kosten = new JTextField();
            JTextField vereiste_rating = new JTextField();
            JTextField max_aantallen = new JTextField();

            addMasterclassPanel.add(new JLabel("<html>Datum:<br>(dd-mm-jjjj)</html>: "));
            addMasterclassPanel.add(datum);
            addMasterclassPanel.add(new JLabel("<html>Begintijd:<br>(hh:mm:ss)</html>: "));
            addMasterclassPanel.add(begintijd);
            addMasterclassPanel.add(new JLabel("<html>Eindtijd:<br>(hh:mm:ss)</html>: "));
            addMasterclassPanel.add(eindtijd);
            addMasterclassPanel.add(new JLabel("Kosten: "));
            addMasterclassPanel.add(kosten);
            addMasterclassPanel.add(new JLabel("Minimum rating: "));
            addMasterclassPanel.add(vereiste_rating);
            addMasterclassPanel.add(new JLabel("Maximum aantal: "));
            addMasterclassPanel.add(max_aantallen);
            addMasterclassPanel.add(new JLabel("Locatie: "));
            addMasterclassPanel.add(locatie);

            clearTextmC(datum);
            clearTextmC(begintijd);
            clearTextmC(eindtijd);
            clearTextmC(kosten);
            clearTextmC(vereiste_rating);
            clearTextmC(max_aantallen);

            frame.add(addMasterclassPanel);
            frame.pack();
            frame.setSize(800, 250);

            toevoegen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean ingevuld = true;

                    if (max_aantallen.getText().equals("")) {
                        max_aantallen.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                        melding.setText("Vul het maximum aantal in");
                    } else {
                        max_aantallen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (vereiste_rating.getText().equals("")) {
                        vereiste_rating.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                        melding.setText("Vul de vereiste rating in");
                    } else {
                        vereiste_rating.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (kosten.getText().equals("")) {
                        kosten.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                        melding.setText("Vul de kosten in");
                    } else {
                        kosten.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (eindtijd.getText().equals("")) {
                        eindtijd.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                        melding.setText("Vul de eindtijd in");
                    } else {
                        eindtijd.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (begintijd.getText().equals("")) {
                        begintijd.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                        melding.setText("Vul de begintijd in");
                    } else {
                        begintijd.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    String gbdate = "";
                    if (datum.getText().equals("")) {
                        datum.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                        melding.setText("Vul de datum in");
                    } else {
                        datum.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        String gb = datum.getText();
                        gbdate = checkDateFormat(gb);
                        if (gbdate.equals("incorrect")) {
                            melding.setText("geboortedatum incorrect format");
                            datum.setBorder(BorderFactory.createLineBorder(Color.RED));
                            ingevuld = false;
                        }
                    }

                    if (ingevuld) {
                        melding.setText("");
                        String query = "INSERT INTO masterClass(datum, begintijd, eindtijd, kosten, vereiste_rating, max_aantallen, locatie) VALUES ('"+gbdate+"', '"+begintijd.getText()+"', '"+eindtijd.getText()+"', '"+kosten.getText()+"', '"+vereiste_rating.getText()+"', '"+max_aantallen.getText()+"', '" + locatie.getSelectedItem() +"')";
                        try {
                            DBConnector.executeQuery(query);
                            query = "SELECT code FROM masterClass WHERE code = (SELECT max(code) FROM masterClass)";
                            ResultSet rs = DBConnector.query(query);
                            if(rs.next()){
                                int id = rs.getInt("code");
                                frame.remove(addMasterclassPanel);
                                Masterclass.showMasterclass(frame, id);
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT code, datum FROM masterClass";
                        Masterclasses.showMasterclasses(frame, addMasterclassPanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            created = true;
    }

    public static void clearTextmC(JTextField field) {
        field.setText("");
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}

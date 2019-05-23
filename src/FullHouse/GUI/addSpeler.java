package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sjoer on 22-5-2019.
 */
public class addSpeler {
    private static boolean created = false;
    private static JPanel addSpelerPanel = new JPanel(new GridLayout(5, 5, 5, 5));
    public static void showAddSpeler(JFrame frame, JPanel panel) {
        if (!created) {
            frame.setTitle("Speler toevoegen");
            frame.remove(panel);

            JButton terug = new JButton("Terug");
            addSpelerPanel.add(terug);
            addSpelerPanel.add(new JLabel());
            JLabel melding = new JLabel();
            addSpelerPanel.add(melding);
            addSpelerPanel.add(new JLabel());
            JButton toevoegen = new JButton("Toevoegen");
            addSpelerPanel.add(toevoegen);
            JTextField naam = new JTextField();
            JTextField adres = new JTextField();
            JTextField woonplaats = new JTextField();
            JTextField telefoonnummer = new JTextField();
            JTextField email = new JTextField();
            JTextField gbdatum = new JTextField();
            JTextField geslacht = new JTextField();
            JTextField rating = new JTextField();
            addSpelerPanel.add(new JLabel("Naam: "));
            addSpelerPanel.add(naam);
            JLabel melding2 = new JLabel();
            addSpelerPanel.add(melding2);
            addSpelerPanel.add(new JLabel("Adres: "));
            addSpelerPanel.add(adres);
            addSpelerPanel.add(new JLabel("Woonplaats: "));
            addSpelerPanel.add(woonplaats);
            addSpelerPanel.add(new JLabel());
            addSpelerPanel.add(new JLabel("Telefoonnummer: "));
            addSpelerPanel.add(telefoonnummer);
            addSpelerPanel.add(new JLabel("E-mail: "));
            addSpelerPanel.add(email);
            addSpelerPanel.add(new JLabel());
            addSpelerPanel.add(new JLabel("<html>Geboortedatum:<br>(dd-mm-jjjj)</html>"));
            addSpelerPanel.add(gbdatum);
            addSpelerPanel.add(new JLabel("Geslacht: "));
            addSpelerPanel.add(geslacht);
            addSpelerPanel.add(new JLabel());
            addSpelerPanel.add(new JLabel());
            addSpelerPanel.add(new JLabel());

            frame.add(addSpelerPanel);
            frame.pack();
            frame.setSize(800, 250);

            toevoegen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean ingevuld = true;
                    String gbdate = "";
                    int telnummer = 0;
                    if (naam.getText().equals("")) {
                        naam.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    } else {
                        naam.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (adres.getText().equals("")) {
                        adres.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    } else {
                        adres.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (woonplaats.getText().equals("")) {
                        woonplaats.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    } else {
                        woonplaats.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (telefoonnummer.getText().equals("")) {
                        telefoonnummer.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    } else {
                        telefoonnummer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        if (telefoonnummer.getText().length() == 10) {
                            telnummer = Integer.parseInt(telefoonnummer.getText());
                        } else {
                            melding2.setText("incorrect telefoonnummer");
                            telefoonnummer.setBorder(BorderFactory.createLineBorder(Color.RED));
                            ingevuld = false;
                        }
                    }

                    if (email.getText().equals("")) {
                        email.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    } else {
                        email.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }

                    if (gbdatum.getText().equals("")) {
                        gbdatum.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    } else {
                        gbdatum.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        String gb = gbdatum.getText();
                        System.out.println(checkDateFormat(gb));
                        gbdate = checkDateFormat(gb);
                        if (gbdate.equals("incorrect")) {
                            melding.setText("geboortedatum incorrect");
                            gbdatum.setBorder(BorderFactory.createLineBorder(Color.RED));
                            ingevuld = false;
                        }
                    }

                    if (geslacht.getText().equals("")) {
                        geslacht.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    } else {
                        geslacht.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }


                    if (ingevuld) {
                        melding.setText("");
                        String query = "insert into Speler(naam, adres, woonplaats, telefoonnummer, email, geboortedatum, geslacht) VALUES ('" + naam.getText() + "','" + adres.getText() + "','" + woonplaats.getText() + "'," + telnummer + ",'" + email.getText() + "','" + gbdate + "','" + geslacht.getText() + "')";
                        try {
                            DBConnector.executeQuery(query);
                            melding.setText("Speler "+naam.getText()+" aangemaakt");
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
                        String query = "SELECT naam, id FROM Speler";
                        Spelers.showSpelers(frame, addSpelerPanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            created = true;
        }
        else{
            frame.setTitle("Speler toevoegen");
            frame.remove(panel);
            frame.add(addSpelerPanel);
            frame.pack();
            frame.setSize(800,250);
        }
    }

    public static String checkDateFormat(String date) {
        if (date.length() == 10) {
            if (date.charAt(2) == '-' && date.charAt(5) == '-') {
                String jaar = String.valueOf(date.charAt(6));
                jaar += String.valueOf(date.charAt(7));
                jaar += String.valueOf(date.charAt(8));
                jaar += String.valueOf(date.charAt(9));
                int jaarInt = Integer.parseInt(jaar);
                if (jaarInt < 0) {
                    return "incorrect";
                }

                String maand = String.valueOf(date.charAt(3));
                maand += String.valueOf(date.charAt(4));
                int maandInt = Integer.parseInt(maand);
                if (maandInt > 12 || maandInt < 1) {
                    return "incorrect";
                }

                String dag = String.valueOf(date.charAt(0));
                dag += String.valueOf(date.charAt(1));
                int dagInt = Integer.parseInt(dag);
                if (dagInt > 31 || dagInt < 1) {
                    return "incorrect";
                }

                Date parseDate = new Date(jaarInt - 1900, maandInt - 1, dagInt);
                String datum = new SimpleDateFormat("yyyy-MM-dd").format(parseDate);
                return datum;
            } else {
                return "incorrect";
            }
        } else {
            return "incorrect";
        }
    }
}

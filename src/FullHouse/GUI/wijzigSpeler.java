package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static FullHouse.Classes.checkDatum.checkDateFormat;

/**
 * Created by sjoer on 24-5-2019.
 */
public class wijzigSpeler {
    private static JTextField naam = new JTextField();
    private static JTextField adres = new JTextField();
    private static JTextField woonplaats = new JTextField();
    private static JTextField telefoonnummer = new JTextField();
    private static JTextField email = new JTextField();
    private static JTextField gbdatum = new JTextField();
    private static JTextField geslacht = new JTextField();
    private static JTextField rating = new JTextField();
    private static JComboBox<String> type;

    public static void showWijzigSpeler(JFrame frame, JPanel panel, int id) throws SQLException, ClassNotFoundException {
        frame.remove(panel);
        frame.setTitle("Speler wijzigen");

        JPanel spelerPanel = new JPanel(new GridLayout(5,5,5,5));
        String[] types = new String[]{"<html>Gecontracteerde<br>speler</html>","Speler"};
        type = new JComboBox(types);
        JButton terug = new JButton("Terug");
        spelerPanel.add(terug);
        spelerPanel.add(new JLabel());
        JLabel melding = new JLabel();
        spelerPanel.add(melding);
        spelerPanel.add(new JLabel());
        JButton wijzig = new JButton("Wijzigen");
        spelerPanel.add(wijzig);
        spelerPanel.add(new JLabel("Naam: "));
        spelerPanel.add(naam);
        JLabel melding2 = new JLabel();
        spelerPanel.add(melding2);
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
        spelerPanel.add(new JLabel("<html>Geboortedatum:<br>(dd-mm-jjjj)</html>"));
        spelerPanel.add(gbdatum);
        spelerPanel.add(new JLabel("<html>Geslacht:<br>(M/V)</html>"));
        spelerPanel.add(geslacht);
        spelerPanel.add(new JLabel());
        spelerPanel.add(new JLabel("Type speler:"));
        spelerPanel.add(type);
        setText(id);

        frame.add(spelerPanel);
        frame.pack();
        frame.setSize(800, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        wijzig.addActionListener(new ActionListener() {
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
                        telnummer = Integer.parseInt("0"+telefoonnummer.getText());
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
                    if(geslacht.getText().equals("M") || geslacht.getText().equals("V")){
                        geslacht.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }
                    else{
                        geslacht.setBorder(BorderFactory.createLineBorder(Color.RED));
                        ingevuld = false;
                    }
                }


                if (ingevuld) {
                    melding.setText("");
                    String query = "UPDATE Speler SET naam='"+naam.getText()+"', adres='"+adres.getText()+"',woonplaats='"+woonplaats.getText()+"',telefoonnummer="+telnummer+",email='"+email.getText()+"',geboortedatum='"+gbdate+"',geslacht='"+geslacht.getText()+"',rating="+rating.getText()+" WHERE id="+id;
                    try {
                        DBConnector.executeQuery(query);
                        melding.setText("Speler "+naam.getText()+" gewijzigd");
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
                frame.remove(spelerPanel);
                try {
                    Speler.showSpeler(frame, id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
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
                type.setSelectedItem("<html>Gecontracteerde<br>speler</html>");
            }
            else{
                type.setSelectedItem("Speler");
            }
        }
    }
}

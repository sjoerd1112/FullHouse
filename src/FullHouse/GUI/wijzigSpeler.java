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
    private static JTextField telefoonnummer = new JTextField();
    private static JTextField email = new JTextField();
    private static JTextField gbdatum = new JTextField();
    private static JTextField geslacht = new JTextField();
    private static JTextField rating = new JTextField();
    private static JComboBox<String> type;
    private static ArrayList<Boolean> checkValue = new ArrayList<>();

    private static int telnummer = 0;
    private static String gbdate = "";
    private static JLabel melding;
    private static JLabel melding2;

    private static JPanel spelerPanel;

    private static void addComponent(Component... panel) {
        for (Component panels : panel) {
            spelerPanel.add(panels);
        }
    }

    public static void showWijzigSpeler(JFrame frame, JPanel panel, int id) throws SQLException, ClassNotFoundException {
        frame.remove(panel);
        frame.setTitle("Speler wijzigen");

        spelerPanel = new JPanel(new GridLayout(5,5,5,5));
        String[] types = new String[]{"<html>Gecontracteerde<br>speler</html>","Speler"};
        type = new JComboBox(types);
        JButton terug = new JButton("Terug");
        melding = new JLabel();
        melding2 = new JLabel();
        JButton wijzig = new JButton("Wijzigen");

        //add components to panel
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

        setText(id);

        frame.add(spelerPanel);
        frame.pack();
        frame.setSize(800, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        wijzig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check(naam);
                check(adres);
                check(woonplaats);
                check(email);

                if (telefoonnummer.getText().equals("")) {
                    telefoonnummer.setBorder(BorderFactory.createLineBorder(Color.RED));
                    checkValue.add(false);
                } else {
                    telefoonnummer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    if (telefoonnummer.getText().length() == 10) {
                        telnummer = Integer.parseInt("0"+telefoonnummer.getText());
                        checkValue.add(true);
                    } else {
                        melding2.setText("incorrect telefoonnummer");
                        telefoonnummer.setBorder(BorderFactory.createLineBorder(Color.RED));
                        checkValue.add(false);
                    }
                }

                if (gbdatum.getText().equals("")) {
                    gbdatum.setBorder(BorderFactory.createLineBorder(Color.RED));
                    checkValue.add(false);
                } else {
                    gbdatum.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    String gb = gbdatum.getText();
                    gbdate = checkDateFormat(gb);
                    checkValue.add(true);
                    if (gbdate.equals("incorrect")) {
                        melding.setText("geboortedatum incorrect");
                        gbdatum.setBorder(BorderFactory.createLineBorder(Color.RED));
                        checkValue.add(false);
                    }
                }

                if (geslacht.getText().equals("")) {
                    geslacht.setBorder(BorderFactory.createLineBorder(Color.RED));
                    checkValue.add(false);
                } else {
                    if(geslacht.getText().equals("M") || geslacht.getText().equals("V")){
                        geslacht.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        checkValue.add(true);
                    } else{
                        geslacht.setBorder(BorderFactory.createLineBorder(Color.RED));
                        checkValue.add(false);
                    }
                }

                if (!checkValue.contains(false)) {
                    melding.setText("");
                    String query = "UPDATE Speler SET naam='"+naam.getText()+"', adres='"+adres.getText()+"',woonplaats='"+woonplaats.getText()+"',telefoonnummer="+telnummer+",email='"+email.getText()+"',geboortedatum='"+gbdate+"',geslacht='"+geslacht.getText()+"',rating="+rating.getText()+" WHERE id="+id;
                    try {
                        DBConnector.executeQuery(query);
                        melding.setText("Speler "+naam.getText()+" gewijzigd");
                        checkValue.clear();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    checkValue.clear();
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

    public static void check(JTextField field) {
        boolean result;
        if (field.getText().equals("")) {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
            result = false;
        } else {
            field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            result = true;
        }
        checkValue.add(result);
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

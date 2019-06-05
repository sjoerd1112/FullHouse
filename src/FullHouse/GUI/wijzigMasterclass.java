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
import java.util.Date;

import static FullHouse.Classes.checkDatum.checkDateFormat;

/**
 * Created by sjoer on 24-5-2019.
 */
public class wijzigMasterclass {
    private static JTextField code = new JTextField();
    private static JTextField datum = new JTextField();
    private static JTextField begintijd= new JTextField();
    private static JTextField eindtijd= new JTextField();
    private static JTextField kosten= new JTextField();
    private static JTextField vereiste_rating = new JTextField();
    private static JTextField max_aantallen= new JTextField();
    private static JLabel melding;
    private static JLabel melding2;
    private static ArrayList<Boolean> checkValue = new ArrayList<>();
    private static String gbdate = "";


    private static JPanel masterclassPanel;

    private static void addComponent(Component... com) {
        for (Component components : com) {
            masterclassPanel.add(components);
        }
    }

    public static void showWijzigMasterclass(JFrame frame, JPanel panel, int id) throws SQLException, ClassNotFoundException {
        frame.remove(panel);
        frame.setTitle("Masterclass wijzigen");

        masterclassPanel = new JPanel(new GridLayout(5,5,5,5));
        JButton terug = new JButton("Terug");
        melding = new JLabel();
        melding2 = new JLabel();
        JButton wijzig = new JButton("Wijzigen");

        //add components to panel
        addComponent(terug,
                melding,
                new JLabel(),
                wijzig,
                new JLabel("Code: "),
                code,
                new JLabel("Datum: "),
                datum,
                new JLabel("Begintijd: "),
                begintijd,
                new JLabel("Eindtijd: "),
                eindtijd,
                new JLabel("Kosten: "),
                kosten,
                new JLabel("Minimum rating:"),
                vereiste_rating,
                new JLabel("Maximaal aantal:"),
                max_aantallen);

        setText(id);

        frame.add(masterclassPanel);
        frame.pack();
        frame.setSize(800, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        wijzig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check(code);
                check(datum);
                check(begintijd);
                check(eindtijd);
                check(kosten);
                check(vereiste_rating);
                check(max_aantallen);

                if (datum.getText().equals("")) {
                    datum.setBorder(BorderFactory.createLineBorder(Color.RED));
                    checkValue.add(false);
                } else {
                    datum.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    String gb = datum.getText();
                    gbdate = checkDateFormat(gb);
                    checkValue.add(true);
                    if (gbdate.equals("incorrect")) {
                        melding.setText("geboortedatum incorrect");
                        datum.setBorder(BorderFactory.createLineBorder(Color.RED));
                        checkValue.add(false);
                    }//?
                }


                if (!checkValue.contains(false)) {
                    melding.setText("");
                    String query = "UPDATE masterClass SET code="+code.getText()+", datum='"+gbdate+"',begintijd='"+begintijd.getText()+"',eindtijd='"+eindtijd.getText()+"',kosten="+kosten.getText()+",vereiste_rating="+vereiste_rating.getText()+",max_aantallen="+max_aantallen.getText()+" WHERE code="+id;
                    try {
                        DBConnector.executeQuery(query);
                        melding.setText("Masterclass "+code.getText()+" gewijzigd");
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
                frame.remove(masterclassPanel);
                try {
                    Masterclass.showMasterclass(frame, id);
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
        String query = "SELECT * FROM masterClass WHERE code="+id;
        ResultSet rs = DBConnector.query(query);
        while(rs.next()){
            code.setText(rs.getString("code"));
            datum.setText(rs.getString("datum"));
            begintijd.setText(rs.getString("begintijd"));
            eindtijd.setText(rs.getString("eindtijd"));
            kosten.setText(rs.getString("kosten"));
            //String data = new SimpleDateFormat("dd-MM-yyyy").format(datum);
            //datum.setText(data);
            vereiste_rating.setText(rs.getString("vereiste_rating"));
            max_aantallen.setText(rs.getString("max_aantallen"));
        }
    }
}

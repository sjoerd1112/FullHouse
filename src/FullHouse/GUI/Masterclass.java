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

import static FullHouse.DB.DBConnector.query;

public class Masterclass {
    private static boolean created = false;
    private static JPanel masterclassPanel = new JPanel(new GridLayout(5, 5, 5, 5));
    private static JLabel code = new JLabel();
    private static JLabel datum = new JLabel();
    private static JLabel begintijd = new JLabel();
    private static JLabel eindtijd = new JLabel();
    private static JLabel kosten = new JLabel();
    private static JLabel vereiste_rating = new JLabel();
    private static JLabel max_aantallen = new JLabel();

    public static void showMasterclass(JFrame frame, int id) throws SQLException, ClassNotFoundException {
        if(!created) {
            frame.setTitle("Speler");
            JButton terug = new JButton("Terug");
            masterclassPanel.add(terug);
            masterclassPanel.add(code);
            JButton wijzigen = new JButton("Wijzigen");
            JButton verwijderen = new JButton("Verwijderen");
            masterclassPanel.add(wijzigen);
            masterclassPanel.add(verwijderen);
            masterclassPanel.add(new JLabel("Datum: "));
            masterclassPanel.add(datum);
            masterclassPanel.add(new JLabel("Begintijd: "));
            masterclassPanel.add(begintijd);
            masterclassPanel.add(new JLabel("Eindtijd: "));
            masterclassPanel.add(eindtijd);
            masterclassPanel.add(new JLabel("Kosten: "));
            masterclassPanel.add(kosten);
            masterclassPanel.add(new JLabel("Minimale rating: "));
            masterclassPanel.add(vereiste_rating);
            masterclassPanel.add(new JLabel("Maximaal aantal spelers: "));
            masterclassPanel.add(max_aantallen);

            String query = "SELECT * FROM masterClass WHERE code =" + id;
            setText(query);

            frame.add(masterclassPanel);
            frame.pack();
            frame.setSize(800, 250);

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String query = "SELECT code, datum FROM masterClass";
                        Masterclasses.showMasterclasses(frame, masterclassPanel, query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            wijzigen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        wijzigMasterclass.showWijzigMasterclass(frame, masterclassPanel, id);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            verwijderen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        verwijderMasterclass(id, frame);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        else{
            frame.setTitle("Masterclass");
            masterclassPanel.removeAll();
            created = false;
            showMasterclass(frame, id);
        }
        created = true;
    }

    public static void verwijderMasterclass(int id, JFrame frame) throws SQLException, ClassNotFoundException {
        if(JOptionPane.showConfirmDialog(null, "Wilt u deze Masterclass verwijderen", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            String query = "delete from masterClass where code = "+id;
            DBConnector.executeQuery(query);
            query = "SELECT code, datum FROM masterClass";
            Masterclasses.showMasterclasses(frame, masterclassPanel, query);
        }
        else{

        }

    }

    public static void setText(String query) throws SQLException {
        ResultSet rs = query(query);
        while (rs.next()) {
            code.setText(rs.getString("code"));
            begintijd.setText(rs.getString("begintijd"));
            eindtijd.setText(rs.getString("eindtijd"));
            kosten.setText(rs.getString("kosten"));
            Date date = rs.getDate("datum");
            String dateformat = new SimpleDateFormat("dd-MM-yyyy").format(date);
            datum.setText(dateformat);
            vereiste_rating.setText(rs.getString("vereiste_rating"));
            max_aantallen.setText(rs.getString("max_aantallen"));
        }

    }
}

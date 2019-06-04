package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sjoer on 3-6-2019.
 */
public class Inschrijven {
    private static JPanel inschrijfPanel = new JPanel(new GridLayout(4,5, 5, 5));
    private static JLabel naam = new JLabel();
    private static JTextField idInput = new JTextField();
    private static JLabel melding = new JLabel();

    public static void showInschrijven(JFrame frame, JPanel panel, int id) throws SQLException {
        frame.remove(panel);
        frame.setTitle("Inschrijven/uitschrijven");

        JButton terug = new JButton("Terug");
        String[] types = new String[]{"Masterclass", "Toernooi"};
        JComboBox<String> type = new JComboBox(types);
        JButton inschrijven = new JButton("Inschrijven");
        JButton uitschrijven = new JButton("Uitschrijven");

        inschrijfPanel.add(terug);
        addEmptyLabel(1);
        inschrijfPanel.add(naam);
        addEmptyLabel(3);
        inschrijfPanel.add(new JLabel("Toernooi/MC ID: "));
        inschrijfPanel.add(idInput);
        inschrijfPanel.add(melding);
        addEmptyLabel(3);
        inschrijfPanel.add(type);
        addEmptyLabel(1);
        inschrijfPanel.add(uitschrijven);
        addEmptyLabel(4);
        inschrijfPanel.add(inschrijven);

        setNaam(id);

        frame.add(inschrijfPanel);
        frame.pack();
        frame.setSize(800, 250);

        terug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    idInput.setText("");
                    melding.setText("");
                    inschrijfPanel.removeAll();
                    frame.remove(inschrijfPanel);
                    Speler.showSpeler(frame, id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        inschrijven.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idString = idInput.getText();
                if(idString.matches("[0-9]+")){
                    melding.setText("");
                    int toernooiId = Integer.parseInt(idString);
                    try {
                        inschrijving(id, toernooiId, (String) type.getSelectedItem());
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    idInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }
                else{
                    idInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    melding.setText("Vul een ID in");
                }
            }
        });

        uitschrijven.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idString = idInput.getText();
                if(idString.matches("[0-9]+")){
                    melding.setText("");
                    int toernooiId = Integer.parseInt(idString);
                    try {
                        uitschrijven(id, toernooiId, (String) type.getSelectedItem());
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    idInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }
                else{
                    idInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    melding.setText("Vul een ID in");
                }
            }
        });
    }

    private static void addEmptyLabel(int aantal){
        for(int i = 0;i<aantal;i++){
            inschrijfPanel.add(new JLabel());
        }
    }

    private static void setNaam(int id) throws SQLException {
        String query = "SELECT naam FROM Speler WHERE ID=" + id;
        ResultSet rs = DBConnector.query(query);
        while (rs.next()) {
            naam.setText(rs.getString("naam"));
        }
    }

    private static void inschrijving(int id, int toernooiId, String type) throws SQLException, ClassNotFoundException {
        if(type.equals("Toernooi")) {
            if(checkToernooi(toernooiId)==false){
                idInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                melding.setText("<html>Toernooi id <br>bestaat niet</html>");
            }
            else{
                if(!checkToernooiInschrijving(id, toernooiId)) {
                    melding.setText("Speler ingeschreven");
                    String query = "INSERT INTO toernooi_inschrijving(speler, toernooi) VALUES (" + id + ", " + toernooiId + ")";
                    DBConnector.executeQuery(query);
                    idInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }
                else {
                    melding.setText("Speler is al ingeschreven");
                }
            }
        }else{
            String query = "SELECT rating FROM Speler WHERE id="+id;
            ResultSet rs = DBConnector.query(query);
            int rating = 0;
            while(rs.next()){
                rating = rs.getInt("rating");
            }
            if(checkMasterclass(id, toernooiId)) {
                query = "SELECT vereiste_rating FROM masterClass WHERE code="+toernooiId;
                rs = DBConnector.query(query);
                boolean minRating = false;
                while (rs.next()) {
                    if (rs.getInt("vereiste_rating") <= rating) {
                        minRating = true;
                    }
                }
                if(minRating==false){
                    idInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    melding.setText("<html>Minimale rating<br>niet hoog genoeg</html>");
                }
                else {
                    if (!checkMasterclassInschrijving(id, toernooiId)) {
                        melding.setText("Speler ingeschreven");
                        query = "INSERT INTO mC_inschrijving(masterClass, speler) VALUES (" + toernooiId + ", " + id + ")";
                        DBConnector.executeQuery(query);
                        idInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    }
                    else{
                        melding.setText("Speler is al ingeschreven");
                    }
                }
            }
            else {
                    idInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                    melding.setText("<html>Masterclass id <br>bestaat niet</html>");
                }
            }
        }

    private static void uitschrijven(int id, int toernooiId, String type) throws SQLException, ClassNotFoundException {
        if (type.equals("Toernooi")) {
            if (checkToernooi(toernooiId) == false) {
                idInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                melding.setText("<html>Toernooi id <br>bestaat niet</html>");
            } else {
                if(checkToernooiInschrijving(id, toernooiId)){
                    melding.setText("Speler uitgeschreven");
                    String query = "DELETE FROM toernooi_inschrijving WHERE speler=" + id;
                    DBConnector.executeQuery(query);
                    idInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }
                else{
                    melding.setText("<html>Speler is niet<br>ingeschreven</html>");
                }
            }
        }
        else{
            String query = "SELECT code FROM masterClass";
            ResultSet rs = DBConnector.query(query);
            Boolean inRS = false;
            while(rs.next()&&inRS==false){
                int mcId = rs.getInt("code");
                if(mcId==toernooiId){
                    inRS = true;
                }
            }
            if(!inRS){
                idInput.setBorder(BorderFactory.createLineBorder(Color.RED));
                melding.setText("<html>Masterclass id <br>bestaat niet</html>");
            }
            else{
                if(checkMasterclassInschrijving(id, toernooiId)){
                    melding.setText("Speler uitgeschreven");
                    query = "DELETE FROM mC_inschrijving WHERE speler=" + id;
                    DBConnector.executeQuery(query);
                    idInput.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                }
                else{
                    melding.setText("<html>Speler is niet<br>ingeschreven</html>");
                }
            }
        }
    }

    private static boolean checkToernooi(int toernooiId) throws SQLException {
        String query = "SELECT toernooi_id FROM Toernooi";
        ResultSet rs = DBConnector.query(query);
        Boolean inRS = false;
        while (rs.next() && inRS == false) {
            int toernooi_id = rs.getInt("toernooi_id");
            if (toernooi_id == toernooiId) {
                inRS = true;
            }
        }
        return inRS;
    }

    private static boolean checkToernooiInschrijving(int id, int toernooiId) throws SQLException {
        String query = "SELECT * FROM toernooi_inschrijving";
        ResultSet rs = DBConnector.query(query);
        boolean al_ingeschreven = false;
        while (rs.next()) {
            int speler = rs.getInt("speler");
            int toernooi = rs.getInt("toernooi");
            if (speler == id && toernooi == toernooiId) {
                al_ingeschreven = true;
            }
        }
        return al_ingeschreven;
    }

    private static boolean checkMasterclass(int id, int toernooiId) throws SQLException {
        String query = "SELECT code FROM masterClass";
        ResultSet rs = DBConnector.query(query);
        Boolean inRS = false;
        while(rs.next()&&inRS==false){
            int mcId = rs.getInt("code");
            if(mcId==toernooiId){
                inRS = true;
            }
        }
        return inRS;
    }

    private static boolean checkMasterclassInschrijving(int id, int toernooiId) throws SQLException {
        String query = "SELECT * FROM mC_inschrijving";
        ResultSet rs = DBConnector.query(query);
        boolean al_ingeschreven = false;
        while (rs.next()) {
            int speler = rs.getInt("speler");
            int toernooi = rs.getInt("masterClass");
            if (speler == id && toernooi == toernooiId) {
                al_ingeschreven = true;
            }
        }
        return al_ingeschreven;
    }
}

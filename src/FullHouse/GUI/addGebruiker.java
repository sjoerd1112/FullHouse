package FullHouse.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import static FullHouse.DB.DBConnector.query;
import static FullHouse.DB.DBConnector.updateQuery;

/**
 * Created by sjoer on 10-5-2019.
 */
public class addGebruiker {
    private static JPanel gebruikerPanel = new JPanel(new GridLayout(6,5));
    private static JLabel melding = new JLabel();
    private static boolean created = false;
    private static JTextField userTextfield = new JTextField();
    private static JPasswordField passwordTextfield = new JPasswordField();

    public static void showToevoegen(JFrame frame, JPanel panel) {
        if(!created) {
            frame.remove(panel);
            frame.setTitle("Gebruiker Toevoegen");
            gebruikerPanel.add(new JLabel("Toevoegen"));
            addLabel(3);
            JButton terug = new JButton("Terug");
            gebruikerPanel.add(terug);
            addLabel(1);
            gebruikerPanel.add(new JLabel("Gebruikersnaam"));
            addLabel(1);
            gebruikerPanel.add(new JLabel("Wachtwoord"));
            addLabel(2);
            userTextfield.addKeyListener(new checkButton());
            passwordTextfield.addKeyListener(new checkButton());
            gebruikerPanel.add(userTextfield);
            gebruikerPanel.add(melding);
            gebruikerPanel.add(passwordTextfield);
            addLabel(15);
            JButton confirmButton = new JButton("Ok");
            gebruikerPanel.add(confirmButton);

            frame.add(gebruikerPanel);
            frame.pack();
            frame.setSize(800, 250);

            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String gebruiker = userTextfield.getText();
                    String wachtwoord = passwordTextfield.getText();
                    try {
                        addGebruiker(gebruiker, wachtwoord);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(gebruikerPanel);
                    Home.showHome(frame, gebruikerPanel);
                }
            });

            created = true;
        }
        else{
            frame.setTitle("Gebruiker Toevoegen");
            frame.remove(panel);
            frame.add(gebruikerPanel);
            frame.pack();
            frame.setSize(800,250);
        }
    }

    public static void addLabel(int aantal){
        for(int i = 0;i<aantal;i++){
            gebruikerPanel.add(new JLabel());
        }
    }

    private static class checkButton extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode()== KeyEvent.VK_ENTER){
                try {
                    confirmButton();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void confirmButton() throws SQLException, ClassNotFoundException {
        String username = userTextfield.getText();
        String password = passwordTextfield.getText();
        addGebruiker(username, password);
    }

    public static void addGebruiker(String gebruikersnaam, String wachtwoord) throws SQLException, ClassNotFoundException {
        int wachtwoordHash = wachtwoord.hashCode();
        String query = ("SELECT gebruikersnaam FROM login");

        ResultSet rs = query(query);
        boolean bestaatAl = false;
        while(rs.next()){
            if(rs.getString(1).equals(gebruikersnaam)){
                melding.setText("Gebruiker "+gebruikersnaam+" bestaat al");
                bestaatAl = true;
            }
        }
        if(!bestaatAl){
            if(gebruikersnaam.equals("") || wachtwoord.equals("")){
                melding.setText("Vul beide velden in");
            }
            else {
                query = ("INSERT INTO `login` (`Gebruikersnaam`, `Wachtwoord`) VALUES ('" + gebruikersnaam + "', '" + wachtwoordHash + "')");
                if (updateQuery(query) == 1) {
                    melding.setText("<html>Gebruiker " + gebruikersnaam + "<br> aangemaakt.</html>");
                }
            }
        }
    }
}

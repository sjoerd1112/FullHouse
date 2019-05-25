package FullHouse.GUI;

/**
 * Created by sjoer on 8-5-2019.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

import static FullHouse.DB.DBConnector.query;

public class Login {
    private static JFrame frame = new JFrame("FullHouse");
    private static JPanel loginPanel;
    private static JTextField userTextfield = new JTextField();
    private static JPasswordField passwordTextfield = new JPasswordField();
    private static JLabel melding = new JLabel();

    public static void showLogin() throws SQLException, ClassNotFoundException {
        loginPanel = new JPanel(new GridLayout(6, 5));
        loginPanel.add(new JLabel("Login"));
        addLabel(5);
        loginPanel.add(new JLabel("Gebruikersnaam"));
        addLabel(1);
        loginPanel.add(new JLabel("Wachtwoord"));
        addLabel(2);
        loginPanel.add(userTextfield);
        loginPanel.add(melding);
        loginPanel.add(passwordTextfield);
        addLabel(5);
        addLabel(10);
        JButton confirmButton = new JButton("Ok");
        userTextfield.addKeyListener(new checkButton());
        passwordTextfield.addKeyListener(new checkButton());
        loginPanel.add(confirmButton);

        frame.add(loginPanel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmButton();
            }
        });
    }
    public static void showLogin(JFrame frame, JPanel panel){
        frame.remove(panel);
        frame.add(loginPanel);
        userTextfield.setText("");
        passwordTextfield.setText("");
        frame.pack();
        frame.setSize(800,250);
    }

    private static class checkButton extends KeyAdapter{

        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode()== KeyEvent.VK_ENTER){
                confirmButton();
            }
        }
    }

    public static void confirmButton(){
        String username = userTextfield.getText();
        String password = passwordTextfield.getText();
        if(checkLogin(username, password)){
            Home.showHome(frame, loginPanel);
        }
    }

    public static boolean checkLogin(String gebruikersnaam, String wachtwoord){
        if (gebruikersnaam.equals("") || wachtwoord.equals("")) {
            melding.setText("Vul beide velden in");
            return false;
        } else {
            String query = ("SELECT Gebruikersnaam FROM login WHERE '" + gebruikersnaam + "'= Gebruikersnaam AND '" + wachtwoord.hashCode() + "'= Wachtwoord");
            try {
                ResultSet rs = query(query);
                while (rs.next()) {
                    melding.setText("");
                    return true;
                }
                if (rs.next() == false) {
                    melding.setText("onjuiste combinatie");
                    return false;
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }


    public static void addLabel(int aantal){
        for(int i = 0;i<aantal;i++){
            loginPanel.add(new JLabel());
        }
    }
}

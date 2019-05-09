package FullHouse.GUI;

/**
 * Created by sjoer on 8-5-2019.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import static FullHouse.DB.DBConnector.query;

public class Login {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        JFrame frame = new JFrame("FullHouse");
        JPanel loginPanel = new JPanel(new GridLayout(6, 5));

        loginPanel.add(new JLabel("Login"));
        for(int i = 0;i<5;i++){
            loginPanel.add(new JLabel());
        }
        loginPanel.add(new JLabel("Gebruikersnaam"));
        loginPanel.add(new JLabel());
        loginPanel.add(new JLabel("Wachtwoord"));
        loginPanel.add(new JLabel());
        loginPanel.add(new JLabel());
        JTextField userTextfield = new JTextField();
        loginPanel.add(userTextfield);
        JLabel melding = new JLabel();
        loginPanel.add(melding);
        JTextField passwordTextfield = new JTextField();
        loginPanel.add(passwordTextfield);
        for(int i = 0;i<4;i++){
            loginPanel.add(new JLabel());
        }
        JButton resetPassButton = new JButton("Reset wachtwoord");
        loginPanel.add(resetPassButton);
        for(int i = 0;i<4;i++){
            loginPanel.add(new JLabel());
        }

        for(int i = 0;i<6;i++){
            loginPanel.add(new JLabel());
        }
        JButton confirmButton = new JButton("Ok");
        loginPanel.add(confirmButton);

        frame.add(loginPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800,250);
        frame.setVisible(true);


        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String username = userTextfield.getText();
                String password = passwordTextfield.getText();

                if(username.equals("") || password.equals("")){
                    melding.setText("Vul beide velden in");
                }
                else{
                    String query = ("SELECT id FROM login WHERE '" + username + "'=Gebruikersnaam AND '" + password + "'=Wachtwoord");
                    try {
                        ResultSet rs = query(query);
                        while(rs.next()){
                            System.out.println(rs.getInt(1));
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}

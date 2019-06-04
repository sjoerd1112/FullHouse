package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class Toernooi {

    private static int rows = 0;
    private static int aantal = 7;
    private static boolean created = false;

    private static JScrollPane scroll = new JScrollPane();
    private static JTextField zoeken = new JTextField();

    public static void showToernooien(JFrame frame, JPanel panel, String query) throws SQLException {
        if(!created) {
            JPanel toernooiPanel= new ToernooiScrollablePanel();

            JButton terug = new JButton("Terug");
            JButton zoek = new JButton("Zoeken");
            JButton toevoegen = new JButton("Toevoegen");

            JLabel zoekenLabel = new JLabel("Zoek toernooi: ");
            JLabel leeg = new JLabel();
            JLabel empty = new JLabel();

            frame.remove(panel);
            frame.setTitle("Toernooien overzicht");

            toernooiPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            terug.setPreferredSize(new Dimension(100,25));
            toernooiPanel.add(terug);
            leeg.setPreferredSize(new Dimension(100,25));
            toernooiPanel.add(leeg);

            zoekenLabel.setPreferredSize(new Dimension(100,25));
            toernooiPanel.add(zoekenLabel);

            zoeken.setPreferredSize(new Dimension(100,25));
            zoeken.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                }
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                        zoekToernooi(frame, panel);
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            toernooiPanel.add(zoeken);

            zoek.setPreferredSize(new Dimension(100,25));
            toernooiPanel.add(zoek);

            empty.setPreferredSize(new Dimension(100,25));
            toernooiPanel.add(empty);

            toevoegen.setPreferredSize(new Dimension(100,25));
            toernooiPanel.add(toevoegen);

            ResultSet rs = DBConnector.query(query);
            while(rs.next()){
                String naam = rs.getString("toernooi_id");
                if(naam.length()>10 && naam.contains(" ")){
                    for(int i = 0;i<naam.length();i++){
                        if(naam.charAt(i) == ' '){
                            naam = "<html>"+naam.substring(0,i)+"<br>"+naam.substring(i+1, naam.length())+"</html>";
                        }
                    }
                }

                JButton naamButton = new JButton(naam);
                naamButton.setPreferredSize(new Dimension(100, 100));
                int id = rs.getInt("toernooi_id");
                naamButton.putClientProperty("id", id);
                aantal++;

                toernooiPanel.add(naamButton);

                naamButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int i = (int) naamButton.getClientProperty("id");
                        try {
                            frame.remove(scroll);
                            created = false;
                            aantal = 7;
                            Toernooien.showToernooi(frame, i);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        } catch (ClassNotFoundException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }

            toevoegen.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.getContentPane().remove(scroll);
                    created = false;
                    aantal = 7;
                    addToernooi.showAddToernooi(frame, panel);
                }
            });

            zoek.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoekToernooi(frame, panel);
                }
            });

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(scroll);
                    created = false;
                    aantal = 7;
                    Home.showHome(frame);
                }
            });

            rows = Math.round(aantal / 7) + 1;

            scroll.setViewportView(toernooiPanel);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            frame.getContentPane().add(scroll);
            frame.pack();
            frame.setSize(800,250);

        }
        else{
            frame.setTitle("Toernooien overzicht");
            frame.remove(panel);
            frame.getContentPane().add(scroll);
            frame.pack();
            frame.setSize(800,250);
        }
        created = true;
    }

    public static int getRows(){
        return rows;
    }

    public static void zoekToernooi(JFrame frame, JPanel panel) {
        String zoekNaam = zoeken.getText();
        try {
            String query = "SELECT toernooi_id FROM Toernooi WHERE toernooi_id LIKE'%" + zoekNaam + "%'";
            created = false;
            frame.getContentPane().remove(scroll);
            aantal = 7;
            showToernooien(frame, panel, query);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}

class ToernooiScrollablePanel extends JPanel implements Scrollable {
    public Dimension getPreferredSize() {
        return getPreferredScrollableViewportSize();
    }

    public Dimension getPreferredScrollableViewportSize() {
        if (getParent() == null)
            return getSize();
        return new Dimension(10, Toernooi.getRows()*105);
    }
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 50;
    }
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation,int direction) {
        return 10;
    }
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    public boolean getScrollableTracksViewportWidth() {
        return getParent() != null ? getParent().getSize().width > getPreferredSize().width : true;
    }
}

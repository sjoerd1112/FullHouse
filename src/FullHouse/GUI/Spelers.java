package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class Spelers {
    private static int rows = 0;
    private static int aantal = 1;
    private static boolean created = false;
    private static JButton naamButton = new JButton();
    private static JScrollPane scroll = new JScrollPane();

    public static void showSpelers(JFrame frame, JPanel panel, String query) throws SQLException {
        if(!created) {
            frame.remove(panel);
            frame.setTitle("Spelers Overzicht");

            JPanel spelersPanel = new ScrollablePanel();
            spelersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            JButton terug = new JButton("Terug");
            terug.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(terug);

            JLabel leeg = new JLabel();
            leeg.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(leeg);

            JLabel zoekenLabel = new JLabel("Zoek speler: ");
            zoekenLabel.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(zoekenLabel);

            JTextField zoeken = new JTextField();
            zoeken.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(zoeken);

            JButton zoek = new JButton("Zoeken");
            zoek.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(zoek);

            JLabel empty = new JLabel();
            empty.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(empty);

            JButton toevoegen = new JButton("Toevoegen");
            toevoegen.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(toevoegen);

            ResultSet rs = DBConnector.query(query);
            while(rs.next()){
                String naam = rs.getString("naam");
                if(naam.length()>10 && naam.contains(" ")){
                    for(int i = 0;i<naam.length();i++){
                        if(naam.charAt(i) == ' '){
                            naam = "<html>"+naam.substring(0,i)+"<br>"+naam.substring(i+1, naam.length())+"</html>";
                        }
                    }
                }

                naamButton = new JButton(naam);
                naamButton.setPreferredSize(new Dimension(100, 100));
                int id = rs.getInt("id");
                naamButton.putClientProperty("id", id);
                aantal++;

                spelersPanel.add(naamButton);

                naamButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int i = (int) naamButton.getClientProperty("id");
                        try {
                            frame.remove(scroll);
                            Speler.showSpeler(frame, i);
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
                    System.out.println("Toevoegen");
                }
            });

            zoek.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String zoekNaam = zoeken.getText();
                    try {
                        String query = "SELECT naam, id FROM Speler WHERE naam LIKE'%"+zoekNaam+"%'";
                        created = false;
                        frame.getContentPane().remove(scroll);
                        aantal = 1;
                        showSpelers(frame, panel, query);
                    }
                    catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(scroll);
                    Home.showHome(frame);
                }
            });

            rows = Math.round(aantal / 7) + 1;

            scroll.setViewportView(spelersPanel);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            frame.getContentPane().add(scroll);
            frame.pack();
            frame.setSize(800,250);
        }
        else{
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

}
class ScrollablePanel extends JPanel implements Scrollable {
    public Dimension getPreferredSize() {
        return getPreferredScrollableViewportSize();
    }

    public Dimension getPreferredScrollableViewportSize() {
        if (getParent() == null)
            return getSize();
        return new Dimension(10, Spelers.getRows()*105);
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
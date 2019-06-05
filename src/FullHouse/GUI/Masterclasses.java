package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class Masterclasses {
    private static int rows = 0;
    private static int aantal = 7;
    private static boolean created = false;
    private static JScrollPane scroll = new JScrollPane();
    private static JTextField zoeken = new JTextField();

    public static void showMasterclasses(JFrame frame, JPanel panel, String query) throws SQLException {
        if(!created) {
            frame.remove(panel);
            frame.setTitle("Masterclasses Overzicht");

            JPanel MasterclassPanel = new ScrollablePanelMasterclasses();
            MasterclassPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            JButton terug = new JButton("Terug");
            terug.setPreferredSize(new Dimension(100,25));
            MasterclassPanel.add(terug);

            JLabel leeg = new JLabel();
            leeg.setPreferredSize(new Dimension(80,25));
            MasterclassPanel.add(leeg);

            JLabel zoekenLabel = new JLabel("Zoek masterclass: ");
            zoekenLabel.setPreferredSize(new Dimension(120,25));
            MasterclassPanel.add(zoekenLabel);

            zoeken.setPreferredSize(new Dimension(100,25));
            zoeken.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                        zoekMasterclass(frame, panel);
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
            MasterclassPanel.add(zoeken);

            JButton zoek = new JButton("Zoeken");
            zoek.setPreferredSize(new Dimension(100,25));
            MasterclassPanel.add(zoek);

            JLabel empty = new JLabel();
            empty.setPreferredSize(new Dimension(100,25));
            MasterclassPanel.add(empty);

            JButton toevoegen = new JButton("Toevoegen");
            toevoegen.setPreferredSize(new Dimension(100,25));
            MasterclassPanel.add(toevoegen);

            ResultSet rs = DBConnector.query(query);
            while(rs.next()){
                String naam = "<html>";
                naam+= rs.getString("code");
                naam+="<br>";
                naam+= rs.getString("datum");
                naam+="</html>";

                JButton naamButton = new JButton(naam);
                naamButton.setPreferredSize(new Dimension(100, 100));
                int id = rs.getInt("code");
                naamButton.putClientProperty("id", id);
                aantal++;

                MasterclassPanel.add(naamButton);

                naamButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int i = (int) naamButton.getClientProperty("id");
                        try {
                            frame.remove(scroll);
                            created = false;
                            aantal = 7;
                            Masterclass.showMasterclass(frame, i);
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
                    addMasterclass.showAddMasterclass(frame, panel);
                }
            });

            zoek.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoekMasterclass(frame, panel);
                }
            });

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(scroll);
                    created = false;
                    aantal = 7;
                    Home.showHome();
                }
            });

            rows = Math.round(aantal / 7) + 1;

            scroll.setViewportView(MasterclassPanel);
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

    public static void zoekMasterclass(JFrame frame, JPanel panel){
        String zoekCode = zoeken.getText();
        try {
            String query = "SELECT code, datum FROM masterClass WHERE code LIKE'%"+zoekCode+"%'";
            created = false;
            frame.getContentPane().remove(scroll);
            aantal = 7;
            showMasterclasses(frame, panel, query);
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    static class ScrollablePanelMasterclasses extends JPanel implements Scrollable {
        public Dimension getPreferredSize() {
            return getPreferredScrollableViewportSize();
        }

        public Dimension getPreferredScrollableViewportSize() {
            if (getParent() == null)
                return getSize();
            return new Dimension(10, Masterclasses.getRows() * 105);
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 50;
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 10;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        public boolean getScrollableTracksViewportWidth() {
            return getParent() != null ? getParent().getSize().width > getPreferredSize().width : true;
        }
    }
}

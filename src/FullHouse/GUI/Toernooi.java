package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;

public class Toernooi {

    private static int rows = 0;
    private static int aantal = 7;

    private static JScrollPane scroll = new JScrollPane();
    private static JTextField zoeken = new JTextField();

    private static JCheckBox box1;
    private static JCheckBox box2;

    private static JPanel ingeschreven_Panel;

    private static ArrayList<JPanel> panels = new ArrayList<>();
    private static ArrayList<checkBox> list = new ArrayList<>();

    private static JPanel spelersPanel= new ToernooiScrollablePanel();

    public static void clearSearchBar() {
        if (!zoeken.getText().isEmpty()) {
            zoeken.setText("");
        }
    }

    public static void showToernooien(JFrame frame, JPanel panel, String query) throws SQLException {

            panel.removeAll();
            frame.remove(panel);
            frame.setTitle("Toernooi");

            JPanel toernooiPanel= new ToernooiScrollablePanel();

            JButton terug = new JButton("Terug");
            JButton zoek = new JButton("Zoeken");
            JButton toevoegen = new JButton("Toevoegen");

            JLabel zoekenLabel = new JLabel("Zoek toernooi: ");
            JLabel leeg = new JLabel();
            JLabel empty = new JLabel();

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
                    frame.dispose();
                    aantal = 7;
                    Home.showHome();
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

    public static int getRows(){
        return rows;
    }

    public static void zoekToernooi(JFrame frame, JPanel panel) {
        String zoekNaam = zoeken.getText();
        try {
            String query = "SELECT toernooi_id FROM Toernooi WHERE toernooi_id LIKE'%" + zoekNaam + "%'";
            frame.getContentPane().remove(scroll);
            aantal = 7;
            showToernooien(frame, panel, query);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void zoekIngeschrevenSpeler(JFrame frame, JPanel panel, int id) {
        String zoekNaam = zoeken.getText();
        try {
            String query = "SELECT * FROM Speler s INNER JOIN toernooi_inschrijving TI on s.id = TI.speler WHERE TI.toernooi = " + id + " AND s.id IN (SELECT speler FROM toernooi_inschrijving) AND naam LIKE '" + zoekNaam + "%'";
            frame.getContentPane().remove(scroll);
            aantal = 7;
            showIngeschrevenSpelers(frame, panel, id, query);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void showIngeschrevenSpelers(JFrame frame, JPanel panel, int toernooiId, String query) throws SQLException {
            spelersPanel.removeAll();
            panel.removeAll();
            frame.remove(panel);

            frame.setTitle("Ingeschreven spelers");

            JButton terug = new JButton("Terug");
            JButton zoek = new JButton("Zoeken");
            JButton tafelindeling = new JButton("Tafelindelingen");

            JLabel zoekenLabel = new JLabel("Zoek spelers: ");
            JLabel leeg = new JLabel();
            JLabel empty = new JLabel();

            spelersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            terug.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(terug);

            leeg.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(leeg);

            zoekenLabel.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(zoekenLabel);

            zoeken.setPreferredSize(new Dimension(100,25));
            zoeken.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                }
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                        zoekIngeschrevenSpeler(frame, panel, toernooiId);
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                }
            });

            spelersPanel.add(zoeken);

            zoek.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(zoek);

            empty.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(tafelindeling);

            JButton checkButton = new JButton("Check");
            checkButton.setPreferredSize(new Dimension(100, 25));
            spelersPanel.add(checkButton);

            ResultSet rs;

            rs = DBConnector.query(query);
            list.clear();
            panels.clear();
            while(rs.next()){
                ingeschreven(rs, toernooiId);
            }
            setcheckBox();

            tafelindeling.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(scroll);
                    try {
                        Tafelindelingen.showTafelindeling(frame, panel, toernooiId);
                    } catch (SQLException  | ClassNotFoundException e2) {
                        e2.printStackTrace();
                    }
                }
            });

            checkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    check();
                }
            });

            zoek.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    zoekIngeschrevenSpeler(frame, panel, toernooiId);
                }
            });

            terug.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.remove(scroll);
                    aantal = 7;
                    try {
                        Toernooien.showToernooi(frame, toernooiId);
                    } catch (SQLException  | ClassNotFoundException e2) {
                        e2.printStackTrace();
                    }
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

    public static void addListeners(JPanel panel, JCheckBox b, String speler, String tekst, int id) {
        checkBox check = new checkBox(speler, b, tekst);
        list.add(check);
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String query;
                        if (!b.isSelected()) {
                            b.setSelected(false);
                            query = "UPDATE toernooi_inschrijving INNER JOIN Speler s on speler = s.id SET " + b.getText() + " = 'N' WHERE toernooi = " + id + " AND s.naam = '" + speler + "'";
                        } else {
                            b.setSelected(true);
                            query = "UPDATE toernooi_inschrijving INNER JOIN Speler s on speler = s.id SET " + b.getText() + " = 'J' WHERE toernooi = " + id + " AND s.naam = '" + speler + "'";
                        }
                        try {
                            DBConnector.executeQuery(query);
                        } catch (SQLException | ClassNotFoundException e2) {
                            e2.printStackTrace();
                        }
                    }
                });
                panel.add(b);
    }

    public static void setcheckBox() {
        for (checkBox check : list) {
            if (check.getTekst().equals("J")) {
                check.getBox().setSelected(true);
            } else {
                check.getBox().setSelected(false);
            }
        }
        check();
    }

    public static void ingeschreven(ResultSet rs, int toernooiId) {

        box1 = new JCheckBox("betaald");
        box2 = new JCheckBox("aanwezig");

        Font myFont = new Font("Serif", Font.ITALIC, 14);
        box1.setFont(myFont);
        box2.setFont(myFont);

        ingeschreven_Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        try {
            String speler = rs.getString("naam");
            String betaald = rs.getString("betaald");
            String aanwezig = rs.getString("aanwezig");

            ingeschreven_Panel.add(new JLabel(speler));

            addListeners(ingeschreven_Panel, box1, speler, betaald, toernooiId);
            addListeners(ingeschreven_Panel, box2, speler, aanwezig, toernooiId);

            if (panels.isEmpty() || panels.size() < list.size()) {
                for (int j = panels.size(); j < list.size(); j++) {
                    panels.add(ingeschreven_Panel);
                }
            }
            spelersPanel.add(ingeschreven_Panel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void check() {
        for(int i = 0; i < panels.size(); i+=2){
            if(list.get(i).getBox().isSelected() && list.get(i+1).getBox().isSelected()){
                    panels.get(i).setBorder(BorderFactory.createLineBorder(Color.GREEN));
            } else{
                    panels.get(i).setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }
        for (JPanel panel : panels) {
            spelersPanel.add(panel);
        }
    }
}

class checkBox {
    private String speler;
    private JCheckBox box;

    private String tekst;

    public checkBox(String speler, JCheckBox box, String tekst) {
        this.speler = speler;
        this.box = box;
        this.tekst = tekst;
    }

    public String getTekst() {
        return tekst;
    }

    public String getSpeler() {
        return speler;
    }

    public JCheckBox getBox() {
        return box;
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

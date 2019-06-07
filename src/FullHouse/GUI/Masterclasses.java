package FullHouse.GUI;

import FullHouse.DB.DBConnector;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;

public class Masterclasses {
    private static int rows = 0;
    private static int aantal = 7;
    private static boolean created = false;
    private static JScrollPane scroll = new JScrollPane();
    private static JTextField zoeken = new JTextField();

    public static void clearSearchBar() {
        if (!zoeken.getText().isEmpty()) {
            zoeken.setText("");
        }
    }

    public static void showMasterclasses(JFrame frame, JPanel panel, String query) throws SQLException {
        if(!created) {
            frame.remove(panel);
            frame.setTitle("Masterclasses overzicht");

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
                Date gbDate = rs.getDate("datum");
                String datum = new SimpleDateFormat("dd-MM-yyyy").format(gbDate);
                naam+= datum;
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

    private static JPanel spelersPanel= new ScrollablePanelMasterclasses();

    private static ArrayList<JPanel> panels = new ArrayList<>();
    private static ArrayList<checkBox> list = new ArrayList<>();

    private static JCheckBox box1;

    private static JPanel ingeschreven_Panel;

    public static void showIngeschrevenSpelers(JFrame frame, JPanel panel, int masterClass, String query) throws SQLException {
        spelersPanel.removeAll();
        panel.removeAll();
        frame.remove(panel);

        frame.setTitle("Ingeschreven spelers");

        JButton terug = new JButton("Terug");
        JButton zoek = new JButton("Zoeken");

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
                    zoekIngeschrevenSpeler(frame, panel, masterClass);
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
        spelersPanel.add(empty);

        JButton checkButton = new JButton("Check");
        checkButton.setPreferredSize(new Dimension(100, 25));
        spelersPanel.add(checkButton);

        ResultSet rs;

        rs = DBConnector.query(query);
        list.clear();
        panels.clear();
        while(rs.next()){
            ingeschreven(rs, masterClass);
        }
        setcheckBox();

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                check();
            }
        });

        zoek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoekIngeschrevenSpeler(frame, panel, masterClass);
            }
        });

        terug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(scroll);
                aantal = 7;
                try {
                    Masterclass.showMasterclass(frame, masterClass);
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

    public static void zoekIngeschrevenSpeler(JFrame frame, JPanel panel, int id) {
        String zoekNaam = zoeken.getText();
        try {
            String query = "SELECT * FROM Speler s INNER JOIN mC_inschrijving mC on s.id = mC.speler WHERE mC.masterClass = " + id + " AND s.id IN (SELECT speler FROM mC_inschrijving) AND naam LIKE '" + zoekNaam + "%'";
            frame.getContentPane().remove(scroll);
            aantal = 7;
            showIngeschrevenSpelers(frame, panel, id, query);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
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
                    query = "UPDATE mC_inschrijving INNER JOIN Speler s on speler = s.id SET " + b.getText() + " = 'N' WHERE masterClass = " + id + " AND s.naam = '" + speler + "'";
                } else {
                    b.setSelected(true);
                    query = "UPDATE mC_inschrijving INNER JOIN Speler s on speler = s.id SET " + b.getText() + " = 'J' WHERE masterClass = " + id + " AND s.naam = '" + speler + "'";
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

        ingeschreven_Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        box1 = new JCheckBox("betaald");

        Font myFont = new Font("Serif", Font.ITALIC, 14);
        box1.setFont(myFont);

        try {
            String speler = rs.getString("naam");
            String betaald = rs.getString("betaald");

            ingeschreven_Panel.add(new JLabel(speler));

            addListeners(ingeschreven_Panel, box1, speler, betaald, toernooiId);

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
        for(int i = 0; i < panels.size(); i++){
            if(list.get(i).getBox().isSelected()){
                panels.get(i).setBorder(BorderFactory.createLineBorder(Color.GREEN));
            } else{
                panels.get(i).setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        }
        for (JPanel panel : panels) {
            spelersPanel.add(panel);
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

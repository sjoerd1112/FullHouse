package FullHouse.GUI;

import FullHouse.Classes.Tafel;
import FullHouse.Classes.Tafelindeling;
import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sjoer on 7-6-2019.
 */
public class Tafelindelingen {
    private static int rows = 0;

    private static JScrollPane scroll = new JScrollPane();
    private static ArrayList<JTextField> fields = new ArrayList<>();

    public static void showTafelindeling(JFrame frame, JPanel panel, int toernooi_id, int ronde) throws SQLException, ClassNotFoundException {
        JPanel tafelindelingPanel = new ScrollablePanelTafelindeling();
        frame.setTitle("Tafelindeling");
        frame.remove(panel);

        int aantaltafels = 0;

        String query = "SELECT ronde, toernooi FROM Tafelindeling";
        ResultSet rondeRs = DBConnector.query(query);
        boolean bestaatAl = false;
        if(rondeRs!=null){
            while(rondeRs.next()){
                if(rondeRs.getInt("ronde")==ronde&&rondeRs.getInt("toernooi")==toernooi_id){
                    bestaatAl = true;
                }
            }
        }
        if(!bestaatAl) {
            if(ronde == 1) {
                String query1 = "SELECT rating,id FROM Speler JOIN toernooi_inschrijving ON id = toernooi_inschrijving.speler WHERE betaald LIKE 'J' AND aanwezig LIKE 'J' AND toernooi ="+toernooi_id;
                ResultSet rs = DBConnector.query(query1);
                while (rs.next()) {
                    int rating = rs.getInt("rating");
                    rating+= 10;
                    String id = rs.getString("id");
                    String nieuwquery = "UPDATE Speler SET rating = "+rating+" WHERE id = "+id;
                    DBConnector.updateQuery(nieuwquery);
                }
                query = "SELECT count(id) as aantal FROM Speler JOIN toernooi_inschrijving ON id = toernooi_inschrijving.speler WHERE betaald LIKE 'J' AND aanwezig LIKE 'J' AND  toernooi=" + toernooi_id;
            } else{
                query = "SELECT COUNT(speler) as aantal FROM Tafelindeling WHERE resultaat='W' AND toernooi="+toernooi_id+" AND ronde="+(ronde-1);
            }
            ResultSet rs = DBConnector.query(query);
            int aantalSpelers = 0;
            if (rs.next()) {
                aantalSpelers = rs.getInt("aantal");
            }
            query = "SELECT type FROM Toernooi WHERE toernooi_id="+toernooi_id;
            rs = DBConnector.query(query);
            String type = "";
            if(rs.next()){
                type = rs.getString("type");
            }
            ArrayList<Tafel> tafels = Tafelindeling.createTafels(aantalSpelers, type, toernooi_id, ronde);
            aantaltafels = tafels.size();

            if (tafels.size() % 2 == 0) {
                rows = tafels.size() / 2;
            } else {
                rows = tafels.size() / 2 + 1;
            }
        }
        else{
            query = "SELECT COUNT(DISTINCT tafelNummer) as aantalTafels FROM Tafelindeling WHERE toernooi="+toernooi_id+" AND ronde="+ronde;
            ResultSet rs = DBConnector.query(query);
            if(rs.next()){
                aantaltafels = rs.getInt("aantalTafels");
                if(aantaltafels % 2 == 0){
                    rows = aantaltafels/2;
                } else{
                    rows = aantaltafels/2+1;
                }
            }
        }
        tafelindelingPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        JButton terug = new JButton("Terug");
        terug.setPreferredSize(new Dimension(100, 25));
        tafelindelingPanel.add(terug);

        JButton volgende = new JButton("Volgende");
        volgende.setPreferredSize(new Dimension(100,25));
        tafelindelingPanel.add(volgende);

        JLabel melding = new JLabel();
        melding.setPreferredSize(new Dimension(100,25));
        tafelindelingPanel.add(melding);

        for (int i = 0; i < 3; i++) {
            JLabel leeg = new JLabel();
            leeg.setPreferredSize(new Dimension(100, 25));
            tafelindelingPanel.add(leeg);
        }

        for (int i = 0; i < aantaltafels; i++) {
            DefaultListModel list = new DefaultListModel();
            list.addElement("Tafel " + (i + 1));
            query = "SELECT COUNT(tafelNummer) as aantal FROM Tafelindeling WHERE toernooi="+toernooi_id+" AND ronde="+ronde+" GROUP BY tafelNummer";
            ResultSet rs = DBConnector.query(query);
            int aantalSpelers = 0;
            if(rs.absolute(i+1)){
                aantalSpelers = rs.getInt("aantal");
            }
            int aantal = 0;
            query = "SELECT naam, id FROM Speler JOIN Tafelindeling ON Speler.id = Tafelindeling.speler WHERE Tafelindeling.tafelNummer="+i+" AND toernooi="+toernooi_id+" AND ronde="+ronde;
            rs = DBConnector.query(query);
            for (int x = 0; x < aantalSpelers; x++) {
                rs.absolute(x+1);
                int id = rs.getInt("id");
                String naam = rs.getString("naam");
                list.addElement(naam + " - " + id);
                aantal++;
            }
            int resterend = 10 - aantal;
            if (resterend >= 0) {
                for (int x = 0; x < resterend; x++) {
                    list.addElement(" ");
                }
            }
            JList lijst = new JList(list);
            lijst.setPreferredSize(new Dimension(175, 210));
            JPanel listPanel = new JPanel();
            listPanel.setPreferredSize(new Dimension(175, 280));
            listPanel.add(lijst);
            JLabel label = new JLabel("winnaar id: ");
            label.setPreferredSize(new Dimension(175,25));
            JTextField winnaarfield = new JTextField();
            winnaarfield.setPreferredSize(new Dimension(175,25));
            listPanel.add(label);
            listPanel.add(winnaarfield);
            fields.add(winnaarfield);
            tafelindelingPanel.add(listPanel);
        }

        scroll.setViewportView(tafelindelingPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.getContentPane().add(scroll);
        frame.pack();
        frame.setSize(800, 250);

        terug.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(scroll);
                fields.clear();
                try {
                    Toernooien.showToernooi(frame, toernooi_id);
                } catch (SQLException  | ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
            }
        });

        volgende.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean ingevuld = true;
                for(int i = 0;i<fields.size();i++){
                    String idString = fields.get(i).getText();
                    if(!(idString.matches("[0-9]+"))){
                        ingevuld = false;
                        melding.setText("<html>Niet alles is<br>correct ingevuld</html>");
                        fields.get(i).setBorder(BorderFactory.createLineBorder(Color.RED));
                    } else{
                        String query = "SELECT speler FROM Tafelindeling WHERE toernooi="+toernooi_id+" AND ronde="+ronde;
                        boolean bestaat = false;
                        try {
                            ResultSet rs = DBConnector.query(query);
                            while(rs.next()){
                                if(rs.getInt("speler")==Integer.parseInt(fields.get(i).getText())){
                                    bestaat = true;
                                }
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        if(bestaat) {
                            query = "SELECT rating FROM Speler JOIN Tafelindeling ON Speler.id=Tafelindeling.speler WHERE Tafelindeling.toernooi="+toernooi_id+" AND Tafelindeling.tafelNummer="+i;
                            int rating = 0;
                            int aantal = 0;
                            try {
                                ResultSet rs = DBConnector.query(query);
                                while(rs.next()){
                                    rating += rs.getInt("rating");
                                    aantal++;
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            int gem_rating = rating/aantal;

                            query = "SELECT rating, temp_rating FROM Speler WHERE id="+idString;
                            int winnaar_rating = 0;
                            int temp_rating = 0;
                            try {
                                ResultSet rs = DBConnector.query(query);
                                if(rs.next()){
                                    winnaar_rating = rs.getInt("rating");
                                    temp_rating = rs.getInt("temp_rating");
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }

                            int nieuwTempRating = 0;
                            if(winnaar_rating>=gem_rating){
                                nieuwTempRating = temp_rating + gem_rating;
                            } else{
                                nieuwTempRating = temp_rating + (2*gem_rating);
                            }
                            query = "UPDATE Speler SET temp_rating="+nieuwTempRating+"  WHERE id=" + idString;
                            try {
                                DBConnector.updateQuery(query);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }

                            query = "UPDATE Tafelindeling SET resultaat = 'W' WHERE speler=" + idString;
                            try {
                                DBConnector.updateQuery(query);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            } catch (ClassNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            melding.setText("");
                            fields.get(i).setBorder(BorderFactory.createLineBorder(Color.GRAY));
                        } else{
                            melding.setText("ID bestaat niet!");
                        }
                    }
                }
                if(ingevuld) {
                    try {
                        int nieuweRonde = ronde + 1;
                        fields.clear();
                        frame.remove(scroll);
                        tafelindelingPanel.removeAll();
                        showTafelindeling(frame, panel, toernooi_id, nieuweRonde);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                } else{

                }
            }
        });
    }

    public static int getRows(){
        return rows;
    }

    static class ScrollablePanelTafelindeling extends JPanel implements Scrollable {
        public Dimension getPreferredSize() {
            return getPreferredScrollableViewportSize();
        }

        public Dimension getPreferredScrollableViewportSize() {
            if (getParent() == null)
                return getSize();
            return new Dimension(10, Tafelindelingen.getRows() * 360);
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

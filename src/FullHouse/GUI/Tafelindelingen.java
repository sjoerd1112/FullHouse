package FullHouse.GUI;

import FullHouse.Classes.Tafel;
import FullHouse.Classes.Tafelindeling;
import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by sjoer on 7-6-2019.
 */
public class Tafelindelingen {
    private static int rows = 0;
    private static int aantal = 7;

    private static JScrollPane scroll = new JScrollPane();
    private static JTextField zoeken = new JTextField();

    public static void showTafelindeling(JFrame frame, JPanel panel, int toernooi_id) throws SQLException, ClassNotFoundException {
        JPanel tafelindelingPanel = new ScrollablePanelTafelindeling();
        frame.setTitle("Tafelindeling");
        frame.remove(panel);

        String truncateQuery = "TRUNCATE Tafelindeling";
        DBConnector.executeQuery(truncateQuery);

        String query = "SELECT count(id) as aantal FROM Speler JOIN toernooi_inschrijving ON id = toernooi_inschrijving.speler WHERE betaald LIKE 'J' AND aanwezig LIKE 'J' AND  toernooi="+toernooi_id;
        ResultSet rs = DBConnector.query(query);
        int aantalSpelers = 0;
        if(rs.next()){
            aantalSpelers = rs.getInt("aantal");
        }
        ArrayList<Tafel> tafels = Tafelindeling.createTafels(aantalSpelers, "Normaal", 1, 1);
        if(tafels.size()%2==0){
            rows = tafels.size()/2;
        }
        else{
            rows = tafels.size()/2 + 1;
        }

        tafelindelingPanel.setLayout(new FlowLayout(FlowLayout.LEADING,10 ,10));
        JButton terug = new JButton("Terug");
        terug.setPreferredSize(new Dimension(100, 25));
        tafelindelingPanel.add(terug);

        for(int i =0;i<5;i++){
            JLabel leeg = new JLabel();
            leeg.setPreferredSize(new Dimension(100,25));
            tafelindelingPanel.add(leeg);
        }

        for(int i = 0;i<tafels.size();i++){
            Tafel tafel = tafels.get(i);
            DefaultListModel list = new DefaultListModel();
            list.addElement("Tafel "+(i+1));
            int aantal = 0;
            for(int x = 0;x<tafel.getAantal();x++){
                list.addElement(tafel.getSpelers().get(x).getNaam());
                aantal++;
            }
            int resterend = 10-aantal;
            if(resterend>=0) {
                for (int x = 0; x < resterend; x++) {
                    list.addElement(" ");
                }
            }
            JList lijst = new JList(list);
            lijst.setPreferredSize(new Dimension(160,210));
            JPanel listPanel = new JPanel();
            listPanel.setPreferredSize(new Dimension(160,210));
            listPanel.add(lijst);
            tafelindelingPanel.add(listPanel);
        }

        scroll.setViewportView(tafelindelingPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.getContentPane().add(scroll);
        frame.pack();
        frame.setSize(800,250);
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
            return new Dimension(10, Tafelindelingen.getRows() * 305);
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

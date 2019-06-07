package FullHouse.GUI;

import FullHouse.Classes.Tafel;
import FullHouse.Classes.Tafelindeling;
import FullHouse.DB.DBConnector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class Tafelindelingen {

    private static int rows = 0;
    private static int aantal = 7;
    private static boolean created = false;
    private static JScrollPane scroll = new JScrollPane();
    private static JTextField zoeken = new JTextField();

    public static void showTafelindeling(JFrame frame, JPanel panel, int toernooiId) throws SQLException, ClassNotFoundException {
        if(!created) {
            frame.remove(panel);
            frame.setTitle("Tafelindelingen");

            JPanel MasterclassPanel = new ScrollablePanelTafelindeling();

            //zorgen dat er tafels bestaan want tafelindeling zit nog niet aan database
            Tafelindeling.main();
            System.out.println(Tafelindeling.getTafels().size());

            //aantal tafels tellen zodat we weten hoeveel rows we moeten maken
            int aantalRows = 1;
            if( Tafelindeling.getTafels().size() % 2 == 0) {
                aantalRows += (Tafelindeling.getTafels().size() / 2);
            } else  {
                aantalRows += ((Tafelindeling.getTafels().size()+1) / 2);
            }

            //layout
            MasterclassPanel.setLayout(new GridLayout(aantalRows,2, 5, 5));

            //terugknop
            JButton terug = new JButton("Terug");
            MasterclassPanel.add(terug);

            //de combobox zodat we alle 3 de rondes kunnen bekijken, verder nog niks mee gedaan
            String[] rondes = new String[]{"1", "2", "3"};
            JComboBox<String> ronde = new JComboBox(rondes);
            MasterclassPanel.add(ronde);




            //data invoeren
            for(int x = 0; x < Tafelindeling.getTafels().size(); x++) {
                DefaultListModel listModel = new DefaultListModel();
                listModel.addElement("Tafel nr. "+(x+1));
                for(int i = 0; i < Tafelindeling.getTafel(x).getSpelers().size(); i++) {
                    listModel.addElement(Tafelindeling.getTafel(x).getSpelers().get(i));    //.getNaam() werkt niet, eigenlijk moet je ook de database raadplegen hier maar we hebben helemaal geen tabel met tafel en spelers erin dus kunnen we niet per tafel de spelers indelen????
                }
                JList lijst = new JList(listModel);
                lijst.setPreferredSize(new Dimension(300,300));                 //werkt helaas niet :(
                MasterclassPanel.add(lijst);

                aantal++;
                rows++;         //rows of aantal moet omhoog maar geen van beiden doen echt de truc goed
            }


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

    static class ScrollablePanelTafelindeling extends JPanel implements Scrollable {
        public Dimension getPreferredSize() {
            return getPreferredScrollableViewportSize();
        }

        public Dimension getPreferredScrollableViewportSize() {
            if (getParent() == null)
                return getSize();
            return new Dimension(10, Tafelindelingen.getRows() * 105);
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

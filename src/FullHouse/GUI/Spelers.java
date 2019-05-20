package FullHouse.GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Spelers {
    private static int rows = 0;
    private static boolean created = false;
    private static JScrollPane scroll = new JScrollPane();

    public static void showSpelers(JFrame frame, JPanel panel) {
        if(!created) {
            frame.remove(panel);
            frame.setTitle("Spelers Overzicht");

            JPanel spelersPanel = new ScrollablePanel();
            spelersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            JButton terug = new JButton("Terug");
            terug.setPreferredSize(new Dimension(100,25));
            spelersPanel.add(terug);

            for(int i = 0;i<6;i++){
                JLabel label = new JLabel();
                label.setPreferredSize(new Dimension(100,25));
                spelersPanel.add(label);
            }

            int aantal = 1;
            for (int i = 1; i < 111; ++i) {
                JButton p = new JButton();
                p.setPreferredSize(new Dimension(100, 100));
                p.setText("" + i);
                spelersPanel.add(p);
                aantal++;

                int id = aantal;
                p.putClientProperty("id", id);

                p.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(p.getClientProperty("id"));
                    }
                });

                terug.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.remove(scroll);
                        Home.showHome(frame);
                    }
                });
            }
            rows = Math.round(aantal / 7) + 1;

            scroll.setViewportView(spelersPanel);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

            frame.add(scroll);
            frame.pack();
            frame.setSize(800,250);
        }
        else{
            frame.remove(panel);
            frame.getContentPane().add(scroll);
            frame.pack();
            frame.setSize(800,250);

        }
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
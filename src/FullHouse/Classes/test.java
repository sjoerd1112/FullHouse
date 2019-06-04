package FullHouse.Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by sjoer on 30-5-2019.
 */
public class test {
    private static JPanel panel = new JPanel(new FlowLayout());
    private static ArrayList<JCheckBox> list = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        for(int i = 0;i<5;i++){
            JPanel testlabel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            testlabel.add(new JLabel("Naam"));
            JCheckBox box1 = new JCheckBox();
            JCheckBox box2 = new JCheckBox();
            box1.putClientProperty("naam", i+1);
            list.add(box1);
            list.add(box2);
            testlabel.add(box1);
            testlabel.add(box2);
            testlabel.setBorder(BorderFactory.createLineBorder(Color.magenta));
            panel.add(testlabel);
        }
        JButton testbutton = new JButton("Check");
        testbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Check();
            }
        });

        panel.add(testbutton);
        frame.add(panel);
        frame.setSize(800,250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void Check(){
        for(int i = 0;i<list.size();i+=2){
            if(list.get(i).isSelected()==true&&list.get(i+1).isSelected()==true){
                System.out.println("Speler "+list.get(i).getClientProperty("naam")+" doet mee");
            }
            else{
                System.out.println("Speler "+list.get(i).getClientProperty("naam")+" doet niet mee");
            }
        }
    }
}

package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

class MainPanel extends JPanel {
    private final JTextPane label1 = new JTextPane();
    private final JTextArea label2 = new JTextArea();
    private final JLabel    label3 = new JLabel();

    public MainPanel() {
        super(new GridLayout(3,1));
        ImageIcon icon = new ImageIcon(getClass().getResource("wi0124-32.png"));

        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(attr, -0.2f);
        label1.setParagraphAttributes(attr, true);

        String dummyText = "asdfasdfasdfasdfasdfasdfasd";
        label1.setText("JTextPane\n"+dummyText);
        label2.setText("JTextArea\n"+dummyText);
        label3.setText("<html>JLabel+html<br>"+dummyText);
        label3.setIcon(icon);

        add(setLeftIcon(label1, icon));
        add(setLeftIcon(label2, icon));
        add(label3);

        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        setPreferredSize(new Dimension(320, 160));
    }
    private static Box setLeftIcon(JTextComponent label, ImageIcon icon) {
        label.setForeground(UIManager.getColor("Label.foreground"));
        //label.setBackground(UIManager.getColor("Label.background"));
        label.setOpaque(false);
        label.setEditable(false);
        label.setFocusable(false);
        label.setMaximumSize(label.getPreferredSize());
        label.setMinimumSize(label.getPreferredSize());

        JLabel l = new JLabel(icon);
        l.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        Box box = Box.createHorizontalBox();
        box.add(l);
        box.add(Box.createHorizontalStrut(2));
        box.add(label);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
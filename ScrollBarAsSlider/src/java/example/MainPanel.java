package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MainPanel extends JPanel {
    private final JSpinner spinner;
    private final JScrollBar scrollbar;
    private static final int step   = 5;
    private static final int extent = 20;
    private static final int min    = 0;
    private static final int max    = extent*10; //200
    private static final int value  = 50;
    public MainPanel() {
        super(new GridLayout(2,1));
        scrollbar = new JScrollBar(JScrollBar.HORIZONTAL, value, extent, min, max+extent);
        scrollbar.setUnitIncrement(step);
        scrollbar.getModel().addChangeListener(new ChangeListener() {
            @Override public void stateChanged(javax.swing.event.ChangeEvent e) {
                BoundedRangeModel m = (BoundedRangeModel)e.getSource();
                spinner.setValue(m.getValue());
            }
        });

        spinner = new JSpinner(new SpinnerNumberModel(value, min, max, step));
        spinner.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                JSpinner source = (JSpinner)e.getSource();
                Integer iv = (Integer)source.getValue();
                scrollbar.setValue(iv);
            }
        });

        add(makeTitlePanel(spinner, "JSpinner"));
        add(makeTitlePanel(scrollbar, "JScrollBar"));
        setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        setPreferredSize(new Dimension(320, 200));
    }
    private JComponent makeTitlePanel(JComponent cmp, String title) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.insets  = new Insets(5, 5, 5, 5);
        p.add(cmp, c);
        p.setBorder(BorderFactory.createTitledBorder(title));
        return p;
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
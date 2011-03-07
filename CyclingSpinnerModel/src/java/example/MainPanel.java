package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

class MainPanel extends JPanel {
    private final JSpinner spinner01 = new JSpinner();
    private final JSpinner spinner02 = new JSpinner();
    private final JSpinner spinner03 = new JSpinner();
    private final JSpinner spinner04 = new JSpinner();
    private final java.util.List<String> weeks = Arrays.asList("Sun","Mon","Tue","Wed","Thu","Sat");
    public MainPanel() {
        super(new GridLayout(2,1));
        spinner01.setModel(new SpinnerNumberModel(20, 0, 59, 1));
        spinner02.setModel(new SpinnerListModel(weeks));
        spinner03.setModel(new SpinnerNumberModel(20, 0, 59, 1) {
            public Object getNextValue() {
                Object n = super.getNextValue();
                if(n==null) n = getMinimum();
                return n;
            }
            public Object getPreviousValue() {
                Object n = super.getPreviousValue();
                if(n==null) n = getMaximum();
                return n;
            }
        });
        spinner04.setModel(new SpinnerListModel(weeks) {
            public Object getNextValue() {
                Object o = super.getNextValue();
                if(o==null) {
                    o = getList().get(0);
                }
                return o;
            }
            public Object getPreviousValue() {
                Object o = super.getPreviousValue();
                if(o==null) {
                    java.util.List list = getList();
                    o = list.get(list.size()-1);
                }
                return o;
            }
        });
        add(makeTitlePanel("default model", Arrays.asList(spinner01, spinner02)));
        add(makeTitlePanel("cycling model", Arrays.asList(spinner03, spinner04)));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setPreferredSize(new Dimension(320, 200));
    }
    private JComponent makeTitlePanel(String title, java.util.List<? extends JComponent> list) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        GridBagConstraints c = new GridBagConstraints();
        c.fill    = GridBagConstraints.HORIZONTAL;
        c.insets  = new Insets(5, 5, 5, 5);
        c.weightx = 1.0;
        c.gridy   = 0;
        for(JComponent cmp:list) {
            p.add(cmp, c);
            c.gridy++;
        }
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
package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MainPanel extends JPanel{
    private static final String dummy = "Hello";
    private final JTextArea textArea = new JTextArea();
    private final JTextField textField1 = new JTextField(dummy);
    private final JTextField textField2 = new JTextField(dummy);
    private final JTextField textField3 = new JTextField(dummy);
    private final JTextField textField4 = new JTextField(dummy);
    public MainPanel(final JFrame frame) {
        super(new BorderLayout());
        textField3.addHierarchyListener(new HierarchyListener() {
            @Override public void hierarchyChanged(HierarchyEvent e) {
                if((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED)!=0 && textField3.isShowing()) {
                    EventQueue.invokeLater(new Runnable(){
                        @Override public void run() {
                            textField3.requestFocusInWindow();
                        }
                    });
                }
            }
        });
        //http://forums.sun.com/thread.jspa?threadID=777887
        textField4.addAncestorListener(new AncestorListener() {
            @Override public void ancestorAdded(AncestorEvent event) {
                textField4.requestFocusInWindow();
            }
            @Override public void ancestorMoved(AncestorEvent event) {}
            @Override public void ancestorRemoved(AncestorEvent event) {}
        });

        JPanel p = new JPanel(new GridLayout(2,2,5,5));
        p.add(makePanel("Default",           frame, textField1));
        p.add(makePanel2("WindowListener",   frame, textField2));
        p.add(makePanel("HierarchyListener", frame, textField3));
        p.add(makePanel("AncestorListener",  frame, textField4));
        add(p, BorderLayout.NORTH);
        add(new JScrollPane(textArea));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setPreferredSize(new Dimension(320, 180));
    }
    private JPanel makePanel(String title, final JFrame frame, final JTextField textField) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(new JButton(new AbstractAction("show") {
            @Override public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(frame, textField, "Input Text", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if(result==JOptionPane.OK_OPTION) textArea.setText(textField.getText());
            }
        }));
        return p;
    }
    private JPanel makePanel2(String title, final JFrame frame, final JTextField textField) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(new JButton(new AbstractAction("show") {
            @Override public void actionPerformed(ActionEvent e) {
                JOptionPane pane = new JOptionPane(textField, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, null);
                JDialog dialog = pane.createDialog(frame, "Input Text");
                dialog.addWindowListener(new WindowAdapter() {
                    @Override public void windowOpened(WindowEvent e) {
                        textField.requestFocusInWindow();
                    }
                });
                dialog.setVisible(true);
                Object selectedValue = pane.getValue();
                int result = JOptionPane.CLOSED_OPTION;
                if(selectedValue != null && selectedValue instanceof Integer) {
                    result = ((Integer)selectedValue).intValue();
                }
                if(result==JOptionPane.OK_OPTION) textArea.setText(textField.getText());
            }
        }));
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
        frame.getContentPane().add(new MainPanel(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
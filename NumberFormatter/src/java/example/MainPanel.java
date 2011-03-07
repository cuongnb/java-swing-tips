package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.text.*;

public class MainPanel extends JPanel{
    private final JSpinner s0 = new JSpinner(makeSpinnerNumberModel());
    private final JSpinner s1 = new JSpinner(makeSpinnerNumberModel());
    private final JSpinner s2 = makeSpinner2(makeSpinnerNumberModel());
    public MainPanel() {
        super(new GridLayout(3,1));
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor)s1.getEditor();
        DefaultFormatter formatter = (DefaultFormatter) editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        add(makeTitlePanel(s0, "Default"));
        add(makeTitlePanel(s1, "NumberFormatter#setAllowsInvalid(false)"));
        add(makeTitlePanel(s2, "BackgroundColor"));
        setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        setPreferredSize(new Dimension(320, 200));
    }
    private static SpinnerNumberModel makeSpinnerNumberModel() {
        return new SpinnerNumberModel(Long.valueOf(10),    Long.valueOf(0),
                                      Long.valueOf(99999), Long.valueOf(1));
    }
    private static JSpinner makeSpinner2(SpinnerNumberModel m) {
        JSpinner s = new JSpinner(m);
        JSpinner.NumberEditor editor = (JSpinner.NumberEditor)s.getEditor();
        editor.getTextField().setFormatterFactory(makeFFactory(m));
        editor.getTextField().addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                final JTextComponent tc = (JTextComponent)e.getSource();
                EventQueue.invokeLater(new Runnable() {
                    @Override public void run() {
                        tc.setBackground(Color.WHITE);
                    }
                });
            }
        });
        return s;
    }
    private static DefaultFormatterFactory makeFFactory(final SpinnerNumberModel m) { //DecimalFormatSymbols dfs) {
        final NumberFormat format = new DecimalFormat("####0"); //, dfs);
        NumberFormatter displayFormatter = new NumberFormatter(format);
        NumberFormatter editFormatter = new NumberFormatter(format) {
            private final Color color = new Color(255,200,200);
            //protected DocumentFilter getDocumentFilter() {
            //    return new IntegerDocumentFilter();
            //}
            @Override public Object stringToValue(String text) throws ParseException {
                 JFormattedTextField f = getFormattedTextField();
                 try{
                     Long.parseLong(text);
                 }catch(NumberFormatException e) {
                     f.setBackground(color);
                     throw new ParseException("xxx", 0);
                 }
                 Object o = format.parse(text);
                 //System.out.println(o);
                 if(o instanceof Long) {
                     Long val = (Long)format.parse(text);
                     Long max = (Long)m.getMaximum();
                     Long min = (Long)m.getMinimum();
                     if(max.compareTo(val)<0 || min.compareTo(val)>0) {
                         f.setBackground(color);
                         throw new ParseException("xxx", 0);
                     }else{
                         f.setBackground(Color.WHITE);
                     }
                     return val;
                 }
                 throw new ParseException("xxx", 0);
            }
        };
        //editFormatter.setAllowsInvalid(false);
        //editFormatter.setCommitsOnValidEdit(true);
        editFormatter.setValueClass(Long.class);
        return new DefaultFormatterFactory(displayFormatter, displayFormatter, editFormatter);
    }
//     private static JSpinner makeSpinner2(final SpinnerNumberModel m) {
//         JSpinner s = new JSpinner(m);
//         JSpinner.NumberEditor editor = (JSpinner.NumberEditor)s.getEditor();
//         final JFormattedTextField ftf = (JFormattedTextField)editor.getTextField();
//         ftf.setFormatterFactory(makeFFactory2(m));
//         ftf.addFocusListener(new  FocusAdapter() {
//             public void focusLost(final FocusEvent e) {
//                 //JTextComponent textField = (JTextComponent)e.getSource();
//                 System.out.println(ftf.getText());
// //                 try{
// //                     ftf.commitEdit();
// //                 }catch(Exception ex) {
// //                     ex.printStackTrace();
// //                 }
//                 Long value = (Long)m.getValue();
//                 System.out.println(value);
//                 Long max = (Long)m.getMaximum();
//                 Long min = (Long)m.getMinimum();
//                 if(max.compareTo(value)<0) {
//                     m.setValue(max);
//                 }else if(min.compareTo(value)>0) {
//                     m.setValue(min);
//                 }
//             }
//         });
//         return s;
//     }
//     private static DefaultFormatterFactory makeFFactory2(final SpinnerNumberModel m) {
//         NumberFormatter formatter = new NumberFormatter(new DecimalFormat("########0"));
//         formatter.setAllowsInvalid(false);
//         formatter.setCommitsOnValidEdit(true);
//         formatter.setValueClass(Long.class);
//         return new DefaultFormatterFactory(formatter);
//     }
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
// class IntegerDocumentFilter extends DocumentFilter {
//     @Override
//     public void insertString(DocumentFilter.FilterBypass fb,
//                              int offset, String string, AttributeSet attr) throws BadLocationException {
//         if(string == null) {
//             return;
//         }else{
//             replace(fb, offset, 0, string, attr);
//         }
//     }
//     @Override
//     public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
//     throws BadLocationException {
//         replace(fb, offset, length, "", null);
//     }
//     @Override
//     public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
//                         String text, AttributeSet attrs) throws BadLocationException {
//         Document doc = fb.getDocument();
//         int currentLength = doc.getLength();
//         String currentContent = doc.getText(0, currentLength);
//         String before = currentContent.substring(0, offset);
//         String after = currentContent.substring(length+offset, currentLength);
//         String newValue = before + (text == null ? "" : text) + after;
//         //currentValue =
//         checkInput(newValue, offset);
//         fb.replace(offset, length, text, attrs);
//     }
//     private static int checkInput(String proposedValue, int offset)
//     throws BadLocationException {
//         int newValue = 0;
//         if(proposedValue.length() > 0) {
//             try{
//                 newValue = Integer.parseInt(proposedValue);
//             }catch(NumberFormatException e) {
//                 throw new BadLocationException(proposedValue, offset);
//             }
//         }
//         return newValue;
//     }
// }
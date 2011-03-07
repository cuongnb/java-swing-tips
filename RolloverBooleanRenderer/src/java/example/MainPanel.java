package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.table.*;

public class MainPanel extends JPanel {
    public JComponent makeUI() {
        String[] columnNames = {"String", "Integer", "Boolean"};
        Object[][] data = {
            {"aaa", 12, true}, {"bbb", 5, false},
            {"CCC", 92, true}, {"DDD", 0, false}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        JTable table = new JTable(model) {
            @Override public Component prepareEditor(TableCellEditor editor, int row, int column) {
                Component c = super.prepareEditor(editor, row, column);
                if(c instanceof JCheckBox) {
                    ((JCheckBox)c).setBackground(getSelectionBackground());
                }
                return c;
            }
        };
        table.setAutoCreateRowSorter(true);

        HighlightListener highlighter = new HighlightListener(table);
        table.addMouseListener(highlighter);
        table.addMouseMotionListener(highlighter);

        table.setDefaultRenderer(Object.class,  new RolloverDefaultTableCellRenderer(highlighter));
        table.setDefaultRenderer(Number.class,  new RolloverNumberRenderer(highlighter));
        table.setDefaultRenderer(Boolean.class, new RolloverBooleanRenderer(highlighter));

        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                       new JScrollPane(new JTable(model)),
                                       new JScrollPane(table));
        sp.setResizeWeight(0.5);
        return sp;
    }
    public MainPanel() {
        super(new BorderLayout());
        add(makeUI());
        setPreferredSize(new Dimension(320, 240));
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

class HighlightListener extends MouseAdapter {
    private int row = -1;
    private int col = -1;
    private final JTable table;
    public HighlightListener(JTable table) {
        this.table = table;
    }
    public boolean isHighlightableCell(int row, int column) {
        return this.row==row && this.col==column;
    }
    @Override public void mouseMoved(MouseEvent e) {
        Point pt = e.getPoint();
        row = table.rowAtPoint(pt);
        col = table.columnAtPoint(pt);
        if(row<0 || col<0) row = col = -1;
        table.repaint();
    }
    @Override public void mouseExited(MouseEvent e) {
        row = col = -1;
        table.repaint();
    }
}

class RolloverDefaultTableCellRenderer extends DefaultTableCellRenderer {
    private static final Color highlight = new Color(255, 150, 50);
    private final HighlightListener highlighter;
    public RolloverDefaultTableCellRenderer(HighlightListener highlighter) {
        super();
        this.highlighter = highlighter;
    }
    @Override public Component getTableCellRendererComponent(JTable table, Object value,
                                                             boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(highlighter.isHighlightableCell(row, column)) {
            setText("<html><u>"+value.toString());
            setForeground(isSelected?table.getSelectionForeground():highlight);
            setBackground(isSelected?table.getSelectionBackground().darker():table.getBackground());
        }else{
            setText(value.toString());
            setForeground(isSelected?table.getSelectionForeground():table.getForeground());
            setBackground(isSelected?table.getSelectionBackground():table.getBackground());
        }
        return this;
    }
}

class RolloverNumberRenderer extends RolloverDefaultTableCellRenderer {
    public RolloverNumberRenderer(HighlightListener highlighter) {
        super(highlighter);
        setHorizontalAlignment(JLabel.RIGHT);
    }
}

class RolloverBooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource {
    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    private final HighlightListener highlighter;
    public RolloverBooleanRenderer(HighlightListener highlighter) {
        super();
        this.highlighter = highlighter;
        setHorizontalAlignment(JLabel.CENTER);
        setBorderPainted(true);
        setRolloverEnabled(true);
        setOpaque(true);
    }
    @Override public Component getTableCellRendererComponent(JTable table, Object value,
                                                             boolean isSelected, boolean hasFocus, int row, int column) {
        getModel().setRollover(highlighter.isHighlightableCell(row, column));

        if(isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        }else{
            setForeground(table.getForeground());
            setBackground(table.getBackground());
            //setBackground(row%2==0?table.getBackground():Color.WHITE); //Nimbus
        }
        setSelected((value != null && ((Boolean)value).booleanValue()));

        if(hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        }else{
            setBorder(noFocusBorder);
        }
        return this;
    }
    //Overridden for performance reasons. ---->
    @Override public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();
        if(p != null) {
            p = p.getParent();
        } // p should now be the JTable.
        boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
        return !colorMatch && super.isOpaque();
    }
    @Override protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
//         System.out.println(propertyName);
//         if(propertyName=="border" || ((propertyName == "font" || propertyName == "foreground") && oldValue != newValue)) {
//             super.firePropertyChange(propertyName, oldValue, newValue);
//         }
    }
    @Override public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
    @Override public void repaint(long tm, int x, int y, int width, int height) {}
    @Override public void repaint(Rectangle r) {}
    @Override public void repaint() {}
    @Override public void invalidate() {}
    @Override public void validate() {}
    @Override public void revalidate() {}
    //<---- Overridden for performance reasons.
}
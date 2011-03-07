package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class MainPanel extends JPanel{
    private static int BOOLEAN_COLUMN = 2;
    private static String[] columnNames = {
        "String", "Number", "Boolean"
    };
    private static Object[][] data = {
        { "aaa",1,false}, { "bbb",20,false},
        { "ccc",2,false}, { "ddd",3, false},
        { "aaa",1,false}, { "bbb",20,false},
        { "ccc",2,false}, { "ddd",3, false},
    };
    private final DefaultTableModel model = new DefaultTableModel(data, columnNames) {
        @Override public Class<?> getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }
        @Override public boolean isCellEditable(int row, int col) {
            return col == BOOLEAN_COLUMN;
        }
    };
    public MainPanel() {
        super(new BorderLayout());
        final JTable table = makeTable1(model);
        //final JTable table = makeTable2(model);
        model.addTableModelListener(new TableModelListener() {
            @Override public void tableChanged(TableModelEvent e) {
                if(e.getType()==TableModelEvent.UPDATE) {
                    //System.out.println("TableModel: tableChanged");
                    rowRepaint(table, table.convertRowIndexToView(e.getFirstRow()));
                }
            }
        });
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension());
        table.setRowSelectionAllowed(true);
        //table.setSurrendersFocusOnKeystroke(true);
        //table.putClientProperty("JTable.autoStartsEdit", false);
        add(new JScrollPane(table));
        setPreferredSize(new Dimension(320, 200));
    }

    private static JTable makeTable1(final DefaultTableModel model) {
        return new JTable(model) {
            @Override public Component prepareEditor(TableCellEditor editor, int row, int column) {
                Component cmp = super.prepareEditor(editor, row, column);
                if(convertColumnIndexToModel(column) == BOOLEAN_COLUMN) {
                    //System.out.println("JTable: prepareEditor");
                    JCheckBox c = (JCheckBox)cmp;
                    c.setBackground(c.isSelected()?Color.ORANGE:getBackground());
                }
                return cmp;
            }
            @Override public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                Boolean isChecked = (Boolean) model.getValueAt(
                    convertRowIndexToModel(row), BOOLEAN_COLUMN);
                c.setForeground(getForeground());
                c.setBackground(isChecked?Color.ORANGE:getBackground());
                return c;
            }
        };
    }
//     private static JTable makeTable2(final DefaultTableModel model) {
//         final JTable table = new JTable(model);
//         TableColumnModel columns = table.getColumnModel();
//         for(int i=0;i<columns.getColumnCount();i++) {
//             columns.getColumn(i).setCellRenderer(new RowColorTableRenderer(
//                 table.getDefaultRenderer(model.getColumnClass(i))));
//         }
//         return table;
//     }
//     static class RowColorTableRenderer implements TableCellRenderer {
//         private final Color evenColor = new Color(240, 240, 255);
//         private final TableCellRenderer renderer;
//         public RowColorTableRenderer(TableCellRenderer renderer) {
//             this.renderer = renderer;
//         }
//         @Override public Component getTableCellRendererComponent(JTable table, Object value,
//                     boolean isSelected, boolean hasFocus, int row, int column) {
//             Component c = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//             TableModel model = table.getModel();
//             Boolean isChecked = (Boolean) model.getValueAt(table.convertRowIndexToModel(row), BOOLEAN_COLUMN);
//             c.setForeground(table.getForeground());
//             c.setBackground(isChecked?Color.ORANGE:table.getBackground());
//             return c;
//         }
//     }
    private static void rowRepaint(JTable table, int row) {
        Rectangle r = table.getCellRect(row, 0, true);
        //r.height = table.getRowHeight();
        r.width  = table.getWidth();
        table.repaint(r);
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
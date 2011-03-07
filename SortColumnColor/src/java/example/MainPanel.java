package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        super(new BorderLayout());
        TestModel model = new TestModel();
        JTable table = new JTable(model) {
            private final Color evenColor = new Color(250, 230, 230);
            @Override public Component prepareRenderer(TableCellRenderer tcr, int row, int column) {
                Component c = super.prepareRenderer(tcr, row, column);
                if(isRowSelected(row)) {
                    c.setForeground(getSelectionForeground());
                    c.setBackground(getSelectionBackground());
                }else{
                    c.setForeground(getForeground());
                    c.setBackground(isSortingColumn(column)?
                                  evenColor:getBackground());
                }
                return c;
            }
            private boolean isSortingColumn(int column) {
                RowSorter sorter = getRowSorter();
                if(sorter!=null) {
                    java.util.List list = sorter.getSortKeys();
                    if(list.size()>0) {
                        RowSorter.SortKey key0 = (RowSorter.SortKey)list.get(0);
                        if(column==convertColumnIndexToView(key0.getColumn())) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
        table.setRowSorter(sorter);

        model.addTest(new Test("Name 1", "comment..."));
        model.addTest(new Test("Name 2", "Test"));
        model.addTest(new Test("Name d", ""));
        model.addTest(new Test("Name c", "Test cc"));
        model.addTest(new Test("Name b", "Test bb"));
        model.addTest(new Test("Name a", ""));
        model.addTest(new Test("Name 0", "Test aa"));

        TableColumn col = table.getColumnModel().getColumn(0);
        col.setMinWidth(60);
        col.setMaxWidth(60);
        col.setResizable(false);

        add(new JButton(new AbstractAction("clear SortKeys") {
            @Override public void actionPerformed(ActionEvent e) {
                sorter.setSortKeys(null);
            }
        }), BorderLayout.SOUTH);
        add(new JScrollPane(table));
        setPreferredSize(new Dimension(320, 200));
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
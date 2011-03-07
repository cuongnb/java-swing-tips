package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

public class MainPanel extends JPanel {
    private final JTree tree = new JTree();
    public MainPanel() {
        super(new BorderLayout());
        tree.setModel(makeModel());
        tree.setCellRenderer(new RollOverTreeCellRenderer(tree, tree.getCellRenderer()));
        add(new JScrollPane(tree));
        setPreferredSize(new Dimension(320, 200));
    }
    private static DefaultTreeModel makeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode set1 = new DefaultMutableTreeNode("Set 001");
        DefaultMutableTreeNode set2 = new DefaultMutableTreeNode("Set 002");
        DefaultMutableTreeNode set3 = new DefaultMutableTreeNode("Set 003");
        set1.add(new DefaultMutableTreeNode("111111111"));
        set1.add(new DefaultMutableTreeNode("22222222222"));
        set1.add(new DefaultMutableTreeNode("33333"));
        set2.add(new DefaultMutableTreeNode("asdfasdfas"));
        set2.add(new DefaultMutableTreeNode("asdf"));
        set3.add(new DefaultMutableTreeNode("asdfasdfasdf"));
        set3.add(new DefaultMutableTreeNode("qwerqwer"));
        set3.add(new DefaultMutableTreeNode("zvxcvzxcvzxzxcvzxcv"));
        root.add(set1);
        root.add(set2);
        set2.add(set3);
        return new DefaultTreeModel(root);
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

class RollOverTreeCellRenderer extends DefaultTreeCellRenderer implements MouseMotionListener {
    private static final Color rollOverRowColor = new Color(220,240,255);
    private final JTree tree;
    private final TreeCellRenderer renderer;
    public RollOverTreeCellRenderer(JTree tree, TreeCellRenderer renderer) {
        this.tree = tree;
        this.renderer = renderer;
        tree.addMouseMotionListener(this);
    }
    @Override public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JComponent c = (JComponent)renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        if(row==rollOverRowIndex) {
            c.setOpaque(true);
            c.setBackground(rollOverRowColor);
            if(isSelected) c.setForeground(getTextNonSelectionColor());
        }else{
            c.setOpaque(false);
        }
        return c;
    }
    private int rollOverRowIndex = -1;
    @Override public void mouseMoved(MouseEvent e) {
        int row = tree.getRowForLocation(e.getX(), e.getY());
        if(row!=rollOverRowIndex) {
            //System.out.println(row);
            rollOverRowIndex = row;
            tree.repaint();
        }
    }
    @Override public void mouseDragged(MouseEvent e) {}
}
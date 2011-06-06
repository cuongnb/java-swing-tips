package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class MainPanel extends JPanel {
    public MainPanel() {
        super(new BorderLayout());
        JTree tree = new JTree();
        //tree.setRootVisible(false);
        tree.setComponentPopupMenu(new TreePopupMenu());
        add(new JScrollPane(tree));
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

class TreePopupMenu extends JPopupMenu {
    private final JTextField textField = new JTextField(24);
    private TreePath path;
    public TreePopupMenu() {
        super();
        textField.addAncestorListener(new AncestorListener() {
            @Override public void ancestorAdded(AncestorEvent e) {
                textField.requestFocusInWindow();
            }
            @Override public void ancestorMoved(AncestorEvent event) {}
            @Override public void ancestorRemoved(AncestorEvent e) {}
        });
        add(new AbstractAction("add") {
            @Override public void actionPerformed(ActionEvent e) {
                JTree tree = (JTree)getInvoker();
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode)path.getLastPathComponent();
                DefaultMutableTreeNode child  = new DefaultMutableTreeNode("New node");
                //model.insertNodeInto(child, parent, 0);
                parent.add(child);
                model.nodeStructureChanged(parent);
                tree.expandPath(path);
            }
        });
        add(new JMenuItem(new AbstractAction("edit") {
            public void actionPerformed(ActionEvent e) {
                JTree tree = (JTree)getInvoker();
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                //if(path==null) return;
                Object node = path.getLastPathComponent();
                if(node instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode leaf = (DefaultMutableTreeNode)node;
                    textField.setText(leaf.getUserObject().toString());
                    int result = JOptionPane.showConfirmDialog(
                        tree, textField, "edit",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if(result==JOptionPane.OK_OPTION) {
                        String str = textField.getText();
                        if(!str.trim().isEmpty()) {
                            model.valueForPathChanged(path, str);
                            //leaf.setUserObject(str);
                            //model.nodeChanged(leaf);
                        }
                    }
                }
            }
        }));
        addSeparator();
        add(new AbstractAction("remove") {
            @Override public void actionPerformed(ActionEvent e) {
                JTree tree = (JTree)getInvoker();
                DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
                //if(path.getParentPath()!=null) {
                if(!node.isRoot()) {
                    model.removeNodeFromParent(node);
                }
            }
        });
    }
    @Override public void show(Component c, int x, int y) {
        JTree tree = (JTree)c;
        TreePath[] tsp = tree.getSelectionPaths();
        if(tsp!=null) {
            path = tree.getPathForLocation(x, y);
            if(path!=null && Arrays.asList(tsp).contains(path)) {
                super.show(c, x, y);
            }
        }
    }
}
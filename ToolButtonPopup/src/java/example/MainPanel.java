package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import javax.swing.event.*;

class MainPanel extends JPanel{
    private final JToolBar toolbar = new JToolBar("toolbar");
    private final JPopupMenu pop1  = new JPopupMenu();
    private final JPopupMenu pop2  = new JPopupMenu();
    private final URL url = getClass().getResource("ei0021-16.png");
    private final Component rigid = Box.createRigidArea(new Dimension(5,5));
    public MainPanel() {
        super(new BorderLayout());
        pop1.add("000");
        pop1.add("11111");
        pop1.addSeparator();
        pop1.add("2222222");
        pop2.add("33333333333333");
        pop2.addSeparator();
        pop2.add("4444");
        pop2.add("5555555555");

        toolbar.add(makeButton(pop1, "Text", null));
        toolbar.add(rigid);
        toolbar.add(makeButton(pop2, "", new ImageIcon(url)));
        toolbar.add(rigid);
        toolbar.add(makeButton(pop2, "Icon+Text", new ImageIcon(url)));
        toolbar.add(Box.createGlue());

        add(toolbar, BorderLayout.NORTH);
        setPreferredSize(new Dimension(320, 200));
    }

    private AbstractButton makeButton(JPopupMenu pop, String title, ImageIcon icon) {
        MenuToggleButton b = new MenuToggleButton(title, icon);
        b.setPopupMenu(pop);
        return b;
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
class MenuArrowIcon implements Icon {
    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(Color.BLACK);
        g2.translate(x,y);
        g2.drawLine( 2, 3, 6, 3 );
        g2.drawLine( 3, 4, 5, 4 );
        g2.drawLine( 4, 5, 4, 5 );
        g2.translate(-x,-y);
    }
    @Override public int getIconWidth()  { return 9; }
    @Override public int getIconHeight() { return 9; }
}
class MenuToggleButton extends JToggleButton {
    private static final Icon i = new MenuArrowIcon();
    public MenuToggleButton() {
        this("", null);
    }
    public MenuToggleButton(Icon icon) {
        this("", icon);
    }
    public MenuToggleButton(String text) {
        this(text, null);
    }
    public MenuToggleButton(String text, Icon icon) {
        super();
        Action a = new AbstractAction(text) {
            @Override public void actionPerformed(ActionEvent ae) {
                MenuToggleButton b = (MenuToggleButton)ae.getSource();
                if(pop!=null) pop.show(b, 0, b.getHeight());
            }
        };
        a.putValue(Action.SMALL_ICON, icon);
        setAction(a);
        setFocusable(false);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4+i.getIconWidth()));
    }
    protected JPopupMenu pop;
    public void setPopupMenu(final JPopupMenu pop) {
        this.pop = pop;
        pop.addPopupMenuListener(new PopupMenuListener() {
            @Override public void popupMenuCanceled(PopupMenuEvent e) {}
            @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
            @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                setSelected(false);
            }
        });
    }
    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = getSize();
        Insets ins = getInsets();
        int x = dim.width-ins.right;
        int y = ins.top+(dim.height-ins.top-ins.bottom-i.getIconHeight())/2;
        i.paintIcon(this, g, x, y);
    }
}
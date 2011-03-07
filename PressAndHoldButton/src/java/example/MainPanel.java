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
    private final URL url = getClass().getResource("ei0021-16.png");
    private final Component rigid = Box.createRigidArea(new Dimension(5,5));
    public MainPanel() {
        super(new BorderLayout());
        toolbar.add(new PressAndHoldButton("", new ImageIcon(url)));
        add(toolbar, BorderLayout.NORTH);
        add(new JLabel("press and hold the button for 1000 milliseconds"));
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
class PressAndHoldButton extends JToggleButton {
    private static final Icon i = new MenuArrowIcon();
    private final ButtonGroup bg = new ButtonGroup();
    private final JPopupMenu pop = new JPopupMenu();
    public PressAndHoldButton() {
        this("", null);
    }
    public PressAndHoldButton(Icon icon) {
        this("", icon);
    }
    public PressAndHoldButton(String text) {
        this(text, null);
    }
    public PressAndHoldButton(String text, Icon icon) {
        super();
        pop.setLayout(new GridLayout(0,3));
        for(MenuContext m: makeIconList()) {
            AbstractButton b = new JRadioButton();
            b.setBorder(BorderFactory.createEmptyBorder());
            //b.setAction(m.action);
            b.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    System.out.println(bg.getSelection().getActionCommand());
                    pop.setVisible(false);
                }
            });
            b.setIcon(m.small);
            b.setRolloverIcon(m.rollover);
            b.setSelectedIcon(m.rollover);
            b.setActionCommand(m.command);
            b.setFocusable(false);
            b.setPreferredSize(new Dimension(m.small.getIconWidth(),
                                             m.small.getIconHeight()));
            pop.add(b);
            bg.add(b);
            b.setSelected(true);
        }
        ArrowButtonHandler handler = new ArrowButtonHandler();
        handler.putValue(Action.NAME, text);
        handler.putValue(Action.SMALL_ICON, icon);
        setAction(handler);
        addMouseListener(handler);
        setFocusable(false);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4+i.getIconWidth()));
    }
    private java.util.List<MenuContext> makeIconList() {
        return java.util.Arrays.asList(
            new MenuContext("BLUE",    Color.BLUE),
            new MenuContext("CYAN",    Color.CYAN),
            new MenuContext("GREEN",   Color.GREEN),
            new MenuContext("MAGENTA", Color.MAGENTA),
            new MenuContext("ORANGE",  Color.ORANGE),
            new MenuContext("PINK",    Color.PINK),
            new MenuContext("RED",     Color.RED),
            new MenuContext("YELLOW",  Color.YELLOW));
    }
    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = getSize();
        Insets ins = getInsets();
        int x = dim.width-ins.right;
        int y = ins.top+(dim.height-ins.top-ins.bottom-i.getIconHeight())/2;
        i.paintIcon(this, g, x, y);
    }
    private class ArrowButtonHandler extends AbstractAction implements MouseListener {
        private final javax.swing.Timer autoRepeatTimer;
        private AbstractButton arrowButton = null;
        public ArrowButtonHandler() {
            autoRepeatTimer = new javax.swing.Timer(1000, new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    System.out.println("InitialDelay(1000)");
                    if(arrowButton!=null && arrowButton.getModel().isPressed() && autoRepeatTimer.isRunning()) {
                        autoRepeatTimer.stop();
                        pop.show(arrowButton, 0, arrowButton.getHeight());
                        pop.requestFocusInWindow();
                    }
                }
            });
            autoRepeatTimer.setInitialDelay(1000);
            pop.addPopupMenuListener(new PopupMenuListener() {
                @Override public void popupMenuCanceled(PopupMenuEvent e) {}
                @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
                @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    if(arrowButton!=null) {
                        arrowButton.setSelected(false);
                    }
                }
            });
        }
        @Override public void actionPerformed(ActionEvent e) {
            if(autoRepeatTimer.isRunning()) {
                System.out.println("actionPerformed");
                System.out.println("    "+bg.getSelection().getActionCommand());
                if(arrowButton!=null) arrowButton.setSelected(false);
                autoRepeatTimer.stop();
            }
        }
        @Override public void mousePressed(MouseEvent e) {
            System.out.println("mousePressed");
            if(SwingUtilities.isLeftMouseButton(e) && e.getComponent().isEnabled()) {
                arrowButton = (AbstractButton)e.getSource();
                autoRepeatTimer.start();
            }
        }
        @Override public void mouseReleased(MouseEvent e) {
            autoRepeatTimer.stop();
        }
        @Override public void mouseExited(MouseEvent e) {
            if(autoRepeatTimer.isRunning()) {
                autoRepeatTimer.stop();
            }
        }
        @Override public void mouseEntered(MouseEvent e) {}
        @Override public void mouseClicked(MouseEvent e) {}
    }
}
class MenuContext {
    public final String command;
    public final Icon small;
    public final Icon rollover;
    public MenuContext(String cmd, Color c) {
        command = cmd;
        small = new DummyIcon(c);
        rollover = new DummyIcon2(c);
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
class DummyIcon implements Icon {
    private final Color color;
    public DummyIcon(Color color) {
        this.color = color;
    }
    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(color);
        g2.translate(x,y);
        g2.fillOval( 4, 4, 16, 16 );
        g2.translate(-x,-y);
    }
    @Override public int getIconWidth()  {
        return 24;
    }
    @Override public int getIconHeight() {
        return 24;
    }
}
class DummyIcon2 extends DummyIcon {
    public DummyIcon2(Color color) {
        super(color);
    }
    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
        super.paintIcon(c,g,x,y);
        Graphics2D g2 = (Graphics2D)g;
        g2.setPaint(Color.BLACK);
        g2.translate(x,y);
        g2.drawOval( 4, 4, 16, 16 );
        g2.translate(-x,-y);
    }
}
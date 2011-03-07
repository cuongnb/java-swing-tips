package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;

public class MainPanel extends JPanel {
    String str = "icon.addMouseListener(new MouseAdapter() {\n"+
                 "  public void mouseClicked(MouseEvent e) {\n"+
                 "    if(e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {\n"+
                 "      frame.setVisible(true);\n"+
                 "    }else if(frame.isVisible()) {\n"+
                 "      frame.setExtendedState(JFrame.NORMAL);\n"+
                 "      frame.toFront();\n"+
                 "    }\n"+
                 "  }\n"+
                 "});\n";
    public MainPanel() {
        super(new BorderLayout());
        add(new JScrollPane(new JTextArea(str)));
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
        final JFrame frame = new JFrame("@title@");
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if(!SystemTray.isSupported()) {
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            return;
        }
        Image image = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
        new StarIcon().paintIcon(null, image.getGraphics(), 0, 0);

        final SystemTray tray = SystemTray.getSystemTray();
        PopupMenu popup       = new PopupMenu();
        MenuItem open         = new MenuItem("Option");
        MenuItem exit         = new MenuItem("Exit");
        final TrayIcon icon   = new TrayIcon(image, "Click Test", popup);
        popup.add(open);
        popup.add(exit);
        icon.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
                    frame.setVisible(true);
                }else if(frame.isVisible()) {
                    frame.setExtendedState(JFrame.NORMAL);
                    frame.toFront();
                }
            }
        });
        open.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                tray.remove(icon);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                //frame.dispose();
                frame.getToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        try{
            tray.add(icon);
        }catch(AWTException e) {
            e.printStackTrace();
        }
    }
}
class StarIcon implements Icon{
    private final Shape star;
    public StarIcon() {
        star = makeStar(8,4,5);
    }
    private Path2D.Double makeStar(int r1, int r2, int vc) {
        int or = Math.max(r1, r2);
        int ir = Math.min(r1, r2);
        double agl = 0.0;
        double add = 2*Math.PI/(vc*2);
        Path2D.Double p = new Path2D.Double();
        p.moveTo(or*1, or*0);
        for(int i=0;i<vc*2-1;i++) {
            agl+=add;
            int r = i%2==0?ir:or;
            p.lineTo(r*Math.cos(agl), r*Math.sin(agl));
        }
        p.closePath();
        AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2,or,0);
        return new Path2D.Double(p, at);
    }
    @Override public int getIconWidth() {
        return star.getBounds().width;
    }
    @Override public int getIconHeight() {
        return star.getBounds().height;
    }
    @Override public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(x, y);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.PINK);
        g2d.fill(star);
        //g2d.setPaint(Color.BLACK);
        //g2d.draw(star);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.translate(-x, -y);
    }
}
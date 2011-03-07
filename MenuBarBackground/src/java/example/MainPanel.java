package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class MainPanel extends JPanel{
    public MainPanel(JFrame frame) {
        super();
        frame.setJMenuBar(createMenubar());
        setPreferredSize(new Dimension(320, 180));
    }
    public JMenuBar createMenubar() {
        JMenuBar mb = new JMenuBar() {
            private final TexturePaint texture = makeCheckerTexture();
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g;
                g2.setPaint(texture);
                g2.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        mb.setOpaque(false);
        String[] menuKeys = {"File", "Edit", "Help"};
        for(String key: menuKeys) {
            JMenu m = createMenu(key);
            if(m != null) mb.add(m);
        }
        return mb;
    }
    private JMenu createMenu(String key) {
        JMenu menu = new JMenu(key) {
            @Override protected void fireStateChanged() {
                ButtonModel m = getModel();
                if(m.isPressed() && m.isArmed()) {
                    setOpaque(true);
                }else if(m.isSelected()) {
                    setOpaque(true);
                }else if(isRolloverEnabled() && m.isRollover()) {
                    setOpaque(true);
                }else{
                    setOpaque(false);
                }
                super.fireStateChanged();
            };
        };
        menu.setBackground(new Color(0,0,0,0)); //XXX windows lnf?
        menu.add("dummy1"); menu.add("dummy2"); menu.add("dummy3");
        return menu;
    }
    private TexturePaint makeCheckerTexture() {
        int cs = 6;
        int sz = cs*cs;
        BufferedImage img = new BufferedImage(sz,sz,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = img.createGraphics();
        g2.setPaint(new Color(200,150,100,50));
        g2.fillRect(0,0,sz,sz);
        for(int i=0;i*cs<sz;i++) {
            for(int j=0;j*cs<sz;j++) {
                if((i+j)%2==0) g2.fillRect(i*cs, j*cs, cs, cs);
            }
        }
        g2.dispose();
        return new TexturePaint(img, new Rectangle(0,0,sz,sz));
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
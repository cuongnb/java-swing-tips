package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class BasicTabViewButtonUI extends TabViewButtonUI {
    private final static TabViewButtonUI tabViewButtonUI = new BasicTabViewButtonUI();
    protected TabButton tabViewButton;

    public static ComponentUI createUI(JComponent c) {
        return new BasicTabViewButtonUI();
    }
    @Override public void installUI(JComponent c) {
        super.installUI(c);
        this.tabViewButton = (TabButton)c;
        tabViewButton.setRolloverEnabled(true);
        c.setPreferredSize(new Dimension(0, 24));
        c.setOpaque(true);
        Border out = BorderFactory.createMatteBorder(2,0,0,0,c.getBackground());
        Border in  = BorderFactory.createMatteBorder(1,1,0,1,Color.RED);
        c.setBorder(BorderFactory.createCompoundBorder(out, in));
        //c.setForeground(Color.GREEN);
    }
    @Override public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        this.tabViewButton = null;
    }
//     public void installDefaults() {}

    private static Dimension size = new Dimension();
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    @Override public synchronized void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = c.getFontMetrics(f);

        Insets i = c.getInsets();
        size = b.getSize(size);
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = size.width - (i.right + viewRect.x);
        viewRect.height = size.height - (i.bottom + viewRect.y);
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
        textRect.x = textRect.y = textRect.width = textRect.height = 0;

        String text = SwingUtilities.layoutCompoundLabel(
            c, fm, b.getText(), null, //altIcon != null ? altIcon : getDefaultIcon(),
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect,
            0); //b.getText() == null ? 0 : b.getIconTextGap());

        g.setColor(b.getBackground());
        g.fillRect(0,0, size.width, size.height);
        if(text==null) return;
        if(model.isSelected() || model.isArmed()) {
            g.setColor(Color.WHITE);
        }else{
            g.setColor(new Color(220,220,220));
        }
        g.fillRect(viewRect.x, viewRect.y,
                   viewRect.x+viewRect.width, viewRect.y+viewRect.height);

        Color color = new Color(255,120,40);
        if(model.isSelected()) {
            g.setColor(color);
            g.drawLine(viewRect.x+1, viewRect.y-2, viewRect.x+viewRect.width-1, viewRect.y-2);
            g.setColor(color.brighter());
            g.drawLine(viewRect.x+0, viewRect.y-1, viewRect.x+viewRect.width-0, viewRect.y-1);
            g.setColor(color);
            g.drawLine(viewRect.x+0, viewRect.y-0, viewRect.x+viewRect.width-0, viewRect.y-0);
        }else if(model.isRollover()) {
            g.setColor(color);
            g.drawLine(viewRect.x+1, viewRect.y+0, viewRect.x+viewRect.width-1, viewRect.y+0);
            g.setColor(color.brighter());
            g.drawLine(viewRect.x+0, viewRect.y+1, viewRect.x+viewRect.width-0, viewRect.y+1);
            g.setColor(color);
            g.drawLine(viewRect.x+0, viewRect.y+2, viewRect.x+viewRect.width-0, viewRect.y+2);
        }
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if(v!=null) {
            v.paint(g, textRect);
        }else{
            if(model.isSelected()) {
                textRect.y -= 2;
                textRect.x -= 1;
            }
            textRect.x += 4;
            paintText(g, b, textRect, text);
        }
    }
}

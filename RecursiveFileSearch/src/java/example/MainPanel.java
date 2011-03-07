package example;
//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

public class MainPanel extends JPanel {
    private final JFileChooser fileChooser = new JFileChooser();
    private final JTextArea textArea = new JTextArea();
    private final JComboBox dirCombo = new JComboBox();
    private final JProgressBar pBar  = new JProgressBar();
    private final JPanel statusPanel = new JPanel(new BorderLayout());
    private final JButton runButton  = new JButton(new RunAction());
    private final JButton canButton  = new JButton(new CancelAction());
    private final JButton openButton = new JButton(new OpenAction());
    private SwingWorker<String, Message>  worker;

    public MainPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement(System.getProperty("user.dir"));
        dirCombo.setModel(model);
        dirCombo.setFocusable(false);
        textArea.setEditable(false);

        JPanel box1 = new JPanel(new BorderLayout(5, 5));
        box1.add(new JLabel("Search folder:"), BorderLayout.WEST);
        box1.add(dirCombo);
        box1.add(openButton, BorderLayout.EAST);

        Box box2 = Box.createHorizontalBox();
        box2.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        box2.add(Box.createHorizontalGlue());
        box2.add(runButton);
        box2.add(Box.createHorizontalStrut(2));
        box2.add(canButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(box1, BorderLayout.NORTH);
        panel.add(box2, BorderLayout.SOUTH);

        add(new JScrollPane(textArea));
        add(panel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(320, 240));
    }
    public static void addItem(JComboBox dirCombo, String str, int max) {
        if(str==null || str.trim().isEmpty()) return;
        dirCombo.setVisible(false);
        DefaultComboBoxModel model = (DefaultComboBoxModel) dirCombo.getModel();
        model.removeElement(str);
        model.insertElementAt(str, 0);
        if(model.getSize()>max) {
            model.removeElementAt(max);
        }
        dirCombo.setSelectedIndex(0);
        dirCombo.setVisible(true);
    }
    class RunAction extends AbstractAction{
        public RunAction() {
            super("Run");
        }
        @Override public void actionPerformed(ActionEvent evt) {
            addItem(dirCombo, (String)dirCombo.getEditor().getItem(), 4);
            statusPanel.removeAll();
            statusPanel.add(pBar);
            statusPanel.revalidate();
            dirCombo.setEnabled(false);
            openButton.setEnabled(false);
            runButton.setEnabled(false);
            canButton.setEnabled(true);
            pBar.setIndeterminate(true);
            textArea.setText("");
            worker = new SwingWorker<String, Message>() {
                @Override public String doInBackground() {
                    File dir = new File((String)dirCombo.getSelectedItem());
                    if(dir==null || !dir.exists()) {
                        publish(new Message("The directory does not exist.",true));
                        return "Error";
                    }
                    Vector<File> list = new Vector<File>();
                    try{
                        scount = 0;
                        recursiveSearch(dir, list);
                    }catch(InterruptedException ie) {
                        publish(new Message("The search was canceled",true));
                        return "Interrupted1";
                    }
                    firePropertyChange("clear-textarea", "", "");

                    final int lengthOfTask = list.size();
                    publish(new Message("Length Of Task: "+lengthOfTask,false));
                    publish(new Message("----------------",true));

                    try{
                        int current = 0;
                        while(current<lengthOfTask && !isCancelled()) {
                            if(!pBar.isDisplayable()) {
                                return "Disposed";
                            }
                            File file = list.elementAt(current);
                            Thread.sleep(50); //dummy
                            setProgress(100 * current / lengthOfTask);
                            publish(new Message(current+"/"+lengthOfTask + ", "+file.getAbsolutePath(),true));
                            current++;
                        }
                    }catch(InterruptedException ie) {
                        return "Interrupted";
                    }
                    return "Done";
                }
                @Override protected void process(java.util.List<Message> chunks) {
                    //System.out.println("process() is EDT?: " + EventQueue.isDispatchThread());
                    for(Message c: chunks) {
                        if(c.append) {
                            appendLine(c.message);
                        }else{
                            textArea.setText(c.message+"\n");
                        }
                    }
                }
                @Override public void done() {
                    //System.out.println("done() is EDT?: " + EventQueue.isDispatchThread());
                    dirCombo.setEnabled(true);
                    openButton.setEnabled(true);
                    runButton.setEnabled(true);
                    canButton.setEnabled(false);
                    statusPanel.remove(pBar);
                    statusPanel.revalidate();

                    String text = null;
                    if(isCancelled()) {
                        text = "Cancelled";
                    }else{
                        try{
                            text = get();
                        }catch(Exception ex) {
                            ex.printStackTrace();
                            text = "Exception";
                        }
                    }
                    appendLine("----------------");
                    appendLine(text);
                }
                private int scount = 0;
                private void recursiveSearch(File dir, final Vector<File> list) throws InterruptedException {
                    //System.out.println("recursiveSearch() is EDT?: " + EventQueue.isDispatchThread());
                    for(String fname: dir.list()) {
                        if(Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        File sdir = new File(dir, fname);
                        if(sdir.isDirectory()) {
                            recursiveSearch(sdir, list);
                        }else{
                            scount++;
                            if(scount%100==0) publish(new Message("Results:"+scount+"\n",false));
                            list.add(sdir);
                        }
                    }
                }
            };
            worker.addPropertyChangeListener(new ProgressListener(pBar));
            worker.execute();
        }
    }
    class CancelAction extends AbstractAction{
        public CancelAction() {
            super("Cancel");
        }
        @Override public void actionPerformed(ActionEvent evt) {
            if(worker!=null && !worker.isDone()) {
                worker.cancel(true);
            }
            worker = null;
        }
    }
    private boolean isCancelled() {
        return (worker!=null)?worker.isCancelled():true;
    }
    class OpenAction extends AbstractAction{
        public OpenAction() {
            super("Choose...");
        }
        @Override public void actionPerformed(ActionEvent e) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //fileChooser.setDialogTitle("...");
            fileChooser.setSelectedFile(new File((String) dirCombo.getEditor().getItem()));
            int fcSelected = fileChooser.showOpenDialog(MainPanel.this);
            if(fcSelected==JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if(file==null || !file.isDirectory()) {
                    Object[] obj = {"Please select directory."};
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(MainPanel.this, obj, "", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                addItem(dirCombo, file.getAbsolutePath(), 4);
                repaint();
            }else if(fcSelected==JFileChooser.CANCEL_OPTION) {
                return;
            }else{
                Object[] obj = {"Error."};
                java.awt.Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(MainPanel.this, obj, "", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    private void appendLine(String str) {
        System.out.println(str);
        textArea.append(str+"\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
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
        //frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class ProgressListener implements PropertyChangeListener {
    private final JProgressBar progressBar;
    ProgressListener(JProgressBar progressBar) {
        this.progressBar = progressBar;
        this.progressBar.setValue(0);
    }
    @Override public void propertyChange(PropertyChangeEvent e) {
        String strPropertyName = e.getPropertyName();
        if ("progress".equals(strPropertyName)) {
            progressBar.setIndeterminate(false);
            int progress = (Integer)e.getNewValue();
            progressBar.setValue(progress);
        }
    }
}

class Message {
    public final String message;
    public final boolean append;
    public Message(String message, boolean append) {
        this.message = message;
        this.append  = append;
    }
}
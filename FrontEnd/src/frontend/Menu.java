package frontend;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Menu {
    private final JMenuBar mb;
    
    /**
     * Constructor - creeaza butoanele meniului, cu functionalitati
     */
    public Menu() {
        mb = new JMenuBar();
        
        JMenu file = new JMenu("File");
        mb.add(file);
        
        JMenu edit = new JMenu("Edit");
        mb.add(edit);
        
        JMenu help = new JMenu("Help");
        mb.add(help);
        
        JMenuItem about = new JMenuItem("About");
        help.add(about);
        
        JMenuItem plugins = new JMenuItem("Plugins");
         plugins.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JFrame plugin = new JFrame("Plugins");
                plugin.setSize(400, 200);
                
                JPanel cbPanel = new JPanel();
                JPanel btnPanel = new JPanel();
                
                JCheckBox flash = new JCheckBox("Flash Player");
                JCheckBox audioPlayer = new JCheckBox("Audio Player");
                JCheckBox pdfViewer = new JCheckBox("PDF Viewer");
                
                cbPanel.add(flash);
                cbPanel.add(audioPlayer);
                cbPanel.add(pdfViewer);
                
                JButton ok = new JButton("OK");
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        plugin.dispose();
                    }
            
                });
                
                btnPanel.add(ok);
                
                plugin.add(cbPanel, BorderLayout.NORTH);
                plugin.add(btnPanel, BorderLayout.SOUTH);
                
                plugin.setVisible(true);
            }
            
        });
        edit.add(plugins);
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
            
        });
        file.add(exit);
        
    }
    
    /**
     * adauga meniul la frame
     * @param f frame-ul in care se adauga meniul
     */
    public void addMenu(JFrame f) {
        f.add(mb, BorderLayout.NORTH);
    }
    
}

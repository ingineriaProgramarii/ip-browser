package frontend;

import javax.swing.JFrame;

/**
 * main class
 */
@SuppressWarnings("serial")
public class Window extends JFrame {
    private final JFrame f;
    
    /**
     * Constructor - creeaza frame-ul principal si adauga la el meniul si page pannel-ul
     */
    public Window() {
        f = new JFrame();
        f.setTitle("B3 Browser");
        f.setSize(800, 600);
        
        new Menu().addMenu(f);
        new PagePannel().addPanel(f);
        
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        Window window = new Window();
    }

}

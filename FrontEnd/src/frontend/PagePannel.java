package frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.*;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import backend.BackEnd;

public class PagePannel {
    private final JEditorPane display;
    private final JTextField linkBar;
    
    private Document domTree;
    
	private final Stack<String> urlStack;
    
	private JPanel p;
    private JButton goBtn;
    private JButton backBtn;
    private JButton refreshBtn;
    private JButton homeBtn;
    private JButton bookmarkBtn;
    private JButton downloadBtn;
    private Box box;
	
    
	public PagePannel() {
        urlStack = new Stack<>();
        linkBar = new JTextField();
        
        display = new JEditorPane();
        display.setEditable(false);
        display.addHyperlinkListener( 
                new HyperlinkListener() {
                    @Override
                    public void hyperlinkUpdate(HyperlinkEvent event){
                        if(event.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
                            load(event.getURL().toString());
			}	  
                    }	  
		});
        
        
       
        linkBar.addActionListener(
	        new ActionListener(){
                    @Override
		    public void actionPerformed(ActionEvent event){
                    	urlStack.push(linkBar.getText());
                        load(event.getActionCommand());
		    }
		});
        
        buttons();
        
        
        
    }
	
	
	/**
	 * sets the buttons
	 */
	public void buttons() {
		p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        
        /*
        	GO button
        */
        ImageIcon icon = new ImageIcon("icons/go_button.png");
	    goBtn = new JButton(icon);
	    goBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    goBtn.setOpaque(false);
	    goBtn.setContentAreaFilled(false);
	    goBtn.setBorderPainted(false);
	    
	    goBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				urlStack.push(linkBar.getText());
				domTree = BackEnd.getInstance().getDOM(linkBar.getText(), "GET", null);
				display.setContentType("text/html");
				display.setText(domTree.html());
			}
	    	
	    });
	    
	    
	    /*
        	REFRESH button
	    */
	    icon = new ImageIcon("icons/refresh_button.png");
	    refreshBtn = new JButton(icon);
	    refreshBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    refreshBtn.setOpaque(false);
	    refreshBtn.setContentAreaFilled(false);
	    refreshBtn.setBorderPainted(false);
	    
	    refreshBtn.addActionListener(new ActionListener() {
	
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	Document domTree2 = BackEnd.getInstance().getDOM(linkBar.getText(), "GET", null);
	        	display.setContentType("text/html");
	        	display.setText(domTree2.html());
	        	
	        }
	    });
	    
	    
	    /*
        	BACK button
	    */
	    icon = new ImageIcon("icons/back_button.png");
	    backBtn = new JButton(icon);
	    backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    backBtn.setOpaque(false);
	    backBtn.setContentAreaFilled(false);
	    backBtn.setBorderPainted(false);
	    backBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if(urlStack.size()<=1) return;
				try {
					urlStack.pop();
					String urlString = (String)urlStack.peek();
					linkBar.setText(urlString);
					domTree = BackEnd.getInstance().getDOM(linkBar.getText(), "GET", null);
					display.setContentType("text/html");
					display.setText(domTree.html());
				} catch(Exception e) {
					System.out.println("ERROR!!!     " + e);
				}
			}
	    	
	    });
	    
	    
	    /*
        	HOME button
	    */
	    icon = new ImageIcon("icons/home_button.png");
	    homeBtn = new JButton(icon);
	    homeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    homeBtn.setOpaque(false);
	    homeBtn.setContentAreaFilled(false);
	    homeBtn.setBorderPainted(false);
	    
	    homeBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				linkBar.setText("http://google.ro");
				domTree = BackEnd.getInstance().getDOM("http://google.ro", "GET", null);
				display.setContentType("text/html");
				display.setText(domTree.html());
			}
	    	
	    });
    
    
	    /*
	        DOWNLOAD button
	    */
	    icon = new ImageIcon("icons/download_button.png");
	    downloadBtn = new JButton(icon);
	    downloadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    downloadBtn.setOpaque(false);
	    downloadBtn.setContentAreaFilled(false);
	    downloadBtn.setBorderPainted(false);
    
    
	    /*
	        BOOKMARK button
	    */
	    icon = new ImageIcon("icons/bookmark_button.png");
	    bookmarkBtn = new JButton(icon);
	    bookmarkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	    bookmarkBtn.setOpaque(false);
	    bookmarkBtn.setContentAreaFilled(false);
	    bookmarkBtn.setBorderPainted(false);
    
	    box = Box.createVerticalBox();
	    box.add(homeBtn);
	    box.add(bookmarkBtn);
	    box.add(downloadBtn);
	    box.add(Box.createVerticalGlue());
	    box.add(backBtn);
	    box.add(refreshBtn);
	    box.add(goBtn);
	    p.add(box);
	}
    
    /**
     * se adauga linkbar-ul, display-ul si butoanele
     * @param f frame-ul in care se adauga linkbar-ul si display-ul
     */
    public void addPanel(JFrame f) {
        f.add(linkBar, BorderLayout.PAGE_END);
        f.add(new JScrollPane(display), BorderLayout.CENTER);
        f.add(p, BorderLayout.WEST);
    }
    
    /**
     * incarca in display html-ul care se afla la link-ul introdus
     * @param userText link-ul introdus
     */
    private void load(String userText){
		linkBar.setText(userText);
		//try {
		//	display.setPage(userText);
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
		//urlStack.push(linkBar.getText());
		
		domTree = BackEnd.getInstance().getDOM(linkBar.getText(), "GET", null);
		
		display.setContentType("text/html");
		
		display.setText(domTree.html());
    }
    
    public JEditorPane getDisplay() {
        return display;
    }
    
    public JTextField getLinkBar() {
        return linkBar;
    }
    
	public Stack<String> getUrlStack() {
        return urlStack;
    }
    
}

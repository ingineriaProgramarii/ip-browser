package browserAcces;
/*
 * Created on Dec 10, 2004
 *
 */

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdfViewer.PDFViewerBean;
/**
 * @author Gerald Holmann
 *
 */
public class PDFViewerApplet extends JApplet
{
	private PDFViewerBean m_ViewerBean = null;
	private JPanel jPanel = null;
	
	public final static String STRING_FALSE = "False";
	public final static String NUMBER_FALSE = "0";
	public final static String STRING_TRUE = "True";
	public final static String NUMBER_TRUE = "1";
    private JPanel ToolbarOptions = null;
    private JCheckBox jcbShowToolbar = null;
    private JCheckBox jcbShowSelectToolbar = null;
    
    private class LoadPDFRunnable implements Runnable
    {
        private URL m_PDFURL;
        
        public LoadPDFRunnable (URL pdfURL)
        {
            m_PDFURL = pdfURL;
        }

        public void run()
        {
            try
            {
                getPDFViewerBean().loadPDF(m_PDFURL);
            }
            catch (PDFException pdfE)
            {
                displayError (pdfE.getMessage());
            }
            catch (Throwable t)
            {
                displayError ("Error loading PDF: " + t.getMessage());
            }
        }
    }

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() 
	{
		setLookAndFeel();
		
        this.setContentPane(getJPanel());
	}
	
	/**
	 * Set the look and feel.
	 */
	private void setLookAndFeel()
	{
		// Set the look and feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Throwable t)
		{
			// ignore
		}
	}
	
	public void start ()
	{
	    // Set open button visibility according to the BrowseAllowed flag.
	    getPDFViewerBean().getToolbar().getjbOpen().setVisible (toBoolean (getParameter("BrowseAllowed")));

	    // Set print button visibility according to the PrintAllowed flag.
	    getPDFViewerBean().getToolbar().getjbPrint().setVisible (toBoolean (getParameter ("PrintAllowed")));

        // Set the toolbar to visible / not visible according to the ToolbarVisible flag
        getPDFViewerBean().getToolbar().setVisible (toBoolean (getParameter ("ToolbarVisible")));
        
        int scale = toInteger (getParameter ("Scale"));
        if (scale > 0)
        {
            getPDFViewerBean().setScale2D(scale);
        }
        
        // Set the thumbnail pane visible / not visible according to the ThumbnailsVisible flag
        boolean splitVisible = toBoolean (getParameter ("ThumbnailsVisible"));
        if (splitVisible == false)
        {
            getPDFViewerBean().setSplitPolicy(PDFViewerBean.SPLITPOLICY_NEVER_VISIBLE);
        }
        
        // Load a PDF document from a URL
	    final String urlString = getParameter("url");
		if (urlString != null && urlString.trim ().length() > 0)
		{
		    loadPDF("c:/users/lawrence/desktop/Java_1.pdf");
        }
	}
	
	public void stop ()
	{
	    getPDFViewerBean().setDocument(null);
	}
	
    private int toInteger (String str)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException nfe)
        {
            return 0;
        }
    }

    private boolean toBoolean (String str)
	{
	    if (str == null || str.equalsIgnoreCase(STRING_TRUE) || str.equalsIgnoreCase(NUMBER_TRUE))
	    {
	        return true;
	    }
	    return false;
	}

    /**
     * Loads a PDF document into the PDFViewerBean.  This method loads the PDF document
     * inside a privileged action so that it can be called from Javascript.  Otherwise,
     * the method would throw a security exception because Javascript does not get much
     * permissions to start with.
     * 
     * @param pdfURLString The URL to the PDF document.
     * 
     * @return The number of pages in the document
     */
    private int loadPDF (final String pdfURLString)
    {
        // We need to enclose this in a privileged action so that it
        // can execute properly when called from Javascript
        Object rc = AccessController.doPrivileged(new java.security.PrivilegedAction() 
        {
            public Object run()
            {
                int pageCount = privLoadPDF (pdfURLString);
                return new Integer (pageCount);
            }
        });
        
        if (rc != null && rc instanceof Integer)
        {
            return ((Integer)rc).intValue();
        }
        return 0;

    }

    /**
     * Internal implementation of the load PDF method.  This method is safe
     * to call from the applet context, but not from Javascript.  If calling
     * from Javascript, then loadPDF() should be used instead.
     * 
     * @param pdfURLString The URL to the PDF document.
     * 
     * @return The number of pages in the document
     */
    private int privLoadPDF (String pdfURLString)
    {
        try
        {
            // Form URL object
            URL pdfURL = new URL (pdfURLString);
            
            // Load the PDF in the swing thread
            if (SwingUtilities.isEventDispatchThread())
            {
                getPDFViewerBean().loadPDF(pdfURL);
                return getPDFViewerBean().getPageCount();
            }
            else
            {
                // Create PDF loader and call it in the Swing event thread
                LoadPDFRunnable loader = new LoadPDFRunnable (pdfURL);
                SwingUtilities.invokeAndWait(loader);
            }
        }
        catch (MalformedURLException mURL)
        {
            displayError ("Invalid PDF URL: " + mURL.getMessage());
        }
        catch (Throwable t)
        {
            if (t.getCause() instanceof PDFException)
            {
                displayError (t.getCause().getMessage());
            }
            else
            {
                displayError ("Error loading PDF: " + t.getMessage());
            }
        }
        return 0;
    }

	/**
	 * This method initializes PDFViewerBean	
	 * 	
	 * @return com.qoppa.pdfViewer.PDFViewerBean	
	 */    
	public PDFViewerBean getPDFViewerBean() 
    {
		if (m_ViewerBean == null) {
            m_ViewerBean = new PDFViewerBean();
            m_ViewerBean.setName("PDFViewerBean");
            m_ViewerBean.add(getToolbarOptions(), java.awt.BorderLayout.SOUTH);
		}
		return m_ViewerBean;
	}
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout ());
			jPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
			jPanel.add(getPDFViewerBean(), BorderLayout.CENTER);
		}
		return jPanel;
	}
    /**
     * This method initializes ToolbarOptions	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getToolbarOptions()
    {
        if (ToolbarOptions == null)
        {
            ToolbarOptions = new JPanel();
            ToolbarOptions.setLayout(new BoxLayout(getToolbarOptions(), BoxLayout.Y_AXIS));
            ToolbarOptions.add(getJcbShowToolbar(), null);
            ToolbarOptions.add(getJcbShowSelectToolbar(), null);
        }
        return ToolbarOptions;
    }
    /**
     * This method initializes jcbShowToolbar	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJcbShowToolbar()
    {
        if (jcbShowToolbar == null)
        {
            jcbShowToolbar = new JCheckBox();
            jcbShowToolbar.setText("Show Toolbar");
            jcbShowToolbar.setSelected(true);
            jcbShowToolbar.addChangeListener(new javax.swing.event.ChangeListener() { 
                public void stateChanged(javax.swing.event.ChangeEvent e)
                {
                    getPDFViewerBean().getToolbar().setVisible(jcbShowToolbar.isSelected());
                }
            });
        }
        return jcbShowToolbar;
    }
    /**
     * This method initializes jcbShowSelectToolbar	
     * 	
     * @return javax.swing.JCheckBox	
     */
    private JCheckBox getJcbShowSelectToolbar()
    {
        if (jcbShowSelectToolbar == null)
        {
            jcbShowSelectToolbar = new JCheckBox();
            jcbShowSelectToolbar.setText("Show Select Toolbar");
            jcbShowSelectToolbar.setSelected(true);
            jcbShowSelectToolbar.addChangeListener(new javax.swing.event.ChangeListener() { 
                public void stateChanged(javax.swing.event.ChangeEvent e)
                {
                    getPDFViewerBean().getSelectToolbar().setVisible(jcbShowSelectToolbar.isSelected());
                }
            });
        }
        return jcbShowSelectToolbar;
    }
    
    private void displayError (String errorMsg)
    {
        JOptionPane.showMessageDialog(PDFViewerApplet.this, errorMsg);
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"

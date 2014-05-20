package browserAcces;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdf.source.PDFSource;
import com.qoppa.pdfViewer.PDFViewerBean;

public class testPdf
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame frame = new JFrame("");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				PDFViewerBean pdf = new PDFViewerBean();
				
				File doc = new File("c:\\users\\lawrence\\desktop\\Java_1.pdf");
				
				InputStream stream;
				try
				{
					stream = new FileInputStream(doc.getAbsoluteFile());
					
					pdf.getToolbar().getjbOpen().setVisible(false);
					pdf.getToolbar().getjbLoupe().setVisible(false);
					pdf.getToolbar().getjbPanAndZoom().setVisible(false);
					
					pdf.loadPDF("c:/users/lawrence/desktop/Java_1.pdf");
					
				} catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PDFException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				frame.getContentPane().add(pdf);
				
				frame.setSize(800, 600);
				frame.setLocationByPlatform(true);
				frame.setVisible(true);
				frame.pack();
			}
		});

	}

}

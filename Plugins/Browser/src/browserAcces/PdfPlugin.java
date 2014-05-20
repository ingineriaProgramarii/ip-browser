package browserAcces;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JComponent;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdfViewer.PDFViewerBean;

public class PdfPlugin extends PluginBase
{
	private PDFViewerBean pdfViewer;
	
	String myPath;
	
	public PdfPlugin(String id, String path, PluginManager creator) throws ExceptionIdIsNotUnique, FileNotFoundException, PDFException
	{
		if(creator != null)
		{
			if(creator.idIsUnique(id))
			{
				instanceId = id;
				
				myPath = path;
				
				pdfViewer = new PDFViewerBean();
					
				pdfViewer.loadPDF(myPath);
			}
			else
			{
				throw new ExceptionIdIsNotUnique();
			}
		}
		else
		{
			throw new ExceptionInInitializerError();
		}
	}
	
	@Override
	public JComponent getSwingComponent()
	{
		if(pdfViewer == null)
		{
			pdfViewer = new PDFViewerBean();
		
			try
			{
				pdfViewer.loadPDF(myPath);
			} 
			
			catch (PDFException e)
			{
				e.printStackTrace();
			}
		}
		
		return pdfViewer;
	}
	
	@Override
	public String getInstanceId()
	{
		return super.instanceId;
	}
	
	@Override
	public void onDestroy()
	{
		if(pdfViewer != null)
		{
			pdfViewer.setDocument(null);
		}
	}
}

package browserAcces;

import java.io.IOException;
import javax.swing.JComponent;

/**
 * clasa pt un plugin specific(nu testati)
 *
 */
public class PluginBase extends Thread implements PluginInterface
{
	protected String instanceId;
	protected PluginManager manager;
	
	@Override
	public void onStart() throws IOException
	{
		
	}

	@Override
	public void onDestroy()
	{
		super.interrupt();
	}
	
	@Override
	public void run()
	{
		try
		{
			onStart();
		}
		
		catch (IOException e)
		{
			e.printStackTrace();
		}		
	}

	@Override
	public void loadContent(String path)
	{
		
	}
	
	@Override
	public void playContent()
	{
		
	}
	
	@Override
	public void stopContent()
	{
		
	}
	
	@Override
	public void resumeContent()
	{
		
	}

	@Override
	public JComponent getSwingComponent() 
	{
		return null;
	}
	
	@Override
	public String getInstanceId()
	{
		return instanceId;
	}

	@Override
	public PluginManager getManager() 
	{
		return manager;
	}
}

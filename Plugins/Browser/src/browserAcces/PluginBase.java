package browserAcces;
import java.io.IOException;
import java.net.URL;

public class PluginBase extends Thread implements PluginInterface
{
	@Override
	public void onStart() throws IOException
	{
		
	}

	@Override
	public void onPause()
	{
		
	}

	@Override
	public void onDestroy()
	{
		super.interrupt();
	}

	@Override
	public void onResume()
	{
		
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
	public void loadContent(URL path)
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
}

package browserAcces;

import javax.swing.JComponent;

import audioPlugin.AudioPluginControler;

public class AudioPlayer extends PluginBase
{
	private AudioPluginControler audioPlugin;
	
	private String myPath;
	
	public AudioPlayer(String id, String path, PluginManager creator) throws ExceptionIdIsNotUnique
	{
		if(creator != null)
		{
			if(creator.idIsUnique(id))
			{
				instanceId = id;
				
				myPath = path;
				
				audioPlugin = new AudioPluginControler(path);
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
	
	/**
	 * returneaza componenta grafica a pluginului
	 */
	@Override
	public JComponent getSwingComponent()
	{
		if(audioPlugin == null)
		{
			//path init
			
			audioPlugin = new AudioPluginControler(myPath);
		}
		
		return audioPlugin;
	}
	
	/**
	 * returneaza id-ul acestui plugin in cadrul managerului
	 */
	@Override
	public String getInstanceId()
	{
		return super.instanceId;
	}
	
	/**
	 * event destroy
	 */
	@Override
	public void onDestroy()
	{
		if(audioPlugin != null)
		{
			audioPlugin.pause();
		}
	}
}

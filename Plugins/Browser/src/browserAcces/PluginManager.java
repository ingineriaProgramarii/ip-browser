package browserAcces;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

import audioplayer.AudioPlayer;

public class PluginManager extends JApplet
{
	public String name;
	public File[] plugins;
	public File pluginsDir;
	public String namePlugin = "";
	public String[] namesPlugin = null;

	public PluginManager(String name)
	{
		this.name = name;
	}

	/**
	 * Metoda care are rolul de a adauga un plugin in lista creata de
	 * PluginManager.
	 **/
	private void loadPlugins()
	{
		try
		{
			AudioPlayer audioP = new AudioPlayer();
			URL f = new URL("http://www.nch.com.au/acm/11kulaw.wav");
			audioP.loadContent(f);

		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}

	}

	/**
	 * Metoda care are rolul de a elimina toate plugin-urile existente din
	 * PluginManager
	 **/
	public void clear()
	{
		this.plugins = null;
		this.namesPlugin = null;
		System.out.print("S-a eliberat lista de plugin");
	}

	public File getPlugin(String namePlugin)
	{
		try
		{
			for (int i = 0; i < namesPlugin.length; i++)
				if (namesPlugin[i].equals(namePlugin))
					return plugins[i];
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return null;
	}

	public void execPlugin(String pluginType, String path)
	{
		try
		{
			switch (pluginType)
			{
				case "audioPlayer":
				{
					AudioPlayer audioP = new AudioPlayer();
	
					URL f;
					f = new URL("http://www.nch.com.au/acm/11kulaw.wav");
					audioP.loadContent(f);
	
					break;
				}
				case "pdfViewer":
				{
					
					break;
				}
				case "flashPlayer":
				{
	
					break;
				}

			}
		}

		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		PluginManager plugManager = new PluginManager("DSFSD");

		plugManager.execPlugin("pdfViewer",
				"http://www.nch.com.au/acm/11kulaw.wav");

		System.out.println("done");
	}
}
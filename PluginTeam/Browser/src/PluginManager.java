import java.util.Vector;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import audioplayer.AudioPlayer; 


public class PluginManager extends Plugin
{
  public String name;
  public File[] plugins;
  public File pluginsDir;
  public String namePlugin="";
  public String[] namesPlugin = null;
    /**
   * 
   * @throws InterruptedException 
     * @throws LineUnavailableException 
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     * @element-type Plugin
   */
  
  public PluginManager(String name) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException
  {
	  this.name= name;
	loadPlugins();
  }

  /**
   *Metoda care are rolul de a adauga un plugin in lista creata de PluginManager.
   **/
  private void loadPlugins() 
  { 	
		try 
		{
			AudioPlayer audioP= new AudioPlayer();
			URL f=new URL("http://www.nch.com.au/acm/11kulaw.wav");
			audioP.play(f);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
  	
  }
  /**
   *Metoda care are rolul de a elimina toate plugin-urile existente din PluginManager
   **/
  public void clear() 
  {
	this.plugins=null;
	this.namesPlugin=null;
	System.out.print("S-a eliberat lista de plugin");
  }

  public File getPlugin(String namePlugin) 
  {
	  try {
			for(int i=0;i<namesPlugin.length;i++)
				if(namesPlugin[i].equals(namePlugin))
					return plugins[i];
	  	}
	  catch (Exception e)
	  {
			throw new RuntimeException(e);
	  } 
  return null;
  }

  public void execPlugin(String namePlugin, int option) 
  {
	  try {
			for(int i=0;i<namesPlugin.length;i++)
				if(namesPlugin[i].equals(namePlugin))
				{
					Plugin selPlugin=new Plugin();
					if(option==0)
						selPlugin.onStart();
					else if(option==1)
						selPlugin.onPause();
					else if(option==2)
						selPlugin.onResume();
					else if(option==3)
						selPlugin.onDestroy();
				}
	  	}
	  catch (Exception e)
	  {
			throw new RuntimeException(e);
	  } 
  }
}
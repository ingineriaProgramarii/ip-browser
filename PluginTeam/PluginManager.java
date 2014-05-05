import java.util.Vector;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.Iterator;

public class PluginManager
{
  public String name;
  public File[] plugins;
  public File pluginsDir;
  public String namePlugin="";
  public String[] namesPlugin = null;
    /**
   * 
   * @element-type Plugin
   */
  
  public PluginManager(String name)
  {
	  this.name= name;
	  loadPlugins();
  }

  /**
   *Metoda care are rolul de a adauga un plugin in lista creata de PluginManager.
   **/
  private void loadPlugins() 
  { 	
		this.pluginsDir = new File("plugins");
  	
  	this.plugins = pluginsDir.listFiles(new FilenameFilter() 
  	{

			public boolean accept(File dir, String name) 
			{
				return name.toLowerCase().endsWith(".jar");
			}
  		
  	});
  	
		try {
			InputStream inputStream = 
	                Plugin.class.getResourceAsStream("xPlugin.jar");
			namePlugin=Plugin.class.getName();
			for (int i = 0; i < plugins.length; i++) 
			{
				if(namesPlugin[i]==null)
					namesPlugin[i]= namePlugin;
			}
			
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
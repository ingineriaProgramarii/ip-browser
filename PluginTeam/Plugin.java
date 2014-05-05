import java.io.IOException;

public class Plugin implements PluginInterface
{
	int state=-1;
  public String name;

  public Object pluginRef;

  public Browser myBrowser;

  public Plugin Plugin() {
	  
  return null;
  }

  @Override
  public void onStart() throws IOException 
  {
	  Process process = Runtime.getRuntime().exec("cmd /java -jar "+this.name);
	  this.state=1;
  }

  @Override
  public void onPause() 
  {
	  if(this.state==1)
		  {
		  	this.state=0;
		  }
  }
  
  @Override
  public void onResume()
  {
	  if(this.state==0)
	  {
		  this.state=1;
	  }
  }
  
  @Override
  public void onDestroy() 
  {
  }
 }
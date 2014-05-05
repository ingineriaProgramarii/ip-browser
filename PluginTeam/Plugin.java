import java.io.IOException;

public class Plugin {

	int state=-1;
  public String name;

  public Object pluginRef;

  public Browser myBrowser;

  public Plugin Plugin() {
	  
  return null;
  }

  public void onStart() throws IOException 
  {
	  Process process = Runtime.getRuntime().exec("cmd /java -jar "+this.name);
	  this.state=1;
  }

  public void onPause() 
  {
	  if(this.state==1)
		  {
		  	this.state=0;
		  }
  }
  public void onResume() 
  {
	  if(this.state==0)
	  {
		  this.state=1;
	  }
  }

  public void onDestroy() 
  {
  }
 }
import java.io.IOException;
public class Plugin implements PluginInterface {

  int state=-1;
  public String name;
  Process process;
  public Plugin() 
  {  
	  
  }
/**
 * Inceperea executiei unui proces prin intermediul metodei onStart()
 **/
  public void onStart() throws IOException 
  {
	  this.process = Runtime.getRuntime().exec("cmd /java -jar "+this.name);
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
/**
 * Distrugerea procesului curent
 **/
  public void onDestroy() 
  {
	  this.process.destroy();
  }
 }
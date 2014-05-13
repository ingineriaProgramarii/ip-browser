package browserAcces;
import java.io.IOException;
import java.net.URL;

public interface PluginInterface
{	
	public void onStart() throws IOException;
	public void onPause();
	public void onDestroy();
	public void onResume();
	
	public void loadContent(URL path);
	public void playContent();
	public void stopContent();
	public void resumeContent();
}


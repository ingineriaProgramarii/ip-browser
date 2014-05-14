package browserAcces;

import java.io.IOException;
import javax.swing.JComponent;

/**
 * interfata pt plugins(nu testati)
 *
 */
public interface PluginInterface
{	
	public void onStart() throws IOException;
	public void onDestroy();
	
	public void loadContent(String path);
	public void playContent();
	public void stopContent();
	public void resumeContent();
	
	public String getInstanceId();
	
	public JComponent getSwingComponent();
	public PluginManager getManager();
}


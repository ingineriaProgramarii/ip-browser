package browserAcces;

import chrriis.dj.nativeswing.swtimpl.components.JFlashPlayer;

public class FlashPlayer extends PluginBase
{
	private JFlashPlayer flashPlayer;
	
	public void onStart()
	{
		
	}
	
	public void onPause()
	{
		
	}
	
	public void onResume()
	{
		
	}
	
	@Override
	public void onDestroy()
	{
		//flash player stop
		
		super.interrupt();
	}
}

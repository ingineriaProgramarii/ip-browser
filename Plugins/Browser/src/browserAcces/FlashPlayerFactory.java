package browserAcces;

public class FlashPlayerFactory
{
	public static FlashPlayer getNewFlashPlayer()
	{
		return new FlashPlayer();
	}
	
	public static void destroyFlashPlayer(FlashPlayer player)
	{
		player.onDestroy();
	}
}

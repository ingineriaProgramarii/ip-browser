public interface PluginInterface //strategy pattern
{
	public void onStart() throws IOException;
	public void onPause();
	public void onDestroy();
	public void onResume();
}
package audioPlugin;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class AudioListener implements LineListener
{
	private boolean completed = false;

	@Override
	public synchronized void update(LineEvent event)
	{
		LineEvent.Type eventType = event.getType();
		
		if (eventType == LineEvent.Type.STOP || eventType == LineEvent.Type.CLOSE)
		{
			completed = true;
			notifyAll();
		}
	}

	public synchronized void waitUntilDone() throws InterruptedException
	{
		while (!completed)
		{
			wait();
		}
	}
}

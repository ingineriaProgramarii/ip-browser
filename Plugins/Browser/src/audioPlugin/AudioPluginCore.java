package audioPlugin;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * clasa care asigura functionalitatea propriu-zisa a pluginului
 * @author lawrence
 *
 */
public class AudioPluginCore implements Runnable
{
	AudioInputStream input;
	Clip audioClip;
	
	FloatControl volumeControl;
	
	float volume;
	
	String path;
	
	/**
	 * constructor pt clasa aceasta
	 * @param clipPath calea la care se gaseste clipul care va fi deschis
	 * @param vol volumul initial al playerului
	 */
	public AudioPluginCore(String clipPath, float vol)
	{		
		volume = vol;
		
		path = clipPath;
		
		try
		{
			audioClip = AudioSystem.getClip();
		} 
		
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * seteaza volumul playerului curent
	 * @param val valoarea actuala(1 - 100)
	 */
	public void setVolume(float val)
	{
		volume = (float) (-75.9999f + val / 100 * 75.9999);
		
		volumeControl.setValue(volume);
	}
	
	/**
	 * pauzeaza clipul actual
	 */
	public void pause()
	{
		audioClip.stop();
	}
	
	/**
	 * reia redarea clipului actual
	 */
	public void resume()
	{
		audioClip.start();
	}
	
	/**
	 * returneaza lungimea la care este playerul in clip in microsecunde
	 * @return lungimea clipului
	 */
	public long getLength()
	{		
		return audioClip.getMicrosecondPosition();
	}
	
	/**
	 * lungimea maxima a playerului
	 * @return luyngimea in microsecunde
	 */
	public long getMaxLength()
	{
		return audioClip.getMicrosecondLength();
	}
	
	/**
	 * seteaza playerul la o anumita perioada in clip
	 * @param percent procentul la care va fi setat
	 */
	public void setPlayedAmount(float percent)
	{		
		double newValue = (double)getLength() * percent;
		
		System.out.println("new song value is " + (long) newValue);
		
		audioClip.setMicrosecondPosition((long) newValue);
	}

	/**
	 * functionalitatea pluginului implementata cu threaduri
	 */
	@Override
	public void run()
	{		
		try
		{
			input = AudioSystem.getAudioInputStream(new File(path));
			
			AudioListener listener = new AudioListener();
			
			audioClip.addLineListener(listener);
			
			audioClip.open(input);
		} 
		
		catch (UnsupportedAudioFileException | IOException e)
		{
			e.printStackTrace();
		} 
		
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}		
		
		volumeControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
		
		setVolume(volume);
		
		audioClip.start();
	}
}

package audioplayer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.*;



public class AudioPlayer
{
    private int bufferSize = 4096; // Tamanho de buffer padrALo 4k
    private volatile boolean paused = false;
    private final Object lock = new Object();
    private SourceDataLine line;
    private int secondsFade = 0;
    private ArrayList<AudioPlayerListener> _listeners = new ArrayList<AudioPlayerListener>();

    public void stop()
    {
        if(line != null)
        {
            line.stop();
            line.close();
        }
    }

    public boolean isPaused()
    {
        return this.paused;
    }


    public void pause()
    {
        if(!this.isPaused())
            paused = true;
    }

    public void resume()
    {
        if(this.isPaused())
        {
            synchronized(lock){
                lock.notifyAll();
                paused = false;
            }
        }
    }

    public void play(URL url) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException
    {
    	  
        AudioInputStream encoded = AudioSystem.getAudioInputStream(url);
        AudioFormat encodedFormat = encoded.getFormat();
        AudioFormat decodedFormat = this.getDecodedFormat(encodedFormat);
        Long duration = null;
        AudioInputStream currentDecoded = AudioSystem.getAudioInputStream(decodedFormat, encoded);
        line = AudioSystem.getSourceDataLine(decodedFormat);
        line.open(decodedFormat);
        line.start();
        boolean fezFadeIn = false;
        boolean fezFadeOut = false;
        byte[] b = new byte[this.bufferSize];
        int i = 0;
        Map properties = null;
        try 
        {
            properties = AudioUtil.getMetadata(url);
            duration = (Long) properties.get("duration");
        } 
        catch (Exception ex)
        {
            duration = 0L;
        }

        duration = duration < 0 ? 0 : duration;

        synchronized(lock)
        {
            //Parametro que ativa ou nALo o fade de acordo com o tamanho do A!udio
            long paramFade = (secondsFade*2+1)*1000000;
            //long paramFade = 0;
            //Logger.getLogger(this.getClass().getName()).info("Arquivo: "+file+", DURACAO DO AUDIO: "+duration+", paramfade: "+paramFade);
            while(true)
            {
                if(secondsFade > 0 && !fezFadeIn && duration >= paramFade)
                {
                    fezFadeIn = true;
                    fadeInAsync(this.secondsFade);
                }

                if( secondsFade > 0 &&
                        duration > paramFade &&
                        !fezFadeOut &&
                        line.getMicrosecondPosition() >= duration - ((this.secondsFade+1)*1000000) )
                {
                    this.fireAboutToFinish();
                    fadeOutAsync(this.secondsFade);
                    fezFadeOut = true;
                }

                if(paused == true)
                {
                    line.stop();
                    lock.wait();
                    line.start();
                }

                i = currentDecoded.read(b, 0, b.length);
                if(i == -1)
                    break;

                line.write(b, 0, i);
            }
        }

        if(  !fezFadeOut && line.isOpen() )
            this.fireAboutToFinish();

        line.drain();
        line.stop();
        line.close();
        currentDecoded.close();
        encoded.close();
    }

    public synchronized void fadeInAsync(final int seconds)
    {
        if(line != null && line.isOpen())
        {
            Thread t = new Thread(new Fader(true, this, secondsFade));
            t.start();
        }
    }

    public synchronized void fadeOutAsync(final int seconds)
    {
        if(line != null && line.isOpen())
        {
            Thread t = new Thread(new Fader(false, this, secondsFade));
            t.start();            
        }
    }

    public void setVolume(double value) 
    {
        if(!line.isOpen())
            return;
        // value is between 0 and 1
        value = (value<=0.0)? 0.0001 : ((value>1.0)? 1.0 : value);
        try
        {
            float dB = (float)(Math.log(value)/Math.log(10.0)*20.0);
            ((FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN)).setValue(dB);
        }
        catch(Exception ex)
        {

        }
    }

    public boolean isPlaying()
    {
        return (line != null && line.isOpen());
    }


    protected AudioFormat getDecodedFormat(AudioFormat format)
    {
        AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,  // Encoding to use
                format.getSampleRate(),           // sample rate (same as base format)
                16,               // sample size in bits (thx to Javazoom)
                format.getChannels(),             // # of Channels
                format.getChannels()*2,           // Frame Size
                format.getSampleRate(),           // Frame Rate
                false                 // Big Endian
        );
        return decodedFormat;    
    }


    public int getBufferSize()
    {
        return bufferSize;
    }


    public void setBufferSize(int bufferSize)
    {
        if(bufferSize <= 0)
            return;
        this.bufferSize = bufferSize;
    }

    /**
     * @return the secondsFade
     */
    public int getSecondsFade() {
        return secondsFade;
    }

    /**
     * @param secondsFade the secondsFade to set
     */
    public void setSecondsFade(int secondsFade) {
        if(secondsFade < 0 || secondsFade > 10)
            throw new IllegalArgumentException("Erro ao configurar cross-fade com valor em segundos: "+secondsFade);
        this.secondsFade = secondsFade;
    }

    public void addAudioPlayerListener(AudioPlayerListener a)
    {
        this._listeners.add(a);
    }

    public void removeAudioPlayerListener(AudioPlayerListener a)
    {
        this._listeners.remove(a);
    }

    private void fireAboutToFinish()
    {
        for(AudioPlayerListener a : this._listeners)
            a.aboutToFinish(this);
    }
}
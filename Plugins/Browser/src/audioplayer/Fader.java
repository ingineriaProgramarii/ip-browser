package audioplayer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

class Fader implements Runnable
{
    private boolean fadeIn;
    private int seconds=0;
    private final AudioPlayer player;
    private float increaseParam;

    public Fader(boolean fadeIn, AudioPlayer player, int secondsToFade) 
    {
        this.fadeIn = fadeIn;
        this.seconds = secondsToFade;
        this.player = player;
        if(fadeIn)
            increaseParam = 0.01F;
        else
            increaseParam = -0.01F;
    }

    @Override
    public void run() 
    {
        try
        {
            encapsulateRun();
        }
        catch(Exception ex)
        {
            if(fadeIn)
                player.setVolume(1.0F);
            else
                player.setVolume(0.0F);
        }
    }

    private void encapsulateRun() throws Exception
    {
        synchronized(player)
        {            
            float per;
            if(fadeIn)
            {
                Logger.getLogger(getClass().getName()).info("Fazendo fade in");
                per = 0.0F;
            }
            else
            {
                Logger.getLogger(getClass().getName()).info("Fazendo fade out");
                per = 1.0F;
            }
            player.setVolume(per);
            if(fadeIn)
            {
                while(per < 1.00F)
                {
                    per = per + increaseParam;
                    player.setVolume(per);
                    Thread.sleep(10*seconds);
                }
            }
            else
            {
                while(per > 0.00F)
                {
                    per = per + increaseParam;
                    player.setVolume(per);
                    Thread.sleep(10*seconds);
                }
            }
        }
    }

}
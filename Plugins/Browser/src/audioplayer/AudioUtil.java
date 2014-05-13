package audioplayer;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
public class AudioUtil
{
    private static ArrayList<URLMap> cache = new ArrayList<URLMap>();
    private static int _CACHE_SIZE = 5;

    public static Map<String, Object> getMetadata(URL filename) throws Exception
    {
        URLMap fm = new URLMap(filename, null);
        int index = cache.indexOf(fm);
        if(index >= 0)
            return cache.get(index).getMap();

        AudioFileFormat format = AudioSystem.getAudioFileFormat(filename);

        Map<String, Object> mapa = new HashMap<String, Object>();
        mapa.putAll(format.properties());

/*
        if(mapa.get("author") == null && filename.getName().contains(" - "))
        {
            mapa = new HashMap<String, Object>();
            String[] s = filename.getName().split(" - ");
            mapa.put("author", s[0]);
            s[1] = s[1].substring(0, s[1].length()-4);
            mapa.put("title", s[1]);
        }

        if(mapa.get("author") == null)
        {
            mapa.put("author", "Desconhecido");
            mapa.put("title", "Desconhecido");
        }
*/
        Object o = format.properties().get("duration");

        if(o == null)
            mapa.put("duration", 0);

        fm.setMap(mapa);
        cache.add(fm);
        while(cache.size() > _CACHE_SIZE)
            cache.remove((int)0);

        return mapa;
    }
}
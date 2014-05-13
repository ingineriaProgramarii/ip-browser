
package audioplayer;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
class URLMap
{
    private URL url;
    private Map<String, Object> map;

    public URLMap(URL url, Map<String, Object> map) 
    {
        this.url = url;
        this.map = map;
    }

    public URL getURL() {
        return url;
    }

    public void setURL(URL url) {
        this.url = url;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final URLMap other = (URLMap) obj;
        if (this.url != other.url && (this.url == null || !this.url.equals(other.url))) 
        {
            return false;
        }
        return true;
    }

}
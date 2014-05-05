package cache;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Cache {

    public ArrayList<Cookie> cookies;

    public ArrayList<HistoryItem> History;

    public void addCookie( String name, String domain, String value, Date expireDate, String path, Boolean secure ) {
    }

    public Boolean removeCookie( String name, String domain ) {
        return null;
    }

    public ArrayList<Cookie> getDomainCookies( String domain ) {
        return null;
    }

    public Cache() {
    }

    public void save() {
    }

    public void addHistoryItem( String name, String url ) {
    }

    public ArrayList<HistoryItem> getHistory( HashMap<String, String> filters ) {
        return null;
    }

}

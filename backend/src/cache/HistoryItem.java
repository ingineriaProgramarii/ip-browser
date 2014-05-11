package cache;
import java.util.Date;

public class HistoryItem {

    private String name;

    private Date lastAccess;

    private String url;

    public HistoryItem() {
    }

    public HistoryItem(String _name, Date _lastAccess, String _url) {
        name = _name;
        lastAccess = _lastAccess;
        url = _url;
    }

    public void delete() {
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess( Date lastAccess ) {
        this.lastAccess = lastAccess;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }
}

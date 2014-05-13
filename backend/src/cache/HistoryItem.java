package cache;
import java.util.Date;

public class HistoryItem {

    private String name;

    private Date lastAccess;

    private String url;

    private String tag;

    public HistoryItem() {
    }

    public HistoryItem(String _name, Date _lastAccess, String _url, String _tag) {
        name = _name;
        lastAccess = _lastAccess;
        url = _url;
        tag = _tag;
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

    public String getDate() {
        return lastAccess.toString();
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

    public void setTag( String _tag) {
        tag = _tag;
    }

    public String getTag() {
        return tag;
    }

    public void save() {

    }
}

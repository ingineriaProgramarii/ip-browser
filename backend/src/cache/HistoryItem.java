package cache;


import java.util.Date;

public class HistoryItem {

    public String name;

    public Date lastAccess;

    public String url;

    public HistoryItem() {
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

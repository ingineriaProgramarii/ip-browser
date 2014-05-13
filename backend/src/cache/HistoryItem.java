package cache;
import java.util.Date;

public class HistoryItem {

    private String name;

    private Date lastAccess;

    private String url;

    private String tag;

    /**
     * Standard constructor for HistoryItems
     * This objects are used in Cache class
     * Describe the history of web pages
     */
    public HistoryItem() {
    }

    /**
     * Constructor for HistoryItem object
     * @param _name - the name of the site which was accessed by user
     * @param _lastAccess - date when user acces web site
     * @param _url - address of web site
     * @param _tag - tag created by user
     */
    public HistoryItem(String _name, Date _lastAccess, String _url, String _tag) {
        name = _name;
        lastAccess = _lastAccess;
        url = _url;
        tag = _tag;
    }

    /**
     * This method delete from data base, tabel history the line which contain this HistoryItem
     */
    public void delete() {
    }

    /**
     *
     * @return the name of the HistoryItem
     */
    public String getName() {
        return name;
    }

    /**
     * This method set name of the History item
     * @param name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     *
     * @return field lastAccess in Date format
     */
    public Date getLastAccess() {
        return lastAccess;
    }

    /**
     *
     * @return date of lastAccess in string format
     */
    public String getDate() {
        return lastAccess.toString();
    }

    /**
     *
     * @param lastAccess
     */
    public void setLastAccess( Date lastAccess ) {
        this.lastAccess = lastAccess;
    }

    /**
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * set the url for this HistoryItem
     * @param url
     */
    public void setUrl( String url ) {
        this.url = url;
    }

    /**
     * Set the tag for this web page
     * @param _tag
     */
    public void setTag( String _tag) {
        tag = _tag;
    }

    /**
     *
     * @return the tag of the site
     */
    public String getTag() {
        return tag;
    }

    /**
     * save this object in data base
     */
    public void save() {

    }
}

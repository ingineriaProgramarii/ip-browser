package cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cache {

    public ArrayList<Cookie> cookies;

    public ArrayList<HistoryItem> History;

    private Connection dbConnection;

    /**
     * Add custom cookie to list
     * @param name - name of cookie
     * @param domain - domain of cookie
     * @param value - value for cookie
     * @param expireDate - date when cookie become invalid
     * @param path - location for cookie
     * @param secure - secure option
     */
    public void addCookie( String name, String domain, String value, String expireDate, String path, int secure ) {
        Cookie c = new Cookie( domain, name, value, expireDate, path, secure );
        if( this.cookies == null ) {
            this.cookies = new ArrayList<Cookie>();
        }
        this.cookies.add( c );
    }

    /**
     * Add standard object Cookie to list
     * @param c - one object which descrie one cookie
     */
    public void addCookie( Cookie c ) {
        this.cookies.add( c );
    }

    /**
     * get all cookie from sqllite-browser.db
     */
    public void loadCookies() {
        this.cookies = new ArrayList<Cookie>();
        String sql = "SELECT * FROM cookies;";
        java.sql.Statement stmt = null;
        try {
            stmt = dbConnection.createStatement();
            ResultSet results = stmt.executeQuery( sql );
            while( results.next() ) {
                this.cookies.add( new Cookie(
                        results.getString( "domain" ),
                        results.getString( "name" ),
                        results.getString( "value" ),
                        results.getString( "expireDate" ),
                        results.getString( "path" ),
                        results.getInt( "secure" ) )
                                );
            }
            results.close();
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Check cookies which are expired and remove them
     * Save cookies
     */
    public void saveCookies() {
        Date now = new Date();
        try {
            Date date1 = new SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyy" ).parse( now.toString() );
            for( Cookie c : this.cookies ) {
                try {
                    System.out.println( "Datb : " + c.getExpireDate() );
                    Date cookieDate;
                    if( c.getExpireDate().length() > 0 ) {
                        if( c.getExpireDate().indexOf( "," ) > 0 ) {
                            cookieDate = new SimpleDateFormat( "EEE, dd-MMM-yyyy HH:mm:ss z",
                                                               Locale.ENGLISH ).parse( c.getExpireDate() );
                        }
                        else {
                            cookieDate = new SimpleDateFormat( "yyyy-mm-dd HH:mm:ss", Locale.ENGLISH ).parse( c.getExpireDate() );
                        }
                        if( date1.compareTo( cookieDate ) < 0 ) {
                            c.save();
                        }
                    }
                }
                catch( ParseException e ) {
                    e.printStackTrace();
                }
            }
        }
        catch( ParseException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Constructor for Cache object
     * This method initializate history, cookies adn dbConnection fields
     */
    public Cache() {
        this.dbConnection = Database.getInstance().getConn();
        this.History = new ArrayList<HistoryItem>();
        this.cookies = new ArrayList<Cookie>();

        this.loadCookies();

        String sql = "SELECT * FROM history";
        java.sql.Statement stmt = null;
        try {
            stmt = this.dbConnection.createStatement();
            ResultSet results = stmt.executeQuery( sql );
            while( results.next() ) {
                String itemName = results.getString( "name" );
                String itemUrl = results.getString( "url" );
                String itemTag = results.getString( "tag" );
                Date itemDate = results.getDate( "date" );
                History.add( new HistoryItem( itemName, itemDate, itemUrl, itemTag ) );
            }
            results.close();
        }
        catch( SQLException e1 ) {
            e1.printStackTrace();
        }
    }

    /**
     * Add in database current page of browser
     * @param name
     * @param url
     */
    public void addHistoryItem( String name, String url ) {
        HistoryItem hi = new HistoryItem( name, new Date(), url, "" );
        History.add( hi );
        hi.save();
    }

    /**
     * Get all historyItems whitch match the filter
     * @param filters - two strings - ex ( "name","google")
     * @return one list of History Items
     */
    public ArrayList<HistoryItem> getHistory( HashMap<String, String> filters ) {
        ArrayList<HistoryItem> filterItems = new ArrayList<HistoryItem>();

        Iterator it = filters.entrySet().iterator();
        while( it.hasNext() ) {
            Map.Entry pairs = ( Map.Entry ) it.next();
            Method method;

            for( Iterator<HistoryItem> i = History.iterator(); i.hasNext(); ) {
                HistoryItem item = i.next();
                String methodName = "get" + pairs.getKey();
                String result = "";
                try {
                    method = item.getClass().getMethod( methodName );
                    Object value = method.invoke( item );
                    result = ( String ) value;
                }
                catch( NoSuchMethodException e ) {
                    e.printStackTrace();
                }
                catch( InvocationTargetException e ) {
                    e.printStackTrace();
                }
                catch( IllegalAccessException e ) {
                    e.printStackTrace();
                }
                if( pairs.getValue().toString().toUpperCase().contains( result.toUpperCase() ) ) {
                    filterItems.add( item );
                }
            }
            it.remove();
        }
        return null;
    }

    /**
     * detele expired cookies from data base
     */
    public void deleteExpiredCookies() {
        String sql = "DELETE FROM cookies WHERE expireDate < DATETIME('now')";
        java.sql.Statement stmt = null;
        try {
            stmt = this.dbConnection.createStatement();
            ResultSet results = stmt.executeQuery( sql );
            results.close();
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Search for cache item
     * @param url
     * @return - ( String ) path
     */
    public String getCacheItem( String url ) {
        String sql = "SELECT path FROM cache WHERE url='" + url + "' LIMIT 1;";
        String path = null;
        java.sql.Statement stmt = null;
        try {
            stmt = this.dbConnection.createStatement();
            ResultSet results = stmt.executeQuery( sql );
            while( results.next() ) {
                path = results.getString( "path" );
            }
            results.close();
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * Get all cookies from a certain domain
     * @param domain - domaine for cookies
     * @param path
     * @param https - the protocol is secure ( https)
     * @return
     */
    public String getDomainCookies( String domain, String path, Boolean https ) {
        String cookies = "";
        Date now = new Date();
        for( Cookie c : this.cookies ) {
            if( c.getDomain() == domain && c.getPath() == path ) {
                if( https || ( !https && c.getSecure() == 0 ) ) {
                    try {
                        Date cookieDate = new SimpleDateFormat( "yyyy-MM-d HH:mm:ss" ).parse( c.getExpireDate() );
                        if( now.compareTo( cookieDate ) < 0 ) {
                            cookies += c.getName() + "=" + c.getValue() + ";";
                        }
                    }
                    catch( ParseException e ) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return cookies;
    }

    /**
     * Check if one resource exists cache data base
     * @param url
     * @return
     */
    public String resourceExists( String url ) {
        String sql = "SELECT * FROM cache WHERE url = '" + url + "';";
        String path = null;
        try {
            java.sql.Statement st = this.dbConnection.createStatement();
            ResultSet rs = st.executeQuery( sql );
            while( rs.next() ) {
                path = rs.getString( "path" );
            }
            return path;
        }
        catch( SQLException e ) {
            System.out.println( e.getMessage() );
            return null;
        }
    }

    /**
     * Add resource to data base - local path
     * @param url - url for resource
     * @param path - local path
     */
    public void addResourceToDB( String url, String path ) {
        String sql = "INSERT INTO cache VALUES ('" + path + "', '" + url + "');";
        try {
            java.sql.Statement stmt = this.dbConnection.createStatement();
            stmt.executeUpdate( sql );
            //this.dbConnection.commit();
        }
        catch( SQLException e ) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}

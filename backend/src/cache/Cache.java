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

    public void addCookie( String name, String domain, String value, String expireDate, String path, Boolean secure ) {
        Cookie c = new Cookie( domain, name, value, expireDate, path, secure );
        if( this.cookies == null ) {
            this.cookies = new ArrayList<>();
        }
        this.cookies.add( c );
    }

    public void addCookie( Cookie c ) {
        this.cookies.add( c );
    }

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
                        results.getBoolean( "secure" ) )
                                );
            }
            results.close();
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    public void saveCookies() {
        Date now = new Date();
        for( Cookie c : this.cookies ) {
            try {
                Date cookieDate = new SimpleDateFormat( "yyyy-MM-d HH:mm:ss" ).parse( c.getExpireDate() );
                if( now.compareTo( cookieDate ) < 0 ) {
                    c.save();
                }
            }
            catch( ParseException e ) {
                e.printStackTrace();
            }
        }
    }

    public Cache() {
        this.dbConnection = Database.getInstance().getConn();
        this.History = new ArrayList<>();
        this.cookies = new ArrayList<>();

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

    public void addHistoryItem( String name, String url ) {
        History.add( new HistoryItem( name, new Date(), url, "" ) );
    }

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

    public String getDomainCookies( String domain, String path, Boolean https ) {
        String cookies = "";
        Date now = new Date();
        for( Cookie c : this.cookies ) {
            if( c.getDomain() == domain && c.getPath() == path ) {
                if( https || ( !https && !c.getSecure() ) ) {
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
}

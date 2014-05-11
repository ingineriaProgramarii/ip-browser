package cache;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.*;
import java.lang.reflect.Method;

public class Cache {

    public ArrayList<Cookie> cookies;

    public ArrayList<HistoryItem> History;

    private Connection dbConnection;

    public void addCookie( String name, String domain, String value, Date expireDate, String path, Boolean secure ) {
    }

    public Boolean removeCookie( String name, String domain ) {
        return null;
    }

    public ArrayList<Cookie> getDomainCookies( String domain ) {
        this.cookies = new ArrayList<Cookie>();
        String sql = "SELECT * FROM cookies WHERE domain = '" + domain + "' AND expireDate < DATETIME('now');";
        java.sql.Statement stmt = null;
        try {
            stmt = dbConnection.createStatement();
            ResultSet results = stmt.executeQuery( sql );
            while( results.next() ) {
                cookies.add( new Cookie(
                        domain,
                        results.getString( "name" ),
                        results.getString( "value" ),
                        results.getDate( "expireDate" ),
                        results.getString( "path" ),
                        results.getBoolean( "secure" ) )
                           );
            }
            results.close();
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
        return this.cookies;
    }

    public Cache() {
        this.dbConnection = Database.getInstance().getConn();
    }

    public void save() {
    }

    public void addHistoryItem( String name, String url ) {
        History.add(new HistoryItem(name, new Date(), url, ""));
    }

    public ArrayList<HistoryItem> getHistory( HashMap<String, String> filters ) {
        ArrayList<HistoryItem> filterItems = new ArrayList<HistoryItem>();

        Iterator it = filters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Method method;

            for(Iterator<HistoryItem> i = History.iterator(); i.hasNext(); ) {
                    HistoryItem item = i.next();
                    String methodName = "get" + pairs.getKey();
                    String result = "";
                    try {
                        method = item.getClass().getMethod(methodName);
                        Object value = method.invoke(item);
                        result = (String)value;
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (pairs.getValue().equals(result))
                        filterItems.add(item);
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

}

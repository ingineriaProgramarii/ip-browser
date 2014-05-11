package cache;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
        this.dbConnection = Database.getInstance().getConn();
        String sql = "SELECT * FROM cookies WHERE domain = '" + domain + "';";
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
    }

    public void save() {
    }

    public void addHistoryItem( String name, String url ) {
    }

    public ArrayList<HistoryItem> getHistory( HashMap<String, String> filters ) {
        return null;
    }

}

package cache;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public Connection conn;
    public static Database instance = null;

    private Database() {
        String url = "https://students.info.uaic.ro/db/";
        String dbName = "ipCache";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "ipCache";
        String password = "j5QQ9DcQ7h";
        try {
            Class.forName( driver ).newInstance();
            this.conn = DriverManager.getConnection( url + dbName, userName, password );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if( instance == null ) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConn() {
        return this.conn;
    }
}

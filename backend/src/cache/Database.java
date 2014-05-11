package cache;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private Connection conn;
    private static Database instance = null;

    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
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

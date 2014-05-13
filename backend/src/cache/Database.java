package cache;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private Connection conn;
    private static Database instance = null;

    /**
     * Pattern Singleton
     * Class constructor
     * Is private
     * It wants a single object instance for Database - SQlite - browser.db
     */
    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:src/cache/browser.db");
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * static method
     * @return instance of object DataBase
     */
    public static Database getInstance() {
        if( instance == null ) {
            instance = new Database();
        }
        return instance;
    }

    /**
     *
     * @return connection from DataBase - SQlite
     */
    public Connection getConn() {
        return this.conn;
    }
}

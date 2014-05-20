package cache;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Cookie {

    private String domain;

    private String name;

    private String value;

    private String expireDate;

    private String path;

    private int secure;
    private Connection dbConnection;

    /**
     * Standard constructor for cookie object
     */
    public Cookie() {
        this.setConnection();
    }

    /**
     * Custom constructor for cookie object
     * @param domain - domain of cookie
     * @param name - name fof cookie
     * @param value - value for cookie
     * @param expireDate - the date when the cookie expire
     * @param path - path for cookie
     * @param secure - secure option
     */
    public Cookie( String domain, String name, String value, String expireDate, String path, int secure ) {
        this.domain = domain;
        this.name = name;
        this.path = path;
        this.value = value;
        this.expireDate = expireDate;
        this.secure = secure;
        this.setConnection();
    }

    /**
     * initializate database connection
     */
    private void setConnection() {
        this.dbConnection = Database.getInstance().getConn();
    }

    /**
     * delete cookie from database
     */
    public void delete() {
        String sql = "DELETE FROM cookies " +
                     "WHERE " +
                     "domain = '" + this.domain + "' AND " +
                     "name   = '" + this.name + "' AND " +
                     "path   = '" + this.path + "';";
        try {
            java.sql.Statement stmt = this.dbConnection.createStatement();
            stmt.executeQuery( sql );
            this.dbConnection.commit();
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    /**
     * get domain for this cookie
     * @return (string) name of domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * getter for name field
     * @return name for this cookie
     */
    public String getName() {
        return name;
    }

    /**
     * getter for value field
     * @return (string) value for this cookie
     */
    public String getValue() {
        return value;
    }

    /**
     * getter for expireDate field
     * @return (string) the date when this cookie expire
     */
    public String getExpireDate() {
        return expireDate;
    }

    /**
     * getter for path field
     * @return path for this cookie
     */
    public String getPath() {
        return path;
    }

    /**
     * getter for secure option
     * @return 1 if cookie is secure 0 else
     */
    public int getSecure() {
        return secure;
    }

    /**
     * Set name of domain for this cookie
     * @param domain
     */
    public void setDomain( String domain ) {
        this.domain = domain;
    }

    /**
     * setter for name
     * @param name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * setter for value
     * @param value
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * setter for expire date
     * @param expireDate
     */
    public void setExpireDate( String expireDate ) {
        this.expireDate = expireDate;
    }

    /**
     * setter for path field
     * @param path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * setter for secure field
     * @param secure
     */
    public void setSecure( int secure ) {
        this.secure = secure;
    }

    /**
     * check if this cookie exists in data base
     * @return true or false
     */
    public boolean exists() {
        String sql = "SELECT * FROM cookies WHERE domain = '" +
                     this.domain + "' AND path = '" +
                     this.path + "' AND name = '" +
                     this.name + "';";
        try {
            java.sql.Statement st = this.dbConnection.createStatement();
            ResultSet rs = st.executeQuery( sql );
            int i = 0;
            while( rs.next() ) {
                i++;
            }
            return i > 0;
        }
        catch( SQLException e ) {
            System.out.println( e.getMessage() );
            return false;
        }
    }

    /**
     * save this cookie in database
     */
    public void save() {
        if( this.exists() ) {
            this.update();
        }
        else {
            this.insert();
        }
    }

    /**
     * insert cookie in data base if does not exist
     */
    private void insert() {
        String sql = "INSERT INTO cookies( domain, name, value, expireDate, path, secure ) " +
                     "VALUES( '" +
                     this.domain + "', '" +
                     this.name + "','" +
                     this.value + "','" +
                     this.expireDate + "','" +
                     this.path + "'," +
                     this.secure +
                     " );";
        try {
            java.sql.Statement st = this.dbConnection.createStatement();
            st.executeUpdate( sql );
            this.dbConnection.commit();
        }
        catch( SQLException e ) {
            System.out.println( e.getMessage() );
        }
    }

    /**
     * if cookie exist in database just update him
     */
    private void update() {
        String sql = "UPDATE cookies " +
                     "SET value   = '" + this.value + "'," +
                     " expireDate = '" + this.expireDate + "'," +
                     " secure     = " + this.secure +
                     " WHERE (" +
                     " domain = '" + this.domain + "' AND " +
                     " name   = '" + this.name + "' AND " +
                     " path   = '" + this.path + "');";
        try {
            java.sql.Statement stmt = this.dbConnection.createStatement();
            stmt.executeUpdate( sql );
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
    }
}

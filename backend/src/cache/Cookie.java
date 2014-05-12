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

    private Boolean secure;
    private Connection dbConnection;

    public Cookie() {
        this.setConnection();
    }

    public Cookie( String domain, String name, String value, String expireDate, String path, Boolean secure ) {
        this.domain = domain;
        this.name = name;
        this.path = path;
        this.value = value;
        this.expireDate = expireDate;
        this.secure = secure;
        this.setConnection();
    }

    private void setConnection() {
        this.dbConnection = Database.getInstance().getConn();
    }

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

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getPath() {
        return path;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setDomain( String domain ) {
        this.domain = domain;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setValue( String value ) {
        this.value = value;
    }

    public void setExpireDate( String expireDate ) {
        this.expireDate = expireDate;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public void setSecure( Boolean secure ) {
        this.secure = secure;
    }

    public boolean exists() {
        String sql = "SELECT * FROM cookies WHERE domain = '" +
                     this.domain + "' AND path = '" +
                     this.path + "' AND name = '" +
                     this.name + "');";
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

    public void save() {
        if( this.exists() ) {
            this.update();
        }
        else {
            this.insert();
        }
    }

    private void insert() {
        String sql = "INSERT INTO cookies( domain, name, value, expireDate, path, secure ) " +
                     "VALUES( '" +
                     this.domain + "', '" +
                     this.name + "','" +
                     this.value + "'," +
                     this.expireDate + ",'" +
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
            this.dbConnection.commit();
        }
        catch( SQLException e ) {
            e.printStackTrace();
        }
    }
}

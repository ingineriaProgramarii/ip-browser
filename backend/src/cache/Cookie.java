package cache;

import java.util.Date;

public class Cookie {

    private String domain;

    private String name;

    private String value;

    private Date expireDate;

    private String path;

    private Boolean secure;

    public Cookie() {
    }

    public void delete() {
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

    public Date getExpireDate() {
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

    public void setExpireDate( Date expireDate ) {
        this.expireDate = expireDate;
    }

    public void setPath( String path ) {
        this.path = path;
    }

    public void setSecure( Boolean secure ) {
        this.secure = secure;
    }

}

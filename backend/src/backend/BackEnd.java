package backend;

import cache.Cache;
import cache.HistoryItem;
import org.w3c.dom.Document;
import requests.Parser;
import requests.Requests;

import java.util.ArrayList;
import java.util.HashMap;

public class BackEnd {

    private static BackEnd instance = null;

    private Parser parser;

    public Requests request;

    private Cache cache;

    public static void main( String args[] ) {
        instance = new BackEnd();
        instance.setRequest( new Requests() );
        try {
            instance.request.sendGet();
        }
        catch( Exception e ) {
            System.out.println( e.getMessage() );
        }
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser( Parser parser ) {
        this.parser = parser;
    }

    public Requests getRequest() {
        return request;
    }

    public void setRequest( Requests request ) {
        this.request = request;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache( Cache cache ) {
        this.cache = cache;
    }

    private BackEnd() {
        this.parser = new Parser();
    }

    public BackEnd getInstance() {
        if( instance == null ) {
            instance = new BackEnd();
        }
        return instance;
    }

    public Document getDOM() {
        return null;
    }

    public Boolean makeRequest( String type, String url, String params ) {
        return null;
    }

    public ArrayList<HistoryItem> getHistory( HashMap<String, String> filters ) {
        return null;
    }

}

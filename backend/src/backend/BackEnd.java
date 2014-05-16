package backend;

import cache.Cache;
import cache.HistoryItem;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import requests.Requests;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.HashMap;

//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

public class BackEnd {

    private static BackEnd instance = null;

    private Requests request;

    private Cache cache;

    private int respCode;

    /**
     * getter for request object
     * @return Requests object that is responsible for making requests
     */
    public Requests getRequest() {
        return request;
    }

    /**
     * Assign the request objects
     * @param request -
     */
    public void setRequest( Requests request ) {
        this.request = request;
    }

    /**
     * getter for Cache
     * @return Cache object that represents the application repository
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * setter for respcode field
     * @param code - response code form page
     */
    public void setRespCode( int code ) {
        this.respCode = code;
    }

    /**
     * Pattern Singleton
     * Class constructor
     * Is private
     * It wants a single object instance for backend
     */
    private BackEnd() {
        this.request = new Requests();
    }

    /**
     * Static method return instace of object
     * @return instance of object BackEnd
     */
    public static BackEnd getInstance() {
        if( instance == null ) {
            instance = new BackEnd();
        }
        return instance;
    }

    /**
     * Method created for Front-End
     * It is responsible for parsing the dom tree of the web page
     * @param link web site link
     * @param typeRequest the type of request (get,post)
     * @param params - parameters for request
     * @return
     */
    public Document getDOM( String link, String typeRequest, HashMap<String, String> params ) {
        //String htmlResource = new String();

        request.setType( typeRequest );
        request.setParams( params );
        request.setUrl( link );
        //htmlResource = request.sendRequest();
        Document d = request.sendRequest();
         // Document d = parser.getDoc();
        Elements paragraphs = d.select( "script[src]" );
        for( org.jsoup.nodes.Element p : paragraphs ) { System.out.println( p.attr( "abs:src" ) ); }

        return d;
    }

    public String getResourcePath(String url) {
        return this.cache.resourceExists( url );
    }

  /*  public Boolean makeRequest( String type, String url, String params ) {
        return null;
    }*/

    /**
     * History filter after a certain characteristic
     * @param filters
     * @return one list of HistoryItems from filtering history
     */
    public ArrayList<HistoryItem> getHistory( HashMap<String, String> filters ) {
        return null;
    }
    
  /*  private static boolean skipNL;
    
    private static String printXML(Node rootNode) {
    String tab = "";
    skipNL = false;
    return(printXML(rootNode, tab));
}
private static String printXML(Node rootNode, String tab) {
    String print = "";
    if(rootNode.getNodeType()==Node.ELEMENT_NODE) {
        print += "\n"+tab+"<"+rootNode.getNodeName()+">";
    }
    NodeList nl = rootNode.getChildNodes();
    if(nl.getLength()>0) {
        for (int i = 0; i < nl.getLength(); i++) {
            print += printXML(nl.item(i), tab+"  ");    // \t
        }
    } else {
        if(rootNode.getNodeValue()!=null) {
            print = rootNode.getNodeValue();
        }
        skipNL = true;
    }
    if(rootNode.getNodeType()==Node.ELEMENT_NODE) {
        if(!skipNL) {
            print += "\n"+tab;
        }
        skipNL = false;
        print += "</"+rootNode.getNodeName()+">";
    }
    return(print);
} */

}

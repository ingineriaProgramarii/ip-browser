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


    public Requests getRequest() {
        return request;
    }

    public void setRequest( Requests request ) {
        this.request = request;
    }

    public Cache getCache() {
        return cache;
    }

    public void setRespCode( int code ) {
        this.respCode = code;
    }

    private BackEnd() {
        this.request = new Requests();
    }

    public static BackEnd getInstance() {
        if( instance == null ) {
            instance = new BackEnd();
        }
        return instance;
    }

    // functie apelata de cei de la front-end
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

  /*  public Boolean makeRequest( String type, String url, String params ) {
        return null;
    }*/

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

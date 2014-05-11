package backend;

import cache.Cache;
import cache.HistoryItem;
//import org.w3c.dom.Document;
import requests.Parser;
import requests.Requests;


import java.util.ArrayList;
import java.util.HashMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class BackEnd {

    private static BackEnd instance = null;

    private Parser parser;

    private Requests request;

    private Cache cache;

    public Parser getParser() {
        return parser;
    }

    /*public void setParser( Parser parser ) {
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
*/
    private BackEnd() {
        this.parser = new Parser();
        this.request = new Requests();
    }

    public static BackEnd getInstance() {
        if( instance == null ) {
            instance = new BackEnd();
        }
        return instance;
    }
    
    // functie apelata de cei de la front-end
    public Document getDOM(String link, String typeRequest, HashMap params) {
        String htmlResource = new String();
        
        request.setType(typeRequest);
        request.setParams(params);
        request.setUrl(link);
        htmlResource = request.sendRequest();
        parser.setHtml(htmlResource);
        parser.parse();
       // return 
         Document d = parser.getDoc();
    //     printXML(d);
      Elements paragraphs = d.select("p");
  for(Element p : paragraphs)
    System.out.println(p.text());
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

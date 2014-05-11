package requests;


import backend.BackEnd;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Requests {

    private String type;

    private String USER_AGENT;

    private HashMap<String, String> params;
    
    private String url;
    
    public void setUrl(String link) {
        this.url = link;
    }
    
    public String getUrl() {
        return this.url;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams( HashMap<String, String> params ) {
        this.params = params;
    }

    public Requests() {
        type = new String();
        params = new HashMap();
        url = new String();
    }
    
    public void getResource(Document domTree) 
    {
       /* Elements links = domTree.select("a[href]");
        //img si scripturi
     //   Elements scripts = domTree.select("script[src]"); 
        Elements media = domTree.select("[src]");
        Elements imports = domTree.select("link[href]");
        System.out.println("\nMedia : " + media.size());
        for (Element src : media)
        {
            if(src.tagName().equals("img"))
                System.out.println(" * " + src.tagName() + ": <" + src.attr("abs:src") + "> " + src.attr("width") +
                        " * " + src.attr("height")  + " (" + src.attr("alt") + ")");
            else
                System.out.println(" * " + src.tagName() + " : <" + src.attr("abs:src") + ">");
        }
        System.out.println("\nImports : " + imports.size());
        for (Element link : imports)
        {
            System.out.println(" * " + link.tagName() + ": <" + link.attr("abs:href") + "> " + " (" + link.attr("rel") + ")");
        }
        System.out.println("\nLinks : " + links.size());
        for (Element link : links)
        {
            System.out.println(" * a: <" + link.attr("abs:href") + "> " + " (" + link.text() + ")");
        }
	*/	
    }
    

    // HTTP GET request
    public Document sendGet()  {

        Document domTree = null;
        try {
            Response res = Jsoup.connect(this.url).method(Method.GET).execute();
            int code = res.statusCode();
            
            System.out.println("Headers: " + res.headers());
            System.out.println("Response code: " + code);
            
            if (code == 200)
            {
                BackEnd.getInstance().setRespCode(code);
                domTree = res.parse();
            }
            else
            {
                BackEnd.getInstance().setRespCode(code);
            }
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return domTree;
        

        //print result
        //System.out.println( response.toString() );
      /*  List<String> cookies = con.getHeaderFields().get( "Set-Cookie" );

        System.out.println( cookies.get( 0 ).toString() );
        System.out.println( cookies.get( 1 ).toString() );
        System.out.println( con.getContentType() ); */

    }

    // HTTP POST request
    public Document sendPost() throws Exception {

        HashMap<String, String> postParams = new HashMap<>();
        Document domTree = Jsoup.connect(this.url)
        .data(this.params)
        .post();
        System.out.println(domTree);
        return domTree;


    }
    
    public Document sendRequest() {
        
        //String requestResponse = new String();
        Document domTree = null;
        
        if (type.compareTo("GET") == 0)
        {
            try {
                domTree = this.sendGet();
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
        else
            if (type.compareTo("POST") == 0)
            {
                try {
                    domTree = this.sendPost();
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
             else
                 System.out.println("Metoda invalida");
        return domTree;
    }

}



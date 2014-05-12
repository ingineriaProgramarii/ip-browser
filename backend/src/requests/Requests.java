package requests;


import cache.Cache;
import cache.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.List;

public class Requests {

    private String type;

    private String USER_AGENT;

    private HashMap<String, String> params;

    private String url;

    private Cache cache;

    public void setUrl( String link ) {
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
        this.cache = new Cache();
    }

    public void getResource( Document domTree ) {
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

    public void getCookies( List<String> headers ) {
        for( String cookie : headers ) {
            String[] sc = cookie.split( ";" );
            Cookie c = new Cookie();
            String[] nameValue = sc[0].split( "=", 1 );
            c.setName( nameValue[0].trim() );
            c.setValue( nameValue[1].trim() );
            c.setSecure( false );
            for( int i = 1; i < sc.length; i++ ) {
                if( sc[i].trim().toLowerCase() == "secure" ) {
                    c.setSecure( true );
                }
                else if( sc[i].contains( "=" ) ) {
                    nameValue = sc[i].split( "=", 1 );
                    String key = nameValue[0].trim();
                    String value = nameValue[1].trim();
                    switch( key.toLowerCase() ) {
                        case "path":
                            c.setPath( value );
                            break;
                        case "domain":
                            c.setDomain( value );
                            break;
                        case "expires":
                            c.setExpireDate( value );
                            break;
                    }
                }
            }
            this.cache.addCookie( c );
        }
    }

    // HTTP GET request
    public Document sendGet() {

        URL obj = null;
        Document domTree = null;
        try {
            obj = new URL( this.url );

            HttpURLConnection con = ( HttpURLConnection ) obj.openConnection();

            // optional default is GET
            con.setRequestMethod( "GET" );

            //add request header
            con.setRequestProperty( "User-Agent", this.USER_AGENT );
            con.setRequestProperty( "Cookie", this.cache.getDomainCookies( this.getDomainName( this.url ), obj.getPath(), url
                    .startsWith( "https" ) ) );

            int responseCode = con.getResponseCode();
            System.out.println( "\nSending 'GET' request to URL : " + url );
            System.out.println( "Response Code : " + responseCode );

            BufferedReader in = new BufferedReader(
                    new InputStreamReader( con.getInputStream() ) );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while( ( inputLine = in.readLine() ) != null ) {
                response.append( inputLine );
            }

            this.getCookies( con.getHeaderFields().get( "Set-Cookie" ) );
            domTree = Jsoup.parse( response.toString() );
            in.close();
            System.out.println( "domTree:" );
            System.out.println( domTree );
            return domTree;
        }
        catch( MalformedURLException e ) {
            e.printStackTrace();
            return domTree;
        }
        catch( IOException e ) {
            e.printStackTrace();
            return domTree;
        }
    }

    // HTTP POST request
    public Document sendPost() throws Exception {

        HashMap<String, String> postParams = new HashMap<String, String>();
        Document domTree = Jsoup.connect( this.url )
                                .data( this.params )
                                .post();
        System.out.println( domTree );
        return domTree;

    }

    public Document sendRequest() {

        //String requestResponse = new String();
        Document domTree = null;

        if( type.compareTo( "GET" ) == 0 ) {
            try {
                domTree = this.sendGet();
            }
            catch( Exception ex ) {
                ex.getMessage();
            }
        }
        else if( type.compareTo( "POST" ) == 0 ) {
            try {
                domTree = this.sendPost();
            }
            catch( Exception ex ) {
                ex.getMessage();
            }
        }
        else { System.out.println( "Metoda invalida" ); }
        return domTree;
    }

    public String getDomainName( String url ) {
        URI uri = null;
        try {
            uri = new URI( url );
            String domain = uri.getHost();
            return domain.startsWith( "www." ) ? domain.substring( 4 ) : domain;
        }
        catch( URISyntaxException e ) {
            e.printStackTrace();
            return "";
        }
    }

}



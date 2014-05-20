package requests;


import cache.Cache;
import cache.Cookie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;

public class Requests {

    private String type;

    private String USER_AGENT;

    private HashMap<String, String> params;

    private String url;

    private Cache cache;

    /**
     * setter for link field
     * @param link
     */
    public void setUrl( String link ) {
        this.url = link;
    }

    /**
     * getter for url field
     * @return string url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * setter for type field - (type of request)
     * @param type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * getter for type of request
     * @return String type
     */
    public String getType() {
        return type;
    }

    /**
     * getter for parameters
     * @return HashMap<string, string> contains parameters
     */
    public HashMap<String, String> getParams() {
        return params;
    }

    /**
     * setter for parameters
     * @param params
     */
    public void setParams( HashMap<String, String> params ) {
        this.params = params;
    }

    /**
     * Constructor for requests
     * Initialize type, params and url
     */
    public Requests() {
        type = new String();
        params = new HashMap();
        url = new String();
        this.cache = new Cache();
    }

    /**
     * From domTree document extract resource and put in local directory
     * Local path are saved in data base
     * @param domTree
     */
    public void getResource( Document domTree ) {
        dirExists();

        Elements media = domTree.select( "[src]" );
        Elements imports = domTree.select( "link[href]" );



        String localPath = "";
        String uuid;

        System.out.println(media);

        for( Element src : media ) {
            String vasile = "";
            System.out.println("Link : " + src.attr("abs:src"));

            localPath = this.cache.resourceExists( src.attr("abs:src") );
            System.out.println(vasile);
            if( localPath == null ) {

                uuid = genUUID();
                String extension = getExtension( src.attr( "abs:src" ) );
                System.out.println("Accesez : " + src.attr( "abs:src" ));
                URL cale = null;
                try {
                    cale = new URL( src.attr( "abs:src" ) );
                }
                catch( MalformedURLException e ) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                String fileName = uuid + "." + extension;
                String basePath = new File( "" ).getAbsolutePath();
                String path = new File( basePath + "\\temp\\" + fileName ).getAbsolutePath();

                if( src.tagName().equals( "img" ) ) {
                    if( !( extension.substring( 0, 3 ).equals( "jpg" ) || extension.substring( 0, 3 ).equals( "gif" ) ||
                           extension.substring( 0, 3 ).equals( "png" ) || extension.substring( 0, 3 )
                                                                                   .equals( "tif" ) || extension.substring( 0, 4 )
                                                                                                                .equals( "jpeg" ) ) ) {
                        continue;
                    }
                    if( extension.length() > 3 && extension.substring( 0, 4 ).equals( "jpeg" ) ) {
                        extension = extension.substring( 0, 4 );
                    }
                    extension = extension.substring( 0, 3 );
                    fileName = uuid + "." + extension;
                    path = new File( basePath + "\\temp\\" + fileName ).getAbsolutePath();
                    System.out.println( "Extensia este : " + extension );
                    System.out
                          .println( " * " + src.tagName() + ": <" + src.attr( "abs:src" ) + "> " + src.attr( "width" ) +
                                    " * " + src.attr( "height" ) + " (" + src.attr( "alt" ) + ")" );

                    if( extension.equals( "gif" ) ) {
                        byte[] b = new byte[1];

                        try {
                            URLConnection urlConnection = cale.openConnection();
                            urlConnection.connect();
                            DataInputStream di = new DataInputStream( urlConnection.getInputStream() );

                            FileOutputStream fo = new FileOutputStream( path );
                            while( -1 != di.read( b, 0, 1 ) ) { fo.write( b, 0, 1 ); }
                            di.close();
                            fo.close();
                            this.cache.addResourceToDB( src.attr( "abs:src" ), path );
                        }
                        catch( IOException e ) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                    else {
                        try {
                            BufferedImage image = null;
                            image = ImageIO.read( cale );
                            if( image != null ) {
                                ImageIO.write( image, extension, new File( path ) );
                                this.cache.addResourceToDB( src.attr( "abs:src" ), path );
                            }
                        }
                        catch( IOException e ) {
                            System.out.println( "" );
                        }
                    }
                }
                else {
                    System.out.println( " * " + src.tagName() + " : <" + src.attr( "abs:src" ) + ">" );
                    if( src.tagName().equals( "script" ) ) {

                        extension = extension.substring( 0, 2 );
                        fileName = uuid + "." + extension;
                        path = new File( basePath + "\\temp\\" + fileName ).getAbsolutePath();

                        byte[] b = new byte[1];
                        URLConnection urlConnection = null;
                        try {
                            urlConnection = cale.openConnection();
                            urlConnection.connect();

                            DataInputStream di = new DataInputStream( urlConnection.getInputStream() );

                            FileOutputStream fo = new FileOutputStream( path );
                            while( -1 != di.read( b, 0, 1 ) ) { fo.write( b, 0, 1 ); }
                            di.close();
                            fo.close();

                            this.cache.addResourceToDB( src.attr( "abs:src" ), path );
                        }
                        catch( IOException e ) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                    }
                }
            }

        }
        for( Element link : imports ) {
            localPath = this.cache.resourceExists( link.attr( "abs:href" ) );
            if( localPath == null ) {
                if( link.attr( "rel" ).equals( "stylesheet" ) ) {
                    uuid = genUUID();
                    String extension = "css";

                    String fileName = uuid + "." + extension;
                    URL cale = null;
                    try {
                        cale = new URL( link.attr( "abs:href" ) );
                        String basePath = new File( "" ).getAbsolutePath();
                        String path = new File( basePath + "\\temp\\" + fileName ).getAbsolutePath();

                        byte[] b = new byte[1];
                        URLConnection urlConnection = cale.openConnection();
                        urlConnection.connect();

                        DataInputStream di = new DataInputStream( urlConnection.getInputStream() );

                        FileOutputStream fo = new FileOutputStream( path );
                        while( -1 != di.read( b, 0, 1 ) ) { fo.write( b, 0, 1 ); }
                        di.close();
                        fo.close();
                        this.cache.addResourceToDB( link.attr( "abs:href" ), path );
                    }
                    catch( MalformedURLException e ) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    catch( FileNotFoundException e ) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    catch( IOException e ) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
                else {
                    if( link.attr( "rel" ).contains( "icon" ) || link.attr( "rel" ).contains( "image" ) ) {
                        uuid = genUUID();
                        String extension = getExtension( link.attr( "abs:href" ) );
                        System.out.println( "Extensia : " + extension );
                        if( extension.equals( "ico" ) ) { System.out.println( "Fisier format ico" ); }
                        else {
                            String fileName = uuid + "." + extension;
                            URL cale = null;
                            try {
                                cale = new URL( link.attr( "abs:href" ) );
                                String basePath = new File( "" ).getAbsolutePath();
                                String path = new File( basePath + "\\temp\\" + fileName ).getAbsolutePath();

                                byte[] b = new byte[1];
                                URLConnection urlConnection = cale.openConnection();
                                urlConnection.connect();

                                DataInputStream di = new DataInputStream( urlConnection.getInputStream() );

                                FileOutputStream fo = new FileOutputStream( path );
                                while( -1 != di.read( b, 0, 1 ) ) { fo.write( b, 0, 1 ); }
                                di.close();
                                fo.close();
                                this.cache.addResourceToDB( link.attr( "abs:href" ), path );
                            }
                            catch( MalformedURLException e ) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                            catch( FileNotFoundException e ) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                            catch( IOException e ) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Extract extension for resource
     * @param path - path for resource
     * @return string - represent extension
     */
    private String getExtension( String path ) {
        String extension = "";
        int i = path.lastIndexOf( "." );
        if( i > 0 ) {
            extension = path.substring( i + 1 );
        }
        return extension;
    }

    /**
     * Generate UUID
     * @return a string which represent a randUUID
     */
    private String genUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll( "-", "" );
        return uuid;
    }

    /**
     * check if directory exists and create a new file temp
     */
    private void dirExists() {
        File f = new File( "temp" );
        if( !f.exists() || !f.isDirectory() ) { f.mkdir(); }
    }

    /**
     * Extract cookies from a list of headers and add him to catch cookie
     * @param headers
     */
    public void getCookies( List<String> headers ) {
        for( String cookie : headers ) {
            String[] sc = cookie.split( ";" );
            Cookie c = new Cookie("", "", "", "", "", 0);
            String[] nameValue = sc[0].split( "=", 2 );
            c.setName( nameValue[0].trim() );
            c.setValue( nameValue[1].trim() );
            c.setSecure( 0 );
            for( int i = 1; i < sc.length; i++ ) {
                if( sc[i].trim().toLowerCase().contains( "secure" ) ) {
                    c.setSecure( 1 );
                }
                else if( sc[i].contains( "=" ) ) {
                    nameValue = sc[i].split( "=", 2 );
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

    /**
     * Send Get request and return one Document as response
     * @return Document
     */
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
            con.setRequestProperty( "Cookie", this.cache
                    .getDomainCookies( this.getDomainName( this.url ), obj.getPath(), url
                            .startsWith( "https" ) ) );
            con.connect();

          //  System.out.println("Afis " + con.);
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

            System.out.println( con.getHeaderFields() );

            this.getCookies( con.getHeaderFields().get( "Set-Cookie" ) );

            this.cache.addHistoryItem( this.url, this.url );

            this.cache.saveCookies();
            domTree = Jsoup.parse( response.toString());
            System.out.println("DOM : " + domTree);
            in.close();
            Connection.Response res = Jsoup.connect(url).method( Connection.Method.GET).execute();
            Document doc = res.parse();
            this.getResource( doc );

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

    /**
     * Http send Post request and recieve one Document
     * @return Document
     * @throws Exception
     */
    public Document sendPost() throws Exception {

       /* HashMap<String, String> postParams = new HashMap<String, String>();
        Document domTree = Jsoup.connect( this.url )
                                .data( this.params )
                                .post();
        System.out.println( domTree );
        return domTree; */
        URL targetUrl;
        HttpURLConnection connection = null;
        try {
            //Create connection
            targetUrl = new URL(this.url);
            connection = (HttpURLConnection)targetUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                                          "application/x-www-form-urlencoded");
            connection.setRequestProperty( "Cookie", this.cache
                    .getDomainCookies( this.getDomainName( this.url ), targetUrl.getPath(), url
                            .startsWith( "https" ) ) );

           // StringBuilder parametriPost = new StringBuilder( "&" );
            StringBuilder parametriPost = new StringBuilder(  );
            for (Map.Entry<String, String> entry  : this.params.entrySet())
                parametriPost.append( entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
            parametriPost.deleteCharAt( parametriPost.length() -1 );

            connection.setRequestProperty("Content-Length", "" +
                                                            Integer.toString(parametriPost.toString().getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (parametriPost.toString());
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            //return response.toString();
            System.out.println( connection.getHeaderFields() );

            this.getCookies( connection.getHeaderFields().get( "Set-Cookie" ) );

            this.cache.addHistoryItem( this.url, this.url );

            this.cache.saveCookies();
            Document domTree = Jsoup.parse( response.toString());

            System.out.println(domTree);
            Connection.Response res = Jsoup.connect(url).method( Connection.Method.POST).data( this.params ).execute();
            Document doc = res.parse();
            this.getResource( doc );
            return  domTree;

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
        //return null;
    }

    /**
     * Send Request - get/post
     * @return Document - resource from server
     */
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

    /**
     * Get domain name form a ceratin url
     * @param url
     * @return string - domain name
     */
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



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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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


    private String getExtension( String path ) {
        String extension = "";
        int i = path.lastIndexOf( "." );
        if( i > 0 ) {
            extension = path.substring( i + 1 );
        }
        return extension;
    }

    private String genUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll( "-", "" );
        return uuid;
    }

    private void dirExists() {
        File f = new File( "temp" );
        if( !f.exists() || !f.isDirectory() ) { f.mkdir(); }
    }

    public void getCookies( List<String> headers ) {
        for( String cookie : headers ) {
            String[] sc = cookie.split( ";" );
            Cookie c = new Cookie();
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
                    System.out.println(value);
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
            this.cache.saveCookies();
            domTree = Jsoup.parse( response.toString());
            //System.out.println("DOM : " + domTree);
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



package requests;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Requests {

    private String type;

    private String USER_AGENT;

    private HashMap params;
    
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

    public HashMap getParams() {
        return params;
    }

    public void setParams( HashMap params ) {
        this.params = params;
    }

    public Requests() {
        type = new String();
        params = new HashMap();
        url = new String();
    }
    

    // HTTP GET request
    public String sendGet()  {

        //String url = "http://localhost/test.php";

        URL obj;
        String html = new String();
        try {
            obj = new URL( this.url );
            HttpURLConnection con = ( HttpURLConnection ) obj.openConnection();

            // optional default is GET
            con.setRequestMethod( "GET" );
            con.setDoOutput(true);
            //add request header
            con.setRequestProperty( "User-Agent", USER_AGENT );

            int responseCode = con.getResponseCode();
           // System.out.println( "\nSending 'GET' request to URL : " + url );
           System.out.println( "Response Code : " + responseCode );

            /*BufferedReader in = new BufferedReader(
                    new InputStreamReader( con.getInputStream() ) );
            String inputLine;
            StringBuffer response = new StringBuffer();

            while( ( inputLine = in.readLine() ) != null ) {
                response.append( inputLine );
            }
            in.close(); */

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n"); 
            }
            html = builder.toString();
            reader.close();
        } catch (MalformedURLException ex) {
            System.out.println("Exceptie la crearea url-ului");
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Exceptie la sendGet");
            System.out.println(ex.getMessage());
            
        }
        
       // System.out.println(html);
        return html;
        

        //print result
        //System.out.println( response.toString() );
      /*  List<String> cookies = con.getHeaderFields().get( "Set-Cookie" );

        System.out.println( cookies.get( 0 ).toString() );
        System.out.println( cookies.get( 1 ).toString() );
        System.out.println( con.getContentType() ); */

    }

    // HTTP POST request
    private String sendPost() throws Exception {

        //String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL( this.url );
        HttpsURLConnection con = ( HttpsURLConnection ) obj.openConnection();

        //add reuqest header
        con.setRequestMethod( "POST" );
        con.setRequestProperty( "User-Agent", USER_AGENT );
        con.setRequestProperty( "Accept-Language", "en-US,en;q=0.5" );

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput( true );
        DataOutputStream wr = new DataOutputStream( con.getOutputStream() );
        wr.writeBytes( urlParameters );
        wr.flush();
        wr.close();

        /*int responseCode = con.getResponseCode();
        //System.out.println( "\nSending 'POST' request to URL : " + url );
        //System.out.println( "Post parameters : " + urlParameters );
        //System.out.println( "Response Code : " + responseCode );

        BufferedReader in = new BufferedReader(
                new InputStreamReader( con.getInputStream() ) );
        String inputLine;
        StringBuffer response = new StringBuffer();

        while( ( inputLine = in.readLine() ) != null ) {
            response.append( inputLine );
        }
        in.close();*/
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\n"); 
        }
        String html = builder.toString();
        reader.close();
        return html;

        //print result
        //System.out.println( response.toString() );

    }
    
    public String sendRequest() {
        
        String requestResponse = new String();
        
        if (type.compareTo("GET") == 0)
        {
            try {
                requestResponse = this.sendGet();
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
        else
            if (type.compareTo("POST") == 0)
            {
                try {
                    requestResponse = this.sendPost();
                } catch (Exception ex) {
                    ex.getMessage();
                }
            }
             else
                 System.out.println("Metoda invalida");
        return requestResponse;
    }

}



package requests;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Requests {

    private String type;

    private String USER_AGENT;

    private ArrayList<String> params;

    public void sendRequest() {
    }

    public void setType( String type ) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams( ArrayList<String> params ) {
        this.params = params;
    }

    public Requests() {
    }

    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "http://localhost/test.php";

        URL obj = new URL( url );
        HttpURLConnection con = ( HttpURLConnection ) obj.openConnection();

        // optional default is GET
        con.setRequestMethod( "GET" );

        //add request header
        con.setRequestProperty( "User-Agent", USER_AGENT );

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
        in.close();

        //print result
        //System.out.println( response.toString() );
        List<String> cookies = con.getHeaderFields().get( "Set-Cookie" );

        System.out.println( cookies.get( 0 ).toString() );
        System.out.println( cookies.get( 1 ).toString() );
        System.out.println( con.getContentType() );

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL( url );
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

        int responseCode = con.getResponseCode();
        System.out.println( "\nSending 'POST' request to URL : " + url );
        System.out.println( "Post parameters : " + urlParameters );
        System.out.println( "Response Code : " + responseCode );

        BufferedReader in = new BufferedReader(
                new InputStreamReader( con.getInputStream() ) );
        String inputLine;
        StringBuffer response = new StringBuffer();

        while( ( inputLine = in.readLine() ) != null ) {
            response.append( inputLine );
        }
        in.close();

        //print result
        System.out.println( response.toString() );

    }

}

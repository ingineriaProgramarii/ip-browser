
import backend.BackEnd;
import org.jsoup.nodes.Document;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author fasdf
 */
public class MainClass {
    public static BackEnd backEnd;

    public static void main( String[] args ) {
        backEnd = BackEnd.getInstance();
        Document d = backEnd.getDOM( "http://google.com", "GET", null );
    }

}

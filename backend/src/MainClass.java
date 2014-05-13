
import backend.BackEnd;
import org.jsoup.nodes.Document;

import java.util.HashMap;

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
        HashMap<String, String> inputFields = new HashMap<>(  );
        inputFields.put( "lsd", "AVoPoI8k" );
        inputFields.put( "email", "ursulet.andreea@yahoo.com" );
        inputFields.put( "pass", "abyma8yqe6u2y2u" );
        Document d = backEnd.getDOM( "https://www.facebook.com/login.php?login_attempt=1", "POST",  inputFields);
        //Document doc = backEnd.getDOM( "https://www.google.ro/?gws_rd=cr&ei=XRGZUtDqOMrJ4ATJi4CoCg", "GET", inputFields);

    }

}

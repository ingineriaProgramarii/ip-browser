
import backend.BackEnd;
import org.jsoup.nodes.Document;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pispirica
 */
public class MainClass {
    public static BackEnd backEnd;

    public static void main(String[] args) {
        backEnd = BackEnd.getInstance();
        Document d = backEnd.getDOM("http://www.emag.ro/telefon-mobil-samsung-galaxy-note-3-n9005-5-7-13mp-32gb-wi-fi-4g-android-4-3-pink-sm-n9005zierom/pd/DRSRJBBBM/", "GET", null);
    }
    
}

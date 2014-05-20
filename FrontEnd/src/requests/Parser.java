package requests;

//import org.w3c.dom.Document;
import org.jsoup.*;
import org.jsoup.nodes.Document;

public class Parser {

    public Document doc;

    public String html;

    /**
     * getter for document
     * @return Document
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * setter for document
     * @param doc
     */
    public void setDoc( Document doc ) {
        this.doc = doc;
    }

    /**
     * getter for html
     * @return String format of html
     */
    public String getHtml() {
        return html;
    }

    /**
     * setter for html - string format
     * @param html
     */
    public void setHtml( String html ) {
        this.html = html;
    }

    /**
     * Parse the html document with jsoup and init doc with result
     */
    public void parse() {
        doc =  Jsoup.parse(html);
    }

}

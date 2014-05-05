package requests;

import org.w3c.dom.Document;

public class Parser {

    public Document doc;

    public String html;

    public Document getDoc() {
        return doc;
    }

    public void setDoc( Document doc ) {
        this.doc = doc;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml( String html ) {
        this.html = html;
    }

    public Document getDOM() {
        return null;
    }

    public void parse( String HTML ) {
    }

    public Parser() {
    }
}

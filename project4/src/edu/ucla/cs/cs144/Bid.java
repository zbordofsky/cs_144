/* CS144
 *
 * TODO: Add info about file here
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;


public class Bid {

    private String Bidder;
    private String Rating;
    private String Location;
    private String Country;
    private String Time;
    private String Amount;

    public Bid(Element bid) throws Exception {
        
        Element bidder = MyParser.getElementByTagNameNR(bid, "Bidder");
        this.Bidder = bidder.getAttribute("UserID");
        this.Rating = bidder.getAttribute("Rating");
        this.Location = MyParser.getElementTextByTagNameNR(bidder, "Location");
        if (this.Location == "")
            this.Location = "N/A";
        this.Country = MyParser.getElementTextByTagNameNR(bidder, "Country");
        if (this.Country == "")
            this.Country = "N/A";
        this.Time = MyParser.getElementTextByTagNameNR(bid, "Time");
        this.Amount = MyParser.getElementTextByTagNameNR(bid, "Amount");

    }

    public String getBidder() {
        return this.Bidder;
    }

    public String getRating() {
        return this.Rating;
    }

    public String getLocation() {
        return this.Location;
    }

    public String getCountry() {
        return this.Country;
    }

    public String getTime() {
        return this.Time;
    }

    public String getAmount() {
        return this.Amount;
    }

}

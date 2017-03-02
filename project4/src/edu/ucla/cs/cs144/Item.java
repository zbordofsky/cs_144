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


class Item {

    private String ItemID;
    private String Name;
    private List<String> Categories;
    private String Currently;
    private String Buy_Price;
    private String First_Bid;
    private String Number_of_Bids;
    private List<Bid> Bids;
    private String Latitude;
    private String Longitude;
    private String Location;
    private String Country;
    private String Started;
    private String Ends;
    private String SellerID;
    private String SellerRating;
    private String Description;

    public Item(String itemXML) throws Exception {
        
        // Initialize lists to hold all relevant categories and current bids that apply to item
        Categories = new ArrayList<String>();
        Bids = new ArrayList<Bid>();

        // Convert input XML string to document we can parse more effectively
        Document docXML = getDocumentFromXML(itemXML);

        Element item = docXML.getDocumentElement();
        this.ItemID = item.getAttribute("ItemID");
        this.Name = MyParser.getElementTextByTagNameNR(item, "Name");
        this.Description = MyParser.getElementTextByTagNameNR(item, "Description");
        this.Categories = getCategoriesFromElement(item);
        this.Currently = MyParser.getElementTextByTagNameNR(item, "Currently");
        this.Buy_Price = MyParser.getElementTextByTagNameNR(item, "Buy_Price");
        if (this.Buy_Price == "")
            this.Buy_Price = "N/A";
        this.First_Bid = MyParser.getElementTextByTagNameNR(item, "First_Bid");
        this.Number_of_Bids = MyParser.getElementTextByTagNameNR(item, "Number_of_Bids");
        this.Bids = getBidsFromElement(item);
       
        Element location = MyParser.getElementByTagNameNR(item, "Location");
        this.Latitude = location.getAttribute("Latitude");
        if (this.Latitude == "")
            this.Latitude = "N/A";
        this.Longitude = location.getAttribute("Longitude");
        if (this.Longitude == "")
            this.Longitude = "N/A";
        this.Location = MyParser.getElementText(location);
        this.Country = MyParser.getElementTextByTagNameNR(item, "Country");

        this.Started = MyParser.getElementTextByTagNameNR(item, "Started");
        this.Ends = MyParser.getElementTextByTagNameNR(item, "Ends");

        Element seller = MyParser.getElementByTagNameNR(item, "Seller");
        this.SellerID = seller.getAttribute("UserID");
        this.SellerRating = seller.getAttribute("Rating");
    }

    private Document getDocumentFromXML(String itemXML) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        InputSource inpSource = new InputSource(new StringReader(itemXML));
        return docBuilder.parse(inpSource);
    }

    private List<String> getCategoriesFromElement(Element item) throws Exception {
        List<String> categoryList = new ArrayList<String>();
        Element[] categories = MyParser.getElementsByTagNameNR(item, "Category");
        for (int i = 0; i < categories.length; i++) {
            String cat = MyParser.getElementText(categories[i]);
            categoryList.add(cat);
        }
        return categoryList;
    }

    private List<Bid> getBidsFromElement(Element item) throws Exception {
        List<Bid> bidList = new ArrayList<Bid>();
        Element bids_list = MyParser.getElementByTagNameNR(item, "Bids");
        Element[] bids = MyParser.getElementsByTagNameNR(bids_list, "Bid");
        for (int i = 0; i < bids.length; i++) {
            Bid currentBid = new Bid(bids[i]);
            bidList.add(currentBid);
        }
        return bidList;
    }

    public String getItemId() {
        return this.ItemID;
    }

    public String getName() {
        return this.Name;
    }

    public List<String> getCategories() {
        return this.Categories;
    }

    public String getCurrentBid() {
        return this.Currently;
    }

    public String getBuyPrice() {
        return this.Buy_Price;
    }

    public String getFirstBid() {
        return this.First_Bid;
    }

    public String getNumBids() {
        return this.Number_of_Bids;
    }

    public List<Bid> getBids() {
        return this.Bids;
    }

    public String getLatitude() {
        return this.Latitude;
    }

    public String getLongitude() {
        return this.Longitude;
    }

    public String getLocation() {
        return this.Location;
    }

    public String getCountry() {
        return this.Country;
    }

    public String getStartDate() {
        return this.Started;
    }

    public String getEndDate() {
        return this.Ends;
    }

    public String getSellerID() {
        return this.SellerID;
    }

    public String getSellerRating() {
        return this.SellerRating;
    }

    public String getDescription() {
        return this.Description;
    }

}

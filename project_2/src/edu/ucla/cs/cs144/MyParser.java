/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
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


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };

    private static FileWriter item_dat;
    private static FileWriter category_dat;
    private static FileWriter bidder_dat;
    private static FileWriter seller_dat;
    private static FileWriter bid_dat;
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element[] items = getElementsByTagNameNR(doc.getDocumentElement(), "Item"); 
        for(int i = 0; i < items.length; i++) {

            String[] item_dat_line;

            String id = items[i].getAttribute("ItemID"); 
            String name = getElementTextByTagNameNR(items[i], "Name");
            String description = getElementTextByTagNameNR(items[i], "Description");
            if(description.length() > 4000)
                description = description.substring(0, 4000); 
            System.out.println("item number: " + i);
            String currently = strip(getElementTextByTagNameNR(items[i], "Currently")); 
            String buy_price = strip(getElementTextByTagNameNR(items[i], "Buy_Price"));
            String first_bid = strip(getElementTextByTagNameNR(items[i], "First_Bid"));
            String number_of_bids = getElementTextByTagNameNR(items[i], "Number_of_Bids");
            Element location = getElementByTagNameNR(items[i], "Location"); 
            String latitude = location.getAttribute("Latitude"); 
            String longitude = location.getAttribute("Longitude");
            String location_text = getElementText(location); 
            String country = getElementTextByTagNameNR(items[i], "Country");
            String started = getElementTextByTagNameNR(items[i], "Started");
            String ends = getElementTextByTagNameNR(items[i], "Ends");
            String format_started = format_timestamp(started); 
            String format_ends = format_timestamp(ends);

            Element seller = getElementByTagNameNR(items[i], "Seller"); 
            String seller_id = seller.getAttribute("UserID");

            print_load_line(item_dat, id, name, description, seller_id, currently, buy_price,
                first_bid, number_of_bids, location_text, latitude, longitude, country,
                    format_started, format_ends);

            create_seller(seller); 
            create_categories(items[i]);
            create_bid(items[i]); 
        }
        
        
        /**************************************************************/
        
    }

    static void create_bid(Element items) {
        String item_id = items.getAttribute("ItemID"); 
        Element bids_list = getElementByTagNameNR(items, "Bids"); 
        Element[] bids = getElementsByTagNameNR(bids_list, "Bid"); 
        for(int i = 0; i < bids.length; i++) {
            Element bidder = getElementByTagNameNR(bids[i], "Bidder"); 

            String bidder_id = bidder.getAttribute("UserID");
            String bidder_Rating = bidder.getAttribute("Rating"); 
            String bidder_Location = getElementTextByTagNameNR(bidder, "Location");
            String bidder_Country = getElementTextByTagNameNR(bidder, "Country");
            //String bidder_rating = bids[i].getAttribute("UserID"); 
            String amount = strip(getElementTextByTagNameNR(bids[i], "Amount")); 
            String time = getElementTextByTagNameNR(bids[i], "Time"); 
            String format_time = format_timestamp(time); 

            System.out.println(bidder_id);
            System.out.println(amount);
            System.out.println(format_time); 
            System.out.println(bidder_Country);
            System.out.println(bidder_Location); 
            System.out.println(bidder_Rating);

            print_load_line(bidder_dat, bidder_id, bidder_Rating, bidder_Location, bidder_Country);
            print_load_line(bid_dat, item_id, bidder_id, format_time, amount);  
        } 
    }

    static void create_categories(Element item) {
        String id = item.getAttribute("ItemID"); 
        Element[] categories = getElementsByTagNameNR(item, "Category");
        for(int i = 0; i < categories.length; i++) {
            String cat = getElementText(categories[i]);
            print_load_line(category_dat, id, cat); 
        }  
    }
    static String format_timestamp(String date_time)  {

        String inputFormat  = "MMM-dd-yy HH:mm:ss";
        String outputFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat java_to_mysql = new SimpleDateFormat(inputFormat);
        Date reformat;
        String outputTimestamp = "";

        try {
            reformat = java_to_mysql.parse(date_time);
            java_to_mysql.applyPattern(outputFormat);
            outputTimestamp = java_to_mysql.format(reformat);
        }
        catch(ParseException error) {
            System.err.println("ERROR: Cannot parse input \"" + date_time + "\"");
        }

        return outputTimestamp;
    }

    static void create_seller(Element seller){
        String seller_id = seller.getAttribute("UserID"); 
        String rating = seller.getAttribute("Rating");
        print_load_line(seller_dat, seller_id, rating);
    }

    static void print_load_line(FileWriter load_file, String... data) {

        String data_entry = "";
        
        for (int i = 0; i < (data.length-1); i++) {
            data_entry += data[i] + columnSeparator;
        }
        
        data_entry += data[data.length-1] + "\n";

        try {
            load_file.write(data_entry);
        }
        catch (IOException error) {
            System.err.println("ERROR: Cannot print data to load file");
        }
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        try {
            item_dat     = new FileWriter("items.dat", true);
            category_dat = new FileWriter("categories.dat", true);
            bidder_dat   = new FileWriter("bidders.dat", true);
            seller_dat   = new FileWriter("sellers.dat", true);
            bid_dat      = new FileWriter("bids.dat", true);

            /* Process all files listed on command line. */
            for (int i = 0; i < args.length; i++) {
                File currentFile = new File(args[i]);
                processFile(currentFile);
            }

            item_dat.close();
            category_dat.close();
            bidder_dat.close();
            seller_dat.close();
            bid_dat.close();
        }
        catch (IOException error) {
            System.out.println("ERROR: Cannot open load files for writing");
        }
    }
}

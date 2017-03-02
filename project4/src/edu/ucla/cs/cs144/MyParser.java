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


public class MyParser {
    
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

    /* Load file for Item relation, whicb will contain info about items being sold  */
    private static FileWriter item_dat;

    /* Load file for Category relation, which will give the categories for a given item  */
    private static FileWriter category_dat;

    /* Load file for Bidder relation, which will contain bidding-related info of users  */
    private static FileWriter bidder_dat;

    /* Load file for Seller relation, which will contain selling-related info of users  */
    private static FileWriter seller_dat;

    /* Load file for Bid relation, which will contain info about all bids made on all items  */
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

        /* Get a list of all items under the root Element */
        Element[] items = getElementsByTagNameNR(doc.getDocumentElement(), "Item"); 

        /* Iterate through each item, acquiring the data necessary to generate the load files.
           In this function, we will populate the 'items.dat' load file. We call separate functions
           to generate the 'categories.dat', 'bidders.dat', 'bids.dat', and 'sellers.dat' files.  */
        for(int i = 0; i < items.length; i++) {

            /* Get unique identifier # for item, as well as its given name and description  */
            String id = items[i].getAttribute("ItemID");
            String name = getElementTextByTagNameNR(items[i], "Name");
            String description = getElementTextByTagNameNR(items[i], "Description");
            if(description.length() > 4000)
                description = description.substring(0, 4000);  // max len allowed is 4000 chars

            /* Get bidding / pricing related info for this item  */
            String currently = strip(getElementTextByTagNameNR(items[i], "Currently"));

            /* If buy_price is null, we need to use '\N' for load file to get NULL.
               Otherwise, we will get 0.00  */ 
            String buy_price = strip(getElementTextByTagNameNR(items[i], "Buy_Price"));
            if (buy_price == "")
                buy_price = "\\N";
            String first_bid = strip(getElementTextByTagNameNR(items[i], "First_Bid"));
            String number_of_bids = getElementTextByTagNameNR(items[i], "Number_of_Bids");

            /* Get text description of item's location, as well as latitude/longitude (if specified) */
            Element location = getElementByTagNameNR(items[i], "Location"); 
            String latitude = location.getAttribute("Latitude"); 
            if (latitude == "")
            	latitude = "\\N";
            String longitude = location.getAttribute("Longitude");
            if (longitude == "")
            	longitude = "\\N";
            String location_text = getElementText(location); 
            String country = getElementTextByTagNameNR(items[i], "Country");

            /* Get bidding time window for this item, reformatting to match with MySQL TIMESTAMP  */
            String started = getElementTextByTagNameNR(items[i], "Started");
            String ends = getElementTextByTagNameNR(items[i], "Ends");
            String format_started = format_timestamp(started); 
            String format_ends = format_timestamp(ends);

            /* Determine who is selling this item (just id, more info goes in Seller table)  */
            Element seller = getElementByTagNameNR(items[i], "Seller"); 
            String seller_id = seller.getAttribute("UserID");

            /* Add an entry to the 'items.dat' load file, i.e. a row in the Item table. The order
               in which the values are passed in is the order they will appear in the table  */
            print_load_line(item_dat, id, name, description, seller_id, currently, buy_price,
                first_bid, number_of_bids, location_text, latitude, longitude, country,
                    format_started, format_ends);

            /* Acquire more data from current Element to populate 'sellers.dat'  */
            create_seller(seller); 

            /* Acquire more data from current Element to populate 'categories.dat'  */
            create_categories(items[i]);

            /* Acquire more data from current Element to populate 'bids.dat' and 'bidders.dat'  */
            create_bid(items[i]); 
        }
             
        /**************************************************************/
        
    }

    /* Function for populating 'bids.dat' and 'bidders.dat' load files  */
    static void create_bid(Element items) {

        /* Item id tells us which item this bid is for  */
        String item_id = items.getAttribute("ItemID");

        /* Get list of bids for this item  */ 
        Element bids_list = getElementByTagNameNR(items, "Bids"); 
        Element[] bids = getElementsByTagNameNR(bids_list, "Bid");
        for(int i = 0; i < bids.length; i++) {

            /* Get bidder info (id, rating, location) for the 'bidders.dat' load file  */
            Element bidder = getElementByTagNameNR(bids[i], "Bidder"); 
            String bidder_id = bidder.getAttribute("UserID");
            String bidder_Rating = bidder.getAttribute("Rating"); 
            String bidder_Location = getElementTextByTagNameNR(bidder, "Location");
            if (bidder_Location == "")
            	bidder_Location = "\\N";
            String bidder_Country = getElementTextByTagNameNR(bidder, "Country");
            if (bidder_Country == "")
            	bidder_Country = "\\N";
            print_load_line(bidder_dat, bidder_id, bidder_Rating, bidder_Location, bidder_Country);            

            /* Get info about bid (bid amount, what time bid was made) for 'bids.dat' load file  */
            String amount = strip(getElementTextByTagNameNR(bids[i], "Amount")); 
            String time = getElementTextByTagNameNR(bids[i], "Time"); 
            String format_time = format_timestamp(time); 
            print_load_line(bid_dat, item_id, bidder_id, format_time, amount);  
        } 
    }

    /* Function for populating 'categories.dat' load file  */
    static void create_categories(Element item) {   
        /* Add a line to the load file for each category that an item falls under  */
        String id = item.getAttribute("ItemID");
        Element[] categories = getElementsByTagNameNR(item, "Category");
        for(int i = 0; i < categories.length; i++) {
            String cat = getElementText(categories[i]);
            print_load_line(category_dat, id, cat); 
        }  
    }

    /* Function for converting dates/times given in XML to TIMESTAMP format for MySQL  */
    static String format_timestamp(String date_time)  {

        String inputFormat  = "MMM-dd-yy HH:mm:ss";     /* Format in the XML  */
        String outputFormat = "yyyy-MM-dd HH:mm:ss";    /* Desired format for MySQL TIMESTAMP  */

        SimpleDateFormat java_to_mysql = new SimpleDateFormat(inputFormat);
        Date reformat;
        String outputTimestamp = "";

        /* Perform conversion using the 'SimpleDateFormat'  */
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

    /* Function for populating 'sellers.dat' load file  */
    static void create_seller(Element seller){
        String seller_id = seller.getAttribute("UserID"); 
        String rating = seller.getAttribute("Rating");
        print_load_line(seller_dat, seller_id, rating);
    }

    /* Function for printing a line on a specified 'load_file'. 'String... data' represents a
     * variable number of inputs (depends on which load file we're writing to) that contain the data */
    static void print_load_line(FileWriter load_file, String... data) {

        String data_entry = "";

        /* Separate each value by the specified column separator  */
        for (int i = 0; i < (data.length-1); i++) {
            data_entry += data[i] + columnSeparator;
        }
        
        /* Dont' put a column separator after the last value for the entry, use newline to end entry  */
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
            /* Instantiate FileWriter for each of the 5 load files we will be generating  */
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

            /* Close the load files when we are finished processing all files  */
            item_dat.close();
            category_dat.close();
            bidder_dat.close();
            seller_dat.close();
            bid_dat.close();
        }
        catch (IOException error) {
            System.err.println("ERROR: Cannot open load files for writing");
        }
    }
}

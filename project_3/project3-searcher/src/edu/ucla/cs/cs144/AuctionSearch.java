package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet; 
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) 
	{
		/* Use ArrayList to easily add search results w/o worrying about space allocation for our result  */
		ArrayList<SearchResult> basicResults = new ArrayList<SearchResult>();

		try {

			/* The minimum # of hits we must check to return `numResultsToReturn`  */
			int minResults = numResultsToReturn + numResultsToSkip;

			/* Access the Lucene index we created to obtain the top matches for the given query  */
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index")))); 
			QueryParser parser = new QueryParser("content", new StandardAnalyzer()); 
			Query queryObj = parser.parse(query); 
			TopDocs docs = searcher.search(queryObj, minResults); 
			ScoreDoc[] hits = docs.scoreDocs;
			
			/* Skipping over the first `numResultsToSkip` results, add the remainder to our SearchResult[]  */
			for(int i = numResultsToSkip; i < minResults; i++) {
				
				/* We haven't returned `numResultsToReturn` results but there are no more results to add  */
				if(i >= hits.length) {
					break; 
				}

				Document resultDoc = searcher.doc(hits[i].doc); 
				basicResults.add(new SearchResult(resultDoc.get("ItemId"), resultDoc.get("Name")));
			}
		} 
		catch(IOException | ParseException ex) {
			System.err.println(ex); 
		}
		
		/* Convert our ArrayList to a SearchResult[] of correct size  */
		SearchResult[] endResults = basicResults.toArray(new SearchResult[basicResults.size()]);
		return endResults; 
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) 
	{
		/* Use ArrayList to add results w/o worrying about space allocation  */
		ArrayList<SearchResult> spatialResults = new ArrayList<SearchResult>();

		try {
			
			/* Access the Lucene index we created to obtain the top matches for the given query  */
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index")))); 
			QueryParser parser = new QueryParser("content", new StandardAnalyzer()); 
			Query queryObj = parser.parse(query); 
			TopDocs docs = searcher.search(queryObj, Integer.MAX_VALUE);
			ScoreDoc[] hits = docs.scoreDocs; 
		
			/* Connect to the database and get all items found in the given search region  */
			Connection conn = DbManager.getConnection(true);
        	Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        	String locationQuery = String.format("SELECT itemID FROM ItemLocation WHERE MBRContains(GeomFromText('Polygon((%f %f, %f %f, %f %f, %f %f, %f %f))'), latLong)", 
        		region.getLx(), region.getLy(), region.getLx(), region.getRy(), region.getRx(), region.getRy(), region.getRx(), region.getLy(), region.getLx(), region.getLy()); 
        	ResultSet locationResult = stmt.executeQuery(locationQuery);
        	
        	/* Add all items in the given SearchRegion to HashSet  */
        	HashSet <String> locationIDs = new HashSet <String>(); 
        	while(locationResult.next()) {
        		locationIDs.add(locationResult.getString("itemID"));
        	}

        	/* For every hit return by the keyword search, use HashSet to quickly check if the item is
        	   within the search region. Continue until we have added the number of desired results or
        	   until we have examined every hit returned by the keyword search.  */
        	int skips = 0;
        	int numAdded = 0; 
			for(int i = 0; (numAdded < numResultsToReturn) && (i < hits.length); i++) {
				Document doc = searcher.doc(hits[i].doc); 
				if(locationIDs.contains(doc.get("ItemId"))) {
					if(skips < numResultsToSkip) {
						skips++; 
					}
					else {
						spatialResults.add(new SearchResult(doc.get("ItemId"), doc.get("Name"))); 
						numAdded++; 
					}
				}
			}

			conn.close();
		}
		catch(IOException | ParseException | SQLException ex) {
			System.err.println(ex); 
		}
		
		/* Convert our ArrayList to a SearchResult[] of correct size  */
		SearchResult[] endResults = spatialResults.toArray(new SearchResult[spatialResults.size()]);
		return endResults; 
	}

	public String getXMLDataForItemId(String itemId) {
		
		/* Iteratively build XML text output for the given item  */
		String xmlOutput = "";

		try {
			
			/* Open database connection to retrieve all info associated with the given item id  */
			Connection conn = DbManager.getConnection(true);
			Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			/* Obtain all information from Item table. Escape characters '<', '>', '&', '"', '''  */
			ResultSet itemContents = stmt.executeQuery("SELECT * FROM Item WHERE itemID = " + itemId);		
			if (!itemContents.next()) {
				return "";
			}
			String name = escapeCharacters(itemContents.getString("name"));
			String description = escapeCharacters(itemContents.getString("description"));
			String userID = escapeCharacters(itemContents.getString("userID"));
			String currently = escapeCharacters(itemContents.getString("currently"));
			String buyPrice = escapeCharacters(itemContents.getString("buyPrice"));
			String firstBid = escapeCharacters(itemContents.getString("firstBid"));
			String numBids = escapeCharacters(itemContents.getString("numBids"));
			String location = escapeCharacters(itemContents.getString("location"));
			String latitude = escapeCharacters(itemContents.getString("latitude"));
			String longitude = escapeCharacters(itemContents.getString("longitude"));
			String country = escapeCharacters(itemContents.getString("country"));
			String started = escapeCharacters(itemContents.getString("started"));
			String ends = escapeCharacters(itemContents.getString("ends"));

			/* Obtain all information from Category table. Escape characters as needed  */
			ResultSet categoryContents = stmt.executeQuery("SELECT category FROM Category WHERE itemID = " + itemId);
			ArrayList<String> itemCategories = new ArrayList<String>();
			while (categoryContents.next()) {
				itemCategories.add(escapeCharacters(categoryContents.getString("category")));
			}

			/* Obtain all information from Seller table. Escape characters as needed  */
			ResultSet sellerContents = stmt.executeQuery("SELECT sellerRating FROM Seller WHERE userID = '" + userID + "'");
			sellerContents.next();
			String sellerRating = escapeCharacters(sellerContents.getString("sellerRating"));

			/* Begin building the XML output string  */
			xmlOutput += String.format("<Item ItemID=\"%s\">\n", itemId);
			xmlOutput += String.format("  <Name>%s</Name>\n", name);
			for (String category : itemCategories)
				xmlOutput += String.format("  <Category>%s</Category>\n", category);
			xmlOutput += String.format("  <Currently>$%s</Currently>\n", currently);
			if (!buyPrice.equals(""))
				xmlOutput += String.format("  <Buy_Price>$%s</Buy_Price>\n", buyPrice);
			xmlOutput += String.format("  <First_Bid>$%s</First_Bid>\n", firstBid);
			xmlOutput += String.format("  <Number_of_Bids>%s</Number_of_Bids>\n", numBids);

			/* Add all bid info, if any exists, to the XML output  */
			if (Integer.parseInt(numBids) != 0) {
				ResultSet bidContents = stmt.executeQuery("SELECT userID, time, amount FROM Bid WHERE itemID = " + itemId);
				xmlOutput += "  <Bids>\n";
				while (bidContents.next()) {
					String bidderID = escapeCharacters(bidContents.getString("userID"));
					String time = escapeCharacters(bidContents.getString("time"));
					String amount = escapeCharacters(bidContents.getString("amount"));
					
					Statement stmt2 = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
					ResultSet bidderInfo = stmt2.executeQuery("SELECT bidderRating, location, country FROM Bidder WHERE userID = '" + bidderID + "'");
					bidderInfo.next();
					String bidderRating = escapeCharacters(bidderInfo.getString("bidderRating"));
					String bidderLocation = escapeCharacters(bidderInfo.getString("location"));
					String bidderCountry = escapeCharacters(bidderInfo.getString("country"));

					xmlOutput += "    <Bid>\n";
					xmlOutput += String.format("      <Bidder Rating=\"%s\" UserID=\"%s\">\n",bidderRating,bidderID);
					if (!bidderLocation.equals(""))
						xmlOutput += String.format("        <Location>%s</Location>\n", bidderLocation);
					if (!bidderCountry.equals(""))
						xmlOutput += String.format("        <Country>%s</Country>\n", bidderCountry);
					xmlOutput += "      </Bidder>\n";
					xmlOutput += String.format("      <Time>%s</Time>\n", format_timestamp(time));
					xmlOutput += String.format("      <Amount>$%s</Amount>\n", amount);
					xmlOutput += "    </Bid>\n";
				}
				xmlOutput += "  </Bids>\n";
			}
			else {
				xmlOutput += "  <Bids />\n";
			}

			/* Fill out remaining info in XML output  */
			xmlOutput += "  <Location";
			if (!latitude.equals(""))
				xmlOutput += String.format(" Latitude=\"%s\"", latitude);
			if (!longitude.equals(""))
				xmlOutput += String.format(" Longitude=\"%s\"", longitude);
			xmlOutput += String.format(">%s</Location>\n", location);
			xmlOutput += String.format("  <Country>%s</Country>\n", country);
			xmlOutput += String.format("  <Started>%s</Started>\n", format_timestamp(started));
			xmlOutput += String.format("  <Ends>%s</Ends>\n", format_timestamp(ends));
			xmlOutput += String.format("  <Seller Rating=\"%s\" UserID=\"%s\" />\n", sellerRating, userID);
			xmlOutput += String.format("  <Description>%s</Description>\n", description);
			xmlOutput += "</Item>";
		}
		catch (SQLException ex) {
			System.err.println(ex);
		}

		return xmlOutput;
	}

	/* Escape characters '<', '>', '&', '"', ''' with appropriate replacements for a given string */
	private String escapeCharacters(String field) {
		
		/* Used for optional fields, like latitude or longitude  */
		if (field == null) {
			return "";
		}

		String escapedText = "";
		char currentChar;
		for (int i = 0; i < field.length(); i++) {
			currentChar = field.charAt(i);
			if (currentChar == '<')
				escapedText = escapedText + "&lt;";
			else if (currentChar == '>')
				escapedText = escapedText + "&gt;";
			else if (currentChar == '&')
				escapedText = escapedText + "&amp;";
			else if (currentChar == '\"')
				escapedText = escapedText + "&quot;";
			else if (currentChar == '\'')
				escapedText = escapedText + "&apos;";
			else
				escapedText = escapedText + currentChar;
		}

		return escapedText;
	}

    /* Function for converting dates/times given in MySQL TIMESTAMP format to XML  */
    private static String format_timestamp(String date_time) {

        String outputFormat  = "MMM-dd-yy HH:mm:ss";     /* Format in the XML  */
        String inputFormat  = "yyyy-MM-dd HH:mm:ss";    /* Format for MySQL TIMESTAMP  */

        SimpleDateFormat mysql_to_java = new SimpleDateFormat(inputFormat);
        Date reformat;
        String outputTimestamp = "";

        /* Perform conversion using the 'SimpleDateFormat'  */
        try {
            reformat = mysql_to_java.parse(date_time);
            mysql_to_java.applyPattern(outputFormat);
            outputTimestamp = mysql_to_java.format(reformat);
        }
        catch(Exception error) {
            System.err.println(error);
        }

        return outputTimestamp;
    }

	public String echo(String message) {
		return message;
	}
}

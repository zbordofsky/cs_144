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
			int numResultsToReturn) {
		// TODO: Your code here!

		SearchResult[] results = {}; 
		try {
			int minResults = numResultsToReturn + numResultsToSkip; 
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index")))); 
			QueryParser parser = new QueryParser("content", new StandardAnalyzer()); 
			Query queryObj = parser.parse(query); 
			TopDocs docs = searcher.search(queryObj, minResults); 
			ScoreDoc[] hits = docs.scoreDocs; 
			results = new SearchResult[numResultsToReturn]; 
			
			for(int i = numResultsToSkip; i < minResults; i++) {
				if(i >= hits.length) {
					break; 
				}
				Document resultDoc = searcher.doc(hits[i].doc); 
				results[i] = new SearchResult(resultDoc.get("ItemId"), resultDoc.get("Name")); 
			}
		} catch(IOException | ParseException ex) {
			System.err.println(ex); 
		}
		
		return results; 
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		try {
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index")))); 
			QueryParser parser = new QueryParser("content", new StandardAnalyzer()); 
			Query queryObj = parser.parse(query); 
			TopDocs docs = searcher.search(queryObj, Integer.MAX_VALUE); //keyword search results 
		
			SearchResult[] spatialResult; 

		
			Connection conn = DbManager.getConnection(true);
        	Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        	String locationQuery = String.format("SELECT itemID FROM ItemLocation WHERE MBRContains(GeomFromText('Polygon((%f %f, %f %f, %f %f, %f %f, %f %f))'), coord)", 
        		region.getLx(), region.getLy(), region.getLx(), region.getRy(), region.getRx(), region.getRy(), region.getRx(), region.getLy(), region.getLx(), region.getLy()); 
        	ResultSet locationResult = stmt.executeQuery(locationQuery);
        	HashSet <String> locationIDs = new HashSet <String>(); 
        	while(locationResult.next()) {
        		locationIDs.add(locationResult.getString("itemID")); 
        	}
        
			/*for(SearchResult result : basicResult) {
				System.out.println(result.getItemId() + ": " + result.getName());
				result_item = stmt.executeQuery("SELECT ");
			}*/
		}
		catch(IOException | ParseException | SQLException ex) {
			System.err.println(ex); 
		}
		
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

	/*private ScoreDoc[] keywordSearch(String query) {
		try{
			IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index")))); 
			QueryParser parser = new QueryParser("content", new StandardAnalyzer()); 
			Query queryObj = parser.parse(query); 
			TopDocs docs = searcher.search(queryObj, Integer.MAX_VALUE); 
			ScoreDoc[] hits = docs.scoreDocs; 
			return hits; 
		}
		catch(IOException | ParseException ex) {
			System.err.println(ex); 
		}
		return new ScoreDoc[0]; 
	}*/

}

package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    ResultSet result_items; 
    ResultSet result_categories; 
    String dir_LUCENE = "/var/lib/lucene"; 
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    public void rebuildIndexes() {

        Connection conn = null;


        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
        Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        result_items = stmt.executeQuery("SELECT itemID, name, description FROM Item;"); 

        Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index")); 
        IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer()); 
        IndexWriter indexBuilder = new IndexWriter(indexDir, indexConfig); 

        while(result_items.next()) {

            int itemID = result_items.getInt("itemID"); 
            String itemID_string = "" + itemID; 
            String name = result_items.getString("name");
            String description = result_items.getString("description");


            Statement stmt_cat = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            result_categories = stmt_cat.executeQuery("SELECT category FROM Category WHERE itemID = " + itemID);  
            String categories = ""; 
            
            while(result_categories.next()){
                categories += result_categories.getString("category") + " "; 
            }

            String union_terms = name + " " + description + " " + categories; 
            Document index_doc = new Document(); 
            index_doc.add(new StringField("ItemId", itemID_string, Field.Store.YES)); 
            index_doc.add(new StringField("Name", name, Field.Store.YES));
            index_doc.add(new StringField("Description", description, Field.Store.YES));
            index_doc.add(new StringField("Category", categories, Field.Store.YES));
            index_doc.add(new TextField("Full Search", union_terms, Field.Store.NO));

            indexBuilder.addDocument(index_doc); 
            result_categories.close(); 


        }

        result_items.close(); 
        indexBuilder.close(); 

        //Directory directIndex = FSDirectory.open(new File("index-directory")); 




	} catch (SQLException | IOException ex) {
	    System.out.println(ex);
	}


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */


        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}

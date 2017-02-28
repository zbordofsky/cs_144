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

            /* Open connection to database and retrieve id, name, description for each item  */
    	    conn = DbManager.getConnection(true);
            Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            result_items = stmt.executeQuery("SELECT itemID, name, description FROM Item;"); 

            /* Set up index writer, which will be used to create index at specified directory  
               OpenMode = CREATE. Every time we rebuild the index, we overwrite the existing
               index, if it existed  */
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index")); 
            IndexWriterConfig indexConfig = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter indexBuilder = new IndexWriter(indexDir, indexConfig); 

            /* Go through each item and create entries in our index  */
            while (result_items.next()) {

                /* Get individual attributes retrieved for each item  */
                int itemID = result_items.getInt("itemID"); 
                String itemID_string = "" + itemID; 
                String name = result_items.getString("name");
                String description = result_items.getString("description");

                /* Retrieve all categories the current item falls under  */
                Statement stmt_cat = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                result_categories = stmt_cat.executeQuery("SELECT category FROM Category WHERE itemID = " + itemID);  
                String categories = ""; 
                while (result_categories.next()) {
                    categories += result_categories.getString("category") + " "; 
                }

                /* We will perform keyword search over union of item name, description, and categories  */
                String union_terms = name + " " + description + " " + categories;

                /* Create document to store current element. The first four fields are stored as atomic
                   values and may be used when returning info about search results. The final field is
                   tokenized into words for the purpose of indexing.  */
                Document index_doc = new Document(); 
                index_doc.add(new StringField("ItemId", itemID_string, Field.Store.YES)); 
                index_doc.add(new StringField("Name", name, Field.Store.YES));
                index_doc.add(new StringField("Description", description, Field.Store.YES));
                index_doc.add(new StringField("Category", categories, Field.Store.YES));
                index_doc.add(new TextField("content", union_terms, Field.Store.NO));

                indexBuilder.addDocument(index_doc); 
                result_categories.close(); 
            }

            /* After checking all items, close the result set and index writer so that changes are saved  */
            result_items.close(); 
            indexBuilder.close(); 
	    } 
        catch (SQLException | IOException ex) {
    	    System.out.println(ex);
    	}

        // close the database connection
    	try {
    	    conn.close();
    	} 
        catch (SQLException ex) {
    	    System.out.println(ex);
	   }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}

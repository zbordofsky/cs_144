package edu.ucla.cs.cs144;

import java.io.*;   // TODO remove?
import java.util.*; // TODO remove?
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	try {
	        String itemId = request.getParameter("itemId");
	        String itemXML = AuctionSearch.getXMLDataForItemId(itemId);
	        Item parsedXML = new Item(itemXML);
	        request.setAttribute("ItemId", parsedXML.getItemId());
	        request.setAttribute("Name", parsedXML.getName());
	        request.setAttribute("Categories", parsedXML.getCategories());
	        request.setAttribute("CurrentBid", parsedXML.getCurrentBid());
	        request.setAttribute("BuyPrice", parsedXML.getBuyPrice());
	        request.setAttribute("FirstBid", parsedXML.getFirstBid());
	        request.setAttribute("NumBids", parsedXML.getNumBids());
	        request.setAttribute("Bids", parsedXML.getBids());
	        request.setAttribute("Latitude", parsedXML.getLatitude());
	        request.setAttribute("Longitude", parsedXML.getLongitude());
	        request.setAttribute("Location", parsedXML.getLocation());
	        request.setAttribute("Country", parsedXML.getCountry());
	        request.setAttribute("StartDate", parsedXML.getStartDate());
	        request.setAttribute("EndDate", parsedXML.getEndDate());
	        request.setAttribute("SellerID", parsedXML.getSellerID());
	        request.setAttribute("SellerRating", parsedXML.getSellerRating());
	        request.setAttribute("Description", parsedXML.getDescription());
	        request.setAttribute("item", parsedXML);
	        PrintWriter out = response.getWriter();
	        out.println("<html>");
	        out.println("<body><h1>Results</h1>");
	        out.println(parsedXML.getItemId());
	        out.println(parsedXML.getName());
	        out.println(parsedXML.getCategories());
	        out.println(parsedXML.getCurrentBid());
	        out.println(parsedXML.getBuyPrice());
	        out.println(parsedXML.getFirstBid());
	        out.println(parsedXML.getNumBids());

	        List<Bid> Bids = new ArrayList<Bid>();
	        Bids = parsedXML.getBids();
	        for (Bid currBid : Bids) {
	        	out.println(currBid.getBidder());
	        	out.println(currBid.getRating());
	        	out.println(currBid.getLocation());
	        	out.println(currBid.getCountry());
	        	out.println(currBid.getTime());
	        	out.println(currBid.getAmount());
	        }

	        out.println(parsedXML.getLatitude());
	        out.println(parsedXML.getLongitude());
	        out.println(parsedXML.getLocation());
	        out.println(parsedXML.getCountry());
	        out.println(parsedXML.getStartDate());
	        out.println(parsedXML.getEndDate());
	        out.println(parsedXML.getSellerID());
	        out.println(parsedXML.getSellerRating());
	        out.println(parsedXML.getDescription());

	        //request.getRequestDispatcher("/itemInfo.jsp").forward(request, response);
    	}
    	catch (Exception e) {
    		e.printStackTrace(); // TODO: change
    	}
    }
}

package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Obtain user search input. If page has loaded for first time, value will be 'null'
        String query = request.getParameter("query");

        // First time search page has loaded, or user did not specify a query to search by
        if (query == null || query.isEmpty()) {
            response.sendRedirect("/eBay/auctionSearch.html");
            return;
        }

        // If query is not empty, obtain search parameters
        Integer numResultsToSkip = Integer.parseInt(request.getParameter("numToSkip"));
        Integer numResultsToReturn = Integer.parseInt(request.getParameter("numToReturn"));
        SearchResult[] searchResults = AuctionSearch.basicSearch(query, numResultsToSkip, numResultsToReturn);
       
        request.setAttribute("searchResults", searchResults);
        request.setAttribute("query", query);
        request.setAttribute("numToSkip", numResultsToSkip);
        request.setAttribute("numToReturn", numResultsToReturn);
        request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
    }
}

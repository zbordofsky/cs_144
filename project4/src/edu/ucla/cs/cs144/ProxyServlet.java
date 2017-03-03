package edu.ucla.cs.cs144;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Build URL string
        String query = request.getParameter("query");
        String baseURL = "http://google.com/complete/search?output=toolbar&q=";
        String queryURL = URLEncoder.encode(query, "UTF-8");
        String completeURL = baseURL + queryURL;

        // Open connection to google
        URL u = new URL(completeURL);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.connect();

        huc.setRequestMethod("GET");
        huc.setRequestProperty("Content-type", "text/xml");
        response.setContentType("text/xml");

        // read from connection
        InputStreamReader hucInpStream = new InputStreamReader(huc.getInputStream());
        BufferedReader hucInpReader = new BufferedReader(hucInpStream);
        StringBuffer hucResponse = new StringBuffer();
        String line;
        while ((line = hucInpReader.readLine()) != null) {
        	hucResponse.append(line);
        }

        PrintWriter output = response.getWriter();
        output.write(hucResponse.toString());
        hucInpReader.close();
        huc.disconnect();


    }
}

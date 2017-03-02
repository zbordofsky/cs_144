
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<%@ page import="edu.ucla.cs.cs144.Item" %>
<%@ page import="edu.ucla.cs.cs144.Bid" %>

<html>

<head>
    <title>Item Info</title>
</head>

<body>
    <h3>Item Information</h3>
    <form action="/eBay/search" method="GET">
    	<input type="text" name="query" placeholder="What are you looking for?">
    	<input type="hidden" name="numToSkip" value="0">
    	<input type="hidden" name="numToReturn" value="10"> 
    	<input type="submit" value="Search">
    </form>

    Displaying item information...<br>
    <br>
    <br>
    <br>


</body>

</html>
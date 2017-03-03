<%@ page import="edu.ucla.cs.cs144.SearchResult" %>

<html>

<head>
    <title>Search Results</title>
    <script type="text/javascript" src="SuggestionProvider.js"></script>
    <script type="text/javascript" src="SearchSuggest.js"></script>
    <link rel="stylesheet" type="text/css" href="searchSuggest.css" />
    <script type="text/javascript">
        window.onload = function () {
            var oTextbox = new AutoSuggestControl(document.getElementById("query"), new SuggestionProvider());
        }
    </script>
</head>

<body>
    <h3>Search Results</h3>
    <form action="/eBay/search" method="GET">
    	<input type="text" name="query" placeholder="What are you looking for?" id="query">
    	<input type="hidden" name="numToSkip" value="0">
    	<input type="hidden" name="numToReturn" value="10"> 
    	<input type="submit" value="Search">
    </form>
    <% SearchResult[] searchResults = (SearchResult[]) request.getAttribute("searchResults"); 
       Integer numToSkip = Integer.parseInt(request.getParameter("numToSkip"));
       Integer numToReturn = Integer.parseInt(request.getParameter("numToReturn"));
       Integer nextNumToSkip = numToSkip + numToReturn; 
       String query = (String) request.getAttribute("query"); %>

    Displaying results for "<%= query %>"
    <br>
    <br>

	<%   for (SearchResult result : searchResults) { %>
	   		<a href="/eBay/item?itemId=<%= result.getItemId() %>"><%= result.getName() %></a><br>
	   <% } %>
	<br>
	<br>
	<form action="/eBay/search" method="GET">
		<input type="hidden" name="query" value="<%= query %>">
		<input type="hidden" name="numToSkip" value="<%= nextNumToSkip %>">
		<input type="hidden" name="numToReturn" value="<%= numToReturn %>">
		<input type="submit" value="Next">
	</form>


</body>

</html>
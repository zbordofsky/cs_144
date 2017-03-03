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
    <form action="/eBay/search" method="GET">
    	<input type="text" name="query" size="60" placeholder="What are you looking for today?" id="query">
    	<input type="hidden" name="numToSkip" value="0">
    	<input type="hidden" name="numToReturn" value="10"> 
    	<input type="submit" value="Search">
    </form>
    <% SearchResult[] searchResults = (SearchResult[]) request.getAttribute("searchResults"); 
       Integer numToSkip = Integer.parseInt(request.getParameter("numToSkip"));
       Integer numToReturn = Integer.parseInt(request.getParameter("numToReturn"));

       Integer nextNumToSkip;
       if (searchResults.length == 0) {
           nextNumToSkip = numToSkip;
       }
       else {
           nextNumToSkip = numToSkip + numToReturn;
       }

       Integer prevNumToSkip;
       if (numToSkip == 0) {
           prevNumToSkip = numToSkip;      
       }
       else {
           prevNumToSkip = numToSkip - numToReturn;
       }

       String query = (String) request.getAttribute("query"); %>

    <h3>Displaying results for "<%= query %>"</h3>


	<%   for (SearchResult result : searchResults) { %>
	   		<a href="/eBay/item?itemId=<%= result.getItemId() %>"><%= result.getName() %></a><br>
	   <% } %>
	<br>
	<br>
    <form action="/eBay/search" method="GET" style="float:left">
        <input type="hidden" name="query" value="<%= query %>">
        <input type="hidden" name="numToSkip" value="<%= prevNumToSkip %>">
        <input type="hidden" name="numToReturn" value="<%= numToReturn %>">
        <input type="submit" value="Prev 10 Results">
    </form>

	<form action="/eBay/search" method="GET" style="float:left">
		<input type="hidden" name="query" value="<%= query %>">
		<input type="hidden" name="numToSkip" value="<%= nextNumToSkip %>">
		<input type="hidden" name="numToReturn" value="<%= numToReturn %>">
		<input type="submit" value="Next 10 Results">
	</form>


</body>

</html>
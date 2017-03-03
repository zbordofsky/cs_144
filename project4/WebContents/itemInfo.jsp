
<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.ucla.cs.cs144.MyParser" %>
<%@ page import="edu.ucla.cs.cs144.Item" %>
<%@ page import="edu.ucla.cs.cs144.Bid" %>

<!DOCTYPE html> 
<html>

<head>
    <style type="text/css">
        #map_canvas { 
            height: 500px;
            width: 100%;
        }
        div.text-body{
            width:50%;
            position: relative; 
            float: left;
        }
    </style>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>Item Info</title>
    <script type="text/javascript" 
        src="http://maps.google.com/maps/api/js?sensor=false"> 
    </script> 

    <script type="text/javascript" src="SuggestionProvider.js"></script>
    <script type="text/javascript" src="SearchSuggest.js"></script>
    <link rel="stylesheet" type="text/css" href="searchSuggest.css" />

    

    <script type="text/javascript"> 
      function initialize() { 
        var oTextbox = new AutoSuggestControl(document.getElementById("query"), new SuggestionProvider());
        var longitude = '<%=(String)request.getAttribute("Longitude")%>'; 
        var latitude = '<%=(String)request.getAttribute("Latitude")%>'; 
        if(longitude != "n/a" && latitude != "N/A"){
            var latlng = new google.maps.LatLng(latitude, longitude); 
            var myOptions = { 
                zoom: 14, // default is 8  
                center: latlng, 
                mapTypeId: google.maps.MapTypeId.ROADMAP 
            }; 
        }
        else{
            var latlng = new google.maps.LatLng(0,0); 
            var myOptions = { 
                zoom: 1, // default is 8  
                center: latlng, 
                mapTypeId: google.maps.MapTypeId.ROADMAP 
            }; 
        }
        
        
        var map = new google.maps.Map(document.getElementById("map_canvas"), 
            myOptions); 
      } 
    </script> 
</head>
<% Item item = (Item) request.getAttribute("item"); %>
<body onload='initialize()'>
    <h3>Item Information</h3>
    <form action="/eBay/search" method="GET">
    	<input type="text" name="query" placeholder="What are you looking for?" id="query">
    	<input type="hidden" name="numToSkip" value="0">
    	<input type="hidden" name="numToReturn" value="10"> 
    	<input type="submit" value="Search">
    </form>

    <div class="text-body">
        Displaying item information...<br>
        
        <%= item.getItemId() %> 
        <br>
        <%= item.getName() %>
        <br>
        <% for (String category : item.getCategories()) { %>
            <%= category %><br>
        <% } %>
        <%= item.getCurrentBid() %>
        <br>
        <%= item.getBuyPrice() %>
        <br>
         <%= item.getFirstBid() %>
        <br>
        <%= item.getNumBids() %>
        <br>
        <% for (Bid bid : item.getBids()) { %>
          <%= bid.getBidder() %><br>
          <%= bid.getRating() %><br>
          <%= bid.getLocation() %><br>
          <%= bid.getCountry() %><br>
          <%= bid.getTime() %><br>
          <%= bid.getAmount() %><br>
        <% } %>
        <%= item.getLatitude() %>
        <br>
        <%= item.getLongitude() %>
        <br>
        <%= item.getLocation() %>
        <br>
        <%= item.getCountry() %>
        <br>
        <%= item.getStartDate() %>
        <br>
        <%= item.getEndDate() %>
        <br>
        <%= item.getSellerID() %>
        <br>
        <%= item.getSellerRating() %>
        <br>
        <%= item.getDescription() %>      
        <br>
        <br>
        <br>
    </div>
    <div class="text-body">
        <div id="map_canvas"></div>
    </div>

</body>

</html>
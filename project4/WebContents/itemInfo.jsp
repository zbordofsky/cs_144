
<%@ page import="java.util.ArrayList" %>
<%@ page import="edu.ucla.cs.cs144.MyParser" %>
<%@ page import="edu.ucla.cs.cs144.Item" %>
<%@ page import="edu.ucla.cs.cs144.Bid" %>

<!DOCTYPE html> 
<html>

<head>
    <style type="text/css">
        #map_canvas { 
            height:500px;
            width: 100%;
        }
        div.text-body{
            width:50%;
            position: relative; 
            float: left;
        }
        div.upper-window{
            width:100%;
            float: left;
        }
        table, th, td {
            border: 1px solid black;
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
    <form action="/eBay/search" method="GET">
    	<input type="text" name="query" size="60" placeholder="What are you looking for today?" id="query">
    	<input type="hidden" name="numToSkip" value="0">
    	<input type="hidden" name="numToReturn" value="10"> 
    	<input type="submit" value="Search">
    </form>

    <div class="upper-window">
    <div class="text-body">

        <h3>Item Information</h3>
        <table style="width:100%">
            
            <tr>
                <th align="left">Item ID</th>
                <td><%= item.getItemId() %></td>
            </tr>

            <tr>
                <th align="left">Name</th>
                <td><%= item.getName() %></td>
            </tr>

            <tr>
                <th align="left">Categories</th>
                <td>        <% for (String category : item.getCategories()) { %>
            <%= category %><br>
        <% } %></td>
            </tr>

            <tr>
                <th align="left">Highest Bid</th>
                <td><%= item.getCurrentBid() %></td>
            </tr>

            <tr>
                <th align="left">Buy Price</th>
                <td><%= item.getBuyPrice() %></td>
            </tr>

            <tr>
                <th align="left">First Bid</th>
                <td><%= item.getFirstBid() %></td>
            </tr>

            <tr>
                <th align="left"># Bids Made</th>
                <td><%= item.getNumBids() %></td>
            </tr>

            <tr>
                <th align="left">Location</th>
                <td><%= item.getLocation() %></td>
            </tr>

            <tr>
                <th align="left">Latitude</th>
                <td><%= item.getLatitude() %></td>
            </tr>

            <tr>
                <th align="left">Longitude</th>
                <td><%= item.getLongitude() %></td>
            </tr>

            <tr>
                <th align="left">Country</th>
                <td><%= item.getCountry() %></td>
            </tr>

            <tr>
                <th align="left">Bidding Begins</th>
                <td><%= item.getStartDate() %></td>
            </tr>

            <tr>
                <th align="left">Bidding Ends</th>
                <td><%= item.getEndDate() %></td>
            </tr>

            <tr>
                <th align="left">Seller</th>
                <td><%= item.getSellerID() %></td>
            </tr>

            <tr>
                <th align="left">Seller's Rating</th>
                <td><%= item.getSellerRating() %></td>
            </tr>


        </table>
    </div>
    <div class="text-body">
        <div id="map_canvas"></div>
    </div>
    </div>
    <h3>Description</h3>
    <%= item.getDescription() %>
    <br>

    <h3>Bid History</h3>
    <table style="width:50%">
        <tr>
            <th>Time</th>
            <th>Amount</th>
            <th>User ID</th>
            <th>User Rating</th>
            <th>User Location</th>
            <th>User Country</th>
        </tr>
        <% Integer numBids = 0; %>
        <% for (Bid bid : item.getBids()) { %>
        <% numBids++; %>
        <tr>
            <td><%= bid.getTime() %></td>
            <td><%= bid.getAmount() %></td>
            <td><%= bid.getBidder() %></td>
            <td><%= bid.getRating() %></td>
            <td><%= bid.getLocation() %></td>
            <td><%= bid.getCountry() %></td>
        </tr>
        <% } %>
    </table>

</body>

</html>
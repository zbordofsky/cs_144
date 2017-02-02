CREATE TABLE IF NOT EXISTS Category
(	
    itemID		  INTEGER 		 NOT NULL
   ,category      VARCHAR(100)   NOT NULL
   ,PRIMARY KEY(itemID, category)
);

CREATE TABLE IF NOT EXISTS Seller
(
userID		  INTEGER		 NOT NULL
,sellerRating  INTEGER			      NOT NULL
,PRIMARY KEY(userID)
);

CREATE TABLE IF NOT EXISTS Item
(
itemID       INTEGER        NOT NULL
,name         VARCHAR(100)   NOT NULL
,description  VARCHAR(4000)  NOT NULL
,userID       VARCHAR(100)   NOT NULL
,currently		    DECIMAL(8,2)   NOT NULL
,buy_price		      DECIMAL(8,2) DEFAULT NULL
,first_bid	        DECIMAL(8,2)	 NOT NULL
,num_bids     INTEGER        NOT NULL
,location   VARCHAR(100)   NOT NULL
,latitude		      VARCHAR(100)
,longitude	        VARCHAR(100)
,country		  VARCHAR(100)	 NOT NULL
,started	   TIMESTAMP	     NOT NULL
,ends	   		       TIMESTAMP   NOT NULL
,PRIMARY KEY (itemID)
);

CREATE TABLE IF NOT EXISTS Bidder
(	     
userID	   INTEGER        NOT NULL
,bidderRating  INTEGER        NOT NULL
,location     VARCHAR(100)
,country	    VARCHAR(100)
,PRIMARY KEY (userID)
);

CREATE TABLE IF NOT EXISTS Bids
(
itemID    INTEGER   NOT NULL
,userID	      	    INTEGER   NOT NULL
,time 		    	        TIMESTAMP   NOT NULL
,amount				      DECIMAL(8,2)   NOT NULL
,PRIMARY KEY (itemID, userID, time)
);

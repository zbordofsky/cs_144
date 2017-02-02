
-- Create table containing info about items being sold
CREATE TABLE IF NOT EXISTS Item
(
  itemID INTEGER NOT NULL
 ,name VARCHAR(100) NOT NULL
 ,description VARCHAR(4000)  NOT NULL
 ,userID VARCHAR(100) NOT NULL
 ,currently DECIMAL(8,2) NOT NULL
 ,buyPrice DECIMAL(8,2) NULL
 ,firstBid DECIMAL(8,2)	NOT NULL
 ,numBids INTEGER NOT NULL
 ,location VARCHAR(100) NOT NULL
 ,latitude VARCHAR(100) NULL
 ,longitude	VARCHAR(100) NULL
 ,country VARCHAR(100) NOT NULL
 ,started TIMESTAMP	NOT NULL
 ,ends TIMESTAMP NOT NULL
 ,PRIMARY KEY (itemID)
);

-- Create table listing the categories that an item falls under
CREATE TABLE IF NOT EXISTS Category
(	
  itemID INTEGER NOT NULL
 ,category VARCHAR(100) NOT NULL
 ,PRIMARY KEY(itemID, category)
);

-- Create table listing the *seller* rating for a particular user
CREATE TABLE IF NOT EXISTS Seller
(
  userID VARCHAR(100) NOT NULL
 ,sellerRating INTEGER NOT NULL
 ,PRIMARY KEY(userID)
);

-- Create table listing the *bidder* rating for a particular user, plus (optionally) location
CREATE TABLE IF NOT EXISTS Bidder
(	     
  userID VARCHAR(100) NOT NULL
 ,bidderRating INTEGER NOT NULL
 ,location VARCHAR(100) NULL
 ,country VARCHAR(100) NULL
 ,PRIMARY KEY (userID)
);

-- Create table listing info on all bids made on all items
CREATE TABLE IF NOT EXISTS Bid
(
  itemID INTEGER NOT NULL
 ,userID VARCHAR(100) NOT NULL
 ,time TIMESTAMP NOT NULL
 ,amount DECIMAL(8,2) NOT NULL
 ,PRIMARY KEY (itemID, userID, time)
);

Kyle Haacker, 904467146
Zach Bordofsky, 304443257

CS 144 - Project 2
Data Conversion and Loading

For our implementation of project 2, we use the following five relations:

	Item
	(
      itemID      INTEGER       NOT NULL
	 ,name        VARCHAR(100)  NOT NULL
	 ,description VARCHAR(4000) NOT NULL
	 ,userID      VARCHAR(100)  NOT NULL
	 ,currently	  DECIMAL(8,2)  NOT NULL
	 ,buy_price	  DECIMAL(8,2)      NULL
	 ,first_bid	  DECIMAL(8,2)  NOT NULL
	 ,num_bids    INTEGER       NOT NULL
	 ,location    VARCHAR(100)  NOT NULL
	 ,latitude    VARCHAR(100)      NULL
	 ,longitude	  VARCHAR(100)      NULL
	 ,country     VARCHAR(100)  NOT NULL
	 ,started     TIMESTAMP	    NOT NULL
	 ,ends        TIMESTAMP     NOT NULL
	 ,PRIMARY KEY(itemID)
	)

	Category
    (	
      itemID   INTEGER      NOT NULL
     ,category VARCHAR(100) NOT NULL
     ,PRIMARY KEY(itemID, category)
    )

    Seller
    (
      userID       VARCHAR(100) NOT NULL
     ,sellerRating INTEGER      NOT NULL
     ,PRIMARY KEY(userID)
    )

    Bidder
    (	     
      userID       VARCHAR(100) NOT NULL
     ,bidderRating INTEGER      NOT NULL
     ,location     VARCHAR(100) NULL
     ,country      VARCHAR(100) NULL
     ,PRIMARY KEY (userID)
    )

    Bid
    (
      itemID INTEGER NOT NULL
     ,userID VARCHAR(100) NOT NULL
     ,time TIMESTAMP NOT NULL
     ,amount DECIMAL(8,2) NOT NULL
     ,PRIMARY KEY (itemID, userID, time)
    )

PART B Questions

(1) All nontrivial functional dependencies that we have identified effectively specify keys.
    Namely, we have identified the following functional dependencies:

    (1) itemID -> name, description, userID, currently, buy_price, first_bid, num_bids,
                  location, latitude, longitude, country, started, ends

	(2) userID -> sellerRating, bidderRating, location, country

	(3) itemID, userID, time -> amount

(2) All of the relations are in BCNF, because for each of the functional dependencies X -> Y
    that are listed in the answer to (1), the left-hand side (i.e., X) is a superkey for the
    relational schema. For example, for the Bidder relation, we have {userID}+ = {userID,
    bidderRating, location, country, sellerRating} and hence userID is a superkey for the
    Bidder relation. Note that while we could have kept the Seller and Bidder tables together
    as one 'User' table, we found it simpler to split the two tables up for querying purposes.

(3) We believe our relational design to satisfy the fourth normal form (4NF). For the Item,
    Bidder, and Seller relations, the respective id columns of these tables effectively
    specifies the remainder of the data for that row. There is no possibility for redundancy
    in describing an item or user. For the category table, we only list the categories that
    the given item falls under, and again there is no redundancy in list the categories for
    an item across multiple rows of a table. Finally, for the Bid table, a user can only bid
    on a particular item once at a given time. It is not possbile that they bid different
    amounts at the same time or on different items at the same time.

-- Find the number of users in the database
SELECT COUNT(DISTINCT users.userID)
FROM   (SELECT bid.userID
        FROM   Bidder bid
        UNION
        SELECT sell.userID
        FROM   Seller sell) users;

-- Find the number of items in "New York" (exact, case-sensitive)
SELECT COUNT(*)
FROM   Item itm
WHERE  BINARY itm.location = 'New York';

-- Find the number of auctions belonging to exactly four categories
SELECT COUNT(catFour.itemID)
FROM   (SELECT cat.itemID
        FROM   Category cat
        GROUP BY cat.itemID
        HAVING COUNT(cat.itemID) = 4) catFour;

-- Find the ID(s) of current (unsold) auction(s) with the highest bid.
SELECT itm.itemID
FROM   Item itm
      ,Bid  bid
WHERE  itm.itemID = bid.itemID
AND    itm.started < '2001-12-20 00:00:01'
AND    itm.ends > '2001-12-20 00:00:01'
AND    bid.amount >= ALL (SELECT bid2.amount
                          FROM   Bid  bid2
                                ,Item itm2
                          WHERE  bid2.itemID = itm2.itemID
                          AND    itm2.started < '2001-12-20 00:00:01'
                          AND    itm2.ends > '2001-12-20 00:00:01');

-- Find the number of sellers whose rating is higher than 1000
SELECT COUNT(*)
FROM   Seller
WHERE  sellerRating > 1000;

-- Find the number of users who are both sellers and bidders
SELECT COUNT(*)
FROM   Seller sell
WHERE  sell.userID IN (SELECT bid.userID
                       FROM   Bidder bid);

-- Find the number of categories that include at least one item with a bid of more than $100
SELECT COUNT(DISTINCT catBids.category)
FROM   (SELECT cat.category
              ,bid.amount
        FROM   Category cat
              ,Bid      bid
        WHERE  cat.itemID = bid.itemID) catBids
WHERE  catBids.amount > 100;
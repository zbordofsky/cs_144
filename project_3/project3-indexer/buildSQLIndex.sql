-- Create a table containing the itemID and (latitude, longitude) of each item
CREATE TABLE IF NOT EXISTS ItemLocation
(
  itemID INTEGER NOT NULL PRIMARY KEY
 ,latLong POINT NOT NULL
) ENGINE = MyISAM;

-- Populate the table with itemID, latitude, and longitude information
INSERT INTO ItemLocation(itemID, latLong)
SELECT Item.itemID
      ,POINT(Item.latitude+0, Item.longitude+0)
FROM   Item
WHERE  Item.latitude IS NOT NULL 
AND    Item.longitude IS NOT NULL;

-- Create spatial index for region search support
CREATE SPATIAL INDEX LocationIndex ON ItemLocation (latLong);


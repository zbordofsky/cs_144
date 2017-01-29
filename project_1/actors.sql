-- Create table with the specified schema
CREATE TABLE Actors(Name VARCHAR(40), Movie VARCHAR(80), Year INTEGER, Role VARCHAR(40)); 

-- Load data in actors.csv into the Actors table
LOAD DATA LOCAL INFILE '~/data/actors.csv' INTO TABLE Actors FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';

-- Retrieve the names of all the actors in the movie 'Die Another Day'
SELECT Name FROM Actors WHERE Movie = 'Die Another Day';

-- Drop the Actors table when finished
DROP TABLE Actors; 
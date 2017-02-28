Kyle Haacker, 904467146
Zach Bordofsky, 304443257

CS 144 - Project 3
Search and Retrieval
Part A: Keyword Search

For our implementation in part A.2 of this project, we decided to create a single Lucene
index on the `itemID`, `name`, and `description` attributes of the `Item` table, and the `category` attribute of the `Category` table. 

The `itemID` and `name` attributes are to be returned in the search results; hence, we
also specify that these fields are to be stored. The `description` and `category` fields
may be useful to be returned in search results at some point, and thus we have included
(and stored) these fields in the index as well. Finally, we have the union of `name`,
`description`, and `category` in the index which we will actually perform the keyword
search on. Since this union may be particularly large, and we will not need it to be
displayed in search results or later queries, we do not store the union.

Note that the union of `name`, `description`, and `category` is stored as a `TextField`
because it should be treated as an English text which should be tokenized into a set of
words for indexing. We use `StringField` for the other fields because we want them to
be stored, as is, and returned, as is. We do not need these fields to be tokenized as
we are using the union field for indexing.
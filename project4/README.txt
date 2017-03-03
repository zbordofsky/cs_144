Kyle Haacker, 904467146
Zach Bordofsky, 304443257

CS 144 - Project 4
Building a Website

For our implementation of the search interface in part A.3 of this project, we display the
top 10 results for a given page. The user can view the next (or previous) 10 top results
by clicking the buttons on the bottom of the search results page. Each result is linked
to a detailed information page for that item.

We created two Java classes ‘Item’ and ‘Bid’ to simplify our retrieval of item information
in part A.4. We pass that Item object to our item information page, which processes and
displays the information in a human-digestible form. The layout is fairly bare, but implementing the underlying functionality of the website was an emphasis for this project. The layout may be improved upon later.

Our implementation of Google Maps follows closely from the example on the spec.

We add a CSS and 2 JavaScript files to assist with our creation of the dropdown autosuggest feature of our search bar, in a similar manner to what was done on the example given in the spec. We have disabled type ahead for our autosuggestion because we found that, if the user was typing quickly, the type ahead feature would fill the text box with the top suggestion instead of letting the user continue typing what they wanted to search for.

We believe all core functionality described in the spec has been correctly implemented for our website.
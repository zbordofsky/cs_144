Kyle Haacker, 904467146
Zach Bordofsky, 304443257

CS 144 - Project 5
Spark and Map Reduce

We believe we have implemented all functionality for this project. Comparing to the
code requirements described in the spec:

1. The code in “topUsers.scala” does read from the twitter graph “twitter.edges” in the
   current directory - see line 2.

2. The code utilizes ‘map’, ‘flatMap’, ‘reduceByKey’, and ‘filter’ transformations to get
   all Twitter users who have followers, count the number of followers each of these users
   has, and return only those users with more than 1000 followers. See lines 4-13.

3. The results are saved to the “output” subdirectory in line 14 via ‘saveAsTextFile’.

4. Basically, the entire structure of our code is a series of transformations.
   In practice, we could fit all of these transformations into a single line of code, but
   it is a bit more readable to split up the transformations onto separate lines.

We tested our code by opening the “part-00000” file in the “output” subdirectory after
running the Scala script. We first compared our results to the results listed in the spec
for 7 different user ID’s, which were all the same. We then checked some other results
in our “part-00000” file and searched for some of the those user ID’s in the file
“twitter.edges”, confirming that the number of matches in “twitter.edges” was equivalent
to the number listed in “part-00000”.
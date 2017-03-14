// Read twitter graph from file in current directory
val lines = sc.textFile("twitter.edges") 

// For each entry in graph, get the list of users that are being followed
val users = lines.map(line => line.split(": "))
val followed = users.flatMap(line => line(1).split(","))

// For each user that is followed, count how many times they are followed
val followedCount = followed.map(line => (line, 1))
val finalCounts = followedCount.reduceByKey((a,b) => a+b)

// Filter out users with <= 1000 followers, save output, and exit
val filterCounts = finalCounts.filter(a => a._2 > 1000)
filterCounts.saveAsTextFile("output")
System.exit(0)
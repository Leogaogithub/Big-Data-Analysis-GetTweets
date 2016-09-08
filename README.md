# MyGetTweets
This is a simple project to show you how to get amount of tweets about specific topic from twitter and upload the file to hdfs.

## Things about codeg
Getting tweets is implemented in `MyGetTweets.MyGetTweets.App`

Uploading these tweets is implemented in `MyGetTweets.MyGetTweets.Upload`

## Compile (you should use maven to build this project.)
  `mvn package`
  
## Output of compiler 
It will output 2 jar files.
`MyGetTweets-1.0-jar-with-dependencies.jar` is used to get tweets and automatly to upload tweets to hdfs directory.	

## Configure 
  Before run the application, you should modify twitter4j.properties to get  authorization to access twitter 
  according to [configuration](http://twitter4j.org/en/configuration.html)
	
## Run the application 
  Following command is used to get tweets and automatly to upload tweets to hdfs directory.
   
  Usage: `hadoop jar MyGetTweets-1.0-jar-with-dependencies.jar  <tweetsNum> <tweetJsonName> <hdfsDirectory>  <topic>`
   
  `hadoop jar MyGetTweets-1.0-jar-with-dependencies.jar 10000 tweetJson.txt  hdfs://cshadoop1/user/lxg151530/assignment1/partii trump`     

## Output of appliction
`MyGetTweets-1.0-jar-with-dependencies.jar` will put tweets file in local dir named as <tweetJsonName>.

then upload this <tweetJsonName> file to <hdfsDirectory>

## Notice 
Because there are some storeage limitation on our utd account, we could not put many tweets on our utd drive. 
And this may cause my App could not upload tweets to hdfs.  So make sure you get enough storage before my App running.


  


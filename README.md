Technologies:
	YouTube API: query for videos
	Hadoop
	Maven
	Java
	Mockito
	
Computes discriminatory search words for two YouTube video query terms taking into consideration the title and the description of the videos
Use auto_run.sh script to make two queries (after pig and duck) and obtain 5 discriminatory results for each.

Add your own API KEY in the youtube.properties file (see https://console.developers.google.com/start)

This program has two executable jars.

First jar (youtubeRequester.jar) is used for:
•	getting any number of videos using YouTube API for a specified query term (e.g. duck).
•	it saves in a file all titles and descriptions found for all the videos of that query term
•	computes the frequency of each word from the video title and description file using Hadoop MR. (classical WordCount example)
Used main class is: HadoopMain.java
Run the jar in an environment where Hadoop 1 is installed:
hadoop jar youtubeRequester.jar /input /output 4
where:

input = folder in hdfs where the file containing video titles and descriptions is copied
output = folder when hadoop will save its output (i.e. folder containing each word and frequency)
4 = number of pages wanted for returned results. Each page has 25 videos.

The program will prompt you with the message:
Enter query string:

Enter your query term, e.g. duck

*If you want to change the number of videos per page go to YouTubeQueryConstants and change 
the NUMBER_OF_RESULTS_RETURNED_PER_PAGE constant
The output files (word, frequency) will be saved locally by default in hadoopOutput folder.

Second jar (discriminateWords.jar)

•	computes discriminatory words for two query terms (e.g. pig videos and duck videos)
•	saves them in files called queryTermResults.txt

Used main class is: DiscriminatoryWordsMain.java

It takes the 2 output files generated by Hadoop (word, frequency FILES) when running first jar, loads them into a list of tuples(word, frequency), sorts them by frequency and computes first x (where x is a command line argument) words that discriminate the two videos.
A word is considered a discriminative word, if it is among the x most frequent words and:

a) it does not appear in the second video file
b) or if it appears, there is a threshold difference between the number of times the same word appears in both files. 
The threshold is given as a command line argument.

*Some words such as prepositions or conjunctions are considered irrelevant words and not taken into consideration. Add more irrelevant words to IrrelevantWordsEnum.java

Run the jar:

java -jar discriminateWords.jar pig duck 5 30
where:

pig: 1st query term used in running the first jar 
duck: 2nd query term used in running the first jar
5: number of discriminatory words wanted for each query term
30: threshold between the word frequency

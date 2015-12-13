#clean folders and files before starting
hadoop fs -rm -r /input
hadoop fs -rm -r /output
rm -rf hadoopOutput
rm pigResults.txt
rm duckResults.txt

#run the program twice once for each query term
hadoop jar youtubeRequester.jar /input /output 4
#delete the created directories in hadoop
hadoop fs -rm -r /input
hadoop fs -rm -r /output
hadoop jar youtubeRequester.jar /input /output 5
#compute discriminatory words
java -jar discriminateWords.jar pig duck 5 30

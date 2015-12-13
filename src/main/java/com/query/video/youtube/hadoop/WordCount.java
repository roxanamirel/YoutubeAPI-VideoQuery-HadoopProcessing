package com.query.video.youtube.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.query.video.youtube.enums.IrrelevantWordsEnum;

public class WordCount {

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			// remove all no letter characters from the word and set to
			// lowercase
			String cleanWord = value.toString().replaceAll("[^\\p{L} ]", "").toLowerCase();
			StringTokenizer itr = new StringTokenizer(cleanWord);
			while (itr.hasMoreTokens()) {
				String wordString = itr.nextToken();
				// filter words that are irrelevant or words composed of one
				// letter
				
				if (!IrrelevantWordsEnum.contains(wordString) && wordString.length() > 1) {
					word.set(wordString);
					context.write(word, one);
				}
			}
		}
	}

	public static class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
	}
}

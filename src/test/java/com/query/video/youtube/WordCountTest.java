package com.query.video.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.query.video.youtube.hadoop.WordCount.SumReducer;
import com.query.video.youtube.hadoop.WordCount.TokenizerMapper;

public class WordCountTest {

	MapReduceDriver<Object, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;
	MapDriver<Object, Text, Text, IntWritable> mapDriver;
	ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;

	@Before
	public void setUp() {

		TokenizerMapper mapper = new TokenizerMapper();
		SumReducer reducer = new SumReducer();
		mapDriver = new MapDriver<Object, Text, Text, IntWritable>();
		mapDriver.setMapper(mapper);
		reduceDriver = new ReduceDriver<Text, IntWritable, Text, IntWritable>();
		reduceDriver.setReducer(reducer);
		mapReduceDriver = new MapReduceDriver<Object, Text, Text, IntWritable, Text, IntWritable>();
		mapReduceDriver.setMapper(mapper);
		mapReduceDriver.setReducer(reducer);
	}

	@Test
	public void testMapper() throws IOException {
		mapDriver.withInput(new LongWritable(1), new Text("pig pig duck"));
		mapDriver.withOutput(new Text("pig"), new IntWritable(1));
		mapDriver.withOutput(new Text("pig"), new IntWritable(1));
		mapDriver.withOutput(new Text("duck"), new IntWritable(1));
		mapDriver.runTest();
	}

	@Test
	public void testReducer() throws IOException {
		List<IntWritable> values = new ArrayList<IntWritable>();
		values.add(new IntWritable(1));
		values.add(new IntWritable(1));
		reduceDriver.withInput(new Text("pig"), values);
		reduceDriver.withOutput(new Text("pig"), new IntWritable(2));
		reduceDriver.runTest();
	}

	@Test
	public void testMapReduce() throws IOException {
		mapReduceDriver.withInput(new LongWritable(1), new Text("pig pig duck"));
		mapReduceDriver.addOutput(new Text("duck"), new IntWritable(1));
		mapReduceDriver.addOutput(new Text("pig"), new IntWritable(2));
		mapReduceDriver.runTest();
	}

}

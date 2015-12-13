package com.query.video.youtube.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.constants.HadoopConstants;
import com.query.video.youtube.constants.YouTubeQueryConstants;
import com.query.video.youtube.hadoop.WordCount;
import com.query.video.youtube.hadoop.WordCount.TokenizerMapper;
import com.query.video.youtube.hadoop.WordCount.SumReducer;
import com.query.video.youtube.models.SearchParameters;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;
import com.query.video.youtube.utils.Utils;

public class HadoopMain {

	public static void main(String[] args) {
		VideoQueryService service = new VideoQueryServiceImpl();

		if (args.length != 3) {
			System.out.println("USAGE + First Parameter = hadoop input directory (e.g. /input)  \n"
					+ "Second parameter = hadoop output directory \n "
					+ "Third parameter = no. of results pages (!there are 25 results per page)");
			System.exit(-1);
		}
		int numberOfPages = Integer.parseInt(args[2]);
		
		// Prompt the user to enter a query term.
		String queryTerm = Utils.getSearchCriteria();

		SearchParameters searchParams = setSearchParameters(queryTerm);
		YouTube.Search.List search = service.defineVideoSearchRequest(searchParams);
		// get search results
		List<SearchResult> searchResultList = service.getVideoQuerySearchResults(search,
				numberOfPages);

		// write results to File
		File resultFile = Utils.writeVideoInfoToFile(searchResultList, queryTerm);

		try {
			Configuration conf = new Configuration();
			conf.addResource(new Path(HadoopConstants.CORE_SITE));
			conf.addResource(new Path(HadoopConstants.HDFS_SITE));
			FileSystem fs = FileSystem.get(conf);
			Job job = Job.getInstance(conf, "word count");
			job.setJarByClass(WordCount.class);
			job.setMapperClass(TokenizerMapper.class);
			job.setCombinerClass(SumReducer.class);
			job.setReducerClass(SumReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
			fs.copyFromLocalFile(new Path(resultFile.getName()), new Path(args[0] + "/" + resultFile.getName()));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			job.waitForCompletion(true);
			// set the name of the output directory to be the same as the output
			// file name but without .txt
			String outputDir = HadoopConstants.HADOOP_OUTPUT_DIR + "/"
					+ resultFile.getName().substring(0, resultFile.getName().length() - 4);
			fs.copyToLocalFile(new Path(args[1]), new Path(outputDir));

		} catch (IOException | ClassNotFoundException | InterruptedException e) {
			System.err.println(e.getCause() + " : " + e.getMessage());
			e.printStackTrace();
		}

	}

	private static SearchParameters setSearchParameters(String queryTerm) {
		SearchParameters searchOptionalParams = new SearchParameters.Builder(YouTubeQueryConstants.PART)
				// set search resource properties
				.withType(YouTubeQueryConstants.SEARCH_VIDEO_TYPE).withQueryTerm(queryTerm)
				.withQueryFields(YouTubeQueryConstants.SEARCH_QUERY_FIELDS)
				.withNumberOfResults(YouTubeQueryConstants.NUMBER_OF_RESULTS_RETURNED_PER_PAGE).build();
		return searchOptionalParams;
	}
}

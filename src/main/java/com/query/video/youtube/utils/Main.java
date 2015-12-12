package com.query.video.youtube.utils;

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

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.hadoop.WordCount;
import com.query.video.youtube.hadoop.WordCount.TokenizerMapper;
import com.query.video.youtube.hadoop.WordCount.TokenizerReducer;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;

public class Main {
	private static final String PART = "snippet";
	private static final String SEARCH_QUERY_FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/description),nextPageToken";
	private static final String SEARCH_VIDEO_TYPE = "video";
	private static final long NUMBER_OF_RESULTS_RETURNED = 25;
	private static final int NUMBER_OF_PAGES = 3;
	private static final String hadoopOutputDir = "hadoopOutputDir";

	public static void main(String[] args) {

		File resultFile = new File("");
		String queryTerm = "";
		try {
			// Prompt the user to enter a query term.
			queryTerm = Utils.getSearchCriteria();

			SearchParameters searchOptionalParams = setSearchParameters(queryTerm);

			VideoQueryService service = new VideoQueryServiceImpl();
			YouTube.Search.List search = service.defineVideoSearchRequest(searchOptionalParams);

			// get search results
			List<SearchResult> searchResultList = service.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);

			// write results to File
			resultFile = Utils.writeToFile(searchResultList, queryTerm);

		} catch (GoogleJsonResponseException e) {
			System.err.println(e.getDetails().getCode() + " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getCause() + " : " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		try {
			Configuration conf = new Configuration();
			conf.addResource(new Path("/home/hadoop/hadoop-2.7.1/etc/hadoop/core-site.xml"));
			conf.addResource(new Path("/home/hadoop/hadoop-2.7.1/etc/hadoop/hdfs-site.xml"));
			FileSystem fs = FileSystem.get(conf);
			Job job = Job.getInstance(conf, "word count");
			job.setJarByClass(WordCount.class);
			job.setMapperClass(TokenizerMapper.class);
			job.setCombinerClass(TokenizerReducer.class);
			job.setReducerClass(TokenizerReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
			fs.copyFromLocalFile(new Path(resultFile.getName()), new Path(args[0] + "/" + resultFile.getName()));
			FileOutputFormat.setOutputPath(job, new Path(args[1]));
			job.waitForCompletion(true);
			fs.copyToLocalFile(new Path(args[1]), new Path(
					hadoopOutputDir + "/" + resultFile.getName().substring(0, resultFile.getName().length() - 4)));

		} catch (IOException | ClassNotFoundException |

		InterruptedException e)

		{
			System.err.println(e.getCause() + " : " + e.getMessage());
			e.printStackTrace();
		}

	}

	private static SearchParameters setSearchParameters(String queryTerm) {
		SearchParameters searchOptionalParams = new SearchParameters.Builder()
				// set search resource properties
				.withPart(PART).withType(SEARCH_VIDEO_TYPE).withQueryTerm(queryTerm)
				.withQueryFields(SEARCH_QUERY_FIELDS).withNumberOfResults(NUMBER_OF_RESULTS_RETURNED).build();
		return searchOptionalParams;
	}
}

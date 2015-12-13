package com.query.video.youtube.main;

import java.util.List;

import com.query.video.youtube.models.WordFrequencyTuple;
import com.query.video.youtube.utils.Utils;
import com.query.video.youtube.utils.VideoManager;

public class ComputeDiscriminatoryWordsMain {
	private static final String HADOOP_OUTPUT_DIR = "hadoopOutputDir";
	private static final String FILENAME_PART = "Results";

	public static void main(String[] args) {

		if (args.length != 4) {
			System.out.println("USAGE:  First parameter = queryString1 (e.g. pig) \n"
					+ "Second paramenter = queryString2 (e.g. duck) \n"
					+ "Third parameter = no of discriminatory words desired (e.g. 2) \n"
					+ "Fourth parameter = threshold for discrimination (e.g. 30) ");
			System.exit(-1);
		}
		String outputDir1 = HADOOP_OUTPUT_DIR + "/" + args[0] + FILENAME_PART;
		String outputDir2 = HADOOP_OUTPUT_DIR + "/" + args[1] + FILENAME_PART;

		VideoManager videoManager = new VideoManager();

		List<WordFrequencyTuple> list1 = videoManager.createSortedWordFrequencyTuples(outputDir1);
		List<WordFrequencyTuple> list2 = videoManager.createSortedWordFrequencyTuples(outputDir2);

		int noOfDiscriminatoryWords = Integer.parseInt(args[2]);
		int thresholdForDiscrimination = Integer.parseInt(args[3]);

		List<WordFrequencyTuple> discList1 = videoManager.getDiscriminatoryWords(list1, list2, noOfDiscriminatoryWords,
				thresholdForDiscrimination);
		List<WordFrequencyTuple> discList2 = videoManager.getDiscriminatoryWords(list2, list1, noOfDiscriminatoryWords,
				thresholdForDiscrimination);

		Utils.writeDiscriminatoryWordsToFile(discList1, args[0]);
		Utils.writeDiscriminatoryWordsToFile(discList2, args[1]);

	}
}

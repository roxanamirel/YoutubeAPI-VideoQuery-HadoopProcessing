package com.query.video.youtube.main;

import java.util.List;

import com.query.video.youtube.constants.HadoopConstants;
import com.query.video.youtube.models.WordFrequencyTuple;
import com.query.video.youtube.service.impl.VideoManagerImpl;
import com.query.video.youtube.utils.Utils;

public class DiscriminatoryWordsMain {

	private static final String FILENAME_PART = "Results";

	public static void main(String[] args) {

		if (args.length != 4) {
			System.out.println("USAGE:  First parameter = queryString1 (e.g. pig) \n"
					+ "Second paramenter = queryString2 (e.g. duck) \n"
					+ "Third parameter = no of discriminatory words desired (e.g. 2) \n"
					+ "Fourth parameter = threshold for discrimination (e.g. 30) ");
			System.exit(-1);
		}
		String outputDir1 = HadoopConstants.HADOOP_OUTPUT_DIR + "/" + args[0] + FILENAME_PART;
		String outputDir2 = HadoopConstants.HADOOP_OUTPUT_DIR + "/" + args[1] + FILENAME_PART;

		VideoManagerImpl videoManager = new VideoManagerImpl();

		// first sort the words by their frequency
		List<WordFrequencyTuple> list1 = videoManager.createSortedWordFrequencyTuples(outputDir1);
		List<WordFrequencyTuple> list2 = videoManager.createSortedWordFrequencyTuples(outputDir2);

		int noOfDiscriminatoryWords = Integer.parseInt(args[2]);
		int thresholdForDiscrimination = Integer.parseInt(args[3]);

		// get the most frequent words from list1 which are not frequent in
		// list2
		List<WordFrequencyTuple> discList1 = videoManager.getDiscriminatoryWords(list1, list2, noOfDiscriminatoryWords,
				thresholdForDiscrimination);
		// get the most frequent words from list2 which are not frequent in
		// list1
		List<WordFrequencyTuple> discList2 = videoManager.getDiscriminatoryWords(list2, list1, noOfDiscriminatoryWords,
				thresholdForDiscrimination);

		Utils.writeDiscriminatoryWordsToFile(discList1, args[0]);
		Utils.writeDiscriminatoryWordsToFile(discList2, args[1]);

	}
}

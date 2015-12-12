package com.query.video.youtube.main;

import java.util.List;

import com.query.video.youtube.utils.Utils;
import com.query.video.youtube.utils.WordFrequencyTuple;

public class ComputeDiscriminatoryWordsMain {
	private static final String hadoopOutputDir = "hadoopOutputDir";

	public static void main(String[] args) {

		if (args.length != 4) {
			System.out.println("USAGE:  First parameter = queryString1 (e.g. pig) \n"
					+ "Second paramenter = queryString2 (e.g. duck) \n"
					+ "Third parameter = no of discriminatory words desired (e.g. 2) \n"
					+ "Fourth parameter = threshold for discrimination (e.g. 30) ");
			System.exit(-1);
		}
		String outputDir1 = hadoopOutputDir + "/" + args[0] + "Results";
		String outputDir2 = hadoopOutputDir + "/" + args[1] + "Results";

		List<WordFrequencyTuple> list1 = Utils.createWordFrequencyTuples(outputDir1);
		List<WordFrequencyTuple> list2 = Utils.createWordFrequencyTuples(outputDir2);

		int noOfDiscriminatoryWords = Integer.parseInt(args[2]);
		int thresholdForDiscrimination = Integer.parseInt(args[3]);

		List<WordFrequencyTuple> discList1 = Utils.getDiscriminatoryWords(list1, list2, noOfDiscriminatoryWords,
				thresholdForDiscrimination);
		List<WordFrequencyTuple> discList2 = Utils.getDiscriminatoryWords(list2, list1, noOfDiscriminatoryWords,
				thresholdForDiscrimination);

		Utils.writeDiscriminatoryWordsToFile(discList1, args[0]);
		Utils.writeDiscriminatoryWordsToFile(discList2, args[1]);

	}
}

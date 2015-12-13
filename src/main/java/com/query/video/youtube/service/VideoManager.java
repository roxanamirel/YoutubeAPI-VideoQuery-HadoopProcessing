package com.query.video.youtube.service;

import java.util.List;

import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.models.WordFrequencyTuple;

public interface VideoManager {

	/**
	 * Checks that the result is a video
	 * 
	 * @param items
	 *            - video items to be checked
	 * @return the video results
	 */
	List<SearchResult> checkVideoResults(List<SearchResult> items);

	/**
	 * Reads the file output generated by hadoop and creates a sorted list of
	 * {@code WordFrequencyTuple}.
	 * 
	 * @param filePath
	 *            - path towards the file generated by hadoop
	 * @return list of {@code WordFrequencyTuple}.
	 */
	List<WordFrequencyTuple> createSortedWordFrequencyTuples(String filePath);

	/**
	 * @param from
	 *            - list from where discriminatory words are extracted
	 * @param against
	 *            - list against which the words in {@code from} are compared to
	 * @param numberOfDiscriminatoryWords
	 *            - number of discriminatory words to find
	 * @param threshold
	 *            - maximum admitted word frequency difference
	 * @return the list of of {@code WordFrequencyTuple}, i.e., discriminatory
	 *         words and their frequency
	 */
	List<WordFrequencyTuple> getDiscriminatoryWords(List<WordFrequencyTuple> from, List<WordFrequencyTuple> against,
			int numberOfDiscriminatoryWords, int threshold);

}

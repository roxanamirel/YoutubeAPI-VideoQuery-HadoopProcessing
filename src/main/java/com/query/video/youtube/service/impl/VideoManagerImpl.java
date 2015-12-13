package com.query.video.youtube.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.constants.HadoopConstants;
import com.query.video.youtube.models.WordFrequencyTuple;
import com.query.video.youtube.service.VideoManager;
import com.query.video.youtube.utils.WordFrequencyTupleComparator;

public class VideoManagerImpl implements VideoManager {

	private static final String KIND = "youtube#video";

	public List<SearchResult> checkVideoResults(List<SearchResult> items) {
		List<SearchResult> videoResults = new ArrayList<SearchResult>();
		if (items == null) {
			return videoResults;
		}

		for (SearchResult singleVideo : items) {
			if (singleVideo.getId().getKind().equals(KIND)) {
				videoResults.add(singleVideo);
			}
		}
		return videoResults;
	}

	public List<WordFrequencyTuple> createSortedWordFrequencyTuples(String filePath) {
		List<WordFrequencyTuple> wordFreqTuples = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath + HadoopConstants.OUTPUT_FILE_NAME));
			String line = br.readLine();

			while (line != null) {
				String[] splits = line.split("\t");
				int freq = Integer.parseInt(splits[1]);
				wordFreqTuples.add(new WordFrequencyTuple(splits[0], freq));
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(wordFreqTuples, new WordFrequencyTupleComparator());
		return wordFreqTuples;
	}

	public List<WordFrequencyTuple> getDiscriminatoryWords(List<WordFrequencyTuple> from,
			List<WordFrequencyTuple> against, int numberOfDiscriminatoryWords, int threshold) {

		List<WordFrequencyTuple> result = new ArrayList<>();
		int index = 0;

		while (result.size() < numberOfDiscriminatoryWords && index < from.size()) {

			// if there are more elements in from than against, those elements
			// are discriminatory
			if (index < from.size() && index > against.size()) {
				for (int i = index; i < from.size(); i++) {
					WordFrequencyTuple tupleList1 = from.get(index);
					WordFrequencyTuple tupleList2 = getTupleWithSameWord(tupleList1, against);
					if (result.size() < numberOfDiscriminatoryWords) {
						addDiscriminatoryWord(threshold, result, tupleList1, tupleList2);
					} else {
						break;
					}
				}
				break;

			} else {
				WordFrequencyTuple tupleList1 = from.get(index);
				WordFrequencyTuple tupleList2 = getTupleWithSameWord(tupleList1, against);
				addDiscriminatoryWord(threshold, result, tupleList1, tupleList2);
				index++;
			}
		}
		return result;
	}

	private void addDiscriminatoryWord(int threshold, List<WordFrequencyTuple> result, WordFrequencyTuple tupleList1,
			WordFrequencyTuple tupleList2) {
		if (null != tupleList2) {
			if ((tupleList1.getFrequency() - tupleList2.getFrequency()) >= threshold) {
				result.add(tupleList1);
			}
		} else {
			result.add(tupleList1);
		}
	}

	private WordFrequencyTuple getTupleWithSameWord(WordFrequencyTuple tuple, List<WordFrequencyTuple> list) {
		for (WordFrequencyTuple wordFrequencyTuple : list) {
			if (tuple.getWord().equals(wordFrequencyTuple.getWord())) {
				return wordFrequencyTuple;
			}
		}
		return null;
	}
}

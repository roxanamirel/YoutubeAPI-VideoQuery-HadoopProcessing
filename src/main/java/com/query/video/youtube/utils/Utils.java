package com.query.video.youtube.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;

public class Utils {
	private static final String CSN = "UTF-8";
	private static final String FILE_NAME_PART = "Results.txt";
	private static final String OUTPUT_FILE_NAME = "/part-r-00000";

	/*
	 * Prompts the user with option to enter search criteria and returns the
	 * search criteria typed by the user
	 * 
	 */
	public static String getSearchCriteria() throws IOException {

		String searchString = "";

		System.out.print("Enter search string: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		searchString = bReader.readLine();

		if (searchString.length() < 1) {
			System.err.println("You did not enter a search criteria!");
			System.exit(-1);
		}
		return searchString;
	}

	public static List<SearchResult> checkVideoResults(List<SearchResult> items) {
		List<SearchResult> videoResults = new ArrayList<SearchResult>();
		if (items == null) {
			return videoResults;
		}
		Iterator<SearchResult> iteratorSearchResults = items.iterator();

		while (iteratorSearchResults.hasNext()) {
			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Check that the result is a video
			if (rId.getKind().equals("youtube#video")) {
				videoResults.add(singleVideo);
			}
		}
		return videoResults;
	}

	public static File writeToFile(List<SearchResult> searchResults, String query) {
		if (searchResults.isEmpty()) {
			System.out.println(" No video info to write to file for query:" + query);
			return new File("");
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter(getFileName(query), CSN);
			for (SearchResult videoResult : searchResults) {
				writer.println(videoResult.getSnippet().getTitle());
				writer.println(videoResult.getSnippet().getDescription());
			}

			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new File(getFileName(query));

	}

	public static List<WordFrequencyTuple> createWordFrequencyTuples(String filePath) {
		List<WordFrequencyTuple> wordFreqTuples = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath + OUTPUT_FILE_NAME));
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

	public static List<WordFrequencyTuple> getDiscriminatoryWords(List<WordFrequencyTuple> list1,
			List<WordFrequencyTuple> list2, int numberOfDiscriminatoryWords, int threshold) {
		List<WordFrequencyTuple> result = new ArrayList<>();
		int index = 0;

		while (result.size() < numberOfDiscriminatoryWords) {
			WordFrequencyTuple tupleList1 = list1.get(index);
			WordFrequencyTuple tupleList2 = getTupleWithSameWord(tupleList1, list2);
			if (null != tupleList2) {
				if (tupleList1.getFrequency() - tupleList2.getFrequency() >= threshold) {
					result.add(tupleList1);
				}
			} else {
				result.add(tupleList1);
			}
			index++;
		}
		return result;
	}

	private static WordFrequencyTuple getTupleWithSameWord(WordFrequencyTuple tuple, List<WordFrequencyTuple> list) {
		for (WordFrequencyTuple wordFrequencyTuple : list) {
			if (tuple.getWord().equals(wordFrequencyTuple.getWord())) {
				return wordFrequencyTuple;
			}
		}
		return null;
	}

	public static File writeDiscriminatoryWordsToFile(List<WordFrequencyTuple> list, String query) {
		if (list.isEmpty()) {
			System.out.println(" No discriminatory words for:" + query);
			return new File("");
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter(getFileName(query), CSN);
			for (WordFrequencyTuple wordFrequencyTuple : list) {
				writer.println(wordFrequencyTuple.getWord() + " " + wordFrequencyTuple.getFrequency());
			}

			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new File(getFileName(query));
	}

	public static String getFileName(String query) {
		return query + FILE_NAME_PART;

	}
}

package com.query.video.youtube.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.models.WordFrequencyTuple;

public class Utils {

	private static final String CSN = "UTF-8";
	private static final String FILE_NAME_PART = "Results.txt";

	/**
	 * Prompts the user with option to enter search criteria and returns the
	 * search criteria typed by the user.
	 * 
	 * @return input typed by user
	 */
	public static String getSearchCriteria() {

		String searchString = "";
		System.out.print("Enter search string: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));

		try {
			searchString = bReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (searchString.length() < 1) {
			System.err.println("You did not enter a search criteria!");
			System.exit(-1);
		}
		return searchString;
	}

	/**
	 * @param searchResults: list of search results to be written into file
	 * @param query: helps at creating the file name
	 * @return file
	 */
	public static File writeVideoInfoToFile(List<SearchResult> searchResults, String query) {
		if (searchResults.isEmpty()) {
			System.out.println(" No video info to write to file for query:" + query);
			return new File("");
		}
		try {
			PrintWriter writer = new PrintWriter(getFileName(query), CSN);
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

	/**
	 * @param list: list of tuples to be written into file
	 * @param query: helps at creating the file name
	 * @return file
	 */
	public static File writeDiscriminatoryWordsToFile(List<WordFrequencyTuple> list, String query) {
		if (list.isEmpty()) {
			System.out.println(" No discriminatory words for:" + query);
			return new File("");
		}		
		try {
			PrintWriter writer = new PrintWriter(getFileName(query), CSN);
			for (WordFrequencyTuple wordFrequencyTuple : list) {
				writer.println(wordFrequencyTuple.getWord() + " " + wordFrequencyTuple.getFrequency());
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new File(getFileName(query));
	}

	private static String getFileName(String query) {
		return query + FILE_NAME_PART;
	}
}

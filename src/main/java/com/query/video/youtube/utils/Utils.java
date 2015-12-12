package com.query.video.youtube.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;

public class Utils {
	private static final String CSN = "UTF-8";
	private static final String FILE_NAME_PART = "Results.txt";

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

	public static String getFileName(String query) {
		return query + FILE_NAME_PART;

	}
}

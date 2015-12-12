package com.query.video.youtube.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;

public class Utils {

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

	/*
	 * Prints out all results in the Iterator. For each result, print the
	 * title,description and video ID
	 *
	 * @param iteratorSearchResults Iterator of SearchResults to print
	 *
	 * @param query Search query (String)
	 */
	public static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

		System.out.println("\n=============================================================");
		System.out.println("  New videos for search on \"" + query + "\".");
		System.out.println("=============================================================\n");

		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" No videos found for:" + query);
		}

		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Check that the result is a video
			if (rId.getKind().equals("youtube#video")) {

				System.out.println(" Video Id" + rId.getVideoId());
				System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
				System.out.println(" Description: " + singleVideo.getSnippet().getDescription());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}

	public static void writeToFile(Iterator<SearchResult> iteratorSearchResults, String query) {
		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" No video info to write to file for query:" + query);
		}
		PrintWriter writer;
		try {
			writer = new PrintWriter(query + "Results.txt", "UTF-8");
			while (iteratorSearchResults.hasNext()) {
				SearchResult singleVideo = iteratorSearchResults.next();
				ResourceId rId = singleVideo.getId();

				// Check that the result is a video
				if (rId.getKind().equals("youtube#video")) {
					writer.println(singleVideo.getSnippet().getTitle());
					writer.println(singleVideo.getSnippet().getDescription());
				}
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

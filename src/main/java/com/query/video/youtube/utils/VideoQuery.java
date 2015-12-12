package com.query.video.youtube.utils;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.List;

public class VideoQuery {

	private static final String PART = "snippet";
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
	private static int NUMBER_OF_PAGES = 3;

	/**
	 * Global instance of a Youtube object used to make YouTube Data API
	 * requests
	 */
	private static YouTube youtube;

	public static void main(String[] args) {
		try {

			youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
				@Override
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName(PropertiesManagement.getPropertyFromProperties("app.name")).build();

			// Prompt the user to enter a query term.
			String queryTerm = Utils.getSearchCriteria();

			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list(PART);

			// Set the developer key
			search.setKey(PropertiesManagement.getPropertyFromProperties("youtube.apikey"));
			search.setQ(queryTerm);

			// Restrict the search results to only include videos
			search.setType("video");

			// Only retrieve the fields that the application uses.
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/description),nextPageToken");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

			// use pagination to get results from all NUMBER_OF_PAGES pages
			String nextToken = "";
			do {
				NUMBER_OF_PAGES--;
				System.out.println("-----------NEXT TOKEN" + nextToken + "----------------");
				search.setPageToken(nextToken);
				// Call the API and print results.
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();
				if (searchResultList != null) {
					//Utils.prettyPrint(searchResultList.iterator(), queryTerm);
					Utils.writeToFile(searchResultList.iterator(), queryTerm);
				}
				nextToken = searchResponse.getNextPageToken();
			} while (nextToken != null && NUMBER_OF_PAGES > 0);

		} catch (GoogleJsonResponseException e) {
			System.err.println(
					"There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

}

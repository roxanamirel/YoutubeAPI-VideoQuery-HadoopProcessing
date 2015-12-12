package com.query.video.youtube.utils;

import java.io.IOException;
import java.util.List;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;

public class Main {
	private static final String PART = "snippet";
	private static final String SEARCH_QUERY_FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/description),nextPageToken";
	private static final String SEARCH_VIDEO_TYPE = "video";
	private static final long NUMBER_OF_RESULTS_RETURNED = 25;
	private static final int NUMBER_OF_PAGES = 3;

	public static void main(String[] args) {
		try {
			// Prompt the user to enter a query term.
			String queryTerm = Utils.getSearchCriteria();

			SearchParameters searchOptionalParams = setSearchParameters(queryTerm);

			VideoQueryService service = new VideoQueryServiceImpl();
			YouTube.Search.List search = service.defineVideoSearchRequest(searchOptionalParams);

			// get search results
			List<SearchResult> searchResultList = service.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);

			// write results to File
			Utils.writeToFile(searchResultList, queryTerm);

		} catch (GoogleJsonResponseException e) {
			System.err.println(e.getDetails().getCode() + " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getCause() + " : " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}

	}

	private static SearchParameters setSearchParameters(String queryTerm) {
		SearchParameters searchOptionalParams = new SearchParameters.Builder()
				// set search resource properties
				.withPart(PART).withType(SEARCH_VIDEO_TYPE).withQueryTerm(queryTerm)
				.withQueryFields(SEARCH_QUERY_FIELDS).withNumberOfResults(NUMBER_OF_RESULTS_RETURNED).build();
		return searchOptionalParams;
	}
}

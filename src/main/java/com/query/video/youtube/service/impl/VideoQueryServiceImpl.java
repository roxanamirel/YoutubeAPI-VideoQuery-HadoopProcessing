package com.query.video.youtube.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.models.SearchParameters;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.utils.PropertiesFileLoader;

public class VideoQueryServiceImpl implements VideoQueryService {

	/**
	 * Instance of a YouTube object used to make YouTube Data API requests
	 */
	private YouTube youtube;

	public VideoQueryServiceImpl() {

		youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) throws IOException {
			}
		}).setApplicationName(PropertiesFileLoader.getPropertyFromProperties(PropertiesFileLoader.PROP_APP_NAME))
				.build();
	}

	@Override
	public List<SearchResult> getVideoQuerySearchResults(YouTube.Search.List search, int numberOfPages) {
		// use pagination to get results from all numberOfPages pages
		String nextToken = "";
		List<SearchResult> searchResultList = new ArrayList<SearchResult>();
		do {
			numberOfPages--;
			search.setPageToken(nextToken);
			SearchListResponse searchResponse = new SearchListResponse();
			try {
				searchResponse = search.execute();
			} catch (IOException e) {
				System.err.println(e.getCause() + " : " + e.getMessage());
				e.printStackTrace();
			}
			List<SearchResult> videoResults = new VideoManagerImpl().checkVideoResults(searchResponse.getItems());
			searchResultList.addAll(videoResults);
			nextToken = searchResponse.getNextPageToken();
		} while (nextToken != null && numberOfPages > 0);
		return searchResultList;
	}

	@Override
	public YouTube.Search.List defineVideoSearchRequest(SearchParameters searchParams) {
		YouTube.Search.List search = null;
		try {
			search = youtube.search().list(searchParams.getPart());
		} catch (IOException e) {
			System.err.println(e.getCause() + " : " + e.getMessage());
			e.printStackTrace();
		}
		if (search != null) {
			// Set the developer key
			search.setKey(PropertiesFileLoader.getPropertyFromProperties(PropertiesFileLoader.PROP_YOUTUBE_APIKEY))
					.setQ(searchParams.getQueryTerm())
					// Restrict the search results to only include videos
					.setType(searchParams.getType())
					// Only retrieve the fields that the application uses
					.setFields(searchParams.getQueryFields()).setMaxResults(searchParams.getNumberOfVideos());
		}
		return search;
	}
}

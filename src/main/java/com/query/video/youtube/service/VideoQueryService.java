package com.query.video.youtube.service;

import java.util.List;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.models.SearchParameters;

public interface VideoQueryService {

	/**
	 * @param search  - API request for retrieving search results
	 * @param numberOfPages - the number of Youtube page results
	 * @return list of search results
	 */
	List<SearchResult> getVideoQuerySearchResults(YouTube.Search.List search, int numberOfPages);

	/**
	 * @param searchParams - parameters for the request
	 * @return API request for retrieving search results
	 */
	YouTube.Search.List defineVideoSearchRequest(SearchParameters searchParams);

}

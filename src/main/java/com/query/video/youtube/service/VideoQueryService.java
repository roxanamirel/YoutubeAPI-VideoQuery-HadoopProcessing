package com.query.video.youtube.service;

import java.util.List;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.utils.SearchParameters;

public interface VideoQueryService {
	
	List<SearchResult> getVideoQuerySearchResults(YouTube.Search.List search, int numberOfPages);

	YouTube.Search.List defineVideoSearchRequest(SearchParameters searchParams);

}

package com.query.video.youtube;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;
import com.query.video.youtube.utils.SearchParameters;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class VideoQueryTest extends TestCase {
	private VideoQueryService videoQuery;
	private static final String SEARCH_QUERY_FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/description),nextPageToken";
	private static final String PART = "snippet";
	private static final String SEARCH_VIDEO_TYPE = "video";
	private static final long NUMBER_OF_RESULTS_RETURNED = 25;
	private static final int NUMBER_OF_PAGES = 3;

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public VideoQueryTest(String testName) {
		super(testName);
		videoQuery = new VideoQueryServiceImpl();
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(VideoQueryTest.class);
	}

	/**
	 * @throws IOException
	 * tests the number of videos returned
	 */
	public void testSearchResultsSize() throws IOException {
		String queryTerm = "test";
		SearchParameters searchParams = setSearchParameters(queryTerm);
		YouTube.Search.List search = videoQuery.defineVideoSearchRequest(searchParams);
		// get search results
		List<SearchResult> searchResultList = videoQuery.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);
		assertEquals(75, searchResultList.size());
	}

	/**
	 * tests the returned fields
	 */
	public void testSearchResultsFields() {
		String queryTerm = "test";
		SearchParameters searchParams = setSearchParameters(queryTerm);

		YouTube.Search.List search = videoQuery.defineVideoSearchRequest(searchParams);
		List<SearchResult> searchResultList = videoQuery.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);
		Iterator<SearchResult> iterator = searchResultList.iterator();
		while (iterator.hasNext()) {
			SearchResult singleVideo = iterator.next();
			ResourceId rId = singleVideo.getId();
			if (rId.getKind().equals("youtube#video")) {
				assertNotNull(singleVideo.getSnippet().getTitle(), true);
				assertNotNull(singleVideo.getSnippet().getDescription(), true);
			}
		}
	}

	private static SearchParameters setSearchParameters(String queryTerm) {
		SearchParameters searchOptionalParams = new SearchParameters.Builder()
				.withPart(PART).withType(SEARCH_VIDEO_TYPE).withQueryTerm(queryTerm)
				.withQueryFields(SEARCH_QUERY_FIELDS).withNumberOfResults(NUMBER_OF_RESULTS_RETURNED).build();
		return searchOptionalParams;
	}
}

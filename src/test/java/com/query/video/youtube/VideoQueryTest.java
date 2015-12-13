package com.query.video.youtube;

import java.io.IOException;
import java.util.List;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.constants.YouTubeQueryConstants;
import com.query.video.youtube.models.SearchParameters;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for VideoQueryService
 */
public class VideoQueryTest extends TestCase {

	private static final int NUMBER_OF_PAGES = 3;

	private VideoQueryService videoQuery;

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
	 *             tests the number of videos returned
	 */
	public void testSearchResultsSize() throws IOException {
		String queryTerm = "test";
		SearchParameters searchParams = UtilsTest.setSearchParameters(queryTerm,
				YouTubeQueryConstants.SEARCH_VIDEO_TYPE);
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
		SearchParameters searchParams = UtilsTest.setSearchParameters(queryTerm,
				YouTubeQueryConstants.SEARCH_VIDEO_TYPE);

		YouTube.Search.List search = videoQuery.defineVideoSearchRequest(searchParams);
		List<SearchResult> searchResultList = videoQuery.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);
		for (SearchResult singleVideo : searchResultList) {
			assertNotNull(singleVideo.getSnippet().getTitle(), true);
			assertNotNull(singleVideo.getSnippet().getDescription(), true);
		}
	}

}

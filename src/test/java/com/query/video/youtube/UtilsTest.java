package com.query.video.youtube;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.constants.YouTubeQueryConstants;
import com.query.video.youtube.models.SearchParameters;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;
import com.query.video.youtube.utils.Utils;

/**
 * Unit test for Utils
 */
public class UtilsTest {

	private static final int NUMBER_OF_PAGES = 3;

	private static VideoQueryService videoQuery;
	private static File file;

	@BeforeClass
	public static void initializeResources() {
		videoQuery = new VideoQueryServiceImpl();
	}

	@Test
	public void testWriteToFile() {
		SearchParameters searchParams = setSearchParameters("test", YouTubeQueryConstants.SEARCH_VIDEO_TYPE);
		YouTube.Search.List search = videoQuery.defineVideoSearchRequest(searchParams);
		List<SearchResult> videosResultList = videoQuery.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);
		Utils.writeVideoInfoToFile(videosResultList, "test");
		file = new File("testResults.txt");
		assertTrue(file.exists());
		assertTrue(file.length() > 0);

	}

	@AfterClass
	public static void cleanResources() {
		file.delete();
	}

	public static SearchParameters setSearchParameters(String queryTerm, String type) {
		SearchParameters searchOptionalParams = new SearchParameters.Builder(YouTubeQueryConstants.PART).withType(type)
				.withQueryTerm(queryTerm).withQueryFields(YouTubeQueryConstants.SEARCH_QUERY_FIELDS)
				.withNumberOfResults(YouTubeQueryConstants.NUMBER_OF_RESULTS_RETURNED_PER_PAGE).build();
		return searchOptionalParams;
	}

}

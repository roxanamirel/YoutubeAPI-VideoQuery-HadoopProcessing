package com.query.video.youtube;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;
import com.query.video.youtube.utils.SearchParameters;
import com.query.video.youtube.utils.Utils;


/**
 * Unit test for Utils
 */
public class UtilsTest {
	private static VideoQueryService videoQuery;
	private static  File file;
	
	private static final String SEARCH_QUERY_FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/description),nextPageToken";
	private static final String PART = "snippet";
	private static final String SEARCH_CHANNEL_TYPE = "channel";
	private static final String SEARCH_VIDEO_TYPE = "video";
	private static final long NUMBER_OF_RESULTS_RETURNED = 25;
	private static final int NUMBER_OF_PAGES = 3;


	@BeforeClass
	public static void initializeResources(){
		videoQuery = new VideoQueryServiceImpl();	
	}
	

	/**
	 * tests the type of videos returned
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCheckVideoResults() {
		SearchParameters searchParams = setSearchParameters("test", SEARCH_CHANNEL_TYPE);
		YouTube.Search.List search = videoQuery.defineVideoSearchRequest(searchParams);
		List<SearchResult> channelsResultList = videoQuery.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);
		assertEquals(0, channelsResultList.size());

	}
	@Test
	public void testWriteToFile(){
		SearchParameters searchParams = setSearchParameters("test", SEARCH_VIDEO_TYPE);
		YouTube.Search.List search = videoQuery.defineVideoSearchRequest(searchParams);
		List<SearchResult> videosResultList = videoQuery.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);
		Utils.writeToFile(videosResultList, "test");
		file = new File ("testResults.txt");
		assertTrue(file.exists());
		assertTrue(file.length()>0);
		
	}
	
	@AfterClass
	public static void cleanResources(){
		file.delete();
	}
	
	private SearchParameters setSearchParameters(String queryTerm, String type) {
		SearchParameters searchOptionalParams = new SearchParameters.Builder().withPart(PART)
				.withType(type).withQueryTerm(queryTerm).withQueryFields(SEARCH_QUERY_FIELDS)
				.withNumberOfResults(NUMBER_OF_RESULTS_RETURNED).build();
		return searchOptionalParams;
	}

}

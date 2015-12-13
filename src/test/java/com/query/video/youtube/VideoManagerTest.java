package com.query.video.youtube;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.query.video.youtube.models.SearchParameters;
import com.query.video.youtube.models.WordFrequencyTuple;
import com.query.video.youtube.service.VideoManager;
import com.query.video.youtube.service.VideoQueryService;
import com.query.video.youtube.service.impl.VideoManagerImpl;
import com.query.video.youtube.service.impl.VideoQueryServiceImpl;
import com.query.video.youtube.utils.WordFrequencyTupleComparator;

public class VideoManagerTest {

	private static final int NUMBER_OF_PAGES = 3;
	private static final String SEARCH_CHANNEL_TYPE = "channel";

	private static VideoQueryService videoQuery;
	private static VideoManager videoManager;

	private List<WordFrequencyTuple> from;
	private List<WordFrequencyTuple> against;

	@BeforeClass
	public static void initializeResources() {
		videoQuery = new VideoQueryServiceImpl();
		videoManager = new VideoManagerImpl();

	}

	/**
	 * tests the type of videos returned
	 *
	 */
	@Test
	public void testCheckVideoResults() {
		String queryTerm = "test";
		SearchParameters searchParams = UtilsTest.setSearchParameters(queryTerm, SEARCH_CHANNEL_TYPE);
		YouTube.Search.List search = videoQuery.defineVideoSearchRequest(searchParams);
		List<SearchResult> channelsResultList = videoQuery.getVideoQuerySearchResults(search, NUMBER_OF_PAGES);
		assertEquals(0, channelsResultList.size());

	}

	@Test
	public void testGetDiscriminatoryWordsWithLowThreshold() {
		mockLists(175, 185);
		sortLists();

		List<WordFrequencyTuple> discList1 = videoManager.getDiscriminatoryWords(from, against, 3, 20);
		assertEquals(discList1.get(0).getWord(), "pig");
		assertEquals(discList1.get(1).getWord(), "peppa");
		assertEquals(discList1.get(2).getWord(), "pigus");

		List<WordFrequencyTuple> discList2 = videoManager.getDiscriminatoryWords(against, from, 3, 20);
		assertEquals(discList2.get(0).getWord(), "duck");
		assertEquals(discList2.get(1).getWord(), "donald");
		assertEquals(discList2.get(2).getWord(), "duckie");

	}

	@Test
	public void testGetDiscriminatoryWordsWithHighThreshold() {
		mockLists(125, 185);
		sortLists();

		List<WordFrequencyTuple> discList1 = videoManager.getDiscriminatoryWords(from, against, 3, 20);
		assertEquals(discList1.get(0).getWord(), "pig");
		assertEquals(discList1.get(1).getWord(), "peppa");
		assertEquals(discList1.get(2).getWord(), "pigus");

		List<WordFrequencyTuple> discList2 = videoManager.getDiscriminatoryWords(against, from, 3, 20);
		assertEquals(discList2.get(0).getWord(), "duck");
		assertEquals(discList2.get(1).getWord(), "animal");
		assertEquals(discList2.get(2).getWord(), "donald");

	}

	@Test
	public void testGetDiscriminatoryWordsWithHighNumberOfWordsTest() {
		mockLists(125, 185);
		sortLists();

		List<WordFrequencyTuple> discList1 = videoManager.getDiscriminatoryWords(from, against, 20, 20);
		assertEquals(discList1.get(0).getWord(), "pig");
		assertEquals(discList1.get(1).getWord(), "peppa");
		assertEquals(discList1.get(2).getWord(), "pigus");

	}

	@Test
	public void testGetDiscriminatoryWordsWithMoreElementsInFromThanInAgainst() {
		mockLists(125, 185);
		sortLists();

		List<WordFrequencyTuple> discList1 = videoManager.getDiscriminatoryWords(from, against, 6, 20);
		assertEquals(discList1.get(0).getWord(), "pig");
		assertEquals(discList1.get(1).getWord(), "peppa");
		assertEquals(discList1.get(2).getWord(), "pigus");
		assertEquals(discList1.get(3).getWord(), "series");
		assertEquals(discList1.get(4).getWord(), "momopig");

	}

	private void sortLists() {
		Collections.sort(from, new WordFrequencyTupleComparator());
		Collections.sort(against, new WordFrequencyTupleComparator());
	}

	private void mockLists(int freqForAnimalPig, int freqForAnimalDuck) {
		from = new ArrayList<>();
		against = new ArrayList<>();
		// for from list
		WordFrequencyTuple p1 = mock(WordFrequencyTuple.class);
		when(p1.getWord()).thenReturn("pig");
		when(p1.getFrequency()).thenReturn(180);
		from.add(p1);

		WordFrequencyTuple p2 = mock(WordFrequencyTuple.class);
		when(p2.getWord()).thenReturn("peppa");
		when(p2.getFrequency()).thenReturn(158);
		from.add(p2);

		WordFrequencyTuple p3 = mock(WordFrequencyTuple.class);
		when(p3.getWord()).thenReturn("pigus");
		when(p3.getFrequency()).thenReturn(100);
		from.add(p3);

		WordFrequencyTuple p4 = mock(WordFrequencyTuple.class);
		when(p4.getWord()).thenReturn("animal");
		when(p4.getFrequency()).thenReturn(freqForAnimalPig);
		from.add(p4);

		WordFrequencyTuple p5 = mock(WordFrequencyTuple.class);
		when(p5.getWord()).thenReturn("momopig");
		when(p5.getFrequency()).thenReturn(50);
		from.add(p5);

		WordFrequencyTuple p6 = mock(WordFrequencyTuple.class);
		when(p6.getWord()).thenReturn("series");
		when(p6.getFrequency()).thenReturn(80);
		from.add(p6);

		// for against list
		WordFrequencyTuple d1 = mock(WordFrequencyTuple.class);
		when(d1.getWord()).thenReturn("duck");
		when(d1.getFrequency()).thenReturn(190);
		against.add(d1);

		WordFrequencyTuple d2 = mock(WordFrequencyTuple.class);
		when(d2.getWord()).thenReturn("donald");
		when(d2.getFrequency()).thenReturn(158);
		against.add(d2);

		WordFrequencyTuple d3 = mock(WordFrequencyTuple.class);
		when(d3.getWord()).thenReturn("duckie");
		when(d3.getFrequency()).thenReturn(100);
		against.add(d3);

		WordFrequencyTuple d4 = mock(WordFrequencyTuple.class);
		when(d4.getWord()).thenReturn("animal");
		when(d4.getFrequency()).thenReturn(freqForAnimalDuck);
		against.add(d4);

	}

}

package com.query.video.youtube.constants;

public class YouTubeQueryConstants {
	public static final String PART = "snippet";
	public static final String SEARCH_QUERY_FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/description),nextPageToken";
	public static final String SEARCH_VIDEO_TYPE = "video";
	public static final int NUMBER_OF_RESULTS_RETURNED_PER_PAGE = 25;

}

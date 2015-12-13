package com.query.video.youtube.enums;

/**
 * All words that are not relevant for the videos
 *
 */
public enum IrrelevantWordsEnum {
	the, a, an, compilation, in, on, and, or, so, such, at, video, de, fr, en;
	
	
	public static boolean contains(String word) {

	    for (IrrelevantWordsEnum iword : IrrelevantWordsEnum.values()) {
	        if (iword.toString().equals(word)) {
	            return true;
	        }
	    }

	    return false;
	}

}

package com.query.video.youtube.utils;

import java.util.Comparator;

public class WordFrequencyTupleComparator implements Comparator<WordFrequencyTuple> {

	@Override
	public int compare(WordFrequencyTuple o1, WordFrequencyTuple o2) {
		return -1 * ((Integer) o1.getFrequency()).compareTo((Integer) o2.getFrequency());
	}

}

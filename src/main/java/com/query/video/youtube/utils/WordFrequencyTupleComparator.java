package com.query.video.youtube.utils;

import java.util.Comparator;

import com.query.video.youtube.models.WordFrequencyTuple;

public class WordFrequencyTupleComparator implements Comparator<WordFrequencyTuple> {

	@Override
	public int compare(WordFrequencyTuple o1, WordFrequencyTuple o2) {
		return ((Integer) o2.getFrequency()).compareTo((Integer) o1.getFrequency());
	}
}

package com.query.video.youtube.utils;

import java.util.Comparator;

import com.query.video.youtube.models.SortingOrder;
import com.query.video.youtube.models.WordFrequencyTuple;

public class WordFrequencyTupleComparator implements Comparator<WordFrequencyTuple> {
	
	private SortingOrder sortingOrder;
	
	public WordFrequencyTupleComparator(SortingOrder sortingOrder) {
		this.sortingOrder = sortingOrder;
	}

	@Override
	public int compare(WordFrequencyTuple o1, WordFrequencyTuple o2) {
		return sortingOrder.getOrder() * ((Integer) o1.getFrequency()).compareTo((Integer) o2.getFrequency());
	}
}

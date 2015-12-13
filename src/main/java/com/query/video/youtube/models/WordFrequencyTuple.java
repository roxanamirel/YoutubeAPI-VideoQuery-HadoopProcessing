package com.query.video.youtube.models;

public class WordFrequencyTuple {
	
	private String word;
	private int frequency;

	
	public WordFrequencyTuple(String word, int frequency) {
		this.word = word;
		this.frequency = frequency;
	}

	public String getWord() {
		return word;
	}

	public int getFrequency() {
		return frequency;
	}
}

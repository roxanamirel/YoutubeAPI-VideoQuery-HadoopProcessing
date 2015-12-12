package com.query.video.youtube.utils;

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

	public void setWord(String word) {
		this.word = word;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

}

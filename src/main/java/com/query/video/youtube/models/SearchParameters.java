package com.query.video.youtube.models;

public class SearchParameters {
	
	private String part;
	private String type;
	private String queryTerm;
	private long numberOfResults;
	private String queryFields;

	private SearchParameters(Builder builder) {
		part = builder.newPart;
		type = builder.newType;
		queryTerm = builder.newQueryTerm;
		numberOfResults = builder.newNumberOfResults;
		queryFields = builder.newQueryFields;
	}

	public String getPart() {
		return part;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getNumberOfResults() {
		return numberOfResults;
	}

	public String getQueryTerm() {
		return queryTerm;
	}

	public long getNumberOfVideos() {
		return numberOfResults;
	}

	public String getQueryFields() {
		return queryFields;
	}

	public static class Builder {
		private String newPart;
		private String newType;
		private String newQueryTerm;
		private long newNumberOfResults;
		private String newQueryFields;
		
		public Builder(String newPart) {
			this.newPart = newPart;
		}

		public Builder withType(String type) {
			this.newType = type;
			return this;
		}

		public Builder withQueryTerm(String newQueryTerm) {
			this.newQueryTerm = newQueryTerm;
			return this;
		}

		public Builder withNumberOfResults(long newNumberOfVideos) {
			this.newNumberOfResults = newNumberOfVideos;
			return this;
		}

		public Builder withQueryFields(String newQueryFields) {
			this.newQueryFields = newQueryFields;
			return this;
		}

		public SearchParameters build() {
			return new SearchParameters(this);
		}
	}
}

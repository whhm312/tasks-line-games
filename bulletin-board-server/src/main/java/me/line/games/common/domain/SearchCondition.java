package me.line.games.common.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCondition {
	private String searchType;
	private String searchText;
	private int page;
	private int row;

	public SearchCondition(String searchType, String searchText, int page, int row) {
		this.searchType = searchType;
		this.searchText = searchText;
		this.page = page;
		this.row = row;
	}

}
